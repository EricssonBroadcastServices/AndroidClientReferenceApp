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

import net.ericsson.emovs.utilities.models.EmpImage;
import com.emp.demo.R;
import com.emp.demo.activity.SeriesDetails;
import net.ericsson.emovs.utilities.models.EmpSeries;
import com.emp.demo.app.AppController;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Joao Coelho on 16/07/2017.
 */

public class SeriesAdapter extends BaseAdapter {
    ArrayList<EmpSeries> allSeries;
    Activity root;


    public SeriesAdapter(Activity root, ArrayList<EmpSeries> allSeries) {
        this.allSeries = allSeries;
        this.root = root;
    }

    @Override
    public int getCount() {
        if(this.allSeries == null) {
            return 0;
        }
        return this.allSeries.size();
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
        final EmpSeries series = this.allSeries.get(i);

        //if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.root.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.series_item, null);
            view.setTag(series);
        //} else {
        //    carousel = (EmpChannel) view.getTag();
        //}

        TextView channelNameView = (TextView) view.findViewById(R.id.series_name);
//        ImageView channelLogoView = (ImageView) view.findViewById(R.id.series_logo);
        View seriesLogoView = view.findViewById(R.id.series_logo);

        channelNameView.setText(series.localized.getTitle("en"));
//        EmpImage image = AppController.getImage(series.localized);
//        if (image != null && image.url != null) {
//            AppController.PICASSO.load(image.url).into(channelLogoView);
//        }
//        else {
//            AppController.PICASSO.load(R.drawable.noimage_thumbnail).into(channelLogoView);
//        }

        seriesLogoView.setBackgroundColor(series.seriesId.hashCode());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(root, SeriesDetails.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("series", series);
                root.startActivity(intent);
            }
        });

        return view;
    }

    public void setAllSeries(ArrayList<EmpSeries> allSeries) {
        this.allSeries = allSeries;
    }
}
