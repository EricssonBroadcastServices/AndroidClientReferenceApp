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
import com.emp.demo.adapters.ChannelsAdapter;
import com.emp.demo.callbacks.ChannelsCallback;

/**
 * Created by Joao Coelho
 */
public class ChannelsFragment extends Fragment {
    View rootView;
    ChannelsAdapter channelsAdapter;

    public ChannelsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.channelsAdapter = new ChannelsAdapter(getActivity(), null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.channels, container, false);

        ListView channels = (ListView) rootView.findViewById(R.id.channel_list);
        channels.setAdapter(this.channelsAdapter);

        EMPMetadataProvider.getInstance().getChannels(new ChannelsCallback(this.channelsAdapter));

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
