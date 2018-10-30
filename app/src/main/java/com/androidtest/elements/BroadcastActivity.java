package com.androidtest.elements;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import helloworld.android.com.androidtest.R;

public class BroadcastActivity extends Activity {
    // 广播接收者
    private BroadcastReceiverMonitor broadcastRe = new BroadcastReceiverMonitor();
    private Intent registBroadcastIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast);

    }

    // 监听广播事件
    public void monitorBroadarcast(View view){
        Intent intent = new Intent(this,BroadcastReceiverActivity.class);
        startActivity(intent);
    }

    // 发送无序广播
    // 特点: 1>不可以被中断
    //      2>数据不可以修改
    public void sendUnorderBrodarcast(View view){
        Intent intent = new Intent();
        intent.setAction("com.custom.unorderBroadcast");
        intent.putExtra("broadcastMessage","自定义无序广播");
        sendBroadcast(intent);
    }

    // 发送有序广播
    // 特点: 1>可以被中断
    //      2>数据可以修改
    public void sendOrderBroadcast(View view){
        Intent intent = new Intent();
        intent.setAction("com.custom.orderBroadcast");
        /*
         * 发送有序广播
         * 参数1 intent
         * 参数2 接受权限
         * 参数3 最终的接收者(不需要在清单文件里面配置,即使中途广播被终止也可以接收到)
         * 参数4 handle
         * 参数5 初始码
         * 参数6 初始数据
         * 参数7 初始扩展数据
         */
        sendOrderedBroadcast(intent,null,new BroadcastReceiverFinal(),null,0,"有序广播数据:" + 1000,null);
    }

    // 注册广播
    // 手机锁屏和解锁，电量变化等比较频繁的广播事件在清单文件里面配置无效
    // 必须通过代码动态注册才有效,且在activity销毁的时候必须注销
    public void registBroadcast(View view){
        IntentFilter intentFilter = new IntentFilter();
        // 注册屏幕解锁和锁屏广播
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        // 注册电池电量变化
        intentFilter.addAction("android.intent.action.BATTERY_CHANGED");

        registBroadcastIntent = registerReceiver(broadcastRe,intentFilter);

        Toast.makeText(this,"注册锁屏,解锁,电量变化广播成功",Toast.LENGTH_SHORT).show();
        // 进入监听广播页面
        monitorBroadarcast(null);
    }

    // 注销广播
    public void unregistBroadcast(View view){
        if (registBroadcastIntent != null){
            unregisterReceiver(broadcastRe);
            registBroadcastIntent = null;
            Toast.makeText(this,"锁屏,解锁,电量变化广播已注销",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this,"广播已被注销",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销广播
        unregistBroadcast(null);
    }
}
