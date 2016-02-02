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
import android.widget.TextView;

import com.firsteat.firsteat.R;
import com.firsteat.firsteat.activities.Summary;
import com.firsteat.firsteat.models.Menu;
import com.firsteat.firsteat.models.MyOrders;
import com.firsteat.firsteat.utils.TagsPreferences;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by touchmagics on 12/4/2015.
 */
public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {
    private static final String TAG = MyOrderAdapter.class.getSimpleName();
    private final SharedPreferences prefs;
    List<Menu.DataEntity> menu;
    Context context;
    private  int selecteditem=0;
    private static int cost=0;
    private List<MyOrders.DataEntity> orderList=new ArrayList<>();
    public static String orderid;

    public MyOrderAdapter(Context ctx, MyOrders orders) {
        this.orderList = orders.getData();
        this.context=ctx;
        prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_item_order_history, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        MyOrders.DataEntity order = orderList.get(position);

        String formatedDate=getFormatedTime(order.getOrdered_at())[0];
        String formatedTime = getFormatedTime(order.getOrdered_at())[1];
        Log.d(TAG, "formated date: " + formatedDate + "\n formated time :" + formatedTime);

//                String today = formatter.format(new Date());
        holder.txtOrderHistoryID.setText(order.getId());
//        holder.txtOrderHistoryAddress.setText(order.getOrderAddresses().get(0).);
        holder.txtOrderHistoryNameNAdd.setText(""+prefs.getString(TagsPreferences.PROFILE_USER_NAME,"name")+
        ", "+prefs.getString(TagsPreferences.ADDRESS_BASE_LINE,""));
        holder.txtOrderHistoryStatus.setText(order.getOrderStatuses().get(order.getOrderStatuses().size()-1).getOrder_status());
        holder.txtOrderHistoryDate.setText(""+formatedDate);
        holder.txtOrderHistoryTotal.setText(""+context.getResources().getString(R.string.Rs)+order.getTotal_amount());
        holder.txtOrderHistoryTTime.setText(""+formatedTime);


    }

    /*
    * convert date in specific format
    * */
    private String getFormatedDate(String ordered_at) {
        String dtStart = ordered_at;
        Date date;
        String datetime="";
        SimpleDateFormat format = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
        try {
            date = format.parse(dtStart);
            datetime = format.format(date);
            System.out.println(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return datetime;
    }
    /*
    * convert date in specific format
    * */
    private String[] getFormatedTime(String ordered_at) {
        String[] datetime = new String[0];
        try {
            String dtStart = ordered_at;
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
            Date date = null;
            date = format.parse(dtStart);

            DateFormat dateFormat = new SimpleDateFormat("EEEE dd,MMM");
            DateFormat timeFormat = new SimpleDateFormat("hh:mm a");
            String dateOfOrder = dateFormat.format(date);
            String timeOfOrder = timeFormat.format(date);

            datetime = new String[]{dateOfOrder, timeOfOrder};


        } catch (ParseException e) {
            e.printStackTrace();
        }


        return datetime;
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtOrderHistoryID,txtOrderHistoryNameNAdd,txtOrderHistoryStatus,txtOrderHistoryDate,txtOrderHistoryTotal,txtOrderHistoryTTime;

        public ViewHolder(View itemView) {
            super(itemView);
            txtOrderHistoryID = (TextView)itemView.findViewById(R.id.txtOrderHistoryID);
            txtOrderHistoryNameNAdd = (TextView)itemView.findViewById(R.id.txtOrderHistoryNameNAdd);
            txtOrderHistoryStatus = (TextView)itemView.findViewById(R.id.txtSummaryItemQty);
            txtOrderHistoryDate = (TextView)itemView.findViewById(R.id.txtOrderHistoryDate);
            txtOrderHistoryTotal = (TextView)itemView.findViewById(R.id.txtOrderHistoryTotal);
            txtOrderHistoryTTime = (TextView)itemView.findViewById(R.id.txtOrderHistoryTTime);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    MyOrderAdapter.this.orderid=orderList.get(getLayoutPosition()).getId();

                    Activity activity= (Activity) context;
                    Intent toSummary= new Intent(activity, Summary.class);
                    toSummary.putExtra("isfromhistory",true);
                    context.startActivity(toSummary);
                    Log.d(TAG,"item clicked at my order list");
                }
            });
        }
    }


}
