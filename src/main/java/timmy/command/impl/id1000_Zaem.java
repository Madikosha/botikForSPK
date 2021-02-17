package timmy.command.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import timmy.command.Command;

public class id1000_Zaem extends Command {
    @Override
    public boolean execute() throws TelegramApiException {
        sendMessageWithKeyboard(getText(2013), 17);
        bot.execute(new SendPhoto().setChatId(chatId).setPhoto("AgACAgIAAxkBAAIHEl-PwoInSvaiLICkvZx19VtCcmewAALirzEbcMaASJTCL2egjVpwFqp0ly4AAwEAAwIAA3kAAz0bAgABGwQ"));
        bot.execute(new SendPhoto().setChatId(chatId).setPhoto("AgACAgIAAxkBAAIanF-Sw80divJtPSXq5V_R_wN50r52AAIesTEb5VmZSEH6CkHjQpsBwmBQmC4AAwEAAwIAA3kAA9kXAgABGwQ"));
        return true;
    }
}
