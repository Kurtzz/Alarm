package pl.edu.agh.io.alarm.sqlite.model;

import java.util.Comparator;

public class Friend {
    private String id;
    private String nick;
    private int level;
    private boolean isBlocked;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

        return getId().equals(friend.getId());

    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    public enum  FriendComparator implements Comparator<Friend> {
        FRIEND_ID_SORT {
            public int compare(Friend lhs, Friend rhs) {
                return lhs.getId().compareTo(rhs.getId());
            }
        },
        NICK_SORT {
            public int compare(Friend lhs, Friend rhs) {
                return lhs.getNick().compareToIgnoreCase(rhs.getNick());
            }
        },
        LEVEL_SORT {
            public int compare(Friend lhs, Friend rhs) {
                return ((Integer)lhs.getLevel()).compareTo(rhs.getLevel());
            }
        },
        BLOCK_SORT {
            public int compare(Friend lhs, Friend rhs) {
                if (lhs.isBlocked() == rhs.isBlocked()) {
                    return 0;
                }
                return lhs.isBlocked() ? 1 : -1;
            }
        };

        public static Comparator<Friend> getFriendComparator(final FriendComparator... multipleOptions) {
            return new Comparator<Friend>() {
                public int compare(Friend o1, Friend o2) {
                    for (FriendComparator option : multipleOptions) {
                        int result = option.compare(o1, o2);
                        if (result != 0) {
                            return result;
                        }
                    }
                    return 0;
                }
            };
        }
    }
}
