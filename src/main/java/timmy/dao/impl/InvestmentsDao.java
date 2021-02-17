package timmy.dao.impl;

import timmy.dao.AbstractDao;
import timmy.entity.custom.Investments;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class InvestmentsDao extends AbstractDao<Investments> {

    private static String sql;

    public static void insert(Investments investments) {
        sql = "INSERT INTO INVESTMENTS(FULL_NAME, CONTACT, COMMENT, EMAIL, COMPANY,DEPARTMENT, TEXT) VALUES (?,?,?,?,?,?,?)";
        getJdbcTemplate().update(sql, investments.getFullName(), investments.getContact(), investments.getComment(), investments.getEmail(), investments.getCompany(), investments.getDepartment(), investments.getText());
    }

    public List<Investments> getAll() {
        sql = "SELECT * FROM INVESTMENTS";
        return getJdbcTemplate().query(sql, this::mapper);
    }

    public int          count() {
        sql = "SELECT count(ID) FROM INVESTMENTS";
        return getJdbcTemplate().queryForObject(sql, Integer.class);
    }
    @Override
    protected Investments mapper(ResultSet rs, int index) throws SQLException {
        Investments investments = new Investments();
        investments.setId(rs.getInt(1));
        investments.setFullName(rs.getString(2));
        investments.setContact(rs.getString(3));
        investments.setComment(rs.getString(4));
        investments.setEmail(rs.getString(5));
        investments.setCompany(rs.getString(6));
        investments.setDepartment(rs.getString(7));
        investments.setText(rs.getString(8));
        return investments;
    }
}