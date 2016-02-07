package com.firsteat.firsteat.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.firsteat.firsteat.R;
import com.firsteat.firsteat.activities.LoginActivity;
import com.firsteat.firsteat.activities.MainActivity;
import com.firsteat.firsteat.app.AppController;
import com.firsteat.firsteat.utils.Constants;
import com.firsteat.firsteat.utils.TagsPreferences;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class Refer extends Fragment implements FragmentManager.OnBackStackChangedListener, View.OnClickListener {
    public static final String TAG = Refer.class.getSimpleName();
    View rootView;
    private SharedPreferences prefs;
    private SharedPreferences.Editor preEditor;
    Context context;
    TextView txtReferal_ReedeamPoint;
    private TextView txtReferalCode;
    private String sharingText;

    public Refer() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Listen for changes in the back stack

        getActivity().getSupportFragmentManager().addOnBackStackChangedListener(this);
        //Handle when activity is recreated like on orientation Change
//        shouldDisplayHomeUp();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.fragment_refer, container, false);
        context=getActivity();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Refer a Friend");
        prefs = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
        preEditor=prefs.edit();
        sharingText="Yes!! Now you can download food too :D First Eat is giving you 50 credits you can use to order yum meals. Download the super app  https://goo.gl/7jpfHN and use the code "+ prefs.getString(TagsPreferences.PROFILE_REFERAL_CODE,"")+" to redeem credits. #PehleKhao";

        int[] clickables={
                R.id.imgWhatsapp,
                R.id.imgFacebook,
                R.id.imgTwitter,
                R.id.txtReferalOrderNow
        };

        for(int id:clickables){
           View clickable = rootView.findViewById(id);
            clickable.setOnClickListener(this);
        }

        txtReferal_ReedeamPoint= (TextView) rootView.findViewById(R.id.txtReferal_ReedeamPoint);
        txtReferalCode= (TextView) rootView.findViewById(R.id.txtReferalCode);

        if(prefs.getBoolean(TagsPreferences.LOGIN_STATUS,false)){
            txtReferalCode.setText(prefs.getString(TagsPreferences.PROFILE_REFERAL_CODE, "NULL"));
            getReedeamPoint();
        }else{
            Intent toMain=new Intent(getActivity(), LoginActivity.class);
            toMain.putExtra(TagsPreferences.NEXT, MainActivity.class.getCanonicalName());
            context.startActivity(toMain);
        }
        return rootView;
    }

    private void getReedeamPoint() {
        final ProgressDialog pd= new ProgressDialog(getActivity());
        pd.setMessage("Please wait......\nFetching data");
        pd.setCancelable(false);
        pd.show();
        String url= Constants.getUrlGetReedeamPoint(prefs.getString(TagsPreferences.PROFILE_USER_ID, "0"));
        JsonObjectRequest requestReferal=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    pd.dismiss();
                    Log.d(TAG, "reedeam point :" + response.toString());

                    prefs.edit().putString(TagsPreferences.PROFILE_REFERAL_CODE,response.getJSONObject("data").getString("referal_code").toString());
                    txtReferal_ReedeamPoint.setText(response.getJSONObject("data").getJSONObject("points").getString("total_points").toString());
                    txtReferalCode.setText(response.getJSONObject("data").getString("referal_code").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"Server error",Toast.LENGTH_LONG).show();
                pd.dismiss();
                Log.d(TAG,"reedeam point error :"+error.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(requestReferal);
    }

    @Override
    public void onBackStackChanged() {
//        shouldDisplayHomeUp();
    }

    public void shouldDisplayHomeUp(){
        //Enable Up button only  if there are entries in the back stack
        boolean canback = getActivity().getSupportFragmentManager().getBackStackEntryCount()>0;
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(canback);
    }





    public void shareFacebook() {
        String fullUrl = "https://m.facebook.com/sharer.php?u=..";
        try {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setClassName("com.facebook.katana",
                    "com.facebook.katana.ShareLinkActivity");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, sharingText);
            startActivity(sharingIntent);

        } catch (Exception e) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(fullUrl));
            startActivity(i);

        }
    }

    public void shareWhatsapp() {

        PackageManager pm=getActivity().getPackageManager();
        try {

            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");

            PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            //Check if package exists or not. If not then code
            //in catch block will be called
            waIntent.setPackage("com.whatsapp");

            waIntent.putExtra(Intent.EXTRA_TEXT, sharingText);
            startActivity(Intent.createChooser(waIntent, "Share with"));

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(getActivity(), "WhatsApp not Installed", Toast.LENGTH_SHORT)
                    .show();
        }

    }

    public void shareTwitter() {

        try {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setClassName("com.twitter.android", "com.twitter.android.PostActivity");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, sharingText);
            startActivity(sharingIntent);
        } catch (Exception e) {
            Log.e("In Exception", "Comes here");
            Intent i = new Intent();
            i.putExtra(Intent.EXTRA_TEXT, sharingText);
            i.setAction(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://mobile.twitter.com/compose/tweet"));
            startActivity(i);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgWhatsapp:
                shareWhatsapp();
                break;
            case R.id.imgFacebook:
                shareFacebook();
                break;
            case R.id.imgTwitter:
                shareTwitter();
                break;
            case R.id.txtReferalOrderNow:
                startActivity(new Intent(getActivity(),MainActivity.class));
                getActivity().finish();
                break;
        }
    }
}
