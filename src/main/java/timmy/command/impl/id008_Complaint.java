package timmy.command.impl;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import timmy.command.Command;
import timmy.entity.custom.Complaint;
import timmy.entity.custom.ComplaintType;
import timmy.entity.custom.RegistrationService;
import timmy.entity.enums.WaitingType;
import timmy.entity.standart.User;
import timmy.utils.ButtonsLeaf;
import timmy.utils.Const;

import java.util.ArrayList;
import java.util.List;

public class id008_Complaint extends Command {
    private RegistrationService registrationService = new RegistrationService();
    private Complaint complaint = new Complaint();
    private User user;
    private ButtonsLeaf buttonsLeaf;
    private String category;
    @Override
    public boolean execute()       throws TelegramApiException {
        if (!isRegistered()) {
            if (!registrationService.isRegistration(update, botUtils)) {
                waitingType = WaitingType.START;
                return COMEBACK;
            } else {
                userDao.insert(registrationService.getUser());
                waitingType = WaitingType.START;
            }
        }
        switch (waitingType) {
            case START:
                category = update.getCallbackQuery().getData().replaceAll("[^a-zA-ZА-Яа-я0-9]"," ");
                System.out.println(category);
                user = userDao.getUserByChatId(chatId);
                complaint.setFullName(user.getFullName());
                complaint.setContact(user.getPhone());
                complaint.setEmail(user.getEmail());
                complaint.setCompany(user.getCompany());
                complaint.setDepartment(user.getDepartment());
                complaint.setCategory(category);
                getComplaint();
                waitingType     = WaitingType.SET_TEXT;
                return COMEBACK;
            case SET_TEXT:
                if (hasMessageText()) {
                    complaint.setText(updateMessageText);
                    complaint.setCategory(category);
                    complaintDao.insert(complaint);
                    String text = String.format(getText(Const.COMPLAINT_DONE_MESSAGE), user.getFullName());
                    sendMessage(text);
                    return EXIT;
                } else {
                    wrongData();
                    getComplaint();
                }
                return COMEBACK;
        }
        return EXIT;
    }

    private int     getComplaint()  throws TelegramApiException { return botUtils.sendMessage(600, chatId); }

    private int     wrongData()     throws TelegramApiException { return botUtils.sendMessage(Const.WRONG_DATA_TEXT, chatId); }

}