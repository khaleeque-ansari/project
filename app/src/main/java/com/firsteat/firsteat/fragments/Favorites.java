package com.firsteat.firsteat.fragments;


import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.firsteat.firsteat.R;
import com.firsteat.firsteat.adapters.FavMenuAdapter;
import com.firsteat.firsteat.app.AppController;
import com.firsteat.firsteat.models.Menu;
import com.firsteat.firsteat.utils.Constants;
import com.firsteat.firsteat.utils.TagsPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Favorites extends Fragment {
    public static final String TAG = Favorites.class.getSimpleName();
   View rootview;
    ArrayList<String> favMenuIds=new ArrayList<>();
    private SharedPreferences prefs;
    private SharedPreferences.Editor preEditor;
    private SwipeRefreshLayout srlFavorite;
    private SwipeRefreshLayout srlScrollViewFavorite;

    public Favorites() {
        // Required empty public constructor
    }

    public static Favorites newInstance(String param1, String param2) {
        return new Favorites();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager
                .getDefaultSharedPreferences(getActivity().getApplicationContext());
        preEditor=prefs.edit();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootview=inflater.inflate(R.layout.fragment_favorites, container, false);


        /*
        * Download favurite menu
        * */
        downloadFavMenu();

        srlFavorite = (SwipeRefreshLayout) rootview.findViewById(R.id.srlFavorite);
        srlFavorite.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                downloadFavMenu();
                Toast.makeText(getActivity(), "Menu List Refreshed", Toast.LENGTH_SHORT).show();
                srlFavorite.setRefreshing(false);
            }
        });
        srlScrollViewFavorite = (SwipeRefreshLayout) rootview.findViewById(R.id.srlScrollViewFavorite);
        srlFavorite.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                downloadFavMenu();
                Toast.makeText(getActivity(), "Menu List Refreshed", Toast.LENGTH_SHORT).show();
                srlFavorite.setRefreshing(false);
            }
        });
        // Inflate the layout for this fragment
        return rootview;
    }

    private void downloadFavMenu() {
        final ProgressDialog pd=new ProgressDialog(getActivity());
        pd.setMessage("Please wait.......\ndownloading your fav menu ");
        pd.setCancelable(false);
        pd.show();
        String url= Constants.getUrlUsersFavMenu(prefs.getString(TagsPreferences.PROFILE_USER_ID, null));
        JsonObjectRequest getFavMenuRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                favMenuIds.clear();
                try {
                    pd.dismiss();
                    Log.d(TAG, "fav menu downloaded :" + response.toString());
                    JSONArray data = response.getJSONArray("data");
                    for(int i=0;i<data.length();i++){
                        String menu_id = data.getJSONObject(i).getString("menu_id");
                        favMenuIds.add(menu_id);
                    }

                    /*
                    * check for data in fav list
                    * if no data then show message with refresh layout
                    * if data then show actual list
                    * */
                    if (favMenuIds!=null&&favMenuIds.size()>0){
                        srlFavorite.setVisibility(View.VISIBLE);
                        srlScrollViewFavorite.setVisibility(View.GONE);
                        //update favMenu list
                        updateFavList(favMenuIds);
                    }else{
                        srlFavorite.setVisibility(View.GONE);
                        srlScrollViewFavorite.setVisibility(View.VISIBLE);
                        //update favMenu list
                        updateFavList(favMenuIds);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    pd.dismiss();
                    Log.d(TAG,"error in fav menu json parsing : "+e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
            }
        });
        AppController.getInstance().addToRequestQueue(getFavMenuRequest);
    }

    private void updateFavList(ArrayList<String> favMenuIds) {
        if(prefs.getBoolean(TagsPreferences.FLAG_DOWNLOAD_MENU,false)){
//            DbHelper helper=new DbHelper(getActivity());
//            Menu menu = helper.fetchFavMenu(favMenuIds);


            String menu=prefs.getString(TagsPreferences.JSON_SERVICE_DOWNLOAD_MENU,"{}");
            Gson gson=new Gson();
            Menu menulist = gson.fromJson(menu, Menu.class);
            List<Menu.DataEntity> alacarteMenuItems = menulist.getData();
            List<Menu.DataEntity> favMenuList=new ArrayList<>();
            Menu menuWithFavItem=new Menu();
            for (int i = 0; i < favMenuIds.size(); i++) {
                //if id of favmenu item matches with id of the menuitem from alacarte menu list
                if(favMenuIds.get(i)==String.valueOf(alacarteMenuItems.get(i).getId())){
                    //fetch menuItem from Alacarte menu and store it to a list
                    favMenuList.add(alacarteMenuItems.get(i));
                }
            }
            menuWithFavItem.setData(favMenuList);

//
            RecyclerView rv_favourite= (RecyclerView) rootview.findViewById(R.id.rv_favourite);
            rv_favourite.hasFixedSize();
            LinearLayoutManager lm=new LinearLayoutManager(getActivity());
            rv_favourite.setLayoutManager(lm);

            FavMenuAdapter adapter=new FavMenuAdapter(getActivity(),menuWithFavItem);
            rv_favourite.setAdapter(adapter);

        }
    }


}
