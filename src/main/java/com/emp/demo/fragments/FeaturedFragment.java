package com.emp.demo.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import com.ebs.android.exposure.metadata.EMPMetadataProvider;
import com.emp.demo.R;
import com.emp.demo.adapters.CarouselGroupAdapter;
import com.emp.demo.callbacks.MainConfigCallback;

/**
 * Created by Joao Coelho on 6/7/2017.
 */
public class FeaturedFragment extends Fragment {
    View rootView;
    CarouselGroupAdapter carouselGroupAdapter;
    ProgressDialog pDialog;



    public FeaturedFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.carouselGroupAdapter = new CarouselGroupAdapter(getActivity(), null);

        final MainConfigCallback mainConfigBuilder = new MainConfigCallback(carouselGroupAdapter);
        EMPMetadataProvider.getInstance().getMainJson(mainConfigBuilder);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.featured, container, false);

        ListView carouselGroup = (ListView) rootView.findViewById(R.id.featured_carousel_group);
        carouselGroup.setAdapter(this.carouselGroupAdapter);

        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);


        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
