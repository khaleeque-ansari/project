package com.firsteat.firsteat.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.firsteat.firsteat.R;
import com.firsteat.firsteat.adapters.MenuAdapter;
import com.firsteat.firsteat.adapters.MenuItemAdapter;
import com.firsteat.firsteat.app.AppController;
import com.firsteat.firsteat.models.Locations;
import com.firsteat.firsteat.models.Menu;
import com.firsteat.firsteat.models.UserDetails;
import com.firsteat.firsteat.utils.Constants;
import com.firsteat.firsteat.utils.DbHelper;
import com.firsteat.firsteat.utils.TagsPreferences;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Alacarte extends Fragment implements AdapterView.OnItemSelectedListener {
    public static final String TAG = Alacarte.class.getSimpleName();
    private View rootView;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MenuItemAdapter mAdapter;
    DbHelper helper;
    private Menu alacarteMenu;
    List<Locations.DataEntity> locationData =null;
    Locations.DataEntity locationDataEntry =null;
    List<Menu.DataEntity> alacarteMenuData=null;
    Menu.DataEntity alacarteMenuEntry;
    private int location;//location according to which menu will be loaded
    private SharedPreferences prefs;
    private SwipeRefreshLayout srlAlacarte,srlScrollView;
    private Spinner spiAlacarteCartDay;
    private SharedPreferences.Editor preEditor;
    public static LinearLayout customLinearLayout;
    public static  FrameLayout snackbarframe;
    public static Menu.OfferOfTheDayEntity offer_of_the_day;

    public static int selectedDateFlag;
    public static String selectedDate;

    public Alacarte() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"oncreate Alacarte");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG,"onCreateView");
        rootView=inflater.inflate(R.layout.fragment_alacarte, container, false);
        prefs = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
        preEditor=prefs.edit();
        srlAlacarte = (SwipeRefreshLayout) rootView.findViewById(R.id.srlAlacarte);
        srlScrollView = (SwipeRefreshLayout) rootView.findViewById(R.id.srlScrollView);
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view);
        spiAlacarteCartDay = (Spinner)rootView.findViewById(R.id.spiAlacarteCartDay);
        customLinearLayout= (LinearLayout) rootView.findViewById(R.id.customLinearLayout);

        /*
        * Fetch system time
        * */
        fetchAndUpdateDays();

        spiAlacarteCartDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        prefs.edit().putString(TagsPreferences.DATE,prefs.getString(TagsPreferences.DATE_TODAY,"")).commit();
                        selectedDateFlag =0;//0=today
                        selectedDate=prefs.getString(TagsPreferences.DATE_TODAY,"");
                        downloadMenu();
                        break;
                    case 1:
                        prefs.edit().putString(TagsPreferences.DATE,prefs.getString(TagsPreferences.DATE_TOMORROW,"")).commit();
                        selectedDateFlag =1;//1=tomorrow
                        selectedDate=prefs.getString(TagsPreferences.DATE_TOMORROW,"");
                        downloadMenu();
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

           /*check whether the app is running very first time or not*/
//        if (!prefs.getBoolean(TagsPreferences.FLAG_DOWNLOAD_MENU,false)){
//            Log.d(TAG, "downloding menu if part");
//            // Download menu and store it to the local database
//
//        }else{
//            Log.d(TAG,"downloding menu else part");
//            updateMenuWith();
//        }


//        MainActivity.showToolbarLayout();

//        Toolbar toolbarmain = (Toolbar) getActivity().findViewById(R.id.toolbar);
//        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbarAlacarte);
//        toolbarmain.setVisibility(View.GONE);
//        AppCompatActivity activity = (AppCompatActivity) getActivity();
//        activity.setSupportActionBar(toolbar);
//
//        Spinner spiLocation= (Spinner) getActivity().findViewById(R.id.spiMainLocation);
//        spiLocation.setOnItemSelectedListener(this);


        srlAlacarte.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                downloadMenu();


//                /*
//                * Get user's detail every time theu refresh if user already logged in
//                * */
//                if(prefs.getBoolean(TagsPreferences.LOGIN_STATUS,false)){
//                    Toast.makeText(getActivity(), "Menu & user details List Refreshed", Toast.LENGTH_SHORT).show();
//                    getUsersDetails(prefs.getString(TagsPreferences.PROFILE_USER_ID,"0"));
//                }else{
//                    Toast.makeText(getActivity(), "Menu List Refreshed", Toast.LENGTH_SHORT).show();
//                }
                srlAlacarte.setRefreshing(false);
            }
        });

        srlScrollView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                downloadMenu();
//                Toast.makeText(getActivity(), "Menu List Refreshed", Toast.LENGTH_SHORT).show();
                srlScrollView.setRefreshing(false);
            }
        });




//        mAdapter = new MenuItemAdapter();
//        mRecyclerView.setAdapter(mAdapter);
        // Inflate the layout for this fragment
        return rootView;
    }

    private void fetchAndUpdateDays() {


        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();

        DateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMM");

        DateFormat dateFormatForJson = new SimpleDateFormat("yyyy-MM-dd");
        prefs.edit().putString(TagsPreferences.DATE_TODAY,dateFormatForJson.format(today)).commit();
        prefs.edit().putString(TagsPreferences.DATE_TOMORROW,dateFormatForJson.format(tomorrow)).commit();

        String todayAsString = dateFormat.format(today);
        String tomorrowAsString = dateFormat.format(tomorrow);

        Log.d(TAG, "date " + todayAsString);
        Log.d(TAG, "date " + tomorrowAsString);
        Log.d(TAG, "datejson " + prefs.getString(TagsPreferences.DATE_TODAY,""));
        Log.d(TAG, "datejson " + prefs.getString(TagsPreferences.DATE_TOMORROW, ""));

        ArrayList<String> days=new ArrayList<>();
        days.add("Today ("+todayAsString+")");
        days.add("Tomorrow ("+tomorrowAsString+")");
        days.trimToSize();

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item,days);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spiAlacarteCartDay.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        if(MainActivity.locations!=null)
//        {
//            Locations.DataEntity location = MainActivity.locations.getData().get(position);
//            Log.d(TAG,"location id test "+location.getId());
//            prefs.edit().putInt(DbHelper.TAG_LOCATION_ID,location.getId()).commit();
//
//        }
//
//        Toast.makeText(getActivity(), "check alcartemenu "+position, Toast.LENGTH_SHORT).show();
//        Log.d(TAG, "check alararteMenu " + position);
//        Log.d(TAG,"Location_id in preferences "+prefs.getInt(DbHelper.TAG_LOCATION_ID,-1));
//
//        if(prefs.getInt(DbHelper.TAG_LOCATION_ID,-1)>=0&&prefs.getBoolean(DbHelper.TAG_FLAG_CREATE_TABLE_MENUITEM,false)){//if >=0 e.i-location_id has been stored in preferences
//
//            Log.d(TAG,"whether menu created in db "+prefs.getBoolean(DbHelper.TAG_FLAG_CREATE_TABLE_MENUITEM, false));
//            Menu menu = helper.fetchMenu(prefs.getInt(DbHelper.TAG_LOCATION_ID, -1));//pass location id to fetch corresponding menu
//
//            if(menu.getData().size()>0)
//            updateMenuWith();//pass menu to update alacarte list
//            else
//                Toast.makeText(getActivity(), "Currently their is no service in the selected area", Toast.LENGTH_SHORT).show();
//        }

//
//        if(locationData!=null){
//            Toast.makeText(getActivity(), "id :" + locationDataEntry.getId() + "lat: " + locationDataEntry.getLatitude(), Toast.LENGTH_LONG).show();
//            if(alacarteMenu!=null) {
//                alacarteMenuData=alacarteMenu.getData();
//                alacarteMenuEntry = alacarteMenuData.get(position);
//
//
//
//                alacarteMenu=helper.fetchMenu(location);
//
//                updateMenuWith(alacarteMenu);
//            }
//        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
//        Toast.makeText(getActivity(), "no item selected", Toast.LENGTH_SHORT).show();

    }

    private void downloadMenu() {
        Log.d(TAG,"date before adding in menu all services "+prefs.getString(TagsPreferences.DATE,""));
        String url=Constants.URL_MENU_ALL_ALACARTE+"?d="+prefs.getString(TagsPreferences.DATE,"");
        JsonObjectRequest downloadMenuAlacarte=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                prefs.edit().putBoolean(TagsPreferences.FLAG_DOWNLOAD_MENU, true).commit();
                prefs.edit().putString(TagsPreferences.JSON_SERVICE_DOWNLOAD_MENU, response.toString()).commit();


                Log.d(TAG, "menu has been downloaded");
                Log.d(TAG, "menu response: " + prefs.getString(TagsPreferences.JSON_SERVICE_DOWNLOAD_MENU, null));

                Gson gson=new Gson();
                Menu menu=gson.fromJson(response.toString(), Menu.class);

                if(menu.getData()!=null){
                    //extract offer of the day and store it as public variable so that it could be used in cartAdapter
                    offer_of_the_day = menu.getOffer_of_the_day();

                    //set redeam point value 1point=? Rs.
                    prefs.edit().putInt(TagsPreferences.REDEEM_POINT_VALUE, menu.getNum_points() / menu.getPoint_value()).commit();
                }

                if (menu.getData()!=null && menu.getData().size()>0){
                    srlAlacarte.setVisibility(View.VISIBLE);
                    srlScrollView.setVisibility(View.GONE);
                    updateMenuWith();
                }else{
                    srlAlacarte.setVisibility(View.GONE);
                    srlScrollView.setVisibility(View.VISIBLE);
//                    DialogUtils.showDialog(getActivity(), "there is no menu to show ", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            downloadMenu();
//                        }
//                    });
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(downloadMenuAlacarte);
    }

    private void updateMenuWith() {
        mRecyclerView.setHasFixedSize(false);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager.setRecycleChildrenOnDetach(false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        Gson gson=new Gson();
        alacarteMenu =new Menu();

        String json=prefs.getString(TagsPreferences.JSON_SERVICE_DOWNLOAD_MENU,"");
        Log.d(TAG,"json "+json);
        alacarteMenu =gson.fromJson(prefs.getString(TagsPreferences.JSON_SERVICE_DOWNLOAD_MENU,""), Menu.class);
        Log.d(TAG, "updating ");



        int calories = alacarteMenu.getData().get(0).getFood_details().getCalories();
        Log.d(TAG,"caleroeies "+calories);

        MenuAdapter menuAdapter=new MenuAdapter(getActivity(),alacarteMenu);
        mRecyclerView.setAdapter(menuAdapter);
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
                preEditor.putString(TagsPreferences.PROFILE_USER_REFERAL_POINT,userDetails.getData().get(0).getUserPoints().getTotal_points()).commit();
                preEditor.putString(TagsPreferences.PROFILE_USER_ROLE,userDetails.getData().get(0).getUserRole().getRole_name()).commit();
                preEditor.putString(TagsPreferences.PROFILE_USER_REDEEM_POINT,userDetails.getData().get(0).getUserPoints().getTotal_points()).commit();

//                MainActivity.updateHeader();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,"getUsersDetails error:"+error.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(requestUsersDetail);
    }

}
