package com.androidTest.elements;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.androidTest.R;

public class ServiceActivity extends Activity {

    // 开启或绑定服务
    private Intent intent;

    // 绑定服务以后返回的binder 为了调用服务里面的方法
    private ServiceTest.MyBinder binder;
    private ServiceTest.ServiceInterface interfaceBinder;

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

    // 服务中注册锁屏和解锁广播接收者
    // 目的是为了能够一直监听锁屏和解锁
    public void registReceiver(View view){
        // 开启服务
        startService(new Intent(this,ServiceTest.class));
    }

    // 绑定服务
    public void bindService(View view){
        bindService(intent,mySerCon,BIND_AUTO_CREATE);
    }

    // 解绑服务
    // activity销毁必须解绑服务，否则会报错
    // 重复调用解绑服务也会报错
    public void unBindService(View view){
        if (binder != null || interfaceBinder != null)
        unbindService(mySerCon);
        binder = null;
        interfaceBinder = null;
    }

    // 通过绑定服务调用服务里面的方法
    public void callServiceMethod1(View view){
        // 先判断是否绑定了服务
        if (binder == null){
            Toast.makeText(this,"请先绑定服务",Toast.LENGTH_SHORT).show();
        }else {
            // 调用服务里面的方法
            binder.callServiceMethod("通过绑定服务调用服务里面的方法");
        }
    }

    // 通过接口方式调用服务里面的方法前要先通过这种方式绑定服务
    public void bindInteService(View view){
        if (binder != null){
            unBindService(null);
        }
        Intent inten = new Intent(this,ServiceTest.class);
        inten.putExtra("name","bind2");
        bindService(inten,mySerCon,BIND_AUTO_CREATE);
    }

    // 通过接口方式调用服务里面的方法
    public void callServiceMethod2(View view){
        if (interfaceBinder == null){
            Toast.makeText(this,"请先绑定服务",Toast.LENGTH_SHORT).show();
        }else {
            // 调用服务里面的方法
            interfaceBinder.callMyMethod("通过接口方式调用服务里面的方法");
        }
    }

    // 进入aidl页面
    public void toAidl(View view){
        Intent myIntent = new Intent(this,ServiceAidlActivity.class);
        startActivity(myIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 解绑服务
        unBindService(null);
    }

    // 绑定服务需要的参数
    private MyServiceConnection mySerCon = new MyServiceConnection();

    private class MyServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 绑定服务成功,如果服务的onBind方法返回空,该方法不会执行
            Toast.makeText(ServiceActivity.this,"调用了onServiceConnected方法",Toast.LENGTH_SHORT).show();


            if (service.getClass().equals(ServiceTest.MyBinder.class)){
                // 获取绑定服务以后，服务返回的Binder，为了调用服务里面的方法
                binder = (ServiceTest.MyBinder) service;
            }else {
                interfaceBinder = (ServiceTest.ServiceInterface)service;
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // 解绑服务成功,如果服务的onBind方法返回空,该方法不会执行
            Toast.makeText(ServiceActivity.this,"调用了onServiceDisconnected方法",Toast.LENGTH_SHORT).show();
        }
    }
}

