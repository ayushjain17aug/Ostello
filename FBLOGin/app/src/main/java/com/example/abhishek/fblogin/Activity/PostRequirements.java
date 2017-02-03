package com.example.abhishek.fblogin.Activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.abhishek.fblogin.R;

import java.util.ArrayList;
import java.util.List;

public class PostRequirements extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private SearchManager searchManager;
    private TextView location;
    private Spinner citySpinner;
    private String city = "Raipur";
    private String gender;
    private String sharing;
    private String budget;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_requirements);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        location = (TextView) findViewById(R.id.location);
        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) findViewById(R.id.search_location);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setIconifiedByDefault(false);
        searchView.setQueryRefinementEnabled(true);

        citySpinner = (Spinner) findViewById(R.id.spinner);
        citySpinner.setOnItemSelectedListener(this);
        List<String> cities = new ArrayList<String>();
        cities.add("Raipur");
        cities.add("Bhilai");
        cities.add("Bilaspur");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cities);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(dataAdapter);

        searchView.setQueryHint("Type a location to search..");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Snackbar.make(findViewById(R.id.post_req_relLayt), "No match found for " + query, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        handleIntent(getIntent());
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
            location.setText(intent.getData().toString());
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        city = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }


    public void pgType(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.female:
                if (checked)
                    gender = "Female";
                break;
            case R.id.male:
                if (checked)
                    gender = "Male";
                break;
        }
    }

    public void SharingSelect(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.Any:
                if (checked)
                    sharing = "Any";
                break;
            case R.id.sharing1:
                if (checked)
                    sharing = "1";
                break;
            case R.id.sharing2:
                if (checked)
                    sharing = "2";
                break;
            case R.id.sharing21:
                if (checked)
                    sharing = "2+";
                break;
        }
    }

    public void BudgetSelect(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.budget1:
                if (checked)
                    budget = "1";
                break;
            case R.id.budget2:
                if (checked)
                    budget = "2";
                break;
            case R.id.budget3:
                if (checked)
                    budget = "3";
                break;
            case R.id.budget4:
                if (checked)
                    budget = "4";
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    public void submit(View view) {

    }
}