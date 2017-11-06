package com.emp.demo.callbacks;

import android.util.Log;

import com.ebs.android.exposure.clients.exposure.ExposureError;
import com.ebs.android.exposure.metadata.IMetadataCallback;
import com.emp.demo.adapters.SeriesAdapter;
import com.ebs.android.exposure.models.EmpSeries;

import java.util.ArrayList;

/**
 * Created by Joao Coelho on 2017-07-21.
 */
public class SeriesCallback implements IMetadataCallback<ArrayList<EmpSeries>> {
    private final SeriesAdapter seriesAdapter;

    public SeriesCallback(SeriesAdapter seriesAdapter) {
        this.seriesAdapter = seriesAdapter;
    }

    @Override
    public void onMetadata(ArrayList<EmpSeries> metadata) {
        this.seriesAdapter.setAllSeries(metadata);
        this.seriesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onError(ExposureError error) {
        Log.d(this.getClass().toString(), error.toString());
    }
}