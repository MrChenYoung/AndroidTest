package com.androidTest.elements;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.androidTest.R;

public class ActivitySingleTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_single_task_model);
    }

    // 启动singleTop模式activity
    public void openNewActivity(View view){
        Intent intent = new Intent(this,ActivitySingleTaskActivity.class);
        startActivity(intent);
    }

    // 启动测试activity
    public void openTestActivity(View view){
        Intent intent = new Intent(this,ActivityLaunchModelTestActivity.class);
        intent.putExtra(ActivityLaunchModelTestActivity.LAUNCHMODELKEY,ActivityLaunchModelTestActivity.LAUNCHMODELSINGLETASK);
        startActivity(intent);
    }
}
