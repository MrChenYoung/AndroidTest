package com.androidTest.other;

import android.Manifest;
import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidTest.R;
import com.androidTest.main.PermissionBaseActivity;
import com.utiles.PermissonUtil;

import java.io.File;
import java.io.IOException;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class RecordActivity extends PermissionBaseActivity {

    // 录音存放目录
    private File recordDir;
    private MediaRecorder mediaRecorder;

    // 蒙版
    private RelativeLayout cover;
    private TextView tv_loading;

    // 历史录音列表
    private ListView listView;

    // 录音控制按钮
    private Button bt_startRecord;
    private Button bt_stopRecord;

    // 录音状态 是否正在录音
    private boolean isRecording = false;
    // 是否正在播放录音
    private boolean isPlayingRecord = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        // 获取指定控件
        cover = findViewById(R.id.cover);
        tv_loading = findViewById(R.id.tv_loading);
        bt_startRecord = findViewById(R.id.bt_startRecord);
        bt_stopRecord = findViewById(R.id.bt_stopRecord);
        listView = findViewById(R.id.listView);

        // 设置录音存储位置
        setRecordSavePath();
    }

    // 设置录音存储位置
    private void setRecordSavePath(){
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED){
            // 如果有sd卡 存储到sd卡
            recordDir = new File(Environment.getExternalStorageDirectory(),"sounds");
        }else {
            // 如果没有sd卡 存储到手机内部存储
            recordDir = new File(getFilesDir(),"sounds");
        }

        // 如果文件夹不存在，创建
        if (!recordDir.exists()){
            recordDir.mkdirs();
        }
    }

    // 初始化录音器
    private void initialRecorder(){
        // 检查录音权限
        if (PermissonUtil.hasPermissions(this,this,PermissonUtil.RECORD,"需要录音权限",new String[]{Manifest.permission.RECORD_AUDIO})){
            // 有录音权限
            setRecorder();
        }else {
            // 没有录音权限

        }
    }

    // 设置录音器
    @AfterPermissionGranted(PermissonUtil.RECORD)
    private void setRecorder(){
        if (mediaRecorder == null){
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_WB);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);
        }
    }

    // 开始录音
    public void startRecord(View view){
        // 初始化录音器
        initialRecorder();

        // 设置录音路径
        File desPath = new File(recordDir,System.currentTimeMillis() + ".amr");
        mediaRecorder.setOutputFile(desPath.getAbsolutePath());

        // 开始录音
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            isRecording = true;
            tv_loading.setText("录音中...");
            cover.setVisibility(View.VISIBLE);
            reloadViews();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this,"开启录音器失败",Toast.LENGTH_SHORT).show();
        }
    }

    // 停止录音
    public void stopRecord(View view){
        if (mediaRecorder != null){
            try {
                mediaRecorder.stop();
                isRecording = false;
                cover.setVisibility(View.GONE);
                reloadViews();
            }catch (Exception e){
                Toast.makeText(this,"停止录音失败",Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 播放录音
    public void playRecord(View view){
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_VIEW);
//        intent.setDataAndType(Uri.parse(desPath.getAbsolutePath()),"video/*");
//        startActivity(intent);

//        try {
//            MediaPlayer mediaPlayer = new MediaPlayer();
//            mediaPlayer.setDataSource(desPath.getAbsolutePath());
//            mediaPlayer.prepare();
//            mediaPlayer.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    // 刷新界面
    private void reloadViews(){
        if (isPlayingRecord){
            bt_startRecord.setEnabled(false);
            bt_stopRecord.setEnabled(false);
        }

        if (isRecording){
            bt_startRecord.setEnabled(false);
            bt_stopRecord.setEnabled(true);
        }

        if (!isRecording && !isPlayingRecord){
            bt_startRecord.setEnabled(true);
            bt_stopRecord.setEnabled(true);
        }
    }
}
