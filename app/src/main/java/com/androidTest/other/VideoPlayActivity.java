package com.androidTest.other;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.VideoView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import com.androidTest.R;

public class VideoPlayActivity extends Activity {

    private VideoView videoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoplay);

        // 设置调用系统播放器
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

    // 调用系统自带的播放器播放本地视频
    public void playWithSystemPlayer(View view){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);


        // 播放本地具体路径下的视频记得使用Uri,fromFile，不要使用Uri.parse
//        Uri uri = null;
//        try{
//            int videoId = R.raw.class.getDeclaredField("movie").getInt(this);
//            uri = Uri.parse("android.resource://" + this.getPackageName() + "/" + videoId);
//        }catch (Exception e){
//            e.printStackTrace();
//        }

        // 播放sd卡下的视频设置Uri
        String path = "/mnt/sdcard/movie.mp4";
        File file = new File(path);
        if (!file.exists()){
            try{
                InputStream inputStream = getResources().openRawResource(R.raw.movie);
                FileOutputStream outputStream = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int length = -1;
                while ((length = inputStream.read(buffer)) != -1){
                    outputStream.write(buffer,0,length);
                }

                inputStream.close();
                outputStream.close();

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        Uri uri = Uri.fromFile(file);

        intent.setDataAndType(uri,"video/*");
        startActivity(intent);
    }

    // 调用系统播放器播放网络视频
    public void playNetVideo(View view){
        String url = "http://ips.ifeng.com/video19.ifeng.com/video09/2018/02/05/20148493-102-009-133227.mp4?vid=be48c1ae-724e-4c19-a9fa-59ad8246b3ea&uid=1514278329160_gemzl08766&from=v_Free&pver=vHTML5Player_v2.0.0&sver=&se=%E6%9D%A5%E6%90%9E%E7%AC%91&cat=66-68&ptype=66&platform=pc&sourceType=h5&dt=1517808604000&gid=-Eo8MWbyPgyM&sign=2e93fec1786a386e3f02b86a4b35eb6c&tm=1541662444684";
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri,mimeType);
        startActivity(intent);
    }

}
