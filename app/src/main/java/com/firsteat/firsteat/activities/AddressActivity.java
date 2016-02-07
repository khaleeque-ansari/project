package com.firsteat.firsteat.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.firsteat.firsteat.R;
import com.firsteat.firsteat.adapters.AddressAdapter;
import com.firsteat.firsteat.app.AppController;
import com.firsteat.firsteat.models.UsersAddresses;
import com.firsteat.firsteat.utils.Constants;
import com.firsteat.firsteat.utils.DialogUtils;
import com.firsteat.firsteat.utils.RecyclerItemClickListener;
import com.firsteat.firsteat.utils.TagsPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddressActivity extends AppCompatActivity {

    private static final String TAG = AddressActivity.class.getSimpleName();
    private RecyclerView rv_address;
    private ArrayList<UsersAddresses> addresses;
    private AddressAdapter adapter;
    private SharedPreferences prefs;
    private SharedPreferences.Editor preEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        prefs = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        preEditor=prefs.edit();

        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbarAddress);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Select an address");

        Log.d(TAG, "address passed from cart" + Cart.addresses.size());
        addresses = Cart.addresses;


        Log.d(TAG, "address int Address Activity" + this.addresses.size());

        rv_address= (RecyclerView) findViewById(R.id.rv_address);
        rv_address.setHasFixedSize(true);
        LinearLayoutManager lm=new LinearLayoutManager(AddressActivity.this);
        rv_address.setLayoutManager(lm);

        for(int i=0;i<Cart.addresses.size();i++){
            UsersAddresses temp=Cart.addresses.get(i);
            Log.d(TAG,"addresActivity recieved address at "+i+" "+temp.getAdd_0());
        }
        for(int i=0;i<addresses.size();i++){
            UsersAddresses temp=addresses.get(i);
            Log.d(TAG,"addresActivity address at "+i+" "+temp.getAdd_0());

        }

        /*
        * Add click lister to items so that the selected item will appear on cart screen
        * */
        rv_address.addOnItemTouchListener(new RecyclerItemClickListener(AddressActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                finish();
                Log.d(TAG,"Address rv item clicked");
                Cart.updateAddress(addresses.get(position));
            }
        }));
        adapter=new AddressAdapter(AddressActivity.this,addresses);
        rv_address.setAdapter(adapter);

        if(addresses.size()==0){
            DialogUtils.showDialog(AddressActivity.this,"Please enter your doorstep address");
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.address, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_add_address:
               createExampleDialog();

                break;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);

                break;
        }


        return super.onOptionsItemSelected(item);
    }
    /**
     * Create and return an example alert dialog with an edit text box.
     */
    private void createExampleDialog() {


        /*
        *
        *
        * */
        final View view = AddressActivity.this.getLayoutInflater().inflate(R.layout.dialog_add_address, null);
        TextView btnDialogAdd2= (TextView) view.findViewById(R.id.btnDialogAdd2);
        RadioGroup rg= (RadioGroup) view.findViewById(R.id.radiogrp);

        btnDialogAdd2.setText(prefs.getString(TagsPreferences.ADDRESS_BASE_LINE, "").toString());
        rg.check(R.id.chkDialogAddhome);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add new Address");
        builder.setView(view);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
              //all logics will be covered in custom listener
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Button theButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        theButton.setOnClickListener(new CustomListener(alertDialog));
    }

    /*
    * Upload entered Address to server
    * */
    private void uploadAddToServer(String line0, String line1, String line2, String category, final UsersAddresses item2) {
        final ProgressDialog pd=new ProgressDialog(AddressActivity.this);
        pd.setMessage("Uploading your last address to server");
        pd.setCancelable(false);
        pd.show();
        String url= Constants.getUrlUploadAdd(prefs.getString(TagsPreferences.PROFILE_USER_ID,null),line0,line1,line2,category);

        JsonObjectRequest uploadAddtoserver=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "uploadAddtoserver response:" + response.toString());
                pd.dismiss();
                try {
                    item2.setAdress_id(response.getJSONObject("data").getInt("address_id"));
                    addresses.add(item2);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(AddressActivity.this, "Server error", Toast.LENGTH_LONG).show();
                Log.d(TAG, "uploadAddtoserver error:" + error.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(uploadAddtoserver);
    }

    /*
    * custom listener for feedback
    * */
    class CustomListener implements View.OnClickListener {
        private final Dialog dialog;
        public CustomListener(Dialog dialog) {
            this.dialog = dialog;
        }
        @Override
        public void onClick(View v) {
            UsersAddresses item2 = new UsersAddresses();
            EditText houseno = (EditText) dialog.findViewById(R.id.tilDialogAdd0);
//                EditText line1 = (EditText) view.findViewById(R.id.tilDialogAdd1);

            RadioButton rb;
            String category = null;
            Log.d(TAG, "address line 0" + houseno.getText().toString());

            item2.setAdd_0("address lin 0");//dummy value because it is not being used for now
            item2.setAdd_1(houseno.getText().toString());
            item2.setAdd_2(prefs.getString(TagsPreferences.ADDRESS_BASE_LINE, ""));
            int[] radio = {
                    R.id.chkDialogAddhome,
                    R.id.chkDialogAddOffice
            };
            for (int id : radio) {
                rb = (RadioButton) dialog.findViewById(id);
                if (rb.isChecked()) {
                    category = rb.getText().toString();

                }
            }
            item2.setCategory(category);

            if(houseno.getText().toString().trim().length()<4){
                houseno.setError("This field is mandetory, at least provide your house no.");

            }else{
                uploadAddToServer("address line 0", houseno.getText().toString(),
                        prefs.getString(TagsPreferences.ADDRESS_BASE_LINE, "").toString().toString(),
                        category,
                        item2);
                dialog.dismiss();
            }
        }
    }
}
