package com.example.abhishek.fblogin.Activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.abhishek.fblogin.API.MyProfile;
import com.example.abhishek.fblogin.R;
import com.example.abhishek.fblogin.helper.DataBaseHandler;
import com.example.abhishek.fblogin.helper.SQLiteHandler;
import com.example.abhishek.fblogin.helper.SessionManager;

public class MyProfileActivity extends AppCompatActivity {
    String gender=null;
    private DataBaseHandler db;
    private EditText name, age, phone, email;
    private MyProfile myProfile;
    private RadioButton Male,Female;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db = new DataBaseHandler(getApplicationContext());

        name = (EditText) findViewById(R.id.name);
        age = (EditText) findViewById(R.id.age);
        phone = (EditText) findViewById(R.id.phone_no);
        email = (EditText) findViewById(R.id.email);
        Male=(RadioButton)findViewById(R.id.radioButton2);
        Female=(RadioButton)findViewById(R.id.radioButton3);

        myProfile = db.getMyProfile();
        if (myProfile == null) {
            Snackbar.make(findViewById(R.id.my_profile_relLayout), "Please Enter the Details and click Save Button!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
        else {
            name.setText(myProfile.getName());
            if (myProfile.getAge() != null)
                age.setText(myProfile.getAge());
            if (myProfile.getPhone() != null)
                phone.setText(myProfile.getPhone());
            if (myProfile.getEmail() != null)
                email.setText(myProfile.getEmail());
            Log.d("abhi", "Setting Gender as Male" + myProfile.getGender());
            if (myProfile.getGender().toString().equals("Male")) {
                Male.setChecked(true);
                Female.setChecked(false);
            } else {
                Female.setChecked(true);
                Male.setChecked(false);
            }
        }
    }

    public void GenderSelect(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radioButton3:
                if (checked)
                    gender = "Female";
                break;
            case R.id.radioButton2:
                if (checked)
                    gender = "Male";
                break;
        }
    }

    public void savingProfile(View view) {
        Log.d("abhi", "in savingProfile " + gender+" "+name.getText().toString()+" "+email.getText().toString());
        if (new SessionManager(this).isLoggedIn()) {
            if (!name.getText().toString().equals(null)) {
                if (!phone.getText().toString().equals(null) && phone.getText().toString().trim().length() == 10) {
                    if (!age.getText().toString().equals(null)) {
                        if (!email.getText().toString().equals(null)&&email.getText().toString().indexOf("@")!=-1&&email.getText().toString().trim().endsWith(".com")) {
                            if (gender != null) {
                                MyProfile myProfile1 = new MyProfile(new SQLiteHandler(this).getUser().getUser_id(), name.getText().toString(), phone.getText().toString().trim(),
                                        age.getText().toString(), email.getText().toString(), gender);
                                db.deleteMyProfile();
                                db.addProfile(myProfile1);
                                Snackbar.make(findViewById(R.id.my_profile_relLayout), "Profile Added Succcessfully!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            } else
                                Snackbar.make(findViewById(R.id.my_profile_relLayout), "Select a Gender!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        }else
                            Snackbar.make(findViewById(R.id.my_profile_relLayout), "Enter the correct email!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    } else
                        Snackbar.make(findViewById(R.id.my_profile_relLayout), "Enter the age!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                } else
                    Snackbar.make(findViewById(R.id.my_profile_relLayout), "Enter the Correct Phone Number of 10 digits!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            } else
                Snackbar.make(findViewById(R.id.my_profile_relLayout), "Enter the name!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        } else
            Snackbar.make(findViewById(R.id.my_profile_relLayout), "Please Login to Create Profile!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }
}