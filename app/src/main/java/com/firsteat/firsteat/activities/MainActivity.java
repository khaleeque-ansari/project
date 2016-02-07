package com.firsteat.firsteat.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.firsteat.firsteat.R;
import com.firsteat.firsteat.adapters.MenuItemAdapter;
import com.firsteat.firsteat.adapters.ViewPagerAdapter;
import com.firsteat.firsteat.app.AppController;
import com.firsteat.firsteat.fragments.AboutUs;
import com.firsteat.firsteat.fragments.Alacarte;
import com.firsteat.firsteat.fragments.BreakfastProgram;
import com.firsteat.firsteat.fragments.ContactUs;
import com.firsteat.firsteat.fragments.MyOrder;
import com.firsteat.firsteat.fragments.Refer;
import com.firsteat.firsteat.models.DeliveryLocations;
import com.firsteat.firsteat.models.Locations;
import com.firsteat.firsteat.utils.ChangeFragment;
import com.firsteat.firsteat.utils.Constants;
import com.firsteat.firsteat.utils.DbHelper;
import com.firsteat.firsteat.utils.DialogUtils;
import com.firsteat.firsteat.utils.TagsPreferences;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

        private static final String TAG = MainActivity.class.getSimpleName();
        private static Context context;
        RecyclerView mRecyclerView;
        RecyclerView.LayoutManager mLayoutManager;
        MenuItemAdapter mAdapter;
        private ViewPager viewPager;
        private TabLayout tabLayout;
        private SharedPreferences prefs;
        private Spinner spiMainLocation;
        public static Locations locations;
        private DbHelper helper;
        private ImageView imgNotification;
        private NavigationView navigationView;
        private TextView txtHeaderName,txtHeaderPhone;
        public static DeliveryLocations.DataEntity deiveryLocationDataEntry;
    public static FrameLayout snackbarframeMain;


    @Override
        protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "oncreate");
        context=MainActivity.this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        helper=new DbHelper(MainActivity.this);
        prefs = PreferenceManager
                .getDefaultSharedPreferences(this);

        checkShowTutorial();

        spiMainLocation =(Spinner)findViewById(R.id.spiMainLocation);
        imgNotification = (ImageView) findViewById(R.id.imgSpecialNotification);
        snackbarframeMain= (FrameLayout) findViewById(R.id.snackbarframeMain);



        if(prefs.getBoolean(TagsPreferences.FLAG_MSG_OUT_OF_SERVICE_AREA,true)){
            DialogUtils.showDialog(context, "Out of our radar, we're expanding soon!\nSelect a drop point");
        }





            /*
            * check for special notification
            * */
        imgNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, SpecialNotifications.class));


            }
        });

        /*
        * Show service area into the spinner
        * */
//        updateLocation();
            downloadLocation();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        View header = LayoutInflater.from(this).inflate(R.layout.fragment_navigation_header, null);
        navigationView.addHeaderView(header);
        txtHeaderName = (TextView) header.findViewById(R.id.txtHeaderName);
        txtHeaderPhone = (TextView) header.findViewById(R.id.txtHeaderPhone);

        /*
        * if Login status is true the show user's data in navigation drawer
        * */
        if(prefs.getBoolean(TagsPreferences.LOGIN_STATUS,false)){
            String fullname = prefs.getString(TagsPreferences.PROFILE_USER_NAME, "Hello Guest");
            if(fullname.contains(" ")){
                String users_name=fullname.substring(0,fullname.indexOf(" "));
                txtHeaderName.setText("Hello! " + users_name);
            }else{
                txtHeaderName.setText("Hello! " + fullname);
            }
            txtHeaderPhone.setText(prefs.getString(TagsPreferences.PROFILE_USER_MOBILE, "Phone"));

       }else
       {
           txtHeaderName.setText("Hello Guest");
//           address.setText("Address");
       }


        navigationView.setNavigationItemSelectedListener(this);
//        navigationView.setCheckedItem(R.id.nav_menu);
        ChangeFragment.replaceFragment(getSupportFragmentManager(), R.id.frame_main, new Alacarte(), Alacarte.TAG);
//        TextView txtEmail= (TextView) navigationView.findViewById(R.id.name);
//        txtEmail.setText("Pallaw.pathak@gmail.com");


        /*
        * if got an instruction with intent to change fragment
        * */
        if(getIntent().getExtras()!=null){
            Log.d(TAG,"having bundle");
            /*
            * this is because we are currently sending only extra to the main activity for changing
             * fragment and show myOrders fragment
            * */
            ChangeFragment.replaceFragment(getSupportFragmentManager(), R.id.frame_main, new MyOrder(), "change");
        }
    }



    private void downloadLocation() {
        String url= Constants.URL_DELIVERY_LOCATIONS;
        JsonObjectRequest requestDeliveryLocation= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
        Log.d(TAG,"download location response :"+response.toString());
                prefs.edit().putString(TagsPreferences.JSON_SERVICE_AREA_LOCATIONS,response.toString()).commit();


                updateLocation();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"Server error",Toast.LENGTH_LONG).show();
                Log.d(TAG,"download location response :"+error.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(requestDeliveryLocation);
    }


    private void updateLocation() {
        Log.d(TAG,"updateLocation");
        String serviceAreaLocationJSON=prefs.getString(TagsPreferences.JSON_SERVICE_AREA_LOCATIONS,"{}");
        Log.d(TAG,"serviceAreaLocationJSON : "+serviceAreaLocationJSON);

        Gson gson=new Gson();
        final DeliveryLocations deliveryLocations = gson.fromJson(serviceAreaLocationJSON, DeliveryLocations.class);

        final ArrayList<String> addresses=new ArrayList<>();
        for(int i=0;i<=deliveryLocations.getData().size();i++){
            if(i==0)
                addresses.add(prefs.getString(TagsPreferences.LOCATION_MY_ADDRESS,""));
            else
                addresses.add(deliveryLocations.getData().get(i-1).getLocation_name());
        }

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(MainActivity.this,R.layout.custom_array_adapter_layout,addresses);
        adapter.setDropDownViewResource(R.layout.custom_array_adapter_dropdown_layout);
        spiMainLocation.setAdapter(adapter);

        final ArrayList<String> addre=addresses;
        spiMainLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                prefs.edit().putString(TagsPreferences.ADDRESS_BASE_LINE, addre.get(position)).commit();
                if (prefs.getBoolean(TagsPreferences.FLAG_MSG_OUT_OF_SERVICE_AREA, true)) {//out of service area
                    //set the position of the spinner in preference which will be used to get kitchen id from kitchen json
                    //at the time of placing order

                    if (position != 0) {
                        prefs.edit().putString(TagsPreferences.ADDRESS_BASE_LINE, addre.get(position)).commit();
                        deiveryLocationDataEntry = deliveryLocations.getData().get(position - 1);
                        //alter preference to do not show area dialog
                        prefs.edit().putBoolean(TagsPreferences.FLAG_MSG_OUT_OF_SERVICE_AREA, false).commit();
                    }

                } else {//inside service area
                    prefs.edit().putString(TagsPreferences.ADDRESS_BASE_LINE, addre.get(position)).commit();

                    if (position == 0) {
                        //alter preference to do not show area dialog
                        prefs.edit().putBoolean(TagsPreferences.FLAG_MSG_OUT_OF_SERVICE_AREA, true).commit();
                        deiveryLocationDataEntry = new DeliveryLocations.DataEntity();
                        deiveryLocationDataEntry.setLatitude(prefs.getString(TagsPreferences.LOCATION_MY_LATITUDE, ""));
                        deiveryLocationDataEntry.setLatitude(prefs.getString(TagsPreferences.LOCATION_MY_LONGITUDE, ""));
                        deiveryLocationDataEntry.setLatitude(prefs.getString(TagsPreferences.LOCATION_MY_ADDRESS, ""));

                    } else {
                        deiveryLocationDataEntry = deliveryLocations.getData().get(position - 1);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

        private void setupViewPager(ViewPager viewPager) {
        Log.d(TAG,"updateLocation");
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Alacarte(), "ALACARTE");
        adapter.addFragment(new BreakfastProgram(), "BREAKFAST PROG.");
        viewPager.setAdapter(adapter);
    }

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Log.d(TAG,"onNavigationItemSelected");
        int id = item.getItemId();

        FragmentManager fm=getSupportFragmentManager();

        switch (id){
            case R.id.nav_menu:

                ChangeFragment.changeFragment(getSupportFragmentManager(), R.id.frame_main, new Alacarte());
//                FragmentTransaction ft= MainActivity.this.getSupportFragmentManager().beginTransaction();
//                ft.replace(R.id.container_body,new HomeFragment()).commit();

                break;
            case R.id.nav_order:
                Intent toMyOrder=new Intent(MainActivity.this,BaseFragmentActivity.class);
                toMyOrder.putExtra("frag", MyOrder.class.getCanonicalName());
                startActivity(toMyOrder);
//                ChangeFragment.changeFragment(getSupportFragmentManager(), R.id.frame_main, new MyOrder());
                break;
            case R.id.nav_about_us:
                Intent toAboutUs=new Intent(MainActivity.this,BaseFragmentActivity.class);
                toAboutUs.putExtra("frag", AboutUs.class.getCanonicalName());
                startActivity(toAboutUs);
//                ChangeFragment.changeFragment(getSupportFragmentManager(), R.id.frame_main, new AboutUs());
                break;
            case R.id.nav_contact_us:
                Intent toContactUs=new Intent(MainActivity.this,BaseFragmentActivity.class);
                toContactUs.putExtra("frag", ContactUs.class.getCanonicalName());
                startActivity(toContactUs);
//                ChangeFragment.changeFragment(getSupportFragmentManager(), R.id.frame_main, new ContactUs());
                break;
            case R.id.nav_referal:
                Intent toReferal=new Intent(MainActivity.this,BaseFragmentActivity.class);
                toReferal.putExtra("frag", Refer.class.getCanonicalName());
                startActivity(toReferal);

                break;

            case R.id.nav_feedback:
                if(prefs.getBoolean(TagsPreferences.LOGIN_STATUS,false))
                    dialogFeedback();
                else{
                    Intent tomain=new Intent(MainActivity.this, LoginActivity.class);
                    tomain.putExtra(TagsPreferences.INTENT_TYPE,TagsPreferences.INTENT_TYPE_DIALOG);
                    tomain.putExtra(TagsPreferences.NEXT,MainActivity.class.getCanonicalName());
                    startActivity(tomain);
                }
                break;
            /*
            * favourite commented for next release
            * */
//            case R.id.nav_favorites:
//                if(prefs.getBoolean(TagsPreferences.LOGIN_STATUS,false)){
//
//                    Intent toMyFav=new Intent(MainActivity.this,BaseFragmentActivity.class);
//                    toMyFav.putExtra("frag", Favorites.class.getCanonicalName());
//                    startActivity(toMyFav);
//                    ChangeFragment.changeFragment(getSupportFragmentManager(), R.id.frame_main, new Favorites());
//                }
//
//                else{
//                    Intent tomain=new Intent(MainActivity.this, LoginActivity.class);
//                    tomain.putExtra(TagsPreferences.INTENT_TYPE,TagsPreferences.INTENT_TYPE_DIALOG);
//                    tomain.putExtra(TagsPreferences.NEXT,MainActivity.class.getCanonicalName());
//                    startActivity(tomain);
//                }
//                break;
            /*
            * Logout commented for the release
            * */
//            case R.id.nav_logout:
//                if(prefs.getBoolean(TagsPreferences.LOGIN_STATUS,false)){
//                    prefs.edit().putBoolean(TagsPreferences.LOGIN_STATUS,false).commit();
//                    DialogUtils.showDialog(MainActivity.this, "You are successfully logged out you'll need to Login again to order anything");
//                    prefs.edit().putString(TagsPreferences.PROFILE_NAME, "Guest").commit();
//                    prefs.edit().putString(TagsPreferences.PROFILE_NAME,"Mobile").commit();
//                    prefs.edit().putBoolean(TagsPreferences.FLAG_DOWNLOAD_USERS_ADDRESS, false).commit();
//                    prefs.edit().putString(TagsPreferences.ADDRESS_LINE_0, "Address line 0").commit();
//                    prefs.edit().putString(TagsPreferences.ADDRESS_LINE_0, "Address line 1").commit();
//                    prefs.edit().putString(TagsPreferences.ADDRESS_LINE_0, "Address line 2").commit();
//                    txtHeaderName.setText("Hello! Guest");
//                    txtHeaderPhone.setText("Mobile");
//
//                }else
//                    DialogUtils.showDialog(MainActivity.this,"You are not logged in");
//                break;

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

        public static void hideToolbarLayout() {
        Log.d(TAG, "hideToolbarLayout");
        Activity activity=(Activity)context;
        View view = activity.findViewById(R.id.tollbarMainCustom);
        view.setVisibility(View.GONE);

    }

        public static void showToolbarLayout() {
        Log.d(TAG, "showToolbarLayout");
        Activity activity=(Activity)context;
        View view = activity.findViewById(R.id.tollbarMainCustom);
        view.setVisibility(View.VISIBLE);
    }

    private void dialogFeedback() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setCancelable(false)
                .setMessage("Please Enter data")
                .setView(R.layout.fragment_feedback) //<-- layout containing EditText
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        prefs.edit().putString(TagsPreferences.PROFILE_NUMBER, "").commit();
                        Log.d(TAG, "mobile number " + prefs.getString(TagsPreferences.PROFILE_NUMBER, ""));
                        //All of the fun happens inside the CustomListener now.
                        //I had to move it to enable data validation.
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Button theButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        theButton.setOnClickListener(new CustomListener(alertDialog));


    }

    /*
    * custom listener for feedback
    * */
    class CustomListener implements View.OnClickListener {
        private final Dialog dialog;
        public CustomListener(Dialog dialog) {
            this.dialog = dialog;
        }
        @Override
        public void onClick(View v) {
            // put your code here
            EditText mEdtText= (EditText) dialog.findViewById(R.id.edtDFeedback);
            RatingBar ratingBar= (RatingBar) dialog.findViewById(R.id.ratingBar);
            String mValue = mEdtText.getText().toString();
            if(mEdtText.getText().toString().trim().length()<4){
                mEdtText.setError("If you are writing, write at least a word");
                Log.d(TAG, "mobile number " + prefs.getString(TagsPreferences.PROFILE_NUMBER, ""));
            }else{
                float rating = ratingBar.getRating();
                String feedback = mEdtText.getText().toString();
                /*
                * send feedback to server
                * */
                sendFeedback(feedback,rating);
                dialog.dismiss();
            }
        }
    }

    private void sendFeedback(String feedback, float rating) {
        String url= Constants.getUrlFeedback(prefs.getString(TagsPreferences.PROFILE_USER_ID, "0"), feedback, String.valueOf(rating));
        final ProgressDialog pd=new ProgressDialog(MainActivity.this);
        pd.setCancelable(false);
        pd.setMessage("sending your feedback .......");
        pd.show();
        JsonObjectRequest requestFeedback=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pd.dismiss();
                Log.d(TAG, "requestFeedback response" + response.toString());
                Toast.makeText(MainActivity.this, "your feedback has been sent successfully", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"Server error",Toast.LENGTH_LONG).show();
                pd.dismiss();
                Log.d(TAG,"requestFeedback error"+error.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(requestFeedback);
    }

    private void checkShowTutorial(){
        int oldVersionCode = Constants.getAppPrefInt(this, "version_code");
        int currentVersionCode = Constants.getAppVersionCode(this);

        Log.d(TAG,"Old version code: "+oldVersionCode);
        Log.d(TAG, "Current version code: " + currentVersionCode);

        if(currentVersionCode>oldVersionCode){
            //clear cache

            startActivity(new Intent(MainActivity.this,ProductTourActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            Constants.putAppPrefInt(this, "version_code", currentVersionCode);
        }
//        else{
//            if(!(prefs.getBoolean(TagsPreferences.LOGIN_STATUS,false)))
//                startActivity(new Intent(MainActivity.this,LoginActivity.class));
//        }
    }



}
