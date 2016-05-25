package pl.agh.ki.io.alarm.sqlite.model;

/**
 * Created by P on 18.05.2016.
 */
public class Friend implements Comparable<Friend> {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Friend friend = (Friend) o;

        return getId() == friend.getId();

    }

    @Override
    public int hashCode() {
        return getId();
    }

    @Override
    public int compareTo(Friend another) {
        return this.getNick().compareTo(another.getNick());
    }
}
