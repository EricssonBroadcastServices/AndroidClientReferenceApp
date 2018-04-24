package com.emp.demo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.emp.demo.R;
import com.emp.demo.app.Constants;

import net.ericsson.emovs.playback.EMPPlayer;
import net.ericsson.emovs.playback.EmptyPlaybackEventListener;
import net.ericsson.emovs.playback.PlaybackProperties;
import net.ericsson.emovs.playback.Player;
import net.ericsson.emovs.utilities.emp.UniversalPackagerHelper;
import net.ericsson.emovs.playback.ui.activities.SimplePlaybackActivity;
import net.ericsson.emovs.playback.ui.views.EMPPlayerView;
import net.ericsson.emovs.utilities.system.RunnableThread;

import org.joda.time.DateTime;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MyVideoPlayer extends SimplePlaybackActivity {

    RunnableThread testControlsThread;

    public MyVideoPlayer() {
        super(PlaybackProperties.DEFAULT.withNativeControlsHideOnTouch(false).withNativeControlsShowTimeoutMs(0));
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
            if (Constants.TEST_MODE != null) {
                view.getPlayer().addListener(new EmptyPlaybackEventListener(view.getPlayer()) {
                    @Override
                    public void onStop() {
                        findViewById(R.id.testControls).setVisibility(View.INVISIBLE);
                    }
                    @Override
                    public void onPlaying() {
                        if (testControlsThread != null && testControlsThread.isInterrupted() == false) {
                            testControlsThread.interrupt();
                            testControlsThread = null;
                        }
                        if (player.getEntitlement() == null || UniversalPackagerHelper.isUniversalPackager(player.getEntitlement().mediaLocator) == false) {
                            findViewById(R.id.testControls).setVisibility(View.GONE);
                            return;
                        }
                        testControlsThread = new RunnableThread(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0;; i++) {
                                    final EMPPlayer player = view.getPlayer();
                                    final long[] timeRangeV = player.getSeekTimeRange();
                                    final long[] bufTimeRangeV = player.getBufferedTimeRange();
                                    final long[] bufRangeV = player.getBufferedRange();
                                    final long[] seekRangeV = player.getSeekRange();
                                    if(timeRangeV == null || bufTimeRangeV == null || bufRangeV == null || seekRangeV == null) {
                                        break;
                                    }
                                    final int j = i;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (j == 0) {
                                                findViewById(R.id.testControls).setVisibility(View.VISIBLE);
                                            }
                                            DateTime playheadDT = new DateTime(player.getPlayheadTime());
                                            playheadTime.setText("PlayheadTime: " + playheadDT.toLocalTime());
                                            timeRange.setText("SeekTimeRange = [ " + new DateTime(timeRangeV[0]).toLocalTime() + ", " + new DateTime(timeRangeV[1]).toLocalTime() + " ]");
                                            bufferedTimeRange.setText("BufferedTimeRange = [ " + new DateTime(bufTimeRangeV[0]).toLocalTime() + ", " + new DateTime(bufTimeRangeV[1]).toLocalTime() + " ]");

                                            playheadPosition.setText("PlayheadPosition: " + player.getPlayheadPosition());
                                            seekRange.setText("SeekRange = [ " + seekRangeV[0] + ", " + seekRangeV[1] + " ]");
                                            bufferedRange.setText("BufferedRange = [ " + bufRangeV[0] + ", " + bufRangeV[1] + " ]");

                                            Method field = null;
                                            try {
                                                field = ((Player)player).getClass().getSuperclass().getDeclaredMethod("getTimehisftDelay");
                                                field.setAccessible(true);
                                                long timeshiftVal = (Long) field.invoke(player);
                                                timeshiftDelay.setText("TimeshiftDelay: " + timeshiftVal);
                                            }
                                            catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
                                                e.printStackTrace();
                                            }
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
                        });
                        testControlsThread.start();
                    }
                });
            }
        }
    }
}
