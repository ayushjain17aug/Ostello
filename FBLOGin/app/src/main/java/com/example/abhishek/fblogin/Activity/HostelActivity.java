package com.example.abhishek.fblogin.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.abhishek.fblogin.API.PG;
import com.example.abhishek.fblogin.Fragment.swipe_image;
import com.example.abhishek.fblogin.R;
import com.example.abhishek.fblogin.helper.DataBaseHandler;

public class HostelActivity extends AppCompatActivity {


    private String hostelId;
    private TextView result;
    private PG pg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hostel);
        hostelId = getIntent().getData().toString();
        result = (TextView) findViewById(R.id.result);


        FragmentTransaction top_fragment = getSupportFragmentManager().beginTransaction();
        swipe_image frag = new swipe_image();
        Bundle data = new Bundle();
        data.putString("activityname","HostelActivity.java");
        data.putString("hostelId",hostelId);
        frag.setArguments(data);
        top_fragment.replace(R.id.top_placeholder,frag);
        top_fragment.commit();


        DataBaseHandler db = new DataBaseHandler(getApplicationContext());
        pg = db.getPG(hostelId);
        db.close();
        result.setText(pg.getImage_url1());


    }
}
