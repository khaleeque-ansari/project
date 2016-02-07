package com.firsteat.firsteat.fragments;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.firsteat.firsteat.R;
import com.firsteat.firsteat.app.AppController;
import com.firsteat.firsteat.utils.Constants;
import com.firsteat.firsteat.utils.TagsPreferences;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class Feedback extends Fragment {

View rootview;
    public static final String TAG = Feedback.class.getSimpleName();
    private RatingBar ratingBar;
    private TextView txtRatingValue;
    private Button btn;
    private SharedPreferences prefs;
    private EditText edtDFeedback;

    public Feedback() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        prefs = PreferenceManager
                .getDefaultSharedPreferences(getActivity());

        // Inflate the layout for this fragment
        rootview=inflater.inflate(R.layout.fragment_feedback, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Rate us");

        ratingBar = (RatingBar) rootview.findViewById(R.id.ratingBar);
        txtRatingValue = (TextView) rootview.findViewById(R.id.txtDFRating);

        edtDFeedback= (EditText) rootview.findViewById(R.id.edtDFeedback)
        ;
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                txtRatingValue.setText(String.valueOf(rating));

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFeedBack(prefs.getString(TagsPreferences.PROFILE_USER_ID, "0"),edtDFeedback.getText().toString(),String.valueOf(ratingBar.getRating()));
            }
        });
        return rootview;
    }

    private void sendFeedBack(String userId, String msg, String rating) {
        String url= Constants.getUrlFeedback(userId,msg,rating);
        final ProgressDialog pd=new ProgressDialog(getActivity());
        pd.setMessage("sending your feedback .......");
        pd.setCancelable(false);
        pd.show();
        JsonObjectRequest requestFeedback=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pd.dismiss();
                Log.d(TAG, "requestFeedback response" + response.toString());
                Toast.makeText(getActivity(),"your feedback has been sent successfully",Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"Server error",Toast.LENGTH_LONG).show();
                pd.dismiss();
                Log.d(TAG,"requestFeedback error"+error.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(requestFeedback);
    }


}
