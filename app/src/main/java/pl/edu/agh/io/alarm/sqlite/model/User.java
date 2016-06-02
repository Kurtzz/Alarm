package pl.edu.agh.io.alarm.sqlite.model;

public class User {

    private String nickname;
    private String token;
    private String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public String getToken() {
        return token;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        return uid.equals(user.uid);

    }

    @Override
    public int hashCode() {
        return uid.hashCode();
    }
}
