package timmy.command.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import timmy.command.Command;

public class id2222_Zaem extends Command {
    @Override
    public boolean execute() throws TelegramApiException {
        sendMessageWithKeyboard(getText(2008), 12);
        bot.execute(new SendPhoto().setChatId(chatId).setPhoto("AgACAgIAAxkBAAIGrl-PwG2PoxrKYLZ87AQyS5m0xuOWAALgrzEbcMaASGVtRCjJbm8twKFMli4AAwEAAwIAA3kAA2lYAwABGwQ"));
        bot.execute(new SendPhoto().setChatId(chatId).setPhoto("AgACAgIAAxkBAAIa5F-Sxl9oG1pS3-MlneiIskUf00xpAAIqsTEb5VmZSIEmAqrHEbHgMwefli4AAwEAAwIAA3kAA-EEAwABGwQ"));
        return true;
    }
}
