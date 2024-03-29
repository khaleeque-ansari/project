package com.firsteat.firsteat.app;


import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDex;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.facebook.FacebookSdk;
import com.firsteat.firsteat.activities.ProductTourActivity;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.firsteat.firsteat.utils.Constants;
import com.firsteat.firsteat.utils.LruBitmapCache;
import com.firsteat.firsteat.utils.TagsPreferences;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Created by touchmagics on 11/25/2015.
 */
public class AppController extends Application {

    public static final String TAG = AppController.class
            .getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private static AppController mInstance;
    private SharedPreferences prefs;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        prefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        findKeyHash();

        //check whether to delete Cache or not
        checkClearCache();

//        findDeviceIdAndGoogleAdId();



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


                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;

            }




        }.execute(null, null, null);

    }

    private void findKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.firsteat.firsteat",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("keyhash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    private void checkClearCache(){
        int oldVersionCode = Constants.getAppPrefInt(this, "version_code");
        int currentVersionCode = Constants.getAppVersionCode(this);

        Log.d(TAG,"Old version code: "+oldVersionCode);
        Log.d(TAG, "Current version code: " + currentVersionCode);
        //when user will open the app next he will get incremented old version
        //this scenario is taking place in MainActivity

        if(currentVersionCode>oldVersionCode){
            //clear cache
            clearApplicationData();
        }
    }


    /*
    * Method to clear cache when applicaation is launched very first time
    * or application version is changed
    * */
    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.d("TAG", "**************** File /data/data/APP_PACKAGE/" + s + " DELETED *******************");
                }
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }
}
