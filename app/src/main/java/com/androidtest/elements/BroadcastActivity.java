package com.androidtest.elements;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import helloworld.android.com.androidtest.R;

public class BroadcastActivity extends Activity {
    private BroadcastReceiverTest broadcastRe = new BroadcastReceiverTest();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast);

    }

    // 注册广播
    public void registBroadcast(View view){
        registerReceiver(broadcastRe,new IntentFilter(BroadcastReceiverTest.ACTION));
    }

    // 注销广播
    public void unRegistBroadcast(View view){
        unregisterReceiver(broadcastRe);
    }

    // 发送广播
    public void sendBroadcast(View view){
        // 发送广播内容设置
        EditText editText = findViewById(R.id.editText);
        String content = editText.getText().toString();

        // 发送广播
        Intent intent = new Intent(BroadcastReceiverTest.ACTION);
        intent.putExtra("content",content);
        sendBroadcast(intent);
    }
}
