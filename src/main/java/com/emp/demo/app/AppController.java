package com.emp.demo.app;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.ebs.android.exposure.auth.EMPAuthProvider;
import com.ebs.android.exposure.auth.EMPAuthProviderWithStorage;
import com.ebs.android.utilities.ViewHelper;
import com.emp.demo.R;
import com.emp.demo.activity.MyDownloads;
import com.emp.demo.activity.MyVideoPlayer;

import com.ebs.android.exposure.interfaces.IPlayable;

import net.ericsson.emovs.download.EMPDownloadProvider;
import net.ericsson.emovs.playback.ui.views.OverlayPlayerView;
import net.ericsson.emovs.utilities.ContextRegistry;

import java.util.ArrayList;
//import com.onesignal.OneSignal;


public class AppController extends Application {
    public static final String TAG = AppController.class.getSimpleName();

    public static EMPAuthProvider mAuthProvider;
    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        ContextRegistry.bind(this);
        try {
            EMPDownloadProvider.getInstance().startService();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAuthProvider = EMPAuthProviderWithStorage.getInstance(Constants.API_URL, Constants.CUSTOMER, Constants.BUSSINESS_UNIT);
//    OneSignal.setLogLevel(OneSignal.LOG_LEVEL.DEBUG, OneSignal.LOG_LEVEL.WARN);
//    OneSignal.startInit(this)
//            .setNotificationOpenedHandler(new ExampleNotificationOpenedHandler())
//            .setAutoPromptLocation(true)
//            .init();
    }
	
	/*private class ExampleNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
        @Override
        public void notificationOpened(String message, JSONObject additionalData, boolean isActive) {
            String additionalMessage = "";

            try {
                if (additionalData != null) {
                    if (additionalData.has("actionSelected"))
                        additionalMessage += "Pressed ButtonID: " + additionalData.getString("actionSelected");

                    additionalMessage = message + "\nFull additionalData:\n" + additionalData.toString();
                }

                Log.d("OneSignalExample", "message:\n" + message + "\nadditionalMessage:\n" + additionalMessage);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }*/

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
            if (!ActivityCompat.shouldShowRequestPermissionRationale(actvity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(actvity, R.style.MyDialogTheme);
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
            } else {
                ActivityCompat.requestPermissions(actvity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 541);
            }
        }
    }
}