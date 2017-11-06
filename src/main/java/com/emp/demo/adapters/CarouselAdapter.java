package com.emp.demo.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ebs.android.exposure.models.EmpAsset;
import com.ebs.android.exposure.models.EmpImage;
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
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent opn = new Intent(main.getContext(),News_Details.class);
//                opn.putExtra("img", holder.img_str);
//                opn.putExtra("heading",holder.hline_str);
//                opn.putExtra("desc",holder.description_str);
//                opn.putExtra("date",holder.date_str);
//                opn.putExtra("source",holder.source_str);
//                main.getContext().startActivity(opn);
//            }
//        });
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
        TextView titleView;
        TextView durationView;
        TextView descriptionView;
        ImageView imageView;

//        String hline_str;
//        String date_str;
//        String description_str;
//        String img_str;
//        public String source_str;

        public ViewHolder(View itemView) {
            super(itemView);
//            titleView = (TextView) itemView.findViewById(R.id.hline);
//            durationView = (TextView) itemView.findViewById(R.id.duration);
//            descriptionView = (TextView) itemView.findViewById(R.id.description);
            imageView = (ImageView) itemView.findViewById(R.id.news_img);
        }

        public void render(final EmpAsset asset) {
//            titleView.setText(asset.originalTitle);
//            durationView.setText(asset.duration);
            EmpImage image = asset.getImage("en", EmpImage.Orientation.PORTRAIT);
            if(image == null) {
                image = asset.getImage("en", EmpImage.Orientation.LANDSCAPE);
            }
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
