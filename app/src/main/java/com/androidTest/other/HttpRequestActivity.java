package com.androidTest.other;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.androidTest.R;

public class HttpRequestActivity extends Activity {
    private EditText editText;
    private ImageView imageView;
    private String BaseUrl = "http://tcc.taobao.com/cc/json/mobile_tel_segment.htm";
    private AlertDialog.Builder builder;
    private TextView resultTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_httprequest);

        // 找到所需控件
        editText = findViewById(R.id.editText);
        imageView = findViewById(R.id.imageView);
        resultTextView = findViewById(R.id.tv_result);
    }

    // get请求
    public void requestWithGet(View view) {
        // 首先清空结果显示框
        resultTextView.setText("");

        // 发送GET请求
        new MyAsyncTask().execute("GET");
    }

    // post请求
    public void requestWithPost(View view) {
        // 请求结果显示框
        resultTextView.setText("");

        // 发送POST请求
        new MyAsyncTask().execute("POST");
    }

    // 异步请求内部类
    class MyAsyncTask extends AsyncTask<String, Void, String> {
        @Override // 子线程执行
        protected String doInBackground(String... strings) {

            // 获取请求类型(GET或POST)
            String method = strings[0];

            try {
                String urlString = null;
                if (method.equals("GET")) {
                    // get请求
                    urlString = BaseUrl + "?tel=" + editText.getText().toString().trim();

                } else if (method.equals("POST")) {
                    // post请求
                    urlString = BaseUrl;
                }

                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                // 5秒超时时间
                connection.setConnectTimeout(5000);
                if (method.equals("POST")){
                    // 添加请求方法和参数
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);
                    OutputStream os = connection.getOutputStream();
                    String params = String.format("tel=%s",editText.getText().toString().trim());
                    os.write(params.getBytes());
                }
                int code = connection.getResponseCode();
                if (code == 301 || code == 302){
                    // 重定向，重新请求
                    url = new URL(connection.getHeaderField("Location"));
                    connection = (HttpURLConnection) url.openConnection();
                    code = connection.getResponseCode();
                }

                // 解析请求结果
                InputStream stream = connection.getInputStream();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] bytes = new byte[1024];
                int length = -1;
                while ((length = stream.read(bytes)) != -1){
                    outputStream.write(bytes,0,length);
                }
                stream.close();
                String str = new String(outputStream.toByteArray(),"gbk");

                if (code == 200) {
                    // 请求成功
                    return str;
                } else {
                    // 请求失败
                    return "查询出错" + code;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return "请求出错";
        }

        @Override // 主线程执行 参数是doInBackground方法的返回值
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            // 根据请求结果更新界面
            resultTextView.setText(s);
        }
    }
}


