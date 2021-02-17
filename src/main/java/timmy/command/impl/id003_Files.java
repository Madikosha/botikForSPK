package timmy.command.impl;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import timmy.command.Command;
import timmy.entity.enums.WaitingType;

public class id003_Files extends Command {
    @Override
    public boolean execute() throws TelegramApiException {
        switch (waitingType) {
            case START:
                sendMessage("Photo");
                waitingType = WaitingType.SET_PHOTO;
                return COMEBACK;
            case SET_PHOTO:
                if (hasAudio()) {
                    sendMessage(update.getMessage().getAudio().getFileId());
                }
                if (hasPhoto()) {
                    sendMessage(updateMessagePhoto);
                }
                if (hasVideo()) {
                    sendMessage(update.getMessage().getVideo().getFileId());
                }

                if (hasDocument()) {
                    sendMessage(update.getMessage().getDocument().getFileId());
                }

        }
        return EXIT;
    }
}
