package com.firsteat.firsteat.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firsteat.firsteat.R;
import com.firsteat.firsteat.models.SpecialNotification;

import java.util.List;

/**
 * Created by touchmagics on 12/4/2015.
 */
public class SpecialNotificationAdapter extends RecyclerView.Adapter<SpecialNotificationAdapter.ViewHolder> {


    private final SharedPreferences prefs;
    private final Context context;
    private SpecialNotification sNotifications;

    public SpecialNotificationAdapter(Context ctx, SpecialNotification specialNotification) {
        sNotifications = specialNotification;
        context=ctx;
        prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_item_special_notification, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        List<SpecialNotification.DataEntity> data = sNotifications.getData();
        SpecialNotification.DataEntity dataEntity = data.get(position);

        holder.txtSN.setText(dataEntity.getContent_text());


    }


    @Override
    public int getItemCount() {
        return sNotifications.getData().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtSN;

        public ViewHolder(View itemView) {
            super(itemView);

            txtSN = (TextView)itemView.findViewById(R.id.txtSN);

        }
    }


}
