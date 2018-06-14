package com.emp.demo.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import net.ericsson.emovs.exposure.metadata.EMPMetadataProvider;
import net.ericsson.emovs.playback.PlaybackProperties;
import net.ericsson.emovs.playback.ui.views.FloatingPlayerView;
import net.ericsson.emovs.utilities.interfaces.IPlayable;

import com.emp.demo.R;
import com.emp.demo.adapters.GenericAssetCarouselAdapter;
import com.emp.demo.app.AppController;
import com.emp.demo.callbacks.AssetListCallback;

public class GenericAssetCarousel extends AppCompatActivity {
    Toolbar mToolbar;
    private GenericAssetCarouselAdapter adapter;

    private FrameLayout mFloatingPlayerView;
    private FloatingPlayerView mPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_generic_asset_carousel);

        Bundle args = getIntent().getExtras();
        String endpoint = args.getString("endpoint");
        String title = args.getString("title");
        loadActionBar(title);
        loadUi(endpoint);
        loadFloatingPlayerView();
    }

    public void showPlayer(final Context ctx, IPlayable playable, PlaybackProperties properties) {
        mFloatingPlayerView.setVisibility(View.VISIBLE);

        mPlayerView.play(playable);
    }

    public void hidePlayer() {
        mPlayerView.stop();

        mFloatingPlayerView.setVisibility(View.GONE);
    }

    void loadUi(String endpoint) {
        this.adapter = new GenericAssetCarouselAdapter(this);

        RecyclerView episodesCarousel = (RecyclerView) findViewById(R.id.carousel_items);
        episodesCarousel.setAdapter(this.adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        episodesCarousel.setLayoutManager(layoutManager);

        EMPMetadataProvider.getInstance().getAssets(endpoint, new AssetListCallback(this.adapter));
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

    private void loadFloatingPlayerView() {
        mFloatingPlayerView = findViewById(R.id.v_floating_player_view);

        mPlayerView = mFloatingPlayerView.findViewById(R.id.dragLayout);

        mFloatingPlayerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidePlayer();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        AppController.loadActionBarMenu(this, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 3) {
            moveTaskToBack(false);
        } else {
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