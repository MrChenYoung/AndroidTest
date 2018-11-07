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
import android.os.StrictMode;
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
import java.io.FileInputStream;
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

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
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

    // 调用系统自带的播放器播放
    public void playWithSystemPlayer(View view){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);


        // 播放本地具体路径下的视频记得使用Uri,fromFile，不要使用Uri.parse
        Uri uri = null;
        try{
            int videoId = R.raw.class.getDeclaredField("movie").getInt(this);
            uri = Uri.parse("android.resource://" + this.getPackageName() + "/" + videoId);
        }catch (Exception e){
            e.printStackTrace();
        }

        // 播放sd卡下的视频设置Uri
        //        String path = "/mnt/sdcard/movie.mp4";
//        File file = new File(path);
//        if (!file.exists()){
//            try{
//                InputStream inputStream = getResources().openRawResource(R.raw.movie);
//                FileOutputStream outputStream = new FileOutputStream(file);
//                byte[] buffer = new byte[1024];
//                int length = -1;
//                while ((length = inputStream.read(buffer)) != -1){
//                    outputStream.write(buffer,0,length);
//                }
//
//                inputStream.close();
//                outputStream.close();
//
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//        Uri uri = Uri.fromFile(file);

        intent.setDataAndType(uri,"video/*");
        startActivity(intent);
    }
}
