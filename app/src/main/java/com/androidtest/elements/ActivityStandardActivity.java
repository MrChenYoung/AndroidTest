package com.androidtest.elements;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import helloworld.android.com.androidtest.R;

public class ActivityStandardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_standard_model);
    }

    // 启动标准模式activity
    public void openNewActivity(View view){
        Intent intent = new Intent(this,ActivityStandardActivity.class);
        startActivity(intent);
    }

    // 启动测试activity
    public void openTestActivity(View view){
        Intent intent = new Intent(this,ActivityLaunchModelTestActivity.class);
        intent.putExtra(ActivityLaunchModelTestActivity.LAUNCHMODELKEY,ActivityLaunchModelTestActivity.LAUNCHMODELSTANDAED);
        startActivity(intent);
    }
}
