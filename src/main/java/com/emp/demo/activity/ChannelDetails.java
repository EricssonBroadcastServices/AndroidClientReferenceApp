package com.emp.demo.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ebs.android.exposure.metadata.EMPMetadataProvider;
import com.ebs.android.exposure.models.EmpChannel;
import com.emp.demo.R;
import com.emp.demo.adapters.EpgCarouselAdapter;
import com.emp.demo.callbacks.EpgCallback;

public class ChannelDetails extends AppCompatActivity {
    Toolbar mToolbar;
    private EpgCarouselAdapter epgCarouselAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_information);

        EmpChannel channel = (EmpChannel) getIntent().getExtras().get("channel");
        loadActionBar(channel.localized.getTitle("en"));
        loadUi(channel);
    }

    void loadUi(EmpChannel channel) {
        this.epgCarouselAdapter = new EpgCarouselAdapter(this, channel);

        RecyclerView epgCarousel = (RecyclerView) findViewById(R.id.carousel_epg_items);
        epgCarousel.setAdapter(this.epgCarouselAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        epgCarousel.setLayoutManager(layoutManager);

        if(channel.programs == null) {
            EMPMetadataProvider.getInstance().getEpg(channel.channelId, new EpgCallback(epgCarousel));
        }
    }

    void loadActionBar(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle(title);
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

}