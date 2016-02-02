package com.firsteat.firsteat.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.squareup.picasso.Picasso;
import com.firsteat.firsteat.R;
import com.firsteat.firsteat.app.AppController;
import com.firsteat.firsteat.models.Menu;
import com.firsteat.firsteat.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by touchmagics on 12/4/2015.
 */
public class FavMenuAdapter extends RecyclerView.Adapter<FavMenuAdapter.ViewHolder> {
    private static final String TAG = FavMenuAdapter.class.getSimpleName();
    private final SharedPreferences prefs;
    List<Menu.DataEntity> menu;
    Context context;
    private  int selecteditem=0;
    private static int cost=0;
    static Menu.DataEntity menuItem;
    public static HashMap<String,HashMap<String,Menu.DataEntity>> selected_item=new HashMap<>();

    public FavMenuAdapter(Context ctx, Menu locationMenu) {
        this.menu = locationMenu.getData();
        this.context=ctx;
        prefs = PreferenceManager
                .getDefaultSharedPreferences(context);

        /*
        * clear selected item list
        * */
        selected_item.clear();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_item_fav_menu, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        menuItem = menu.get(position);

        if(menuItem.getMax_order_limit()==0)
            holder.txtStock.setVisibility(View.VISIBLE);
        holder.txtName.setText(menuItem.getName());
        holder.txtPrice.setText("" +context.getResources().getString(R.string.Rs)+" "+ menuItem.getPrice());
        holder.txtDetails.setText("" + menuItem.getDescription());
        holder.txtBestSuited.setText("" + menuItem.getBest_suited());

        Log.d(TAG, "menu check :" + menuItem.getPrice());
        Menu.DataEntity.FoodDetailsEntity food_details = menuItem.getFood_details();

        holder.txtCal.setText("" + food_details.getCalories());
        holder.txtFiber.setText("" + food_details.getFibre()+" g.");
        holder.txtCarb.setText("" + food_details.getCarbohydrates()+" g.");
        holder.txtProtein.setText("" + food_details.getProteins()+" g.");
        holder.txtFat.setText("" + food_details.getFats() + " g.");

        int categoryRsr= (menuItem.getCategory().toString().equalsIgnoreCase("0")) ? R.drawable.icn_veg_semisphare : (menuItem.getCategory().toString().equalsIgnoreCase("1")) ? R.drawable.icn_egg_up : R.drawable.icn_nonveg_semisphare;
        holder.imgRv_menu_category.setBackgroundResource(categoryRsr);

        Picasso.with(context)
                .load(menuItem.getImg_url())
                .error(R.drawable.image_not_found)
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.imgThumbnail);


        int itemViewType = holder.getItemViewType();
        Log.d(TAG,"item view typle "+itemViewType);
    }

    @Override
    public int getItemCount() {
        return menu.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgThumbnail;
        public TextView txtStock,txtName,txtPrice,txtDetails,txtBestSuited;
        public ImageView imgRv_menu_category;
        public TextView txtCal,txtFiber,txtCarb,txtProtein,txtFat;
        FrameLayout frameImgBtns,frameClickable;
        LinearLayout frameImgDetails;

        public ViewHolder(View itemView) {
            super(itemView);

            imgRv_menu_category= (ImageView) itemView.findViewById(R.id.fm_imgRv_menu_category);
            imgThumbnail = (ImageView)itemView.findViewById(R.id.fm_img_thumbnail);
            txtStock = (TextView)itemView.findViewById(R.id.fm_tv_stock);
            txtName = (TextView)itemView.findViewById(R.id.fm_txtName);
            txtPrice = (TextView)itemView.findViewById(R.id.fm_txtPrice);
            txtDetails = (TextView)itemView.findViewById(R.id.fm_txtDetails);
            txtBestSuited = (TextView)itemView.findViewById(R.id.fm_txtBestSuited);
            txtCal= (TextView) itemView.findViewById(R.id.fm_txtCal);
            txtFiber= (TextView) itemView.findViewById(R.id.fm_txtFiber);
            txtCarb= (TextView) itemView.findViewById(R.id.fm_txtCarb);
            txtProtein= (TextView) itemView.findViewById(R.id.fm_txtProtein);
            txtFat= (TextView) itemView.findViewById(R.id.fm_txtFat);
            frameImgBtns = (FrameLayout) itemView.findViewById(R.id.fm_frameImgBtns);
            frameImgDetails = (LinearLayout) itemView.findViewById(R.id.fm_frameImgDetails);
            frameClickable = (FrameLayout) itemView.findViewById(R.id.fm_frameClickable);


            frameClickable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (frameImgBtns.getVisibility() == View.VISIBLE) {
                        frameImgBtns.setVisibility(View.GONE);
                        frameImgDetails.setVisibility(View.VISIBLE);
                    } else {
                        frameImgBtns.setVisibility(View.VISIBLE);
                        frameImgDetails.setVisibility(View.GONE);
                    }
                }
            });
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


}
