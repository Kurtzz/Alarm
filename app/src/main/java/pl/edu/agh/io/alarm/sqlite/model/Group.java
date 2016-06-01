package pl.edu.agh.io.alarm.sqlite.model;

import java.util.Comparator;
import java.util.List;

public class Group {
    private int id;
    private String groupName;
    private int groupLevel;
    private List<Friend> friends;

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

    public List<Friend> getFriends() {
        return friends;
    }

    public void setFriends(List<Friend> friends) {
        this.friends = friends;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Group group = (Group) o;

        return getId() == group.getId();

    }

    @Override
    public int hashCode() {
        return getId();
    }

    public enum  GroupComparator implements Comparator<Group> {
        GROUP_ID_SORT {
            public int compare(Group lhs, Group rhs) {
                return ((Integer)lhs.getId()).compareTo(rhs.getId());
            }
        },
        GROUP_NAME_SORT {
            public int compare(Group lhs, Group rhs) {
                return lhs.getGroupName().compareToIgnoreCase(rhs.getGroupName());
            }
        },
        GROUP_LEVEL_SORT {
            public int compare(Group lhs, Group rhs) {
                return ((Integer)lhs.getGroupLevel()).compareTo(rhs.getGroupLevel());
            }
        },
        FRIEND_SIZE_SORT {
            public int compare(Group lhs, Group rhs) {
                return ((Integer)lhs.getFriends().size()).compareTo(rhs.getFriends().size());
            }
        };

        public static Comparator<Group> getGroupComparator(final GroupComparator... multipleOptions) {
            return new Comparator<Group>() {
                public int compare(Group o1, Group o2) {
                    for (GroupComparator option : multipleOptions) {
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
