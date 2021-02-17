package timmy.command.impl;

import timmy.command.Command;
import timmy.entity.enums.Language;
import timmy.services.LanguageService;
import timmy.utils.Const;
import timmy.entity.custom.RegistrationService;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class id002_SelectionLanguage extends Command {
    private RegistrationService registrationService = new RegistrationService();
    @Override
    public boolean execute() throws TelegramApiException {
//        deleteMessage(updateMessageId);
        chosenLanguage();
//        if (!isRegistered()) {
//            if (!registrationService.isRegistration(update, botUtils)) {
//                return COMEBACK;
//            } else {
//                userDao.insert(registrationService.getUser());
//            }
//        }
//            sendMessage(Const.WELCOME_TEXT_WHEN_START);
            sendMessage(Const.WELCOME_TEXT_WHEN_START);
        return EXIT;
    }

    private void chosenLanguage() {
        if (isButton(Const.RU_LANGUAGE)) {
            LanguageService.setLanguage(chatId, Language.ru);
        }
        if (isButton(Const.KZ_LANGUAGE)) {
            LanguageService.setLanguage(chatId, Language.kz);
        }
    }
}

