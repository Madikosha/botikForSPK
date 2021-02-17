package timmy.utils.reminder;

import lombok.extern.slf4j.Slf4j;
import timmy.config.Bot;
import timmy.utils.reminder.SendDoc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Timer;

@Slf4j
public class Reminder {

    private Bot bot;
    private Timer   timer = new Timer(true);

    public Reminder(Bot bot) {
        this.bot = bot;
        sendDoc(LocalDate.now().atTime(16,46));
    }

    public void sendDoc(LocalDateTime lDT) {
        log.info("Next check db task set to " + lDT.toString());
        SendDoc sendDoc = new SendDoc(this);
        timer.schedule(sendDoc, Date.from(lDT.atZone(ZoneId.systemDefault()).toInstant()));
    }
}