package com.emp.demo.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ebs.android.exposure.models.EmpAsset;
import com.emp.demo.app.AppController;
import com.emp.demo.R;

import java.util.ArrayList;

/**
 * Created by Joao Coelho on 16/07/2017.
 */

public class SearchResultsAdapter extends BaseAdapter {
    ArrayList<EmpAsset> assets;
    Activity root;

    public SearchResultsAdapter(Activity root, ArrayList<EmpAsset> assets) {
        this.assets = assets;
        this.root = root;
    }

    public void setAssets(ArrayList<EmpAsset> assets) {
        this.assets = assets;
    }

    @Override
    public int getCount() {
        if(this.assets == null) {
            return 0;
        }
        return this.assets.size();
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
        final EmpAsset asset = this.assets.get(i);

        LayoutInflater inflater = (LayoutInflater) this.root.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.search_result_item, null);
        view.setTag(asset);

        TextView channelNameView = (TextView) view.findViewById(R.id.asset_title);
        channelNameView.setText(asset.originalTitle);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            AppController.playAsset(root, asset);
            }
        });

        return view;
    }
}
