package com.example.abhishek.fblogin.helper;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by Abhishek on 30-01-2017.
 */

public class LocationContentProvider extends ContentProvider {


    private DataBaseHandler handler;
    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        handler = new DataBaseHandler(getContext());
        db = handler.getWritableDatabase();
        return false;
    }

    private static final HashMap<String, String> PROJECTION_MAP = new HashMap<String, String>();

    static {
        PROJECTION_MAP.put("_id", "_id");
        PROJECTION_MAP.put(SearchManager.SUGGEST_COLUMN_TEXT_1,
                "address AS " + SearchManager.SUGGEST_COLUMN_TEXT_1);
        PROJECTION_MAP.put(SearchManager.SUGGEST_COLUMN_INTENT_DATA, "address AS " + SearchManager.SUGGEST_COLUMN_INTENT_DATA);
    }
    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables("pg_table");
        String query = selectionArgs[0];
        Log.d("abhi", "query is in " + query);
        if (query.equals("")) {
            return null;
        }
        builder.appendWhere("INSTR(UPPER(address),UPPER('" + query + "'))");
        builder.setProjectionMap(PROJECTION_MAP);
        return builder.query(db, projection, null, null, null, null, null);
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}