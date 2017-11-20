package com.emp.demo.callbacks;

import android.util.Log;


import net.ericsson.emovs.exposure.metadata.IMetadataCallback;
import net.ericsson.emovs.exposure.models.EmpAsset;
import net.ericsson.emovs.utilities.Error;

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
    public void onError(Error error) {
        Log.d(getClass().toString(), error.toString());
    }
}
