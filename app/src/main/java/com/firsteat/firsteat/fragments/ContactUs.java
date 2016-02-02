package com.firsteat.firsteat.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firsteat.firsteat.R;
import com.firsteat.firsteat.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactUs extends Fragment implements View.OnClickListener {

    View rootview;
    TextView txtContactUsFooter;

    public ContactUs() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_contact_us, container, false);
        txtContactUsFooter = (TextView) rootview.findViewById(R.id.txtContactUsFooter);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Contact Us");

        txtContactUsFooter.setText("Copyright " +
                getResources().getString(R.string.copyright) +
                " 2016. F.E. Food Tech Pvt Limited");

        int[] shareBtns = {
                R.id.imgContactUsEmail,
                R.id.imgContactUsFacebook,
                R.id.imgContactUsTwitter,
                R.id.imgContactUsInstagram,
                R.id.imgContactUsPhone
        };

        for (int id : shareBtns) {
            ImageButton btn = (ImageButton) rootview.findViewById(id);
            btn.setOnClickListener(this);
        }

        return rootview;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgContactUsEmail:
                sendEmail();
                break;
            case R.id.imgContactUsFacebook:
                Intent facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.FE_FACEBOOK));
                startActivity(facebookIntent);
                break;
            case R.id.imgContactUsTwitter:
                Intent twitterIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.FE_TWITTER));
                startActivity(twitterIntent);
                break;
            case R.id.imgContactUsInstagram:
                Intent instaIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.FE_INSTA));
                startActivity(instaIntent);
                break;
            case R.id.imgContactUsPhone:
                Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                phoneIntent.setData(Uri.parse("tel:" + Constants.FE_PHONE));
                startActivity(phoneIntent);
                break;
        }
    }

    private void sendEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("message/rfc822");//for opening only mailing clients
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{Constants.FE_EMAIL});

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
