package com.androidtest.elements;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.View;

import helloworld.android.com.androidtest.R;

public class ServiceActivity extends Activity {
    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        intent = new Intent(this,ServiceTest.class);
    }

    // 开启服务查看服务的生命周期
    public void startService(View view){
        Intent intent1 = new Intent(this,ServiceTest.class);
        startService(intent1);
    }

    // 关闭服务查看服务生命周期
    public void stopService(View view){
        Intent intent1 = new Intent(this,ServiceTest.class);
        stopService(intent1);
    }

    // start方式开启服务
    public void startService1(View view){
        startService(intent);
    }

    // stop方式关闭服务
    public void stopService1(View view){
        stopService(intent);
    }

    // 绑定服务
    public void bindService(View view){
        bindService(intent,mySerCon,BIND_AUTO_CREATE);
    }

    // 解绑服务
    public void unBindService(View view){
        unbindService(mySerCon);
    }


    private MyServiceConnection mySerCon = new MyServiceConnection();

    private class MyServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
}

