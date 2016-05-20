package pl.agh.ki.io.alarm.sqlite.model;

/**
 * Created by P on 18.05.2016.
 */
public class Friend {
    private int id;
    private String nick;
    private int level;
    private boolean isBlocked;

    public Friend() {

    }

    public Friend(int id, String nick, int level, boolean isBlocked) {
        this.id = id;
        this.nick = nick;
        this.level = level;
        this.isBlocked = isBlocked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }
}
