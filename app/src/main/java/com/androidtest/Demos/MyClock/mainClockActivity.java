package com.androidtest.Demos.MyClock;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TabHost;
import android.widget.Toast;

import helloworld.android.com.androidtest.R;

public class mainClockActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_clock);
        initTabHost();
    }

    // 初始化tabHost
    private void initTabHost(){
        TabHost tabHost = findViewById(R.id.tabHost);
        tabHost.setup();

        // 添加标签
        TabHost.TabSpec timeSpec = tabHost.newTabSpec("tag0");
        timeSpec.setIndicator("时钟");
        timeSpec.setContent(R.id.time);
        tabHost.addTab(timeSpec);

        TabHost.TabSpec alarmSpec = tabHost.newTabSpec("tag1");
        alarmSpec.setIndicator("闹钟");
        alarmSpec.setContent(R.id.alarm);
        tabHost.addTab(alarmSpec);
    }
}
