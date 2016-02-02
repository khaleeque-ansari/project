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
import com.firsteat.firsteat.models.OrderSummaryItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by touchmagics on 12/4/2015.
 */
public class SummaryOrderItemAdapter extends RecyclerView.Adapter<SummaryOrderItemAdapter.ViewHolder> {
    private final SharedPreferences prefs;
    private final Context context;
    private List<OrderSummaryItem> summaryOrderItemsList=new ArrayList<>();

    public SummaryOrderItemAdapter(Context ctx, List<OrderSummaryItem> summaryOrderItems) {
        this.context=ctx;
        summaryOrderItemsList = summaryOrderItems;
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
        OrderSummaryItem orderSummaryItem = summaryOrderItemsList.get(position);


        holder.txtSummaryItemName.setText("Item name : "+orderSummaryItem.getItemName());
        holder.txtSummaryItemQty.setText("Quantity: "+orderSummaryItem.getQuantity());
        holder.txtSummaryItemPrice.setText(""+context.getResources().getString(R.string.Rs)+" "+orderSummaryItem.getPrice());
    }

    @Override
    public int getItemCount() {

        return summaryOrderItemsList.size();
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
