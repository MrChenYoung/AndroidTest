package helloworld.android.com.androidtest;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
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
        build.setSmallIcon(android.R.drawable.sym_def_app_icon);
        build.setContentTitle("通知");
        build.setContentText("消息内容");
        build.setTicker("来消息啦...");
        Notification notification = build.build();
        notificationMgr.notify(1,notification);
    }
}
