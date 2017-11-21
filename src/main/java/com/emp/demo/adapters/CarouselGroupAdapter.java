package com.emp.demo.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.emp.demo.R;
import net.ericsson.emovs.utilities.models.EmpCarousel;

import java.util.ArrayList;

/**
 * Created by Joao Coelho on 16/07/2017.
 */

public class CarouselGroupAdapter extends BaseAdapter {
    ArrayList<EmpCarousel> carousels;
    Activity root;

    public CarouselGroupAdapter(Activity root, ArrayList<EmpCarousel> carousels) {
        this.carousels = carousels;
        this.root = root;
    }

    public void setCarousels(ArrayList<EmpCarousel> carousels) {
        this.carousels = carousels;
    }

    @Override
    public int getCount() {
        if(this.carousels == null) {
            return 0;
        }
        return this.carousels.size();
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
        EmpCarousel carousel = this.carousels.get(i);

        //if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.root.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.carousel, null);
            view.setTag(carousel);

            RecyclerView carouselItems = (RecyclerView) view.findViewById(R.id.carousel_items);
            carouselItems.setAdapter(new CarouselAdapter(root, carousel.assets));

            LinearLayoutManager layoutManager = new LinearLayoutManager(root, LinearLayoutManager.HORIZONTAL, false);
            carouselItems.setLayoutManager(layoutManager);

        //} else {
        //    carousel = (EmpCarousel) view.getTag();
        //}

        TextView carouselTitleView = (TextView) view.findViewById(R.id.carousel_title);


        carouselTitleView.setText(carousel.localized.getTitle("en"));

        return view;
    }
}
