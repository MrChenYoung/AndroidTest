package com.androidTest.other;

import android.Manifest;
import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.format.Formatter;
import android.util.Log;
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
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class RecordActivity extends PermissionBaseActivity {

    private final int READ_FILE_HISTORY_RECORD = 200;
    private final int READ_FILE_CLEAR_FILE = 300;

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
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
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
        if (mediaRecorder == null){
            mediaRecorder = new MediaRecorder();
            // 设置参数(顺序不可变,否则报异常)
            /**
             * VOICE_CALL 通话中，对方、自己声音都会录下来
             * VOICE_DOWNLINK 只录取扬声器、听筒声音
             * VOICE_RECOGNITION 跟MIC一样，只录取麦克风声音，但扬声器太大声的话也会录到
             */
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_WB);
            /**
             * 设置录制的声音的输出格式（必须在设置声音编码格式之前设置）
             * THREE_GPP  3gp格式，H263视频/ARM音频编码
             * MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
             */
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_WB);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);

//            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        }
    }

    // 开始录音
    public void startRecord(View view){
        // 检查录音权限
        if (PermissonUtil.hasPermissions(this,this,PermissonUtil.RECORD,"需要录音权限",new String[]{Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE})){
            // 有录音权限
            startRecord();
        }
    }

    @AfterPermissionGranted(PermissonUtil.RECORD)
    private void startRecord(){
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
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this,"开启录音器失败",Toast.LENGTH_SHORT).show();
        }
    }

    // 停止录音
    public void stopRecord(View view){
        if (mediaRecorder != null){
            try {
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;
                isRecording = false;
                cover.setVisibility(View.GONE);
                reloadViews();
                getHistoryRecords();
            }catch (Exception e){
                mediaRecorder.reset();
                mediaRecorder.release();
                mediaRecorder = null;
                Toast.makeText(this,"停止录音失败",Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 播放录音
    public void playRecord(int position){
        // 调用系统播放器播放
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_VIEW);
//        intent.setDataAndType(Uri.parse(desPath.getAbsolutePath()),"video/*");
//        startActivity(intent);

        try {
            mediaPlayer.reset();

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
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this,"录音播放失败",Toast.LENGTH_SHORT).show();
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
    @AfterPermissionGranted(READ_FILE_HISTORY_RECORD)
    private void getHistoryRecords(){
        if (PermissonUtil.hasPermissions(this,this,READ_FILE_HISTORY_RECORD,"需要读取sd卡内容权限",new String[]{Manifest.permission.READ_EXTERNAL_STORAGE})){
            try {
                recordList.clear();
                recordFiles.clear();
                File[] records = recordDir.listFiles();
                for (File record : records){
                    if (record.isDirectory() || record.getName().startsWith(".")) continue;

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
                    String duration = getAmrDuration(record) + "s";
                    recordBean.setRecordDuration(duration);
                    recordList.add(recordBean);
                    recordFiles.add(record);
                }

                // 刷新列表
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this,"读取文件失败",Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * 得到amr的时长
     *
     * @param file
     * @return amr文件时间长度
     * @throws IOException
     */
    public int getAmrDuration(File file) throws IOException {
        long duration = -1;
        int[] packedSize = { 12, 13, 15, 17, 19, 20, 26, 31, 5, 0, 0, 0, 0, 0,
                0, 0 };
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(file, "rw");
            long length = file.length();// 文件的长度
            int pos = 6;// 设置初始位置
            int frameCount = 0;// 初始帧数
            int packedPos = -1;

            byte[] datas = new byte[1];// 初始数据值
            while (pos <= length) {
                randomAccessFile.seek(pos);
                if (randomAccessFile.read(datas, 0, 1) != 1) {
                    duration = length > 0 ? ((length - 6) / 650) : 0;
                    break;
                }
                packedPos = (datas[0] >> 3) & 0x0F;
                pos += packedSize[packedPos] + 1;
                frameCount++;
            }

            duration += frameCount * 20;// 帧数*20
        } finally {
            if (randomAccessFile != null) {
                randomAccessFile.close();
            }
        }
        return (int)((duration/1000)+1);
    }

    // 清除无效的录音文件
    public void clearInvalidFile(View view){
        if (PermissonUtil.hasPermissions(this,this,READ_FILE_CLEAR_FILE,"需要读取sd卡内容权限",new String[]{Manifest.permission.READ_EXTERNAL_STORAGE})){
            // 有权限
            clearInvalidFile();
        }
    }

    @AfterPermissionGranted(READ_FILE_CLEAR_FILE)
    private void clearInvalidFile(){
        if (recordDir == null || !recordDir.exists()) return;

        File[] files = recordDir.listFiles();
        if (files == null) return;
        for(File record : files){
            try {
                MediaPlayer mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(record.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
                record.delete();
            }
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

    // 界面销毁停止录音，防止内存泄漏

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopRecord(null);
    }
}
