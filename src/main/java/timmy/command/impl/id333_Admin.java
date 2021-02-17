package timmy.command.impl;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import timmy.command.Command;
import timmy.entity.standart.User;
import timmy.utils.Const;
import timmy.utils.DateUtil;

import java.util.Date;
import java.util.List;
@Slf4j
public class id333_Admin extends Command {
    private StringBuilder text;
    private List<Long>    allAdmins;
    private int           message;
    private static String delete;
    private static String deleteIcon;
    private static String showIcon;

    @Override
    public boolean execute() throws TelegramApiException {
        if (!isAdmin() && !isMainAdmin()) {
            sendMessage(Const.NO_ACCESS);
            return EXIT;
        }
        if (deleteIcon == null) {
            deleteIcon = getText(1051);
            showIcon = getText(1052);
            delete = getText(1053);
        }
        if (message != 0) deleteMessage(message);
        if (hasContact()) {
            registerNewAdmin();
            return COMEBACK;
        }
        if(updateMessageText.contains(delete)) {
            try {
                if (allAdmins.size() > 1) {
                    int numberAdminList = Integer.parseInt(updateMessageText.replaceAll("[^0-9]",""));
                    adminDao.delete(allAdmins.get(numberAdminList));
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        sendEditorAdmin();
        return COMEBACK;
    }

    private boolean registerNewAdmin() throws TelegramApiException {
        long newAdminChatId = update.getMessage().getContact().getUserID();
        if (!userDao.isRegistered(newAdminChatId)) {
            sendMessage("Пользователь не зарегистрирован в данном боте");
            return EXIT;
        } else {
            if (adminDao.isAdmin(newAdminChatId)) {
                sendMessage("Пользователь уже администратор");
                return EXIT;
            } else {
                User user = userDao.getUserByChatId(newAdminChatId);
                adminDao.addAssistant(newAdminChatId, String.format("%s %s %s", user.getUserName(), user.getPhone(), DateUtil.getDbMmYyyyHhMmSs(new Date())));
                User userAdmin = userDao.getUserByChatId(chatId);
                log.info("{} added new admin - {} ", getInfoByUser(userAdmin), getInfoByUser(user));
                sendEditorAdmin();
            }
        }
        return COMEBACK;
    }

    private String getInfoByUser(User user) {
        return String.format("%s %s %s", user.getFullName(), user.getPhone(), user.getChatId());
    }

    private void sendEditorAdmin() throws TelegramApiException {
        deleteMessage(updateMessageId);
        try {
            getText(true);
            message = sendMessage(String.format(getText(1054), text.toString()));
        } catch (TelegramApiException e) {
            getText(false);
            message = sendMessage(String.format(getText(1054), text.toString()));
        }
        toDeleteMessage(message);
    }

    private void getText(boolean withLink) {
        text = new StringBuilder();
        allAdmins = adminDao.getAll();
        int count = 0;
        for (Long admin : allAdmins) {
            try {
                User user = userDao.getUserByChatId(admin);
                if (allAdmins.size() == 1) {
                    if (withLink) {
                        text.append(getLinkForUser(user.getChatId(), user.getUserName())).append(space).append(next);
                    } else {
                        text.append(getInfoByUser(user)).append(space).append(next);
                    }
                    text.append("Должен быть минимум 1 администратор.").append(next);
                } else {
                    if (withLink) {
                        text.append(delete).append(count).append(deleteIcon).append(" - ").append(showIcon).append(getLinkForUser(user.getChatId(), user.getUserName())).append(space).append(next);
                    } else {
                        text.append(delete).append(count).append(deleteIcon).append(" - ").append(getInfoByUser(user)).append(space).append(next);
                    }
                }
                count++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
