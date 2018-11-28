package com.utiles;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.androidTest.other.DeviceReceiver;

public class DeviceManager {
    // 设置当前类为单利
    private static DeviceManager deviceInstance;
    // 设备管理器状态
    private boolean isActive;

    // 上下文
    private Context context;
    private final DevicePolicyManager devicePolicyManager;
    private final ComponentName componentName;

    /**
     * 私有构造方法
     * @param context
     */
    private DeviceManager(Context context) {
        this.context = context;

        // 获取设备管理服务
        devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(context,DeviceReceiver.class);
        isActive = devicePolicyManager.isAdminActive(componentName);
    }

    /**
     * 获取单利实例
     * @param context 上线文
     * @return
     */
    public static DeviceManager getDeviceInstance(Context context){
        if (deviceInstance == null){
            synchronized (DeviceManager.class){
                if (deviceInstance == null){
                    deviceInstance = new DeviceManager(context);
                }
            }
        }

        return deviceInstance;
    }


    /**
     * 激活设备管理器,获取管理员权限
     */
    public void activateDeviceMgr(){
        if (isActive()){
            Toast.makeText(context,"设备管理器已经激活,不可重复激活",Toast.LENGTH_SHORT).show();
        }else {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,componentName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"激活提示文字");
            context.startActivity(intent);
        }
    }

    /**
     * 取消激活设备管理器,取消管理员权限(卸载当前应用前要取消激活，否则无法卸载)
     */
    public void unActivateDeviceMgr(){
        if (isActive()){
            devicePolicyManager.removeActiveAdmin(componentName);
        }else {
            Toast.makeText(context,"设备管理器尚未激活,无法取消激活!",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 设置解锁方式(不需要激活设备)
     */
    public void setLockMethod(){
        Intent intent = new Intent(DevicePolicyManager.ACTION_SET_NEW_PASSWORD);
        context.startActivity(intent);
    }

    /**
     * 设置解锁方式(需要先激活设备管理器)
     */
    public void setLockMethodPwd(){
        if (isActive){
            // PASSWORD_QUALITY_ALPHABETIC
            // 用户输入的密码必须要有字母（或者其他字符）。
            // PASSWORD_QUALITY_ALPHANUMERIC
            // 用户输入的密码必须要有字母和数字。
            // PASSWORD_QUALITY_NUMERIC
            // 用户输入的密码必须要有数字
            // PASSWORD_QUALITY_SOMETHING
            // 由设计人员决定的。
            // PASSWORD_QUALITY_UNSPECIFIED
            // 对密码没有要求。
            Intent intent = new Intent(DevicePolicyManager.ACTION_SET_NEW_PASSWORD);
            devicePolicyManager.setPasswordQuality(componentName,DevicePolicyManager.PASSWORD_QUALITY_NUMERIC);
            context.startActivity(intent);
        }else {
            Toast.makeText(context,"请先激活设备",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 给手机设置解锁密码
     * @param password 解锁密码
     */
    public void setPassWord(String password){
        if (isActive){
            if (password.length() < 4){
                Toast.makeText(context,"密码至少是四个字符",Toast.LENGTH_SHORT).show();
            }else {
                devicePolicyManager.resetPassword(password,DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY);
            }
        }else {
            Toast.makeText(context,"请先激活设备",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 立即锁屏
     */
    public void lockPhoneNow(){
        if (isActive){
            devicePolicyManager.lockNow();
        }else {
            Toast.makeText(context,"请先激活设备",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 设置延迟delayTime时间后锁屏
     * @param delayTime 延迟时长
     */
    public void lockPhoneDelay(long delayTime){
        if (isActive){
            devicePolicyManager.setMaximumTimeToLock(componentName,delayTime);
        }else {
            Toast.makeText(context,"请先激活设备",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 清空sd卡上的数据
     */
    public void wipeExternalData(){
        if (isActive){
            devicePolicyManager.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
        }else {
            Toast.makeText(context,"请先激活设备",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 清空内部存储卡数据(恢复出厂设置)
     */
    public void wipeData(){
        if (isActive){
            devicePolicyManager.wipeData(0);
        }else {
            Toast.makeText(context,"请先激活设备",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取设备管理器的激活状态(通过这种方式获取状态可能不准确,所以用单例里面的属性来记录比较准确)
     * @return 已经激活返回true 没有激活返回false
     */
//    public boolean isActive(){
//        return devicePolicyManager.isAdminActive(componentName);
//    }


    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
