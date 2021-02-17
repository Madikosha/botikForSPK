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

public class id555_Quest extends Command {
    private RegistrationService registrationService = new RegistrationService();
    private Question question = new Question();
    private User user;
    private List<Quest> questList;
    private ButtonsLeaf buttonsLeaf;
    private List<String> list;
    private Quest quest;
    private String name;
    private boolean updateKostil;

    @Override
    public boolean execute() throws TelegramApiException {
        switch (waitingType){
            case START:
                sendInfo();
                waitingType = WaitingType.CHOOSE_QUESTION;
                return COMEBACK;
            case CHOOSE_QUESTION:
                if (hasCallbackQuery()) {
                    if (!updateKostil) {
                        name = list.get(Integer.parseInt(updateMessageText));
                        if(!buttonDao.getButtonText(2035).equals(name)) {
                            quest = questList.get(Integer.parseInt(updateMessageText));
                        }
                        updateKostil = true;
                    }
                    if (!isRegistered()) {
                        if (!registrationService.isRegistration(update, botUtils)) {
                            return COMEBACK;
                        } else {
                            User user = registrationService.getUser();
                            userDao.insert(user);
                            question.setFullName(user.getFullName());
                            question.setContact(user.getPhone());
                            question.setEmail(user.getEmail());
                            question.setCompany(user.getCompany());
                            question.setDepartment(user.getDepartment());

                            if(buttonDao.getButtonText(2035).equals(name)) {
                                sendMessage(5008);
                                waitingType = WaitingType.SET_COMPLAINT;
                                return COMEBACK;
                            } else {
//                            Quest quest = questList.get(Integer.parseInt(updateMessageText));
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append(quest.getName()).append(next);
                                stringBuilder.append(quest.getText());
                                toDeleteKeyboard(sendMessageWithKeyboard(stringBuilder.toString(), 43));
                                waitingType = WaitingType.BACK;
                                return COMEBACK;
                            }
                        }
                    } else {
                        if(buttonDao.getButtonText(2035).equals(name)) {
                            sendMessage(5008);
                            waitingType = WaitingType.SET_COMPLAINT;
                            return COMEBACK;
                        } else {
                            Quest quest = questList.get(Integer.parseInt(updateMessageText));
                            user = userDao.getUserByChatId(chatId);
                            question.setFullName(user.getFullName());
                            question.setContact(user.getPhone());
                            question.setEmail(user.getEmail());
                            question.setCompany(user.getCompany());
                            question.setDepartment(user.getDepartment());
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(quest.getName()).append(next);
                            stringBuilder.append(quest.getText());
                            toDeleteKeyboard(sendMessageWithKeyboard(stringBuilder.toString(), 43));
                            waitingType = WaitingType.BACK;
                            return COMEBACK;
                        }
                    }
                }
                if (!isRegistered()) {
                    if (!registrationService.isRegistration(update, botUtils)) {
                        return COMEBACK;
                    } else {
                        User user = registrationService.getUser();
                        userDao.insert(user);
                        question.setFullName(user.getFullName());
                        question.setContact(user.getPhone());
                        question.setEmail(user.getEmail());
                        question.setCompany(user.getCompany());
                        question.setDepartment(user.getDepartment());

                        if(buttonDao.getButtonText(2035).equals(name)) {
                            sendMessage(5008);
                            waitingType = WaitingType.SET_COMPLAINT;
                            return COMEBACK;
                        } else {
//                            Quest quest = questList.get(Integer.parseInt(updateMessageText));
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(quest.getName()).append(next);
                            stringBuilder.append(quest.getText());
                            toDeleteKeyboard(sendMessageWithKeyboard(stringBuilder.toString(), 43));
                            waitingType = WaitingType.BACK;
                            return COMEBACK;
                        }
                    }
                }


//                    if(buttonDao.getButtonText(2035).equals(list.get(Integer.parseInt(updateMessageText)))) {
//                        sendMessage(5008);
//                        waitingType = WaitingType.SET_COMPLAINT;
//                        return COMEBACK;
//                    } else {
//                        Quest quest = questList.get(Integer.parseInt(updateMessageText));
//                        StringBuilder stringBuilder = new StringBuilder();
//                        stringBuilder.append(quest.getName()).append(next);
//                        stringBuilder.append(quest.getText());
//                        toDeleteKeyboard(sendMessageWithKeyboard(stringBuilder.toString(), 43));
//                        waitingType = WaitingType.BACK;
//                        return COMEBACK;
//                    }
//                }
                return COMEBACK;
            case BACK:
                //deleteMessage(updateMessageId);
                if(isButton(6025)) {
                    sendMessageWithKeyboard("Список вопросов", buttonsLeaf.getListButton());
                    waitingType = WaitingType.CHOOSE_QUESTION;
                    updateKostil = false;
                    return COMEBACK;
                }
                return COMEBACK;
            case SET_COMPLAINT:
//                if (!isRegistered()) {
//                    if (!registrationService.isRegistration(update, botUtils)) {
//                        return COMEBACK;
//                    } else {
//                        userDao.insert(registrationService.getUser());
//                    }
//                }
                if (hasMessageText()) {
                    question.setQuestion(updateMessageText);
                    questionDao.insert(question);
                    String text5 = String.format(getText(Const.QUESTION_DONE_MESSAGE), question.getFullName());
                    sendMessageWithKeyboard(text5, 3);
                    return EXIT;
                } else {
                    wrongData();
                    getComplaint();
                }
                return COMEBACK;
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
