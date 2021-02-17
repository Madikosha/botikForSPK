package timmy.dao.impl;

import timmy.dao.AbstractDao;
import timmy.entity.custom.Application;
import timmy.entity.custom.Suggestion;
import timmy.utils.Const;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class SuggestionDao extends AbstractDao<Suggestion> {

    public void             insert(Suggestion suggestion) {
        sql = "INSERT INTO SUGGESTION (FULL_NAME, COMPANY, PHONE, EMAIL, COMMENT, FILE, DEPARTMENT,DESCRIPTION) VALUES (?,?,?,?,?,?,?,?)";
        getJdbcTemplate().update(sql, setParam(
                suggestion.getFullName(), suggestion.getCompany(), suggestion.getPhoneNumber(), suggestion.getEmail(), suggestion.getComment(), suggestion.getFile(), suggestion.getDepartment(),suggestion.getDescription()));
    }
    public List<Suggestion> getAll(){
        sql = "SELECT * FROM SUGGESTION";
        return getJdbcTemplate().query(sql, this::mapper);
    }


    public int              getCount() {
        sql = "SELECT COUNT(ID) FROM " + Const.TABLE_NAME + ".SUGGESTION";
        return getJdbcTemplate().queryForObject(sql, Integer.class);
    }

    public int              getCountComplaint() {
        sql = "SELECT COUNT(ID) FROM " + Const.TABLE_NAME + ".COMPLAINT";
        return getJdbcTemplate().queryForObject(sql, Integer.class);
    }

    public List<Suggestion> getSuggestionsByTime(Date dateBegin, Date deadline) {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".SUGGESTION WHERE POST_DATE BETWEEN ? AND ? ORDER BY ID";
        return getJdbcTemplate().query(sql, setParam(dateBegin, deadline), this::mapper);
    }

    public List<Suggestion> getComplaintsByTime(Date dateBegin, Date deadline) {
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".COMPLAINT WHERE POST_DATE BETWEEN to_date(?, 'YYYY-MM-DD') AND to_date(?, 'YYYY-MM-DD') ORDER BY ID";
        return getJdbcTemplate().query(sql, setParam(dateBegin, deadline), this::mapper);
    }

    public List<Suggestion> view(){
        sql = "SELECT * FROM " + Const.TABLE_NAME + ".SUGGESTION";
        return getJdbcTemplate().query(sql, this::mapper);
    }

    @Override
    protected Suggestion    mapper(ResultSet rs, int index) throws SQLException {
        Suggestion entity = new Suggestion();
        entity.setId(rs.getInt(1));
        entity.setFullName(rs.getString(2));
        entity.setCompany(rs.getString(3));
        entity.setEmail(rs.getString(4));
        entity.setPhoneNumber(rs.getString(5));
        entity.setComment(rs.getString(6));
        entity.setFile(rs.getString(7));
        entity.setDepartment(rs.getString(8));
        entity.setDescription(rs.getString(9));
        return entity;
    }
}