package helloworld.android.com.androidtest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BroadcastReceiverTest extends BroadcastReceiver {

    public static final String ACTION = "action";

    @Override
    public void onReceive(Context context, Intent intent) {

        // 获取接收到的广播内容
        String content = intent.getStringExtra("content");

        Toast.makeText(context,"接收到广播:" + content,Toast.LENGTH_LONG).show();
    }
}
