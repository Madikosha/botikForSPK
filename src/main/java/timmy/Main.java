package timmy;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import timmy.config.Bot;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import timmy.utils.reminder.Reminder;

import java.sql.SQLOutput;

@Slf4j
public class Main {
    private static Reminder reminder;
    private static DefaultAbsSender bot;
    public static void main(String[] args) {
        System.out.println("Hello");
//        ApiContextInitializer.init();
//        log.info("ApiContextInitializer.InitNormal()");
//        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
//        Bot bot                         = new Bot();
//        Main.bot = bot;
//        reminder                        = new Reminder(bot);
//        try {
//            telegramBotsApi.registerBot(bot);
//            log.info("Bot was registered: " + bot.getBotUsername());
//        } catch (TelegramApiRequestException e) {
//            log.error("Error in main class", e);
//        }
    }
}

//    public static DefaultAbsSender getBot(){
//        return bot;
//    }
//}

