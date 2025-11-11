package Entities;

public class Config extends BaseEntity {

    private String title;
    private String value;

    public Config(int id, String title, String value) {
        super(id);
        this.title = title;
        this.value = value;
    }

    public Config() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
