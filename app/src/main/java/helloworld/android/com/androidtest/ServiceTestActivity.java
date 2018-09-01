package helloworld.android.com.androidtest;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class ServiceTestActivity extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(this,"服务绑定",Toast.LENGTH_SHORT).show();

        Binder binder = new Binder();
        return binder;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this,"服务开启",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this,"服务关闭",Toast.LENGTH_SHORT).show();
    }
}
