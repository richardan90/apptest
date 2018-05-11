package com.example.richardantonios.testapp.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.List;

public class SampleSQLiteDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    // Database name
    private static final String DATABASE_NAME = "users";

    // Table user
    public static final String TABLE_USER_INFO = "user_info";

    // table user Columns
    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";
    public static final String USER_PASSWORD = "user_password";

    private Context context;

    public SampleSQLiteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_USER_INFO + " (" +
                USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USER_NAME + " TEXT," +
                USER_PASSWORD + " TEXT" +
                ")");

        sqLiteDatabase.beginTransaction();
        try {
            saveNewUser(sqLiteDatabase, "richard", "antonios");
            saveNewUser(sqLiteDatabase, "philippe", "chami");
            sqLiteDatabase.setTransactionSuccessful();
        }finally {
            sqLiteDatabase.endTransaction();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_INFO);
        onCreate(sqLiteDatabase);
    }

    private static void saveNewUser(SQLiteDatabase sqLiteDatabase, String username, String password) {
        ContentValues values = new ContentValues();
        values.put(SampleSQLiteDBHelper.USER_NAME, username);
        values.put(SampleSQLiteDBHelper.USER_PASSWORD, password);
        sqLiteDatabase.insert(SampleSQLiteDBHelper.TABLE_USER_INFO, null, values);
    }
}
