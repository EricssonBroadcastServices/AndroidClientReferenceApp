package com.emp.demo.callbacks;

import android.util.Log;

import com.ebs.android.exposure.clients.exposure.ExposureError;
import com.ebs.android.exposure.metadata.IMetadataCallback;
import com.ebs.android.exposure.models.EmpAsset;
import com.emp.demo.interfaces.IAssetCarouselAdapter;

import java.util.ArrayList;

/**
 * Created by Joao Coelho on 23/07/2017.
 */

public class AssetListCallback implements IMetadataCallback<ArrayList<EmpAsset>> {
    IAssetCarouselAdapter adapter;

    public AssetListCallback(IAssetCarouselAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onMetadata(ArrayList<EmpAsset> metadata) {
        this.adapter.setAssets(metadata);
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void onError(ExposureError error) {
        Log.d(getClass().toString(), error.toString());
    }
}
