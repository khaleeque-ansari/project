package com.firsteat.firsteat.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.firsteat.firsteat.R;
import com.firsteat.firsteat.adapters.CartAdapter;
import com.firsteat.firsteat.app.AppController;
import com.firsteat.firsteat.fragments.Alacarte;
import com.firsteat.firsteat.models.CouponJson;
import com.firsteat.firsteat.models.DistenceMatrix;
import com.firsteat.firsteat.models.Kitchens;
import com.firsteat.firsteat.models.Menu;
import com.firsteat.firsteat.utils.Constants;
import com.firsteat.firsteat.utils.DialogUtils;
import com.firsteat.firsteat.utils.TagsPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PreConfirmOrder extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = PreConfirmOrder.class.getSimpleName();
    private Context context;
    private SharedPreferences prefs;
    private SharedPreferences.Editor preEditor;
    private int changeFor=0;
    ArrayList<Menu.DataEntity.FoodDetailsEntity.OffersEntity> redemedOffers=new ArrayList<>();
    private int totalprice;
    private int totalitem;
    private ProgressDialog dialogOrderItems;
    EditText edtCO_CoupounCode;
    private ArrayList<HashMap<String, Menu.DataEntity>> mainCartItem=new ArrayList<>();
    private TextView txtCO_TotalPrice;
    private Button btnCO_CoupounCode;
    private Button btnCO_ReedeamPoint;
    private TextView txtCO_Redeem;
    private String menuItemIds="",menuItemIdsWithCount="";
    private String menuItemQtys=null;
    private boolean isCouponApplied=false;
    private String redeamedCoupon="NULL";
    int mainItemCount=0,offerItemCount=0,mainItemTotal=0,offerItemTotal=0;
    private String couponId="";
    private boolean isPointRedeemed=false;
    private Button btnConfirm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_confirm_order);

        context=PreConfirmOrder.this;
        prefs = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        preEditor=prefs.edit();

        mainCartItem=CartAdapter.cartItems;
        redemedOffers=CartAdapter.redemedOffers;

        //calculate main and offer item seperatly
        calMainNOfferSep();

        //        redemedOffers = CartAdapter.redemedOffers;
//        ArrayList<HashMap<String, Menu.DataEntity>> itemsInCart= CartAdapter.cartItems;
//        totalprice=CartAdapter.totalprice;
//        totalitem= CartAdapter.totalitem;
        dialogOrderItems =new ProgressDialog(context);
        dialogOrderItems.setCancelable(false);

        edtCO_CoupounCode= (EditText) findViewById(R.id.edtCO_CoupounCode);
        txtCO_TotalPrice= (TextView) findViewById(R.id.txtCO_TotalPrice);
        btnCO_CoupounCode= (Button) findViewById(R.id.btnCO_CoupounCode);
        btnConfirm= (Button) findViewById(R.id.btnConfirm);
        btnCO_ReedeamPoint= (Button) findViewById(R.id.btnCO_ReedeamPoint);
        txtCO_Redeem= (TextView) findViewById(R.id.txtCO_Redeem);



        txtCO_Redeem.setText("You have " + prefs.getString(TagsPreferences.PROFILE_USER_REDEEM_POINT, "0") + " Points to redeem");
        int i = calculateTotalAmountWithoutCoupon(prefs.getInt(TagsPreferences.CART_SUB_TOTAL, 0));
        txtCO_TotalPrice.setText("" + getResources().getString(R.string.Rs) +
                calculateTotalAmountWithoutCoupon(prefs.getInt(TagsPreferences.CART_SUB_TOTAL, 0)));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarConfirmOrder);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Confirm order");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int[] btns={
                R.id.btnCO_CoupounCode,
                R.id.btnCO_ReedeamPoint,
                R.id.btnConfirm,
                R.id.btnCO_BringChange_100,
                R.id.btnCO_BringChange_500,
                R.id.btnCO_BringChange_1000
        };

        for(int id:btns){
            Button b= (Button) findViewById(id);
            b.setOnClickListener(this);
        }

    }

    /*
    * calculate main and offer item seperatly
    * */
    private void calMainNOfferSep() {

        ArrayList<HashMap<String, Menu.DataEntity>> cartItems = CartAdapter.cartItems;
        for (int i = 0; i < cartItems.size(); i++) {
            HashMap<String, Menu.DataEntity> itemWithCount = cartItems.get(i);
            for (String key:itemWithCount.keySet()){
                Menu.DataEntity Mainitem = itemWithCount.get(key);
                int count=Integer.parseInt(key);
                mainItemCount=(count+mainItemCount);
                int price= Mainitem.getPrice();
                mainItemTotal=(mainItemTotal+(count*price));
            }
        }
        ArrayList<Menu.DataEntity.FoodDetailsEntity.OffersEntity> redemedOffers = CartAdapter.redemedOffers;
        offerItemCount=redemedOffers.size();
        for (int j = 0; j < redemedOffers.size(); j++) {
            Menu.DataEntity.FoodDetailsEntity.OffersEntity offersEntity = redemedOffers.get(j);
            offerItemTotal=(offerItemTotal+offersEntity.getPrice());
        }
    }


    /*
    * calculate total amount without redeem and discount
    * */
    private int calculateTotalAmountWithoutCoupon(int subtotal) {

        Log.d(TAG, "subtotal=" + subtotal);
        /*
         * Calculate Vat
         * */
        float vat= Float.valueOf((float) ((subtotal*12.5)/100)); Log.d(TAG,"vat="+vat);
        preEditor.putFloat(TagsPreferences.VAT, vat).commit();//12.5% of subtotal
        Log.d(TAG, "vat in preferences=" + prefs.getFloat(TagsPreferences.VAT, 0f));

        /*
         * Calculating surcharge
         * */
        float surcharge=(vat*5)/100;Log.d(TAG, "surcharge=" + surcharge);
        preEditor.putFloat(TagsPreferences.SURCHARGE, surcharge).commit();//5% of subtotal
        Log.d(TAG, "surcharge in preference=" + prefs.getFloat(TagsPreferences.SURCHARGE, 0f));
        /*
        * Calculate ServiceTax
        * */
//        float serviceTax=(vat*5)/100; Log.d(TAG,"serviceTax="+serviceTax);//5% of subtotal+vat+surcharge
//        preEditor.putFloat(TagsPreferences.SERVICE_TAX, serviceTax).commit();
//        Log.d(TAG, "serviceTax in preference=" + prefs.getFloat(TagsPreferences.SERVICE_TAX, 0f));

     /*
     * Calculating total
     * */
        int TotalAmount= (int) (subtotal+surcharge+vat); Log.d(TAG,"total amount="+TotalAmount);
        preEditor.putInt(TagsPreferences.TOTAL,TotalAmount).commit();

        return TotalAmount;
    }

    /*
    * Add new order
    * 1st of 3 services for ordering item
    * */
    private void addNewOrder(int kitchenId) {

        dialogOrderItems.setMessage("placing your order");
        dialogOrderItems.show();
        String url= Constants.getUrlAddNewOrder(prefs.getString(TagsPreferences.PROFILE_USER_ID, null), String.valueOf(changeFor), couponId, String.valueOf(kitchenId));
        JsonObjectRequest placeNewOrder=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "place new order response " + response.toString());
                try {

                    String orderId=response.getJSONObject("data").getString("order_id");
                    preEditor.putString(TagsPreferences.ORDER_ID,orderId).commit();

                    addOrderItems(orderId, mainCartItem);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PreConfirmOrder.this, "Server error", Toast.LENGTH_LONG).show();
                dialogOrderItems.dismiss();
                Log.d(TAG, "error in place new order response " + error.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(placeNewOrder);

    }
    /*
    * Addre order items
    * 2nd A of 3 services for ordering item
    * */
    private void addOrderItems(final String orderId, ArrayList<HashMap<String, Menu.DataEntity>> cartItems) {
        //fetch menu item ids and their quantities
        fetchMenuItemIdsAndQttys(cartItems);

        String url= Constants.getUrlAddOrderItem(orderId, menuItemIds, menuItemQtys);
        JsonObjectRequest requestAddOrderitems=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "addOrderItems response :" + response.toString());
                Log.d(TAG, "Order items are successfully added");

                /*
                * final service for cart to update order
                * */

                if(redemedOffers!=null){
                    addOffers(orderId,CartAdapter.redemedOffers);
                }else{
                    updateOrder(orderId, String.valueOf(prefs.getInt(TagsPreferences.CART_SUB_TOTAL,0)));
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(PreConfirmOrder.this,"Server error",Toast.LENGTH_LONG).show();
                dialogOrderItems.dismiss();
                Log.d(TAG, "Something went wrong \nOrder items were not added successfully try again");
                //rollback order
                rollBackOrder(orderId);
            }
        });
        AppController.getInstance().addToRequestQueue(requestAddOrderitems);




    }

    /*
    * Rollback the order if any error occured during placing order
    * */
    private void rollBackOrder(final String orderId) {
        String url=Constants.getUrlRollbackOrder(orderId);
        JsonObjectRequest rollBackOrderRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.d(TAG,"Previous order is cancelled order id ="+orderId);
                DialogUtils.showDialog(PreConfirmOrder.this,"We are sorry we couldn't place your order try again");
                btnConfirm.setEnabled(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(PreConfirmOrder.this,"server error",Toast.LENGTH_SHORT).show();
                Log.d(TAG,"Cancellation of order is failed order id ="+orderId);
                DialogUtils.showDialog(PreConfirmOrder.this,"We are sorry we couldn't place your order try again");
                btnConfirm.setEnabled(true);
            }
        });
        AppController.getInstance().addToRequestQueue(rollBackOrderRequest);
    }

    /*
    * add offers to the order
    * 2nd B of 3 services for ordering item
    * */
    private void addOffers(final String orderId, ArrayList<Menu.DataEntity.FoodDetailsEntity.OffersEntity> redemedOffers) {
        final String order_id=orderId;
        ArrayList<String> offerIDs=new ArrayList<>();
        Menu.DataEntity.FoodDetailsEntity.OffersEntity offer;
        for(int i=0;i<redemedOffers.size();i++){
            offer = redemedOffers.get(i);
            offerIDs.add(String.valueOf(offer.getId()));
        }
        String url=Constants.getUrlAddOrderOffers(orderId, offerIDs);
        JsonObjectRequest reqestAddOffers=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "addOffers response: " + response.toString());
                //update order
                updateOrder(order_id, String.valueOf(prefs.getInt(TagsPreferences.CART_SUB_TOTAL, 0)));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PreConfirmOrder.this,"Server error",Toast.LENGTH_LONG).show();
                Log.d(TAG, "addOffers error: " + error.toString());
                //rollback order
                rollBackOrder(orderId);
            }
        });
        AppController.getInstance().addToRequestQueue(reqestAddOffers);
    }

    /*
   * Update order
   * 3rd of 3 services for ordering item
   * */
    private void updateOrder(final String orderId, String subTotal) {
        String addId=prefs.getString(TagsPreferences.ADDRESS_ID,"0");
        String timeSlot=prefs.getString(TagsPreferences.TIME_SLOT, "1");
        int total = prefs.getInt(TagsPreferences.TOTAL, 0);
        String url=Constants.getUrlUpdateOrder(orderId,
                subTotal,
                String.valueOf(prefs.getInt(TagsPreferences.COUPON_DISCOUNT, 0)),
                String.valueOf(prefs.getFloat(TagsPreferences.VAT, 0f)),
                String.valueOf(prefs.getFloat(TagsPreferences.SURCHARGE, 0f)),
                String.valueOf(prefs.getInt(TagsPreferences.TOTAL, 0)),
                addId, timeSlot,
                Alacarte.selectedDate);
        JsonObjectRequest requestUpdateOrder=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dialogOrderItems.dismiss();
                Log.d(TAG, "update order response :" + response.toString());
                //store response for using it further in summary activity
                preEditor.putString(TagsPreferences.JSON_SERVICE_UPDATE_ORDER,response.toString()).commit();

                DialogUtils.showDialog(PreConfirmOrder.this, "your order for main items have been successfully placed");
//                Toast.makeText(context, "your order for main items have been successfully placed", Toast.LENGTH_LONG).show();

                Activity activity= (Activity) context;
                context.startActivity(new Intent(activity, ConfirmOrder.class));
                activity.finish();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PreConfirmOrder.this,"Server error",Toast.LENGTH_LONG).show();
                dialogOrderItems.dismiss();
                Log.d(TAG,"update order response error :"+error.toString());
                //rollback order
                rollBackOrder(orderId);
            }
        });
        AppController.getInstance().addToRequestQueue(requestUpdateOrder);

    }

    /*
     * fetch menu item ids and their quantities from cartitem
     * these ids and qttys will be passed into addItem service and redeemcoupon service
     * */
    private void fetchMenuItemIdsAndQttys(ArrayList<HashMap<String, Menu.DataEntity>> cartItems) {

        ArrayList<String> menuIds=new ArrayList<>();
        ArrayList<String> menuIdsWithCount=new ArrayList<>();
        ArrayList<String> quantities=new ArrayList<>();
        Menu.DataEntity item;
        for(HashMap<String, Menu.DataEntity> cartItem:cartItems){
            for(String key:cartItem.keySet()){
                int count=Integer.parseInt(key);
                item = cartItem.get(key);
                menuIds.add(String.valueOf(item.getId()));
                //add menu item to menuIdsWithCount for value of "count" times
                for (int i = 0; i < count; i++) {
                    menuIdsWithCount.add(String.valueOf(item.getId()));
                }
                quantities.add(String.valueOf(count));
            }
        }

        /*
        * convert arraylist of Ids in simple string
        * each item seperated by ","
        * */
        menuItemIds=convertMenuIdsToString(menuIds);
        menuItemIdsWithCount=convertMenuIdsToString(menuIdsWithCount);

        for(int i=0;i<quantities.size();i++){
            if(i==0)
                menuItemQtys=quantities.get(i).toString();
            else
                menuItemQtys=menuItemQtys+","+quantities.get(i).toString();
        }
        Log.d(TAG, "quantities :" + menuItemQtys);

    }

    private String convertMenuIdsToString(ArrayList<String> menuIds) {
        String menuString="";
        for(int i=0;i<menuIds.size();i++){
            if(i==0)
                menuString=menuIds.get(i).toString();
            else
                menuString=menuString+","+menuIds.get(i).toString();
        }
        Log.d(TAG, "menuItemIds :" + menuString.toString());

        return menuString;
    }


    /*
    * fetch kitchenId to place order
    * */
    private void getKitechId() {
        String originLat=MainActivity.deiveryLocationDataEntry.getLatitude();
        String originLon=MainActivity.deiveryLocationDataEntry.getLongitude();

        String kitchenJson = prefs.getString(TagsPreferences.JSON_KITCHEN_AREA_LOCATIONS, "{}");
        Gson gson=new Gson();
        final Kitchens kitchens = gson.fromJson(kitchenJson,Kitchens.class);
        String url = Constants.getUrlGoogleDistenceMatrix(originLat,
                originLon, kitchens);
        Log.d(TAG, "distence matrix api url: " + url);

        JsonObjectRequest requestDistenceMatrix = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "requestDistenceMatrix response :" + response.toString());
                Gson gson = new Gson();
                DistenceMatrix matrix = gson.fromJson(response.toString(), DistenceMatrix.class);

                /*
                * since the service area radius is previously determined in Splash activity so we don't have to that
                * at this point user is in service area (if not user could not reach here)
                * so we only need to check which kitchen is nearest
                * Check which kitchen is nearest to the location and set DELIVERY_KITCHEN_ID to the preferences
                * */
                int distence=0;
                for (int i = 0; i < matrix.getRows().get(0).getElements().size(); i++) {
                    DistenceMatrix.RowsEntity.ElementsEntity destination = matrix.getRows().get(0).getElements().get(i);
                    if(i==0){
                        distence= destination.getDistance().getValue();
                        preEditor.putInt(TagsPreferences.DELIVERY_KITCHEN_ID,kitchens.getData().get(i).getId()).commit();
                    }else if(destination.getDistance().getValue()<distence){
                        distence=destination.getDistance().getValue();
                        preEditor.putInt(TagsPreferences.DELIVERY_KITCHEN_ID,kitchens.getData().get(i).getId()).commit();
                    }
                }

                if(isPointRedeemed){
                    //actually redeam point before placing order
                    getRedeemPoint();
                }else{
                /*
                * pass the kitchen id in addNewOrder to place a new order in that kitchen
                * */
                    addNewOrder(prefs.getInt(TagsPreferences.DELIVERY_KITCHEN_ID,-1));
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PreConfirmOrder.this,"server error",Toast.LENGTH_SHORT).show();
                Log.d(TAG, "requestDistenceMatrix error :" + error.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(requestDistenceMatrix);
    }

    /*
    * Selector on change for buttons
    * */
    private void selectedChangeFor(View v) {
        int[] changeForBtns={
                R.id.btnCO_BringChange_100,
                R.id.btnCO_BringChange_500,
                R.id.btnCO_BringChange_1000
        };
        for(int id:changeForBtns){
            Button b= (Button) findViewById(id);
            b.setSelected(false);
        }
        v.setSelected(true);
    }

    /*
    * switch selection
    * */
    private void switchSelection(View v) {
        View view=v;
        view.setPressed(!(view.isSelected()));
    }

    /*
    * Redeem available point of the user
    * */
    private void getRedeemPoint() {
        int totalRedeempoint = Integer.parseInt(prefs.getString(TagsPreferences.PROFILE_USER_REDEEM_POINT, "0"));

        final ProgressDialog pd=new ProgressDialog(PreConfirmOrder.this);
        pd.setCancelable(false);
        pd.setMessage("Please wait...");
        pd.show();

        //pointWhichCanBeRedeem=total*redeampointvalue
        int pointWhichCanBeRedeem=prefs.getInt(TagsPreferences.TOTAL,0)*prefs.getInt(TagsPreferences.REDEEM_POINT_VALUE,1);
        int redeemPoint=(pointWhichCanBeRedeem<totalRedeempoint)?pointWhichCanBeRedeem:totalRedeempoint;

        String url=Constants.getUrlReedeamPoint(prefs.getString(TagsPreferences.PROFILE_USER_ID, "0"),
                String.valueOf(redeemPoint));
        JsonObjectRequest requestRedeemPoint= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pd.dismiss();

                try {
                    if(response.getString("status").equalsIgnoreCase("0")){


//                        preEditor.putInt(TagsPreferences.COUPON_DISCOUNT,
//                                prefs.getInt(TagsPreferences.COUPON_DISCOUNT,0)+//previous value of discount
//                                        response.getJSONObject("data").getInt("discount")).commit();//+new discount

                        //update user's point as its realy redeemed now
                        preEditor.putString(TagsPreferences.PROFILE_USER_REDEEM_POINT,String.valueOf(response.getJSONObject("data").getInt("balance_points"))).commit();

//                        DialogUtils.showDialog(context, "FE points redeem successful" +
//                                "\nYou have redeemed " + response.getJSONObject("data").getString("points_redeemed") +" points"+
//                                "\nYour have " + String.valueOf(response.getJSONObject("data").getInt("balance_points"))+" points remaining ");
//                        updateTotalAmountAfterRedeem(response.getJSONObject("data").getInt("discount"));
                    }else
                        DialogUtils.showDialog(PreConfirmOrder.this, "something went wrong we couldn't redeem your point");
//                        Toast.makeText(PreConfirmOrder.this, "something went wrong we couldn't redeem your point", Toast.LENGTH_SHORT).show();


                    /*
                    * place order after actually redeem points
                    * */
                    addNewOrder(prefs.getInt(TagsPreferences.DELIVERY_KITCHEN_ID, -1));

                } catch (JSONException e) {
                    pd.dismiss();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PreConfirmOrder.this,"server error",Toast.LENGTH_SHORT).show();
                pd.dismiss();
                Log.d(TAG,"getRedeemPoint error: "+error.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(requestRedeemPoint);


    }

    /*
    * update total amount after reedeam
    * */
    private void updateTotalAmountAfterRedeem(int redeemAmount){
//        int newTotalAmount = prefs.getInt(TagsPreferences.TOTAL, 0) - redeemAmount;
//        preEditor.putInt(TagsPreferences.TOTAL, newTotalAmount).commit();

        int cartSubtotal= prefs.getInt(TagsPreferences.CART_SUB_TOTAL,0)-redeemAmount;
        calculateTotalAmountWithoutCoupon(cartSubtotal);

    }


    /*
    * validate coupon
    * */
    private void validateCoupouns() {


        Log.d(TAG, "coupon logic normal");
        String url=Constants.getUrlCoupounValidation(edtCO_CoupounCode.getText().toString().trim(),
                prefs.getString(TagsPreferences.PROFILE_USER_ID, "0"),
                String.valueOf(prefs.getInt(TagsPreferences.CART_SUB_TOTAL, 0)), menuItemIds,menuItemQtys);
                JsonObjectRequest requestValidatecoupon= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "validateCoupouns response :" + response.toString());


                try {
                    if(response.getString("status").equalsIgnoreCase("0")){
                        Gson gson=new Gson();
                        CouponJson couponJson = gson.fromJson(response.toString(), CouponJson.class);
                        couponId=String.valueOf(couponJson.getData().getId());

                       if(couponJson.getFinalX()==0&&mainItemCount==5){//this is team 250 coupon
                            calculateTotalAmountWithTEAM250();
                        }else{// this is normal coupon

                            //deduct coupon amount from total
                            txtCO_TotalPrice.setText("" + getResources().getString(R.string.Rs) + " " + calculateTotalAmountWithCoupon(couponJson));
                            DialogUtils.showDialog(PreConfirmOrder.this, "Your coupon has been applied");
                            edtCO_CoupounCode.setText("");
                            redeamedCoupon=String.valueOf(couponJson.getData().getId());
                            isCouponApplied=true;
                        }

                    }else if(response.getString("status").equalsIgnoreCase("1")){
                        DialogUtils.showDialog(PreConfirmOrder.this, "Coupon not found");
//                        Toast.makeText(PreConfirmOrder.this,"Coupon not found",Toast.LENGTH_LONG).show();
                    }else
                        DialogUtils.showDialog(PreConfirmOrder.this, "Coupon not valid for the current item");
//                        Toast.makeText(PreConfirmOrder.this,"Coupon not valid for the current item",Toast.LENGTH_LONG).show();

                    //enable apply coupon button
                    btnCO_CoupounCode.setEnabled(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PreConfirmOrder.this,"Server error",Toast.LENGTH_LONG).show();
                //enable apply coupon button
                btnCO_CoupounCode.setEnabled(true);
                DialogUtils.showDialog(PreConfirmOrder.this,"Sorry the coupon is not applied try again");
                Log.d(TAG,"validateCoupouns error :"+error.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(requestValidatecoupon);

    }

    /*
    * TEAM 250 coupon
    * */
    private void calculateTotalAmountWithTEAM250() {

        if(mainItemTotal>250) {//apply 250 logic
            /*
            * calculate discount from the total amount before applying team 250 coupon
            * discount=discount+(total-250)
            * */
            preEditor.putInt(TagsPreferences.COUPON_DISCOUNT,
                    prefs.getInt(TagsPreferences.COUPON_DISCOUNT,0)+prefs.getInt(TagsPreferences.TOTAL,0)-250).commit();

            /*
            * mainitemTotal replaced with 250
            * subtotal=offerTotal+250
            * then calculate other variables with without coupon logic
            * */
            preEditor.putInt(TagsPreferences.CART_SUB_TOTAL, 250+offerItemTotal).commit();
            txtCO_TotalPrice.setText("" + getResources().getString(R.string.Rs) + " " +
                    calculateTotalAmountWithoutCoupon(prefs.getInt(TagsPreferences.CART_SUB_TOTAL, 0)));
            DialogUtils.showDialog(PreConfirmOrder.this, "Your coupon has been applied");
            edtCO_CoupounCode.setText("");

        }

    }

    /*
    * Calculate total amount after applying coupon
    * */
    private int calculateTotalAmountWithCoupon(CouponJson couponJson) {
        /*
         * Calculate Discount
         * */
        if(couponJson.getData().getDiscount_type().equalsIgnoreCase("Amount")){//if discount type is simple amount

            if(Double.parseDouble(couponJson.getData().getDiscount_value())<couponJson.getData().getMax_discount_amount())
                preEditor.putInt(TagsPreferences.COUPON_DISCOUNT,
                        prefs.getInt(TagsPreferences.COUPON_DISCOUNT,0)+//previous value of discount
                                (int) Double.parseDouble(couponJson.getData().getDiscount_value())).commit();//+new discount
            else
                preEditor.putInt(TagsPreferences.COUPON_DISCOUNT, couponJson.getData().getMax_discount_amount()).commit();
        }else{//if discount type is in percent format
            int subtotal=prefs.getInt(TagsPreferences.CART_SUB_TOTAL, 0);
            int discountPercent= (int) Double.parseDouble(couponJson.getData().getDiscount_value());
            int discount=(subtotal*discountPercent)/100;
            if(discount<couponJson.getData().getMax_discount_amount())
                preEditor.putInt(TagsPreferences.COUPON_DISCOUNT,
                        (prefs.getInt(TagsPreferences.COUPON_DISCOUNT,0)+discount)).commit();
            else
                preEditor.putInt(TagsPreferences.COUPON_DISCOUNT,
                        (prefs.getInt(TagsPreferences.COUPON_DISCOUNT,0)+couponJson.getData().getMax_discount_amount())).commit();

        }

//        int total = prefs.getInt(TagsPreferences.TOTAL, 0) - prefs.getInt(TagsPreferences.COUPON_DISCOUNT, 0);
//        preEditor.putInt(TagsPreferences.TOTAL,total).commit();
        int newsubtotal=prefs.getInt(TagsPreferences.CART_SUB_TOTAL, 0)-prefs.getInt(TagsPreferences.COUPON_DISCOUNT,0);
        int total=calculateTotalAmountWithoutCoupon(newsubtotal);
        return total;
//                 /*
//                * Calculate Vat
//                * */
//        int vat=((prefs.getInt(TagsPreferences.CART_SUB_TOTAL,0)-prefs.getInt(TagsPreferences.COUPON_DISCOUNT,0))*125)/1000;
//        preEditor.putInt(TagsPreferences.VAT,vat).commit();
//
//                /*
//                * Calculating surcharge
//                * */
//        int surcharge=(vat*5)/100;
//        preEditor.putInt(TagsPreferences.SURCHARGE,vat).commit();
//
//                /*
//                * calculating total amount
//                * */
//        int total= (surcharge+vat)-Integer.parseInt(prefs.getString(TagsPreferences.REDEEMED_AMOUNT,"0"));
//
//        preEditor.putInt(TagsPreferences.TOTAL,total).commit();
//
//        txtCO_TotalPrice.setText("" + getResources().getString(R.string.Rs) + " " + prefs.getInt(TagsPreferences.TOTAL, 0));
    }


    /*
    *
    * Overridden method
    *
    * */
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
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
        startActivity(new Intent(PreConfirmOrder.this,Cart.class));
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCO_CoupounCode:
                if(isCouponApplied)
                    DialogUtils.showDialog(context,"One coupon has already been applied for this order");
                else{
                    //disable apply coupon button
                    btnCO_CoupounCode.setEnabled(false);
                    //fetch menu item ids and their quantities
                    fetchMenuItemIdsAndQttys(mainCartItem);
                    validateCoupouns();
                }

                break;
            case R.id.btnConfirm:
                btnConfirm.setEnabled(false);
                getKitechId();
                break;
            case R.id.btnCO_BringChange_100:
                changeFor= 100;
                selectedChangeFor(v);
                break;
            case R.id.btnCO_BringChange_500:
                changeFor= 500;
                selectedChangeFor(v);
                break;
            case R.id.btnCO_BringChange_1000:
                changeFor= 1000;
                selectedChangeFor(v);
                break;
            case R.id.btnCO_ReedeamPoint:
                if(isPointRedeemed)
                    DialogUtils.showDialog(context,"You have already redeemed your points for this order");
                else{
                    dummyRedeem();
//                    getRedeemPoint();
                }

                break;
        }
    }

    private void dummyRedeem() {
        int totalRedeempoint = Integer.parseInt(prefs.getString(TagsPreferences.PROFILE_USER_REDEEM_POINT, "0"));
        if(totalRedeempoint==0){
            DialogUtils.showDialog(PreConfirmOrder.this,"You Don't have any FE credit to Redeem");
        }else{// redeem point if having redeem points

            //pointWhichCanBeRedeem=total*redeampointvalue
            int pointWhichCanBeRedeem=prefs.getInt(TagsPreferences.TOTAL,0)*prefs.getInt(TagsPreferences.REDEEM_POINT_VALUE,1);
            int redeemPoint=(pointWhichCanBeRedeem<totalRedeempoint)?pointWhichCanBeRedeem:totalRedeempoint;

            DialogUtils.showDialog(context, "FE points redeem successful" +
                    "\nYou have redeemed " + redeemPoint +" points"+
                    "\nYour have " + (totalRedeempoint-redeemPoint)+" points remaining ");

            int redeemDiscount = redeemPoint / prefs.getInt(TagsPreferences.REDEEM_POINT_VALUE, 1);

            preEditor.putInt(TagsPreferences.COUPON_DISCOUNT,
                    prefs.getInt(TagsPreferences.COUPON_DISCOUNT,0)+//previous value of discount
                            redeemDiscount).commit();//+new discount

            int newSub=prefs.getInt(TagsPreferences.CART_SUB_TOTAL,0)-redeemDiscount;

            int newTotal = calculateTotalAmountWithoutCoupon(newSub);
            txtCO_TotalPrice.setText("" + getResources().getString(R.string.Rs) +
                    String.valueOf(newTotal));
            txtCO_Redeem.setText("You have " + (totalRedeempoint - redeemPoint) + " to redeem");
            isPointRedeemed=true;
        }

    }

}
