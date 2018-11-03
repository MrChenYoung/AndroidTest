package com.androidtest.other;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import helloworld.android.com.androidtest.R;

public class IntentActivity extends Activity {

    private View cover;

    private final static String SENT_SMS_ACTION = "SENT_SMS_ACTION";
    private final static String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";
    private SMSBroadcastReceiver1 receiver1;
    private SMSBroadcastReceiver2 receiver2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent);

        cover = findViewById(R.id.cover);

        // 注册短信发送和接受结果广播
        receiver1 = new SMSBroadcastReceiver1();
        IntentFilter intentFilter1 = new IntentFilter(SENT_SMS_ACTION);
        registerReceiver(receiver1,intentFilter1);

        receiver2 = new SMSBroadcastReceiver2();
        IntentFilter intentFilter2 = new IntentFilter(DELIVERED_SMS_ACTION);
        registerReceiver(receiver2,intentFilter2);

    }

    // 隐式意图跳转
    // 定义: 通过制定一组动作(action)或者数据(data)跳转
    // 一般用于应用间跳转，不需要知道类名和包名
    public void implicitSkip(View view){

        // 隐式Intent
        Intent intent = new Intent();
        intent.setAction("helloworld.android.com.androidtest.MyIntent");
        intent.setDataAndType(Uri.parse("myintent:" + "111"),"aa/bb");
        startActivity(intent);
    }

    // 显式意图跳转
    // 定义:通过制定具体的包名和类名
    // 一般用于应用内跳转
    public void explicitSkip(View view){
        // 显式Intent
        Intent intent = new Intent();
        intent.setClass(this,Intent1Activity.class);
        startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 注销广播
        unregisterReceiver(receiver1);
        unregisterReceiver(receiver2);
    }

    // 打开图片浏览器
    public void openPhotoBrowser(View view){
        // 拷贝图片到指定位置
        File path = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            // sd卡可用,拷贝图片到sd卡
            path = new File(Environment.getExternalStorageDirectory(),"a1.png");
        }else {
            // sd卡不可用,拷贝图片到内部存储
            path = new File(getFilesDir(),"a1.png");
        }

        if (path.exists()){
            path.delete();
        }

        final File outPath = path;
        // 拷贝图片到sd卡
        cover.setVisibility(View.VISIBLE);
        new Thread(){
            @Override
            public void run() {
                // 方式一
//                InputStream inputStream = getResources().openRawResource((int)R.drawable.a3);
//                copyFileWithIOStream(inputStream,path);

                // 方式二
                copyFileWithBitmap(R.drawable.a3,outPath);

                // 拷贝完成，主线程更新UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cover.setVisibility(View.GONE);

                        // 用图片浏览器查看
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(outPath),"image/*");

                        startActivity(intent);
                    }
                });
            }
        }.start();
    }

    // 打开网页
    public void openWebView(View view){
        Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.baidu.com"));
        startActivity(intent);
    }

    // 打开打电话界面
    public void callPhone1(View view){
        // 使用Intent.ACTION_VIEW也可以
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:10086"));
        startActivity(intent);
    }

    // 拨打电话
    public void callPhone2(View view){
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel://10086"));
        startActivity(intent);
    }

    // 发送短信 跳转到发送短信界面(无法监听短信是否发送成功，收件人是否收到短信)
    public void sendMessage1(View view){
        Intent intent = new Intent(Intent.ACTION_SENDTO,Uri.parse("smsto:1008611"));
        intent.putExtra("sms_body","短信内容");
        startActivity(intent);
    }

    // 发送短信  直接发送（可以监听短信发送是否成功以及收件人是否收到短信）
    public void sendMessage2(View view){
        // 发送短信内容
        String msg = "短信内容";

        // 短信发送器
        SmsManager smsManager = SmsManager.getDefault();

        // 如果短信字数超出限制，拆分成多条发送
        List<String> msgs = smsManager.divideMessage(msg);

        Intent intent1 = new Intent(SENT_SMS_ACTION);
        Intent intent2 = new Intent(DELIVERED_SMS_ACTION);
        PendingIntent sendIntent = PendingIntent.getBroadcast(this,0,intent1,PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent deliveredIntent = PendingIntent.getBroadcast(this,0,intent2,PendingIntent.FLAG_CANCEL_CURRENT);

        for (String message : msgs) {
            smsManager.sendTextMessage("5556",null,message,sendIntent,deliveredIntent);
        }
    }

    // 跳转到下个页面，把当前页数据带过去
    public void nextPage(View view){
        // 获取输入的姓名和年龄
        EditText ed_name = findViewById(R.id.ed_name);
        String name = ed_name.getText().toString().trim();
        EditText ed_age = findViewById(R.id.ed_age);
        int age = Integer.parseInt(ed_age.getText().toString().trim());

        // 跳转并携带数据
        Intent intent = new Intent();
        intent.setClass(this,Intent1Activity.class);
        intent.putExtra("name",name);
        intent.putExtra("age",age);

        // 下个页面不需要回传数据的时候用这个
//        startActivity(intent);

        // 下个页面返回需要回传数据的时候用这个
        startActivityForResult(intent,2);
    }

    // 这个页面被返回的时候调用，用于上个页面返回到这个页面带数据回来
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 获取数据
        String result = data.getStringExtra("result");
        EditText editText = findViewById(R.id.ed_result);
        editText.setText(result);
    }

    // 短信发送结果监听
    private class SMSBroadcastReceiver1 extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (getResultCode() == Activity.RESULT_OK){
                Toast.makeText(IntentActivity.this,"短信发送成功",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(IntentActivity.this,"短信发送失败",Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 发送的短信接受结果监听(经测试无效果)
    private class SMSBroadcastReceiver2 extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(IntentActivity.this,"短信已被接受",Toast.LENGTH_SHORT).show();
        }
    }

    // 拷贝drawable目录下的文件到sd卡或者内部存储卡
    // 方式一:通过IO流的形式
    public void copyFileWithIOStream(InputStream inputStream, File toPath){
        FileOutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(toPath);

            byte[] buffer = new byte[1024];
            int length = -1;
            while ((length = inputStream.read(buffer)) != -1){
                outputStream.write(buffer,0,length);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null){
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 方式二:转换成bitmap形式
    public void copyFileWithBitmap(int fileId,File toPath){
        Drawable drawable = this.getResources().getDrawable(fileId);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),fileId);

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(toPath);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null){
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

