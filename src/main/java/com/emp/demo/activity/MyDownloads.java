package com.emp.demo.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import com.emp.demo.R;
import com.emp.demo.adapters.DownloadListAdapter;

import net.ericsson.emovs.download.DownloadItem;
import net.ericsson.emovs.download.EMPDownloadProvider;
import net.ericsson.emovs.download.interfaces.IDownload;

import java.util.ArrayList;

public class MyDownloads extends AppCompatActivity {
    Toolbar mToolbar;
    private DownloadListAdapter downloadListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_downloads);
        loadActionBar("My Downloads");
        loadUi();
    }

    void loadUi() {
        ArrayList<IDownload> downloadItems = new ArrayList<>();

        ArrayList<IDownload> queued = EMPDownloadProvider.getInstance().getDownloads(DownloadItem.State.QUEUED);
        ArrayList<IDownload> paused = EMPDownloadProvider.getInstance().getDownloads(DownloadItem.State.PAUSED);
        ArrayList<IDownload> downloading = EMPDownloadProvider.getInstance().getDownloads(DownloadItem.State.DOWNLOADING);
        ArrayList<IDownload> failed = EMPDownloadProvider.getInstance().getDownloads(DownloadItem.State.FAILED);
        ArrayList<IDownload> completed = EMPDownloadProvider.getInstance().getDownloads(DownloadItem.State.COMPLETED);

        downloadItems.addAll(completed);
        downloadItems.addAll(downloading);
        downloadItems.addAll(paused);
        downloadItems.addAll(queued);
        downloadItems.addAll(failed);

        this.downloadListAdapter = new DownloadListAdapter(this, downloadItems);

        ListView downloadList = (ListView) findViewById(R.id.download_list);
        downloadList.setAdapter(this.downloadListAdapter);
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
        this.downloadListAdapter.removeListeners();
        super.onDestroy();
    }

}