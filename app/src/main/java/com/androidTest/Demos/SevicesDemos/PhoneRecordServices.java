package com.androidTest.Demos.SevicesDemos;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import java.io.File;
import java.io.IOException;

public class PhoneRecordServices extends Service {

    // 录音器
    private PhoneRecordTools phoneRecorder;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // 获取电话管理者
        TelephonyManager manager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        // 监听电话状态
        manager.listen(new MyPhoneListener(),PhoneStateListener.LISTEN_CALL_STATE);
    }



    private class MyPhoneListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String phoneNumber) {
            super.onCallStateChanged(state, phoneNumber);

            switch (state){
                case TelephonyManager.CALL_STATE_IDLE:
                    // 空闲状态
                    if (phoneRecorder != null){
                        // 录音完成
                        phoneRecorder.stopRecord();
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    // 接听,因为打电话给别人的时候也会执行该代码，所以判断一下mediaRecorder是否为空,开始录音
                    if (phoneRecorder != null){
                        phoneRecorder.startRecord();
                    }
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    // 响铃状态,准备录音机
                    if (phoneRecorder == null){
                        // 初始化录音器
                        phoneRecorder = new PhoneRecordTools(PhoneRecordServices.this);
                    }

                    // 创建文件
                    File outputPath = new File(PhoneRecordActivity.baseOutputPath,System.currentTimeMillis() + ".m4a");
                    if (!outputPath.exists()){
                        try {
                            outputPath.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    // 设置输出路径
                    phoneRecorder.setOutputFile(outputPath.getAbsolutePath());
                    try {
                        // 准备录音
                        phoneRecorder.prepareRecord();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }
    }
}
