package com.openclassrooms.netapp.Controllers.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.openclassrooms.netapp.Controllers.Fragments.DetailFragment;
import com.openclassrooms.netapp.R;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_USER_ID = "com.openclassrooms.netapp.Controllers.Activities.DetailActivity.EXTRA_USER_ID";
    private DetailFragment detailFragment;

    public String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Log.d("DetailActivity", "in onCreate(), before Fragment_config()");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.configureAndShowDetailFragment();
    }


    private void configureAndShowDetailFragment() {
        detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.activity_detail_frame_layout);
        if (detailFragment == null) {
            detailFragment = new DetailFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_detail_frame_layout, detailFragment)
                    .commit();
        }

        Log.d("DetailActivity", "in fragment_config(), before bundle_config()");
        username = getIntent().getExtras().getString(EXTRA_USER_ID);
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        detailFragment.setArguments(bundle);
    }

}

