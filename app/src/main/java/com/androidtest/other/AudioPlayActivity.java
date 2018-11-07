package com.androidtest.other;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import helloworld.android.com.androidtest.R;

public class AudioPlayActivity extends Activity {

    private MediaPlayer mediaPlayer;
    private FileOutputStream fileOutputStream;
    private MediaPlayer mediaPlayer1;
    private MediaPlayer mediaPlayer2;
    private SoundPool soundPool1;
    private SoundPool soundPool2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audioplay);

    }

    // 用SoundPool播放单个音频(播放较小的文件,可以播放多个文件)
    public void playWithSoundPool(View view){
        soundPool1 = new SoundPool(1, AudioManager.STREAM_MUSIC,0);
        final int soundId = soundPool1.load(this,R.raw.note1,1);

        if (soundId == 0){
            // 加载失败
            Toast.makeText(this,"加载音频文件失败",Toast.LENGTH_SHORT).show();
        }else {
            // 加载成功

            // 音量
            AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
            float audioMaxVolumn = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            float volumnCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);
            final float volumnRatio = volumnCurrent / audioMaxVolumn;

            soundPool1.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                    soundPool.play(soundId,volumnRatio,volumnRatio,1,0,1);
                }
            });
        }
    }

    // 用SoundPool播放多个音频(播放较小的文件,可以播放多个文件)
    public void playWithSoundPool2(View view){
        soundPool2 = new SoundPool(5, AudioManager.STREAM_MUSIC,0);

        soundPool2.load(this,R.raw.note1,1);
        soundPool2.load(this,R.raw.note2,1);
        soundPool2.load(this,R.raw.note3,1);
        soundPool2.load(this,R.raw.note4,1);
        soundPool2.load(this,R.raw.note5,1);

        soundPool2.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if (status != 0) return;

                // 音量
                AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
                float audioMaxVolumn = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                float volumnCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                float volumnRatio = volumnCurrent / audioMaxVolumn;

                soundPool.play(sampleId,volumnRatio,volumnRatio,1,0,1);
            }
        });

    }

    // 用MediaPlayer从资源文件播放(播放较大的文件,只能播放单个文件)
    public void playWithMediaPlayer1(View view){

        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.song);

            // 从资源文件播放不应该调用prepare方法，否则会抛出异常
//            try {
//                mediaPlayer.prepare();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }

        mediaPlayer.start();
    }

    // 用MediaPlayer从文件系统中播放(播放较大的文件,只能播放单个文件)
    public void playWithMediaPlayer2(View view){
        String path = "/sdcard/Music/song.mp3";
        File file = new File(path);

        // 如果文件不存在，拷贝
        if (!file.exists()){
            InputStream inputStream = getResources().openRawResource(R.raw.song);
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                byte[] bytes = new byte[inputStream.available()];

                int length = 0;

                while ((length = inputStream.read(bytes)) != -1) {
                    fileOutputStream.write(bytes,0,length);
                }

                fileOutputStream.close();
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mediaPlayer1 = new MediaPlayer();
        try {
            mediaPlayer1.setDataSource(path);
            mediaPlayer1.prepare();
            mediaPlayer1.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // 用MediaPlayer播放网络音乐(播放较大的文件,只能播放单个文件)
    // 首先加入入网权限
    public void playWithMediaPlayer3(View view){
        String path = "http://sc1.111ttt.cn:8282/2018/1/03m/13/396131229550.m4a?tflag=1519095601&pin=6cd414115fdb9a950d827487b16b5f97#.mp3";

        // uri方式
//        mediaPlayer2 = MediaPlayer.create(this,Uri.parse(path));
//        mediaPlayer2.start();


        // 设置数据源方式
        mediaPlayer2 = new MediaPlayer();
        try {
            mediaPlayer2.setDataSource(path);
            mediaPlayer2.prepareAsync();
            mediaPlayer2.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        if (mediaPlayer1 != null){
            mediaPlayer1.stop();
            mediaPlayer1.release();
        }

        if (mediaPlayer2 != null) {
            mediaPlayer2.stop();
            mediaPlayer2 = null;
        }
    }
}
