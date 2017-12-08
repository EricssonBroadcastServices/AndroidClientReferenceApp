package com.emp.demo.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.emp.demo.R;
import com.emp.demo.adapters.EpisodesCarouselAdapter;
import com.emp.demo.app.AppController;

import net.ericsson.emovs.utilities.models.EmpSeries;

public class SeriesDetails extends AppCompatActivity {
    Toolbar mToolbar;
    private EpisodesCarouselAdapter episodesCarouselAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series_episodes);

        EmpSeries series = (EmpSeries) getIntent().getExtras().get("series");
        loadActionBar(series.localized.getTitle("en"));
        loadUi(series);
    }

    void loadUi(EmpSeries series) {
        this.episodesCarouselAdapter = new EpisodesCarouselAdapter(this, series);

        RecyclerView episodesCarousel = (RecyclerView) findViewById(R.id.carousel_series_items);
        episodesCarousel.setAdapter(this.episodesCarouselAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        episodesCarousel.setLayoutManager(layoutManager);

        if(series.episodes == null) {
            // TODO: get programs for a specific series && seasonID
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        AppController.loadActionBarMenu(this, menu);
        return true;
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