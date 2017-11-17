package com.emp.demo.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.ericsson.emovs.exposure.entitlements.Entitlement;
import net.ericsson.emovs.exposure.interfaces.IPlayable;
import net.ericsson.emovs.exposure.models.EmpAsset;
import net.ericsson.emovs.exposure.models.EmpImage;
import com.emp.demo.R;
import com.emp.demo.app.AppController;
import com.squareup.picasso.Picasso;

import net.ericsson.emovs.download.DownloadItem;
import net.ericsson.emovs.download.EMPDownloadProvider;
import net.ericsson.emovs.download.interfaces.IDownload;
import net.ericsson.emovs.download.interfaces.IDownloadEventListener;
import net.ericsson.emovs.utilities.StorageMetrics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import static com.emp.demo.R.id.pauseBtn;
import static com.emp.demo.R.id.resumeBtn;

/**
 * Created by Joao Coelho on 16/07/2017.
 */

public class DownloadListAdapter extends BaseAdapter {
    ArrayList<IDownload> downloadItems;
    ArrayList<Pair<String, IDownload>> listenerKeys;
    Activity root;

    public DownloadListAdapter(Activity root, ArrayList<IDownload> downloadItems) {
        this.listenerKeys = new ArrayList<>();
        this.downloadItems = downloadItems;
        this.root = root;
    }

    public void setDownloadItems(ArrayList<IDownload> downloadItems) {
        this.downloadItems = downloadItems;
    }

    @Override
    public int getCount() {
        if(this.downloadItems == null) {
            return 0;
        }
        return this.downloadItems.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void setPauseListener(ImageButton btn, final IPlayable playable) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EMPDownloadProvider.getInstance().pause(playable);
            }
        });
    }

    public void setResumeListener(ImageButton btn, final IPlayable playable) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EMPDownloadProvider.getInstance().resume(playable);
            }
        });
    }

    public void setRetryListener(ImageButton btn, final IPlayable playable, final Runnable runnable) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadStartUi(null, runnable);
                EMPDownloadProvider.getInstance().retry(playable);
            }
        });
    }

    public void setDeleteListener(View v, final Runnable runnable) {
        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                runnable.run();
                return false;
            }
        });
    }

    public void downloadStartUi(final IDownload item, final Runnable runnable) {
        root.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                runnable.run();
            }
        });
    }

    public void removeListeners() {
        for (Pair<String,IDownload> key : this.listenerKeys) {
            key.second.removeEventListener(key.first);
        }
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final IDownload item = this.downloadItems.get(i);

        LayoutInflater inflater = (LayoutInflater) this.root.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.download_item, null);
        view.setTag(item);

        final TextView assetTitleView = (TextView) view.findViewById(R.id.asset_title);
        final TextView stateView = (TextView) view.findViewById(R.id.state);
        final ImageView assetImageView = (ImageView) view.findViewById(R.id.asset_image);
        final ImageButton playBtnView = (ImageButton) view.findViewById(R.id.playbtn);
        final ImageButton pauseBtnView = (ImageButton) view.findViewById(pauseBtn);
        final ImageButton resumeBtnView = (ImageButton) view.findViewById(resumeBtn);
        final ImageButton retryBtnView = (ImageButton) view.findViewById(R.id.retryBtn);
        final TextView progressView = (TextView) view.findViewById(R.id.progress);
        final TextView assetSizeView = (TextView) view.findViewById(R.id.asset_size);

        final Runnable runnableStartDownload = new Runnable() {
            @Override
            public void run() {
                stateView.setText("");
                progressView.setVisibility(View.VISIBLE);
                playBtnView.setVisibility(View.GONE);
                pauseBtnView.setVisibility(View.VISIBLE);
                resumeBtnView.setVisibility(View.GONE);
                retryBtnView.setVisibility(View.GONE);
                progressView.setText(item == null ? "0 %" : Integer.toString((int) Math.round(item.getProgress())) + " %");
                assetSizeView.setVisibility(View.VISIBLE);
            }
        };

        final Runnable deleteRunnable = new Runnable() {
            @Override
            public void run() {
                EMPDownloadProvider.getInstance().delete(item.getOnlinePlayable());
                //finalView.setVisibility(View.GONE);
                downloadItems.remove(item);
                notifyDataSetChanged();
                Toast.makeText(root.getApplicationContext(), "ASSET DELETED", Toast.LENGTH_SHORT).show();
            }
        };

        setDeleteListener(view, deleteRunnable);
        setDeleteListener(progressView, deleteRunnable);
        setDeleteListener(assetTitleView, deleteRunnable);
        setDeleteListener(assetImageView, deleteRunnable);
        setDeleteListener(stateView, deleteRunnable);
        setRetryListener(retryBtnView, item.getOnlinePlayable(), runnableStartDownload);
        setPauseListener(pauseBtnView, item.getOnlinePlayable());
        setResumeListener(resumeBtnView, item.getOnlinePlayable());

        if (item.getOnlinePlayable() instanceof EmpAsset) {
            EmpAsset asset = (EmpAsset) item.getOnlinePlayable();
            assetTitleView.setText(asset.originalTitle);
            EmpImage image = AppController.getImage(asset.localized);
            if (image != null && image.url != null) {
                Picasso.with(root.getApplicationContext()).load(image.url).fit().into(assetImageView);
            }
            else {
                Picasso.with(root.getApplicationContext()).load(R.drawable.noimage_thumbnail).into(assetImageView);
            }
        }

        assetSizeView.setText(StorageMetrics.formatSize(item.getDownloadedSize()));

        if (item.getState() == DownloadItem.State.COMPLETED) {
            stateView.setText("");
            progressView.setVisibility(View.GONE);
            pauseBtnView.setVisibility(View.GONE);
            resumeBtnView.setVisibility(View.GONE);
            retryBtnView.setVisibility(View.GONE);
            playBtnView.setVisibility(View.VISIBLE);
            assetSizeView.setVisibility(View.VISIBLE);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppController.playAsset(root, item.getDownloadedAsset());
                }
            });
            assetTitleView.setClickable(true);
            assetTitleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppController.playAsset(root, item.getDownloadedAsset());
                }
            });
            assetImageView.setClickable(true);
            assetImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppController.playAsset(root, item.getDownloadedAsset());
                }
            });
        }
        else if (item.getState() == DownloadItem.State.FAILED) {
            progressView.setVisibility(View.GONE);
            playBtnView.setVisibility(View.GONE);
            pauseBtnView.setVisibility(View.GONE);
            resumeBtnView.setVisibility(View.GONE);
            retryBtnView.setVisibility(View.VISIBLE);
            assetSizeView.setVisibility(View.GONE);
            stateView.setText("ERROR");
        }
        else if (item.getState() == DownloadItem.State.DOWNLOADING) {
            downloadStartUi(item, runnableStartDownload);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppController.playAsset(root, item.getDownloadedAsset());
                }
            });
            assetTitleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppController.playAsset(root, item.getDownloadedAsset());
                }
            });
            assetImageView.setClickable(true);
            assetImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppController.playAsset(root, item.getDownloadedAsset());
                }
            });
        }
        else if (item.getState() == DownloadItem.State.PAUSED) {
            stateView.setText("");
            progressView.setVisibility(View.VISIBLE);
            playBtnView.setVisibility(View.GONE);
            pauseBtnView.setVisibility(View.GONE);
            resumeBtnView.setVisibility(View.VISIBLE);
            retryBtnView.setVisibility(View.GONE);
            assetSizeView.setVisibility(View.VISIBLE);
            progressView.setText(Integer.toString((int) Math.round(item.getProgress())) + " %");
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppController.playAsset(root, item.getDownloadedAsset());
                }
            });
            assetTitleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppController.playAsset(root, item.getDownloadedAsset());
                }
            });
            assetImageView.setClickable(true);
            assetImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppController.playAsset(root, item.getDownloadedAsset());
                }
            });
        }
        else if (item.getState() == DownloadItem.State.QUEUED) {
            progressView.setVisibility(View.GONE);
            playBtnView.setVisibility(View.GONE);
            pauseBtnView.setVisibility(View.GONE);
            stateView.setText("QUEUED");
            resumeBtnView.setVisibility(View.GONE);
            retryBtnView.setVisibility(View.GONE);
            assetSizeView.setVisibility(View.GONE);
        }

        final View finalView = view;

        String adapterKey = UUID.randomUUID().toString();
        this.listenerKeys.add(new Pair<>(adapterKey, item));

        item.addEventListener(adapterKey, new IDownloadEventListener() {
            @Override
            public void onStart() {
                root.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        downloadStartUi(item, runnableStartDownload);
                    }
                });
            }

            @Override
            public void onProgressUpdate(final double progress) {
                if (progress < 0) {
                    return;
                }
                final String newProgress = Integer.toString((int) Math.round(progress)) + " %";
                if (progressView.getText().equals(newProgress) == false){
                    root.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(stateView != null) {
                                stateView.setText("");
                            }
                            if (progressView != null) {
                                progressView.setText(newProgress);
                                progressView.setVisibility(View.VISIBLE);
                            }
                            if(assetSizeView != null) {
                                assetSizeView.setText(StorageMetrics.formatSize(item.getDownloadedSize()));
                            }
                        }
                    });
                }
            }

            @Override
            public void onStop() {

            }

            @Override
            public void onPause() {
                root.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pauseBtnView.setVisibility(View.GONE);
                        resumeBtnView.setVisibility(View.VISIBLE);
                        retryBtnView.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onResume() {
                root.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pauseBtnView.setVisibility(View.VISIBLE);
                        resumeBtnView.setVisibility(View.GONE);
                        retryBtnView.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onEntitlement(Entitlement entitlement) {

            }

            @Override
            public void onError(int errorCode, String errorMessage) {
                root.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pauseBtnView.setVisibility(View.GONE);
                        progressView.setVisibility(View.GONE);
                        resumeBtnView.setVisibility(View.GONE);
                        retryBtnView.setVisibility(View.VISIBLE);
                        assetSizeView.setVisibility(View.GONE);
                        if(stateView != null) {
                            stateView.setText("ERROR");
                        }
                    }
                });

            }

            @Override
            public void onSuccess() {
                root.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressView.setVisibility(View.GONE);
                        pauseBtnView.setVisibility(View.GONE);
                        playBtnView.setVisibility(View.VISIBLE);
                        resumeBtnView.setVisibility(View.GONE);
                        retryBtnView.setVisibility(View.GONE);
                        finalView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AppController.playAsset(root, item.getDownloadedAsset());
                            }
                        });
                        assetTitleView.setClickable(true);
                        assetTitleView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AppController.playAsset(root, item.getDownloadedAsset());
                            }
                        });
                        assetImageView.setClickable(true);
                        assetImageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AppController.playAsset(root, item.getDownloadedAsset());
                            }
                        });
                    }
                });
            }
        });

        return view;
    }
}
