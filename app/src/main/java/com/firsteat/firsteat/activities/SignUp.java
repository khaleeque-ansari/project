package com.firsteat.firsteat.activities;

import android.app.LoaderManager.LoaderCallbacks;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.firsteat.firsteat.R;
import com.firsteat.firsteat.app.AppController;
import com.firsteat.firsteat.models.RegisterUser;
import com.firsteat.firsteat.models.UserDetails;
import com.firsteat.firsteat.utils.Constants;
import com.firsteat.firsteat.utils.DialogUtils;
import com.firsteat.firsteat.utils.TagsPreferences;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class SignUp extends AppCompatActivity implements LoaderCallbacks<Cursor> {

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
    private static final String TAG =SignUp.class.getSimpleName();
    private static Context context;


    // UI references.
    private AutoCompleteTextView tilSignupEmail;
    private EditText tilSignupName,tilSignupPass1,tilSignupPass2,tilSignupReferal;
    private View mLoginFormView;
    private SharedPreferences.Editor preEditor;
    private SharedPreferences prefs;
    private EditText tilSignupMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.context =SignUp.this;

        prefs = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        preEditor=prefs.edit();

        // Set up the login form.
        tilSignupEmail = (AutoCompleteTextView) findViewById(R.id.tilSignupEmail);
        populateAutoComplete();

        tilSignupPass1 = (EditText) findViewById(R.id.tilSignupPass1);
        tilSignupName = (EditText) findViewById(R.id.tilSignupName);
        tilSignupPass2 = (EditText) findViewById(R.id.tilSignupPass2);
        tilSignupReferal = (EditText) findViewById(R.id.tilSignupReferal);
        tilSignupMobile = (EditText) findViewById(R.id.tilSignupMobile);
        tilSignupPass2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    Toast.makeText(SignUp.this,"yes the logic is working",Toast.LENGTH_SHORT).show();
                  attemptSignUp();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.btnSignup);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignUp();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);

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
//            Snackbar.make(tilSignupEmail, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
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
    private void attemptSignUp() {

        // Reset errors.
        tilSignupEmail.setError(null);
        tilSignupName.setError(null);
        tilSignupPass2.setError(null);
        tilSignupPass1.setError(null);

        // Store values at the time of the login attempt.
        String email = tilSignupEmail.getText().toString().trim();
        String password = tilSignupPass1.getText().toString().trim();
        String password2 = tilSignupPass2.getText().toString().trim();
        String name = tilSignupName.getText().toString().trim();
        String referalID=tilSignupReferal.getText().toString().trim();
        String mobile=tilSignupMobile.getText().toString().trim();

        boolean error = false;
        boolean mandetoryDone=true;//check for the mandetory fields
        View focusView = null;



        //check for the mandetory field name
        int[] mandetoryFields={R.id.tilSignupPass2,
                R.id.tilSignupPass1,
                R.id.tilSignupEmail,
                R.id.tilSignupName,
                R.id.tilSignupMobile};
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
            /*
            TIPS: use your validation to the input field from bottom to top
            * */
            //check for the password match
            if(!password.equals(password2)){
                tilSignupPass2.setError("Passwords do not match");
                focusView = tilSignupPass2;
                error = true;
            }
            // Check for a valid password, if the user entered one.
            if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
                tilSignupPass1.setError(getString(R.string.error_invalid_password));
                focusView = tilSignupPass1;
                error = true;
            }

            if (!isEmailValid(email)) {
                tilSignupEmail.setError(getString(R.string.error_invalid_email));
                focusView = tilSignupEmail;
                error = true;
            }
            // if no referal code entered the replace blank with NULL
            // its mandetory and case sensetive
            if(referalID.length()<1)
            {
                referalID="NULL";
            }
        }

        if (error) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            // do a async task here
            registerUser(name,mobile,email,password,referalID);
        }
    }

    private void registerUser(String name, String mobile,String email, String password,String referalID) {
        String url= Constants.getUrlRegsitration(email,password,name,mobile,Constants.DEVICE_ID,Constants.ADVERT_ID,referalID);
        Log.d(TAG, "registration url: " + url);
        final ProgressDialog pd=new ProgressDialog(SignUp.this);
        pd.setCancelable(false);
        pd.setMessage("New User is being registered Please wait....");
        pd.show();
        JsonObjectRequest registrationrequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pd.dismiss();
                Log.d(TAG,"registration response:"+response.toString());
                Gson gson=new Gson();
                RegisterUser user=gson.fromJson(response.toString(), RegisterUser.class);

                if(user.getStatus().equalsIgnoreCase("0")){
                    Toast.makeText(context,user.getData().getRegister(),Toast.LENGTH_SHORT).show();
                    Log.d(TAG, user.getData().getRegister());

                    preEditor.putString(TagsPreferences.PROFILE_USER_ID, String.valueOf(user.getData().getUser_id())).commit();
                    if(user.getData().getKeys()!=null){
                        preEditor.putString(TagsPreferences.PROFILE_REFERAL_CODE, user.getData().getKeys().getReferal_code());
                    }

                    //getUserDetails
                    getUsersDetails(prefs.getString(TagsPreferences.PROFILE_USER_ID, "0"));
                }else if(user.getStatus().equalsIgnoreCase("2")){
                    DialogUtils.showDialog(SignUp.this,"User already exist for the given credential+" +
                            "\nFollowing information must be unique for new registration"+
                            "\n1) Email"+
                            "\n2) Mobile number"+
                            "\n3) Device (one registration per device)");
                }else{
                    DialogUtils.showDialog(SignUp.this,"error signup" );
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            pd.dismiss();
                Log.d(TAG,"error in signup request "+error.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(registrationrequest);
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /*
        * Get user's detail
        * */
    private void getUsersDetails(String userId) {
        String url=Constants.getUrlUsersDetail(userId);
        JsonObjectRequest requestUsersDetail= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "getUsersDetails response:" + response.toString());
                Gson gson=new Gson();

                UserDetails userDetails=gson.fromJson(response.toString(), UserDetails.class);
                preEditor.putString(TagsPreferences.PROFILE_USER_ID,userDetails.getData().get(0).getId()).commit();
                preEditor.putString(TagsPreferences.PROFILE_USER_EMAIL,userDetails.getData().get(0).getUser_email()).commit();
                preEditor.putString(TagsPreferences.PROFILE_USER_NAME, userDetails.getData().get(0).getFull_name()).commit();
                preEditor.putString(TagsPreferences.PROFILE_USER_MOBILE,userDetails.getData().get(0).getMobile()).commit();
                preEditor.putString(TagsPreferences.PROFILE_USER_REFERAL_CODE,userDetails.getData().get(0).getReferal_code()).commit();
                if(userDetails.getData().get(0).getUserPoints()!=null) {
                    preEditor.putString(TagsPreferences.PROFILE_USER_REDEEM_POINT, userDetails.getData().get(0).getUserPoints().getTotal_points()).commit();
                    preEditor.putString(TagsPreferences.PROFILE_USER_REFERAL_POINT, userDetails.getData().get(0).getUserPoints().getTotal_points()).commit();
                }
                else{
                    preEditor.putString(TagsPreferences.PROFILE_USER_REDEEM_POINT, "o").commit();
                    preEditor.putString(TagsPreferences.PROFILE_USER_REFERAL_POINT, "0").commit();
                }

                preEditor.putString(TagsPreferences.PROFILE_USER_ROLE,userDetails.getData().get(0).getUserRole().getRole_name()).commit();


                //change login status and start next activity
                preEditor.putBoolean(TagsPreferences.LOGIN_STATUS,true).commit();
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

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(SignUp.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        tilSignupEmail.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /*
        * Open next Activity according to extra data
        * */
    private void nextActivity() {
        try {
            Bundle extras = getIntent().getExtras();
            String classname=extras.getString(TagsPreferences.NEXT);
            Class<?> clazz = null;
            clazz = Class.forName(classname);
            Intent i = new Intent(SignUp.this, clazz);
            startActivity(i);
            finish();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}

