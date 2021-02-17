package timmy.dao.impl;

import timmy.dao.AbstractDao;
import timmy.entity.custom.Quest;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class QuestDao extends AbstractDao<Quest> {

    public int getId() {
        sql = "SELECT MAX(ID) FROM QUEST_TABLE";
        return getJdbcTemplate().queryForObject(sql, Integer.class);
    }

    public void         insert(Quest quest) {
        sql = "INSERT INTO QUEST_TABLE (ID, NAME, TEXT, LANG_ID) VALUES (?,?,?,?)";
        getJdbcTemplate().update(sql, quest.getId(), quest.getName(), quest.getText(), quest.getLangId());
    }

    public Quest getById(long id){
        sql = "SELECT * FROM QUEST_TABLE WHERE ID = ?";
        return getJdbcTemplate().queryForObject(sql, setParam(id), this::mapper);
    }

    public List<Quest> getAll(){
        sql = "SELECT * FROM QUEST_TABLE WHERE LANG_ID = ? ";
        return getJdbcTemplate().query(sql,setParam(getLanguage().getId()), this::mapper);
    }
    public void update(Quest quest) {
        sql = "UPDATE QUEST_TABLE SET NAME = ? WHERE ID = ?";
        getJdbcTemplate().update(sql, quest.getName(), quest.getId());
    }
    public void updateText(Quest quest) {
        sql = "UPDATE QUEST_TABLE SET TEXT = ? WHERE ID = ? AND LANG_ID = ?";
        getJdbcTemplate().update(sql, quest.getText(), quest.getId(), quest.getLangId());
    }
    public void delete(int questId) {
        sql = "DELETE FROM QUEST_TABLE WHERE ID = ?";
        getJdbcTemplate().update(sql, questId);
    }

    @Override
    protected Quest mapper(ResultSet rs, int index) throws SQLException {
        Quest newQuest = new Quest();
        newQuest.setId(rs.getInt(1));
        newQuest.setName(rs.getString(2));
        newQuest.setText(rs.getString(3));
        newQuest.setLangId(rs.getInt(4));
        return newQuest;
    }
}
