package com.firsteat.firsteat.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.firsteat.firsteat.R;
import com.firsteat.firsteat.activities.Cart;
import com.firsteat.firsteat.activities.LoginActivity;
import com.firsteat.firsteat.activities.MainActivity;
import com.firsteat.firsteat.app.AppController;
import com.firsteat.firsteat.models.Menu;
import com.firsteat.firsteat.utils.Constants;
import com.firsteat.firsteat.utils.DialogUtils;
import com.firsteat.firsteat.utils.TagsPreferences;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by touchmagics on 12/4/2015.
 */
public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    private static final String TAG = MenuAdapter.class.getSimpleName();
    private final SharedPreferences prefs;
    List<Menu.DataEntity> menu;
    Context context;
    public static  int selecteditem=0;
    private static int cost=0;
    static Menu.DataEntity menuItem;
    public static HashMap<String,HashMap<String,Menu.DataEntity>> selected_item=new HashMap<>();

    public MenuAdapter(Context ctx,Menu locationMenu) {
        this.menu = locationMenu.getData();
        this.context=ctx;
        prefs = PreferenceManager
                .getDefaultSharedPreferences(context);

        /*
        * clear selected item list
        * */
        selected_item.clear();
        selecteditem=0;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_item_menu, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        menuItem = menu.get(position);

        holder.txtName.setText(menuItem.getName());
        holder.txtPrice.setText("" +context.getResources().getString(R.string.Rs)+" "+ menuItem.getPrice());
        holder.txtDetails.setText("" + menuItem.getDescription());
        holder.txtBestSuited.setText("" + menuItem.getBest_suited());

//        if(menuItem.getFood_details().getStock().getPresent_orders()>=menuItem.getFood_details().getStock().getMax_order_limit()||
//                !(menuItem.getIn_stock().equalsIgnoreCase("yes"))){
//            holder.txtStock.setVisibility(View.VISIBLE);
//        }
        Log.d(TAG, "menu check :" + menuItem.getPrice());
        Menu.DataEntity.FoodDetailsEntity food_details = menuItem.getFood_details();

        if (selected_item.containsKey(String.valueOf(position))) {
            HashMap<String, Menu.DataEntity> entity = selected_item.get(String.valueOf(position));
            holder.txtCounter.setText(entity.keySet().iterator().next());
        } else {
            holder.txtCounter.setText("0");
        }

        holder.txtCal.setText("" + food_details.getCalories());
        holder.txtFiber.setText("" + food_details.getFibre()+" g.");
        holder.txtCarb.setText("" + food_details.getCarbohydrates() + " g.");
        holder.txtProtein.setText("" + food_details.getProteins()+" g.");
        holder.txtFat.setText("" + food_details.getFats() + " g.");

        int categoryRsr= (menuItem.getCategory().toString().equalsIgnoreCase("0")) ? R.drawable.icn_veg_semisphare : (menuItem.getCategory().toString().equalsIgnoreCase("1")) ? R.drawable.icn_egg_up : R.drawable.icn_nonveg_semisphare;
        holder.imgRv_menu_category.setBackgroundResource(categoryRsr);

        Picasso.with(context)
                .load(menuItem.getImg_url())
                .error(R.drawable.image_not_available)
                .placeholder(R.drawable.loading_image)
                .into(holder.imgThumbnail);

        showSnackBar(selecteditem);

        int itemViewType = holder.getItemViewType();
        Log.d(TAG,"item view typle "+itemViewType);


        holder.btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = Integer.parseInt(holder.txtCounter.getText().toString());
                if (i != 0) {//if i < minimum order i.e= 0
                    i = i - 1;
                    selecteditem = selecteditem - 1;
                    showSnackBar(selecteditem);
                    holder.txtCounter.setText("" + i);
                    //Remove item
                    if (selected_item.containsKey(String.valueOf(position))) {
                        selected_item.remove(String.valueOf(position));

                    }

                }

            }
        });
        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int diffrenceOfStock = menu.get(position).getFood_details().getStock().getMax_order_limit() -
                        menu.get(position).getFood_details().getStock().getPresent_orders();
                int limit = (diffrenceOfStock < menu.get(position).getMax_order_limit()) ? diffrenceOfStock : menu.get(position).getMax_order_limit();
                int i = Integer.parseInt(holder.txtCounter.getText().toString());
                if (diffrenceOfStock != 0) {
                    if (i < limit) {
                        i = i + 1;

                        selecteditem = selecteditem + 1;
                        showSnackBar(selecteditem);
                        holder.txtCounter.setText("" + i);
                        HashMap<String, Menu.DataEntity> itemwithCount = new HashMap();
                        itemwithCount.put(String.valueOf(i), menu.get(position));


                        List<Menu.DataEntity.FoodDetailsEntity.OffersEntity> offers = menu.get(position).getFood_details().getOffers();
                        if (offers != null) {
                            Log.d(TAG, "offers size " + offers.size());
                        }

                        if (selected_item.containsKey(String.valueOf(position))) {
                            selected_item.remove(String.valueOf(position));
                            selected_item.put(String.valueOf(position), itemwithCount);
                        } else {
                            selected_item.put(String.valueOf(position), itemwithCount);
                        }
                    } else {
                        DialogUtils.showDialog(context, "You have reached maximum order limit!");
                    }
                } else {
                    DialogUtils.showDialog(context, "Oops! This has been sold out. Please try other equally good options, see you soon!");
                }

            }
        });
        holder.btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prefs.getBoolean(TagsPreferences.LOGIN_STATUS, false)) {
                    holder.btnFav.setBackground(context.getResources().getDrawable(R.drawable.fav_filled));
                    holder.btnFav.setEnabled(false);
                    int menuId = menu.get(position).getId();
                    uploadFavItem(prefs.getString(TagsPreferences.PROFILE_USER_ID, null), menuId, v);

                } else {
                    Activity activity = (Activity) context;
                    Intent tomain = new Intent(activity, LoginActivity.class);
                    tomain.putExtra(TagsPreferences.NEXT, MainActivity.class.getCanonicalName());
                    context.startActivity(tomain);
                    activity.finish();
                }


            }
        });
        holder.frameClickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.frameImgBtns.getVisibility() == View.VISIBLE) {
                    holder.frameImgBtns.setVisibility(View.GONE);
                    holder.frameImgDetails.setVisibility(View.VISIBLE);
                } else {
                    holder.frameImgBtns.setVisibility(View.VISIBLE);
                    holder.frameImgDetails.setVisibility(View.GONE);
                }
                notifyDataSetChanged();
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return menu.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgThumbnail;
        public TextView txtStock,txtName,txtPrice,txtCounter,txtDetails,txtBestSuited;
        public ImageButton btnSub,btnAdd,btnFav;
        public ImageView imgRv_menu_category;
        public TextView txtCal,txtFiber,txtCarb,txtProtein,txtFat;
        FrameLayout frameImgBtns,frameClickable;
        LinearLayout frameImgDetails;

        public ViewHolder(View itemView) {
            super(itemView);

            imgRv_menu_category= (ImageView) itemView.findViewById(R.id.imgRv_menu_category);
            imgThumbnail = (ImageView)itemView.findViewById(R.id.img_thumbnail);
            txtStock = (TextView)itemView.findViewById(R.id.tv_stock);
            txtName = (TextView)itemView.findViewById(R.id.txtName);
            txtPrice = (TextView)itemView.findViewById(R.id.txtPrice);
            txtDetails = (TextView)itemView.findViewById(R.id.txtDetails);
            txtBestSuited = (TextView)itemView.findViewById(R.id.txtBestSuited);
            txtCounter = (TextView)itemView.findViewById(R.id.txtCounter);
            btnSub= (ImageButton) itemView.findViewById(R.id.btnSub);
            btnAdd= (ImageButton) itemView.findViewById(R.id.btnAdd);
            btnFav= (ImageButton) itemView.findViewById(R.id.btnFav);
            txtCal= (TextView) itemView.findViewById(R.id.txtCal);
            txtFiber= (TextView) itemView.findViewById(R.id.txtFiber);
            txtCarb= (TextView) itemView.findViewById(R.id.txtCarb);
            txtProtein= (TextView) itemView.findViewById(R.id.txtProtein);
            txtFat= (TextView) itemView.findViewById(R.id.txtFat);
            frameImgBtns = (FrameLayout) itemView.findViewById(R.id.frameImgBtns);
            frameImgDetails = (LinearLayout) itemView.findViewById(R.id.frameImgDetails);
            frameClickable = (FrameLayout) itemView.findViewById(R.id.frameClickable);

//            btnSub.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int i = Integer.parseInt(txtCounter.getText().toString());
//                    if (i != 0) {//if i < minimum order i.e= 0
//                        i = i - 1;
//                        selecteditem = selecteditem - 1;
//                        showSnackBar(selecteditem);
//                        txtCounter.setText("" + i);
//                        //Remove item
//                        if (selected_item.containsKey(String.valueOf(getLayoutPosition()))) {
//                            selected_item.remove(String.valueOf(getLayoutPosition()));
//
//                        }
//                        notifyDataSetChanged();
//                        notifyItemChanged(getLayoutPosition());
//
//                    }
//
//                }
//            });
//            btnAdd.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int diffrenceOfStock = menu.get(getLayoutPosition()).getFood_details().getStock().getMax_order_limit() -
//                            menu.get(getLayoutPosition()).getFood_details().getStock().getPresent_orders();
//                    int limit=(diffrenceOfStock<menu.get(getLayoutPosition()).getMax_order_limit())?diffrenceOfStock:menu.get(getLayoutPosition()).getMax_order_limit();
//                    int i = Integer.parseInt(txtCounter.getText().toString());
//                    if(diffrenceOfStock!=0){
//                        if (i < limit) {
//                            i = i + 1;
//
//                            selecteditem = selecteditem + 1;
//                            showSnackBar(selecteditem);
//                            txtCounter.setText("" + i);
//                            HashMap<String, Menu.DataEntity> itemwithCount = new HashMap();
//                            itemwithCount.put(String.valueOf(i), menu.get(getLayoutPosition()));
//
//
//                            List<Menu.DataEntity.FoodDetailsEntity.OffersEntity> offers = menu.get(getLayoutPosition()).getFood_details().getOffers();
//                            if (offers != null) {
//                                Log.d(TAG, "offers size " + offers.size());
//                            }
//
//                            if (selected_item.containsKey(String.valueOf(getLayoutPosition()))) {
//                                selected_item.remove(String.valueOf(getLayoutPosition()));
//                                selected_item.put(String.valueOf(getLayoutPosition()), itemwithCount);
//                            } else {
//                                selected_item.put(String.valueOf(getLayoutPosition()), itemwithCount);
//                            }
//
//                            notifyDataSetChanged();
//                            notifyItemChanged(getLayoutPosition());
//
//                        } else {
//                            DialogUtils.showDialog(context,"You have reached maximum order limit!");
//                        }
//                    }else{
//                        DialogUtils.showDialog(context,"Oops! This has been sold out. Please try other equally good options, see you soon!");
//                    }
//
//                }
//            });
//            btnFav.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (prefs.getBoolean(TagsPreferences.LOGIN_STATUS, false)) {
//                        btnFav.setBackground(context.getResources().getDrawable(R.drawable.fav_filled));
//                        btnFav.setEnabled(false);
//                        int menuId = menu.get(getLayoutPosition()).getId();
//                        uploadFavItem(prefs.getString(TagsPreferences.PROFILE_USER_ID, null), menuId, v);
//
//                    } else {
//                        Activity activity = (Activity) context;
//                        Intent tomain = new Intent(activity, LoginActivity.class);
//                        tomain.putExtra(TagsPreferences.NEXT, MainActivity.class.getCanonicalName());
//                        context.startActivity(tomain);
//                        activity.finish();
//                    }
//
//
//                }
//            });
//            frameClickable.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (frameImgBtns.getVisibility() == View.VISIBLE) {
//                        frameImgBtns.setVisibility(View.GONE);
//                        frameImgDetails.setVisibility(View.VISIBLE);
//                    } else {
//                        frameImgBtns.setVisibility(View.VISIBLE);
//                        frameImgDetails.setVisibility(View.GONE);
//                    }
//                    notifyDataSetChanged();
//                    notifyItemChanged(getLayoutPosition());
//                }
//            });
        }
    }

    private void uploadFavItem(String userId, int menuId, final View v) {
        String url= Constants.getUrlUsersFavMenuUpload(userId,menuId);
        JsonObjectRequest uploadfavRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG,""+response.getJSONObject("data").getString("msg"));
                    Toast.makeText(context, "item added to your favurite list", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "error in uploading your fav", Toast.LENGTH_SHORT).show();
                ImageButton fav= (ImageButton) v;
                v.setBackground(context.getResources().getDrawable(R.drawable.icn_fav));
                v.setEnabled(true);
            }
        });
        AppController.getInstance().addToRequestQueue(uploadfavRequest);
    }

    private void showSnackBar(final int selecteditem) {

        final Activity activity = (Activity) context;
        FrameLayout snackbarframe = MainActivity.snackbarframeMain;
        snackbarframe.setVisibility(View.VISIBLE);
        TextView txtSnackbar = (TextView) snackbarframe.findViewById(R.id.txtSnackBarMain);
        Button btnSnackbar= (Button) snackbarframe.findViewById(R.id.btnSnackbarMain);

        txtSnackbar.setText(""+selecteditem+" item(s) selected");
        btnSnackbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prefs.getBoolean(TagsPreferences.FLAG_MSG_OUT_OF_SERVICE_AREA, true) == false)//location is in service area
                {
                    if (selecteditem != 0) {
                        Log.d(TAG, "Login status at menu adapter :" + prefs.getBoolean(TagsPreferences.LOGIN_STATUS, false));
                        if (prefs.getBoolean(TagsPreferences.LOGIN_STATUS, false)) {
                            Log.d(TAG, "login status true");
                            Intent toCart = new Intent(new Intent(activity, Cart.class));
                            toCart.putExtra(TagsPreferences.NEXT, Cart.class.getCanonicalName());
                            context.startActivity(toCart);
                        } else {
                            Log.d(TAG, "login status false");
                            Intent toCart = new Intent(new Intent(activity, LoginActivity.class));
                            toCart.putExtra(TagsPreferences.NEXT, Cart.class.getCanonicalName());
                            Log.d(TAG, Cart.class.getCanonicalName());
                            context.startActivity(toCart);
                        }
                    } else {
                        Toast.makeText(context, "Choose at least one item", Toast.LENGTH_SHORT).show();
                    }
                } else {//location is out of service area
                    DialogUtils.showDialog(context, "Select a drop point.");
                }
            }
        });


        /*
        * barrier
        *
        * */
//        final Activity activity = (Activity) context;
//
//        final Snackbar snackbar = Snackbar
//                .make(Alacarte.customLinearLayout, ""+selecteditem+" items selected ", Snackbar.LENGTH_LONG)
//                .setAction("ADD TO CART", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        showSnackBar(selecteditem);
//                        if (prefs.getBoolean(TagsPreferences.FLAG_MSG_OUT_OF_SERVICE_AREA, true)==false)//location is in service area
//                        {
//                            if(selecteditem!=0){
//                                Log.d(TAG, "Login status at menu adapter :" + prefs.getBoolean(TagsPreferences.LOGIN_STATUS, false));
//                                if(prefs.getBoolean(TagsPreferences.LOGIN_STATUS,false)) {
//                                        Log.d(TAG, "login status true");
//                                        Intent toCart = new Intent(new Intent(activity, Cart.class));
//                                        toCart.putExtra(TagsPreferences.NEXT, Cart.class.getCanonicalName());
//                                        context.startActivity(toCart);
//                                }
//                                else{
//                                    Log.d(TAG, "login status false");
//                                    Intent toCart=new Intent(new Intent(activity, LoginActivity.class));
//                                    toCart.putExtra(TagsPreferences.NEXT,Cart.class.getCanonicalName());
//                                    Log.d(TAG,Cart.class.getCanonicalName());
//                                    context.startActivity(toCart);
//                                }
//                            }else{
//                                Toast.makeText(context, "Choose at least one item", Toast.LENGTH_SHORT).show();
//                            }
//                        }else{//location is out of service area
//                            DialogUtils.showDialog(context, "Select an area from the drop down above");
//                        }
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
//        actionText.setTextSize(TypedValue.COMPLEX_UNIT_SP,15f);
////        actionText.setBackground(new ColorDrawable(getResources().getColor(R.color.colorAccent)));
//        actionText.setBackground(activity.getResources().getDrawable(R.drawable.ractangle_filled_white));
//
//        snackbar.show();

    }
}
