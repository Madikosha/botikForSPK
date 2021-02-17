package timmy.entity.custom;

import lombok.Data;

import java.util.Date;
@Data
public class Suggestion {
    private int    id;
    private String fullName;
    private String company;
    private String phoneNumber;
    private String email;
    private String file;
    private String comment;
    private String department;
    private String description;
}