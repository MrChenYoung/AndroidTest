package com.androidtest.elements;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class ServiceTest extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(this,"服务绑定",Toast.LENGTH_SHORT).show();

        Binder binder = new Binder();
        return binder;
    }


    @Override // 服务被创建的时候调用
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this,"调用了服务的onCreate方法",Toast.LENGTH_SHORT).show();
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
    }
}
