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
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        handleIntent(getIntent());
    }

    /*using "singleTop" launch mode is usually ideal, because chances are good that once a search is done,
         the user will perform additional searches and it's a bad experience if your application creates
         multiple instances of the searchable activity.
        So, we recommend that you set your searchable activity to "singleTop" launch mode in the application manifest*/
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

        Log.d("abhi", "In do my Search" + query);
       /* String columns[] = {"_id", "name", "address","phone_no"};
        //Cursor cursor=db.query("search_table",columns,"name"+" LIKE '"+query+"'",null,null,null,null);
        Cursor cursor=db.query("search_table",columns,"INSTR(name,'"+query+"')",null,null,null,null);

        while(cursor.moveToNext()==true){
            s=s+cursor.getInt(0)+""+cursor.getString(1)+" "+cursor.getString(2)+" "+cursor.getString(3)+"\n";
        }*/
        String s;
        s = "" + getIntent().getData().toString();
        Result.setText(s);
        /*if (cursor==null)
            return null;
        else if(!cursor.moveToFirst()){
            cursor.close();
            return null;
        }*/
    }

}