package com.firsteat.firsteat.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firsteat.firsteat.R;
import com.firsteat.firsteat.activities.MainActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutUs extends Fragment {

    public static final String TAG = AboutUs.class.getSimpleName();
    View rootview;
    public AboutUs() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootview=inflater.inflate(R.layout.fragment_about_us, container, false);
        MainActivity.hideToolbarLayout();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("About Us");

        TextView txtAboutUs= (TextView) rootview.findViewById(R.id.txtAboutUs);
        TextView txtAboutUsHeader= (TextView) rootview.findViewById(R.id.txtAboutUsHeader);

        txtAboutUsHeader.setText("Good things come to those who wait? false!\n Good things come to those who dont't skip their breakfast\n-Every Indian Mom");

        String txt=getTextFromFile();
        txtAboutUs.setText(txt);
        // Inflate the layout for this fragment
        return rootview;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private String getTextFromFile() {
        String txt=null;
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(getActivity().getAssets().open("aboutus.txt")));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                total.append(line);
            }
            txt=total.toString();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return txt;
    }




}
