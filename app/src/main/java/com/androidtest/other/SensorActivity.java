package com.androidtest.other;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import helloworld.android.com.androidtest.R;

public class SensorActivity extends Activity implements SensorEventListener {

    private TextView textView;
    private SensorManager sensorManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        textView = findViewById(R.id.textView);
    }

    // 获取当前设备支持的传感器
    public void click(View view) {
        StringBuilder builder =
                new StringBuilder();
        List<Sensor> sensors = null;
        try {
            sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
            if (sensors == null) {
                Toast.makeText(this, "你的手机不支持任何传感器,赶紧换手机吧..", Toast.LENGTH_LONG).show();
            } else {
                for (Sensor sensor : sensors) {
                    String sensorName = sensorName(sensor.getType());

                    if (sensorName.equals("未知传感器")) {
                        sensorName = sensor.getType() + "";
                    }

                    builder.append(sensorName + "\n");
                }

                textView.setText(builder.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 传感器英文转中文
    private String sensorName(int type) {
        String string = "未知传感器";
        switch (type) {
            case Sensor.TYPE_ACCELEROMETER:
                string = "加速度传感器（硬件）";
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                string = "磁场传感器（硬件）";
                break;
            case Sensor.TYPE_ORIENTATION:
                string = "方向传感器（软件传感器，数据来自重力和磁场传感器）";
                break;
            case Sensor.TYPE_GYROSCOPE:
                string = "陀螺仪传感器（硬件）";
                break;
            case Sensor.TYPE_LIGHT:
                string = "光线传感器（硬件）";
                break;
            case Sensor.TYPE_PRESSURE:
                string = "压力传感器（硬件）";
                break;
            case Sensor.TYPE_PROXIMITY:
                string = "临近传感器（硬件）";
                break;
            case Sensor.TYPE_GRAVITY:
                string = "重场传感器（硬件或软件）";
                break;
            case Sensor.TYPE_LINEAR_ACCELERATION:
                string = "线性加速度传感器（硬件或软件）";
                break;
            case Sensor.TYPE_ROTATION_VECTOR:
                string = "旋转矢量传感器（硬件或软件）";
                break;
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                string = "湿度传感器（硬件）";
                break;
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                string = "温度传感器（硬件）";
                break;
            case Sensor.TYPE_TEMPERATURE:
                string = "温度传感器（硬件），从Android4.0开始被TYPE_AMBIENT_TEMPERATURE取代";
                break;
            case Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED:
                string = "无标定磁场";
                break;
            case Sensor.TYPE_GAME_ROTATION_VECTOR:
                string = "无标定旋转矢量";
                break;
            case Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR:
                string = "地磁旋转矢量";
                break;
        }

        return string;
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 注册要监听的传感器
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY),SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onStop() {
        super.onStop();

        // 注销监听的传感器
        sensorManager.unregisterListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        sensorManager.unregisterListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 注销监听的传感器
        sensorManager.unregisterListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        sensorManager.unregisterListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY));
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // 传感器数据发生改变调用
        switch (event.sensor.getType()){
            case Sensor.TYPE_ACCELEROMETER:
                String text = "加速度x:" + event.values[0] + "y:" + event.values[1] + "z:" + event.values[2];
                Toast.makeText(this,text,Toast.LENGTH_LONG).show();
                break;
            case Sensor.TYPE_GRAVITY:
                String text1 = "重力x:" + event.values[0] + "y:" + event.values[1] + "z:" + event.values[2];
                Toast.makeText(this,text1,Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // 精度发生改变调用

    }
}
