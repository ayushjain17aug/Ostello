package com.example.abhishek.fblogin.Activity;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.abhishek.fblogin.helper.AppController;
import com.example.abhishek.fblogin.helper.DataBaseHandler;
import com.example.abhishek.fblogin.API.PG;
import com.example.abhishek.fblogin.R;
import com.example.abhishek.fblogin.helper.Urls;
import com.example.abhishek.fblogin.Adapter.pgListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ExplorePGActivity extends AppCompatActivity {

    private DataBaseHandler db;
    RecyclerView pgRecyclerView;
    private static ArrayList<PG> pgList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_pg);
        setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db = new DataBaseHandler(getApplicationContext());
        pgList=db.getAllPGs();
        pgRecyclerView = (RecyclerView) findViewById(R.id.pg_recycler_view);
        pgRecyclerView.setLayoutManager(new LinearLayoutManager(pgRecyclerView.getContext()));
        pgListAdapter adapter = new pgListAdapter(ExplorePGActivity.this, pgList);
        pgRecyclerView.setAdapter(adapter);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) findViewById(R.id.search_pg);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setIconifiedByDefault(false);
        searchView.setQueryRefinementEnabled(true);
        searchView.setQueryHint("Type a name to search..");

       searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {


            @Override
            public boolean onQueryTextSubmit(String query) {
                Snackbar.make(findViewById(R.id.explorePg_relLayout), "No match found for " + query, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        getPgList();
    }

    private void getPgList() {
        Log.d("abhi", "In the get pg");
        StringRequest strReq = new StringRequest(Request.Method.GET, Urls.pgListUrls,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("abhi", "Got Response for Hostel List");
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            JSONArray jsonArray = jsonResponse.getJSONArray("hostels");
                            db.deletePGs();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Log.d("abhi","In the for loop");
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                PG pg = new PG(jsonObject.getInt("id"),jsonObject.getString("hostel_name"),jsonObject.getString("address"), jsonObject.getString("image_url"),jsonObject.getString("phone_no"));
                                db.addPG(pg);
                            }
                            pgList=db.getAllPGs();
                            pgListAdapter adapter = new pgListAdapter(ExplorePGActivity.this, pgList);
                            pgRecyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("abhi","Json Exception");
                            pgList=db.getAllPGs();
                            pgListAdapter adapter = new pgListAdapter(ExplorePGActivity.this, pgList);
                            pgRecyclerView.setAdapter(adapter);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("abhi", "Volley Error " + error.getMessage());
                Snackbar.make(findViewById(R.id.explorePg_relLayout), "Please check your connection!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                pgList=db.getAllPGs();
                pgListAdapter adapter = new pgListAdapter(ExplorePGActivity.this, pgList);
                pgRecyclerView.setAdapter(adapter);
            }
        });
        AppController.getInstance().addToRequestQueue(strReq, "req_hostelList");
    }
}