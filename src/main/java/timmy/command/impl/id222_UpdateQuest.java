package timmy.command.impl;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import timmy.command.Command;
import timmy.entity.custom.Quest;
import timmy.entity.enums.WaitingType;
import timmy.utils.ButtonsLeaf;
import timmy.utils.Const;

import java.util.List;
@Slf4j
public class id222_UpdateQuest extends Command {

    private List<Quest> questList;
    private Quest quest;
    private int              deleteMessageId;
    private ButtonsLeaf buttonsLeaf;
    private List<String> list;
    private Quest newQuest;
    private int id;

    @Override
    public boolean execute() throws TelegramApiException {

        if (!isAdmin() && !isMainAdmin()) {
            sendMessage(Const.NO_ACCESS);
            return EXIT;
        }
        switch(waitingType){
            case START:
                sendListCategory();
                waitingType = WaitingType.CHOOSE_QUESTION;
                return COMEBACK;
            case CHOOSE_QUESTION:
                deleteMessage(updateMessageId);
                deleteMessage(deleteMessageId);
                if (hasMessageText()) {
                    if (isCommand("/new")) {
                        sendMessage("Введите название для нового вопрос(рус)");
                        waitingType = WaitingType.NEW_QUEST;
                        return COMEBACK;
                    } else if (isCommand("/st")) {
                        quest = questList.get(getInt());
                        sendQuestInfo();
                        waitingType = WaitingType.EDITION;
                    }
                }
                return COMEBACK;
            case NEW_QUEST:
                deleteMessage(updateMessageId);
                if (hasMessageText()) {
                    newQuest = new Quest();
                    id = questDao.getId() + 1;
                    newQuest.setId(id);
                    newQuest.setName(updateMessageText);
                    newQuest.setLangId(1);
                    sendMessage("Введите ответ(рус)");
                    waitingType=waitingType.SET_TEXT;
                }
                return COMEBACK;
            case SET_TEXT:
                if (hasMessageText()) {
                    newQuest.setText(updateMessageText);
                    questDao.insert(newQuest);
                    sendMessage("Введите название для нового вопрос(каз)");
                    waitingType = WaitingType.SET_COMPLAINT;
                }
                return COMEBACK;
            case SET_COMPLAINT:
                if(hasMessageText()){
                    newQuest.setName(updateMessageText);
                    sendMessage("Введите ответ(каз)");
                    waitingType = WaitingType.SET_CONSULTATION_TYPE;
                }
                return COMEBACK;
            case SET_CONSULTATION_TYPE:
                if(hasMessageText()){
                    newQuest.setText(updateMessageText);
                    newQuest.setLangId(2);
                    questDao.insert(newQuest);
                    sendListCategory();
                    waitingType = WaitingType.CHOOSE_QUESTION;
                }
                return COMEBACK;
            case EDITION:
                deleteMessage(updateMessageId);
                deleteMessage(deleteMessageId);
                if (hasMessageText()) {
                    if (isCommand("/back")) {
                        sendListCategory();
                        waitingType = WaitingType.CHOOSE_QUESTION;
                    } else if (isCommand("/edit")) {
                        deleteMessageId = sendMessage("Введите название для вопроса:");
                        waitingType = WaitingType.UPDATE_QUEST;
                    }else if(isCommand("/answer")){
                        deleteMessageId = sendMessage("Введите ответ для вопроса:(рус)");
                        waitingType = WaitingType.UPDATE_TEXT;
                    }
                    else if (isCommand("/drop")) {
                        questDao.delete(quest.getId());
                        sendListCategory();
                        waitingType = WaitingType.CHOOSE_QUESTION;
                        return COMEBACK;
                    }
                    return COMEBACK;
                }
                return COMEBACK;
            case UPDATE_TEXT:
                if(hasMessageText()){
                    quest.setText(updateMessageText);
                    quest.setLangId(1);
                    questDao.updateText(quest);
                    sendMessage("Введите ответ для вопроса (каз)");
                    waitingType = WaitingType.UPDATE_BUTTON_LINK;
                    return COMEBACK;
                }
                return COMEBACK;
            case UPDATE_BUTTON_LINK:
                if(hasMessageText()){
                    quest.setText(updateMessageText);
                    quest.setLangId(2);
                    questDao.updateText(quest);
                    sendQuestInfo();
                    waitingType = WaitingType.EDITION;
                    return COMEBACK;
                }
                return COMEBACK;
            case UPDATE_QUEST:
                deleteMessage(updateMessageId);
                if (hasMessageText()) {
                    quest.setName(updateMessageText);
                    questDao.update(quest);
                    sendQuestInfo();
                    waitingType = WaitingType.EDITION;
                    return COMEBACK;
                }
        }
        return EXIT;
    }
    private boolean isCommand(String command) {
        return updateMessageText.startsWith(command);
    }
    private void sendListCategory() throws TelegramApiException {
        String formatMassage = getText(10000);
        StringBuilder infoByEmployee = new StringBuilder();
        questList = questDao.getAll();
        String format = getText(10001);
        for (int i = 0; i < questList.size(); i++) {
            Quest quest = questList.get(i);
            infoByEmployee.append(String.format(format, "/st" + i, quest.getName())).append(next);
        }
        deleteMessageId = sendMessage(String.format(formatMassage, infoByEmployee.toString(), "/new"));
    }
    private void sendQuestInfo() throws TelegramApiException {
        String formatMessage = getText(10510);
        deleteMessageId = sendMessage(String.format(formatMessage, quest.getName(), "/edit", "/answer", "/drop", "/back"));
    }
    private int getInt() {
        return Integer.parseInt(updateMessageText.replaceAll("[^0-9]", ""));
    }

}
