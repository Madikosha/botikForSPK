package timmy.dao.impl;

import timmy.dao.AbstractDao;
import timmy.entity.custom.Question;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class QuestionDao extends AbstractDao<Question> {

    public  void insert(Question question) {
        sql = "INSERT INTO QUESTION (FULL_NAME, EMAIL, QUESTION, COMPANY, DEPARTMENT, CONTACT) VALUES (?,?,?,?,?,?)";
        getJdbcTemplate().update(sql, setParam(question.getFullName(), question.getEmail(), question.getQuestion(), question.getCompany(), question.getDepartment(),question.getContact()));
    }

    public List<Question> getAll(){
        sql = "SELECT * FROM QUESTION";
        return getJdbcTemplate().query(sql, this::mapper);
    }

    protected Question mapper(ResultSet rs, int index) throws SQLException {
        Question question = new Question();
        question.setId(rs.getInt(1));
        question.setFullName(rs.getString(2));
        question.setQuestion(rs.getString(3));
        question.setEmail(rs.getString(4));
        question.setCompany(rs.getString(5));
        question.setDepartment(rs.getString(6));
        question.setContact(rs.getString(7));

        return question;
    }
}