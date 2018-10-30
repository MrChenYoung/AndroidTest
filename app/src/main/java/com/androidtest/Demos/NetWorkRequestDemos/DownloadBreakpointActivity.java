package com.androidtest.Demos.NetWorkRequestDemos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import helloworld.android.com.androidtest.R;

public class DownloadBreakpointActivity extends Activity {

    // 开始下载按钮
    private Button downloadBtn;
    private TextView downloadProgressTextView;
    private ProgressBar downloadProgressBar;
    private TextView timeView;
    private View cover;
    private File destinationPath;
    private long startPoint;
    private long downloadPoint;
    private long totalLength;
    private long startTime;
    private long pauseTotalTime; // 暂停总时长
    private long startPauseTime; // 开始暂停时间

    // 请求标识
    private final int GETTOTALLENGTHSUCCESS = 1; // 获取文件总大小成功
    private final int REQUESTFAILE = 0;          // 请求失败
    private final int REQUESTEXCEPTION = -1;     // 请求出现异常
    private final int DOWNLOADING = 2;           // 正在下载中
    private final int DOWNLOADCOMPLETE = 3;      // 下载完成


    private DownloadHandle handle = new DownloadHandle();
    private DownloadThread thread = new DownloadThread();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_download_breakpoint);

            // 获取控件
            downloadBtn = findViewById(R.id.download_btn);
            // 获取完成文件的总大小以后才能点击下载
            downloadBtn.setEnabled(false);
            View progressMainView = findViewById(R.id.progress);
            downloadProgressTextView = progressMainView.findViewById(R.id.threadName);
            downloadProgressBar = progressMainView.findViewById(R.id.progressbar);
            cover = findViewById(R.id.cover);
            timeView = findViewById(R.id.timeView);

            // 和多线程下载一个存储位置
            destinationPath = MuiltThreadDownloadActivity.getDestPath(this);
            getStartPoint();
            getTotalLength();
    }

    // 开始下载
    public void download(View view){
        String text = downloadBtn.getText().toString();

        if (text.equals("开始下载") || text.equals("从断点处继续下载")){
            // 更新开始位置
            getStartPoint();

            // 修改按钮状态
            downloadBtn.setText("暂停下载");

            // 记录开始时间
            startTime = System.currentTimeMillis();

            // 开启线程下载
            thread.start();
        }else if (text.equals("暂停下载")){
            startPauseTime = System.currentTimeMillis();

            // 暂停下载
            if (thread != null){
                thread.pauseThread();
                downloadBtn.setText("继续下载");
            }
        }else if (text.equals("继续下载")){
            // 计算暂停的时间
            long pauseTime = System.currentTimeMillis() - startPauseTime;
            pauseTotalTime += pauseTime;

            // 继续下载
            if (thread != null){
                thread.resumeThread();
                downloadBtn.setText("暂停下载");
            }
        }
    }

    // 根据上次下载的位置,提示用户继续下载
    public void initialProgressView(){
        String downloadFormater = Formatter.formatFileSize(this,startPoint);
        String totalFormater = Formatter.formatFileSize(this,totalLength);

        StringBuilder message = new StringBuilder();
        message.append("总共:" + totalFormater);
        int progress = 0;

        if (startPoint > 0){
            // 下载过
            message.append(" 上次下载到" + downloadFormater + "处," + "下载了:" + getDownloadPercent() + "%");
        }else {
            // 没有下载过
            message.append(" 还没有下载过");
        }

        downloadProgressTextView.setText(message);
        downloadProgressBar.setProgress((int)getDownloadPercent());
    }

    // 更新进度条
    public void updateProgressView(){
        float downloadProgress = getDownloadPercent();
        String message = "总共:" + Formatter.formatFileSize(this,totalLength) + " 已下载:" + Formatter.formatFileSize(this,downloadPoint) + " 下载了:" + downloadProgress + "%";
        downloadProgressTextView.setText(message);
        downloadProgressBar.setProgress((int)downloadProgress);
    }

    // 更新下载时间
    public void updateTimeView(){
        long currentTime = System.currentTimeMillis();
        long time = currentTime - startTime - pauseTotalTime;

        timeView.setText("总用时:" + MuiltThreadDownloadActivity.formaterTime(time));
    }

    // 获取下载的百分比
    public float getDownloadPercent(){
         float downloadPercent = (float) downloadPoint/totalLength;
         downloadPercent = (float)(downloadPercent * 100.0);

         String per = String.format("%.2f",downloadPercent);
         return Float.parseFloat(per);
    }

    // 获取开始下载的位置
    public void getStartPoint(){
        if (destinationPath.exists() && destinationPath.length() > 0){
            startPoint = destinationPath.length();
        }else {
            startPoint = 0;
        }

        downloadPoint = startPoint;
    }

    // 获取文件总大小
    public void getTotalLength(){
        cover.setVisibility(View.VISIBLE);

        new Thread(){
            @Override
            public void run() {
                HttpURLConnection connection = null;

                Message msg = Message.obtain();
                try {
                    URL url = new URL(MuiltThreadDownloadActivity.downloadUrl);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    int code = connection.getResponseCode();

                    if (code == HttpURLConnection.HTTP_OK){
                        // 获取文件大小
                        totalLength = connection.getContentLength();

                        msg.what = GETTOTALLENGTHSUCCESS;
                        handle.sendMessage(msg);
                    }else {
                        msg.what = REQUESTFAILE;
                        msg.obj = "获取文件大小出错,错误码:" + code;
                        handle.sendMessage(msg);
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                    msg.what = REQUESTEXCEPTION;
                    msg.obj = "获取文件大小出现异常";
                    handle.sendMessage(msg);
                } finally {
                    if (connection != null){
                        connection.disconnect();
                    }
                }
            }
        }.start();
    }

    @Override
    protected void onPause() {
        super.onPause();

        thread.pauseThread();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 销毁线程
        thread.destroyThread();
    }

    private class DownloadHandle extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GETTOTALLENGTHSUCCESS:
                    // 获取文件总大小成功,下载按钮可点击
                    downloadBtn.setEnabled(true);
                    cover.setVisibility(View.GONE);
                    if (destinationPath.exists()){
                        if (destinationPath.length() == totalLength){
                            destinationPath.delete();
                            startPoint = 0;
                            downloadPoint = startPoint;
                        }else {
                            downloadBtn.setText("从断点处继续下载");
                        }
                    }
                    initialProgressView();
                    break;
                case REQUESTFAILE:
                case REQUESTEXCEPTION:
                    String string = (String) msg.obj;
                    Toast.makeText(DownloadBreakpointActivity.this,string,Toast.LENGTH_SHORT).show();
                    break;
                case DOWNLOADING:
                    // 下载中
                    updateProgressView();
                    updateTimeView();
                    break;
                case DOWNLOADCOMPLETE:
                    // 下载完成
                    thread.pauseThread();
                    thread.destroyThread();
                    downloadBtn.setText("开始下载");
                    new AlertDialog.Builder(DownloadBreakpointActivity.this)
                            .setTitle("温馨提示")
                            .setMessage("下载完成，确定安装吗?")
                            .setNegativeButton("取消",null)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    MuiltThreadDownloadActivity.installApk(DownloadBreakpointActivity.this,destinationPath);
                                }
                            })
                            .show();
                    break;
            }
        }
    }

    // 下载线程
    private class DownloadThread extends Thread {
        private final Object lock = new Object();

        private boolean pause = false;

        @Override
        public void run() {
            HttpURLConnection connection = null;
            InputStream inputStream = null;

            try {
                URL url = new URL(MuiltThreadDownloadActivity.downloadUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Range","bytes=" + startPoint + "-" + totalLength);

                int code = connection.getResponseCode();
                if (code == HttpURLConnection.HTTP_PARTIAL){
                    RandomAccessFile raf = new RandomAccessFile(destinationPath,"rw");
                    raf.seek(startPoint);


                    inputStream = connection.getInputStream();
                    byte[] buffer = new byte[1024];
                    int length = -1;
                    while ((length = inputStream.read(buffer)) != -1){
                        raf.write(buffer,0,length);

                        // 更新已经下载的大小
                        downloadPoint += length;

                        Message msg = Message.obtain();
                        msg.what = DOWNLOADING;
                        handle.sendMessage(msg);

                        if (pause){
                            onPause();
                        }
                    }

                    // 下载完成
                    Message msg = Message.obtain();
                    msg.what = DOWNLOADCOMPLETE;
                    handle.sendMessage(msg);
                }else {
                    Message msg = Message.obtain();
                    msg.what = REQUESTFAILE;
                    msg.obj = "下载出现错误,错误码:" + code;
                    handle.sendMessage(msg);
                }
            } catch (Exception e) {
                e.printStackTrace();

                Message msg = Message.obtain();
                msg.what = REQUESTEXCEPTION;
                msg.obj = "下载出现异常";
                handle.sendMessage(msg);
            } finally {
                try {
                    if (inputStream != null){
                        inputStream.close();
                    }

                    if (connection != null){
                        connection.disconnect();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // 销毁线程
        public void destroyThread(){
            interrupt();
        }

        // 重新启动线程
        public void resumeThread(){
            pause = false;
            synchronized (lock){
                lock.notify();
            }
        }

        public void pauseThread(){
            pause = true;
        }

        // 暂停线程
        private void onPause(){
            synchronized (lock){
                try{
                    lock.wait();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
