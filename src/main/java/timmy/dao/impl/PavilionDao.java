package timmy.dao.impl;

import timmy.dao.AbstractDao;
import timmy.entity.custom.Pavilion;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PavilionDao extends AbstractDao<Pavilion> {
    private static String sql;

    public static void insert(Pavilion pavilion) {
        sql = "INSERT INTO PAVILION(FULL_NAME, CONTACT, EMAIL, COMMENT, COMPANY, DEPARTMENT) VALUES (?,?,?,?,?,?)";
        getJdbcTemplate().update(sql, pavilion.getFullName(), pavilion.getContact(), pavilion.getEmail(), pavilion.getComment(), pavilion.getCompany(), pavilion.getDepartment());
    }

    public List<Pavilion> getAll() {
        sql = "SELECT * FROM PAVILION";
        return getJdbcTemplate().query(sql, this::mapper);
    }
    @Override
    protected Pavilion mapper(ResultSet rs, int index) throws SQLException {
        Pavilion pavilion = new Pavilion();
        pavilion.setId(rs.getInt(1));
        pavilion.setFullName(rs.getString(2));
        pavilion.setContact(rs.getString(3));
        pavilion.setEmail(rs.getString(4));
        pavilion.setComment(rs.getString(5));
        pavilion.setCompany(rs.getString(6));
        pavilion.setDepartment(rs.getString(7));
        return pavilion;
    }

}