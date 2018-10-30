package com.androidtest.elements;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import helloworld.android.com.androidtest.R;

public class ActivityLaunchModelActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_launch_model);
    }

    // 标准启动模式
    /*
     * 定义: 开启标准activity的时候，不会检查任务栈是否已经有该类型的activity实例,直接在任务栈中加入新的activity对象
     */
    public void standardModel(View view){
        Intent intent = new Intent(this,ActivityStandardActivity.class);
        startActivity(intent);
    }

    // singleTop模式
    /*
     * 定义:开启singleTop模式activity的时候，会检查任务栈顶部的activity实例,
     *      如果顶部就是该类型的activity实例,会复用该顶部activity实例,如果不是,
     *      会新创建一个activity实例并加入任务栈
     * 应用场景: android浏览器的标签页面
     */
    public void singleTopModel(View view){
        Intent intent = new Intent(this,ActivitySingleTopActivity.class);
        startActivity(intent);
    }

    // singleTask模式
    /*
     * 定义: 开启singleTask模式的activity的时候，
     *      会检查任务栈中是否已经存在了该类型的activity实例,
     *      如果存在,复用该activity实例,并清除任务栈中该activity实例上面所有的activity实例
     * 应用场景: android浏览器的首页
     */
    public void singleTaskModel(View view){
        Intent intent = new Intent(this,ActivitySingleTaskActivity.class);
        startActivity(intent);
    }

    // singleInstance模式
    /*
     * 定义: 开启singleInstance模式activity的时候，会检查开启一个新的任务栈,
     *      并把新创建的activity实例放进去,注意新创建的任务栈里面只能有一个activity实例
     * 应用场景: android手机来电界面
     */
    public void singleInstanceModel(View view){
        Intent intent = new Intent(this,ActivitySingleInstanceActivity.class);
        startActivity(intent);
    }
}
