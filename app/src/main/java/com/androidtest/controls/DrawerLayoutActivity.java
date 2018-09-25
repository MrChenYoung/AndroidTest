package com.androidtest.controls;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import helloworld.android.com.androidtest.R;

public class DrawerLayoutActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    Button btn, btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_layout);

        drawerLayout = (DrawerLayout) findViewById(R.id.draw);
        btn = (Button) findViewById(R.id.btn);
        btn2 = (Button) findViewById(R.id.btn2);
        //实现方法一：定义好xml文件后手指侧滑就可以拉出侧滑界面了
        //实现方法二：定义好xml文件后，在java文件中添加点击事件也可以拉出侧滑菜单，代码如下：




//        点击主界面中的按钮弹出侧滑界面
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

//        点击侧滑界面中的按钮缩回侧滑界面
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(Gravity.LEFT);
            }
        });
    }
}
