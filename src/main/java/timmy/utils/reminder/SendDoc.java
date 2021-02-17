package timmy.utils.reminder;

import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import timmy.Main;
import timmy.dao.DaoFactory;
import timmy.dao.impl.ReminderTaskDao;
import timmy.dao.impl.UserDao;
import timmy.entity.custom.ReminderTask;
import timmy.entity.standart.User;

import java.time.LocalDate;
import java.util.List;
import java.util.TimerTask;

@RequiredArgsConstructor
public class SendDoc extends TimerTask {
    private DaoFactory daoFactory = DaoFactory.getInstance();
    private final Reminder reminder;
    private ReminderTaskDao reminderTask = daoFactory.getReminderTaskDao();
    private UserDao userDao = daoFactory.getUserDao();
    private List<User> users;
//    private final Reminder reminder;

    @Override
    public void run() {
        LocalDate localDate     = LocalDate.now();
        reminderTask.getAll().forEach(reminderTask1 -> {
            userDao.getAll().forEach(user -> {
                try {
                    Main.getBot().execute(new SendMessage().setText(reminderTask1.getText()).setChatId(user.getChatId()));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            });
        });

        reminder.sendDoc(localDate.plusDays(1).atTime(16,45));    }
}
