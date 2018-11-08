package com.androidTest.other;

import android.app.Activity;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.View;

import java.io.File;
import java.io.IOException;

import com.androidTest.R;

public class RecordActivity extends Activity {

    // 录音存放目录
    private File recordDir;
    private MediaRecorder mediaRecorder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        recordDir = new File(Environment.getExternalStorageDirectory(),"sounds");
        if (!recordDir.exists()){
            recordDir.mkdirs();
        }
    }

    // 开始录音
    public void startRecord(View view){

        File desPath = new File(recordDir,System.currentTimeMillis() + ".amr");
        if (!desPath.exists()){
            try {
                desPath.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        if (mediaRecorder == null){
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setOutputFile(desPath.getAbsolutePath());
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_WB);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);

//            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_WB);
//            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);
//            mediaRecorder.setOutputFile(desPath.getAbsolutePath());

            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    // 停止录音
    public void stopRecord(View view){
        if (mediaRecorder != null){
            try {
                mediaRecorder.stop();
            }catch (Exception e){
                mediaRecorder = null;
                mediaRecorder = new MediaRecorder();
            }

            mediaRecorder.release();
            mediaRecorder = null;
        }
    }
}
