package timmy.dao.impl;

import timmy.dao.AbstractDao;
import timmy.entity.custom.Complaint;
import timmy.entity.custom.ComplaintType;
import timmy.entity.custom.Suggestion;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ComplaintDao extends AbstractDao<Complaint> {

    public  void             insert(Complaint complaint) {
        sql = "INSERT INTO COMPLAINT (FULL_NAME, PHONE, TEXT, EMAIL, COMPANY, DEPARTMENT, CATEGORY) VALUES (?,?,?,?,?,?,?)";
        getJdbcTemplate().update(sql, setParam(complaint.getFullName(), complaint.getContact(), complaint.getText(), complaint.getEmail(), complaint.getCompany(), complaint.getDepartment(), complaint.getCategory()));
    }
    public List<Complaint> getAll(){
        sql = "SELECT * FROM COMPLAINT";
        return getJdbcTemplate().query(sql, this::mapper);
    }


    protected Complaint mapper(ResultSet rs, int index) throws SQLException {
        Complaint complaint = new Complaint();
        complaint.setId(rs.getInt(1));
        complaint.setFullName(rs.getString(2));
        complaint.setContact(rs.getString(3));
        complaint.setText(rs.getString(4));
        complaint.setEmail((rs.getString(5)));
        complaint.setCompany(rs.getString(6));
        complaint.setDepartment(rs.getString(7));
        complaint.setCategory(rs.getString(8));
        return complaint;
    }
    protected ComplaintType mappers(ResultSet rs, int index) throws SQLException {
        ComplaintType entity = new ComplaintType();
        entity.setId(rs.getInt(1));
        entity.setName(rs.getString(2));
        return entity;
    }
}