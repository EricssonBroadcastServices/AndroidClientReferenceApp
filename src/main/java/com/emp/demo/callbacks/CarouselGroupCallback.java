package com.emp.demo.callbacks;

import android.util.Log;

import net.ericsson.emovs.exposure.clients.exposure.ExposureError;
import net.ericsson.emovs.exposure.metadata.IMetadataCallback;
import com.emp.demo.adapters.CarouselGroupAdapter;
import net.ericsson.emovs.exposure.models.EmpCarousel;

import java.util.ArrayList;

/**
 * Created by Joao Coelho on 2017-07-17.
 */

public class CarouselGroupCallback implements IMetadataCallback<ArrayList<EmpCarousel>> {
    CarouselGroupAdapter carouselGroupAdapter;

    public CarouselGroupCallback(CarouselGroupAdapter carouselGroupAdapter) {
        super();
        this.carouselGroupAdapter = carouselGroupAdapter;
    }

    @Override
    public void onMetadata(ArrayList<EmpCarousel> metadata) {
        this.carouselGroupAdapter.setCarousels(metadata);
        this.carouselGroupAdapter.notifyDataSetChanged();
    }

    @Override
    public void onError(ExposureError error) {
        Log.d(getClass().toString(), error.toString());
    }
}
