package com.androidTest.elements;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.AndroidAssistant.IMyAidlInterface;

import com.androidTest.R;

public class ServiceAidlActivity extends AppCompatActivity {

    private MyServiceConn conn;

    private IMyAidlInterface iMyAidlInterface;
    private EditText userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_aidl);

        // 要验证的用户名
        userName = findViewById(R.id.ed_userName);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 解绑服务
        unbindService(conn);
    }

    // 绑定辅助工程中的服务
    public void bindRemoteService(View view){
        // 绑定AndroidAssistant项目里面的服务
        Intent intent = new Intent();
        intent.setAction("com.AndroidAssistant.service");
        intent.setPackage("com.AndroidAssistant");
        conn = new MyServiceConn();
        boolean result = bindService(intent,conn,BIND_AUTO_CREATE);

        if (result){
            // 绑定成功
            Toast.makeText(this,"绑定远程服务成功",Toast.LENGTH_SHORT).show();
        }else {
            // 绑定失败
            Toast.makeText(this,"绑定远程服务失败,请安装AndroidAssistant工程并保证在后台运行",Toast.LENGTH_LONG).show();
        }
    }

    // 调用辅助工程中的服务方法
    public void callMethod(View view){
        if (iMyAidlInterface == null){
            Toast.makeText(this,"请先绑定远程服务",Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(userName.getText().toString())){
            Toast.makeText(this,"请输入要验证的用户名",Toast.LENGTH_SHORT).show();
            return;
        }else {
            try {
                // 远程验证用户名 正确用户名是张三
                boolean result = iMyAidlInterface.callCustomMethod(userName.getText().toString().trim());
                if (result){
                    Toast.makeText(getApplicationContext(),"验证通过，登陆成功",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"验证不通过，登陆失败",Toast.LENGTH_SHORT).show();
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private class MyServiceConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 获取绑定服务以后，服务返回的Binder
            iMyAidlInterface = IMyAidlInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
}
