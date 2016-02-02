package com.firsteat.firsteat.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.firsteat.firsteat.R;
import com.firsteat.firsteat.utils.ChangeFragment;

public class BaseFragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_fragment);

        Bundle extras = getIntent().getExtras();

        try {
            Class clazz= Class.forName(extras.getString("frag"));
            Fragment frag= (Fragment) clazz.newInstance();
            ChangeFragment.changeFragment(getSupportFragmentManager(), R.id.frame_baseFragmentActivity, frag);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        setupActionBar(extras.getString("frag"));
    }

    private void setupActionBar(String frag) {
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar_fragments);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(frag.substring(frag.lastIndexOf(".")+1));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
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
