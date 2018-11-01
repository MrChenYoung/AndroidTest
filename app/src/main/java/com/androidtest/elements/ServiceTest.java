package com.androidtest.elements;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class ServiceTest extends Service {

    private BroadcastReceiverMonitor broadcastReceiverMonitor;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(this,"调用了服务的onBind方法",Toast.LENGTH_SHORT).show();

        String name = intent.getStringExtra("name");
        if (name != null && name.equals("bind2")){
            return new MyBinder2();
        }else {
            return new MyBinder();
        }
    }


    @Override // 服务被创建的时候调用
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this,"调用了服务的onCreate方法",Toast.LENGTH_SHORT).show();

        // 注册锁屏和解锁广播接收者
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        broadcastReceiverMonitor = new BroadcastReceiverMonitor();
        registerReceiver(broadcastReceiverMonitor,intentFilter);
    }

    @Override // 每次调用startSevice方法的时候调用
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this,"调用了服务的onStartCommand方法",Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override // 服务被销毁的时候调用
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this,"调用了服务的onDestroy方法",Toast.LENGTH_SHORT).show();

        // 注销锁屏和屏幕解锁广播接收者
        unregisterReceiver(broadcastReceiverMonitor);
    }

    // 服务里面的方法，供外部调用
    public void toCallMethod(String message){
        Toast.makeText(getApplicationContext(),"调用了服务内部的方法:" + message,Toast.LENGTH_SHORT).show();
    }

    // 通过接口方式调用服务里面的方法
    public void callMethod(String message){
        Toast.makeText(getApplicationContext(),"调用了服务内部的方法:" + message,Toast.LENGTH_SHORT).show();
    }

    // 自定义Binder，为了调用服务内部的方法
    public class MyBinder extends Binder{
        public void callServiceMethod(String message){
            toCallMethod(message);
        }
    }

    // 通过接口方式调用服务里面的方法
    private class MyBinder2 extends Binder implements ServiceInterface{
        public void callMyMethod(String message){
            callMethod(message);
        }
    }

    // 通过这个接口调用服务里面的方法,可以保留服务里不想暴露的方法
    public interface ServiceInterface {
        public void callMyMethod(String message);
    }

}
