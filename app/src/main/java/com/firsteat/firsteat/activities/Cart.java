package com.firsteat.firsteat.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.firsteat.firsteat.R;
import com.firsteat.firsteat.adapters.CartAdapter;
import com.firsteat.firsteat.adapters.MenuAdapter;
import com.firsteat.firsteat.app.AppController;
import com.firsteat.firsteat.fragments.Alacarte;
import com.firsteat.firsteat.models.TimeSlots;
import com.firsteat.firsteat.models.UsersAddresses;
import com.firsteat.firsteat.utils.Constants;
import com.firsteat.firsteat.utils.DialogUtils;
import com.firsteat.firsteat.utils.TagsPreferences;
import com.wefika.horizontalpicker.HorizontalPicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class Cart extends AppCompatActivity {

    private static final String TAG = Cart.class.getSimpleName();
    private CardView mCardView;
    private CartAdapter adapter;
    private RecyclerView rv;
    private static SharedPreferences prefs;
    private static SharedPreferences.Editor preEditor;
    public static ArrayList<UsersAddresses> addresses=new ArrayList<>();
    static TextView txtAddressLine0;
    static TextView txtAddressLine1;
    static TextView txtAddressLine2;
    public int cartposition;
    private TabLayout tabLayout;
    private boolean addressExist=false;
    static ImageButton imgCartAddCategory;
    private TimeSlots timeSlots;
    public static boolean isTimeSlotAvailable=true;
    private TextView txtCartTimeSlot;
    public static FrameLayout snackbarframe;
    private boolean isAnyTimeSlotActive=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCart);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Cart");

        Log.d(TAG, String.valueOf(MenuAdapter.selected_item.size()));

        txtAddressLine0= (TextView) findViewById(R.id.txtCO_ConfirmOrder);
        txtAddressLine1= (TextView) findViewById(R.id.txtAddressLine1);
        imgCartAddCategory= (ImageButton) findViewById(R.id.imgCartAddCategory);
        txtCartTimeSlot= (TextView) findViewById(R.id.txtCartTimeSlot);
//        txtAddressLine2= (TextView) findViewById(R.id.txtAddressLine2);
        snackbarframe= (FrameLayout) findViewById(R.id.snackbarframe);

        prefs = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        preEditor=prefs.edit();

        truncateOrderSummaryPreference();

        findViewById(R.id.btnAddress).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Cart.this, AddressActivity.class));
            }
        });



        /*
        Get current address by using gps
        * */
        getLocation();

        /*
        Get list of address associated with a Login user
        * */
        if(!prefs.getBoolean(TagsPreferences.FLAG_DOWNLOAD_USERS_ADDRESS,false)){
            getAddress();
        }else{

            txtAddressLine0.setText(prefs.getString(TagsPreferences.ADDRESS_LINE_0,"House no"));
            txtAddressLine1.setText(prefs.getString(TagsPreferences.ADDRESS_LINE_1,"First Line of Address"));
            getAddress();
//            txtAddressLine2.setText(prefs.getString(TagsPreferences.ADDRESS_LINE_2,"Second Line of Address"));
        }


        //        //getSelected item from intent which is passed from MenuAdapter
//        HashMap<String,HashMap<String, com.touchmagics.firsteat.models.Menu.DataEntity>> selected_item= (HashMap<String, HashMap<String, com.touchmagics.firsteat.models.Menu.DataEntity>>) getIntent().getSerializableExtra("selected_item");
        //get item from selected items for cart
        for (String s : MenuAdapter.selected_item.keySet()) {
            HashMap<String, com.firsteat.firsteat.models.Menu.DataEntity> menuItem = MenuAdapter.selected_item.get(s);
            for (String key : menuItem.keySet()) {
                com.firsteat.firsteat.models.Menu.DataEntity dataEntity = menuItem.get(key);
                Log.d(TAG, "selected items " + dataEntity.getName());
            }
        }

        /*
        * Fetch time slots
        * */
        fetchTimeSlots();

        adapter = new CartAdapter(Cart.this, MenuAdapter.selected_item);
        rv = (RecyclerView) findViewById(R.id.recycler_view_cart);
        //        final LinearLayoutManager layoutManager = new org.solovyev.android.views.llm.LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager lm = new LinearLayoutManager(Cart.this);
        rv.hasFixedSize();
        rv.setLayoutManager(lm);
        Log.d(TAG,"cart adapter print check");
        rv.setAdapter(adapter);


    }

    private void truncateOrderSummaryPreference() {
        prefs.edit().putInt(TagsPreferences.CART_SUB_TOTAL,0).commit();
        preEditor.putFloat(TagsPreferences.VAT, 0f).commit();//12.5% of subtotal
        preEditor.putFloat(TagsPreferences.SURCHARGE, 0f).commit();//5% of subtotal
        preEditor.putInt(TagsPreferences.COUPON_DISCOUNT, 0).commit();
    }
    private void fetchTimeSlots() {

        String url=Constants.getUrlTimeSlots(prefs.getString(TagsPreferences.DATE,""));
        Log.d(TAG, "time slot url" + url);
        //        String url=Constants.getUrlTimeSlots(prefs.getString(TagsPreferences.DATE,""));
        JsonObjectRequest requestDate=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                    Log.d(TAG,"fetchTimeSlots response:"+response.toString());
                Gson gson=new Gson();
                timeSlots = gson.fromJson(response.toString(), TimeSlots.class);

                if(timeSlots.getStatus().equalsIgnoreCase("0"))//if has some value
                setSlotValues(timeSlots);
                else{
//                    DialogUtils.showDialog(Cart.this,"Kitchen is closed for selected date");
                    DialogUtils.showDialog(Cart.this, "Please select another time-slot");
                    isTimeSlotAvailable=false;
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Cart.this,"Server error",Toast.LENGTH_LONG).show();
                Log.d(TAG,"fetchTimeSlots error:"+error.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(requestDate);
    }

    private void setSlotValues(final TimeSlots timeSlots) {
        int currentIndexToShowOnPicker=0;
        HorizontalPicker picker = (HorizontalPicker) findViewById(R.id.picker);
        ArrayList<String> times=new ArrayList<>();
        final String currentTimeHr=timeSlots.getNow().substring(0, timeSlots.getNow().indexOf(":"));
//        final String currentTimeHr="01";
        TimeSlots.DataEntity timeEntry ;

        if(timeSlots.getData()!=null&&timeSlots.getData().size()>0){
            for(int i=0;i<timeSlots.getData().size();i++){
                TimeSlots.DataEntity slot=timeSlots.getData().get(i);

                String deliverySlot=slot.getSlot();
                String startHr=deliverySlot.substring(0, deliverySlot.indexOf(":"));
                String startMin=deliverySlot.substring(deliverySlot.indexOf(":")+1,deliverySlot.indexOf("-") );
                String endHr=deliverySlot.substring(deliverySlot.indexOf("-")+1,deliverySlot.lastIndexOf(":"));
                String endMin=deliverySlot.substring(deliverySlot.lastIndexOf(":")+1);
                String shift= (Integer.parseInt(endHr) >= 12) ? "PM" : "AM";
                String time="";

                //converting time in 12 hours format
                if(Integer.parseInt(startHr)>12){
                    startHr=String.valueOf((Integer.parseInt(startHr)-12));
                    endHr=String.valueOf((Integer.parseInt(endHr)-12));
                }
                //converting time in 12 hours format
                if(Integer.parseInt(endHr)>12){
                    endHr=String.valueOf((Integer.parseInt(endHr)-12));
                }
                //eliminate minute if minute value is 00
                if(Integer.parseInt(startMin)==0){
                    time= startHr+shift+"-"+endHr+shift;
                }else{
                    time= startHr+":"+startMin+"-"+endHr+":"+endMin+shift;
                }
                times.add(time);

                //determine index of current time slot
                if(timeSlots.getActive_slot()!=null){
                    if(timeSlots.getActive_slot().getId()==slot.getId()){
                        currentIndexToShowOnPicker=i;
                        preEditor.putString(TagsPreferences.TIME_SLOT, String.valueOf(timeSlots.getData().get(currentIndexToShowOnPicker).getId())).commit();
                        isAnyTimeSlotActive=true;
                        Log.d(TAG,"currentIndexToShowOnPicker =="+currentIndexToShowOnPicker);
                    }
                }



            }

            picker.setVisibility(View.VISIBLE);
            txtCartTimeSlot.setText("SELECT TIME SLOT");
            picker.setValues(times.toArray(new CharSequence[times.size()]));
            picker.setSelectedItem(currentIndexToShowOnPicker);

            //if value of currentIndexToShowOnPicker is never changed then
            // set isTimeSlotAvailable=false so that we can't move to next page
            if(isAnyTimeSlotActive)
                isTimeSlotAvailable=true;
            else
                isTimeSlotAvailable=false;

            picker.setOnItemClickedListener(new HorizontalPicker.OnItemClicked() {
                @Override
                public void onItemClicked(int index) {
                    TimeSlots.DataEntity dataEntity = timeSlots.getData().get(index);
                    //check for time slot which have been passed for today
                    if(Constants.getMilitryTimming(dataEntity.getEnd_time().trim())<Constants.getMilitryTimming(timeSlots.getNow().trim())
                            && Alacarte.selectedDateFlag ==0){//time slot have been passed 0=today,1=tomorrow
//                        DialogUtils.showDialog(Cart.this, "Kitchen is closed for the day please pre book your order for tomorrow");
                        DialogUtils.showDialog(Cart.this, "Please select another time-slot");
                        isTimeSlotAvailable=false;
                    }else{//time slot is available now check whether it is active or not
                        if(dataEntity.getStatus().equalsIgnoreCase("Active")){
                            preEditor.putString(TagsPreferences.TIME_SLOT, String.valueOf(dataEntity.getId())).commit();
                            isTimeSlotAvailable=true;
                            Log.d(TAG, "time slot :" + prefs.getString(TagsPreferences.TIME_SLOT, "0"));
                        }else{
//                            DialogUtils.showDialog(Cart.this,"this time slot is not active for now");
                            DialogUtils.showDialog(Cart.this, "Please select another time-slot");
                            isTimeSlotAvailable=false;
                        }
                    }


                }
            });

            picker.setOnItemSelectedListener(new HorizontalPicker.OnItemSelected() {
                @Override
                public void onItemSelected(int index) {
                    TimeSlots.DataEntity dataEntity = timeSlots.getData().get(index);
                    //check for time slot which have been passed for today
                    if(Constants.getMilitryTimming(dataEntity.getEnd_time().trim())<Constants.getMilitryTimming(timeSlots.getNow().trim())
                            && Alacarte.selectedDateFlag ==0){//time slot have been passed 0=today,1=tomorrow
//                        DialogUtils.showDialog(Cart.this, "Kitchen is closed for the day please pre book your order for tomorrow");
                        DialogUtils.showDialog(Cart.this, "Please select another time-slot");
                        isTimeSlotAvailable=false;
                    }else{//time slot is available now check whether it is active or not
                        if(dataEntity.getStatus().equalsIgnoreCase("Active")){
                            preEditor.putString(TagsPreferences.TIME_SLOT, String.valueOf(dataEntity.getId())).commit();
                            isTimeSlotAvailable=true;
                            Log.d(TAG, "time slot :" + prefs.getString(TagsPreferences.TIME_SLOT, "0"));
                        }else{
//                            DialogUtils.showDialog(Cart.this,"this time slot is not active for now");
                            DialogUtils.showDialog(Cart.this, "Please select another time-slot");
                            isTimeSlotAvailable=false;
                        }
                    }

                }
            });
        }else{
            picker.setVisibility(View.GONE);
            txtCartTimeSlot.setText("NO TIME SLOTS");
//            DialogUtils.showDialog(Cart.this, "Kitchen is closed for the day");
            DialogUtils.showDialog(Cart.this, "Please select another time-slot");
            isTimeSlotAvailable=false;
        }



    }

    private void updateAddFromCart(int cartAddressPosition) {
        if(cartAddressPosition!=0){
            if(addresses.size()>0){//check if at least one address exist
                UsersAddresses temp=addresses.get(addresses.size()-1);//get last entered address
                txtAddressLine0.setText(temp.getAdd_0());
                txtAddressLine1.setText(temp.getAdd_1());
                txtAddressLine2.setText(temp.getAdd_2());
            }
        }
    }


    private void getAddress() {
        String url=Constants.getUrlUsersAddList(prefs.getString(TagsPreferences.PROFILE_USER_ID, null));
        final ProgressDialog pd=new ProgressDialog(Cart.this);
        pd.setMessage("Please wait.....\nfetching your addresses");
        pd.setCancelable(false);
        pd.show();
        JsonObjectRequest get_user_address_request=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pd.dismiss();
                Log.d(TAG,"address response: "+response.toString());
                try {
                    if(response.getString("status").equalsIgnoreCase("1"))
                        Log.d(TAG,"No addresses found");
                    else//has address in json so parse them
                    {
                        addresses.clear();
                        JSONObject dummy;
                        JSONObject dummyadd;
                        UsersAddresses userAdd;
                        JSONArray jsonAddress=response.getJSONArray("data");
                        for(int i=0;i<jsonAddress.length();i++){
                            dummy=new JSONObject();
                            dummyadd=new JSONObject();
                            userAdd=new UsersAddresses();
                            dummy=jsonAddress.getJSONObject(i);
                            dummyadd=dummy.getJSONObject("addresses");
                            Log.d(TAG,"address at "+i+" "+dummyadd.getString("0"));
                            userAdd.setAdress_id(dummy.getInt("address_id"));
                            userAdd.setAdd_0(dummyadd.getString("0"));
                            userAdd.setAdd_1(dummyadd.getString("1"));
                            userAdd.setAdd_2(dummyadd.getString("2"));
                            userAdd.setCategory(dummyadd.getString("category"));
                            userAdd.setCluster(dummyadd.getString("cluster"));
                            addresses.add(userAdd);
                        }
                        addresses.trimToSize();

                        Log.d(TAG,"addres size"+addresses.size());
                        for(int i=0;i<Cart.addresses.size();i++){
                            UsersAddresses temp=Cart.addresses.get(i);
                            Log.d(TAG,"CartActivity address at "+i+" "+temp.getAdd_0());
                        }
                        /*
                        * update last entered address to UI if exist
                        *
                        * */
                        updateAddress(addresses);
                        preEditor.putBoolean(TagsPreferences.FLAG_DOWNLOAD_USERS_ADDRESS,true).commit();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(Cart.this, "Server error", Toast.LENGTH_LONG).show();
                Log.d(TAG,"error in address json :"+error.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(get_user_address_request);
    }

    private void updateAddress(ArrayList<UsersAddresses> addresses) {
        if(addresses.size()>0){//check if at least one address exist
            preEditor.putBoolean(TagsPreferences.ADDRESS_EXIST,true).commit();
            UsersAddresses temp=addresses.get(addresses.size()-1);//get last entered address
            preEditor.putString(TagsPreferences.ADDRESS_LINE_0, temp.getAdd_0()).commit();
            preEditor.putString(TagsPreferences.ADDRESS_LINE_1, temp.getAdd_1()).commit();
            preEditor.putString(TagsPreferences.ADDRESS_LINE_2, temp.getAdd_2()).commit();
            preEditor.putString(TagsPreferences.ADDRESS_ID, String.valueOf(temp.getAdress_id())).commit();

            txtAddressLine0.setText(prefs.getString(TagsPreferences.ADDRESS_LINE_1,"First Line of Address"));
            txtAddressLine1.setText(prefs.getString(TagsPreferences.ADDRESS_LINE_2,"Secon Line of Address"));
//            txtAddressLine2.setText(prefs.getString(TagsPreferences.ADDRESS_LINE_2,"Second Line of Address"));
        }else{
            preEditor.putBoolean(TagsPreferences.ADDRESS_EXIST,false).commit();
        }
    }

    public static void updateAddress(UsersAddresses address)
    {

        preEditor.putBoolean(TagsPreferences.ADDRESS_EXIST,true).commit();
        preEditor.putString(TagsPreferences.ADDRESS_LINE_0, address.getAdd_0()).commit();
        preEditor.putString(TagsPreferences.ADDRESS_LINE_1, address.getAdd_1()).commit();
        preEditor.putString(TagsPreferences.ADDRESS_LINE_2, address.getAdd_2()).commit();
        preEditor.putString(TagsPreferences.ADDRESS_ID, String.valueOf(address.getAdress_id())).commit();
        preEditor.putString(TagsPreferences.ADDRESS_CATEGORY, address.getCategory()).commit();

        txtAddressLine0.setText(prefs.getString(TagsPreferences.ADDRESS_LINE_1,"House no"));
        txtAddressLine1.setText(prefs.getString(TagsPreferences.ADDRESS_LINE_2,"First Line of Address"));
        int icnRes=(address.getCategory().equalsIgnoreCase("home"))?R.drawable.icn_home:R.drawable.icn_office;
        imgCartAddCategory.setBackground(null);
        imgCartAddCategory.setBackgroundResource(icnRes);

//        txtAddressLine2.setText(prefs.getString(TagsPreferences.ADDRESS_LINE_2,"Second Line of Address"));
    }

    private void getLocation() {
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);


        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
//                makeUseOfNewLocation(location);
                Constants.GPS_LOCATION=location;

                String s = (Geocoder.isPresent()) ? "geocoader is present" : "geocoader is not present";
                Log.d(TAG,""+s);

                Geocoder gc=new Geocoder(Cart.this, Locale.getDefault());
                try {
                    List<Address> addresses = gc.getFromLocation(location.getLatitude(), location.getLongitude(), 2);
                    Log.d(TAG,"GPS AddressActivity: "+addresses.get(0));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
                if(status==LocationProvider.OUT_OF_SERVICE)
                  Log.d(TAG,"location provider out of service");
                if(status==LocationProvider.TEMPORARILY_UNAVAILABLE)
                    Log.d(TAG,"location provider is temporary unavailable");
                if(status==LocationProvider.AVAILABLE)
                    Log.d(TAG,"location provider is available now");
            }

            public void onProviderEnabled(String provider) {
                Log.d(TAG,"GPS : Disabled");
            }

            public void onProviderDisabled(String provider) {
                Log.d(TAG,"GPS : Disabled");
            }
        };


        // Register the listener with the Location Manager to receive location updates
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Log.d(TAG,"Home pressed");
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Cart.this,MainActivity.class));
        finish();
    }
}
