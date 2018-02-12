package com.emp.demo.callbacks;

import android.util.Log;

import net.ericsson.emovs.exposure.metadata.EMPMetadataProvider;
import net.ericsson.emovs.utilities.interfaces.IMetadataCallback;
import net.ericsson.emovs.utilities.models.EmpCustomer;
import net.ericsson.emovs.utilities.errors.Error;

import com.emp.demo.adapters.CarouselGroupAdapter;


/**
 * Created by Joao Coelho on 2017-07-18.
 */
public class MainConfigCallback implements IMetadataCallback<EmpCustomer> {
    CarouselGroupAdapter adapter;

    public MainConfigCallback(CarouselGroupAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onMetadata(EmpCustomer metadata) {
        String carouselGroupId = metadata.getCarouselGroupId();
        if (carouselGroupId != null && this.adapter != null) {
            EMPMetadataProvider.getInstance().getCarouselGroupById(carouselGroupId, new CarouselGroupCallback(this.adapter));
        }
    }

    @Override
    public void onError(Error error) {
        Log.d(this.getClass().toString(), error.toString());
    }
}
