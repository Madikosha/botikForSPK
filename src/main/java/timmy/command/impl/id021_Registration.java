package timmy.command.impl;

import timmy.command.Command;
import timmy.services.Registration;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class id021_Registration extends Command {

    private Registration registration = new Registration();

    @Override
    public boolean execute() throws TelegramApiException {
        if (!isRegistered()) {
            if (!registration.isRegistration(update, botUtils)) {
                return COMEBACK;
            } else {
                userDao.insert(registration.getUser());
                sendMessage(4);
                return EXIT;
            }
        } else {
            sendMessage(4);
            return EXIT;
        }
    }
}