package com.androidTest.other;

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

import com.androidTest.R;

public class NotificationActivity extends Activity {

    private NotificationManager notificationMgr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        // 获取到通知管理者
        notificationMgr = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    // 发送通知
    public void sendNotification(View view){

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

        // 消息到来震动
        notification.defaults = Notification.DEFAULT_ALL;

        // 设置消息不可清除
        notification.flags = Notification.FLAG_NO_CLEAR;

        // 发送通知
        notificationMgr.notify(1,notification);
    }

    // 取消通知
    public void cancelNotification(View view){
        notificationMgr.cancel(1);
    }
}
