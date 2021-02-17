package timmy.entity.custom;

import lombok.Data;

import java.util.Date;

@Data
public class Operators  {
    private int id;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String company;
    private String department;
    private String question;
    private int levelId;
}