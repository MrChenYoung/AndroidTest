package com.androidtest.elements;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.androidtest.elements.BroadcastReceiverActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class BroadcastReceiverMonitor extends BroadcastReceiver {
    @Override // 接受到广播后调用
    public void onReceive(Context context, Intent intent) {

        try {
            // 根据action区分广播类型
            String action = intent.getAction();
            String message = null;
            if (action.equals("android.intent.action.NEW_OUTGOING_CALL")){
                // 监听到有电话拨出
                message = "监听到有电话拨出,拨打号码是:" + getResultData();
            }else if (action.equals("android.intent.action.MEDIA_UNMOUNTED")){
                // 监听到sd卡卸载了
                message = "监听到sd卡被卸载了";
            }else if(action.equals("android.intent.action.MEDIA_MOUNTED")){
                // 监听到sd卡装载
                message = "监听到sd卡装载了";
            }else if (action.equals("android.provider.Telephony.SMS_RECEIVED")){
                // 监听到接收到短信

                // 获取短信信息
                Bundle bundle = intent.getExtras();
                Object[] pdus = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdus.length; i++){
                    SmsMessage msg = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    String sendNumber = msg.getOriginatingAddress();
                    String content = msg.getMessageBody();
                    long time = msg.getTimestampMillis();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒",Locale.getDefault());
                    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
                    String date = dateFormat.format(new Date(time));

                    // 更新listview
                    message = "监听到接收到短信:" + "\n发送者:" + sendNumber + "\n发送时间:" + date + "\n消息内容:" + content;
                    BroadcastReceiverActivity.receiveCall(context,message);
                }

                return;
            }else if (action.equals("android.intent.action.PACKAGE_ADDED")) {
                // 监听到有应用安装了
                message = "监听到有应用被安装了,包名为:" + intent.getDataString();
            }else if (action.equals("android.intent.action.PACKAGE_REMOVED")){
                // 监听到有应用卸载了
                message = "监听到有应用卸载了,包名为:" + intent.getDataString();
            }else if (action.equals("android.intent.action.BOOT_COMPLETED")){
                // 手机启动
                Toast.makeText(context,"手机启动",Toast.LENGTH_SHORT).show();
                return;
            }else if (action.equals("android.intent.action.LOCKED_BOOT_COMPLETED")){
                // 手机解锁
                Toast.makeText(context,"手机解锁",Toast.LENGTH_SHORT).show();
                return;
            }else if (action.equals("android.intent.action.REBOOT")){
                // 手机重启
                Toast.makeText(context,"手机重启",Toast.LENGTH_SHORT).show();
                return;
            }else if (action.equals("com.custom.unorderBroadcast")){
                // 监听到自定义发送的无序广播
                message = intent.getStringExtra("broadcastMessage");
                Toast.makeText(context,"接收到自定义无序广播:" + message,Toast.LENGTH_SHORT).show();
                return;
            }else if (action.equals("android.intent.action.SCREEN_ON")){
                // 屏幕解锁了
                message = "监听到屏幕解锁了";
                Log.e("tag","屏幕解锁了");
            }else if (action.equals("android.intent.action.SCREEN_OFF")){
                // 屏幕锁屏了
                message = "监听到屏幕锁屏了";
                Log.e("tag","锁屏了");
            }else if (action.equals("android.intent.action.BATTERY_CHANGED")){
                // 电池电量变化了
                message = "监听到电池电量变化\n";

                // 获取当前电量和总电量
                Bundle bundle = intent.getExtras();
                int current = bundle.getInt("level");
                int total = bundle.getInt("scale");
                double persent = current * 1.0 / total;

                message = message + "总电量:" + total + "\n当前电量:" + current + "\n电量比:" + (int)(persent * 100) + "%";
            }


            // 设置监听到的电话号码到界面上
            BroadcastReceiverActivity.receiveCall(context,message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
