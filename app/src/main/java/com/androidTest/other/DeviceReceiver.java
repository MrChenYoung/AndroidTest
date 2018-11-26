package com.androidTest.other;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;
import android.util.Log;
import android.widget.Toast;

import com.androidTest.main.App;
import com.utiles.ActivityUtil;
import com.utiles.DeviceManager;

import java.lang.ref.WeakReference;

public class DeviceReceiver extends DeviceAdminReceiver implements DeviceCallback {

    @Override
    public void onEnabled(Context context, Intent intent) {
        super.onEnabled(context, intent);

        // 设备被激活
        updateUI();

        Toast.makeText(context,"设备可用",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        super.onDisabled(context, intent);

        // 设备取消激活
        updateUI();

        Toast.makeText(context,"设备不可用",Toast.LENGTH_LONG).show();
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {

        return "这是一个可选的消息，警告有关禁止用户的请求";
    }

    @Override
    public void onPasswordChanged(Context context, Intent intent, UserHandle user) {
        super.onPasswordChanged(context, intent, user);
        Toast.makeText(context,"密码已变更",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPasswordFailed(Context context, Intent intent, UserHandle user) {
        super.onPasswordFailed(context, intent, user);
        Toast.makeText(context,"修改密码失败",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPasswordSucceeded(Context context, Intent intent, UserHandle user) {
        super.onPasswordSucceeded(context, intent, user);
        Toast.makeText(context,"修改密码成功",Toast.LENGTH_LONG).show();
    }


    @Override
    public void deviceActive(Context context) {
        Toast.makeText(context,"设备激活了=====",Toast.LENGTH_LONG).show();
    }

    // 更新DeviceManagerActivity里面的内容
    public void updateUI(){
        // 获取DeviceManagerActivity对象
        WeakReference<DeviceManagerActivity> deviceActivity = DeviceManagerActivity.mInstance;
        DeviceManagerActivity activity = deviceActivity.get();
        activity.updateUI();
    }

}
