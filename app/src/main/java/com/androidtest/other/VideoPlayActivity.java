package com.androidtest.other;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import helloworld.android.com.androidtest.R;

public class VideoPlayActivity extends Activity {

    private VideoView videoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoplay);

        // VideoView
        videoView = findViewById(R.id.videoView);
    }

    // VideoView播放视频
    public void playVideoWithVideoView(View view){
        String path = "/sdcard/Music/movie.mp4";

        File file = new File(path);
        if (!file.exists()){
            InputStream inputStream = getResources().openRawResource(R.raw.movie);
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(path);
                byte[] bytes = new byte[inputStream.available()];

                int length = 0;
                while ((length = inputStream.read(bytes)) != -1){
                    fileOutputStream.write(bytes,0,length);
                }

                fileOutputStream.close();
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        videoView.setVideoPath(path);
        videoView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    // 用MediaPlayer播放
    public void playWithMediaPlayer(View view){
        Intent intent = new Intent(this,VideoPlayMediaActivity.class);
        startActivity(intent);
    }

    // 用VideoView播放
    public void playWithVideoView(View view){
        Intent intent = new Intent(this,VideoPlayVideoViewActivity.class);
        startActivity(intent);
    }
}
