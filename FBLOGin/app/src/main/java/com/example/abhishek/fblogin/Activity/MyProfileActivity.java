package com.example.abhishek.fblogin.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.abhishek.fblogin.API.MyProfile;
import com.example.abhishek.fblogin.R;
import com.example.abhishek.fblogin.helper.DataBaseHandler;
import com.example.abhishek.fblogin.helper.SQLiteHandler;
import com.example.abhishek.fblogin.helper.SessionManager;
import com.example.abhishek.fblogin.util.RoundedImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MyProfileActivity extends AppCompatActivity {
    String gender = null,image=null;
    private DataBaseHandler db;
    private EditText name, age, phone, email;
    private MyProfile myProfile;
    private RoundedImageView profile_pic;
    private RadioButton Male, Female;
    public static final int GET_FROM_GALLERY = 1;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;

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
        Male = (RadioButton) findViewById(R.id.radioButton2);
        Female = (RadioButton) findViewById(R.id.radioButton3);
        profile_pic = (RoundedImageView) findViewById(R.id.my_profile_pic);
        myProfile = db.getMyProfile();
        if (myProfile == null) {
            Snackbar.make(findViewById(R.id.my_profile_relLayout), "Please Enter the Details and click Save Button!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        } else {
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
            if(myProfile.getImage()!=null)
                profile_pic.setImageBitmap(decodeBase64(myProfile.getImage()));
        }

        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                    // app-defined int constant that should be quite unique
                    return;
                }
                //Snackbar.make(findViewById(R.id.my_profile_relLayout), "Clicked Image Button Succcessfully!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
            }
        });
    }

    public void abhi(View v) {
        Snackbar.make(findViewById(R.id.my_profile_relLayout), "Clicked Image Button Succcessfully!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Detects request codes
        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Log.d("abhi","URI "+selectedImage);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                image=encodeToBase64(bitmap, Bitmap.CompressFormat.PNG,100);
                profile_pic.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
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
        Log.d("abhi", "in savingProfile " + gender + " " + name.getText().toString() + " " + email.getText().toString());
        if (new SessionManager(this).isLoggedIn()) {
            if (!name.getText().toString().equals(null)) {
                if (!phone.getText().toString().equals(null) && phone.getText().toString().trim().length() == 10) {
                    if (!age.getText().toString().equals(null)) {
                        if (!email.getText().toString().equals(null) && email.getText().toString().indexOf("@") != -1 && email.getText().toString().trim().endsWith(".com")) {
                            if (gender != null) {
                                MyProfile myProfile1 = new MyProfile(new SQLiteHandler(this).getUser().getUser_id(), name.getText().toString(), phone.getText().toString().trim(),
                                        age.getText().toString(), email.getText().toString(), gender, image);
                                db.deleteMyProfile();
                                db.addProfile(myProfile1);
                                Snackbar.make(findViewById(R.id.my_profile_relLayout), "Profile Added Succcessfully!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            } else
                                Snackbar.make(findViewById(R.id.my_profile_relLayout), "Select a Gender!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        } else
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


    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }





}