package com.emp.demo.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ebs.android.exposure.models.EmpImage;
import com.emp.demo.app.AppController;
import com.emp.demo.R;
import com.ebs.android.exposure.models.EmpEpisode;
import com.ebs.android.exposure.models.EmpSeries;
import com.squareup.picasso.Picasso;

/**
 * Created by Joao Coelho on 2017-07-19.
 */
public class EpisodesCarouselAdapter extends RecyclerView.Adapter<EpisodesCarouselAdapter.ViewHolder> {
    EmpSeries series;
    Activity root;

    public EpisodesCarouselAdapter(Activity root, EmpSeries series) {
        this.series = series;
        this.root = root;
    }

    public EmpSeries getSeries() {
        return this.series;
    }

    public boolean setSeries(EmpSeries series) {
        if(this.series == null) {
            return false;
        }
        this.series = series;
        return true;
    }

    @Override
    public EpisodesCarouselAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) this.root.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.episode_item, parent, false);
        final EpisodesCarouselAdapter.ViewHolder holder = new EpisodesCarouselAdapter.ViewHolder(view);
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
    public void onBindViewHolder(EpisodesCarouselAdapter.ViewHolder holder, int position) {
        if(this.series == null || this.series.episodes == null) {
            return;
        }
        if(position >= this.series.episodes.size()) {
            return;
        }
        EmpEpisode episode = this.series.episodes.get(position);
        //holder.itemView.setTag(asset);
        holder.render(episode);
    }

    @Override
    public int getItemCount() {
        if(this.series == null) {
            return 0;
        }
        if(this.series.episodes == null) {
            return 0;
        }
        return this.series.episodes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleView;
//        TextView dayRefView;
        TextView episodeNumberView;
        TextView descriptionView;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleView = (TextView) itemView.findViewById(R.id.epg_program_title);
//            dayRefView = (TextView) itemView.findViewById(R.id.epg_program_datetime_dayref);
            episodeNumberView = (TextView) itemView.findViewById(R.id.episode_number);
            descriptionView = (TextView) itemView.findViewById(R.id.epg_program_description);
            imageView = (ImageView) itemView.findViewById(R.id.epg_program_image);
        }

        public void render(final EmpEpisode asset) {
            titleView.setText(asset.originalTitle);
            //dayRefView.setText(asset.timeHumanRefernce(EmpProgram.DateRef.START));
            episodeNumberView.setText("Episode " + asset.episodeNr);
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
