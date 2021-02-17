package timmy.command.impl;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import timmy.command.Command;
import timmy.entity.custom.Quest;
import timmy.entity.custom.Question;
import timmy.entity.custom.RegistrationService;
import timmy.entity.enums.WaitingType;
import timmy.entity.standart.User;
import timmy.utils.ButtonsLeaf;
import timmy.utils.Const;

import java.util.ArrayList;
import java.util.List;

public class id556_Quest extends Command {
    private Question question = new Question();
    private User user;
    private List<Quest> questList;
    private ButtonsLeaf buttonsLeaf;
    private List<String> list;
    private RegistrationService registrationService = new RegistrationService();
    private boolean updateKostil;
    private String questionText;



    @Override
    public boolean execute() throws TelegramApiException {
        switch (waitingType){
            case START:
                //deleteMessage(updateMessageId);
                sendInfo();
                waitingType = WaitingType.CHOOSE_QUESTION;
                return COMEBACK;

            case CHOOSE_QUESTION:
                //deleteMessage(updateMessageId);
                if (hasCallbackQuery()) {
                    if(buttonDao.getButtonText(2035).equals(list.get(Integer.parseInt(updateMessageText)))) {
//                        user = userDao.getUserByChatId(chatId);
//                        question.setFullName(user.getFullName());
//                        question.setContact(user.getPhone());
//                        question.setEmail(user.getEmail());
//                        question.setCompany(user.getCompany());
//                        question.setDepartment(user.getDepartment());
                        sendMessage(5008);
                        waitingType = WaitingType.SET_COMPLAINT;
                        return COMEBACK;
                    } else {
                        Quest quest = questList.get(Integer.parseInt(updateMessageText));
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(quest.getName()).append(next);
                        stringBuilder.append(quest.getText());
                        toDeleteKeyboard(sendMessageWithKeyboard(stringBuilder.toString(), 43));
                        waitingType = WaitingType.BACK;
                        return COMEBACK;
                    }
                }
                return COMEBACK;
            case BACK:
                //deleteMessage(updateMessageId);
                if(isButton(6025)){
                    toDeleteKeyboard(sendMessageWithKeyboard("Список вопросов", buttonsLeaf.getListButton()));
                    updateKostil = false;
                    waitingType = WaitingType.CHOOSE_QUESTION;
                    return COMEBACK;
                }
                return COMEBACK;
            case SET_COMPLAINT:
                if(!updateKostil){
                    questionText = updateMessageText;
                    updateKostil = true;
                }
                if (!isRegistered()) {
                    if (!registrationService.isRegistration(update, botUtils)) {
                        return COMEBACK;
                    } else {
                        userDao.insert(registrationService.getUser());
                        user = userDao.getUserByChatId(chatId);
                        question.setFullName(user.getFullName());
                        question.setContact(user.getPhone());
                        question.setEmail(user.getEmail());
                        question.setCompany(user.getCompany());
                        question.setDepartment(user.getDepartment());
                        question.setQuestion(questionText);
                        questionDao.insert(question);
                        String text5 = String.format(getText(Const.QUESTION_DONE_MESSAGE), question.getFullName());
                        sendMessageWithKeyboard(text5, 3);;
                        return EXIT;
                    }
                }else{
                    question.setQuestion(questionText);
                    user = userDao.getUserByChatId(chatId);
                    question.setFullName(user.getFullName());
                    question.setContact(user.getPhone());
                    question.setEmail(user.getEmail());
                    question.setCompany(user.getCompany());
                    question.setDepartment(user.getDepartment());

                    questionDao.insert(question);
                    String text5 = String.format(getText(Const.QUESTION_DONE_MESSAGE), question.getFullName());
                    sendMessageWithKeyboard(text5, 3);
                    return EXIT;
                }
        }
        return EXIT;
    }
    private void sendInfo() throws TelegramApiException {
        questList = factory.getQuestDao().getAll();
        list = new ArrayList<>();
        questList.forEach(quest -> list.add(quest.getName()));
        list.add(buttonDao.getButtonText(2035));

        buttonsLeaf = new ButtonsLeaf(list);
        sendMessageWithKeyboard("Список вопросов", buttonsLeaf.getListButton());
        //sendMessageWithKeyboard("gghhg",44);
    }
    private int     getComplaint()  throws TelegramApiException { return botUtils.sendMessage(Const.COMPLAINT_SEND_MESSAGE, chatId); }

    private int     wrongData()     throws TelegramApiException { return botUtils.sendMessage(Const.WRONG_DATA_TEXT, chatId); }

}