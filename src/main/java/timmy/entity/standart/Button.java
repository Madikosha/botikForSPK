package timmy.entity.standart;

import timmy.entity.enums.Language;
import lombok.Data;

@Data
public class Button {

    private int      id;
    private String   name;
    private int      commandId;
    private String   url;
    private Language langId;
    private boolean  requestContact;
    private int      messageId;


}
