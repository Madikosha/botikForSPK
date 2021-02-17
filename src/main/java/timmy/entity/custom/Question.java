package timmy.entity.custom;

import lombok.Data;

@Data
public class Question {

    private int     id;
    private String  fullName;
    private String  email;
    private String  company;
    private String department;
    private String question;
    private String contact;
}