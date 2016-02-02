package com.firsteat.firsteat.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.firsteat.firsteat.R;
import com.firsteat.firsteat.activities.Cart;
import com.firsteat.firsteat.activities.PreConfirmOrder;
import com.firsteat.firsteat.fragments.Alacarte;
import com.firsteat.firsteat.models.Menu;
import com.firsteat.firsteat.utils.DbHelper;
import com.firsteat.firsteat.utils.DialogUtils;
import com.firsteat.firsteat.utils.TagsPreferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by touchmagics on 12/4/2015.
 */
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private static final String TAG = CartAdapter.class.getSimpleName();
    private final SharedPreferences prefs;
    private final SharedPreferences.Editor preEditor;
    public static ArrayList<HashMap<String, Menu.DataEntity>> cartItems=new ArrayList<>();
    private final DbHelper helper;
    Context context;
    public  static int totalitem=0,countMainItem=0,counttotalOffers=0;
    public  static int totalprice=0,totalMainItemPrice=0,totalOfferPrice=0;
    public static ArrayList<Menu.DataEntity.FoodDetailsEntity.OffersEntity> redemedOffers=new ArrayList<>();
    private  String orderId;
    final ProgressDialog dialogOrderItems;
    private Menu alacarteMenu;
    private static int offerIndexCounter=0;

    public CartAdapter(Context ctx, HashMap<String, HashMap<String, Menu.DataEntity>> cart) {
        Log.d(TAG,"Constructor ");
        Log.d(TAG, "item in cart passed from previous  " + cart.keySet().size());

        this.context=ctx;
        helper=new DbHelper(context);
        dialogOrderItems =new ProgressDialog(context);

        totalprice=0;
        totalMainItemPrice=0;
        totalOfferPrice=0;

        offerIndexCounter=0;
        redemedOffers.clear();

        prefs = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
        preEditor=prefs.edit();

        cartItems.clear();
        totalitem=0;
        totalprice=0;

        for(String key : cart.keySet()){
            Log.d(TAG,"inside cart loop ");
            HashMap<String, Menu.DataEntity> cart_item = cart.get(key);
            cartItems.add(cart_item);

        }
        cartItems.trimToSize();
        Log.d(TAG,"cart item size: "+cartItems.size());

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_item_cart, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int count = 0;
        Menu.DataEntity item=null;
        holder.RlAddedItem1.setVisibility(View.GONE);
        holder.RlAddedItem2.setVisibility(View.GONE);

        HashMap<String, Menu.DataEntity> cartItem = cartItems.get(position);
        Log.d(TAG,"cart item size inside onBindViewHolder "+cartItem.keySet().size());
        for(String key:cartItem.keySet()){
            count=Integer.parseInt(key);
            item = cartItem.get(key);

            totalprice=totalprice+(count*item.getPrice());
            Log.d(TAG,"total item before addition"+totalitem);
            totalitem=totalitem+count;
            Log.d(TAG,"total item after addition"+totalitem);
            Log.d(TAG,"snackbar calling from bindViewHolder");
            showSnackBar(totalitem,totalprice);
        }


//        List<Menu.DataEntity.FoodDetailsEntity.OffersEntity> offers = item.getFood_details().getOffers();
//        Log.d(TAG,"offers size "+offers.size());

//        if(offers.size()>0){
//            Menu.DataEntity.FoodDetailsEntity.OffersEntity offeritem = offers.get(0);
//            holder.txtRVCartAdded1.setText(offeritem.getOffer_name());
//            holder.txtRVCartAdded1Price.setText(offeritem.getPrice());
//        }

        holder.txtRVCartName.setText(item.getName());
        holder.txtRVCartPrice.setText("Rs " + item.getPrice());
        holder.txtRVCartCounter.setText("" + count);
        Log.d(TAG,"food type "+item.getCategory().toString());
        int categoryRsr= (item.getCategory().toString().equalsIgnoreCase("0")) ? R.drawable.icn_veg_sphare : (item.getCategory().toString().equalsIgnoreCase("1")) ? R.drawable.icn_egg_sphare: R.drawable.icn_nonveg_sphare;
        holder.imgRVCartFoodType.setBackgroundResource(categoryRsr);


//        Gson gson=new Gson();
//        alacarteMenu =new Menu();
//        Log.d(TAG, "downloaded menu :" + prefs.getString(TagsPreferences.JSON_SERVICE_DOWNLOAD_MENU, "{}"));
//        alacarteMenu =gson.fromJson(prefs.getString(TagsPreferences.JSON_SERVICE_DOWNLOAD_MENU,null), Menu.class);
        List<Menu.DataEntity.FoodDetailsEntity.OffersEntity> offers= item.getFood_details().getOffers();

//        //fetching offer of the day (logic if want to show offer of the day for specific item selected)
//        for(Menu.DataEntity.FoodDetailsEntity.OffersEntity OfferOTD:offers){
//            if(OfferOTD.getOffer_of_the_day()==0){
//                RelativeLayout specialOfferView= (RelativeLayout) Cart.snackbarframe.findViewById(R.id.specialofferView);
//                specialOfferView.setVisibility(View.VISIBLE);
//                CheckBox chkOfferOfTheDay= (CheckBox) specialOfferView.findViewById(R.id.chkOfferOfTheDay);
//                TextView txtOfferOfTheDay= (TextView) specialOfferView.findViewById(R.id.txtPriceOfferOfTheDay);
//
//                chkOfferOfTheDay.setText(OfferOTD.getOffer_name());
//                txtOfferOfTheDay.setText(""+context.getResources().getString(R.string.Rs)+" "+OfferOTD.getPrice());
//            }
//        }

        //offer of the day (logic for all menuitems)
        final Menu.OfferOfTheDayEntity offer_of_the_day = Alacarte.offer_of_the_day;
        if (offer_of_the_day !=null){
            RelativeLayout specialOfferView= (RelativeLayout) Cart.snackbarframe.findViewById(R.id.specialofferView);
            specialOfferView.setVisibility(View.VISIBLE);
            CheckBox chkOfferOfTheDay= (CheckBox) specialOfferView.findViewById(R.id.chkOfferOfTheDay);
            TextView txtOfferOfTheDay= (TextView) specialOfferView.findViewById(R.id.txtPriceOfferOfTheDay);

            chkOfferOfTheDay.setText(offer_of_the_day.getOffer_name());
            txtOfferOfTheDay.setText("" + context.getResources().getString(R.string.Rs) + " " + offer_of_the_day.getPrice());

            chkOfferOfTheDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Menu.DataEntity.FoodDetailsEntity.OffersEntity offersEntity= new Menu.DataEntity.FoodDetailsEntity.OffersEntity();
                    //set values to offerentity from offeroftheday entity
                    offersEntity.setStart_date(offer_of_the_day.getStart_date());
                    offersEntity.setQty(offer_of_the_day.getQty());
                    offersEntity.setDescription(offer_of_the_day.getDescription());
                    offersEntity.setStatus(offer_of_the_day.getStatus());
                    offersEntity.setMenu_id(offer_of_the_day.getMenu_id());
                    offersEntity.setId(offer_of_the_day.getId());
                    offersEntity.setOffer_name(offer_of_the_day.getOffer_name());
                    offersEntity.setPrice(offer_of_the_day.getPrice());


                    if (isChecked) {
                        redemedOffers.add(offersEntity);
                        totalprice = totalprice + offersEntity.getPrice();//add price of offer 2
                        totalitem=totalitem+1;
                        Log.d(TAG,"snackbar calling from redeemed checked cart item1 if");
                        showSnackBar(totalitem, totalprice);
                        offerIndexCounter=++offerIndexCounter;
                    } else {
                        redemedOffers.remove(offerIndexCounter-1);
                        totalitem=totalitem-1;
                        totalprice = totalprice - offersEntity.getPrice();// sub price of offer 2
                        Log.d(TAG,"snackbar calling from redeemed checked cart item1 else");
                        showSnackBar(totalitem, totalprice);
                        offerIndexCounter=--offerIndexCounter;
                    }
                }
            });
        }


        if(offers.size()>0){
            holder.RlAddedItem1.setVisibility(View.VISIBLE);
            holder.txtRVCartAdded1.setText(offers.get(0).getOffer_name().toString());
            holder.txtRVCartAdded1Price.setText("" +context.getResources().getString(R.string.Rs)+" "+String.valueOf(offers.get(0).getPrice()));
        if(offers.size()>1){
            holder.RlAddedItem2.setVisibility(View.VISIBLE);
            holder.txtRVCartAdded2.setText(offers.get(1).getOffer_name().toString());
            holder.txtRVCartAdded2Price.setText("" +context.getResources().getString(R.string.Rs)+" "+String.valueOf(offers.get(1).getPrice()));
        }
        }

        Picasso.with(context)
                .load(item.getImg_url())
                .error(R.drawable.image_not_found)
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.imgRVCartThumb);
    }

    @Override
    public int getItemCount() {
        Log.d(TAG,"cart item size in getItemCount "+cartItems.size());
        return cartItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CircularImageView imgRVCartThumb,imgRVCartFoodType;
        public ImageButton imgRVCartMinus,imgRVCartPlus;
        public TextView txtRVCartName,txtRVCartPrice,txtRVCartAdded1,txtRVCartAdded1Price,txtRVCartAdded2,txtRVCartAdded2Price,txtRVCartCounter;
        public RelativeLayout RlAddedItem1,RlAddedItem2;
        public CheckBox chkCartItem1,chkCartItem2;
        public ViewHolder(View itemView) {
            super(itemView);

            chkCartItem1= (CheckBox) itemView.findViewById(R.id.chkCartItem1);
            chkCartItem2= (CheckBox) itemView.findViewById(R.id.chkCartItem2);

            imgRVCartMinus= (ImageButton) itemView.findViewById(R.id.imgRVCartMinus);
            imgRVCartPlus= (ImageButton) itemView.findViewById(R.id.imgRVCartPlus);
            RlAddedItem1= (RelativeLayout) itemView.findViewById(R.id.RlAddedItem1);
            RlAddedItem2= (RelativeLayout) itemView.findViewById(R.id.RlAddedItem2);
            imgRVCartThumb = (CircularImageView)itemView.findViewById(R.id.imgRVCartThumb);
            imgRVCartFoodType = (CircularImageView)itemView.findViewById(R.id.imgRVCartFoodType);
            txtRVCartName = (TextView)itemView.findViewById(R.id.txtCO_ConfirmOrder);
            txtRVCartName = (TextView)itemView.findViewById(R.id.txtCO_ConfirmOrder);
            txtRVCartPrice = (TextView)itemView.findViewById(R.id.txtAddressLine1);
            txtRVCartAdded1 = (TextView)itemView.findViewById(R.id.txtRVCartAdded1);
            txtRVCartAdded1Price = (TextView)itemView.findViewById(R.id.txtRVCartAdded1Price);
            txtRVCartAdded2 = (TextView)itemView.findViewById(R.id.txtRVCartAdded2);
            txtRVCartAdded2Price = (TextView)itemView.findViewById(R.id.txtRVCartAdded2Price);
            txtRVCartCounter = (TextView)itemView.findViewById(R.id.txtRVCartCounter);

            chkCartItem1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Menu.DataEntity item = null;

                    HashMap<String, Menu.DataEntity> cartItem = cartItems.get(getLayoutPosition());
                    for (String key : cartItem.keySet()) {
                        item = cartItem.get(key);
                    }
                    //fetch offers for the particular item from SQLite
                    List<Menu.DataEntity.FoodDetailsEntity.OffersEntity> offers = fetchOffers(item.getId());

                    Menu.DataEntity.FoodDetailsEntity.OffersEntity offer1 = offers.get(0);


                    if (isChecked) {
                        redemedOffers.add(offer1);
                        totalprice = totalprice + offer1.getPrice();//add price of offer 2
                        totalitem=totalitem+1;
                        Log.d(TAG, "snackbar calling from redeemed offers add if");
                        showSnackBar(totalitem,totalprice);
                        offerIndexCounter=++offerIndexCounter;

                        totalOfferPrice=offer1.getPrice()+totalOfferPrice;
                        counttotalOffers=(counttotalOffers+1);
                    } else {
                        redemedOffers.remove(offerIndexCounter-1);
                        Log.d(TAG, "reaching logic");
                        totalitem=totalitem-1;
                        totalprice = totalprice - offer1.getPrice();// sub price of offer 2
                        Log.d(TAG,"snackbar calling from redeemed offers add else");
                        showSnackBar(totalitem, totalprice);
                        offerIndexCounter=--offerIndexCounter;

                        totalOfferPrice=totalOfferPrice-offer1.getPrice();
                        counttotalOffers=(counttotalOffers-1);
                    }

                }
            });
            chkCartItem2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Menu.DataEntity item = null;

                    HashMap<String, Menu.DataEntity> cartItem = cartItems.get(getLayoutPosition());
                    for (String key : cartItem.keySet()) {
                        item = cartItem.get(key);
                    }
                    //fetch offers for the particular item from SQLite
                    List<Menu.DataEntity.FoodDetailsEntity.OffersEntity> offers = fetchOffers(item.getId());
                    Menu.DataEntity.FoodDetailsEntity.OffersEntity offer2 = offers.get(1);


                    if (isChecked) {
                        redemedOffers.add(offer2);
                        totalprice = totalprice + offer2.getPrice();//add price of offer 2
                        totalitem=totalitem+1;
                        Log.d(TAG, "snackbar calling from redeemed checked cart item1 if");
                        showSnackBar(totalitem, totalprice);

                        int newTotal = totalOfferPrice + (offer2.getPrice());
                        totalOfferPrice= newTotal;
                        counttotalOffers=++counttotalOffers;

                        offerIndexCounter=++offerIndexCounter;
                    } else {
                        Log.d(TAG, "reaching logic");
                        redemedOffers.remove(offerIndexCounter - 1);
                        totalitem=(totalitem-1);
                        totalprice = totalprice - offer2.getPrice();// sub price of offer 2
                        Log.d(TAG, "snackbar calling from redeemed checked cart item1 else");
                        showSnackBar(totalitem, totalprice);

                        int newTotal = totalOfferPrice - (offer2.getPrice());
                        totalOfferPrice= newTotal;
                        counttotalOffers=--counttotalOffers;

                        offerIndexCounter=--offerIndexCounter;
                    }

                }
            });

            imgRVCartMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = Integer.parseInt(txtRVCartCounter.getText().toString());
                    if (i != 0) {
                        i = i - 1;

                        Menu.DataEntity item = null;
                        HashMap<String, Menu.DataEntity> cartItem = cartItems.get(getLayoutPosition());
                        for (String key : cartItem.keySet()) {
                            item = cartItem.get(key);

                            totalprice = totalprice - (item.getPrice());

                            int newTotal = totalMainItemPrice - item.getPrice();
                            totalMainItemPrice= newTotal;
                            countMainItem=--countMainItem;

                            totalitem = --totalitem;
                            Log.d(TAG,"snackbar calling from minus button");
                            showSnackBar(totalitem, totalprice);
                            txtRVCartCounter.setText("" + i);
                        }

                    }
                }
            });
            imgRVCartPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = Integer.parseInt(txtRVCartCounter.getText().toString());
                    HashMap<String, Menu.DataEntity> cartIte = cartItems.get(getLayoutPosition());
                    Menu.DataEntity entity=new Menu.DataEntity();
                    for(String key:cartIte.keySet()){
                        entity = cartIte.get(key);
                    }

                    if (i !=entity.getMax_order_limit() ) {
                        i = i + 1;

                        Menu.DataEntity item = null;
                        HashMap<String, Menu.DataEntity> cartItem = cartItems.get(getLayoutPosition());
                        for (String key : cartItem.keySet()) {
                            item = cartItem.get(key);
                            Log.d(TAG,"imageCartplus");
                            totalprice = totalprice + (item.getPrice());

                            int newTotal = totalMainItemPrice + item.getPrice();
                            totalMainItemPrice= newTotal;
                            countMainItem=++countMainItem;


                            totalitem = ++totalitem;
                            Log.d(TAG,"total price "+totalprice+"total item "+totalitem);
                            Log.d(TAG,"snackbar calling from minus button");
                            showSnackBar(totalitem, totalprice);
                            Log.d(TAG, "total price1 " + totalprice + "total iteml " + totalitem);
                            txtRVCartCounter.setText("" + i);
                        }
                    }
                }
            });
        }
    }

    private List<Menu.DataEntity.FoodDetailsEntity.OffersEntity> fetchOffers(int id) {
        List<Menu.DataEntity.FoodDetailsEntity.OffersEntity> offers=new ArrayList<>();
        Gson gson=new Gson();
        Menu menu= gson.fromJson(prefs.getString(TagsPreferences.JSON_SERVICE_DOWNLOAD_MENU, null), Menu.class);

        for(Menu.DataEntity entity: menu.getData()){
            if(entity.getId()==id){
               offers = entity.getFood_details().getOffers();
                Log.d(TAG,"check fetch offer method "+offers.get(0).getOffer_name());
            }
        }
        return offers;
    }

    private void showSnackBar(final int selecteditem, final int cost) {

        final Activity activity = (Activity) context;
        FrameLayout snackbarframe = Cart.snackbarframe;
        snackbarframe.setVisibility(View.VISIBLE);
        TextView txtSnackbar = (TextView) snackbarframe.findViewById(R.id.txtSnackBar);
        Button btnSnackbar= (Button) snackbarframe.findViewById(R.id.btnSnackbar);
        btnSnackbar.setText("CONFIRM");
//        RelativeLayout specialOfferView= (RelativeLayout) Alacarte.snackbarframe.findViewById(R.id.specialofferView);
//        specialOfferView.setVisibility(View.GONE);
        txtSnackbar.setText("Total amount "+context.getResources().getString(R.string.Rs)+cost);
        btnSnackbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(prefs.getBoolean(TagsPreferences.ADDRESS_EXIST,false)&& Cart.isTimeSlotAvailable==true&&selecteditem>0){

                    Log.d(TAG,"selected item ="+selecteditem);
                    Log.d(TAG,"address exits="+prefs.getBoolean(TagsPreferences.ADDRESS_EXIST,false));
                    Log.d(TAG,"is time slot available"+Cart.isTimeSlotAvailable);
                    preEditor.putInt(TagsPreferences.CART_SUB_TOTAL,totalprice).commit();
                    context.startActivity(new Intent(activity, PreConfirmOrder.class));
                }else{
                    if(Cart.isTimeSlotAvailable&&selecteditem>0) {//time slot availabale that means Delivery address not selected
                        Log.d(TAG,"address exits="+prefs.getBoolean(TagsPreferences.ADDRESS_EXIST,false));
                        Log.d(TAG,"is time slot available"+Cart.isTimeSlotAvailable);
                        DialogUtils.showDialog(context, "Select atleast one drop point");
                    }
                    else if(selecteditem<1){
                        Log.d(TAG,"selected item ="+selecteditem);
                        DialogUtils.showDialog(context, "Select atleast one item");
                    }else{
                        Log.d(TAG,"selected item ="+selecteditem);
                        DialogUtils.showDialog(context, "Order only possible for the next time slot");
                    }

                }


            }
        });

//
//        Log.d(TAG,"showSnackBar selected item: "+selecteditem+" total cost "+cost);
//        final Activity activity = (Activity) context;
//
//        Snackbar snackbar = Snackbar
//                .make(activity.findViewById(R.id.clCart), "Total amount "+context.getResources().getString(R.string.Rs)+cost, Snackbar.LENGTH_LONG)
//                .setAction("CONFIRM", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
////                        Dialog exampleDialog = createChangeForDialog();
////                        exampleDialog.show();
////                        if(!prefs.getString(TagsPreferences.ADDRESS_ID,"0").equalsIgnoreCase(String.valueOf(0))){
////
////
////
////                        }else{
////                            Toast.makeText(context, "Choose an address first", Toast.LENGTH_SHORT).show();
////                        }
////                        addNewOrder();
//                        showSnackBar(selecteditem,cost);
//                     if(prefs.getBoolean(TagsPreferences.ADDRESS_EXIST,false)&& Cart.isTimeSlotAvailable){
//
//                         Log.d(TAG,"address exits="+prefs.getBoolean(TagsPreferences.ADDRESS_EXIST,false));
//                         Log.d(TAG,"is time slot available"+Cart.isTimeSlotAvailable);
//                         preEditor.putInt(TagsPreferences.CART_SUB_TOTAL,totalprice).commit();
//                         Activity activity= (Activity) context;
//                         context.startActivity(new Intent(activity, PreConfirmOrder.class));
//                     }else{
//                         if(Cart.isTimeSlotAvailable) {//time slot availabale that means Delivery address not selected
//                             Log.d(TAG,"address exits="+prefs.getBoolean(TagsPreferences.ADDRESS_EXIST,false));
//                             Log.d(TAG,"is time slot available"+Cart.isTimeSlotAvailable);
//                             DialogUtils.showDialog(context, "Select atleast one drop point");
//                         }
//                         else{
//                             Log.d(TAG,"address exits="+prefs.getBoolean(TagsPreferences.ADDRESS_EXIST,false));
//                             Log.d(TAG,"is time slot available"+Cart.isTimeSlotAvailable);
//                             DialogUtils.showDialog(context, "Order only possible for the next time slot");
//                         }
//
//                     }
//
//
//                    }
//                }).setDuration(Snackbar.LENGTH_INDEFINITE);
//
//        // Changing message text color
//        snackbar.setActionTextColor(activity.getResources().getColor(R.color.colorPrimary));
//
//        // Changing action button text color
//        View sbView = snackbar.getView();
//        sbView.setBackground(new ColorDrawable(activity.getResources().getColor(R.color.colorPrimary)));
//        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
//        TextView actionText= (TextView) sbView.findViewById(android.support.design.R.id.snackbar_action);
//        LinearLayout.LayoutParams params = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
//            params = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//        }
//        params.setMargins(20,10,20,10);
//        actionText.setLayoutParams(params);
////        actionText.setBackground(new ColorDrawable(getResources().getColor(R.color.colorAccent)));
//        actionText.setBackground(activity.getResources().getDrawable(R.drawable.ractangle_filled_white));
//
//        snackbar.show();

    }

    private Dialog createOTPDialog() {
        Activity activity= (Activity) context;
        final View view = activity.getLayoutInflater().inflate(R.layout.dialog_otp, null);
        EditText editText= (EditText) view.findViewById(R.id.edtOTPMobile);
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add new Address");
        builder.setView(view);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });

        return builder.create();
    }
    /**
     * Create and return an example alert dialog with an edit text box.
     */
    private Dialog createChangeForDialog() {
        Activity activity= (Activity) context;
        final View view = activity.getLayoutInflater().inflate(R.layout.dialog_add_new_order, null);
        RadioGroup rg= (RadioGroup) view.findViewById(R.id.radiogrp);
        rg.check(R.id.chkDialogNewOrderYes);
        view.findViewById(R.id.btnDialogNewOrderOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean error = false;
                EditText tilDialogNewOrderChange = (EditText) view.findViewById(R.id.tilDialogNewOrderChange);
                RadioButton rb;
                int[] radio = {
                        R.id.chkDialogNewOrderYes,
                        R.id.chkDialogNewOrderNo
                };
                for (int id : radio) {
                    rb = (RadioButton) view.findViewById(id);
                    if (rb.isChecked() && rb.getText().toString().equalsIgnoreCase("yes")) {
                        if (tilDialogNewOrderChange.getText().toString().trim().length() < 2)
                            tilDialogNewOrderChange.setError("Enter a valid amount");
                        error = true;

                    }
                }
                if (!error) {
                    return;
                }
            }
        });
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(view);

        return builder.create();
    }






}
