package com.androidtest.Demos.NetWorkRequestDemos;

import android.content.Context;
import android.os.Message;
import android.text.format.Formatter;
import android.util.Log;

import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyThread extends Thread {
    // 用于暂停和重启线程
    private final Object lock = new Object();
    private boolean pause = false;

    private Context context;

    // 线程id
    private int threadId;
    // 下载开始和结束位置
    private long startPoint;
    private  long endPoint;
    // 该线程要现在的总大小
    private long totalLenth;
    // 该线程已经下载的大小
    private long downloadLength;

    // 下载路径
    private String sourcePath;

    // 文件存储路径
    private String destPath;

    // handle
    private MuiltThreadDownloadActivity.DownloadHandle handle;

    public MyThread(Context context, int threadId, long startPoint, long endPoint, String sourcePath, String destPath, MuiltThreadDownloadActivity.DownloadHandle handle){
        this.context = context;
        this.threadId = threadId;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        totalLenth = endPoint - startPoint;

        this.sourcePath = sourcePath;
        this.destPath = destPath;

        this.handle = handle;
    }

    @Override
    public void run() {
        HttpURLConnection connection = null;
        InputStream inputStream = null;

        try {
            URL url = new URL(sourcePath);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Range","bytes=" + startPoint + "-" + endPoint);
            int code = connection.getResponseCode();

            if (code == HttpURLConnection.HTTP_PARTIAL){
                // 部分资源下载完成(用于多线程下载)
                RandomAccessFile raf = new RandomAccessFile(destPath,"rw");
                raf.seek(startPoint);

                inputStream = connection.getInputStream();
                byte[] buffer = new byte[1024 * 1024];
                int length = -1;
                while ((length = inputStream.read(buffer)) != -1){
                    raf.write(buffer,0,length);

                    // 更新已经下载的文件大小
                    downloadLength += length;


                    // 该线程正在下载中,发送消息给主线程更新下载进度
                    Message msg = Message.obtain();
                    msg.what = MuiltThreadDownloadActivity.DOWNLOADING;
                    msg.arg1 = threadId;
                    handle.sendMessage(msg);

                    // 判断线程是否被暂停了
                    if (pause){
                        onPause();
                    }
                }
            }else {
                // 下载失败,发送消息给主线程提示用户
                Message msg = Message.obtain();
                msg.what = MuiltThreadDownloadActivity.REQUESTFAILE;
                msg.obj = "下载出错,线程id:" + threadId + " 错误码:" + code;
                handle.sendMessage(msg);
            }

        } catch (Exception e) {
            e.printStackTrace();

            // 下载出现异常
            Message msg = Message.obtain();
            msg.what = MuiltThreadDownloadActivity.REQUESTEXCEPTION;
            msg.obj = "下载出现异常,线程id:" + threadId + "异常信息:" + e.toString();
            handle.sendMessage(msg);
        }finally {
            // 释放资源
            if (connection != null){
                connection.disconnect();
            }

            if (inputStream != null){
                inputStream = null;
            }
        }
    }

    /**
     * 调用这个方法实现恢复线程的运行
     */
    void resumeThread() {
        pause = false;
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    /**
     * 调用这个方法实现暂停线程
     */
    void pauseThread() {
        pause = true;
    }

    /**
     * 注意：这个方法只能在run方法里调用，不然会阻塞主线程，导致页面无响应
     */
    void onPause() {
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public int getThreadId() {
        return threadId;
    }

    public long getTotalLenth() {
        return totalLenth;
    }

    public long getDownloadLength() {
        return downloadLength;
    }
}
