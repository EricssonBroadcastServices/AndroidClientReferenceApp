package com.emp.demo.callbacks;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.ebs.android.exposure.clients.exposure.ExposureError;
import com.ebs.android.exposure.metadata.IMetadataCallback;
import com.ebs.android.exposure.models.EmpProgram;
import com.emp.demo.adapters.EpgCarouselAdapter;
import java.util.ArrayList;

/**
 * Created by Joao Coelho on 2017-07-19.
 */
public class EpgCallback implements IMetadataCallback<ArrayList<EmpProgram>> {
    RecyclerView epgCarousel;

    public EpgCallback(RecyclerView epgCarousel) {
        this.epgCarousel = epgCarousel;
    }

    private EpgCarouselAdapter getAdapter() {
        return (EpgCarouselAdapter) epgCarousel.getAdapter();
    }

    @Override
    public void onMetadata(ArrayList<EmpProgram> metadata) {
        getAdapter().setEpg(metadata);
        epgCarousel.scrollToPosition(getAdapter().getChannel().liveProgramIndex());
        getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onError(ExposureError error) {
        Log.d(getClass().toString(), error.toString());
    }
}
