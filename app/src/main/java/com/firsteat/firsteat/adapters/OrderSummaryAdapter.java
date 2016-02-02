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
import com.firsteat.firsteat.models.OrderSummary;

/**
 * Created by touchmagics on 12/4/2015.
 */
public class OrderSummaryAdapter extends RecyclerView.Adapter<OrderSummaryAdapter.ViewHolder> {
    private final SharedPreferences prefs;
    private final Context context;
    private final OrderSummary orderSummary;

    public OrderSummaryAdapter(Context ctx, OrderSummary orderSummaryDetails) {
        this.context=ctx;
        orderSummary = orderSummaryDetails;
        prefs = PreferenceManager
                .getDefaultSharedPreferences(context);



    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_item_summary, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
////        SummaryOrderItems.DataEntity.DetailsEntity detailsEntity = detailsList.get(position);
//
//        holder.txtSummaryItemName.setText(detailsEntity.getMenu().getItem_name());
//        holder.txtSummaryItemQty.setText(detailsEntity.getQty());
//        holder.txtSummaryItemPrice.setText(detailsEntity.getAmount());
    }

    @Override
    public int getItemCount() {

        return orderSummary.getData().getMenu_details().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtSummaryItemName,txtSummaryItemQty,txtSummaryItemPrice;
        public ViewHolder(View itemView) {
            super(itemView);
            txtSummaryItemName= (TextView) itemView.findViewById(R.id.txtSummaryItemName);
            txtSummaryItemQty= (TextView) itemView.findViewById(R.id.txtSummaryItemQty);
            txtSummaryItemPrice= (TextView) itemView.findViewById(R.id.txtSummaryItemPrice);

        }
    }


}
