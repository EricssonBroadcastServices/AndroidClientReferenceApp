package com.emp.demo.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ebs.android.exposure.metadata.EMPMetadataProvider;
import com.emp.demo.R;
import com.emp.demo.adapters.SeriesAdapter;
import com.emp.demo.callbacks.SeriesCallback;

/**
 * Created by Joao Coelho
 */
public class SeriesFragment extends Fragment {
    View rootView;
    SeriesAdapter seriesAdapter;

    public SeriesFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.seriesAdapter = new SeriesAdapter(getActivity(), null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.series, container, false);

        ListView channels = (ListView) rootView.findViewById(R.id.series_list);
        channels.setAdapter(this.seriesAdapter);

        EMPMetadataProvider.getInstance().getSeries(new SeriesCallback(this.seriesAdapter));

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
