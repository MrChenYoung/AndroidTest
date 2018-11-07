package com.androidtest.other;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import helloworld.android.com.androidtest.R;

public class VideoPlayVideoViewActivity extends AppCompatActivity {

    // 播放状态
    private final int MEDIAPLAYER_IS_PLAYING = 1;
    private final int MEDIAPLAYER_IS_PAUSE = 2;
    private final int MEDIAPLAYER_IS_STOP = 3;
    private int MEDIAPLAYER_STATE = MEDIAPLAYER_IS_STOP;

    // 显示视频控件
    private VideoView videoView;
    private ImageView thumbnailImageView;
    private ImageView btn_play;

    // 记录上次播放的位置
    int lastPlayPosition = 0;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play_video_view);

        // 获取上次播放位置
        sharedPreferences = getSharedPreferences("videoprogress1",MODE_PRIVATE);
        lastPlayPosition = sharedPreferences.getInt("progress",0);
        Toast.makeText(this,"上次播放到:" + lastPlayPosition,Toast.LENGTH_SHORT).show();

        // 找到指定控件
        videoView = findViewById(R.id.videoView);
        thumbnailImageView = findViewById(R.id.iv_thumbnail);
        btn_play = findViewById(R.id.play);

        // 初始化播放器
        initailPlayer();
        setCallBack();
    }

    // 初始化播放器
    public void initailPlayer(){
        // 设置播放路径
        try {
            int videoId = R.raw.class.getDeclaredField("movie").getInt(this);
            Uri uri = Uri.parse("android.resource://" + this.getPackageName() + "/" + videoId);
            videoView.setVideoURI(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 设置缩略图
        Bitmap bitmap = getVideoThumbNail(lastPlayPosition);
        thumbnailImageView.setImageBitmap(bitmap);

        // 调整控件大小
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;
        float scale = screenWidth / bitmap.getWidth();
        int videoHeight =(int) (bitmap.getHeight() * scale);

        // 设置videoView大小
        ViewGroup.LayoutParams videoParams = videoView.getLayoutParams();
        videoParams.width = screenWidth;
        videoParams.height = videoHeight;
        videoView.setLayoutParams(videoParams);

        // 设置缩略图大小
        ViewGroup.LayoutParams thumbImageViewParams = thumbnailImageView.getLayoutParams();
        thumbImageViewParams.width = screenWidth;
        thumbImageViewParams.height = videoHeight;
        thumbnailImageView.setLayoutParams(thumbImageViewParams);

        // 设置播放/暂停界面控件大小
        ViewGroup.LayoutParams playBtnParams = btn_play.getLayoutParams();
        playBtnParams.width = screenWidth;
        playBtnParams.height = videoHeight;
        btn_play.setLayoutParams(playBtnParams);
        btn_play.setImageResource(R.drawable.play);
    }

    // 设置回调
    public void setCallBack(){
        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 播放或暂停
                if (MEDIAPLAYER_STATE == MEDIAPLAYER_IS_STOP){
                    // 播放
                    startPlay();
                }else if (MEDIAPLAYER_STATE == MEDIAPLAYER_IS_PLAYING){
                    // 暂停
                    pausePlay();
                }else if (MEDIAPLAYER_STATE == MEDIAPLAYER_IS_PAUSE){
                    // 继续播放
                    resumePlay();
                }
            }
        });

        // 播放错误回调
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });

        // 播放完成
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                // 从头开始播放
                lastPlayPosition = 0;

                thumbnailImageView.setImageBitmap(getVideoThumbNail(lastPlayPosition));
                btn_play.setImageResource(R.drawable.play);

                MEDIAPLAYER_STATE = MEDIAPLAYER_IS_STOP;
            }
        });
    }

    // 开始播放
    public void startPlay(){
        videoView.start();
        videoView.seekTo(lastPlayPosition);

        MEDIAPLAYER_STATE = MEDIAPLAYER_IS_PLAYING;

        thumbnailImageView.setImageBitmap(null);
        btn_play.setImageBitmap(null);
    }

    // 暂停播放
    public void pausePlay(){
        videoView.pause();

        MEDIAPLAYER_STATE = MEDIAPLAYER_IS_PAUSE;
        btn_play.setImageResource(R.drawable.play);
    }

    // 继续播放
    public void resumePlay(){
        videoView.start();

        MEDIAPLAYER_STATE = MEDIAPLAYER_IS_PLAYING;
        btn_play.setImageBitmap(null);
    }

    // 停止播放
    public void stopPlay(){
        videoView.stopPlayback();

        MEDIAPLAYER_STATE = MEDIAPLAYER_IS_STOP;
    }

    @Override
    protected void onPause() {
        super.onPause();

        // 界面消失暂停播放
        pausePlay();

        // 保存播放位置
        lastPlayPosition = videoView.getCurrentPosition();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 界面销毁，停止播放
        stopPlay();

        sharedPreferences.edit().putInt("progress",lastPlayPosition).commit();
    }

    // 获取视频缩略图
    public Bitmap getVideoThumbNail(int position){
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        try {
            // 获取视频文件在raw文件夹下的uri
            int id = R.raw.class.getDeclaredField("movie").getInt(this);
            Uri videoUri = Uri.parse("android.resource://" + this.getPackageName() + "/" + id);

            // 获取视频指定位置缩略图,getFrameAtTime第一个参数单位是微妙
            mediaMetadataRetriever.setDataSource(this,videoUri);
//            bitmap = mediaMetadataRetriever.getFrameAtTime(position*1000,MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
            bitmap = mediaMetadataRetriever.getFrameAtTime(position*1000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                mediaMetadataRetriever.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return bitmap;
    }
}
