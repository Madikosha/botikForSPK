package timmy.command.impl;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import timmy.command.Command;
import timmy.entity.custom.RegistrationService;
import timmy.entity.custom.Suggestion;
import timmy.entity.enums.WaitingType;
import timmy.entity.standart.User;
import timmy.utils.Const;

import static timmy.entity.enums.WaitingType.SET_DESCRIPTION;
import static timmy.entity.enums.WaitingType.SET_FILE;


public class id007_Suggestion extends Command {
    private RegistrationService registrationService = new RegistrationService();
    private Suggestion suggestion = new Suggestion();
    private User user;
    private WaitingType waitingType =  WaitingType.START;
    @Override
    public boolean execute()       throws TelegramApiException {
        if (!isRegistered()) {
            if (!registrationService.isRegistration(update, botUtils)) {
                return COMEBACK;
            } else {
                userDao.insert(registrationService.getUser());
            }
        }
        switch (waitingType) {
            case START:
                user = userDao.getUserByChatId(chatId);
                suggestion.setFullName(user.getFullName());
                suggestion.setPhoneNumber(user.getPhone());
                suggestion.setEmail(user.getEmail());
                suggestion.setCompany(user.getCompany());
                suggestion.setDepartment(user.getDepartment());
                getFile();
                getDescription();
                waitingType = SET_DESCRIPTION;
                return COMEBACK;
            case SET_DESCRIPTION:
                suggestion.setDescription(updateMessageText);
                getPresentation();
                waitingType = SET_FILE;
                return COMEBACK;
            case SET_FILE:
                if (isButton(5009)){
                    waitingType = WaitingType.SET_COMMENT;
                    getComment();
                }
                else if (hasDocument()) {
                    suggestion.setFile(update.getMessage().getDocument().getFileId());
                    getComment();
                    waitingType = WaitingType.SET_COMMENT;
                } else {
                    wrongData();
                    return COMEBACK;
                }
                return COMEBACK;
            case SET_COMMENT:
                if (!isRegistered()) {
                    if (!registrationService.isRegistration(update, botUtils)) {
                        return COMEBACK;
                    } else {
                        userDao.insert(registrationService.getUser());
                    }
                }
                user = userDao.getUserByChatId(chatId);
                suggestion.setFullName(user.getFullName());
                suggestion.setPhoneNumber(user.getPhone());
                suggestion.setEmail(user.getEmail());
                suggestion.setCompany(user.getCompany());
                suggestion.setDepartment(user.getDepartment());

                if (hasMessageText()) {
                    suggestion.setComment(updateMessageText);
                }
                suggestionDao.insert(suggestion);
                String text = String.format(getText((Const.SUGGESTION_DONE)), suggestion.getFullName());
                sendMessageWithKeyboard(text, 5);
                return EXIT;
        }
        return COMEBACK;
    }
    private int getPresentation() throws TelegramApiException{return botUtils.sendMessage(5026,chatId);}
    private  int getDescription() throws TelegramApiException{return botUtils.sendMessage(5025,chatId);}
    private int     wrongData()     throws TelegramApiException { return botUtils.sendMessage(Const.WRONG_DATA_TEXT, chatId); }

    private int     getFile() throws TelegramApiException { return botUtils.sendMessage(5010, chatId); }

    private int     getComment() throws TelegramApiException { return botUtils.sendMessage(6007, chatId); }

}