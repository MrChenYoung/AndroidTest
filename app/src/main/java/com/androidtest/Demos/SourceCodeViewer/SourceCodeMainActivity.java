package com.androidtest.Demos.SourceCodeViewer;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

import helloworld.android.com.androidtest.R;

public class SourceCodeMainActivity extends AppCompatActivity {

    private EditText ed_path;
    private TextView tv_result;
    private MyHandle handle = new MyHandle();

    // 状态码
    private final int REQUESTSUCCESS = 1;
    private final int REQUESTRELOAD = -2;
    private final int REQUESTFAILE = 0;
    private final int REQUESTUNAVAILABLEURL = -1;
    private View coverView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source_code_main);

        // 获取网址输入框
        ed_path = findViewById(R.id.ed_path);

        // 结果显示
        tv_result = findViewById(R.id.tv_code);
        ed_path.setText("http://www.baidu.com");

        // progressbar
        coverView = findViewById(R.id.cover);
    }

    // 查看源码
    public void checkCode(View view){
        tv_result.setText("");

        // 获取网址
        String urlStr = ed_path.getText().toString();
        requestUrl(urlStr);
    }

    // 网络请求
    public void requestUrl(final String urlStr){
        // 显示加载等待progressbar
        coverView.setVisibility(View.VISIBLE);

        // 子线程加载数据
        new Thread(){
            @Override
            public void run() {
                try {
                    // 请求数据
                    URL url = new URL(urlStr);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(2000);
                    int code = connection.getResponseCode();

                    Message msg = new Message();
                    msg.arg1 = code;
                    if (code == 200){
                        // 请求成功
                        String result = streamToString(connection.getInputStream());
                        msg.what = REQUESTSUCCESS;
                        msg.obj = result;
                    }else if(code == 302 || code == 301) {
                        String location = connection.getHeaderField("Location");
                        msg.what = REQUESTRELOAD;
                        msg.obj = location;
                    }else {
                        msg.what = REQUESTFAILE;
                    }

                    handle.sendMessage(msg);
                } catch (Exception e) {
                    // 出现异常，提示
                    Message msg = new Message();
                    msg.what = REQUESTUNAVAILABLEURL;
                    handle.sendMessage(msg);

                    e.printStackTrace();
                }
            }
        }.start();
    }

    // 数据流转换成字符串
    public String streamToString(InputStream stream){
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int len = -1;
            byte[] buffer = new byte[1024];
            while ((len = stream.read(buffer)) != -1){
                baos.write(buffer,0,len);
            }

            stream.close();
            String result = new String(baos.toByteArray());

            return result;
        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this,"1111",Toast.LENGTH_LONG).show();
        }

        return null;
    }

    private class MyHandle extends Handler {
        @Override
        public void handleMessage(Message msg) {
            // 显示加载等待progressbar
            coverView.setVisibility(View.GONE);

            String result = (String)msg.obj;

            switch (msg.what){
                case REQUESTSUCCESS:
                    // 请求成功
                    tv_result.setText(result);
                    break;
                case REQUESTRELOAD:
                    // 重定向,重新请求
                    requestUrl(result);
                    break;
                case REQUESTUNAVAILABLEURL:
                    // 无效的网址
                    Toast.makeText(SourceCodeMainActivity.this,"无效的网址",Toast.LENGTH_LONG).show();
                    break;
                case REQUESTFAILE:
                    // 请求失败
                    Toast.makeText(SourceCodeMainActivity.this,"请求失败,请重试!错误码:" + msg.arg1,Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }
}
