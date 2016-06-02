package pl.edu.agh.io.alarm.sqlite.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import pl.edu.agh.io.alarm.sqlite.model.Friend;
import pl.edu.agh.io.alarm.sqlite.model.Group;
import pl.edu.agh.io.alarm.sqlite.model.User;

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
        friend_id TEXT PRIMARY KEY NOT NULL,
        nick TEXT,
        level INTEGER,
        is_blocked INTEGER
    );
     */
    private static final String CREATE_TABLE_FRIEND =
            "CREATE TABLE " + TABLE_FRIEND
                    + "(" + KEY_FRIEND_ID + " TEXT PRIMARY KEY NOT NULL, "
                    + KEY_NICK + " TEXT, "
                    + KEY_LEVEL + " INTEGER, "
                    + KEY_IS_BLOCKED + " INTEGER"
                    + ")";

    // ------------------------ TABLE GROUP ------------------------ //
    private static final String TABLE_GROUP = "groups";
    //columns
    private static final String KEY_GROUP_NAME_ID = "group_name_id";
    private static final String KEY_GROUP_LEVEL = "group_level";
    //create statement
    /*
    CREATE_TABLE groups (
        group_name_id TEXT PRIMARY KEY NOT NULL,
        group_level INTEGER
    );
     */
    private static final String CREATE_TABLE_GROUP =
            "CREATE TABLE " + TABLE_GROUP
                    + "(" + KEY_GROUP_NAME_ID + " TEXT PRIMARY KEY NOT NULL, "
                    + KEY_GROUP_LEVEL + " INTEGER"
                    + ")";

    // ------------------------ TABLE GROUP FRIEND ------------------------ //
    private static final String TABLE_FRIEND_GROUP = "friend_group";
    /*
    CREATE_TABLE friend_group (
        friend_id TEXT NOT NULL,
        group_name_id TEXT NOT NULL,
        FOREIGN KEY (friend_id) REFERENCES friends(friend_id),
        FOREIGN KEY (group_id) REFERENCES groups(group_id),
        PRIMARY KEY (friend_id, group_id)
    );
     */
    private static final String CREATE_TABLE_FRIEND_GROUP =
            "CREATE TABLE " + TABLE_FRIEND_GROUP
                    + "("
                    + KEY_FRIEND_ID + " TEXT NOT NULL, "
                    + KEY_GROUP_NAME_ID + " TEXT NOT NULL, "
                    + "FOREIGN KEY (" + KEY_FRIEND_ID + ") REFERENCES " + TABLE_FRIEND + "(" + KEY_FRIEND_ID + "), "
                    + "FOREIGN KEY (" + KEY_GROUP_NAME_ID + ") REFERENCES " + TABLE_GROUP + "(" + KEY_GROUP_NAME_ID + "), "
                    + "PRIMARY KEY (" + KEY_FRIEND_ID + ", " + KEY_GROUP_NAME_ID + "))";

    // ----------------------------- TABLE USER ----------------------------- //
    private static final String TABLE_USER = "user";

    /*
    CREATE_TABLE user (
        user_nick TEXT NOT NULL,
        user_token TEXT NOT NULL,
        user_uuid TEXT PRIMARY KEY NOT NULL
    );
    */

    private static final String KEY_USER_NICK = "user_nick";
    private static final String KEY_USER_TOKEN = "user_token";
    private static final String KEY_USER_UUID = "user_uuid";

    private static final String CREATE_TABLE_USER =
            "CREATE TABLE " + TABLE_USER + "(" +
                    KEY_USER_UUID + " TEXT PRIMARY KEY NOT NULL," +
                    KEY_USER_NICK + " TEXT NOT NULL," +
                    KEY_USER_TOKEN + " TEXT NOT NULL);";

    // ------------------------ END TABLES ------------------------ //

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.i("DB", "Constr! " + CREATE_TABLE_USER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_FRIEND);
        db.execSQL(CREATE_TABLE_GROUP);
        db.execSQL(CREATE_TABLE_FRIEND_GROUP);
        db.execSQL(CREATE_TABLE_USER);
        Log.i("DB", "DUPAP! " + CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIEND);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIEND_GROUP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

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
    public Friend getFriend(String friendId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery =
                "SELECT * FROM " + TABLE_FRIEND
                        + " WHERE " + KEY_FRIEND_ID + " = \"" + friendId + "\"";

        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null) {
            c.moveToFirst();
        }

        Friend friend = new Friend();
        friend.setId(c.getString(c.getColumnIndex(KEY_FRIEND_ID)));
        friend.setNick(c.getString(c.getColumnIndex(KEY_NICK)));
        friend.setLevel(c.getInt(c.getColumnIndex(KEY_LEVEL)));
        friend.setBlocked(c.getInt(c.getColumnIndex(KEY_IS_BLOCKED)) != 0);

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
                friend.setId(c.getString(c.getColumnIndex(KEY_FRIEND_ID)));
                friend.setNick(c.getString(c.getColumnIndex(KEY_NICK)));
                friend.setLevel(c.getInt(c.getColumnIndex(KEY_LEVEL)));
                friend.setBlocked(c.getInt(c.getColumnIndex(KEY_IS_BLOCKED)) != 0);

                friends.add(friend);
            } while (c.moveToNext());
        }

        return friends;
    }

    /**
     * Delete friend
     */
    public void deleteFriend(String friendId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Friend friend = getFriend(friendId);

        db.delete(TABLE_FRIEND, KEY_FRIEND_ID + " = ?", new String[]{String.valueOf(friendId)});

        //delete dependency
        deleteGroupFriend(friend);
    }

    // ------------------------ "friends" table methods ----------------//

    /**
     * Create group
     */
    public long createGroup(Group group) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_GROUP_NAME_ID, group.getNameId());
        values.put(KEY_GROUP_LEVEL, group.getGroupLevel());

        long group_id = db.insert(TABLE_GROUP, null, values);
        createGroupFriend(group.getNameId(), group.getFriends());

        return group_id;
    }

    /**
     * Update group
     */
    public void updateGroup(Group group) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_GROUP_LEVEL, group.getGroupLevel());

        db.update(TABLE_GROUP, values, KEY_GROUP_NAME_ID + " = ?",
                new String[]{String.valueOf(group.getNameId())});

        //Update linking tables
        List<Friend> dbFriends = getAllMembersOfTheGroup(group.getNameId());
        if (dbFriends.equals(group.getFriends())) {
            return;
        }
        for (Friend friend : dbFriends) {
            if (!group.getFriends().contains(friend)) {
                deleteGroupFriend(group, friend);
            }
        }
        for (Friend friend : group.getFriends()) {
            if (!dbFriends.contains(friend)) {
                createGroupFriend(group.getNameId(), friend.getId());
            }
        }
    }

    /**
     * Get single group
     */
    public Group getGroup(String groupId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery =
                "SELECT * FROM " + TABLE_GROUP
                        + " WHERE " + KEY_GROUP_NAME_ID + " = \"" + groupId + "\"";

        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null) {
            c.moveToFirst();
        }

        Group group = new Group();
        group.setNameId(c.getString(c.getColumnIndex(KEY_GROUP_NAME_ID)));
        group.setGroupLevel(c.getInt(c.getColumnIndex(KEY_GROUP_LEVEL)));

        List<Friend> friends = getAllMembersOfTheGroup(groupId);
        group.setFriends(friends);

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
                group.setNameId(c.getString(c.getColumnIndex(KEY_GROUP_NAME_ID)));
                group.setGroupLevel(c.getInt(c.getColumnIndex(KEY_GROUP_LEVEL)));

                List<Friend> friends = getAllMembersOfTheGroup(group.getNameId());
                group.setFriends(friends);

                groups.add(group);
            } while (c.moveToNext());
        }

        return groups;
    }

    /**
     * Delete Group
     */
    public void deleteGroup(String groupId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Group group = getGroup(groupId);

        db.delete(TABLE_GROUP, KEY_GROUP_NAME_ID + " = ?", new String[]{String.valueOf(groupId)});

        //delete dependency
        deleteGroupFriend(group);
    }

    // ------------------------ "group friend" table methods ----------------//

    /**
     * Create group_friend
     */
    public void createGroupFriend(String groupId, String friendId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_GROUP_NAME_ID, groupId);
        values.put(KEY_FRIEND_ID, friendId);

        db.insert(TABLE_FRIEND_GROUP, null, values);
    }

    public void createGroupFriend(String groupId, List<Friend> friends) {
        for (Friend friend : friends) {
            createGroupFriend(groupId, friend.getId());
        }
    }

    /**
     * Get all members of the group
     */
    public List<Friend> getAllMembersOfTheGroup(String groupId) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Friend> friends = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_FRIEND_GROUP
                + " WHERE " + KEY_GROUP_NAME_ID + " = \"" + groupId + "\"";

        Cursor c = db.rawQuery(selectQuery, null);
        List<String> friend_ids = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                friend_ids.add(c.getString(c.getColumnIndex(KEY_FRIEND_ID)));
            } while (c.moveToNext());
        }

        for (String id : friend_ids) {
            Friend friend = getFriend(id);
            friends.add(friend);
        }

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
        db.delete(TABLE_FRIEND_GROUP, KEY_GROUP_NAME_ID + " = ?", new String[]{String.valueOf(group.getNameId())});
    }

    /**
     * Delete Group_Friend by group_id and friend_id
     */
    public void deleteGroupFriend(Group group, Friend friend) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FRIEND_GROUP, KEY_GROUP_NAME_ID + " = ? and " + KEY_FRIEND_ID + " = ?",
                new String[]{String.valueOf(group.getNameId()), String.valueOf(friend.getId())});
    }

    public long createUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USER_NICK, user.getNickname());
        values.put(KEY_USER_TOKEN, user.getToken());
        values.put(KEY_USER_UUID, user.getUid());

        return db.insert(TABLE_USER, null, values);
    }

    public User getUser() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery =
                "SELECT * FROM " + TABLE_USER;

        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null) {
            c.moveToFirst();
        }

        User user = new User();
        user.setNickname(c.getString(c.getColumnIndex(KEY_USER_NICK)));
        user.setToken(c.getString(c.getColumnIndex(KEY_USER_TOKEN)));
        user.setUid(c.getString(c.getColumnIndex(KEY_USER_UUID)));

        return user;
    }
}
