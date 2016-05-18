package pl.agh.ki.io.alarm.sqlite.model;

/**
 * Created by P on 18.05.2016.
 */
public class Group {
    private int id;
    private String groupName;
    private int groupLevel;


    public Group() {

    }

    public Group(int id, String groupName, int groupLevel) {
        this.id = id;
        this.groupName = groupName;
        this.groupLevel = groupLevel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getGroupLevel() {
        return groupLevel;
    }

    public void setGroupLevel(int groupLevel) {
        this.groupLevel = groupLevel;
    }
}
