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
     * 激活设备管理器
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
     * 取消激活设备管理器
     */
    public void unActivateDeviceMgr(){
        if (isActive()){
            devicePolicyManager.removeActiveAdmin(componentName);
        }else {
            Toast.makeText(context,"设备管理器尚未激活,无法取消激活!",Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 获取设备管理器的激活状态
     * @return 已经激活返回true 没有激活返回false
     */
    public boolean isActive(){
        if (devicePolicyManager.isAdminActive(componentName)){
            return true;
        }else {
            return false;
        }
    }
}
