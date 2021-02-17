package timmy.command.impl;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import timmy.command.Command;
import timmy.entity.custom.Operators;
import timmy.entity.custom.RegistrationService;
import timmy.entity.enums.WaitingType;
import timmy.entity.standart.User;
import timmy.services.MailService;

public class id012_Yes extends Command {
    private RegistrationService registrationService = new RegistrationService();
    private Operators operators = new Operators();
    private User user;

    @Override
    public boolean execute() throws TelegramApiException {
        switch (waitingType) {
            case START:
                if (isButton(2015)) {
                    operators.setLevelId(1);
                }
                if (isButton(8013)) {
                    operators.setLevelId(2);
                }
                if (isButton(8014)) {
                    operators.setLevelId(3);
                }
                if (isButton(8015)) {
                    operators.setLevelId(4);
                }
                if (!isRegistered()) {
                    if (!registrationService.isRegistration(update, botUtils)) {
                        return COMEBACK;
                    } else {
                        userDao.insert(registrationService.getUser());
                    }
                }
                user = userDao.getUserByChatId(chatId);
                operators.setFullName(user.getFullName());
                operators.setPhoneNumber(user.getPhone());
                operators.setEmail(user.getEmail());
                operators.setCompany(user.getCompany());
                operators.setDepartment(user.getDepartment());
                sendMessage(5016);
                waitingType = WaitingType.SET_QUESTION;
                return COMEBACK;
            case SET_QUESTION:
                if (hasMessageText()) {
                    operators.setQuestion(updateMessageText);
                    operatorsDao.insert(operators);
                }
                if(operators.getLevelId() == 1) {
                    String text1 = String.format(getText(5017), operators.getFullName());
                    sendMessageWithKeyboard(text1, 7);
                }
                if(operators.getLevelId() == 2) {
                    String text2 = String.format(getText(30007), operators.getFullName());
                    sendMessageWithKeyboard(text2, 9);
                }
                if(operators.getLevelId() == 3) {
                    String text = String.format(getText(30008), operators.getFullName());
                    sendMessageWithKeyboard(text, 11);
                }
                if(operators.getLevelId() == 4) {
                    String text = String.format(getText(30009), operators.getFullName());
                    sendMessageWithKeyboard(text, 12);
                }
                MailService.sendMail("spkalmaty.bot@gmail.com", String.format(
                        "Консультация №" +operators.getId()+
                                "\n"+
                                "\nПользователь: " + operators.getFullName()+
                                ",\nСотовый номер:" + operators.getPhoneNumber()+
                                ",\nПочта: "+ operators.getEmail()+
                                ",\nКомпания: "+operators.getCompany()+
                                ",\nОтрасль: "+operators.getDepartment()+
                                ",\nВопрос: "+ operators.getQuestion()));
                return EXIT;
        }
        return EXIT;
    }
}

