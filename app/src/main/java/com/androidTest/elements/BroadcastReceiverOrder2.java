package com.androidTest.elements;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BroadcastReceiverOrder2 extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        // 可以终止广播
        abortBroadcast();

        // 获取广播携带内容
        String content = getResultData();
        Toast.makeText(context,"第二级接收到有序广播:" + content,Toast.LENGTH_SHORT).show();

        // 修改广播内容
        setResultData("有序广播数据:" + 300);
    }
}
