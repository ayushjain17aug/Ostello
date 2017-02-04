package com.example.abhishek.fblogin.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.abhishek.fblogin.API.MyProfile;
import com.example.abhishek.fblogin.API.PG;

import java.util.ArrayList;

/**
 * Created by Abhishek on 02-01-2017.
 */
public class DataBaseHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 2;
    // Database Name
    private static final String DATABASE_NAME = "LocalDatabase";
    private String TABLE_PG = "pg_table";
    private String HOSTEL_ID = "_id";
    private String ADDRESS = "address";
    private String TYPE = "type";
    private String HOSTEL_NAME = "name";
    private String PHONE_NO = "phoneNo";
    private String IMAGE_URL = "image_url";
    private String SHARING = "sharing";
    //MyProfileTableColumn
    private String TABLE_MY_PROFILE = "my_profile_table";
    private String NAME = "name";
    private String AGE = "age";
    private String USER_ID = "user_id";
    private String EMAIL = "email";
    private String GENDER = "gender";
    private String PROFILE_PIC = "profile_pic";

    private String TABLE_FAV_PG = "fav_pg";

    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PG_TABLE = "CREATE TABLE " + TABLE_PG + "("
                + HOSTEL_ID + " INTEGER PRIMARY KEY ," + HOSTEL_NAME + " TEXT,"
                + ADDRESS + " TEXT," + IMAGE_URL + " TEXT," + PHONE_NO + " TEXT" + ")";
        db.execSQL(CREATE_PG_TABLE);

        String CREATE_MY_PROFILE_TABLE = "CREATE TABLE " + TABLE_MY_PROFILE + "("
                + USER_ID + " TEXT ," + NAME + " TEXT,"
                + PHONE_NO + " TEXT," + AGE + " TEXT," + EMAIL + " TEXT," + GENDER + " TEXT," + PROFILE_PIC + " TEXT" + ")";
        db.execSQL(CREATE_MY_PROFILE_TABLE);

        String CREATE_FAV_PG_TABLE = "CREATE TABLE " + TABLE_FAV_PG + "("
                + HOSTEL_ID + " INTEGER ," + HOSTEL_NAME + " TEXT" + ")";
        db.execSQL(CREATE_FAV_PG_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MY_PROFILE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAV_PG);
        onCreate(db);
    }

    public void addPG(PG pg) {
        Log.d("abhi", "Adding PG");
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(HOSTEL_ID, pg.getId());
        values.put(HOSTEL_NAME, pg.getName());
        values.put(ADDRESS, pg.getAddress());
        values.put(IMAGE_URL, pg.getImage_url());
        values.put(PHONE_NO, pg.getPhoneNo());

        // Inserting Row
        db.insert(TABLE_PG, null, values);
        db.close(); // Closing database connection
        Log.e("PG:", "stored in db.");
    }

    public void addFavPg(int id, String name) {
        Log.d("abhi", "inserting" + name + " " + id);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HOSTEL_ID, id);
        values.put(HOSTEL_NAME, name);
        db.insert(TABLE_FAV_PG, null, values);
        db.close();
    }

    public ArrayList<Integer> getFavPGs() {
        ArrayList<Integer> pgFavList = new ArrayList<Integer>();
        String selectQuery = "SELECT  * FROM " + TABLE_FAV_PG + " ORDER BY " + HOSTEL_ID;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                pgFavList.add(cursor.getInt(0));
            } while (cursor.moveToNext());
        }
        //Log.d("abhi","In the getFavPGs"+pgFavList.size());
        return pgFavList;
    }

    public void deleteFavPg(int id) {
        // Log.d("abhi","deleting"+" "+id);
        SQLiteDatabase db = this.getWritableDatabase();
        String delete = "DELETE FROM " + TABLE_FAV_PG + " WHERE " + HOSTEL_ID + " = '" + id + "'";
        db.execSQL(delete);
    }

    public ArrayList<PG> getAllPGs() {
        ArrayList<PG> pgList = new ArrayList<PG>();
        String selectQuery = "SELECT  * FROM " + TABLE_PG + " ORDER BY " + HOSTEL_ID;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                PG pg = new PG(cursor.getInt(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3), cursor.getString(4));
                pgList.add(pg);
            } while (cursor.moveToNext());
        }
        return pgList;
    }

    public PG getPG(String Hostel_ID)
    {
        String selectQuery = "SELECT  * FROM " + TABLE_PG + " WHERE " + HOSTEL_ID + " = " + Hostel_ID;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor  cursor = db.rawQuery(selectQuery,null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        PG pg = new PG(cursor.getInt(0), cursor.getString(1),
                cursor.getString(2), cursor.getString(3), cursor.getString(4));
        return pg;
    }

    public void deletePGs() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_PG);
    }

    public void addProfile(MyProfile myProfile) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_ID, myProfile.getUser_id());
        values.put(NAME, myProfile.getName());
        values.put(PHONE_NO, myProfile.getPhone());
        values.put(AGE, myProfile.getAge());
        values.put(EMAIL, myProfile.getEmail());
        values.put(GENDER, myProfile.getGender());
        values.put(PROFILE_PIC, myProfile.getImage());
        // Inserting Row
        long b = db.insertOrThrow(TABLE_MY_PROFILE, null, values);
        //Log.d("abhi", "Row Id :" + b);
        db.close(); // Closing database connection
    }

    public MyProfile getMyProfile() {
        String selectQuery = "SELECT  * FROM " + TABLE_MY_PROFILE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        MyProfile myProfile = null;
        //Log.d("abhi", " In getMyProfile " + cursor.moveToFirst());
        if (cursor.moveToFirst() && cursor != null) {
            do {
                myProfile = new MyProfile(cursor.getString(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
                Log.d("abhi", "Query " + myProfile.getImage());
                return myProfile;
            } while (cursor.moveToNext());
        }

        return myProfile;
    }

    public void deleteMyProfile() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_MY_PROFILE);
    }

    public ArrayList<PG> getGenderBasedHostel(String gender) {
        SQLiteDatabase db = this.getWritableDatabase();
        String getPgQuery = "SELECT  * FROM " + TABLE_PG + " WHERE " + TYPE + " = '" + gender + "'";
        ArrayList<PG> pgList = new ArrayList<PG>();
        Cursor cursor = db.rawQuery(getPgQuery, null);
        if (cursor.moveToFirst()) {
            do {
                PG pg = new PG(cursor.getInt(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3), cursor.getString(4));
                pgList.add(pg);
            } while (cursor.moveToNext());
        }
        return pgList;
    }
    public ArrayList<PG> getSharingBasedHostel(String sharing) {
        SQLiteDatabase db = this.getWritableDatabase();
        String getPgQuery = "SELECT  * FROM " + TABLE_PG + " WHERE " + SHARING + " = '" + sharing + "'";
        ArrayList<PG> pgList = new ArrayList<PG>();
        Cursor cursor = db.rawQuery(getPgQuery, null);
        if (cursor.moveToFirst()) {
            do {
                PG pg = new PG(cursor.getInt(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3), cursor.getString(4));
                pgList.add(pg);
            } while (cursor.moveToNext());
        }
        return pgList;
    }
    public ArrayList<PG> getGenderAndSharingBasedHostel(String gender,String sharing) {
        SQLiteDatabase db = this.getWritableDatabase();
        String getPgQuery = "SELECT  * FROM " + TABLE_PG + " WHERE " + TYPE + " = '" + gender + "' AND " + SHARING + " = '" + sharing + "'";
        ArrayList<PG> pgList = new ArrayList<PG>();
        Cursor cursor = db.rawQuery(getPgQuery, null);
        if (cursor.moveToFirst()) {
            do {
                PG pg = new PG(cursor.getInt(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3), cursor.getString(4));
                pgList.add(pg);
            } while (cursor.moveToNext());
        }
        return pgList;
    }
}