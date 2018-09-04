package helloworld.android.com.androidtest;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.view.View;

public class NotificationActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

    }

    // 发送通知
    public void sendNotification(View view){

        NotificationManager notificationMgr = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder build = new NotificationCompat.Builder(this);

        // 设置小图标
        build.setSmallIcon(android.R.mipmap.sym_def_app_icon);

        // 设置通知标题
        build.setContentTitle("通知");

        // 设置消息内容
        build.setContentText("消息内容");

        // 设置通知来时显示的提示信息
        build.setTicker("来消息啦...");

        // 设置intent
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.baidu.com"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);
        build.setContentIntent(pendingIntent);

        // 创建通知
        Notification notification = build.build();

        // 发送通知
        notificationMgr.notify(1,notification);
    }
}
