package com.emp.demo.activity;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.emp.demo.R;

import net.ericsson.emovs.playback.EMPPlayer;
import net.ericsson.emovs.playback.EmptyPlaybackEventListener;
import net.ericsson.emovs.playback.PlaybackProperties;
import net.ericsson.emovs.playback.interfaces.ControllerVisibility;
import net.ericsson.emovs.playback.ui.activities.SimplePlaybackActivity;
import net.ericsson.emovs.playback.ui.adapters.LanguageAdapter;
import net.ericsson.emovs.playback.ui.views.EMPPlayerView;
import net.ericsson.emovs.utilities.emp.EMPRegistry;
import net.ericsson.emovs.utilities.system.RunnableThread;

public class MyVideoPlayer extends SimplePlaybackActivity {

    public MyVideoPlayer() {
        super(PlaybackProperties.DEFAULT);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //NOTE: if customer wants a custom view -> bindContentView(R.layout.customview);
        super.onCreate(savedInstanceState);
        injectTestControls();
    }

    protected void injectTestControls() {
        if (this.playerViews == null) {
            return;
        }
        for (final EMPPlayerView view : this.playerViews) {
            if(view == null || view.getPlayer() == null) {
                continue;
            }
            final TextView playheadTime = findViewById(R.id.currentTime);
            final TextView timeRange = findViewById(R.id.timeRange);
            final TextView bufferedTimeRange = findViewById(R.id.bufferedTimeRange);
            final TextView playheadPosition = findViewById(R.id.currentPosition);
            final TextView seekRange = findViewById(R.id.positionRange);
            final TextView bufferedRange = findViewById(R.id.bufferedRange);
            final TextView timeshiftDelay = findViewById(R.id.timeshiftDelay);
            final TextView currentProgram = findViewById(R.id.currentProgram);
            view.getPlayer().addListener(new EmptyPlaybackEventListener(view.getPlayer()) {
                @Override
                public void onPlaying() {
                    new RunnableThread(new Runnable() {
                        @Override
                        public void run() {
                            for (;;) {
                                final EMPPlayer player = view.getPlayer();
                                final long[] timeRangeV = player.getSeekTimeRange();
                                final long[] bufTimeRangeV = player.getBufferedTimeRange();
                                final long[] bufRangeV = player.getBufferedRange();
                                final long[] seekRangeV = player.getSeekRange();
                                if(timeRangeV == null || bufTimeRangeV == null || bufRangeV == null || seekRangeV == null) {
                                    break;
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        playheadTime.setText("PlayheadTime: " + player.getPlayheadTime());
                                        timeRange.setText("SeekTimeRange = [ " + timeRangeV[0] + ", " + timeRangeV[1] + " ]");
                                        bufferedTimeRange.setText("BufferedTimeRange = [ " + bufTimeRangeV[0] + ", " + bufTimeRangeV[1] + " ]");

                                        playheadPosition.setText("PlayheadPosition: " + player.getPlayheadPosition());
                                        seekRange.setText("SeekRange = [ " + seekRangeV[0] + ", " + seekRangeV[1] + " ]");
                                        bufferedRange.setText("BufferedRange = [ " + bufRangeV[0] + ", " + bufRangeV[1] + " ]");

                                        timeshiftDelay.setText("TimeshiftDelay: " + player.getTimehisftDelay());
                                        currentProgram.setText("CurrentProgram: " + (player.getCurrentProgram() != null ? player.getCurrentProgram().programId : "N/A"));
                                    }
                                });
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();
                }
            });
        }
    }
}
