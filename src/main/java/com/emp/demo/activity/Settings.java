package com.emp.demo.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.emp.demo.R;
import com.emp.demo.adapters.DownloadListAdapter;
import com.emp.demo.app.AppController;
import com.emp.demo.app.Constants;

import net.ericsson.emovs.download.DownloadItem;
import net.ericsson.emovs.download.EMPDownloadProvider;
import net.ericsson.emovs.download.interfaces.IDownload;

import java.util.ArrayList;

public class Settings extends AppCompatActivity {
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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