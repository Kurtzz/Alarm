package pl.agh.ki.io.alarm.sqlite.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import pl.agh.ki.io.alarm.sqlite.model.Friend;
import pl.agh.ki.io.alarm.sqlite.model.Group;

/**
 * Created by P on 18.05.2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    //Database name and version
    private static final String DATABASE_NAME = "alarmDatabase";
    private static final int DATABASE_VERSION = 1;


    // ------------------------ TABLES ------------------------ //
    // ------------------------ TABLE FRIEND ------------------------ //
    private static final String TABLE_FRIEND = "friends";
    //columns
    private static final String KEY_FRIEND_ID = "friend_id";
    private static final String KEY_NICK = "nick";
    private static final String KEY_LEVEL = "level";
    private static final String KEY_IS_BLOCKED = "is_blocked";
    //create statement
    /*
    CREATE_TABLE friends (
        friend_id INTEGER PRIMARY KEY NOT NULL,
        nick TEXT
    );
     */
    private static final String CREATE_TABLE_FRIEND =
            "CREATE TABLE " + TABLE_FRIEND
                    + "(" + KEY_FRIEND_ID + " INTEGER PRIMARY KEY NOT NULL, "
                    + KEY_NICK + " TEXT, "
                    + KEY_LEVEL + " INTEGER, "
                    + KEY_IS_BLOCKED + " INTEGER"
                    + ")";

    // ------------------------ TABLE GROUP ------------------------ //
    private static final String TABLE_GROUP = "groups";
    //columns
    private static final String KEY_GROUP_ID = "group_id";
    private static final String KEY_GROUP_NAME = "group_name";
    private static final String KEY_GROUP_LEVEL = "group_level";
    //create statement
    /*
    CREATE_TABLE groups (
        group_id INTEGER PRIMARY KEY NOT NULL,
        group_name TEXT
    );
     */
    private static final String CREATE_TABLE_GROUP =
            "CREATE TABLE " + TABLE_GROUP
                    + "(" + KEY_GROUP_ID + " INTEGER PRIMARY KEY NOT NULL, "
                    + KEY_GROUP_NAME + " TEXT, "
                    + KEY_GROUP_LEVEL + " INTEGER"
                    + ")";

    // ------------------------ TABLE GROUP FRIEND ------------------------ //
    private static final String TABLE_FRIEND_GROUP = "friend_group";
    /*
    CREATE_TABLE friend_group (
        PRIMARY KEY (friend_id, group_id)
        FOREIGN KEY (friend_id) REFERENCES friends(friend_id),
        FOREIGN KEY (group_id) REFERENCES groups(group_id),
        friend_id INTEGER NOT NULL,
        group_id INTEGER NOT NULL
    );
     */
    private static final String CREATE_TABLE_FRIEND_GROUP =
            "CREATE TABLE " + TABLE_FRIEND_GROUP
                    + "("
                    + KEY_FRIEND_ID + " INTEGER NOT NULL, "
                    + KEY_GROUP_ID + " INTEGER NOT NULL, "
                    + "FOREIGN KEY (" + KEY_FRIEND_ID + ") REFERENCES " + TABLE_FRIEND + "(" + KEY_FRIEND_ID + "), "
                    + "FOREIGN KEY (" + KEY_GROUP_ID + ") REFERENCES " + TABLE_GROUP + "(" + KEY_GROUP_ID + "), "
                    + "PRIMARY KEY (" + KEY_FRIEND_ID + ", " + KEY_GROUP_ID + "))";

    // ------------------------ END TABLES ------------------------ //

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_FRIEND);
        db.execSQL(CREATE_TABLE_GROUP);
        db.execSQL(CREATE_TABLE_FRIEND_GROUP);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIEND);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIEND_GROUP);

        // create new tables
        onCreate(db);
    }

    // ------------------------ "friends" table methods ----------------//

    /**
     * Create friend
     */
    public long createFriend(Friend friend) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FRIEND_ID, friend.getId());
        values.put(KEY_NICK, friend.getNick());
        values.put(KEY_LEVEL, friend.getLevel());
        values.put(KEY_IS_BLOCKED, (friend.isBlocked()) ? 1 : 0);

        return db.insert(TABLE_FRIEND, null, values);
    }

    /**
     * Update friend
     */
    public int updateFriend(Friend friend) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NICK, friend.getNick());
        values.put(KEY_LEVEL, friend.getLevel());
        values.put(KEY_IS_BLOCKED, (friend.isBlocked()) ? 1 : 0);

        return db.update(TABLE_FRIEND, values, KEY_FRIEND_ID + " = ?",
                new String[]{String.valueOf(friend.getId())});
    }

    /**
     * Get single friend
     */
    public Friend getFriend(long friend_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery =
                "SELECT * FROM " + TABLE_FRIEND
                        + " WHERE " + KEY_FRIEND_ID + " = " + friend_id;

        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null) {
            c.moveToFirst();
        }

        Friend friend = new Friend();
        friend.setId(c.getInt(c.getColumnIndex(KEY_FRIEND_ID)));
        friend.setNick(c.getString(c.getColumnIndex(KEY_NICK)));
        friend.setLevel(c.getInt(c.getColumnIndex(KEY_LEVEL)));
        friend.setBlocked(c.getInt(c.getColumnIndex(KEY_IS_BLOCKED)) != 0);

        c.close();
        return friend;
    }

    /**
     * Get all friends
     */
    public List<Friend> getFriends() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Friend> friends = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_FRIEND;
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Friend friend = new Friend();
                friend.setId(c.getInt(c.getColumnIndex(KEY_FRIEND_ID)));
                friend.setNick(c.getString(c.getColumnIndex(KEY_NICK)));
                friend.setLevel(c.getInt(c.getColumnIndex(KEY_LEVEL)));
                friend.setBlocked(c.getInt(c.getColumnIndex(KEY_IS_BLOCKED)) != 0);

                friends.add(friend);
            } while (c.moveToNext());
        }

        c.close();
        return friends;
    }

    /**
     * Delete friend
     */
    public void deleteFriend(long friend_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Friend friend = getFriend(friend_id);

        db.delete(TABLE_FRIEND, KEY_FRIEND_ID + " = ?", new String[]{String.valueOf(friend_id)});
    }

    // ------------------------ "friends" table methods ----------------//

    /**
     * Create group
     */
    public long createGroup(Group group) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_GROUP_ID, group.getId());
        values.put(KEY_GROUP_NAME, group.getGroupName());
        values.put(KEY_GROUP_LEVEL, group.getGroupLevel());

        long group_id = db.insert(TABLE_GROUP, null, values);
        createGroupFriend(group_id, group.getFriends());

        return group_id;
    }

    /**
     * Update group
     */
    public int updateGroup(Group group) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_GROUP_NAME, group.getGroupName());
        values.put(KEY_GROUP_LEVEL, group.getGroupLevel());

        return db.update(TABLE_GROUP, values, KEY_GROUP_ID + " = ?",
                new String[]{String.valueOf(group.getId())});
    }

    /**
     * Get single group
     */
    public Group getGroup(long group_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery =
                "SELECT * FROM " + TABLE_GROUP
                        + " WHERE " + KEY_GROUP_ID + " = " + group_id;

        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null) {
            c.moveToFirst();
        }

        Group group = new Group();
        group.setId(c.getInt(c.getColumnIndex(KEY_GROUP_ID)));
        group.setGroupName(c.getString(c.getColumnIndex(KEY_GROUP_NAME)));
        group.setGroupLevel(c.getInt(c.getColumnIndex(KEY_GROUP_LEVEL)));

        List<Friend> friends = getAllMembersOfTheGroup(group_id);
        group.setFriends(friends);

        c.close();
        return group;
    }

    /**
     * Get all groups
     */
    public List<Group> getGroups() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Group> groups = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_GROUP;
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Group group = new Group();
                group.setId(c.getInt(c.getColumnIndex(KEY_GROUP_ID)));
                group.setGroupName(c.getString(c.getColumnIndex(KEY_GROUP_NAME)));
                group.setGroupLevel(c.getInt(c.getColumnIndex(KEY_GROUP_LEVEL)));

                List<Friend> friends = getAllMembersOfTheGroup(group.getId());
                group.setFriends(friends);

                groups.add(group);
            } while (c.moveToNext());
        }

        c.close();
        return groups;
    }

    /**
     * Delete Group
     */
    public void deleteGroup(long group_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Group group = getGroup(group_id);

        db.delete(TABLE_GROUP, KEY_GROUP_ID + " = ?", new String[]{String.valueOf(group_id)});
    }

    // ------------------------ "group friend" table methods ----------------//

    /**
     * Create group_friend
     */
    public void createGroupFriend(long group_id, long friend_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_GROUP_ID, group_id);
        values.put(KEY_FRIEND_ID, friend_id);

        db.insert(TABLE_FRIEND_GROUP, null, values);
    }

    public void createGroupFriend(long group_id, List<Friend> friends) {
        for (Friend friend : friends) {
            createGroupFriend(group_id, friend.getId());
        }
    }

    /**
     * Get all members of the group
     */
    public List<Friend> getAllMembersOfTheGroup(long group_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Friend> friends = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_FRIEND_GROUP
                + " WHERE " + KEY_GROUP_ID + " = " + group_id;

        Cursor c = db.rawQuery(selectQuery, null);
        List<Integer> friend_ids = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                friend_ids.add(c.getInt(c.getColumnIndex(KEY_FRIEND_ID)));
            } while (c.moveToNext());
        }

        for (Integer integer : friend_ids) {
            Friend friend = getFriend(integer);
            friends.add(friend);
        }

        c.close();
        return friends;
    }

    /**
     * Delete Group_Friend by friend_id
     */
    public void deleteGroupFriend(Friend friend) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FRIEND_GROUP, KEY_FRIEND_ID + " = ?", new String[]{String.valueOf(friend.getId())});
    }

    /**
     * Delete Group_Friend by group_id
     */
    public void deleteGroupFriend(Group group) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FRIEND_GROUP, KEY_GROUP_ID + " = ?", new String[]{String.valueOf(group.getId())});
    }

    /**
     * Delete Group_Friend by group_id and friend_id
     */
    public void deleteGroupFriend(Group group, Friend friend) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FRIEND_GROUP, KEY_GROUP_ID + " = ? and " + KEY_FRIEND_ID + " = ?",
                new String[]{String.valueOf(group.getId()), String.valueOf(friend.getId())});
    }
}
