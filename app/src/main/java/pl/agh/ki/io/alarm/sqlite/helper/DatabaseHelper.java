package pl.agh.ki.io.alarm.sqlite.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import pl.agh.ki.io.alarm.sqlite.model.Friend;

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
    //create statement
    /*
    CREATE_TABLE friends (
        friend_id INTEGER PRIMARY KEY NOT NULL,
        nick TEXT
    );
     */
    private static final String CREATE_TABLE_FRIEND =
            "CREATE_TABLE " + TABLE_FRIEND
                    + "(" + KEY_FRIEND_ID + " INTEGER PRIMARY KEY NOT NULL, "
                    + KEY_NICK + " TEXT"
                    + ")";

    // ------------------------ TABLE GROUP ------------------------ //
    private static final String TABLE_GROUP = "groups";
    //columns
    private static final String KEY_GROUP_ID = "group_id";
    private static final String KEY_GROUP_NAME = "group_name";
    //create statement
    /*
    CREATE_TABLE groups (
        group_id INTEGER PRIMARY KEY NOT NULL,
        group_name TEXT
    );
     */
    private static final String CREATE_TABLE_GROUP =
            "CREATE_TABLE " + TABLE_GROUP
                    + "(" + KEY_GROUP_ID + " INTEGER PRIMARY KEY NOT NULL, "
                    + KEY_GROUP_NAME + " TEXT"
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
            "CREATE_TABLE " + TABLE_FRIEND_GROUP
                    + "(" + "PRIMARY KEY (" + KEY_FRIEND_ID + ", " + KEY_GROUP_ID + "), "
                    + "FOREIGN KEY (" + KEY_FRIEND_ID + ") REFERENCES " + TABLE_FRIEND + "(" + KEY_FRIEND_ID + "), "
                    + "FOREIGN KEY (" + KEY_GROUP_ID + ") REFERENCES " + TABLE_GROUP + "(" + KEY_GROUP_ID + "), "
                    + KEY_FRIEND_ID + " INTEGER NOT NULL, "
                    + KEY_GROUP_ID + " INTEGER NOT NULL )";

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
        values.put(KEY_NICK, friend.getNick());

        long friend_id = db.insert(TABLE_FRIEND, null, values);

        return friend_id;
    }
}
