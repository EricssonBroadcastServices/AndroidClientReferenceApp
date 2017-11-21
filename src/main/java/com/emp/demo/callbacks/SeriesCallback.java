package com.emp.demo.callbacks;

import android.util.Log;

import net.ericsson.emovs.exposure.metadata.IMetadataCallback;
import com.emp.demo.adapters.SeriesAdapter;
import net.ericsson.emovs.utilities.models.EmpSeries;
import net.ericsson.emovs.utilities.errors.Error;

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
    public void onError(Error error) {
        Log.d(this.getClass().toString(), error.toString());
    }
}
