package com.example.abhishek.fblogin.Activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.abhishek.fblogin.API.PG;
import com.example.abhishek.fblogin.Adapter.pgListAdapter;
import com.example.abhishek.fblogin.R;
import com.example.abhishek.fblogin.helper.AppController;
import com.example.abhishek.fblogin.helper.DataBaseHandler;
import com.example.abhishek.fblogin.helper.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ExplorePGActivity extends AppCompatActivity {

    private DataBaseHandler db;
    RecyclerView pgRecyclerView;
    private static ArrayList<PG> pgList;
    private String sharing = null;
    private String gender = null;
    private pgListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_pg);
        setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db = new DataBaseHandler(getApplicationContext());
        pgList = db.getAllPGs();
        pgRecyclerView = (RecyclerView) findViewById(R.id.pg_recycler_view);
        pgRecyclerView.setLayoutManager(new LinearLayoutManager(pgRecyclerView.getContext()));
        adapter = new pgListAdapter(ExplorePGActivity.this, pgList, db.getFavPGs());
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
                                Log.d("abhi", "In the for loop");
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                PG pg = new PG(jsonObject.getInt("id"), jsonObject.getString("hostel_name"), jsonObject.getString("image_url1"),
                                        jsonObject.getString("image_url2"), jsonObject.getString("image_url3"),jsonObject.getString("image_url14"),
                                        jsonObject.getString("image_url5"), jsonObject.getString("rental1"), jsonObject.getString("rental2")
                                        , jsonObject.getString("rental3"), jsonObject.getString("address"),
                                        jsonObject.getString("phone_no"), jsonObject.getString("amenties"), jsonObject.getString("cancellation_policy")
                                        , jsonObject.getString("hostel_rule"), jsonObject.getString("gender"), jsonObject.getString("sharing"));
                                db.addPG(pg);
                            }
                            pgList = db.getAllPGs();
                            pgListAdapter adapter = new pgListAdapter(ExplorePGActivity.this, pgList, db.getFavPGs());
                            pgRecyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("abhi", "Json Exception");
                            pgList = db.getAllPGs();
                            pgListAdapter adapter = new pgListAdapter(ExplorePGActivity.this, pgList, db.getFavPGs());
                            pgRecyclerView.setAdapter(adapter);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("abhi", "Volley Error " + error.getMessage());
                Snackbar.make(findViewById(R.id.explorePg_relLayout), "Please check your connection!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                pgList = db.getAllPGs();
                pgListAdapter adapter = new pgListAdapter(ExplorePGActivity.this, pgList, db.getFavPGs());
                pgRecyclerView.setAdapter(adapter);
            }
        });
        AppController.getInstance().addToRequestQueue(strReq, "req_hostelList");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.explore_pg, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.filter) {
            Log.d("abhi", "adding filter");
            addFilter();
        }
        return true;
    }

    private void addFilter() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Filter");
        final View view = getLayoutInflater().inflate(R.layout.filter_pop_up, null);
        setButtons(view, gender, sharing);
        builder.setView(view);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pgType(view);
                SharingSelect(view);
                ArrayList<PG> pgList1 = new ArrayList<PG>();
                if (gender == null && sharing != null)
                    pgList1 = db.getSharingBasedHostel(sharing);
                else if (gender != null && sharing == null)
                    pgList1 = db.getGenderBasedHostel(gender);
                else if (gender != null && sharing != null)
                    pgList1 = db.getGenderAndSharingBasedHostel(gender, sharing);
                if (pgList1 != null) {
                    adapter = new pgListAdapter(ExplorePGActivity.this, pgList1, db.getFavPGs());
                    pgRecyclerView.setAdapter(adapter);
                }
                Log.d("abhi", gender + " " + sharing);
            }
        });
        builder.setNeutralButton("Clear Filter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                gender = null;
                sharing = null;
                pgList = db.getAllPGs();
                adapter = new pgListAdapter(ExplorePGActivity.this, pgList, db.getFavPGs());
                pgRecyclerView.setAdapter(adapter);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void setButtons(View view, String gender, String sharing) {
        final RadioButton female = (RadioButton) view.findViewById(R.id.female);
        final RadioButton male = (RadioButton) view.findViewById(R.id.male);
        final RadioButton sharingAny = (RadioButton) view.findViewById(R.id.Any);
        final RadioButton sharingOne = (RadioButton) view.findViewById(R.id.sharing1);
        final RadioButton sharingTwo = (RadioButton) view.findViewById(R.id.sharing2);
        final RadioButton sharingThreePlus = (RadioButton) view.findViewById(R.id.sharing21);
        if (gender.equals("male"))
            male.setChecked(true);
        else if (gender.equals("female"))
            female.setChecked(true);
        if (sharing.equals("Any"))
            sharingAny.setChecked(true);
        else if (sharing.equals("1"))
            sharingOne.setChecked(true);
        else if (sharing.equals("2"))
            sharingTwo.setChecked(true);
        else if (sharing.equals("3+"))
            sharingThreePlus.setChecked(true);
    }

    public void pgType(View view) {
        final RadioButton female = (RadioButton) view.findViewById(R.id.female);
        final RadioButton male = (RadioButton) view.findViewById(R.id.male);
        if (female.isChecked())
            gender = "female";
        else if (male.isChecked())
            gender = "male";
    }

    public void SharingSelect(View view) {
        final RadioButton sharingAny = (RadioButton) view.findViewById(R.id.Any);
        final RadioButton sharingOne = (RadioButton) view.findViewById(R.id.sharing1);
        final RadioButton sharingTwo = (RadioButton) view.findViewById(R.id.sharing2);
        final RadioButton sharingThreePlus = (RadioButton) view.findViewById(R.id.sharing21);
        if (sharingAny.isChecked())
            sharing = "Any";
        else if (sharingOne.isChecked())
            sharing = "1";
        else if (sharingTwo.isChecked())
            sharing = "2";
        else if (sharingThreePlus.isChecked())
            sharing = "3+";
    }
}