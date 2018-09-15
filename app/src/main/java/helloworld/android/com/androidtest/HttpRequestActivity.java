package helloworld.android.com.androidtest;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HttpRequestActivity extends Activity {
    private MyHandle handler = new MyHandle();

    private final String BASE_GET_URL = "http://tcc.taobao.com/cc/json/mobile_tel_segment.htm";
    private final int LOAD_SUCCESS = 1;
    private final int LOAD_ERROR = 2;


    private EditText editText;
    private ImageView imageView;
    private String BaseGetUrl = "http://tcc.taobao.com/cc/json/mobile_tel_segment.htm";
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_httprequest);
        editText = findViewById(R.id.editText);
        imageView = findViewById(R.id.imageView);

        builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setPositiveButton("确定",null);

    }

    // get请求
    public void requestWithGet(View view) {
        new MyAsyncTask().execute("GET");
    }

    // post请求
    public void requestWithPost(View view) {
        new MyAsyncTask().execute("POST");
    }

    class MyAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String method = strings[0];

            try {
                if (method.equals("GET")) {
                    // get请求
                    String urlString = BaseGetUrl + "?tel=" + editText.getText().toString();
                    URL url = new URL(urlString);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    int code = connection.getResponseCode();
                    if (code == 301 || code == 302){
                        url = new URL(connection.getHeaderField("Location"));
                        connection = (HttpURLConnection) url.openConnection();
                        code = connection.getResponseCode();
                    }

                    InputStream stream = connection.getInputStream();
                    byte[] bytes = new byte[stream.available()];
                    stream.read(bytes);
                    stream.close();
                    String str = new String(bytes);

                    if (code == 200) {
                        // 请求成功
                        return str;
                    } else {
                        // 请求失败
                        return "查询出错" + code;
                    }
                } else if (method.equals("POST")) {
                    // post请求

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            builder.setMessage(s);
            builder.show();
        }
    }
}


//private void requestGet(HashMap<String, String> paramsMap) {
//        try {
//            String baseUrl = BASE_GET_URL;
//            StringBuilder tempParams = new StringBuilder();
//            int pos = 0;
//            for (String key : paramsMap.keySet()) {
//                if (pos > 0) {
//                    tempParams.append("&");
//                }
//                tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key),"utf-8")));
//                pos++;
//            }
//            String requestUrl = baseUrl + "?" + tempParams.toString();
//            // 新建一个URL对象
//            URL url = new URL(requestUrl);
//            // 打开一个HttpURLConnection连接
//            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
//            // 设置连接主机超时时间
//            urlConn.setConnectTimeout(5 * 1000);
//            //设置从主机读取数据超时
//            urlConn.setReadTimeout(5 * 1000);
//            // 设置是否使用缓存  默认是true
//            urlConn.setUseCaches(true);
//            // 设置为Get请求
//            urlConn.setRequestMethod("GET");
//            //urlConn设置请求头信息
//            //设置请求中的媒体类型信息。
//            urlConn.setRequestProperty("Content-Type", "application/json");
//            //设置客户端与服务连接类型
//            urlConn.addRequestProperty("Connection", "Keep-Alive");
//            // 开始连接
//            urlConn.connect();
//            // 判断请求是否成功
//            if (urlConn.getResponseCode() == 200) {
//                // 获取返回的数据
//                String result = streamToString(urlConn.getInputStream());
//                Message msg =  Message.obtain();
//                msg.what = LOAD_SUCCESS;
//                msg.obj = result;
//                handler.sendMessage(msg);
//            } else {
//                Message msg =  Message.obtain();
//                msg.what = LOAD_ERROR;
//                msg.obj = "请求失败";
//                handler.sendMessage(msg);
//            }
//            // 关闭连接
//            urlConn.disconnect();
//        } catch (Exception e) {
//            Message msg =  Message.obtain();
//            msg.what = LOAD_ERROR;
//            msg.obj = "发生异常";
//            handler.sendMessage(msg);
//        }
//    }
//
//
//    private void requestPost(HashMap<String, String> paramsMap) {
//        try {
//            String baseUrl = "";
//            //合成参数
//            StringBuilder tempParams = new StringBuilder();
//            int pos = 0;
//            for (String key : paramsMap.keySet()) {
//                if (pos > 0) {
//                    tempParams.append("&");
//                }
//                tempParams.append(String.format("%s=%s", key,  URLEncoder.encode(paramsMap.get(key),"utf-8")));
//                pos++;
//            }
//            String params =tempParams.toString();
//            // 请求的参数转换为byte数组
//            byte[] postData = params.getBytes();
//            // 新建一个URL对象
//            URL url = new URL(baseUrl);
//            // 打开一个HttpURLConnection连接
//            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
//            // 设置连接超时时间
//            urlConn.setConnectTimeout(5 * 1000);
//            //设置从主机读取数据超时
//            urlConn.setReadTimeout(5 * 1000);
//            // Post请求必须设置允许输出 默认false
//            urlConn.setDoOutput(true);
//            //设置请求允许输入 默认是true
//            urlConn.setDoInput(true);
//            // Post请求不能使用缓存
//            urlConn.setUseCaches(false);
//            // 设置为Post请求
//            urlConn.setRequestMethod("POST");
//            //设置本次连接是否自动处理重定向
//            urlConn.setInstanceFollowRedirects(true);
//            // 配置请求Content-Type
//            urlConn.setRequestProperty("Content-Type", "application/json");
//            // 开始连接
//            urlConn.connect();
//            // 发送请求参数
//            DataOutputStream dos = new DataOutputStream(urlConn.getOutputStream());
//            dos.write(postData);
//            dos.flush();
//            dos.close();
//            // 判断请求是否成功
//            int code = urlConn.getResponseCode();
//            if (code == 200) {
//                // 获取返回的数据
////                InputStream inputStream = urlConn.getInputStream();
////                byte[] bytes = new byte[inputStream.available()];
////                inputStream.read(bytes);
////                inputStream.close();
//
//                String result = streamToString(urlConn.getInputStream());
//                Message msg =  Message.obtain();
//                msg.what = LOAD_SUCCESS;
//                msg.obj = result;
//                handler.sendMessage(msg);
//            } else {
//                Message msg = Message.obtain();
//                msg.what = LOAD_ERROR;
//                msg.obj = "请求失败" + code;
//                handler.sendMessage(msg);
//            }
//            // 关闭连接
//            urlConn.disconnect();
//        } catch (Exception e) {
//            Message msg = Message.obtain();
//            msg.what = LOAD_ERROR;
//            msg.obj = "出现异常";
//            handler.sendMessage(msg);
//        }
//    }
//
//    /**
//     * 将输入流转换成字符串
//     *
//     * @param is 从网络获取的输入流
//     * @return
//     */
//public String streamToString(InputStream is) {
//    try {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        byte[] buffer = new byte[1024];
//        int len = 0;
//        while ((len = is.read(buffer)) != -1) {
//            baos.write(buffer, 0, len);
//        }
//        baos.close();
//        is.close();
//        byte[] byteArray = baos.toByteArray();
//        return new String(byteArray);
//    } catch (Exception e) {
////            Log.e("0", e.toString());
//        return null;
//    }
//}
//
//class MyHandle extends Handler {
//    @Override
//    public void handleMessage(Message msg) {
//        super.handleMessage(msg);
//
//        String str = (String) msg.obj;
//        switch (msg.what){
//            case LOAD_SUCCESS:
////                    Toast.makeText(MainActivity.this,str,Toast.LENGTH_LONG).show();
//                new AlertDialog.Builder(MainActivity.this).setTitle("提示").setMessage(str).show();
//                Log.e("0",">>>>>>>>>" + str);
//                break;
//            case LOAD_ERROR:
//                Toast.makeText(MainActivity.this,str,Toast.LENGTH_LONG).show();
//                break;
//        }
//    }
//}