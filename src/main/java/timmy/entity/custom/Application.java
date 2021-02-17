package timmy.entity.custom;
import lombok.Data;
import java.util.Date;

@Data
public class Application {
    private int id;
    private String fullName;
    private String phoneNumber;
    private String request;
    private String comment;
    private String email;
    private String company;
    private String department;
    private int levelId;
}