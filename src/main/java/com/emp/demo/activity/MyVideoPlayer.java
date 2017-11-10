package com.emp.demo.activity;

import android.os.Bundle;

import com.emp.demo.R;

import net.ericsson.emovs.playback.PlaybackProperties;
import net.ericsson.emovs.playback.ui.activities.SimplePlaybackActivity;

public class MyVideoPlayer extends SimplePlaybackActivity {

    public MyVideoPlayer() {
        super(PlaybackProperties.DEFAULT);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //NOTE: if customer wants a custom view -> bindContentView(R.layout.customview);
        super.onCreate(savedInstanceState);
    }
}
