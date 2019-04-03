package com.cosc417.nelsm.shuffled;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

public class DBHelper extends SQLiteOpenHelper {
    private	static final int DATABASE_VERSION =	8;
    public static final String DATABASE_NAME = "shuffled.db";

    // User Table
    public static final String USER_TABLE = "user";
    public static final String COL_1 = "id";
    public static final String COL_2 = "username";
    public static final String COL_3 = "password";

    // Data Table
    public static final String DATA_TABLE = "data";
    public static final String COL_4 = "data_id";
    public static final String COL_5 = "user_data_id";
    public static final String COL_6 = "current_word";
    public static final String COL_7 = "score";

    // Guessed Words Table
    public static final String WORDS_TABLE = "words";
    public static final String COL_8 = "word_id";
    public static final String COL_9 = "user_word_id";
    public static final String COL_10 = "word";

    public DBHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE user (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT)");
        db.execSQL("CREATE TABLE " + DATA_TABLE + " (data_id INTEGER PRIMARY KEY AUTOINCREMENT, user_data_id INTEGER, current_word TEXT, score INTEGER, FOREIGN KEY (user_data_id) REFERENCES " + USER_TABLE + " (id))");
        db.execSQL("CREATE TABLE " + WORDS_TABLE + " (word_id INTEGER PRIMARY KEY AUTOINCREMENT, user_word_id INTEGER, word TEXT, FOREIGN KEY (user_word_id) REFERENCES " + USER_TABLE + " (id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DATA_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + WORDS_TABLE);
        onCreate(db);
    }

    public long addUser(String user, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", user);
        contentValues.put("password", password);
        long res = db.insert("user", null, contentValues);
        db.close();
        return res;
    }

    public boolean verifyUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String SQL = "SELECT * FROM " + USER_TABLE + " WHERE " + COL_2 + " =? " + " AND " + COL_3 + " =? ";
        String[] arguments = {username, password};

        Cursor cursor = db.rawQuery(SQL, arguments);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        if(count > 0) {
            return true;
        }else{
            return false;
        }
    }

    public int getUserID(String user) {
        SQLiteDatabase db = this.getReadableDatabase();
        String SQL = "SELECT id FROM " + USER_TABLE + " WHERE " + COL_2 + " =? ";
        String[] arguments = {user};
        int id = 0;

        Cursor cursor = db.rawQuery(SQL, arguments);

        if(cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }

        return id;
    }

    public Boolean checkUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String SQL = "SELECT * FROM " + USER_TABLE + " WHERE " + COL_2 + " =? ";
        String[] arguments = {username};

        Cursor cursor = db.rawQuery(SQL, arguments);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        if(count > 0) {
            return true;
        }else{
            return false;
        }
    }

}
