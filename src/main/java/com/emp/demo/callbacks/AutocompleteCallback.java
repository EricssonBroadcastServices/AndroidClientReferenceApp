package com.emp.demo.callbacks;

import android.util.Log;

import com.ebs.android.exposure.clients.exposure.ExposureError;
import com.ebs.android.exposure.metadata.IMetadataCallback;
import com.ebs.android.exposure.models.EmpAsset;
import com.emp.demo.adapters.SearchResultsAdapter;

import java.util.ArrayList;

/**
 * Created by Joao Coelho on 2017-07-18.
 */
public class AutocompleteCallback implements IMetadataCallback<ArrayList<EmpAsset>> {
    SearchResultsAdapter searchResultsAdapter;

    public AutocompleteCallback(SearchResultsAdapter searchResultsAdapter) {
        this.searchResultsAdapter = searchResultsAdapter;
    }

    @Override
    public void onMetadata(ArrayList<EmpAsset> metadata) {
        this.searchResultsAdapter.setAssets(metadata);
        this.searchResultsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onError(ExposureError error) {
        Log.d(getClass().toString(), error.toString());
    }
}
