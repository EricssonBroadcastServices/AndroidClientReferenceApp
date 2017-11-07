package com.emp.demo.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import net.ericsson.emovs.exposure.models.EmpAsset;
import net.ericsson.emovs.exposure.models.EmpImage;
import com.emp.demo.app.AppController;
import com.emp.demo.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Joao Coelho on 16/07/2017.
 */

public class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.ViewHolder> {
    ArrayList<EmpAsset> assets;
    Activity root;

    public CarouselAdapter(Activity root, ArrayList<EmpAsset> assets) {
        this.assets = assets;
        this.root = root;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) this.root.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.carousel_item, parent, false);
        final CarouselAdapter.ViewHolder holder = new CarouselAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(position >= this.assets.size()) {
            return;
        }
        EmpAsset asset = this.assets.get(position);
        //holder.itemView.setTag(asset);
        holder.render(asset);
    }

    @Override
    public int getItemCount() {
        if(this.assets == null) {
            return 0;
        }
        return this.assets.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.asset_img);
        }

        public void render(final EmpAsset asset) {
//            titleView.setText(asset.originalTitle);
//            durationView.setText(asset.duration);
            EmpImage image = AppController.getImage(asset.localized);
            if (image != null && image.url != null) {
                Picasso.with(root.getApplicationContext()).load(image.url).into(imageView);
            }
            else {
                Picasso.with(root.getApplicationContext()).load(R.drawable.noimage_thumbnail).into(imageView);

            }
            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                AppController.playAsset(root, asset);
                }
            });
        }
    }
}
