package timmy.dao.impl;

import timmy.dao.AbstractDao;
import timmy.entity.custom.Application;
import timmy.entity.custom.Operators;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OperatorsDao extends AbstractDao<Operators> {

    private static String sql;

    public void  insert(Operators operators) {
        sql = "INSERT INTO OPERATORS(FULL_NAME, CONTACT, QUESTION, EMAIL, COMPANY,  DEPARTMENT, LEVEL_ID) VALUES (?,?,?,?,?,?,?)";
        getJdbcTemplate().update(sql, setParam(operators.getFullName(), operators.getPhoneNumber(),operators.getQuestion(), operators.getEmail(), operators.getCompany(), operators.getDepartment(), operators.getLevelId()));
    }
    public List<Operators> getAll(){
        sql = "SELECT * FROM OPERATORS";
        return getJdbcTemplate().query(sql, this::mapper);
    }


    @Override
    protected Operators mapper(ResultSet rs, int index) throws SQLException {
        Operators operators = new Operators();
        operators.setId(rs.getInt(1));
        operators.setFullName(rs.getString(2));
        operators.setPhoneNumber(rs.getString(3));
        operators.setEmail(rs.getString(4));
        operators.setQuestion(rs.getString(5));
        operators.setCompany(rs.getString(6));
        operators.setDepartment(rs.getString(7));
        operators.setLevelId(rs.getInt(8));
        return operators;
    }
}