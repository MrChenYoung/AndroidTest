package com.androidtest.other;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import helloworld.android.com.androidtest.R;

public class VideoPlayActivity extends Activity {

    private static final int MEDIAPLAYER_IS_PLAYING = 1;
    private static final int MEDIAPLAYER_IS_PAUSE = 2;
    private static final int MEDIAPLAYER_IS_STOP = 3;
    private static int MEDIAPLAYER_STATE = MEDIAPLAYER_IS_STOP;
    private MediaPlayer mediaPlayer;
    private SurfaceHolder surfaceHolder;
    private VideoView videoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoplay);

        // MediaPlayer
        mediaPlayer = MediaPlayer.create(this,R.raw.movie);
//        mediaPlayer.prepareAsync();
        surfaceHolder = ((SurfaceView)findViewById(R.id.surfaceView)).getHolder();
//        surfaceHolder.setFixedSize(mediaPlayer.getVideoWidth(),
//                mediaPlayer.getVideoHeight());

        // VideoView
        videoView = findViewById(R.id.videoView);
    }

    // MediaPlayer播放视频
    public void playVideoWithMediaPlayer(View view){
        if (MEDIAPLAYER_STATE == MEDIAPLAYER_IS_STOP || MEDIAPLAYER_STATE == MEDIAPLAYER_IS_PAUSE){
            play();
            MEDIAPLAYER_STATE = MEDIAPLAYER_IS_PLAYING;
        }else if (MEDIAPLAYER_STATE == MEDIAPLAYER_IS_PLAYING) {
            pause();
            MEDIAPLAYER_STATE = MEDIAPLAYER_IS_PAUSE;
        }
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

    // 播放
    public void play(){
        mediaPlayer.setDisplay(surfaceHolder);
        mediaPlayer.start();
    }

    // 暂停
    public void pause(){
        mediaPlayer.pause();
    }

    // 停止
    public void stop(){
        mediaPlayer.stop();
    }

    @Override
    protected void onPause() {
        super.onPause();

        stop();
    }
}
