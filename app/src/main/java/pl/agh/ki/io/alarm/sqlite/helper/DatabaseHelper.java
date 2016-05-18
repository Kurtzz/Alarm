package pl.agh.ki.io.alarm.sqlite.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
        FOREIGN KEY (friend_id) REFERENCES friends(id),
        FOREIGN KEY (group_id) REFERENCES groups(id),
        friend_id INTEGER NOT NULL,
        group_id INTEGER NOT NULL
    );
     */
    private static final String CREATE_TABLE_FRIEND_GROUP =
            "CREATE_TABLE " + TABLE_FRIEND_GROUP
                    + "(" + "PRIMARY KEY (" + KEY_FRIEND_ID + ", " + KEY_GROUP_ID + "), "
                    + "FOREIGN KEY (" + KEY_FRIEND_ID + ") REFERENCES " + TABLE_FRIEND + "(id), "
                    + "FOREIGN KEY (" + KEY_GROUP_ID + ") REFERENCES " + TABLE_GROUP + "(id), "
                    + KEY_FRIEND_ID + " INTEGER NOT NULL, "
                    + KEY_GROUP_ID + " INTEGER NOT NULL )";
    
    // ------------------------ END TABLES ------------------------ //

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
