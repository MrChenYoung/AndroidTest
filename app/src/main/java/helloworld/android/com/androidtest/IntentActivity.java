package helloworld.android.com.androidtest;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.List;

public class IntentActivity extends Activity {

    private final static String SENT_SMS_ACTION = "SENT_SMS_ACTION";
    private final static String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";
    private SMSBroadcastReceiver1 receiver1;
    private SMSBroadcastReceiver2 receiver2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent);

        // 注册广播
        receiver1 = new SMSBroadcastReceiver1();
        IntentFilter intentFilter1 = new IntentFilter(SENT_SMS_ACTION);
        registerReceiver(receiver1,intentFilter1);

        receiver2 = new SMSBroadcastReceiver2();
        IntentFilter intentFilter2 = new IntentFilter(DELIVERED_SMS_ACTION);
        registerReceiver(receiver2,intentFilter2);

    }

    // 跳转
    public void click(View view){

        // 显式Intent
//        Intent intent = new Intent();
//        intent.setComponent(new ComponentName("helloworld.android.com.androidtest","helloworld.android.com.androidtest.Intent1Activity"));


        // 隐式Intent
        Intent intent = new Intent("helloworld.android.com.androidtest.MyIntent");
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
    public void click2(View view){
        File file = new File("/mnt/sdcard/Download/a1.png");

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),"image/*");

        startActivity(intent);
    }

    // 打开网页
    public void click4(View view){
        Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.baidu.com"));
        startActivity(intent);
    }

    // 打开打电话界面
    public void click3(View view){
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setData(Uri.parse("tel:110"));
//        startActivity(intent);

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:110"));
        startActivity(intent);
    }

    // 拨打电话
    public void click5(View view){
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel://120"));
        startActivity(intent);
    }

    // 发送短信 跳转到发送短信界面(无法监听短信是否发送成功，收件人是否收到短信)
    public void click6(View view){
        Intent intent = new Intent(Intent.ACTION_SENDTO,Uri.parse("smsto:120"));
        intent.putExtra("sms_body","短信内容");
        startActivity(intent);
    }

    // 发送短信  直接发送（可以监听短信发送是否成功以及收件人是否收到短信）
    public void click7(View view){
        String msg = "短信内容";

        SmsManager smsManager = SmsManager.getDefault();
        List<String> msgs = smsManager.divideMessage(msg);

        Intent intent1 = new Intent(SENT_SMS_ACTION);
        Intent intent2 = new Intent(DELIVERED_SMS_ACTION);
        PendingIntent sendIntent = PendingIntent.getBroadcast(this,0,intent1,0);
        PendingIntent deliveredIntent = PendingIntent.getBroadcast(this,0,intent2,0);

        for (String message : msgs) {
            smsManager.sendTextMessage("5556",null,message,sendIntent,deliveredIntent);
        }
    }

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

    private class SMSBroadcastReceiver2 extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(IntentActivity.this,"短信已被接受",Toast.LENGTH_SHORT).show();
        }
    }

}

