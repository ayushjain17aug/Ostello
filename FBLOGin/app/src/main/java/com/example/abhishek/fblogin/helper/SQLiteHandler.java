package com.example.abhishek.fblogin.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.abhishek.fblogin.API.User;

/**
 * Created by nsn on 9/15/2015.
 */
public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "android_api";

    // Login table name
    private static final String TABLE_LOGIN = "login";
    String CREATE_SEARCH_TABLE = "CREATE TABLE " + "search_table" + "("
            + "_id" + " INTEGER PRIMARY KEY AUTOINCREMENT," + "name" + " TEXT,"
            + "address" + " TEXT," + "phone_no" + " TEXT" + ")";
    // Login Table Columns names
    private static final String USER_ID = "id";
    private static final String NAME = "name";
    private static final String PHOTO_URL = "photo_url";
    private static final String EMAIL = "email";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d("abhi", "In the contructor");
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("abhi", "In the On create");
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
                + USER_ID + " INTEGER PRIMARY KEY," + NAME + " TEXT,"
                + EMAIL + " TEXT UNIQUE," + PHOTO_URL + " TEXT UNIQUE" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);
        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     */
    //public void addUser(int user_id, String firstname, String lastname, String email,String phone,int verified,String college,String member_since,int count_event_registered) {
    public void addUser(String user_id, String name, String email, String photoUrl)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_ID, user_id);
        values.put(NAME, name); // Email
        // values.put(LAST_NAME, lastname); // Email
        values.put(EMAIL, email);
        values.put(PHOTO_URL, photoUrl);
        // Inserting Row
        long id = db.insert(TABLE_LOGIN, null, values);
        db.close(); // Closing database connection
        Log.d(TAG, "New user inserted into sqlite: " + id);
    }


    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(USER_ID, user.getUser_id());
        values.put(NAME, user.getName());
        //values.put(LAST_NAME, user.getLast_name());
        values.put(EMAIL, user.getEmail());
        values.put(PHOTO_URL, user.getPhotoUrl());
        //values.put(PHONE, user.getPhone());
        //values.put(VERIFIED, user.getVerified());
        //values.put(COLLEGE_NAME, user.getCollege());
        //values.put(MEMBER_SINCE, user.getMember_since());
        // updating row
        db.update(TABLE_LOGIN, values, USER_ID + " = ?",
                new String[]{String.valueOf(user.getUser_id())});
    }

    /**
     * Getting user data from database
     */

    public User getUser() {
        User user = new User();
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN +" ORDER BY "+USER_ID;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        if (cursor != null) {
            cursor.moveToFirst();
            user.setUser_id(cursor.getString(0));
            user.setName(cursor.getString(1));
            user.setEmail(cursor.getString(2));
            user.setPhotoUrl(cursor.getString(3));
            cursor.close();
        }
        db.close();
        return user;
    }

    /**
     * Getting user login status return true if rows are there in table
     */
    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();
        // return row count
        return rowCount;
    }

    public void updateeventscount(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        //value.put(COUNT_EVENT_REGISTERED,user.getcount_event_registered());
        db.update(TABLE_LOGIN, value, USER_ID + "=?", new String[]{String.valueOf(user.getUser_id())});

    }

    /**
     * Re crate database Delete all tables and create them again
     */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_LOGIN, null, null);
        db.close();
        Log.d(TAG, "Deleted all user info from sqlite");
    }
}