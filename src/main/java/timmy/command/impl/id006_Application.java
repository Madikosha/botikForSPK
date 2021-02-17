package timmy.command.impl;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import timmy.command.Command;
import timmy.entity.custom.Application;
import timmy.entity.custom.RegistrationService;
import timmy.entity.enums.WaitingType;
import timmy.entity.standart.Button;
import timmy.entity.standart.User;
import timmy.services.MailService;
import timmy.utils.Const;

import static timmy.entity.enums.WaitingType.APPLICATIONS_SET_ID;

public class id006_Application extends Command {
    private Application application = new Application();
    private RegistrationService registrationService = new RegistrationService();
    private Button button = new Button();
    private User user;

    @Override
    public boolean execute() throws TelegramApiException {
        switch (waitingType) {
            case START:
                if (isButton(2026)) {
                    application.setLevelId(1);
                }
                if (isButton(8010)) {
                    application.setLevelId(2);
                }
                if (isButton(8011)) {
                    application.setLevelId(3);
                }
                if (isButton(8012)) {
                    application.setLevelId(4);
                }
                if (isButton(8016)) {
                    application.setLevelId(5);
                }
                if (isButton(9019)) {
                    application.setLevelId(6);
                }
                if (!isRegistered()) {
                    if (!registrationService.isRegistration(update, botUtils)) {
                        return COMEBACK;
                    } else {
                        userDao.insert(registrationService.getUser());
                    }
                }
                user = userDao.getUserByChatId(chatId);
                application.setFullName(user.getFullName());
                application.setPhoneNumber(user.getPhone());
                application.setEmail(user.getEmail());
                application.setCompany(user.getCompany());
                application.setDepartment(user.getDepartment());
                if (application.getLevelId() == 1) {
                    sendMessage(100);
                }
                if (application.getLevelId() == 2) {
                    sendMessage(102);
                }
                if (application.getLevelId() == 3) {
                    sendMessage(101);
                }
                if (application.getLevelId() == 4) {
                    sendMessage(101);
                }
                if (application.getLevelId() == 5) {
                    sendMessage(103);
                }
                if (application.getLevelId() == 6) {
                    sendMessage(100);
                }
                waitingType = APPLICATIONS_SET_ID;
                return COMEBACK;
            case APPLICATIONS_SET_ID:
                if (isButton(5009)) {
                    waitingType = WaitingType.APPLICATIONS_SET_COMMENT;
                    sendMessage(6007);
                }
                if (hasMessageText()) {
                    application.setRequest(updateMessageText);
                    sendMessage(6007);
                    waitingType = WaitingType.APPLICATIONS_SET_COMMENT;
                }
                return COMEBACK;
            case APPLICATIONS_SET_COMMENT:
                if (hasMessageText()) {
                    application.setComment(updateMessageText);
                    applicationDao.insert(application);
                }
                if (isButton(5009)) {
                    applicationDao.insert(application);
                }
                if (application.getLevelId() == 1) {
                    String text1 = String.format(getText(1006), application.getFullName());
                    sendMessageWithKeyboard(text1, 7);
                }
                if (application.getLevelId() == 2){
                    String text2 = String.format(getText(30002), application.getFullName());
                    sendMessageWithKeyboard(text2, 11);
                }
                if(application.getLevelId() == 6) {
                    String text6 = String.format(getText(30003), application.getFullName());
                    sendMessageWithKeyboard(text6, 12);
                }
                if (application.getLevelId() == 3) {
                    String text3 = String.format(getText(30004), application.getFullName());
                    sendMessageWithKeyboard(text3, 15);
                }
                if(application.getLevelId() == 4) {
                    String text4 = String.format(getText(30005), application.getFullName());
                    sendMessageWithKeyboard(text4, 16);
                }
                if(application.getLevelId() == 5) {
                    String text5 = String.format(getText(30006), application.getFullName());
                    sendMessageWithKeyboard(text5, 17);
                }
                MailService.sendMail("spkalmaty.bot@gmail.com", String.format(
                        "Заявление №" +application.getId()+
                         "\n"+
                        "\nПользователь: " + application.getFullName()+
                        ",\nСотовый номер:" + application.getPhoneNumber()+
                        ",\nПочта: "+ application.getEmail()+
                        ",\nКомпания: "+application.getCompany()+
                        ",\nОтрасль: "+application.getDepartment()+
                        ",\nТема: "+ button.getName()+
                        ",\nЗаявление: "+ application.getRequest() +
                        ",\nКомментарий: "+ application.getComment()) );
                return EXIT;
        }
        return EXIT;
    }
}
