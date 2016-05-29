package pl.agh.ki.io.alarm.sqlite.model;

import java.util.Comparator;

/**
 * Created by P on 29.05.2016.
 */
public enum  FriendComparator implements Comparator<Friend> {
    ID_SORT {
        public int compare(Friend lhs, Friend rhs) {
            return ((Integer)lhs.getId()).compareTo(rhs.getId());
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

    public static Comparator<Friend> getComparator(final FriendComparator... multipleOptions) {
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
