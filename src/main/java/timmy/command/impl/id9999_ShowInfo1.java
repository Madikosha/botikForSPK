package timmy.command.impl;

import org.apache.commons.logging.Log;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import timmy.command.Command;
import timmy.entity.custom.RegistrationService;

public class id9999_ShowInfo1 extends Command {
    private RegistrationService registrationService = new RegistrationService();

    private Log log;

    @Override
    public boolean execute() throws TelegramApiException {
        if (!isRegistered()) {
            if (!registrationService.isRegistration(update, botUtils)) {
                return COMEBACK;
            } else {
                userDao.insert(registrationService.getUser());
            }
        }
        sendMessageWithAddition();
        return EXIT;
    }
}
