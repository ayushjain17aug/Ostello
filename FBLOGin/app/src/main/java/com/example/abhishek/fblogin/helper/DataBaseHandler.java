package com.example.abhishek.fblogin.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.abhishek.fblogin.API.PG;

import java.util.ArrayList;

/**
 * Created by Abhishek on 02-01-2017.
 */
public class DataBaseHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "LocalDatabase";
    private String TABLE_PG = "pg_table";
    private String HOSTEL_ID = "_id";
    private String ADDRESS = "address";
    private String HOSTEL_NAME = "name";
    private String PHONE_NO = "phoneNo";
    private String IMAGE_URL = "image_url";


    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PG_TABLE = "CREATE TABLE " + TABLE_PG + "("
                + HOSTEL_ID + " INTEGER PRIMARY KEY ," + HOSTEL_NAME + " TEXT,"
                + ADDRESS + " TEXT," + IMAGE_URL + " TEXT," + PHONE_NO + " TEXT" + ")";
        db.execSQL(CREATE_PG_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PG);
    }

    public void addPG(PG pg) {
        Log.d("abhi","Adding PG");
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

    public void deleteAllAttractions() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_PG);
    }
}
