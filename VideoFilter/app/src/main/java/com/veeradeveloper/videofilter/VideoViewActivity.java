package com.veeradeveloper.videofilter;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.VideoView;

public class VideoViewActivity extends AppCompatActivity {
    String videoInputPath;
    private static final String TAG = "VideoViewActivity";
    VideoView videoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videoview_activity);

        if (getIntent().getExtras() != null) {
            videoInputPath = getIntent().getExtras().getString(ConstantFlag.VIDEO_KEY);
            Log.e(TAG, "Incoming Video File:" + videoInputPath);
        }

        videoView = (VideoView) findViewById(R.id.videoview);
        videoView.setVideoPath(videoInputPath);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.start();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (videoView != null) {
            videoView.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (videoView != null) {
            videoView.resume();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (videoView != null) {
            videoView.stopPlayback();
        }
    }
}
