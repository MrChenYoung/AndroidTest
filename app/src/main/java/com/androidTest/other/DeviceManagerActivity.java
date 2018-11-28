package com.androidTest.other;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.androidTest.R;
import com.utiles.DeviceManager;

import java.lang.ref.WeakReference;

public class DeviceManagerActivity extends AppCompatActivity {

    public static WeakReference<DeviceManagerActivity> mInstance;
    private DeviceManager deviceManager;

    // 激活设备管理器按钮
    private Button btn_active;
    private Button btn_set_lock_pwd;
    private Button btn_set_pwd;
    private Button btn_lock_now;
    private Button btn_lock_delay;
    private Button btn_wipe_external_data;
    private Button btn_wipe_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_manager);

        // 获取设备管理器
        deviceManager = DeviceManager.getDeviceInstance(this);

        // 初始化界面
        initUI();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 保留当前activity，方便在DeviceReceiver类中获取，来更新当前界面
        if (mInstance == null){
            mInstance = new WeakReference<>(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 置空 防止内存泄漏
        if (mInstance != null){
            mInstance = null;
        }
    }

    /**
     * 初始化界面
     */
    private void initUI(){
        // 设置激活按钮
        btn_active = findViewById(R.id.btn_active);
        // 设置解锁方式（带设置密码）
        btn_set_lock_pwd = findViewById(R.id.btn_set_lock_pwd);
        // 设置解锁密码
        btn_set_pwd = findViewById(R.id.btn_set_pwd);
        // 立即锁屏按钮
        btn_lock_now = findViewById(R.id.btn_lock_now);
        //5秒后锁屏
        btn_lock_delay = findViewById(R.id.btn_lock_delay);
        // 清空sd卡上的所有内容
        btn_wipe_external_data = findViewById(R.id.btn_wipe_external_data);
        // 清空内部存储卡上的内容
        btn_wipe_data = findViewById(R.id.btn_wipe_data);

        // 更新UI
        updateUI();
    }

    /**
     * 更新界面
     */
    public void updateUI(){
        boolean active = deviceManager.isActive();
        if (active){
            btn_active.setText("取消激活设备管理器");
        }else {
            btn_active.setText("激活设备管理器");
        }

        btn_set_lock_pwd.setEnabled(active);
        btn_set_pwd.setEnabled(active);
        btn_lock_now.setEnabled(active);
        btn_lock_delay.setEnabled(active);
        btn_wipe_external_data.setEnabled(active);
        btn_wipe_data.setEnabled(active);
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

    /**
     * 设置解锁方式(不需要激活设备管理器)
     * @param view
     */
    public void setLockMethod(View view){
        deviceManager.setLockMethod();
    }

    /**
     * 设置解锁方式(需要设置设备管理器)
     * @param view
     */
    public void setLockMethodPwd(View view){
        deviceManager.setLockMethodPwd();
    }

    /**
     * 设置密码
     * @param view
     */
    public void setLockPwd(View view){
        deviceManager.setPassWord("1234");
    }

    /**
     * 立即锁屏
     * @param view
     */
    public void lockPhone(View view){
        deviceManager.lockPhoneNow();
    }

    /**
     * 设置5秒后锁屏
     * @param view
     */
    public void lockPhoneDelay(View view){
        deviceManager.lockPhoneDelay(5000);
    }

    /**
     * 清空sd卡数据
     * @param view
     */
    public void wipeExternalData(View view){
        new AlertDialog.Builder(this)
                .setTitle("温馨提示")
                .setMessage("确定要清空sd卡上的所有数据吗?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 清空数据
                        deviceManager.wipeExternalData();
                    }
                })
                .setNegativeButton("取消",null)
                .show();
    }

    /**
     * 清空数据,相当于恢复出厂设置(真机慎用)
     * @param view
     */
    public void wipeData(View view){
        new AlertDialog.Builder(this)
                .setTitle("温馨提示")
                .setMessage("清空数据将会删除手机上的所有数据，将手机恢复出厂设置,您确定要这样做吗?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 清空数据
                        deviceManager.wipeData();
                    }
                })
                .setNegativeButton("取消",null)
                .show();
    }
}
