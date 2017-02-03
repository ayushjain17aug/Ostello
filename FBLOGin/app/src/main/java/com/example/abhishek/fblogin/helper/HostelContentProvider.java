package com.example.abhishek.fblogin.helper;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by Abhishek on 29-12-2016.
 */
public class HostelContentProvider extends ContentProvider {

    private DataBaseHandler handler;
    private SQLiteDatabase db;

    UriMatcher mUriMatcher = buildUriMatcher();
    private static String AUTHORITY="com.example.abhishek.fblogin.helper.HostelContentProvider";
    private final int SUGGESTIONS=0;


    private UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        // Suggestion items of Search Dialog is provided by this uri
        uriMatcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY,SUGGESTIONS);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        handler = new DataBaseHandler(getContext());
        db = handler.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        Log.d("abhi", "In the getType()");
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

    private static final HashMap<String, String> PROJECTION_MAP = new HashMap<String, String>();

    static {
        PROJECTION_MAP.put("_id", "_id");
        PROJECTION_MAP.put(SearchManager.SUGGEST_COLUMN_TEXT_1,
                "name AS " + SearchManager.SUGGEST_COLUMN_TEXT_1);
        PROJECTION_MAP.put(SearchManager.SUGGEST_COLUMN_INTENT_DATA,"_id AS "+SearchManager.SUGGEST_COLUMN_INTENT_DATA);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables("pg_table");
        switch (mUriMatcher.match(uri)) {
            case SUGGESTIONS:
                String query = selectionArgs[0];
                Log.d("abhi", "query is "+query);
                if (query.equals("")) {
                    return null;
                }
                builder.appendWhere("INSTR(UPPER(name),UPPER('" + query + "'))");
                builder.setProjectionMap(PROJECTION_MAP);
                break;
        }
    return builder.query(db,projection,null,null,null,null,null);
    }
    }