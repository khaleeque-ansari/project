package com.firsteat.firsteat.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firsteat.firsteat.R;
import com.firsteat.firsteat.models.Menu;
import com.firsteat.firsteat.models.UsersAddresses;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by touchmagics on 12/4/2015.
 */
public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {
    private static final String TAG = AddressAdapter.class.getSimpleName();
    private final SharedPreferences prefs;


    public static HashMap<String,HashMap<String,Menu.DataEntity>> selected_item=new HashMap<>();
    private final Context context;
    public static ArrayList<UsersAddresses> usersAddress=new ArrayList<>();

    public AddressAdapter(Context ctx, ArrayList<UsersAddresses> addresses) {
        usersAddress = addresses;
        this.context=ctx;
        prefs = PreferenceManager
                .getDefaultSharedPreferences(context);

        for(int i=0;i<usersAddress.size();i++){
            UsersAddresses temp=usersAddress.get(i);
            Log.d(TAG, "Address adapter address at " + i + " " + temp.getAdd_0());
        }

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_item_address, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UsersAddresses item=usersAddress.get(position);

        holder.txtAddressLine0.setText(item.getAdd_1());
//        holder.txtAddressLine1.setText(item.getAdd_1());
        holder.txtAddressLine2.setText(item.getAdd_2());
    }

    @Override
    public int getItemCount() {

        return usersAddress.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtAddressLine0,txtAddressLine1,txtAddressLine2;
        public ViewHolder(View itemView) {
            super(itemView);
            txtAddressLine0= (TextView) itemView.findViewById(R.id.txtCO_ConfirmOrder);
//            txtAddressLine1= (TextView) itemView.findViewById(R.id.txtAddressLine1);
            txtAddressLine2= (TextView) itemView.findViewById(R.id.txtAddressLine2);

        }
    }


}
