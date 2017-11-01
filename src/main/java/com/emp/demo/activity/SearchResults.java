package com.emp.demo.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import com.ebs.android.exposure.metadata.EMPMetadataProvider;
import com.emp.demo.R;
import com.emp.demo.adapters.SearchResultsAdapter;
import com.emp.demo.callbacks.AutocompleteCallback;

public class SearchResults extends AppCompatActivity {
    Toolbar mToolbar;
    private SearchResultsAdapter searchResultsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        loadActionBar("Search Results");
        loadUi();
    }

    void loadUi() {
        this.searchResultsAdapter = new SearchResultsAdapter(this, null);

        ListView assetList = (ListView) findViewById(R.id.search_results_list);
        assetList.setAdapter(this.searchResultsAdapter);

        Intent intent = getIntent();

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            EMPMetadataProvider.getInstance().autocomplete(query, new AutocompleteCallback(this.searchResultsAdapter));
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