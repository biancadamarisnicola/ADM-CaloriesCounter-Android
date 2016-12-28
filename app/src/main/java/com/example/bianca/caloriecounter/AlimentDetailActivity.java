package com.example.bianca.caloriecounter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

/**
 * Created by bianca on 30.11.2016.
 */
public class AlimentDetailActivity extends AppCompatActivity {

    private static final String TAG = AlimentDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "on create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_aliment_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);


        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putString(AlimentDetailFragment.ALIMENT_NAME,
                    getIntent().getStringExtra(AlimentDetailFragment.ALIMENT_NAME));
            AlimentDetailFragment fragment = new AlimentDetailFragment();
            fragment.setArguments(arguments);
            Log.d(TAG,"__________________________________");
            Log.d(TAG, fragment.getArguments().toString());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.aliment_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, SearchAlimentActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
