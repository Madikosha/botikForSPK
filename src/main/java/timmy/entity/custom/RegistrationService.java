package timmy.entity.custom;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import timmy.command.Command;
import timmy.dao.DaoFactory;
import timmy.dao.impl.ButtonDao;
import timmy.dao.impl.KeyboardMarkUpDao;
import timmy.utils.Const;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import timmy.entity.enums.WaitingType;
import timmy.entity.standart.User;
import timmy.utils.BotUtil;
import timmy.utils.UpdateUtil;


import java.util.Date;

import static timmy.entity.enums.WaitingType.*;

@Data
public class RegistrationService {

    private int     id;
    private long    chatId;
    private long    iin;
    private int     serviceTypeId;
    private int     serviceId;
    private Date    registrationDate;
    private boolean isCome;
    private               String               updateMessageText;
    private static DaoFactory factory                 = DaoFactory.getInstance();
    private static ButtonDao buttonDao               = factory.getButtonDao();
    private boolean COMEBACK = false;
    private boolean EXIT = true;
    private WaitingType  waitingType = WaitingType.START;
    private BotUtil botUtil =  new BotUtil();
    private User user = new User();

    public  boolean isRegistration(Update update, BotUtil botUtil) throws TelegramApiException {
        if (botUtil == null || chatId == 0) {
            chatId = UpdateUtil.getChatId(update);
            this.botUtil = botUtil;
        }
        switch (waitingType) {
            case START:
                user.setChatId(chatId);
                getName();
                waitingType = SET_FULL_NAME;
                return COMEBACK;
            case SET_FULL_NAME:
                if (update.hasCallbackQuery()) {
                    return EXIT;
                }
                if (update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().length() <= 50) {
                    user.setFullName(update.getMessage().getText());
                    getPhone();
                    waitingType = SET_PHONE_NUMBER;
                } else {
                    wrongData();
                    getName();
                }
                return COMEBACK;
            case SET_PHONE_NUMBER:
                if (botUtil.hasContactOwner(update)) {
                    user.setPhone(update.getMessage().getContact().getPhoneNumber());
                    user.setUserName(UpdateUtil.getFrom(update));
                    getEmail();
                    waitingType = SET_EMAIL;
                } else {
                    wrongData();
                    getPhone();
                }
                return COMEBACK;
            case SET_EMAIL:
                if (update.hasCallbackQuery()){
                    updateMessageText = update.getCallbackQuery().getData();
                    if(isButton( 5009)){
                        getCompany();
                        waitingType = WaitingType.SET_COMPANY;
                    }
                }
                if (update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().length() <= 50) {
                    user.setEmail(update.getMessage().getText());
                    getCompany();
                    waitingType = WaitingType.SET_COMPANY;
                }
                return COMEBACK;
            case SET_COMPANY:
                if (update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().length() <= 50) {
                    user.setCompany(update.getMessage().getText());
                    getDepartment();
                    waitingType = WaitingType.SET_DEPARTMENT;
                } else {
                    wrongData();
                    getCompany();
                }
                return COMEBACK;
            case SET_DEPARTMENT:
                if (update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().length() <= 50) {
                    user.setDepartment(update.getMessage().getText());
                    return EXIT;
                } else {
                    wrongData();
                    getDepartment();
                }
                return COMEBACK;
        }
        return EXIT;
    }

    private int getName () throws TelegramApiException {
        return botUtil.sendMessage(Const.SET_FULL_NAME_MESSAGE, chatId);
    }

    private int getPhone () throws TelegramApiException {
        return botUtil.sendMessage(Const.SEND_CONTACT_MESSAGE, chatId);
    }

    private int wrongData () throws TelegramApiException {
        return botUtil.sendMessage(Const.WRONG_DATA_TEXT, chatId);
    }
    private int getEmail ()                                          throws TelegramApiException {
        return botUtil.sendMessage(5023, chatId);
    }
    private int getCompany ()                                          throws TelegramApiException {
        return botUtil.sendMessage(5024, chatId);
    }
    private int getDepartment ()                                          throws TelegramApiException {
        return botUtil.sendMessage(5027, chatId);
    }

    public User getUser () {
        return user;
    }
    private boolean isButton(int buttonId) {
        return updateMessageText.equals(buttonDao.getButtonText(buttonId)); }

}