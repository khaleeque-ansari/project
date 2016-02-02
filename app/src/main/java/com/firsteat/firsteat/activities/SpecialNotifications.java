package com.firsteat.firsteat.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.firsteat.firsteat.R;
import com.firsteat.firsteat.adapters.SpecialNotificationAdapter;
import com.firsteat.firsteat.models.SpecialNotification;
import com.firsteat.firsteat.utils.DialogUtils;
import com.firsteat.firsteat.utils.TagsPreferences;

public class SpecialNotifications extends AppCompatActivity {

    private SharedPreferences prefs;
    RecyclerView rv_specialNotifiacation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_notifications);

        rv_specialNotifiacation= (RecyclerView) findViewById(R.id.rv_specialNotifiacation);
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbarSN);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Offers");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        prefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        /*
        * check for special notification
        * */
        checkForSpecialNotification();

    }

    private void checkForSpecialNotification() {
        String url=prefs.getString(TagsPreferences.JSON_SERVICE_SPECIAL_NOTIFICATION,"{}");
        Gson gson=new Gson();
        SpecialNotification specialNotification = gson.fromJson(url.toString(), SpecialNotification.class);
        if(specialNotification.getData()!=null){

            rv_specialNotifiacation.hasFixedSize();
            LinearLayoutManager lm=new LinearLayoutManager(SpecialNotifications.this);
            rv_specialNotifiacation.setLayoutManager(lm);
            SpecialNotificationAdapter adapter=new SpecialNotificationAdapter(SpecialNotifications.this,specialNotification);
            rv_specialNotifiacation.setAdapter(adapter);

        }else
        {
            DialogUtils.showDialog(SpecialNotifications.this,"You don't have any special notification to show");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
