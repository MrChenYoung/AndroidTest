package com.androidtest.Demos.SevicesDemos;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import helloworld.android.com.androidtest.R;

public class PhoneRecordActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private Intent intent;

    // 录音文件存储的路径(文件夹)
    public static File baseOutputPath;
    // adapter
    public PhoneRecordAdapter adapter;
    // 数据
    public ArrayList<PhoneRecordFile> recordList = new ArrayList<>();
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_record);
        // 初始化录音存储路径
        initialSavePath();

        // 开启电话监听服务
        intent = new Intent(this,PhoneRecordServices.class);
        startService(intent);

        Toast toast = Toast.makeText(this,"正在监听电话...",Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();

        // 设置listView
        ListView listView = findViewById(R.id.listView);
        adapter = new PhoneRecordAdapter();
        listView.setAdapter(adapter);

        // 刷新listView
        refreshListView();
    }

    // 设置录音文件存储路径
    public void initialSavePath(){
        baseOutputPath = new File(getFilesDir(),"VoiceRecord");
        if (!baseOutputPath.exists()){
            baseOutputPath.mkdirs();
        }
    }

    // 刷新列表
    public void refreshListView(){
        File[] files = baseOutputPath.listFiles();
        for(int i = 0; i < files.length; i++){
            File file = files[i];
            if (file.isFile()){
                // 获取文件名
                String fileName = file.getName();
                String fileSize = Formatter.formatFileSize(this,file.length());
                Date lastModifyDate = new Date(file.lastModified());
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateString = dateFormat.format(lastModifyDate);

                PhoneRecordFile recordFile = new PhoneRecordFile();
                recordFile.setFileName(fileName);
                recordFile.setSize(fileSize);
                recordFile.setDate(dateString);
                recordFile.setPath(file);
                recordList.add(recordFile);
            }
        }

        // 刷新列表
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 关闭服务
        stopService(intent);
    }

    // 播放录音
    public void playRecord(File path){
        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }else {
            //初始化播放器
            mediaPlayer = new MediaPlayer();
        }

        try {
            //设置播放音频数据文件
            mediaPlayer.setDataSource(path.getAbsolutePath());
            //播放器音量配置
            mediaPlayer.setVolume(1, 1);
            //是否循环播放
            mediaPlayer.setLooping(false);
            // 准备播放
            mediaPlayer.prepare();
            // 开始播放
            mediaPlayer.start();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override // 点击每一行播放
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PhoneRecordFile file = recordList.get(position);
        playRecord(file.getPath());
    }

    // listView adapter
    private class PhoneRecordAdapter extends BaseAdapter {
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
            View view;
            if (convertView == null){
                view = getLayoutInflater().inflate(R.layout.phone_recode_item,null);
            }else {
                view = convertView;
            }

            TextView tv_name = view.findViewById(R.id.tv_name);
            TextView tv_date = view.findViewById(R.id.tv_date);
            TextView tv_size = view.findViewById(R.id.tv_size);

            // 设置每条录音的名字 时间和大小
            PhoneRecordFile file = recordList.get(position);
            tv_name.setText(file.getFileName());
            tv_date.setText(file.getDate());
            tv_size.setText(file.getSize());
            return view;
        }
    }
}
