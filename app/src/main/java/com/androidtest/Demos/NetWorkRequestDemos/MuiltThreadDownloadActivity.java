package com.androidtest.Demos.NetWorkRequestDemos;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import helloworld.android.com.androidtest.R;

public class MuiltThreadDownloadActivity extends AppCompatActivity {

    // 默认开启3个线程下载
    private int threadCount = 3;

    // 手机QQ下载地址
//    private String urlString = "http://sqdd.myapp.com/myapp/qqteam/tim/down/tim.apk";
//    private String urlString = "https://dldir1.qq.com/qqfile/QQforMac/QQ_V6.5.1.dmg";
    private String urlString = "http://acj3.pc6.com/pc6_soure/2018-10/com.taobao.taobao_214.apk";

    // 加载loadingCover
    private View cover;

    // 存储的路径
    private File savePath = null;
    // 文件的总大小
    private long totalLength = 0;
    // 已经下载的大小
    private long downloadedLength = 0;
    // 总下载进度条
    private ProgressBar totalProgress;
    // 总下载进度提示文字框
    private TextView totalDownloadTextView;

    // 请求状态标识
    private final int DOWNLOADSTART = -2;   // 开始下载
    private final int DOWNLOADCOMPLETE = 1; // 下载完成
    private final int DOWNLOADING = 2;      // 正在下载
    private final int REQUESTFAILE = 0;     // 请求失败
    private final int REQUESTEXCEPTION = -1;// 请求出现异常

    // 主线程更新界面处理
    private MyHandle handle = new MyHandle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muilt_thread_download);

        // 下载的文件存储路径
        savePath = new File(getFilesDir(),"QQ.apk");
        // 删除已经存在的安装包
        if (savePath.exists()){
            installApk(savePath.getPath());
//            boolean deleteSuccess = savePath.delete();
//            if (deleteSuccess){
//                Toast.makeText(this,"删除老文件成功",Toast.LENGTH_LONG).show();
//            }else {
//                Toast.makeText(this,"删除老文件失败",Toast.LENGTH_LONG).show();
//            }
        }

        // 获取线程数
        EditText ed_threadCount = findViewById(R.id.ed_threadCount);
        String str = ed_threadCount.getText().toString();
        if (!TextUtils.isEmpty(str)){
            threadCount = Integer.parseInt(str);
        }

        cover = findViewById(R.id.cover);
    }

    // 添加进度条
    public void addProgressBar(boolean totalPregressBar,String threadName){
        View progressView = getLayoutInflater().inflate(R.layout.activity_muilt_thread_progress,null);
        TextView textView = progressView.findViewById(R.id.threadName);
        textView.setText(totalPregressBar ? "总进度" : threadName);
        ProgressBar progressBar = progressView.findViewById(R.id.progressbar);
        if (totalPregressBar){
            totalProgress = progressBar;
            totalDownloadTextView = textView;
        }

        LinearLayout mainView = findViewById(R.id.main_view);
        mainView.addView(progressView);
    }

    // 开启多线程下载
    public void startDownload(View view){
        cover.setVisibility(View.VISIBLE);

        new Thread(){
            @Override
            public void run() {
                try{
                    URL url = new URL(urlString);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    int code = connection.getResponseCode();

                    if (code == 200){
                        // 开始多线程下载
                        Message msg = Message.obtain();
                        msg.what = DOWNLOADSTART;
                        handle.sendMessage(msg);

                        // 获取文件总大小
                        totalLength = connection.getContentLength();
                        Log.v("tag","总大小:" + totalLength);

                        // 计算每个线程应该下载的文件大小
                        long everyLength = totalLength / threadCount;

                        // 开启多个线程下载
//                        for (int i = 0; i < threadCount; i++) {
//                            long startIndex = i * everyLength;
//                            long endIndex = (i + 1) * everyLength - 1;
//
//                            if (i == threadCount - 1){
//                                endIndex = totalLength - 1;
//                            }
//
//                            Log.v("tag","线程id：" + i + "开始位置:" + startIndex + "结束位置:" + endIndex);
//                            downloadApk(startIndex,endIndex,i);
//                        }

                        downloadApk(0,0,0);
                    }else {
                        // 获取文件总大小失败
                        Message msg = Message.obtain();
                        msg.what = REQUESTFAILE;
                        msg.obj = "获取文件总大小失败,错误码:" + code;
                        handle.sendMessage(msg);
                    }

                }catch (Exception e){
                    e.printStackTrace();

                    // 出现异常
                    Message msg = Message.obtain();
                    msg.what = REQUESTEXCEPTION;
                    msg.obj = e.toString();
                    handle.sendMessage(msg);
                }
            }
        }.start();
    }

    // 安装apk
    private void installApk(String filePath) {
        File apkFile = new File(filePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Log.w("tag", "版本大于 N ，开始使用 fileProvider 进行安装");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(
                    this
                    , "你的包名.fileprovider"
                    , apkFile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            Log.w("tag", "正常进行安装");
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        startActivity(intent);
    }

    // 开启子线程下载apk
//    public void downloadApk(final long startIndex, final long endIndex, final int threadId){
//        new Thread(){
//            @Override
//            public void run() {
//                try {
//                    URL url = new URL(urlString);
//                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                    connection.setRequestMethod("GET");
////                    connection.setConnectTimeout(5000);
//                    // 设置下载区间
//                    connection.setRequestProperty("Range","bytes=" + startIndex + "-" + endIndex);
//                    int code = connection.getResponseCode();
//                    if (code == 206){
//                        // 部分资源请求成功,写数据到硬盘
//                        RandomAccessFile file = new RandomAccessFile(savePath,"rw");
//                        file.seek(startIndex);
//
//                        InputStream inputStream = connection.getInputStream();
//                        byte[] bytes = new byte[1024 * 1024];
//                        int length = -1;
//                        while ((length = inputStream.read(bytes)) != -1){
//                            file.write(bytes,0,length);
//
//                            // 记录当前已经下载的大小
//                            downloadedLength += length;
//                            // 检测是否下载完成
//                            Message msg = Message.obtain();
//                            if (downloadedLength == totalLength){
//                                // 下载完成
//                                msg.what = DOWNLOADCOMPLETE;
//                            }else {
//                                // 下载中
//                                msg.what = DOWNLOADING;
//                            }
//
//                            handle.sendMessage(msg);
//                        }
//
//                    }else {
//                        // 请求出错
//                        Message msg = Message.obtain();
//                        msg.what = REQUESTFAILE;
//                        msg.obj = "请求错误,错误码:" + code;
//                        handle.sendMessage(msg);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//
//                    // 出现异常
//                    Message msg = Message.obtain();
//                    msg.what = REQUESTEXCEPTION;
//                    msg.obj = e.toString();
//                    handle.sendMessage(msg);
//                }
//            }
//        }.start();
//    }

    public void downloadApk(final long startIndex, final long endIndex, final int threadId){
        new Thread(){
            @Override
            public void run() {
                HttpURLConnection connection = null;
                FileOutputStream outputStream = null;
                InputStream inputStream = null;

                try {
                    URL url = new URL(urlString);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    // 设置下载区间
                    int code = connection.getResponseCode();
                    if (code == 200){
                        outputStream = new FileOutputStream(savePath);
                        inputStream = connection.getInputStream();

                        byte[] bytes = new byte[1024 * 1024];
                        int length = -1;
                        while ((length = inputStream.read(bytes)) != -1) {
                            outputStream.write(bytes, 0, length);

                            // 记录当前已经下载的大小
                            downloadedLength += length;
                            // 检测是否下载完成
                            Message msg = Message.obtain();
                            if (downloadedLength == totalLength) {
                                // 下载完成
                                msg.what = DOWNLOADCOMPLETE;
                            } else {
                                // 下载中
                                msg.what = DOWNLOADING;
                            }

                            handle.sendMessage(msg);
                        }

                    }else {
                        // 请求出错
                        Message msg = Message.obtain();
                        msg.what = REQUESTFAILE;
                        msg.obj = "请求错误,错误码:" + code;
                        handle.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    // 出现异常
                    Message msg = Message.obtain();
                    msg.what = REQUESTEXCEPTION;
                    msg.obj = e.toString();
                    handle.sendMessage(msg);
                }finally {
                    try {
                        if (inputStream != null){
                            inputStream.close();
                        }

                        if (outputStream != null){
                            outputStream.close();
                        }

                        if (connection != null){
                            connection.disconnect();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        // 请求出错
                        Message msg = Message.obtain();
                        msg.what = REQUESTFAILE;
                        msg.obj = "请求异常";
                        handle.sendMessage(msg);
                    }
                }
            }
        }.start();
    }

    private class MyHandle extends Handler{
        @Override
        public void handleMessage(Message msg) {
            cover.setVisibility(View.GONE);

            String message = "";
            String obj = (String) msg.obj;
            switch (msg.what){
                case REQUESTEXCEPTION:
                    message = "请求出现异常:" + obj;
                    break;
                case DOWNLOADSTART:
                    message = "开始下载";
                    if (totalProgress == null){
                        addProgressBar(true,null);
                    }
                    break;
                case DOWNLOADCOMPLETE:
                    message = "下载完成";
                    break;
                case DOWNLOADING:
//                    message = "下载中,进度:" + downloadedLength;
                    float pro = (float) downloadedLength/totalLength;
                    int totalPro = (int)(pro * 100);
                    totalProgress.setProgress(totalPro);
                    totalDownloadTextView.setText("总进度:" + totalPro + "%");
                    break;
                case REQUESTFAILE:
                    message = obj;
                    break;
            }

            if (msg.what != DOWNLOADING) {
                Toast.makeText(MuiltThreadDownloadActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
