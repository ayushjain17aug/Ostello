package com.example.abhishek.fblogin.Activity;

import android.app.SearchManager;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.abhishek.fblogin.R;
import com.example.abhishek.fblogin.helper.SQLiteHandler;

public class SearchableActivity extends AppCompatActivity {

    private SQLiteHandler handler;
    private SQLiteDatabase db;
    private TextView Result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Result = (TextView) findViewById(R.id.result);
        handler = new SQLiteHandler(getApplicationContext());
        db = handler.getWritableDatabase();
        handleIntent(getIntent());
        Log.d("abhi","In the on Create() of Searchable Activity()");
    }
    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        Log.d("abhi", "In New Intent");
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        Log.d("abhi", "In handle Intent" + intent.getAction() + "  " + Intent.ACTION_SEARCH);
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }

    private void doMySearch(String query) {
        String s;
        s = "" + getIntent().getData().toString();
        Result.setText(s);
    }

}