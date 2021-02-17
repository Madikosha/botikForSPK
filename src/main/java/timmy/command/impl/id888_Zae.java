package timmy.command.impl;

import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import timmy.command.Command;

public class id888_Zae extends Command {
    @Override
    public boolean execute() throws TelegramApiException {
        sendMessageWithKeyboard(getText(2005), 9);
        bot.execute(new SendPhoto().setChatId(chatId).setPhoto("AgACAgIAAxkBAAIZiV-StqR_Bmz2RXGyGRyzreFPLDZ_AAI8sjEb5VmJSN60goPcJoRHwBpLli4AAwEAAwIAA3kAAxlvAwABGwQ"));
        bot.execute(new SendPhoto().setChatId(chatId).setPhoto("AgACAgIAAxkBAAIGQV-PveGozC_1s0dLjpbNl7Xbbk6ZAALerzEbcMaASAPitJqL4-f_6AoglS4AAwEAAwIAA3kAA6yaBQABGwQ"));
        return true;
    }
}
