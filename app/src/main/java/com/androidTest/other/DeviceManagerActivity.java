package com.androidTest.other;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.androidTest.R;
import com.utiles.DeviceManager;

public class DeviceManagerActivity extends AppCompatActivity {

    private DeviceManager deviceManager;

    // 激活设备管理器按钮
    private Button bt_active;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_manager);

        // 获取设备管理器
        deviceManager = DeviceManager.getDeviceInstance(this);

        // 初始化界面
        initUI();
    }

    /**
     * 初始化界面
     */
    private void initUI(){
        // 设置激活按钮
        bt_active = findViewById(R.id.bt_active);
        if (deviceManager.isActive()){
            bt_active.setText("取消激活设备管理器");
        }else {
            bt_active.setText("激活设备管理器");
        }
    }

    /**
     * 激活/取消激活设备管理器
     * @param view
     */
    public void activeDeviceMgr(View view){
        if (deviceManager.isActive()){
            // 取消激活
            deviceManager.unActivateDeviceMgr();
        }else {
            // 激活
            deviceManager.activateDeviceMgr();
        }
    }


}
