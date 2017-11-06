package com.emp.demo.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ebs.android.exposure.models.EmpChannel;
import com.emp.demo.R;
import com.emp.demo.activity.ChannelDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Joao Coelho on 16/07/2017.
 */

public class ChannelsAdapter extends BaseAdapter {
    ArrayList<EmpChannel> channels;
    Activity root;

    public ChannelsAdapter(Activity root, ArrayList<EmpChannel> channels) {
        this.channels = channels;
        this.root = root;
    }

    public void setChannels(ArrayList<EmpChannel> channels) {
        this.channels = channels;
    }

    @Override
    public int getCount() {
        if(this.channels == null) {
            return 0;
        }
        return this.channels.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final EmpChannel channel = this.channels.get(i);

        //if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.root.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.channel_item, null);
            view.setTag(channel);
        //} else {
        //    carousel = (EmpChannel) view.getTag();
        //}

        TextView channelNameView = (TextView) view.findViewById(R.id.channel_name);
        ImageView channelLogoView = (ImageView) view.findViewById(R.id.channel_logo);


        channelNameView.setText(channel.name);
        if (channel.logoUrl != null) {
            Picasso.with(root.getApplicationContext()).load(channel.logoUrl).into(channelLogoView);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(root, ChannelDetails.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("channel", channel);
                root.startActivity(intent);
            }
        });

        return view;
    }
}
