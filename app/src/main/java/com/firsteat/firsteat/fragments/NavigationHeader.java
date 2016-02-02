package com.firsteat.firsteat.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.firsteat.firsteat.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationHeader extends Fragment {

View rootview;
    private ImageView imageView;
    private TextView name;
    private TextView nameview;

    public NavigationHeader() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootview=inflater.inflate(R.layout.fragment_navigation_header, container, false);
        imageView= (ImageView) rootview.findViewById(R.id.imageView);
        name= (TextView) rootview.findViewById(R.id.name);

        return rootview;
    }

    public void updateHeader(String image,String name){
        rootview=getActivity().getLayoutInflater().inflate(R.layout.fragment_navigation_header, null);
        imageView= (ImageView) rootview.findViewById(R.id.imageView);
        nameview= (TextView) rootview.findViewById(R.id.name);
        nameview.setText(name);
        Picasso.with(getActivity())
                .load(image)
                .error(R.mipmap.ic_launcher)
                .into(this.imageView);
    }

}
