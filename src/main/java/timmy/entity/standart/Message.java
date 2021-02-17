package timmy.entity.standart;

import lombok.Data;

import timmy.entity.enums.FileType;
import timmy.entity.enums.Language;

@Data
public class Message {

    private long id;
    private String name;
    private String Photo;
    private long keyboardMarkUpId;
    private String file;
    private FileType fileType;
    private Language language;

    public void setFile(String file, FileType fileType){
        this.file = file;
        this.fileType = fileType;
    }

}
