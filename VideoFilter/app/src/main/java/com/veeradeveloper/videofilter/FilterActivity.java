package com.veeradeveloper.videofilter;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class FilterActivity extends Activity implements OnClickListener {
    private static final String TAG = FilterActivity.class.getSimpleName();
    ProgressDialog progressDialog;
    private long durationInMs;
    private String rotation;
    ImageView backIv;
    int checkEffectSelectedOrNot;
    Integer[] effectsArr;
    FFmpeg ffmpeg;
    FilterAdapter gAdapter;
    GridView gridview;
    int height;
    int width;
    String outputPath;
    ImageView overlayIv;

    Bitmap thumb;
    Button tickIv;
    int vidHeight;
    int vidWidth;
    double videoDuration;
    String videoInputPath;
    VideoView videoView;
    String waterMarkerPath;
    LinearLayout demoLinearLayout;

    public FilterActivity() {
        this.checkEffectSelectedOrNot = -1;
        this.thumb = null;
        this.effectsArr = new Integer[]{
                Integer.valueOf(R.drawable.image1), Integer.valueOf(R.drawable.image2), Integer.valueOf(R.drawable.image3),
                Integer.valueOf(R.drawable.image4), Integer.valueOf(R.drawable.image5), Integer.valueOf(R.drawable.image6),
                Integer.valueOf(R.drawable.image7), Integer.valueOf(R.drawable.image8), Integer.valueOf(R.drawable.image9),
                Integer.valueOf(R.drawable.image10), Integer.valueOf(R.drawable.image11), Integer.valueOf(R.drawable.image12),
                Integer.valueOf(R.drawable.image13), Integer.valueOf(R.drawable.image14), Integer.valueOf(R.drawable.image15),
                Integer.valueOf(R.drawable.image16), Integer.valueOf(R.drawable.image17), Integer.valueOf(R.drawable.image18),
                Integer.valueOf(R.drawable.image19), Integer.valueOf(R.drawable.image20), Integer.valueOf(R.drawable.image21),
                Integer.valueOf(R.drawable.image22), Integer.valueOf(R.drawable.image23), Integer.valueOf(R.drawable.image24),
                Integer.valueOf(R.drawable.image25), Integer.valueOf(R.drawable.image26), Integer.valueOf(R.drawable.image27),
                Integer.valueOf(R.drawable.image28), Integer.valueOf(R.drawable.image29), Integer.valueOf(R.drawable.image30)
        };
        this.videoInputPath = null;
        this.outputPath = null;
        this.waterMarkerPath = null;
        this.videoDuration = 0.0d;

    }

    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        Display display = getWindowManager().getDefaultDisplay();
        this.height = display.getHeight();
        this.width = display.getWidth();

        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath().toString() + "/VideoFilter/video");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        this.ffmpeg = FFmpeg.getInstance(this);
        loadFFMpegBinary();

        if (getIntent().getExtras() != null) {

            videoInputPath = getIntent().getExtras().getString(ConstantFlag.VIDEO_KEY);
            Log.e(TAG, "Incoming Video File:" + videoInputPath);
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(this.videoInputPath);
            this.videoDuration = Double.parseDouble(retriever.extractMetadata(9));

            this.durationInMs = VideoControl.getDuration(this, Uri.parse(videoInputPath));
        }



        this.thumb = ThumbnailUtils.createVideoThumbnail(this.videoInputPath, 1);
        initializeViews(this.videoInputPath);
        gettingVideoDimension(this.videoInputPath);
        customVideoEffects();

    }



    private void initializeViews(String videoPath) {
        ConstantFlag.selectedEffect = -1;
        demoLinearLayout = (LinearLayout) findViewById(R.id.demoLinearLayout);
        this.videoView = (VideoView) findViewById(R.id.mVideoView);
        this.gridview = (GridView) findViewById(R.id.filer_gridview);
        this.backIv = (ImageView) findViewById(R.id.back_image);
        this.tickIv = (Button) findViewById(R.id.tick_image);
        this.overlayIv = (ImageView) findViewById(R.id.overlay_iv);

        this.videoView.setVideoURI(Uri.parse(videoPath));
        this.videoView.setOnPreparedListener(new mediaplayerListener());
        this.videoView.requestFocus();
        this.videoView.start();
        this.backIv.setOnClickListener(this);
        this.tickIv.setOnClickListener(this);
        this.gridview.getLayoutParams().width = ((int) (0.2d * ((double) this.width))) * (this.effectsArr.length + 3);

        progressDialog = new ProgressDialog(FilterActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.apply_loader));
        progressDialog.setCancelable(false);
    }


    private void gettingVideoDimension(String path) {
        try {
            MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
            metaRetriever.setDataSource(path);
            this.vidHeight = Integer.parseInt(metaRetriever.extractMetadata(19));
            this.vidWidth = Integer.parseInt(metaRetriever.extractMetadata(18));
            rotation = String.valueOf(metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION));
        } catch (IllegalArgumentException exx) {
            exx.printStackTrace();
        }
    }

    private void customVideoEffects() {
        this.gAdapter = new FilterAdapter(this, this.effectsArr, this.height, this.width, this.thumb);
        this.gridview.setAdapter(this.gAdapter);
        this.gridview.setNumColumns(this.effectsArr.length);
        this.gridview.setOnItemClickListener(new griditemClickListener());
    }

    private void loadFFMpegBinary() {
        try {
            this.ffmpeg.loadBinary(new loadBinaryResponseHandler());
        } catch (FFmpegNotSupportedException e) {
            showUnsupportedExceptionDialog();
        }
    }

    private void showUnsupportedExceptionDialog() {
        new Builder(this).setIcon(getResources().getDrawable(R.mipmap.ic_launcher)).setTitle(getString(R.string.device_not_supported)).setMessage(getString(R.string.device_not_supported_message)).setCancelable(false).setPositiveButton("Cancel", new progressDialogInterface()).create().show();
    }

    class mediaplayerListener implements OnPreparedListener {
        mediaplayerListener() {

        }

        public void onPrepared(MediaPlayer mp) {
            mp.setLooping(true);
            setVideoSize(mp);
        }
    }

    private void setVideoSize(MediaPlayer mediaPlayer) {

        // // Get the dimensions of the video
        int videoWidth = mediaPlayer.getVideoWidth();
        int videoHeight = mediaPlayer.getVideoHeight();
        float videoProportion = (float) videoWidth / (float) videoHeight;

        // Get the width of the screen
        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        float screenProportion = (float) screenWidth / (float) screenHeight;

        // Get the SurfaceView layout parameters
        android.view.ViewGroup.LayoutParams lp = videoView.getLayoutParams();
        if (videoProportion > screenProportion) {
            lp.width = screenWidth;
            lp.height = (int) ((float) screenWidth / videoProportion);
        } else {
            lp.width = (int) (videoProportion * (float) screenHeight);
            lp.height = screenHeight;
        }
        // Commit the layout parameters
        videoView.setLayoutParams(lp);
        overlayIv.setLayoutParams(lp);
    }

    class griditemClickListener implements OnItemClickListener {
        griditemClickListener() {

        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            FilterActivity.this.checkEffectSelectedOrNot = position;
            ConstantFlag.selectedEffect = position;
            Log.e("Position=", TAG + position);
            FilterActivity.this.overlayIv.setVisibility(View.VISIBLE);
            FilterActivity.this.overlayIv.getLayoutParams().height = FilterActivity.this.videoView.getHeight();
            FilterActivity.this.overlayIv.setImageResource(FilterActivity.this.effectsArr[position].intValue());
            FilterActivity.this.gAdapter.notifyDataSetChanged();
        }
    }

    class progressDialogInterface implements DialogInterface.OnClickListener {
        progressDialogInterface() {

        }

        public void onClick(DialogInterface dialog, int which) {
            FilterActivity.this.finish();
        }
    }

    class loadBinaryResponseHandler extends LoadBinaryResponseHandler {
        loadBinaryResponseHandler() {

        }

        public void onFailure() {
            FilterActivity.this.showUnsupportedExceptionDialog();
        }
    }

    class executeBinaryResponseHandler extends ExecuteBinaryResponseHandler {
        final String[] val$command;

        executeBinaryResponseHandler(String[] strArr) {
            this.val$command = strArr;
        }

        public void onFailure(String s) {
            Log.e(TAG, "Failure command : ffmpeg " + s);
            progressDialog.dismiss();
            Toast.makeText(FilterActivity.this, "Failed to save Video", Toast.LENGTH_SHORT).show();
        }

        public void onSuccess(String s) {
            Log.e(TAG, "Succes " + s);
            try {
                new File(FilterActivity.this.videoInputPath).delete();
                Log.e(TAG, "Delete Video File:" + videoInputPath);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                new File(FilterActivity.this.waterMarkerPath).delete();
            } catch (Exception ex2) {
                ex2.printStackTrace();
            }

            progressDialog.dismiss();

            Log.e(TAG, "finalOutput1 : " + outputPath);
            Intent intent = new Intent(FilterActivity.this, VideoViewActivity.class);
            intent.putExtra(ConstantFlag.VIDEO_KEY, outputPath);
            startActivity(intent);
            finish();

        }

        public void onProgress(String s) {
            Log.e(TAG, "Progress command : ffmpeg " + s);
            if (s.contains("time=")) {
                progressDialog.setMessage(getResources().getString(R.string.apply_loader) + ((int) ((((double) VideoControl.progressDurationInMs(s.substring(s.lastIndexOf("time=") + 5, s.lastIndexOf("time=") + 16))) / ((double) FilterActivity.this.durationInMs)) * 100.0d)) + "%             ");
            }
        }

        public void onStart() {
            Log.d(TAG, "Started command : ffmpeg " + this.val$command);

        }

        public void onFinish() {
            Log.d(TAG, "Finished command : ffmpeg " + this.val$command);

        }
    }

    public void onBackPressed() {
        try {
            videoView.pause();
            backActivity();
        } catch (ActivityNotFoundException an) {
            an.printStackTrace();
        }

        try {
            new File(this.waterMarkerPath).delete();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            new File(this.outputPath).delete();
        } catch (Exception ex2) {
            ex2.printStackTrace();
        }
        finish();
    }

    private void backActivity() {
        Intent intent = new Intent(FilterActivity.this, VideoViewActivity.class);
        intent.putExtra(ConstantFlag.VIDEO_KEY, videoInputPath);
        startActivity(intent);
        finish();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_image:
                onBackPressed();
                break;
            case R.id.tick_image:
                Bitmap bm2 = null;

                if (this.checkEffectSelectedOrNot == -1) {
                    Toast.makeText(getApplicationContext(), "Please select atleast one filter effect", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.show();
                if (rotation.equals("90") || rotation.equals("270")) {
                    //portrait
                    bm2 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), this.effectsArr[this.checkEffectSelectedOrNot].intValue()), this.vidHeight, this.vidWidth, false);
                } else {
                    //landscape
                    bm2 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), this.effectsArr[this.checkEffectSelectedOrNot].intValue()), this.vidWidth, this.vidHeight, false);
                }
                String extStorageDirectory = Environment.getExternalStorageDirectory().getAbsolutePath().toString();
                File file = new File(extStorageDirectory, "fx_filter.PNG");
                try {
                    FileOutputStream outStream = new FileOutputStream(file);
                    bm2.compress(CompressFormat.PNG, 100, outStream);
                    outStream.flush();
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.waterMarkerPath = file.getAbsolutePath();
                File dir = new File(extStorageDirectory + "/VideoFilter/video");
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                this.outputPath = dir.getAbsolutePath() + "/Vid_" + System.currentTimeMillis() + ".mp4";

                String[] command = ("-y -i " + this.videoInputPath + " -i " + this.waterMarkerPath + " -filter_complex overlay=main_w-overlay_w:main_h-overlay_h -r 25 -b:v 500k -minrate 500k -maxrate 500k -vcodec mpeg4 -ab 48000 -ac 2 -ar 22050 -c:v libx264 -preset superfast " + this.outputPath).split(" ");
                if (command.length != 0) {
                    execFFmpegBinary(command);
                } else {
                    Toast.makeText(this, getString(R.string.empty_command_toast), Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    private void execFFmpegBinary(String[] command) {
        try {
            this.ffmpeg.execute(command, new executeBinaryResponseHandler(command));
        } catch (FFmpegCommandAlreadyRunningException e) {
        }
    }


    @Override
    protected void onPause() {
        videoView.pause();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        videoView.resume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

