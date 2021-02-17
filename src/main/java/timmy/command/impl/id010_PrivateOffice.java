package timmy.command.impl;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import timmy.command.Command;
import timmy.entity.custom.RegistrationService;
import timmy.entity.enums.WaitingType;
import timmy.entity.standart.User;


public class id010_PrivateOffice extends Command {
    private User user;
    private int deleteMessageId,first, second, third, fourth;
    private RegistrationService registrationService = new RegistrationService();


    public boolean execute() throws TelegramApiException {
        if (!isRegistered()) {
            if (!registrationService.isRegistration(update, botUtils)) {
                return COMEBACK;
            } else {
                userDao.insert(registrationService.getUser());
            }
        }
        switch (waitingType) {
            case START:
                sendMessage(906090);
                sendInfo();
                waitingType = WaitingType.SET_COMPANY_BRANCH;
                return COMEBACK;
            case SET_COMPANY_BRANCH:
                deleteMessage(deleteMessageId);
                deleteMessage(first);
                deleteMessage(second);
                deleteMessage(third);
                deleteMessage(fourth);
                if (isButton(5001)) {
                    sendMessage(5011);
                    waitingType = WaitingType.SET_FULL_NAME;
                } else if (isButton(5002)) {
                    sendMessage(5012);
                    waitingType = WaitingType.SET_PHONE;
                } else if (isButton(5003)) {
                    sendMessage(5013);
                    waitingType = WaitingType.SET_EMAIL;
                } else if (isButton(5004)) {
                    sendMessage(5014);
                    waitingType = WaitingType.SET_COMPANY;
                } else if (isButton(5005)) {
                    sendMessage(5015);
                    waitingType = WaitingType.SET_DEPARTMENT;
                }
                return COMEBACK;
            case SET_FULL_NAME:
                deleteMessage(updateMessageId);
                user.setFullName(updateMessageText);
                userDao.update(user);
                sendInfo();
                waitingType = WaitingType.SET_COMPANY_BRANCH;
                return COMEBACK;
            case SET_PHONE:
                deleteMessage(updateMessageId);
                user.setPhone(updateMessageText);
                userDao.update(user);
                sendInfo();
                waitingType = WaitingType.SET_COMPANY_BRANCH;
                return COMEBACK;
            case SET_EMAIL:
                deleteMessage(updateMessageId);
                user.setEmail(updateMessageText);
                userDao.update(user);
                sendInfo();
                waitingType = WaitingType.SET_COMPANY_BRANCH;
                return COMEBACK;
            case SET_COMPANY:
                deleteMessage(updateMessageId);
                user.setCompany(updateMessageText);
                userDao.update(user);
                sendInfo();
                waitingType = WaitingType.SET_COMPANY_BRANCH;
                return COMEBACK;
            case SET_DEPARTMENT:
                deleteMessage(updateMessageId);
                user.setDepartment(updateMessageText);
                userDao.update(user);
                sendInfo();
                waitingType = WaitingType.SET_COMPANY_BRANCH;
                return COMEBACK;
        }
        return EXIT;
    }

    private void sendInfo() throws TelegramApiException {
        deleteMessage(updateMessageId);
        user = userDao.getUserByChatId(chatId);
        String fullName = String.format(getText(5001), user.getFullName());
        deleteMessageId = sendMessageWithKeyboard(fullName, 51);
        String phone = String.format(getText(5002),user.getPhone());
        first = sendMessageWithKeyboard(phone,52);
        String email = String.format(getText(5003),user.getEmail());
        second = sendMessageWithKeyboard(email,53);
        String company = String.format(getText(5004),user.getCompany());
        third = sendMessageWithKeyboard(company,54);
        String department = String.format(getText(5005),user.getDepartment());
        fourth = sendMessageWithKeyboard(department,55);
    }
}


