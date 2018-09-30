package com.androidtest.Demos.MyClock;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.view.Window;
import android.view.WindowManager;

public class AlarmReceiverActivity extends Activity {

    private MediaPlayer mp;
    private Vibrator vibrator;
    private PowerManager.WakeLock wakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 设置息屏状态下能正常弹出dialog
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.flags = WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON;

        // 不播放闹钟铃声
        playMusic();

        // 手机震动
        startVibrator();

        // 弹出提示框
        shoeAlert();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 唤醒屏幕
        if (wakeLock == null){
            PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
            wakeLock = powerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK,this.getClass().getCanonicalName());
            wakeLock.acquire();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // 息屏
        if (wakeLock != null && wakeLock.isHeld()){
            wakeLock.release();
            wakeLock = null;
        }
    }

    // 播放音乐
    private void playMusic(){
        try {
            if (mp == null){
                mp = new MediaPlayer();
            }
            mp.setDataSource(this,RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 开启手机震动
    private void startVibrator(){
        if (vibrator == null){
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            // 停止 开启  停止  开启
            long[] pattern = {500,1000,500,1000};
            vibrator.vibrate(pattern,0);
        }
    }

    // 弹出提示框
    private void shoeAlert(){
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("闹钟来了")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 关闭铃声
                        if (mp != null){
                            mp.stop();
                            mp.release();
                            mp = null;
                        }

                        // 停止震动
                        if (vibrator != null){
                            vibrator.cancel();
                            vibrator = null;
                        }

                        // 销毁当前activity
                        finish();
                    }
                })
                .show();
    }
}
