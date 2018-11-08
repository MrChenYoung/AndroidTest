package com.androidTest.other;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidTest.R;

public class VideoPlayMediaActivity extends AppCompatActivity {

    // 播放状态
    private final int MEDIAPLAYER_IS_PLAYING = 1;
    private final int MEDIAPLAYER_IS_PAUSE = 2;
    private final int MEDIAPLAYER_IS_STOP = 3;
    private int MEDIAPLAYER_STATE = MEDIAPLAYER_IS_STOP;

    // 记录MediaPlayer上次播放的位置
    private int lastPlayPosition = 0;

    // 播放器
    private MediaPlayer mediaPlayer;

    // 播放按钮
    private ImageView btn_play;
    // 视频缩略图
    private ImageView thumbnailImageView;
    // 视频播放view
    private SurfaceView surfaceView;
    private SharedPreferences sharedPreferences;
    private SurfaceHolder surfaceHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play_media);

        // 上次播放进度读取
        sharedPreferences = getSharedPreferences("videoProgress",MODE_PRIVATE);
        lastPlayPosition = sharedPreferences.getInt("progress",0);
        if (lastPlayPosition > 0){
            Toast.makeText(this,"已跳转到上次播放的位置",Toast.LENGTH_SHORT).show();
        }

        // 初始化mediaplayer播放控件
        initalMediaPlayer();

        // 设置Mediaplayer播放缩略图
        setVideoThumbNail();
    }

    // 初始化mediaPlayer播放器
    public void initalMediaPlayer(){
        // MediaPlayer
        mediaPlayer = MediaPlayer.create(this,R.raw.movie);
        surfaceView = findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        // surfaceHold准备好以后再设置mediaplay显示在SurfaceHolder上,否侧会出现异常
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                // surfaceHold准备好以后再设置mediaplay显示在SurfaceHolder上,否侧会出现异常
                mediaPlayer.setDisplay(holder);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"可以播放了",Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });

        // 播放完成处理
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // 标记到开始位置
                lastPlayPosition = 0;

                thumbnailImageView.setImageBitmap(getVideoThumbNail(lastPlayPosition));
                btn_play.setImageResource(R.drawable.play);

                MEDIAPLAYER_STATE = MEDIAPLAYER_IS_STOP;
            }
        });
    }

    // 设置视频缩略图
    public void setVideoThumbNail(){
        // 获取手机屏幕尺寸
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        // 获取缩略图
        thumbnailImageView = findViewById(R.id.iv_thumbnail);
        Bitmap bitmap = getVideoThumbNail(lastPlayPosition);
        float scale = width/bitmap.getWidth();
        int imageHeigth = (int)(scale * bitmap.getHeight());
        ViewGroup.LayoutParams params = thumbnailImageView.getLayoutParams();
        params.height = imageHeigth;
        params.width = width;
        thumbnailImageView.setLayoutParams(params);
        thumbnailImageView.setImageBitmap(bitmap);

        // 设置播放按钮
        btn_play = findViewById(R.id.play);
        ViewGroup.LayoutParams playParam = btn_play.getLayoutParams();
        playParam.width = width;
        playParam.height = imageHeigth;
        btn_play.setLayoutParams(playParam);
        btn_play.setImageResource(R.drawable.play);
        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 播放或暂停
                if (MEDIAPLAYER_STATE == MEDIAPLAYER_IS_STOP){
                    // 播放
                    mediaPlay();
                }else if (MEDIAPLAYER_STATE == MEDIAPLAYER_IS_PLAYING){
                    // 暂停
                    mediaPause();
                }else if (MEDIAPLAYER_STATE == MEDIAPLAYER_IS_PAUSE){
                    // 继续播放
                    mediaResume();
                }
            }
        });

        // 设置surfaceView的大小
        ViewGroup.LayoutParams surParam = surfaceView.getLayoutParams();
        surParam.width = width;
        surParam.height = imageHeigth;
        surfaceView.setLayoutParams(surParam);
    }

    // mediaplay播放视频
    public void mediaPlay(){
        if (lastPlayPosition > 0){
            mediaPlayer.seekTo(lastPlayPosition);
        }

        mediaPlayer.start();

        MEDIAPLAYER_STATE = MEDIAPLAYER_IS_PLAYING;

        // 设置按钮的图片
        btn_play.setImageBitmap(null);
        thumbnailImageView.setImageBitmap(null);
    }


    // mediaplay暂停播放
    public void mediaPause(){
        if (MEDIAPLAYER_STATE == MEDIAPLAYER_IS_PLAYING){
            mediaPlayer.pause();
            MEDIAPLAYER_STATE = MEDIAPLAYER_IS_PAUSE;

            // 设置按钮图片
            btn_play.setImageResource(R.drawable.play);
        }
    }

    // mediaplay继续播放
    public void mediaResume(){
        mediaPlayer.start();
        MEDIAPLAYER_STATE = MEDIAPLAYER_IS_PLAYING;

        btn_play.setImageBitmap(null);
    }

    // mediaplay停止播放
    public void mediaStop(){
        // 停止播放
        mediaPlayer.stop();

        MEDIAPLAYER_STATE = MEDIAPLAYER_IS_STOP;
    }

    @Override
    protected void onPause() {
        super.onPause();

        // 暂停播放
        mediaPause();

        // 获取停止播放时的播放位置
        if (MEDIAPLAYER_STATE != MEDIAPLAYER_IS_STOP){
            lastPlayPosition = mediaPlayer.getCurrentPosition();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 停止播放
        mediaStop();
        mediaPlayer.release();

        // 存储播放位置
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
