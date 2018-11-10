package com.androidTest.Demos.NetWorkRequestDemos;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import com.androidTest.R;

public class MuiltThreadDownloadActivity extends AppCompatActivity {

    // 默认开启3个线程下载
    private int threadCount = 3;
    private Button downloadBtn;
    private TextView timeView;
    private LinearLayout progressMainView;

    // 手机QQ下载地址
//    private String downloadUrl = "http://sqdd.myapp.com/myapp/qqteam/tim/down/tim.apk";
//    private String downloadUrl = "https://dldir1.qq.com/qqfile/QQforMac/QQ_V6.5.1.dmg";
//    private String downloadUrl = "http://acj3.pc6.com/pc6_soure/2018-10/com.taobao.taobao_214.apk";
    public static final String downloadUrl = "http://acj3.pc6.com/pc6_soure/2018-10/com.taobao.taobao_214.apk";

    // 加载loadingCover
    private View cover;

    // 存储的路径
    private File savePath = null;
    // 文件的总大小
    private long totalLength = 0;
    // 已经下载的大小
    private long downloadedLength = 0;

    // 开始下载时的时间戳
    private long startTime;
    private long pauseTotalTime;
    private long startPauseTime;

    // 请求状态标识
    public static final int DOWNLOADSTART = -2;   // 开始下载
    public static final int DOWNLOADCOMPLETE = 1; // 下载完成
    public static final int DOWNLOADING = 2;      // 正在下载
    public static final int DOWNLOADPARTCOMPLETE = 3; // 某一个线程下载完成
    public static final int REQUESTFAILE = 0;     // 请求失败
    public static final int REQUESTEXCEPTION = -1;// 请求出现异常

    // 线程集
    private HashMap<String,MyThread> threadMap = new HashMap<>();

    // 进度条集合
    private HashMap<String,View> progressViewsMap = new HashMap<>();

    // 主线程更新界面处理
    private DownloadHandle handle = new DownloadHandle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muilt_thread_download);

        // 获取存储路径
        savePath = MuiltThreadDownloadActivity.getDestPath(this);
        // 删除已经存在的安装包
        if (savePath.exists()){
            boolean deleteSuccess = savePath.delete();
            if (deleteSuccess){
                Toast.makeText(this,"删除老文件成功",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this,"删除老文件失败",Toast.LENGTH_SHORT).show();
            }
        }

        // 获取下载按钮和scrollView
        progressMainView = findViewById(R.id.progressMainView);
        downloadBtn = findViewById(R.id.download_btn);
        timeView = findViewById(R.id.timeView);

        // 蒙版
        cover = findViewById(R.id.cover);
    }

    // 获取下载文件存储路径
    public static File getDestPath(final Context context){
        // 创建download文件夹
        File apkPath = new File(context.getFilesDir(),"download");
        if (!apkPath.exists()){
            apkPath.mkdirs();

            // 设置download文件夹权限
            chmodFile(context,apkPath.getAbsolutePath());
        }


        File path = new File(apkPath,"QQ.apk");

        return path;
    }

    // 开启多线程下载
    public void download(View view){
        // 获取下载线程数
        EditText ed_threadCount = findViewById(R.id.ed_threadCount);
        String str = ed_threadCount.getText().toString();
        if (!TextUtils.isEmpty(str)){
            threadCount = Integer.parseInt(str);
        }

        String text = downloadBtn.getText().toString();
        if (text.equals("暂停")){
            startPauseTime = System.currentTimeMillis();

            downloadBtn.setText("继续下载");
            // 暂停下载
            pauseDownload();
        }else if(text.equals("继续下载")) {
            pauseTotalTime += System.currentTimeMillis() - startPauseTime;

            downloadBtn.setText("暂停");
            // 继续下载
            continueDownload();
        }else {
            if (savePath.exists()){
                new AlertDialog.Builder(this)
                        .setTitle("温馨提示")
                        .setMessage("安装包已经存在")
                        .setNegativeButton("重新下载", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                savePath.delete();
                                getTotalLength();
                            }
                        })
                        .setPositiveButton("安装", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                installApk(MuiltThreadDownloadActivity.this,savePath);
                            }
                        })
                        .show();
            }else {
                // 开始下载/重新下载
                // 获取手机QQ文件大小
                getTotalLength();
            }
        }
    }

    // 获取手机QQ文件总大小
    public void getTotalLength(){
        cover.setVisibility(View.VISIBLE);

        new Thread(){
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try{
                    URL url = new URL(downloadUrl);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    int code = connection.getResponseCode();

                    if (code == HttpURLConnection.HTTP_OK){
                        // 获取文件总大小
                        totalLength = connection.getContentLength();

                        // 开始多线程下载
                        Message msg = Message.obtain();
                        msg.what = DOWNLOADSTART;
                        handle.sendMessage(msg);
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
                    msg.obj = "获取文件大小出现异常";
                    handle.sendMessage(msg);
                }finally {
                    if (connection != null){
                        connection.disconnect();
                    }
                }
            }
        }.start();
    }

    // 安装apk
    public static void installApk(Context context,File apkPath) {
        // 首先修改apk权限为可读写,否则会提示解析包出现问题
        chmodFile(context,apkPath.getAbsolutePath());

        // 安装apk
        Intent installIntent = new Intent(Intent.ACTION_VIEW);
        installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            // 系统版本大于 android 7.0

            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            installIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            Uri contentUri = FileProvider.getUriForFile(
                    context.getApplicationContext()
                    , "com.androidtest.fileprovider"
                    , apkPath);
            installIntent.setDataAndType(contentUri, "application/vnd.android.package-archive");


        }else {
            // 小于7.0 版本
            installIntent.setDataAndType(Uri.parse("file://" + apkPath.getAbsolutePath()),
                    "application/vnd.android.package-archive");
        }

        context.startActivity(installIntent);
    }

    // 开启下载手机QQ
    public void startDownload(){
        // 获取当前时间戳
        startTime = System.currentTimeMillis();

        // 计算每个线程应该下载的文件大小
        long everyLength = totalLength / threadCount;

        // 删除多余的缓存进度条
        if (progressViewsMap.size() - 1 > threadCount){
            int lastCount = progressViewsMap.size();
            for (int i = threadCount + 1; i < lastCount; i++){
                View v = progressViewsMap.get(String.valueOf(i));
                progressMainView.removeView(v);
                progressViewsMap.remove(String.valueOf(i));
            }
        }

        // 开启多个子线程
        addProgressBar(0);

        for (int i = 0; i < threadCount; i++) {
            long startIndex = i * everyLength;
            long endIndex = (i + 1) * everyLength - 1;

            if (i == threadCount - 1){
                endIndex = totalLength - 1;
            }

            MyThread thread = new MyThread(this,i + 1,startIndex,endIndex,downloadUrl,savePath.getAbsolutePath(),handle);
            thread.start();
            threadMap.put(String.valueOf(i + 1),thread);

            // 添加进度条
            addProgressBar(i + 1);
        }
    }

    // 暂停下载
    public void pauseDownload(){
        for (int i = 1; i < threadCount + 1;i++){
            MyThread thread = threadMap.get(String.valueOf(i));
            if (thread == null) continue;
            thread.pauseThread();
        }
    }

    // 继续下载
    public void continueDownload(){
        for (int i = 1; i < threadCount + 1;i++){
            MyThread thread = threadMap.get(String.valueOf(i));
            thread.resumeThread();
        }
    }

    // 添加进度条
    public void addProgressBar(int threadId){
        View progressView = progressViewsMap.get(String.valueOf(threadId));
        if (progressView == null){
            progressView = getLayoutInflater().inflate(R.layout.activity_muilt_thread_progress,null);
            progressMainView.addView(progressView);
            progressViewsMap.put(String.valueOf(threadId),progressView);
        }

        if (threadId == 0){
            // 设置粗体
            TextView textView = progressView.findViewById(R.id.threadName);
            TextPaint paint = textView.getPaint();
            paint.setFakeBoldText(true);
//            textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }
    }

    // 更新下载进度条
    public void updateDownloadProgress(int threadId){
        // 获取子线程对象
        MyThread thread = thread = threadMap.get(String.valueOf(threadId));

        // 更新已下载大小
        long length = 0;
        for (int i = 1; i < threadCount + 1; i++){
            MyThread thread1 = threadMap.get(String.valueOf(i));
            if (thread1 == null) continue;
            length += thread1.getDownloadLength();
        }

        downloadedLength = length;

        if (downloadedLength == totalLength){
            // 下载完成
            Message mesg = Message.obtain();
            mesg.what = DOWNLOADCOMPLETE;
            handle.sendMessage(mesg);
        }

        // 更新总进程进度条
        View totalProgressView = progressViewsMap.get("0");
        updateView(totalProgressView,0,null);

        // 更新子线程进度条
        View progressView = progressViewsMap.get(String.valueOf(threadId));
        updateView(progressView,threadId,thread);

        // 更新时间
        updateTimeView();
    }

    //更新进度条
    public void updateView(View progressView,int threadId,MyThread thread){
        TextView textView = progressView.findViewById(R.id.threadName);

        if (thread == null && threadId > 0) return;

        // 格式化总大小和已下载大小
        long totalLen = threadId > 0 ? thread.getTotalLenth() : totalLength;
        long downloadLen = threadId > 0 ? thread.getDownloadLength() : downloadedLength;
        String totalLengthFormater = Formatter.formatFileSize(this,totalLen);
        String downloadLengthFormater = Formatter.formatFileSize(this,downloadLen);
        float downloadProgress = (float)downloadLen/totalLen;
        downloadProgress = (float)(downloadProgress * 100.0);
        String progress = String.format("%.2f",downloadProgress) + "%";
        String threadName = threadId > 0 ? "线程" + threadId : "总进度";
        String tipMsg = threadName + "共" + totalLengthFormater + " 已下载" + downloadLengthFormater + " 进度" + progress;
        textView.setText(tipMsg);
        // 更新进度
        ProgressBar progressBar = progressView.findViewById(R.id.progressbar);
        progressBar.setProgress((int)downloadProgress);
    }

    // 刷新耗用的时间
    public void updateTimeView(){
        long currentTime = System.currentTimeMillis();

        long time = currentTime - startTime - pauseTotalTime;

        String timeString = formaterTime(time);

        timeView.setText("总用时:" + timeString);
    }

    public static String formaterTime(long millis){
        // 时间差 单位毫秒
        double time = (double) millis;

        int year = (int)(time/1000.0/60.0/60.0/24.0/30.0/365.0);
        int month = (int)((time - year * 365.0 * 30.0 * 24.0 * 60.0 * 60.0 * 1000.0)/1000.0/60.0/60.0/24.0/30.0);
        int day = (int)((time - year * 365.0 * 30.0 * 24.0 * 60.0 * 60.0 * 1000.0 - month * 30.0 * 24.0 * 60.0 * 60.0 * 1000.0)/1000.0/60.0/60.0/24.0);
        int hour = (int)((time - year * 365.0 * 30.0 * 24.0 * 60.0 * 60.0 * 1000.0 - month * 30.0 * 24.0 * 60.0 * 60.0 * 1000.0 - day * 24.0 * 60.0 * 60.0 * 1000.0)/1000.0/60.0/60.0);
        int minute = (int)((time - year * 365.0 * 30.0 * 24.0 * 60.0 * 60.0 * 1000.0 - month * 30.0 * 24.0 * 60.0 * 60.0 * 1000.0 - day * 24.0 * 60.0 * 60.0 * 1000.0 - hour * 60.0 * 60.0 * 1000.0)/1000.0/60.0);
        int second = (int)((time - year * 365.0 * 30.0 * 24.0 * 60.0 * 60.0 * 1000.0 - month * 30.0 * 24.0 * 60.0 * 60.0 * 1000.0 - day * 24.0 * 60.0 * 60.0 * 1000.0 - hour * 60.0 * 60.0 * 1000.0 - minute * 60.0 * 1000.0)/1000.0);

        StringBuilder builder = new StringBuilder();
        if (year > 0){
            builder.append(year + "年");
        }

        if (month >0){
            builder.append(month + "个月");
        }

        if (day > 0){
            builder.append(day + "天");
        }

        if (hour > 0){
            builder.append(hour + "小时");
        }

        if (minute > 0){
            builder.append(minute + "分钟");
        }

        if (second > 0){
            builder.append(second + "秒");
        }

        return builder.toString();
    }

    // 修改文件/文件夹权限
    public static void chmodFile(Context context,String path){
        String[] command = {"chmod", "777", path};
        ProcessBuilder builder = new ProcessBuilder(command);
        try {
            builder.start();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context,"修改" + path + "权限失败",Toast.LENGTH_SHORT).show();
        }
    }

    // 销毁所有线程
    public void closeAllThread(){
        for (int i = 1; i < threadCount + 1; i++){
            MyThread thread = threadMap.get(String.valueOf(i));
            if (thread == null) continue;
            thread.closeThread();
            thread = null;
            threadMap.remove(String.valueOf(i));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.e("tag",">>>>>>>>>>>>>>>>>pause");
        pauseDownload();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.e("tag",">>>>>>>>>>>>>>>>>destroy");
        closeAllThread();
    }

    public class DownloadHandle extends Handler{
        @Override
        public void handleMessage(Message msg) {
            // 去掉蒙版
            cover.setVisibility(View.GONE);

            switch (msg.what){
                case DOWNLOADSTART:
                    // 获取文件总大小成功，开始下载
                    // 修改下载按钮为暂停
                    downloadBtn.setText("暂停");
                    Toast.makeText(MuiltThreadDownloadActivity.this,"开始下载",Toast.LENGTH_SHORT).show();
                    startDownload();
                    break;
                case DOWNLOADING:
                    // 正在下载
                    int threadId = msg.arg1;
                    updateDownloadProgress(threadId);
                    break;
                case REQUESTFAILE:
                case REQUESTEXCEPTION:
                    // 请求失败
                    downloadBtn.setText("下载手机QQ");
                    String str = (String) msg.obj;
                    Toast.makeText(MuiltThreadDownloadActivity.this,str,Toast.LENGTH_SHORT).show();
                    break;
                case DOWNLOADCOMPLETE:
                    if (threadMap.size() != 0) {
                        // 下载完成
                        downloadBtn.setText("下载手机QQ");

                        // 销毁所有线程
                        closeAllThread();

                        new AlertDialog.Builder(MuiltThreadDownloadActivity.this)
                                .setTitle("温馨提示")
                                .setMessage("下载完成,确定安装吗?")
                                .setNegativeButton("取消",null)
                                .setPositiveButton("安装", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        installApk(MuiltThreadDownloadActivity.this,savePath);
                                    }
                                })
                                .show();
                    }

                    break;
                case DOWNLOADPARTCOMPLETE:
                    // 某一个线程下载完成,停止线程
                    int thrid = msg.arg1;
                    MyThread thread = threadMap.get(String.valueOf(thrid));
                    thread.pauseThread();
                    break;
            }
        }
    }
}
