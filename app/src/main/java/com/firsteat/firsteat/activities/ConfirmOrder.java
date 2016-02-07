package com.firsteat.firsteat.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.firsteat.firsteat.R;
import com.firsteat.firsteat.app.AppController;
import com.firsteat.firsteat.fragments.MyOrder;
import com.firsteat.firsteat.models.OrderSummary;
import com.firsteat.firsteat.utils.Constants;
import com.firsteat.firsteat.utils.DialogUtils;
import com.firsteat.firsteat.utils.TagsPreferences;

import org.json.JSONObject;

public class ConfirmOrder extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = ConfirmOrder.class.getSimpleName();
    TextView txtConfirmOrderTotal,txtConfirmOrderId,txtConfirmOrderAddress,txtConfirmOrderName,txtConfirmOrderDate,txtConfirmOrderTime;
    private ConfirmOrder context;
    private SharedPreferences prefs;
    private SharedPreferences.Editor preEditor;
    ImageView icnConfirmOrderSummary;
    private OrderSummary orderSummary;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        context=ConfirmOrder.this;
        prefs = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        preEditor=prefs.edit();

        txtConfirmOrderTotal= (TextView) findViewById(R.id.txtConfirmOrderTotal);
        txtConfirmOrderId= (TextView) findViewById(R.id.txtConfirmOrderId);
        txtConfirmOrderAddress= (TextView) findViewById(R.id.txtConfirmOrderAddress);
        txtConfirmOrderName= (TextView) findViewById(R.id.txtConfirmOrderName);
        txtConfirmOrderDate= (TextView) findViewById(R.id.txtConfirmOrderDate);
        txtConfirmOrderTime= (TextView) findViewById(R.id.txtConfirmOrderTime);
        icnConfirmOrderSummary= (ImageView) findViewById(R.id.icnConfirmOrderSummary);

        //download summary
        downloadSummary(prefs.getString(TagsPreferences.ORDER_ID,"0"));


        int[] btns={
                R.id.btnConfirmOrderAgain,
                R.id.btnConfirmOrderHistory
        };
        for(int id:btns){
            Button b= (Button) findViewById(id);
            b.setOnClickListener(this);
        }


        icnConfirmOrderSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConfirmOrder.this,Summary.class));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.btnConfirmOrderAgain:
                startActivity(new Intent(ConfirmOrder.this,MainActivity.class));
                break;
            case  R.id.btnConfirmOrderHistory:
                Intent toMainIntent=new Intent(ConfirmOrder.this,MainActivity.class);
                toMainIntent.putExtra(TagsPreferences.NEXT, MyOrder.class.getCanonicalName());
                startActivity(toMainIntent);

                break;
        }
    }

    /*
   * Download summary for the current order
   * */
    private void downloadSummary(String orderId) {
        String url= Constants.getUrlOrderDetails(orderId);
        JsonObjectRequest requestOrderDetailPreConfirm=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "requestOrderDetailPreConfirm response :" + response.toString());

                Gson gson=new Gson();
                orderSummary = gson.fromJson(response.toString(), OrderSummary.class);

                setValues(orderSummary);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ConfirmOrder.this, "server error", Toast.LENGTH_SHORT).show();
                Log.d(TAG,"requestOrderDetailPreConfirm error :"+error.toString());
                DialogUtils.showDialog(context, "something went order summary not downloaded");
            }
        });
        AppController.getInstance().addToRequestQueue(requestOrderDetailPreConfirm);
    }

    /*
    * set values on textviews
    * */
    private void setValues(OrderSummary orderSummary) {

        txtConfirmOrderId.setText("Order ID: "+orderSummary.getData().getOrder().getId());
        txtConfirmOrderName.setText(orderSummary.getData().getUser().getFull_name());
        txtConfirmOrderAddress.setText(orderSummary.getData().getAddress().getAddress_line_three());
        txtConfirmOrderTotal.setText(""+getResources().getString(R.string.Rs)+" "+orderSummary.getData().getOrder().getTotal_amount());
        txtConfirmOrderTime.setText(Constants.getTimeSlots(orderSummary.getData().getSlot().getStart_time(),orderSummary.getData().getSlot().getEnd_time()));
        txtConfirmOrderDate.setText("Ordered at: "+Constants.getDate(orderSummary.getData().getOrder().getOrdered_at()));
    }
}
