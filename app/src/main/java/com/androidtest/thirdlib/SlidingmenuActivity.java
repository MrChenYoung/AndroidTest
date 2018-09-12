package com.androidtest.thirdlib;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import helloworld.android.com.androidtest.R;

public class SlidingmenuActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_slidingmenu);

        // 创建slidingmenu
        SlidingMenu slidingMenu = new SlidingMenu(this);

        // 设置从屏幕左边滑出
        slidingMenu.setMode(SlidingMenu.LEFT);

        // 设置侧滑栏全屏
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        // 设置侧滑栏宽度
        slidingMenu.setBehindOffsetRes(R.dimen.slidinemenu_width);

        // 设置侧滑栏阴影宽度
        slidingMenu.setShadowWidthRes(R.dimen.slidingmenu_shadow_width);

        // 设置渐进渐出
        slidingMenu.setFadeDegree(0.35f);

        // 绑定到当前activity
        slidingMenu.attachToActivity(this,SlidingMenu.SLIDING_CONTENT);

    }
}
