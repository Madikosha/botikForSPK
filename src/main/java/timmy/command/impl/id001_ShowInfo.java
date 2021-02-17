package timmy.command.impl;

import org.apache.commons.logging.Log;
import timmy.command.Command;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class id001_ShowInfo extends Command {

    private Log log;

    @Override
    public boolean execute() throws TelegramApiException {
        sendMessageWithAddition();
        return EXIT;
    }


    }
