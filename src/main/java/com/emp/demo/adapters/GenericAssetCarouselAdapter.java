package com.emp.demo.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.ericsson.emovs.download.DownloadProperties;
import net.ericsson.emovs.utilities.models.EmpAsset;
import net.ericsson.emovs.utilities.models.EmpImage;

import com.emp.demo.activity.GenericAssetCarousel;
import com.emp.demo.app.AppController;
import com.emp.demo.R;
import com.emp.demo.interfaces.IAssetCarouselAdapter;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import net.ericsson.emovs.download.EMPDownloadProvider;

import java.util.ArrayList;

/**
 * Created by Joao Coelho on 2017-07-19.
 */
public class GenericAssetCarouselAdapter extends RecyclerView.Adapter<GenericAssetCarouselAdapter.ViewHolder> implements IAssetCarouselAdapter {
    ArrayList<EmpAsset> assets;
    GenericAssetCarousel root;

    public GenericAssetCarouselAdapter(GenericAssetCarousel root) {
        this.root = root;
    }

    @Override
    public void setAssets(ArrayList<EmpAsset> assets) {
        this.assets = assets;
    }

    @Override
    public GenericAssetCarouselAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) this.root.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.generic_carousel_asset_item, parent, false);
        final GenericAssetCarouselAdapter.ViewHolder holder = new GenericAssetCarouselAdapter.ViewHolder(view);
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
    public void onBindViewHolder(GenericAssetCarouselAdapter.ViewHolder holder, int position) {
        if(this.assets == null) {
            return;
        }
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
//        TextView dayRefView;
        TextView descriptionView;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleView = (TextView) itemView.findViewById(R.id.asset_title);
//            dayRefView = (TextView) itemView.findViewById(R.id.epg_program_datetime_dayref);
            descriptionView = (TextView) itemView.findViewById(R.id.asset_description);
            imageView = (ImageView) itemView.findViewById(R.id.asset_image);
        }

        public void render(final EmpAsset asset) {
            titleView.setText(asset.originalTitle);
            //dayRefView.setText(asset.timeHumanRefernce(EmpProgram.DateRef.START));
            EmpImage image = AppController.getImage(asset.localized);
            if (image != null && image.url != null) {
                AppController.PICASSO.load(image.url).into(imageView);
            }
            else {
                AppController.PICASSO.load(R.drawable.noimage_thumbnail).into(imageView);
            }
            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //AppController.playOverlayAsset(root, asset);
//                    AppController.playAsset(root, asset, null);
                    root.showPlayer(root, asset, null);
                }
            });

            this.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    //AppController.downloadAsset(root, asset);
                    try {
                        EMPDownloadProvider.getInstance().add(asset, DownloadProperties.DEFAULT);
                        Toast.makeText(root.getApplicationContext(), "ASSET QUEUED FOR DOWNLOAD", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            });
        }
    }
}
