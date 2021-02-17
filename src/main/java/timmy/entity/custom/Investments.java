package timmy.entity.custom;

import lombok.Data;

import java.util.Date;

@Data
public class Investments {
    private int id;
    private String fullName;
    private String contact;
    private String email;
    private String company;
    private String department;
    private String comment;
    private String text;

}