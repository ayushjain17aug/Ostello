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
    private String HOSTEL_NAME = "name";
    private String PHONE_NO = "phoneNo";
    private String IMAGE_URL1 = "image_url1";
    private String IMAGE_URL2 = "image_url2";
    private String IMAGE_URL3 = "image_url3";
    private String IMAGE_URL4 = "image_url4";
    private String IMAGE_URL5 = "image_url5";
    private String RENTAL1 = "rental1";
    private String RENTAL2 = "rental2";
    private String RENTAL3 = "rental3";
    private String AMENTIES = "amenties";
    private String CANCELLATION_POLICY = "cancellation_policy";
    private String HOSTEL_RULE = "hostel_rule";
    private String GENDER_HOSTEL = "gender";
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
                + ADDRESS + " TEXT," + IMAGE_URL1 + " TEXT," + IMAGE_URL2 + " TEXT," + IMAGE_URL3
                +" TEXT," + IMAGE_URL4 + " TEXT," + IMAGE_URL5 + " TEXT," + RENTAL1 + " TEXT,"
                + RENTAL2 + " TEXT," + RENTAL3 + " TEXT," + AMENTIES + " TEXT," + CANCELLATION_POLICY + " TEXT,"
                + HOSTEL_RULE + " TEXT," + GENDER_HOSTEL + " TEXT,"+ SHARING + " TEXT,"+ PHONE_NO + " TEXT"+ ")";
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
        values.put(IMAGE_URL1, pg.getImage_url1());
        values.put(IMAGE_URL2, pg.getImage_url2());
        values.put(IMAGE_URL3, pg.getImage_url3());
        values.put(IMAGE_URL4, pg.getImage_url4());
        values.put(IMAGE_URL5, pg.getImage_url5());
        values.put(RENTAL1, pg.getRental1());
        values.put(RENTAL2, pg.getRental2());
        values.put(RENTAL3, pg.getRental3());
        values.put(AMENTIES, pg.getAmenties());
        values.put(CANCELLATION_POLICY, pg.getCancellation_policy());
        values.put(HOSTEL_RULE, pg.getHostel_rule());
        values.put(GENDER_HOSTEL, pg.getGender());
        values.put(SHARING, pg.getSharing());
        values.put(PHONE_NO, pg.getPhoneNo());

        // Inserting Row
        db.insert(TABLE_PG, null, values);
        db.close(); // Closing database connection
        Log.e("PG:", "stored in db.");
    }

    public void addFavPg(int id, String name) {
        Log.d("abhi","inserting"+name+" "+id);
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
        String delete = "DELETE FROM " + TABLE_FAV_PG + " WHERE "+HOSTEL_ID+" = '"+id+"'";
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
                        cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6),
                        cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10),
                        cursor.getString(11), cursor.getString(12), cursor.getString(13),cursor.getString(14),cursor.getString(15),
                        cursor.getString(16));
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
            cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6),
            cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10),
            cursor.getString(11), cursor.getString(12), cursor.getString(13),cursor.getString(14),cursor.getString(15),
            cursor.getString(16));
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
}