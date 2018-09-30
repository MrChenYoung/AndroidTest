package com.androidtest.Demos.MyClock;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.IOException;
import java.util.Calendar;

import helloworld.android.com.androidtest.R;

public class AlarmView extends LinearLayout implements View.OnClickListener,AdapterView.OnItemLongClickListener {

    private final String localAlarmData = "alarmData";
    private final String alarmDataKey = "alarmKey";
    private Context context;
    private ListView listView;
    private ArrayAdapter<AlarmData> adapter;

    public AlarmView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    // 布局加载完成
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        listView = findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(context,android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(this);

        // 获取已经存储的数据
        readAlarmData();

        Button button = findViewById(R.id.addAlarmBtn);
        button.setOnClickListener(this);
    }

    // 添加闹钟
    @Override
    public void onClick(View v) {
        final Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendar1.set(Calendar.MINUTE,minute);
                calendar1.set(Calendar.SECOND,0);
                calendar1.set(Calendar.MILLISECOND,0);
                setAlarm(new AlarmData(calendar1.getTimeInMillis()));
            }
        },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true).show();
    }

    // 长按删除闹钟
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        new AlertDialog.Builder(context).setTitle("提示").setMessage("确定删除闹钟吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 删除本地存储的闹钟
                deleteAlarm(position);

                // 移除闹钟设置
                removeAlarm(position);

                // 删除列表数据
                adapter.remove(adapter.getItem(position));
            }
        }).setNegativeButton("取消",null).show();

        return true;
    }

    // 删除本地存储闹钟
    private void deleteAlarm(int position){
        AlarmData data = adapter.getItem(position);

        // 删除本地存储数据
        SharedPreferences sp = context.getSharedPreferences(localAlarmData,Context.MODE_PRIVATE);
        String[] strings = sp.getString(alarmDataKey,"").split("&");

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < strings.length; i++) {
            if (!String.valueOf(data.getTimeMillon()).equals(strings[i]) && !TextUtils.isEmpty(strings[i])){
                stringBuilder.append(strings[i] + "&");
            }
        }

        SharedPreferences.Editor editor = sp.edit();
        editor.putString(alarmDataKey,stringBuilder.toString());
        editor.apply();
    }

    // 移除闹钟设置
    private void removeAlarm(int position){
        AlarmData data = adapter.getItem(position);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,(int)data.getTimeMillon(),new Intent(context,AlarmReceiver.class),0);
        alarmManager.cancel(pendingIntent);
    }

    // 设置闹钟
    private void setAlarm(AlarmData alarm){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // 设置
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,(int)alarm.getTimeMillon(),new Intent(context,AlarmReceiver.class),0);
        // 重复闹钟
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,alarm.getTimeMillon(),5*60*1000,pendingIntent);
        // 设置一次性闹钟
        alarmManager.set(AlarmManager.RTC_WAKEUP,alarm.getTimeMillon(),pendingIntent);

        // 添加数据到列表
        adapter.add(alarm);

        // 保存数据到硬盘
        saveAlarmData(alarm);
    }

    // 保存闹钟数据
    private void saveAlarmData(AlarmData alarmData){
        SharedPreferences sp = context.getSharedPreferences(localAlarmData,Context.MODE_PRIVATE);
        StringBuffer stringBuffer = new StringBuffer(sp.getString(alarmDataKey,""));
        stringBuffer.append("&" + String.valueOf(alarmData.getTimeMillon()));

        SharedPreferences.Editor editor = sp.edit();
        editor.putString(alarmDataKey,stringBuffer.toString());
        editor.apply();
    }

    // 读取闹钟数据
    private void readAlarmData(){
        try {
            SharedPreferences sp = context.getSharedPreferences(localAlarmData,Context.MODE_PRIVATE);
            String str = sp.getString(alarmDataKey,"");
            String[] arr = str.split("&");
            for (int i = 0; i < arr.length; i++) {
                if (!TextUtils.isEmpty(arr[i])){
                    long mi = Long.parseLong(arr[i]);
                    adapter.add(new AlarmData(mi));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // 闹钟数据
    private class AlarmData {
        private long timeMillon;

        public AlarmData(long million){
            timeMillon = million;
        }

        public long getTimeMillon() {
            return timeMillon;
        }

        @Override
        public String toString() {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timeMillon);

            // 年月日
            String year = String.valueOf(calendar.get(Calendar.YEAR));
            String month = String.format("%02d",calendar.get(Calendar.MONTH) + 1);
            String day = String.format("%02d",calendar.get(Calendar.DAY_OF_MONTH));
            String pre = year + "年" + month + "月" + day + "日 ";

            // 时分秒
            String hour = String.format("%02d",calendar.get(Calendar.HOUR_OF_DAY));
            String minute = String.format("%02d",calendar.get(Calendar.MINUTE));
            String second = String.format("%02d",calendar.get(Calendar.SECOND));
            String sub = hour + ":" + minute + ":" + second;

            return pre + sub;
        }
    }
}
