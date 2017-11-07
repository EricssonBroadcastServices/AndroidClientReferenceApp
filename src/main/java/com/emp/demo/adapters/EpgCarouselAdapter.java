package com.emp.demo.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ebs.android.exposure.models.EmpChannel;
import com.ebs.android.exposure.models.EmpImage;
import com.ebs.android.exposure.models.EmpProgram;
import com.emp.demo.app.AppController;
import com.emp.demo.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Joao Coelho on 2017-07-19.
 */
public class EpgCarouselAdapter extends RecyclerView.Adapter<EpgCarouselAdapter.ViewHolder> {
    EmpChannel channel;
    Activity root;

    public EpgCarouselAdapter(Activity root, EmpChannel channel) {
        this.channel = channel;
        this.root = root;
    }

    public EmpChannel getChannel() {
        return this.channel;
    }

    public boolean setEpg(ArrayList<EmpProgram> programs) {
        if(this.channel == null) {
            return false;
        }
        this.channel.programs = programs;
        return true;
    }

    @Override
    public EpgCarouselAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) this.root.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.epg_program_item, parent, false);
        final EpgCarouselAdapter.ViewHolder holder = new EpgCarouselAdapter.ViewHolder(view);
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
    public void onBindViewHolder(EpgCarouselAdapter.ViewHolder holder, int position) {
        if(this.channel == null || this.channel.programs == null) {
            return;
        }
        if(position >= this.channel.programs.size()) {
            return;
        }
        EmpProgram program = this.channel.programs.get(position);
        //holder.itemView.setTag(asset);
        holder.render(program);
    }

    @Override
    public int getItemCount() {
        if(this.channel == null) {
            return 0;
        }
        if(this.channel.programs == null) {
            return 0;
        }
        return this.channel.programs.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleView;
        TextView dayRefView;
        TextView timeRefView;
        TextView descriptionView;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleView = (TextView) itemView.findViewById(R.id.epg_program_title);
            dayRefView = (TextView) itemView.findViewById(R.id.epg_program_datetime_dayref);
            timeRefView = (TextView) itemView.findViewById(R.id.epg_program_datetime_time);
            descriptionView = (TextView) itemView.findViewById(R.id.epg_program_description);
            imageView = (ImageView) itemView.findViewById(R.id.epg_program_image);
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        public void render(final EmpProgram asset) {
            titleView.setText(asset.localized.getTitle("sv"));
            dayRefView.setText(asset.timeHumanRefernce(EmpProgram.DateRef.START));
            timeRefView.setText(asset.getTime(EmpProgram.DateRef.START) + " - " + asset.getTime(EmpProgram.DateRef.END));
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
            if(asset.isFuture()) {
                this.itemView.setAlpha(0.5f);
                this.itemView.setEnabled(false);
            }
            else {
                this.itemView.setEnabled(true);
            }
        }
    }
}
