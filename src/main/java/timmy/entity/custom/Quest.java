package timmy.entity.custom;

import lombok.Data;

@Data
public class Quest {
    private int id;
    private String name;
    private String text;
    private int langId;
}
