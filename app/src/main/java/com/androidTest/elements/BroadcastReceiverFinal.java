package com.androidTest.elements;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

// 最终的接收者 不需要配置
public class BroadcastReceiverFinal extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // 获取广播携带内容
        String content = getResultData();
        Toast.makeText(context,"最终接收到有序广播:" + content,Toast.LENGTH_SHORT).show();
    }
}
