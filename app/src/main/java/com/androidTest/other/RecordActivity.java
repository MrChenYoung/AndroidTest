package com.androidTest.other;

import android.Manifest;
import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    private ArrayList<RecordBean> recordList;
    private RecordAdapter adapter;
    private ArrayList<File> recordFiles = new ArrayList<>();

    // 录音控制按钮
    private Button bt_startRecord;
    private Button bt_stopRecord;

    // 录音状态 是否正在录音
    private boolean isRecording = false;
    // 是否正在播放录音
    private boolean isPlayingRecord = false;
    private MediaPlayer mediaPlayer;

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
        recordList = new ArrayList<>();
        adapter = new RecordAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                playRecord(position);
            }
        });

        // 设置录音存储位置
        setRecordSavePath();

        // 创建播放器
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // 播放完成
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RecordActivity.this,"播放完成",Toast.LENGTH_SHORT).show();

                        cover.setVisibility(View.GONE);
                        isPlayingRecord = false;
                        reloadViews();
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // 刷新列表
        getHistoryRecords();
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
    public void playRecord(int position){
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_VIEW);
//        intent.setDataAndType(Uri.parse(desPath.getAbsolutePath()),"video/*");
//        startActivity(intent);

        try {
            // 播放路径
            File desPath = recordFiles.get(position);
            mediaPlayer.setDataSource(desPath.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();

            // 开启蒙版
            tv_loading.setText("播放中");
            cover.setVisibility(View.VISIBLE);
            isPlayingRecord = true;
            reloadViews();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 刷新界面
    private void reloadViews(){
        if (isPlayingRecord){
            bt_startRecord.setEnabled(false);
            bt_stopRecord.setEnabled(false);
            listView.setEnabled(false);
        }

        if (isRecording){
            bt_startRecord.setEnabled(false);
            bt_stopRecord.setEnabled(true);
            listView.setEnabled(false);
        }

        if (!isRecording && !isPlayingRecord){
            bt_startRecord.setEnabled(true);
            bt_stopRecord.setEnabled(true);
            listView.setEnabled(true);
        }
    }

    // 获取历史录音
    private void getHistoryRecords(){
        try {
            recordList.clear();
            recordFiles.clear();
            File[] records = recordDir.listFiles();
            for (File record : records){
                RecordBean recordBean = new RecordBean();
                // 录音名字
                String recordName = record.getName();
                recordBean.setRecordName(recordName);

                // 录音日期
                String date = recordName.split("[.]")[0];
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                long d = Long.parseLong(date);
                Date recordDate = new Date(d);
                String dateF = dateFormat.format(recordDate);
                recordBean.setRecordDate(dateF);

                // 录音文件大小
                String recordSizeF = Formatter.formatFileSize(this,record.length());
                recordBean.setRecordSize(recordSizeF);

                // 录音时长
                MediaPlayer mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(record.getAbsolutePath());
                String duration = mediaPlayer.getDuration()/1000/1000 + "s";
                recordBean.setRecordDuration(duration);
                recordList.add(recordBean);
                recordFiles.add(record);
            }

            // 刷新列表
            adapter.notifyDataSetChanged();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 自定义adapter
    private class RecordAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return recordList.size();
        }

        @Override
        public Object getItem(int position) {
            return recordList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            RelativeLayout item;
            if (convertView == null){
                item = (RelativeLayout) getLayoutInflater().inflate(R.layout.activity_record_item,null);
            }else {
                item = (RelativeLayout) convertView;
            }

            // 获取指定控件
            TextView tv_name = item.findViewById(R.id.tv_name);
            TextView tv_date = item.findViewById(R.id.tv_date);
            TextView tv_size = item.findViewById(R.id.tv_size);

            // 显示数据
            RecordBean bean = recordList.get(position);
            tv_name.setText(bean.getRecordName() + "(" + bean.getRecordDuration() + ")");
            tv_date.setText(bean.getRecordDate());
            tv_size.setText(bean.getRecordSize());

            return item;
        }
    }
}
