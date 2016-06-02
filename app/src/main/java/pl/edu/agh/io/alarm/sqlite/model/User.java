package pl.edu.agh.io.alarm.sqlite.model;

public class User {

    private String nickname;
    private String token;

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

        if (!nickname.equals(user.nickname)) return false;
        return token.equals(user.token);

    }

    @Override
    public int hashCode() {
        int result = nickname.hashCode();
        result = 31 * result + token.hashCode();
        return result;
    }
}
