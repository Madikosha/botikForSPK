package timmy.services;

import timmy.dao.DaoFactory;
import timmy.dao.impl.ButtonDao;
import timmy.entity.enums.WaitingType;
import timmy.dao.impl.MessageDao;
import timmy.entity.standart.User;
import timmy.utils.BotUtil;
import timmy.utils.ButtonsLeaf;
import timmy.utils.Const;
import timmy.utils.UpdateUtil;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Registration {

    private User            user;
    private long            chatId;
    private WaitingType     waitingType = WaitingType.START;
    private BotUtil         botUtil;
    private List<String>    list;
    private ButtonsLeaf     buttonsLeaf;
    private DaoFactory      factory     = DaoFactory.getInstance();
    private MessageDao      messageDao  = factory.getMessageDao();
    private  ButtonDao buttonDao               = factory.getButtonDao();
    private               String               updateMessageText;

    public  boolean isRegistration(Update update, BotUtil botUtil)  throws TelegramApiException {
        if (botUtil == null || chatId == 0) {
            chatId          = UpdateUtil.getChatId(update);
            this.botUtil    = botUtil;
        }
        switch (waitingType) {
            case START:
                user        = new User();
                user.setChatId(chatId);
                getName();
                waitingType = WaitingType.SET_FULL_NAME;
                return false;
            case SET_FULL_NAME:
                if (update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().length() <= 50) {
                    user.setFullName(update.getMessage().getText());
                    getPhone();
                    waitingType = WaitingType.SET_PHONE_NUMBER;
                } else {
                    wrongData();
                    getName();
                }
                return false;
            case SET_PHONE_NUMBER:
                if (botUtil .hasContactOwner(update)) {
                    user    .setPhone(update.getMessage().getContact().getPhoneNumber());
                    user    .setUserName(UpdateUtil.getFrom(update));
                    getEmail();
                    waitingType = WaitingType.SET_EMAIL;
                } else {
                    wrongData();
                    getPhone();
                } return false;
            case SET_EMAIL:
                if (update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().length() <= 50) {
                    user.setEmail(update.getMessage().getText());
                    getCompany();
                    waitingType = WaitingType.SET_COMPANY;
                } else if(isButton(5009)) {
                    getCompany();
                    waitingType = WaitingType.SET_COMPANY;
                } return false;
            case SET_COMPANY:
                if(update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().length() <= 50) {
                    user.setCompany(update.getMessage().getText());
                    getDepartment();
                    waitingType = WaitingType.SET_DEPARTMENT;
                } else {
                    wrongData();
                    getCompany();
                }
                return false;
            case SET_DEPARTMENT:
                if(update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().length() <= 50) {
                    user.setDepartment(update.getMessage().getText());
                    return true;
                } else {
                    wrongData();
                    getDepartment();
                }
                return false;

        }
        return true;
    }

    private int     wrongData()                                     throws TelegramApiException {
        return botUtil.sendMessage(Const.WRONG_DATA_TEXT, chatId);
    }
    private int     getName()                                       throws TelegramApiException {
        return botUtil.sendMessage(Const.SET_FULL_NAME_MESSAGE, chatId);
    }

    private int     getPhone()                                      throws TelegramApiException {
        return botUtil.sendMessage(Const.SEND_CONTACT_MESSAGE, chatId);
    }

    private int getEmail()                                          throws TelegramApiException {
        return botUtil.sendMessage(5023, chatId);
    }
    private int getCompany()                                          throws TelegramApiException {
        return botUtil.sendMessage(5024, chatId);
    }
    private int getDepartment()                                          throws TelegramApiException {
        return botUtil.sendMessage(5027, chatId);
    }


//    private int     getStatus()                                     throws TelegramApiException {
//        list = new ArrayList<>();
//        Arrays.asList(getText(Const.STATUS_TYPE_MESSAGE).split(Const.SPLIT)).forEach((e) -> list.add(e));
//        list.add(getText(Const.OTHERS_MESSAGE));
//        buttonsLeaf = new ButtonsLeaf(list);
//        return botUtil.sendMessageWithKeyboard(getText(Const.STATUS_MESSAGE), buttonsLeaf.getListButton(), chatId);
//    }

    private String  getText(int messageIdFromDb) {
        return messageDao.getMessageText(messageIdFromDb);
    }

    public  User    getUser() { return user; }

    private boolean isButton(int buttonId) {
        return updateMessageText.equals(buttonDao.getButtonText(buttonId)); }
}