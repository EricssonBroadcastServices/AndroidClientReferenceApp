package com.emp.demo.callbacks;

import android.util.Log;

import net.ericsson.emovs.exposure.clients.exposure.ExposureError;
import net.ericsson.emovs.exposure.metadata.IMetadataCallback;
import net.ericsson.emovs.exposure.models.EmpChannel;
import com.emp.demo.adapters.ChannelsAdapter;

import java.util.ArrayList;

/**
 * Created by Joao Coelho on 2017-07-18.
 */
public class ChannelsCallback implements IMetadataCallback<ArrayList<EmpChannel>> {
    ChannelsAdapter channelsAdapter;

    public ChannelsCallback(ChannelsAdapter channelsAdapter) {
        this.channelsAdapter = channelsAdapter;
    }

    @Override
    public void onMetadata(ArrayList<EmpChannel> metadata) {
        this.channelsAdapter.setChannels(metadata);
        this.channelsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onError(ExposureError error) {
        Log.d(getClass().toString(), error.toString());
    }
}
