package pl.edu.agh.io.alarm.sqlite.model;
/**
 * Created by P on 18.05.2016.
 */
public class Friend {
    private int id;
    private String nick;
    private int level;

    public Friend() {

    }

    public Friend(int id, String nick, int level) {
        this.id = id;
        this.nick = nick;
        this.level = level;
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
}
