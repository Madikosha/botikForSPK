package timmy.dao.impl;

import timmy.dao.AbstractDao;
import timmy.entity.enums.Language;
import timmy.entity.standart.Button;
import timmy.exceptions.CommandNotFoundException;
import timmy.utils.Const;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ButtonDao extends AbstractDao<Button> {

    public Button getButton(String text) throws CommandNotFoundException {
        sql = "SELECT * FROM BUTTON WHERE NAME = ? AND LANG_ID = ?";
        try {
            return getJdbcTemplate().queryForObject(sql, setParam(text, getLanguage().getId()), this::mapper);
        } catch (Exception e) {
            if (e.getMessage().contains("Incorrect result size: expected 1, actual 0")) {
                throw new CommandNotFoundException(e);
            }
            throw e;
        }
    }

    public Button getButton(int id) {
        sql = "SELECT * FROM BUTTON WHERE ID = ? AND LANG_ID = ?";
        return getJdbcTemplate().queryForObject(sql, setParam(id, getLanguage().getId()), this::mapper);
    }

    public Button getButton(int id, Language language) {
        sql = "SELECT * FROM BUTTON WHERE ID = ? AND LANG_ID = ?";
        return getJdbcTemplate().queryForObject(sql, setParam(id, language.getId()), this::mapper);
    }

    public String getButtonText(int id) {
        sql = "SELECT NAME FROM BUTTON WHERE ID = ? AND LANG_ID = ?";
        return getJdbcTemplate().queryForObject(sql, setParam(id, getLanguage().getId()), String.class);
    }

    public boolean isExist(String text, Language language) {
        sql = "SELECT count(*) FROM BUTTON WHERE NAME = ? AND LANG_ID = ?";
        return getJdbcTemplate().queryForObject(sql, setParam(text, language.getId()), Integer.class) > 0;
    }

    public void update(Button button) {
        sql = "UPDATE BUTTON SET NAME = ?, URL = ? WHERE ID = ? AND LANG_ID = ?";
        getJdbcTemplate().update(sql, button.getName(), button.getUrl(), button.getId(), button.getLangId().getId());
    }

    @Override
    protected Button mapper(ResultSet rs, int index) throws SQLException {
        Button button = new Button();
        button.setId(rs.getInt(1));
        button.setName(rs.getString(2));
        button.setCommandId(rs.getInt(3));
        button.setUrl(rs.getString(7));
        button.setRequestContact(rs.getBoolean(4));
        button.setMessageId(rs.getInt(5));
        button.setLangId(Language.getById(rs.getInt(6)));
        return button;
    }
}
