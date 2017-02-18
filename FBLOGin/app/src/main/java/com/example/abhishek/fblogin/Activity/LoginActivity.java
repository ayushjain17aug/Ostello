package com.example.abhishek.fblogin.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.abhishek.fblogin.API.User;
import com.example.abhishek.fblogin.R;
import com.example.abhishek.fblogin.helper.SQLiteHandler;
import com.example.abhishek.fblogin.helper.SessionManager;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 007;
    private SQLiteHandler db;
    private GoogleApiClient mGoogleApiClient;
    private SignInButton googleLoginButton;
    private Button btnSignOut;
    private AccessTokenTracker tokenTracker;
    private ProfileTracker profileTracker;
    private ProgressDialog mProgressDialog;
    private LoginButton fbLoginButton;
    private GoogleSignInResult result1;
    private String email;
    private String personName;
    private String photoUrl;
    private SessionManager session;
    private String user_id;
    private String type;
    private CallbackManager mcallbackManager;
    private User user;
    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.d("abhi", "OnSuccess");
            (session).setLogin(true);
            /*AccessToken accessToken = loginResult.getAccessToken();
            GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    Log.i("LoginActivity", response.toString());
                    // Get facebook data from login
                    //Bundle bFacebookData = getFacebookData(object);
                    //email=bFacebookData.getString("email");
                }
            });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "email,gender");
            request.setParameters(parameters);
            request.executeAsync();*/
            Profile profile = Profile.getCurrentProfile();
            if (profile != null) {
                user_id = (profile.getId());
                personName = profile.getName();
                photoUrl = profile.getProfilePictureUri(100, 100).toString();
                type = "facebook";
                Log.d("abhi", "In On Succcess Login " + type);
                user = new User(user_id, personName, email, photoUrl, type);
                db.addUser(user);
                updateUIfbLogin();
            }
        }

        @Override
        public void onCancel() {
            Snackbar.make(findViewById(R.id.rel_layout), "Login Attempt Cancelled!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

        }

        @Override
        public void onError(FacebookException error) {
            Snackbar.make(findViewById(R.id.rel_layout), error.getMessage().toString(), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            Log.d("abhi", "In OnError" + error.getMessage());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        session = new SessionManager(LoginActivity.this);
        db = new SQLiteHandler(getApplicationContext());
        Log.d("abhi", "In the On create");
        //FACEBOOK SDK
        FacebookSdk.sdkInitialize(getApplicationContext());
        mcallbackManager = CallbackManager.Factory.create();
        tokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    session.setLogin(false);
                    Log.d("abhi", "User Logged Out.");
                    db.deleteUsers();
                    updateUI(false);
                    //Do your task here after logout
                }
            }
        };
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {

            }
        };
        profileTracker.startTracking();
        tokenTracker.startTracking();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        googleLoginButton = (SignInButton) findViewById(R.id.google_login_button);
        btnSignOut = (Button) findViewById(R.id.btn_sign_out);
        fbLoginButton = (LoginButton) findViewById(R.id.fb_login_button);
        fbLoginButton.setReadPermissions("user_friends", "email");
        fbLoginButton.registerCallback(mcallbackManager, callback);
        googleLoginButton.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestProfile().requestId().requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        googleLoginButton.setSize(SignInButton.SIZE_STANDARD);
        googleLoginButton.setScopes(gso.getScopeArray());
        if (session.isLoggedIn()) {
            Log.d("abhi", "User is Logged in");
            if (db.getUser().getType().equals("google"))
                updateUI(true);
            else
                updateUIfbLogin();
        }
    }

    @Override
    public void onDestroy() {
        Log.d("abhi", "In On Destroy");
        tokenTracker.stopTracking();
        profileTracker.stopTracking();
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("abhi", "In onActivityResult");
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        mcallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /*private void revokeAccess() {
        Log.d("abhi", "In RevokeAccess");
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                updateUI(false);
            }
        });
    }*/

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("abhi", "On Connection Failed:" + connectionResult);
    }

    private void signIn() {
        Log.d("abhi", "In signIn()");
        session.setLogin(true);
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        session.setLogin(false);
        db.deleteUsers();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                updateUI(false);
            }
        });
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("abhi", "handleSignInResult : " + result.isSuccess());
        if (result.isSuccess()) {
            //Signed in successfully
            session.setLogin(true);
            GoogleSignInAccount acct = result.getSignInAccount();
            user_id = (acct.getId());
            personName = acct.getDisplayName();
            email = acct.getEmail();
            if (acct.getPhotoUrl() != null)
                photoUrl = acct.getPhotoUrl().toString();
            type = "google";
            user = new User(user_id, personName, email, photoUrl, type);
            db.addUser(user);
            updateUI(true);
        } else if (!result.isSuccess() && !session.isLoggedIn()) {
            updateUI(false);
        }
    }

    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.google_login_button:
                signIn();
                break;
            case R.id.btn_sign_out:
                signOut();
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d("abhi", "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading");
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            googleLoginButton.setVisibility(View.GONE);
            btnSignOut.setVisibility(View.VISIBLE);
            fbLoginButton.setVisibility(View.GONE);
            btnSignOut.setEnabled(true);
            googleLoginButton.setEnabled(false);
            Log.d("abhi", "True Signed in");
        } else {
            Log.d("abhi", "False Not Signed in");
            googleLoginButton.setVisibility(View.VISIBLE);
            fbLoginButton.setVisibility(View.VISIBLE);
            btnSignOut.setVisibility(View.GONE);
            btnSignOut.setEnabled(false);
            googleLoginButton.setEnabled(true);

        }
    }

    private Bundle getFacebookData(JSONObject object) {

        try {
            Bundle bundle = new Bundle();
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void updateUIfbLogin() {
        googleLoginButton.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        result1 = Auth.GoogleSignInApi.getSignInResultFromIntent(new Intent());
    }
}