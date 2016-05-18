package pl.agh.ki.io.alarm.sqlite.model;

/**
 * Created by P on 18.05.2016.
 */
public class Friend {
    private int id;
    private String nick;

    public Friend() {

    }
    public Friend(int id, String nick) {
        this.id = id;
        this.nick = nick;
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
}
