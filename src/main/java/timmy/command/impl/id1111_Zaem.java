package timmy.command.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import timmy.command.Command;

public class id1111_Zaem extends Command {
    @Override
    public boolean execute() throws TelegramApiException {
        sendMessageWithKeyboard(getText(2007), 11);
        bot.execute(new SendPhoto().setChatId(chatId).setPhoto("AgACAgIAAxkBAAIGh1-Pv0fgio22_vjRECPmkIu654hcAALfrzEbcMaASLtUDQ9c681b6iXqly4AAwEAAwIAA3kAA8IHAgABGwQ"));
        bot.execute(new SendPhoto().setChatId(chatId).setPhoto("AgACAgIAAxkBAAIa0F-SxXSLOmbvSXSXdgHtzEVPynQAAySxMRvlWZlIT-1RmUKjNoc_fNeWLgADAQADAgADeQADW8cCAAEbBA"));
        return true;
    }
}
