package com.androidtest.elements;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import helloworld.android.com.androidtest.R;

public class ActivityHomeActivity extends AppCompatActivity {

    @Override // activity创建的时候调用
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_home);

    }

    // 进入activity生命周期介绍页面
    public void activityLifeCycle(View view){
        Intent intent = new Intent(this,ActivityLifeCycleActivity.class);
        startActivity(intent);
    }

    // 进入activity四种启动模式
    public void activityLaunchModel(View view){
        Intent intent = new Intent(this,ActivityLaunchModelActivity.class);
        startActivity(intent);
    }
}
