package com.firsteat.firsteat.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
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
import com.firsteat.firsteat.activities.LoginActivity;
import com.firsteat.firsteat.activities.MainActivity;
import com.firsteat.firsteat.adapters.MyOrderAdapter;
import com.firsteat.firsteat.app.AppController;
import com.firsteat.firsteat.models.MyOrders;
import com.firsteat.firsteat.utils.Constants;
import com.firsteat.firsteat.utils.DialogUtils;
import com.firsteat.firsteat.utils.TagsPreferences;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyOrder extends Fragment {

    public static final String TAG = MyOrder.class.getSimpleName();
    View rootView;
    private Context context;
    private SharedPreferences prefs;
    private SharedPreferences.Editor preEditor;

    public MyOrder() {
        // Required empty public constructor
    }

    /*
    * Crate new Instance of this fragment
    * */
    public static MyOrder newInstance() {
        return new MyOrder();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.fragment_my_order, container, false);

        context=getActivity();
        prefs = PreferenceManager
                .getDefaultSharedPreferences(getActivity().getApplicationContext());
        preEditor=prefs.edit();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("My Orders");

        final SwipeRefreshLayout srlMyOrder= (SwipeRefreshLayout) rootView.findViewById(R.id.srlMyOrder);

        srlMyOrder.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                downloadMyOrderList();
                srlMyOrder.setRefreshing(false);

            }
        });

        /*
        * Download MyOrder List
        * */
        downloadMyOrderList();


        return rootView;
    }

    private void downloadMyOrderList() {
        if(prefs.getBoolean(TagsPreferences.LOGIN_STATUS,false)){
            final ProgressDialog pd=new ProgressDialog(getActivity());
            pd.setMessage("downloading your orders list");
            pd.setCancelable(false);
            pd.show();
            String url= Constants.getUrlOrderHistory(String.valueOf(prefs.getString(TagsPreferences.PROFILE_USER_ID, "0")));
            JsonObjectRequest requestMyorder=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    pd.dismiss();
                    Log.d(TAG, "requestMyorder response: " + response.toString());

                    Gson gson=new Gson();
                    MyOrders orders=gson.fromJson(response.toString(), MyOrders.class);

                    if (orders.getStatus().equalsIgnoreCase("0")){
                        updateOrder(orders);
                    }else
                        DialogUtils.showDialog(getActivity(),"no order to display ");

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), "Server error", Toast.LENGTH_LONG).show();
                    pd.dismiss();
                    Log.d(TAG,"requestMyorder error: "+error.toString());
                }
            });
            AppController.getInstance().addToRequestQueue(requestMyorder);
        }else{
            Intent toMain=new Intent(getActivity(), LoginActivity.class);
            toMain.putExtra(TagsPreferences.NEXT, MainActivity.class.getCanonicalName());
            context.startActivity(toMain);
        }


    }

    private void updateOrder(MyOrders orders) {
        RecyclerView rv_my_order= (RecyclerView) rootView.findViewById(R.id.rv_my_order);
        rv_my_order.hasFixedSize();
        LinearLayoutManager lm=new LinearLayoutManager(getActivity());

        MyOrderAdapter adapter=new MyOrderAdapter(getActivity(),orders);
        rv_my_order.setLayoutManager(lm);
        rv_my_order.setAdapter(adapter);
    }


}
