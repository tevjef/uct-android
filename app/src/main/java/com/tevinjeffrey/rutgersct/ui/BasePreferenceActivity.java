package com.tevinjeffrey.rutgersct.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tevinjeffrey.rutgersct.R;

import butterknife.ButterKnife;

public class BasePreferenceActivity extends ActionBarActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_preference);
        setToolbar();
        getFragmentManager().beginTransaction().replace(R.id.content_frame, new SettingsFragment()).commit();
    }

    private void setToolbar() {
        Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitleTextAppearance(this, R.style.toolbar_title);
        toolbar.setSubtitleTextAppearance(this, R.style.toolbar_subtitle);
    }
}
