package com.androidtest.elements;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import helloworld.android.com.androidtest.R;

public class ServiceAidlActivity extends AppCompatActivity {

    private MyServiceConn conn;

//    private IMyAidlInterface iMyAidlInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_aidl);

        // 绑定AndroidAssistant项目里面的服务
        Intent intent = new Intent();
        intent.setAction("com.androidassistant.service");
        intent.setPackage("com.androidassistant");
        conn = new MyServiceConn();
        bindService(intent,conn,BIND_AUTO_CREATE);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 解绑服务
        unbindService(conn);
    }

    // 调用辅助工程中的服务方法
    public void callMethod(View view){
//        try {
//            boolean result = iMyAidlInterface.callCustomMethod("张三");
//        } catch (RemoteException e) {
//            Log.e("tag","===============");
//            e.printStackTrace();
//            Log.e("tag","===============");
//        }
    }

    private class MyServiceConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 获取绑定服务以后，服务返回的Binder
//            iMyAidlInterface = I.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
}
