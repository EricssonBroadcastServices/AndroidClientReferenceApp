package com.emp.demo.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.emp.demo.R;
import com.emp.demo.app.Constants;

public class Settings extends AppCompatActivity {
    Toolbar mToolbar;

    private final String PLAYBACK_SHARED_PREFERENCES = "PLAYBACK_SHARED_PREFERENCES";
    private final String SECURITY_LEVEL_KEY = "securityLevel";
    private final String SECURITY_LEVEL_L3 = "L3";
    private SharedPreferences mPlaybackPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPlaybackPreferences =
                getApplicationContext().getSharedPreferences(PLAYBACK_SHARED_PREFERENCES,
                                                             Context.MODE_PRIVATE);

        setContentView(R.layout.activity_settings);

        loadActionBar("Settings");

        loadUi();
    }

    void loadUi() {
        CheckBox testViewCheckbox = findViewById(R.id.test_view_checkbox);
        testViewCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Constants.TEST_MODE = "ALL";
                } else {
                    Constants.TEST_MODE = null;
                }
            }
        });
        if (Constants.TEST_MODE != null) {
            testViewCheckbox.setChecked(true);
        }
        else {
            testViewCheckbox.setChecked(false);
        }

        CheckBox securityLevelCheckbox = findViewById(R.id.cb_enable_l3);

        securityLevelCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    mPlaybackPreferences.edit().putString(SECURITY_LEVEL_KEY, SECURITY_LEVEL_L3).apply();
                } else {
                    mPlaybackPreferences.edit().remove(SECURITY_LEVEL_KEY).apply();
                }
            }
        });

        if (mPlaybackPreferences.contains(SECURITY_LEVEL_KEY)) {
            String securityLevel = mPlaybackPreferences.getString(SECURITY_LEVEL_KEY, "");

            if (securityLevel.equals(SECURITY_LEVEL_L3)) {
                securityLevelCheckbox.setChecked(true);
            } else {
                securityLevelCheckbox.setChecked(false);
            }
        } else {
            securityLevelCheckbox.setChecked(false);
        }
    }

    void loadActionBar(String title) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        return true;
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 3) {
            moveTaskToBack(false);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}