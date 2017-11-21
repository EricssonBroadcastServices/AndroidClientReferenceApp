package com.emp.demo.app;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import net.ericsson.emovs.utilities.models.EmpImage;
import net.ericsson.emovs.utilities.models.LocalizedMetadata;
import net.ericsson.emovs.utilities.ui.ViewHelper;

import com.emp.demo.activity.MyDownloads;
import com.emp.demo.activity.MyVideoPlayer;
// import com.squareup.leakcanary.LeakCanary;

import net.ericsson.emovs.utilities.interfaces.IPlayable;

import net.ericsson.emovs.download.EMPDownloadProvider;
import net.ericsson.emovs.playback.ui.views.OverlayPlayerView;
import net.ericsson.emovs.utilities.emp.EMPRegistry;

import java.util.ArrayList;


public class AppController extends Application {
    public static final String TAG = AppController.class.getSimpleName();

    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        //if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
        //    return;
        //}
        //LeakCanary.install(this);
        mInstance = this;
        EMPRegistry.bindApplicationContext(this);
        EMPRegistry.bindExposureContext(Constants.API_URL, Constants.CUSTOMER, Constants.BUSSINESS_UNIT);

        //try {
        //    EMPDownloadProvider.getInstance().startService();
        //} catch (Exception e) {
        //    e.printStackTrace();
        //}
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }


    public static void playAsset(Context ctx, IPlayable playable) {
        Intent intent = new Intent(ctx, MyVideoPlayer.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("playable", playable);
        ctx.startActivity(intent);
    }

    public static void playOverlayAsset(Activity activity, IPlayable playable) {
        ArrayList<OverlayPlayerView> views = ViewHelper.getViewsFromViewGroup(activity.getWindow().getDecorView(), OverlayPlayerView.class);
        if(views.size() == 0) {
            return;
        }
        views.get(0).play(playable);
    }

    public static void openMyDownloads(Context ctx) {
        Intent intent = new Intent(ctx, MyDownloads.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }

    public static void requestStoragePermission(final Activity actvity) {
        //Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        //        Uri.parse("package:" + actvity.getPackageName()));
        //intent.addCategory(Intent.CATEGORY_DEFAULT);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //actvity.startActivity(intent);
        if (ContextCompat.checkSelfPermission(actvity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            /*if (!ActivityCompat.shouldShowRequestPermissionRationale(actvity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(actvity, R.style.MyMaterialTheme);
                alertBuilder.setCancelable(true);
                alertBuilder.setTitle("Permission necessary");
                alertBuilder.setMessage("Storage Permission is needed for download functionality.");
                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.parse("package:" + actvity.getPackageName()));
                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        actvity.startActivity(intent);
                    }
                });
                AlertDialog alert = alertBuilder.create();
                alert.show();
            } else {*/
                ActivityCompat.requestPermissions(actvity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 541);
            //}
        }
    }

    public static EmpImage getImage(LocalizedMetadata metadata) {
        EmpImage image = metadata.getImage("en", EmpImage.Orientation.PORTRAIT);
        if(image == null) {
            image = metadata.getImage("en", EmpImage.Orientation.LANDSCAPE);
        }
        return image;
    }
}