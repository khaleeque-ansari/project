package com.firsteat.firsteat.activities;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.firsteat.firsteat.R;
import com.firsteat.firsteat.app.AppController;
import com.firsteat.firsteat.models.DistenceMatrix;
import com.firsteat.firsteat.models.GoogleGeoCoading;
import com.firsteat.firsteat.models.Kitchens;
import com.firsteat.firsteat.models.Locations;
import com.firsteat.firsteat.utils.Constants;
import com.firsteat.firsteat.utils.DbHelper;
import com.firsteat.firsteat.utils.DialogUtils;
import com.firsteat.firsteat.utils.TagsPreferences;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Splash extends AppCompatActivity {
    public static final String TAG = Splash.class.getSimpleName();
    private SharedPreferences prefs;
    public Locations locations;
    public DistenceMatrix matrix;
    private DbHelper helper;
    private Context context;
    private SharedPreferences.Editor preEditor;
    private LocationManager locationManager;
    private LocationListener locationListener;
    public static Kitchens kitchens;
    LinearLayout progressLayoutSplash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = Splash.this;
        helper = new DbHelper(Splash.this);
        prefs = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        preEditor = prefs.edit();
        progressLayoutSplash= (LinearLayout) findViewById(R.id.progressLayoutSplash);

        progressLayoutSplash.setVisibility(View.VISIBLE);
        findDeviceIdAndGoogleAdId();


    }


    private  void findDeviceIdAndGoogleAdId() {
        //Get the instance of TelephonyManager
        TelephonyManager tm=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        Log.d(TAG,"device id: "+tm.getDeviceId());
        Constants.DEVICE_ID=tm.getDeviceId();
        prefs.edit().putString(TagsPreferences.DEVICE_ID,tm.getDeviceId()).commit();

        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                String msg = "";
                try {
                    GoogleCloudMessaging gcm=null;
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    String regid = gcm.register(Constants.SENDER_ID);
                    msg = "Device registered, registration id=" + regid;
                    Constants.ADVERT_ID=regid;
                    prefs.edit().putString(TagsPreferences.GCM_ID,regid).commit();
                    Log.d(TAG,msg);


                    //if application is running very first time
                    if(!(prefs.getBoolean(TagsPreferences.FIRST_SERVICE,false))&&
                            prefs.getString(TagsPreferences.GCM_ID,"")!=""){

                        //start first service
                        firstService();
                    }

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;

            }




        }.execute(null, null, null);

    }

    private void firstService() {

        //get primary account from the system
        AccountManager accManager = AccountManager.get(context);
        Account acc[] = accManager.getAccounts();
        String primaryEmail="";
        for (int i = 0; i < acc.length; i++) {

            String dummy=acc[i].name;
            if(acc[i].type.equalsIgnoreCase("com.google")){
                Log.d(TAG,"accounts : if part");
                primaryEmail=acc[i].name;
                break;
            }else{
                Log.d(TAG,"accounts : else part");
            }

        }
        String url= Constants.getUrlFirstService(Constants.DEVICE_ID,Constants.ADVERT_ID,primaryEmail);
        JsonObjectRequest requestFirstService= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, "requestFirstService response" + response.toString());
                preEditor.putBoolean(TagsPreferences.FIRST_SERVICE,true).commit();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Splash.this,"server error",Toast.LENGTH_SHORT).show();
                Log.d(TAG,"requestFirstService error"+error.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(requestFirstService);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(CheckEnableGPS()){
        /*
        * The scenario works in following flow
        * 1.Get current location
        * 2. download service Area locations
        * 3. download distence matrix and compare currentLocation with matrix
        * Note:- all the steps works in nested form so that it will be dependent on
        * its previous step
        * */
            getMyLocation();

        /*
        * start a method to get special notification
        *
        * */
            getSpecialNotification();
        }



    }

    private void getSpecialNotification() {
        String url = Constants.URL_SPECIAL_NOTIFICATION;
        JsonObjectRequest requestSpecialNotification = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                preEditor.putString(TagsPreferences.JSON_SERVICE_SPECIAL_NOTIFICATION, response.toString()).commit();
                Log.d(TAG, "getSpecialNotification response:" + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Splash.this,"Server error",Toast.LENGTH_LONG).show();
                Log.d(TAG, "getSpecialNotification error:" + error.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(requestSpecialNotification);
    }

    /*
    * Get user's location by using GPS
    * */
    private void getMyLocation() {
        Log.d(TAG, "getMyLocation");
        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);


        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
//                makeUseOfNewLocation(location);

                preEditor.putString(TagsPreferences.LOCATION_MY_LATITUDE, String.valueOf(location.getLatitude())).commit();
                preEditor.putString(TagsPreferences.LOCATION_MY_LONGITUDE, String.valueOf(location.getLongitude())).commit();
                Log.d(TAG, "my location Latitude" + prefs.getString(TagsPreferences.LOCATION_MY_LATITUDE, null) +
                        "longitude:" + prefs.getString(TagsPreferences.LOCATION_MY_LONGITUDE, null));

                //Download kitchen location
                downloadKitchens();

                /*
                * download proper address of myLocation
                * */
                downloadGoogleGeoCode();
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
                if (status == LocationProvider.OUT_OF_SERVICE)
                    Log.d(TAG, "location provider out of service");
                if (status == LocationProvider.TEMPORARILY_UNAVAILABLE)
                    Log.d(TAG, "location provider is temporary unavailable");
                if (status == LocationProvider.AVAILABLE)
                    Log.d(TAG, "location provider is available now");
            }

            public void onProviderEnabled(String provider) {
                Log.d(TAG, "GPS : Disabled");
            }

            public void onProviderDisabled(String provider) {
                Log.d(TAG, "GPS : Disabled");
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 50, locationListener);
    }

    private void downloadGoogleGeoCode() {
        String url = Constants.getUrlGoogleGeocode(prefs.getString(TagsPreferences.LOCATION_MY_LATITUDE, "0"), prefs.getString(TagsPreferences.LOCATION_MY_LONGITUDE, "0"));
        JsonObjectRequest requestGooglGeoCode = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                GoogleGeoCoading googleGeoCoading = gson.fromJson(response.toString(), GoogleGeoCoading.class);

                for (GoogleGeoCoading.ResultsEntity.AddressComponentsEntity entitiy : googleGeoCoading.getResults().get(0).getAddress_components()) {
                    if (entitiy.getTypes().get(0).equalsIgnoreCase("sublocality_level_1")) {
                        preEditor.putString(TagsPreferences.ADDRESS_BASE_LINE, entitiy.getLong_name());
                        preEditor.putString(TagsPreferences.LOCATION_MY_ADDRESS, entitiy.getLong_name());
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Splash.this,"Server error",Toast.LENGTH_LONG).show();
                Log.d(TAG,"downloadGoogleGeoCode error:"+error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(requestGooglGeoCode);
    }

    /*
    * Download Service area locations
    * */
    private void downloadKitchens() {
//        final ProgressDialog pd=new ProgressDialog(context.getApplicationContext());
//        Log.d(TAG, "downloading locations");
//        pd.setMessage("Fetching Location....");
//        pd.show();
        JsonObjectRequest downloadKitchenRequest = new JsonObjectRequest(Request.Method.GET, Constants.URL_KITCHENS, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "downloadKitchenRequest response :" + response.toString());

//                preEditor.putBoolean(TagsPreferences.FLAG_DOWNLOAD_LOCATION,true).commit();//update preferences that json has been downloaded

                preEditor.putString(TagsPreferences.JSON_KITCHEN_AREA_LOCATIONS, response.toString()).commit();
                Gson gson = new Gson();
//                locations = gson.fromJson(response.toString(), Locations.class);
                kitchens = gson.fromJson(response.toString(), Kitchens.class);


                //download distence matrix from current location to
                //the service areas downloaded from this request
                downloadDistenceMatrix();
//                pd.dismiss();//close dialog
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                pd.dismiss();//close dialog
                Log.d(TAG, "downloadKitchenRequest error: " + error.getMessage());
                Toast.makeText(Splash.this,"Server error",Toast.LENGTH_LONG).show();
                progressLayoutSplash.setVisibility(View.GONE);
                DialogUtils.showDialog(context, "Something Went Wrong. This may be caused by" +
                        "\n1) No Internet Connection" +
                        "\n2) Server Error");
            }
        });
        AppController.getInstance().addToRequestQueue(downloadKitchenRequest);
    }

    /*
    * Download distence matrix from current address to the service area locations
    * */
    private void downloadDistenceMatrix() {
        Log.d(TAG, "download distance matrix");
        String url = Constants.getUrlGoogleDistenceMatrix(prefs.getString(TagsPreferences.LOCATION_MY_LATITUDE, null),
                prefs.getString(TagsPreferences.LOCATION_MY_LONGITUDE, null), kitchens);
        Log.d(TAG, "distence matrix api url: " + url);

        //reset out of service dialog flag
        preEditor.putBoolean(TagsPreferences.FLAG_MSG_OUT_OF_SERVICE_AREA, true).commit();

        JsonObjectRequest requestDistenceMatrix = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "requestDistenceMatrix response :" + response.toString());
                Gson gson = new Gson();
                matrix = gson.fromJson(response.toString(), DistenceMatrix.class);

                String myLocation = matrix.getOrigin_addresses().get(0);

                //store own address in preference
//                preEditor.putString(TagsPreferences.LOCATION_MY_ADDRESS, myLocation).commit();



                /*
                * Check which kitchen is nearest to the location and set DELIVERY_KITCHEN_ID to the preferences
                * */
                int counter=0;
                int radius=0;
                for (DistenceMatrix.RowsEntity.ElementsEntity destination : matrix.getRows().get(0).getElements()) {

                    if (destination.getDistance().getValue() < (kitchens.getData().get(counter).getRadius())*1000)//distence in meter
                    {
                        if(radius==0) {//assign radius of the first kitchen to radius variable
                            radius = (kitchens.getData().get(counter).getRadius()) * 1000;
                            preEditor.putInt(TagsPreferences.DELIVERY_KITCHEN_ID,kitchens.getData().get(counter).getId()).commit();
                        }
                        else if((kitchens.getData().get(counter).getRadius())*1000<radius) {//if new radius is less then previous the assign value
                            radius = (kitchens.getData().get(counter).getRadius()) * 1000;
                            preEditor.putInt(TagsPreferences.DELIVERY_KITCHEN_ID,kitchens.getData().get(counter).getId()).commit();
                        }

                        //if value is greater then redius of the respective kitchen then set flag FLAG_MSG_OUT_OF_SERVICE_AREA false in preferences
                        //for now showing out of service area msg
                        Log.d(TAG, "distence is in service area: distence" + destination.getDistance().getValue()+"radius :"+
                                (kitchens.getData().get(counter).getRadius())*1000);
                        preEditor.putBoolean(TagsPreferences.FLAG_MSG_OUT_OF_SERVICE_AREA, false).commit();
                        break;
                    } else {
                        Log.d(TAG, "distence is out service area: distence" + destination.getDistance().getValue()+"radius :"+
                                (kitchens.getData().get(counter).getRadius())*1000);
                    }
                    counter=++counter;
                }
                startActivity(new Intent(Splash.this, MainActivity.class));
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Splash.this,"server error",Toast.LENGTH_SHORT).show();
                Log.d(TAG, "requestDistenceMatrix error :" + error.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(requestDistenceMatrix);

    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(locationListener);


    }


    private class SendHttpRequestTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            String name = params[1];

            String data = sendHttpRequest(url, name);
            System.out.println("Data ["+data+"]");
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
           Log.d(TAG,"result "+result);

        }



    }

    private String sendHttpRequest(String url, String name) {
        StringBuffer buffer = new StringBuffer();
        try {
            System.out.println("URL ["+url+"] - Name ["+name+"]");

            HttpURLConnection con = (HttpURLConnection) ( new URL(url)).openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();
            con.getOutputStream().write( ("name=" + name).getBytes());

            InputStream is = con.getInputStream();
            byte[] b = new byte[1024];

            while ( is.read(b) != -1)
                buffer.append(new String(b));

            con.disconnect();
        }
        catch(Throwable t) {
            t.printStackTrace();
        }

        return buffer.toString();
    }

    private boolean CheckEnableGPS(){
        boolean isGPSEnable=false;

        String provider = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if(!provider.equals("")){
            //GPS Enabled
            isGPSEnable=true;
            Toast.makeText(Splash.this, "GPS Enabled: " + provider,
                    Toast.LENGTH_LONG).show();
        }else{
            isGPSEnable=false;
            DialogUtils.showDialog(Splash.this, "We need your GPS location to move further", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Settings.ACTION_SETTINGS));
                }
            });
        }
        return isGPSEnable;
    }



}

