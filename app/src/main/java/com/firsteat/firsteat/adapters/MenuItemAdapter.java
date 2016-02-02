package com.firsteat.firsteat.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firsteat.firsteat.R;
import com.firsteat.firsteat.models.MenuItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by touchmagics on 11/27/2015.
 */
public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.ViewHolder> {


    List<MenuItem> mItems;
    public MenuItemAdapter() {
        super();
        mItems = new ArrayList<MenuItem>();
        MenuItem nature = new MenuItem();
        nature.setName("Refreshing poha");
        nature.setPrice(65.0);
        nature.setStock(5);
        nature.setThumbnail(R.drawable.img);
         mItems.add(nature);

        nature = new MenuItem();
        nature.setName("Omelette & Veggies");
        nature.setPrice(100.0);
        nature.setStock(6);
        nature.setThumbnail(R.drawable.img);
        mItems.add(nature);

        nature = new MenuItem();
        nature.setName("Gobhi Paratha");
        nature.setPrice(85.0);
        nature.setStock(4);
        nature.setThumbnail(R.drawable.img);
        mItems.add(nature);

        nature = new MenuItem();
        nature.setName("Healthy Upma");
        nature.setPrice(65.0);
        nature.setStock(6);
        nature.setThumbnail(R.drawable.img);
        mItems.add(nature);


        nature = new MenuItem();
        nature.setName("Omelette & Veggies");
        nature.setPrice(100.0);
        nature.setStock(6);
        nature.setThumbnail(R.drawable.img);
        mItems.add(nature);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.rv_item_menu, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        MenuItem nature = mItems.get(i);
        viewHolder.txtStock.setText("Remaining :"+nature.getStock());
        viewHolder.txtName.setText(nature.getName());
        viewHolder.txtPrice.setText("Rs "+nature.getPrice());
//        viewHolder.tvDesNature.setText(nature.getDes());
        viewHolder.imgThumbnail.setImageResource(nature.getThumbnail());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imgThumbnail;
        public TextView txtStock,txtName,txtPrice,txtCounter;
        public ImageButton btnSub,btnAdd;

        public ViewHolder(final View itemView) {
            super(itemView);

            imgThumbnail = (ImageView)itemView.findViewById(R.id.img_thumbnail);
            txtStock = (TextView)itemView.findViewById(R.id.tv_stock);
            txtName = (TextView)itemView.findViewById(R.id.txtName);
            txtPrice = (TextView)itemView.findViewById(R.id.txtPrice);
            txtCounter = (TextView)itemView.findViewById(R.id.txtCounter);
            btnSub= (ImageButton) itemView.findViewById(R.id.btnSub);
            btnAdd= (ImageButton) itemView.findViewById(R.id.btnAdd);

            btnSub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i=Integer.parseInt(txtCounter.getText().toString());
                    if(i!=0){
                        i=i-1;
                        txtCounter.setText(""+i);
                    }

                }
            });
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i=Integer.parseInt(txtCounter.getText().toString());
                    if(i!=6){
                        i=i+1;
                        txtCounter.setText(""+i);
                    }
                }
            });


        }
    }
}
