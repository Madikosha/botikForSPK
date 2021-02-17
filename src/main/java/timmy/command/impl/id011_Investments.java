package timmy.command.impl;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import timmy.command.Command;
import timmy.dao.impl.InvestmentsDao;
import timmy.entity.custom.Investments;
import timmy.entity.custom.RegistrationService;
import timmy.entity.enums.WaitingType;
import timmy.entity.standart.User;
import timmy.utils.Const;

import static timmy.entity.enums.WaitingType.*;

public class id011_Investments extends Command {


    private WaitingType waitingType = WaitingType.START;
    private RegistrationService registrationService = new RegistrationService();
    private Investments investments = new Investments();
    private User user;
    @Override
    public boolean execute() throws TelegramApiException {
        if (!isRegistered()) {
            if (!registrationService.isRegistration(update, botUtils)) {
                waitingType = WaitingType.START;
                return COMEBACK;
            } else {
                userDao.insert(registrationService.getUser());
                waitingType = WaitingType.START;
            }
        }
        switch (waitingType) {
            case START:
                user = userDao.getUserByChatId(chatId);
                investments.setFullName(user.getFullName());
                investments.setContact(user.getPhone());
                investments.setEmail(user.getEmail());
                investments.setCompany(user.getCompany());
                investments.setDepartment(user.getDepartment());
                getZae();
                getDirection();
                waitingType = ZAE;
                return COMEBACK;
            case ZAE:
                investments.setText(updateMessageText);
                getComment();
                waitingType = WaitingType.SET_COMMENT;
                return COMEBACK;
            case SET_COMMENT:
                if (isButton(5009)){
                    String text = String.format(getText(6067), investments.getFullName());
                    sendMessageWithKeyboard(text, 5);
                    InvestmentsDao.insert(investments);
        }else {
                    investments.setComment(updateMessageText);
                    InvestmentsDao.insert(investments);
                    String text = String.format(getText(6067), investments.getFullName());
                    sendMessageWithKeyboard(text, 5);
                }
                    return EXIT;

        }
        return COMEBACK;
    }

    private int getComment() throws TelegramApiException {
        return sendMessage(602,chatId);
    }
    private int wrongData() throws TelegramApiException {
        return sendMessage(Const.WRONG_DATA_TEXT, chatId);
    }
    private int getZae() throws TelegramApiException {
        return sendMessage(1009, chatId);
    }
    private int getDirection() throws TelegramApiException {
        return sendMessage(6030, chatId);
    }

    public Investments getOperators() {
        return investments; }

}