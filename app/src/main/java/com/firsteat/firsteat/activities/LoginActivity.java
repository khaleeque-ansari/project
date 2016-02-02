package com.firsteat.firsteat.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.firsteat.firsteat.R;
import com.firsteat.firsteat.app.AppController;
import com.firsteat.firsteat.fragments.NavigationHeader;
import com.firsteat.firsteat.models.RegisterUser;
import com.firsteat.firsteat.models.UserDetails;
import com.firsteat.firsteat.utils.ChangeFragment;
import com.firsteat.firsteat.utils.Constants;
import com.firsteat.firsteat.utils.DialogUtils;
import com.firsteat.firsteat.utils.TagsPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    private static final String TAG = LoginActivity.class.getSimpleName();
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private ProgressDialog pd;
    private String email;
    private String password;
    private SharedPreferences prefs;
    private Context context;
    private String name;
    private SharedPreferences.Editor preEditor;
    private String picture;
    private boolean isMobileRegistered;
    private Button btnLoginSignup;
    private ProgressBar progressBarLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context=LoginActivity.this;
        prefs = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        preEditor=prefs.edit();
        progressBarLogin = (ProgressBar) findViewById(R.id.progressBarLogin);

        LoginManager logingManager=LoginManager.getInstance();
        logingManager.logOut();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        btnLoginSignup = (Button) findViewById(R.id.btnLoginSignup);

        loginButton.setReadPermissions("user_friends", "public_profile email");
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        /*
        * forgot password
        * */
        findViewById(R.id.txtLogin_forgotPassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogForgotPassword();
            }
        });


        //Testing
        btnLoginSignup.setText(Html.fromHtml("<p >Not a member yet? <u style=\"color:blue;\">Register</u></p>"));
        btnLoginSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle extras = getIntent().getExtras();
                String classname = extras.getString(TagsPreferences.NEXT);
                String intentType = extras.getString(TagsPreferences.INTENT_TYPE);
                Intent tosignup = new Intent(LoginActivity.this, SignUp.class);
                tosignup.putExtra(TagsPreferences.NEXT, classname);
                tosignup.putExtra(TagsPreferences.INTENT_TYPE, intentType);
                progressBarLogin.setVisibility(View.INVISIBLE);
                startActivity(tosignup);

            }
        });

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        if (AccessToken.getCurrentAccessToken() != null) {
                            //request facebook user data by graph search
                            pd=new ProgressDialog(LoginActivity.this);
                            pd.setMessage("Please wait.... \nfetching data");
                            pd.setCancelable(false);
                            pd.show();
                            progressBarLogin.setVisibility(View.VISIBLE);
                            requestFacebookData();

                        }
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException error) {

                    }
                });
            // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBarLogin.setVisibility(View.VISIBLE);
                attemptLogin();

            }
        });

        mLoginFormView = findViewById(R.id.login_form);
//        mProgressView = findViewById(R.id.login_progress);
    }

    /*
    * Open next Activity according to extra data
    * */
    private void nextActivity() {
        try {
            Bundle extras = getIntent().getExtras();
            String intentType=extras.getString(TagsPreferences.INTENT_TYPE);
            Class clazz= Class.forName(extras.getString(TagsPreferences.NEXT));

            if(intentType!=null){
                if(intentType.equalsIgnoreCase(TagsPreferences.INTENT_TYPE_FRAGMENT)){
                    Fragment frag= (Fragment) clazz.newInstance();
                    ChangeFragment.changeFragment(getSupportFragmentManager(), R.id.frame_baseFragmentActivity, frag);
                }
            }
            else {
                progressBarLogin.setVisibility(View.INVISIBLE);
                Intent i = new Intent(LoginActivity.this, clazz);
                startActivity(i);
                finish();
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        float fbIconScale = 1.45F;
        Drawable drawable = LoginActivity.this.getResources().getDrawable(
                com.facebook.R.drawable.com_facebook_button_icon);
        drawable.setBounds(0, 0, (int)(drawable.getIntrinsicWidth()*fbIconScale),
                (int)(drawable.getIntrinsicHeight()*fbIconScale));
        loginButton.setCompoundDrawables(drawable, null, null, null);
        loginButton.setCompoundDrawablePadding(LoginActivity.this.getResources().
                getDimensionPixelSize(R.dimen.fb_margin_override_textpadding));
        loginButton.setPadding(
                LoginActivity.this.getResources().getDimensionPixelSize(
                        R.dimen.fb_margin_override_lr),
                LoginActivity.this.getResources().getDimensionPixelSize(
                        R.dimen.fb_margin_override_top),
                0,
                LoginActivity.this.getResources().getDimensionPixelSize(
                        R.dimen.fb_margin_override_bottom));
    }

    private void requestFacebookData() {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d(TAG, "graph search complete");
                        pd.dismiss();
                        JSONObject json = response.getJSONObject();
                        Log.d(TAG, "graph json: " + json.toString());
                        try {
                            if (json != null) {
                                preEditor.putString(TagsPreferences.PROFILE_USER_NAME, json.getString("name")).commit();
                                preEditor.putString(TagsPreferences.PROFILE_USER_EMAIL, json.getString("email")).commit();
                                preEditor.putString(TagsPreferences.PROFILE_PICTURE, json.getJSONObject("picture").getJSONObject("data").getString("url")).commit();

                                name = prefs.getString(TagsPreferences.PROFILE_NAME, json.getString("name"));
                                email = prefs.getString(TagsPreferences.PROFILE_EMAIL, json.getString("email"));
                                picture = prefs.getString(TagsPreferences.PROFILE_PICTURE, json.getJSONObject("picture").getJSONObject("data").getString("url"));


                                Log.d(TAG, "name:" + json.getString("name") +
                                        "\npicture:" + json.getJSONObject("picture").getJSONObject("data").getString("url"));

                                checkEmailExistence(email);

//                                registerUser(name,
//                                        "",
//                                        email,
//                                        "12345");
//
                            } else
                                Log.d(TAG, "fb graph json is null");

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,name,link,picture.width(400).height(400)");
        request.setParameters(parameters);

        request.executeAsync();

    }

    private void checkEmailExistence(String email) {
        String url=Constants.getUrlCheckEmail(email);
        JsonObjectRequest requestCheck=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG,"checkEmailExistence :response"+response.toString());
                    try {
                        if(response.getString("status").equalsIgnoreCase("1")){
                            prefs.edit().putBoolean(TagsPreferences.LOGIN_STATUS,true).commit();
                            prefs.edit().putString(TagsPreferences.PROFILE_USER_ID,String.valueOf(response.getInt("user_id"))).commit();
                            getUsersDetails(prefs.getString(TagsPreferences.PROFILE_USER_ID, "0"));
                        }else {
                            dialogFacebookRegistration();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,"checkEmailExistence :error"+error.toString());

            }
        });
        AppController.getInstance().addToRequestQueue(requestCheck);
    }

    private boolean dialogFacebookRegistration() {
        isMobileRegistered=false;
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

        builder.setCancelable(false)
                .setTitle("Plese Enter Data")
                .setMessage(""+prefs.getString(TagsPreferences.PROFILE_EMAIL,""))
                .setView(R.layout.dialog_facebook_registration) //<-- layout containing EditText
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DialogUtils.showDialog(LoginActivity.this,"You are not logged in Mobile number is madetory while login with facebook");
                        prefs.edit().putBoolean(TagsPreferences.LOGIN_STATUS,false).commit();
                        LoginManager.getInstance().logOut();
                    }
                })
                .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Button theButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        theButton.setOnClickListener(new CustomListener(alertDialog));

        if(!(prefs.getString(TagsPreferences.PROFILE_NUMBER,"").equalsIgnoreCase(""))){
            isMobileRegistered=true;
        }
        return isMobileRegistered;
    }

    /*
    * dialog forgot password
    * */
    private boolean dialogForgotPassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

        builder.setCancelable(false)
                .setMessage("Please Enter data")
                .setView(R.layout.dialog_forgot_password) //<-- layout containing EditText
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Button theButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        theButton.setOnClickListener(new CustomListener(alertDialog));

        if(!(prefs.getString(TagsPreferences.PROFILE_NUMBER,"").equalsIgnoreCase(""))){
            isMobileRegistered=true;
        }
        return isMobileRegistered;
    }



    private void registerUser(String name,String mobile, String email, String password) {
        String url= Constants.getUrlRegsitration(email, password, name, mobile, Constants.DEVICE_ID, Constants.ADVERT_ID, null);
        Log.d(TAG, "registration url: " + url);
        final ProgressDialog pd=new ProgressDialog(LoginActivity.this);
        pd.setMessage("New User is being registered Please wait....");
        pd.setCancelable(false);
        pd.show();
        JsonObjectRequest registrationrequestFacebook=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pd.dismiss();
                Log.d(TAG, "registration response:" + response.toString());

                Gson gson=new Gson();
                RegisterUser user=gson.fromJson(response.toString(), RegisterUser.class);

                Toast.makeText(context,user.getData().getRegister(),Toast.LENGTH_SHORT).show();
                Log.d(TAG, user.getData().getRegister());

//                int status=(user.getStatus().equalsIgnoreCase("0"))?0:(user.getStatus().equalsIgnoreCase("1"))?1:2;
//                switch (status){
//                    case 0:
//                        break;
//                    case 1:
//                        break;
//                    case 2:
//                        break;
//                }
                preEditor.putString(TagsPreferences.PROFILE_USER_ID, String.valueOf(user.getData().getUser_id()));
                if(user.getData().getKeys()!=null){
                    preEditor.putString(TagsPreferences.PROFILE_REFERAL_CODE, user.getData().getKeys().getReferal_code());
                }

//                        Constants.USER_ROLE=response.getJSONObject("data").getString("role");

                        /*
                        * Update login status to true in preferences
                        * */
                preEditor.putBoolean(TagsPreferences.LOGIN_STATUS,true).commit();
//                        updateNavHeader();
                getUsersDetails(prefs.getString(TagsPreferences.PROFILE_USER_ID, "0"));


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Log.d(TAG,"Registration error :"+error.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(registrationrequestFacebook);
    }

    private void updateNavHeader() {
        NavigationHeader header=new NavigationHeader();
        header.updateHeader(this.picture, this.name);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            return true;
//        }
//        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
//            return true;
//        }
//        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
//            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
//                    .setAction(android.R.string.ok, new View.OnClickListener() {
//                        @Override
//                        @TargetApi(Build.VERSION_CODES.M)
//                        public void onClick(View v) {
//                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
//                        }
//                    });
//        } else {
//            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
//        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        email = mEmailView.getText().toString();
        password = mPasswordView.getText().toString();

        boolean error = false;
        boolean mandetoryDone = true;
        View focusView = null;

        //check for the mandetory field name
        int[] mandetoryFields={R.id.password,
                R.id.email};
        for(int id:mandetoryFields){
            EditText v= (EditText) findViewById(id);
            if(v.getText().toString().trim().length()<1){
                v.setError("This field is mandetory");
                focusView = v;
                error = true;
                mandetoryDone=false;
            }
        }

        if(mandetoryDone){
            // Check for a valid email address.
            if (!isEmailValid(email)) {
                mEmailView.setError(getString(R.string.error_invalid_email));
                focusView = mEmailView;
                error = true;
            }
            // Check for a valid password.
            if (!isPasswordValid(password)) {
                mPasswordView.setError("The password is invalid");
                focusView = mPasswordView;
                error = true;
            }
        }

        if (error) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            requestLogin();

        }
    }

    private void requestLogin() {
        final ProgressDialog pd=new ProgressDialog(LoginActivity.this);
        pd.setMessage("Login user\nPlease wait....");
        pd.setCancelable(false);
        pd.show();
        String url= Constants.getUrlLogin(email,password,Constants.DEVICE_ID,Constants.ADVERT_ID);
        JsonObjectRequest login_request=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pd.dismiss();
                Log.d(TAG, "login response: " + response.toString());
                try {
                    preEditor.putBoolean(TagsPreferences.LOGIN_STATUS, true).commit();
                    preEditor.putString(TagsPreferences.PROFILE_USER_ID, response.getJSONObject("data").getString("user_id")).commit();

                    String authentication=response.getJSONObject("data").getString("authentication");
                    if(authentication.equalsIgnoreCase("successfull")){

                        /*
                        * get User's personal detail
                        * */
                        getUsersDetails(prefs.getString(TagsPreferences.PROFILE_USER_ID,"0"));
                        Toast.makeText(LoginActivity.this,"Successful",Toast.LENGTH_LONG).show();


                    }else{
                        Toast.makeText(LoginActivity.this,""+authentication+" try to login again",Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    pd.dismiss();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Log.d(TAG,"login volley error: "+error.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(login_request);
    }


    /*
    * Get user's detail
    * */
    private void getUsersDetails(String userId) {
        String url=Constants.getUrlUsersDetail(userId);
        JsonObjectRequest requestUsersDetail= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG,"getUsersDetails response:"+response.toString());
                Gson gson=new Gson();
                UserDetails userDetails=gson.fromJson(response.toString(), UserDetails.class);
                preEditor.putString(TagsPreferences.PROFILE_USER_ID,userDetails.getData().get(0).getId()).commit();
                preEditor.putString(TagsPreferences.PROFILE_USER_EMAIL,userDetails.getData().get(0).getUser_email()).commit();
                preEditor.putString(TagsPreferences.PROFILE_USER_NAME,userDetails.getData().get(0).getFull_name()).commit();
                preEditor.putString(TagsPreferences.PROFILE_USER_MOBILE,userDetails.getData().get(0).getMobile()).commit();
                preEditor.putString(TagsPreferences.PROFILE_USER_REFERAL_CODE,userDetails.getData().get(0).getReferal_code()).commit();
                if(userDetails.getData().get(0).getUserPoints()!=null){
                    preEditor.putString(TagsPreferences.PROFILE_USER_REFERAL_POINT,userDetails.getData().get(0).getUserPoints().getTotal_points()).commit();
                    preEditor.putString(TagsPreferences.PROFILE_USER_REDEEM_POINT,userDetails.getData().get(0).getUserPoints().getTotal_points()).commit();
                }else{
                    preEditor.putString(TagsPreferences.PROFILE_USER_REFERAL_POINT,"0").commit();
                    preEditor.putString(TagsPreferences.PROFILE_USER_REDEEM_POINT,"0").commit();
                }

                preEditor.putString(TagsPreferences.PROFILE_USER_ROLE,userDetails.getData().get(0).getUserRole().getRole_name()).commit();


//                MainActivity.updateHeader();
                nextActivity();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,"getUsersDetails error:"+error.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(requestUsersDetail);
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }


    /*
    * custom listener for edittext
    * */
    class CustomListener implements View.OnClickListener {
        private final Dialog dialog;
        public CustomListener(Dialog dialog) {
            this.dialog = dialog;
        }
        @Override
        public void onClick(View v) {
            boolean error=false;
            EditText mMobileNumber= (EditText) dialog.findViewById(R.id.edtMobileReg);
            EditText mNewPass= (EditText) dialog.findViewById(R.id.edtNewPass);
            EditText mConfirmNewPass= (EditText) dialog.findViewById(R.id.edtConfirmNewPass);

            String mMobile = mMobileNumber.getText().toString();
            String mPass = mNewPass.getText().toString();
            String mConfirmPass = mNewPass.getText().toString();
            //check for mandetory fields
            int[] mandetoryFields={
                    R.id.edtMobileReg,
                    R.id.edtNewPass,
                    R.id.edtConfirmNewPass
            };
            for(int id :mandetoryFields){
                EditText edt= (EditText) dialog.findViewById(id);
                if(TextUtils.isEmpty(edt.getText().toString().trim())){
                    edt.setError("This field is mandetory");
                    error = true;
                }
            }

            //check for errors
            if(!error){

                if(mMobileNumber.getText().toString().trim().length()<10||
                        mMobileNumber.getText().toString().trim().length()>10){
                    mMobileNumber.setError("Please enter a 10 digit number");
                }else if(!(mNewPass.getText().toString().trim().equalsIgnoreCase(mConfirmNewPass.getText().toString().trim()))){
                    mConfirmNewPass.setError("Password do not match");
                }else{
                    preEditor.putString(TagsPreferences.PROFILE_NUMBER, mMobileNumber.getText().toString().trim()).commit();
                    Log.d(TAG, "mobile number " + prefs.getString(TagsPreferences.PROFILE_NUMBER, ""));
                    registerUser(name,
                            prefs.getString(TagsPreferences.PROFILE_NUMBER, ""),
                            email,
                            mNewPass.getText().toString().trim());
                    dialog.dismiss();
                }
            }
        }
    }
}

