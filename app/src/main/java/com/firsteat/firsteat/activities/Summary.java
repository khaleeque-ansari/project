package com.firsteat.firsteat.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.firsteat.firsteat.R;
import com.firsteat.firsteat.adapters.MyOrderAdapter;
import com.firsteat.firsteat.adapters.SummaryOrderItemAdapter;
import com.firsteat.firsteat.app.AppController;
import com.firsteat.firsteat.models.OrderSummary;
import com.firsteat.firsteat.models.OrderSummaryItem;
import com.firsteat.firsteat.utils.Constants;
import com.firsteat.firsteat.utils.TagsPreferences;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Summary extends AppCompatActivity {
TextView txtSummaryName,txtSummaryAddLine2,txtSummaryAddLine1, txtSummaryDateStamp,txtSummaryTimeSlot,
        txtSummarySubTotal,txtSummaryDiscount,txtSummaryShipingCharge,txtSummaryVat,txtSummaryTotal,
        txtSummarySurcharge,txtSummaryServiceTax;

    RecyclerView rv_summary;

    private static final String TAG = Summary.class.getSimpleName();
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSummary);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Order summary");
        prefs = PreferenceManager
                .getDefaultSharedPreferences(this);

        /*
        * linking of widgets
        * */
        linking();

        if(getIntent().getBooleanExtra("isfromhistory",false)){

            downloadSummary(MyOrderAdapter.orderid);
            Toast.makeText(Summary.this, "from history page", Toast.LENGTH_SHORT).show();


        }else
        {
            /*
            * set values on text view
            * */
//            setValues();
//            downloadSummaryOrderItems();

            downloadSummary(prefs.getString(TagsPreferences.ORDER_ID,"0"));
            Toast.makeText(Summary.this, "not from history page", Toast.LENGTH_SHORT).show();
        }




    }

    /*
    * download summary for an specific order
    * */
    private void downloadSummary(String orderid) {
        String url= Constants.getUrlOrderDetails(orderid);
        JsonObjectRequest requestOrderDetail=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "requestOrderDetail response :" + response.toString());
                Gson gson=new Gson();
                OrderSummary orderSummary = gson.fromJson(response.toString(), OrderSummary.class);
                //set values on textviews
                setStaticValues(orderSummary);

                List<OrderSummaryItem> orderSummaryItemList=new ArrayList<>();
                if(orderSummary.getData().getMenu_details()!=null){
                    for(OrderSummary.DataEntity.MenuDetailsEntity menu_details: orderSummary.getData().getMenu_details()){
                        OrderSummaryItem orderSummaryItem=new OrderSummaryItem();
                        orderSummaryItem.setItemName(menu_details.getMenu().getItem_name());
                        orderSummaryItem.setPrice(menu_details.getMenu().getPrice());
                        orderSummaryItem.setQuantity(menu_details.getQty());
                        orderSummaryItemList.add(orderSummaryItem);
                    }
                }
                if(orderSummary.getData().getOffers()!=null){
                    for(OrderSummary.DataEntity.OffersEntity menu_offers: orderSummary.getData().getOffers()){
                        OrderSummaryItem orderSummaryItem=new OrderSummaryItem();
                        orderSummaryItem.setItemName(menu_offers.getOffer().getOffer_name());
                        orderSummaryItem.setPrice(menu_offers.getOffer().getPrice());
                        orderSummaryItem.setQuantity(menu_offers.getOffer().getQty());
                        orderSummaryItemList.add(orderSummaryItem);
                    }
                }

                rv_summary.hasFixedSize();
                LinearLayoutManager lm=new LinearLayoutManager(Summary.this);
                SummaryOrderItemAdapter adapter= new SummaryOrderItemAdapter(Summary.this, orderSummaryItemList);

                rv_summary.setLayoutManager(lm);
                rv_summary.setAdapter(adapter);




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Summary.this,"server error",Toast.LENGTH_SHORT).show();
                Log.d(TAG,"requestOrderDetail error :"+error.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(requestOrderDetail);
    }

    /*
    * set values on textviews from downloaded json
    * */
    private void setStaticValues(OrderSummary orderSummary) {

        txtSummaryName.setText(orderSummary.getData().getUser().getFull_name());
//        txtSummaryAddLine0.setText(prefs.getString(TagsPreferences.ADDRESS_LINE_0,"House no."));
        txtSummaryAddLine1.setText(orderSummary.getData().getAddress().getAddress_line_two());
        txtSummaryAddLine2.setText(orderSummary.getData().getAddress().getAddress_line_three());

        txtSummaryShipingCharge.setText(prefs.getString(TagsPreferences.SUMMARY_SHIPING_CHARGE, "0"));
        txtSummaryTotal.setText(""+getResources().getString(R.string.Rs)+" "+orderSummary.getData().getOrder().getTotal_amount());
        txtSummarySubTotal.setText(""+getResources().getString(R.string.Rs)+" "+orderSummary.getData().getOrder().getSub_total());
        txtSummaryDiscount.setText(""+getResources().getString(R.string.Rs)+orderSummary.getData().getOrder().getDiscount());
        txtSummaryVat.setText(""+getResources().getString(R.string.Rs)+" "+orderSummary.getData().getOrder().getVat().toString());
        txtSummarySurcharge.setText(""+getResources().getString(R.string.Rs)+" "+orderSummary.getData().getOrder().getSurcharge());
//        txtSummaryServiceTax.setText(""+getResources().getString(R.string.Rs)+" "+prefs.getFloat(TagsPreferences.SERVICE_TAX,0));
        String datestamp = Constants.getDate(orderSummary.getData().getOrder().getOrdered_at());
        txtSummaryDateStamp.setText("" + orderSummary.getData().getOrder().getOrderStatuses().get(orderSummary.getData().getOrder().getOrderStatuses().size()-1).getOrder_status() + " :" +
                datestamp);
        String timeslot=Constants.getTimeSlots(orderSummary.getData().getSlot().getStart_time(),orderSummary.getData().getSlot().getEnd_time());
        txtSummaryTimeSlot.setText(timeslot);


    }

    private void downloadSummaryOrderItems() {
//        String json= prefs.getString(TagsPreferences.JSON_SERVICE_UPDATE_ORDER,"");
//        Gson gson=new Gson();
//        SummaryOrderItems summaryOrderItems = gson.fromJson(json.toString(), SummaryOrderItems.class);
//
//        rv_summary.hasFixedSize();
//        LinearLayoutManager lm=new LinearLayoutManager(Summary.this);
////        SummaryOrderItemAdapter adapter= new SummaryOrderItemAdapter(Summary.this,summaryOrderItems);
//
//        rv_summary.setLayoutManager(lm);
//        rv_summary.setAdapter(adapter);

    }

    private void setValues() {
        Toast.makeText(Summary.this, "Not from history page", Toast.LENGTH_SHORT).show();
        txtSummaryName.setText(prefs.getString(TagsPreferences.PROFILE_NAME, "Name"));
//        txtSummaryAddLine0.setText(prefs.getString(TagsPreferences.ADDRESS_LINE_0,"House no."));
        txtSummaryAddLine1.setText(prefs.getString(TagsPreferences.ADDRESS_LINE_1, "Address line 1"));
        txtSummaryAddLine2.setText(prefs.getString(TagsPreferences.ADDRESS_LINE_2, "Address line 2"));
        txtSummaryShipingCharge.setText(prefs.getString(TagsPreferences.SUMMARY_SHIPING_CHARGE, "0"));
        txtSummaryTotal.setText(""+getResources().getString(R.string.Rs)+" "+String.valueOf(prefs.getInt(TagsPreferences.TOTAL, 0)));
        txtSummarySubTotal.setText(""+getResources().getString(R.string.Rs)+" "+prefs.getInt(TagsPreferences.CART_SUB_TOTAL,0));
        txtSummaryDiscount.setText(""+getResources().getString(R.string.Rs)+" -"+prefs.getInt(TagsPreferences.COUPON_DISCOUNT,0));
        txtSummaryVat.setText(""+getResources().getString(R.string.Rs)+" "+prefs.getFloat(TagsPreferences.VAT,0f));
        txtSummarySurcharge.setText(""+getResources().getString(R.string.Rs)+" "+prefs.getFloat(TagsPreferences.SURCHARGE,0f));
        txtSummaryServiceTax.setText(""+getResources().getString(R.string.Rs)+" "+prefs.getFloat(TagsPreferences.SERVICE_TAX,0));
//        txtSummaryDateStamp.setText(prefs.getString(TagsPreferences.PROFILE_NAME,"Name"));
        String timeslot = prefs.getString(TagsPreferences.TIME_SLOT, "Name");
        Log.d(TAG,"time slot :"+timeslot);
    }

    private void linking() {
        rv_summary= (RecyclerView) findViewById(R.id.rv_summary);

        txtSummaryName= (TextView) findViewById(R.id.txtSummaryItemQty);
        txtSummaryAddLine2= (TextView) findViewById(R.id.txtSummaryAddLine2);
        txtSummaryAddLine1= (TextView) findViewById(R.id.txtSummaryAddLine1);
        txtSummaryDateStamp = (TextView) findViewById(R.id.txtSummaryDateStamp);
        txtSummaryTimeSlot= (TextView) findViewById(R.id.txtSummaryTimeSlot);

        txtSummarySubTotal= (TextView) findViewById(R.id.txtSummarySubTotal);
        txtSummaryDiscount= (TextView) findViewById(R.id.txtSummaryDiscount);
        txtSummaryShipingCharge= (TextView) findViewById(R.id.txtSummaryShipingCharge);
        txtSummaryVat= (TextView) findViewById(R.id.txtSummaryVat);
        txtSummarySurcharge= (TextView) findViewById(R.id.txtSummarySurcharge);
        txtSummaryServiceTax= (TextView) findViewById(R.id.txtSummaryServiceTax);
        txtSummaryTotal= (TextView) findViewById(R.id.txtSummaryTotal);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Summary.this, MainActivity.class));
        finish();
    }
}
