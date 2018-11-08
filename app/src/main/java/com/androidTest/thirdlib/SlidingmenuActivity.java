package com.androidTest.thirdlib;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import com.androidTest.R;

public class SlidingmenuActivity extends Activity {

    private SlidingMenu slidingMenu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sliding_main_menu);

        // 创建slidingmenu
        slidingMenu = new SlidingMenu(this);

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

        // 设置菜单内容
        slidingMenu.setMenu(R.layout.activity_sliding_menu);
    }

    // 点击按钮显示slidingmenu
    public void showSlidingmenu(View view){
        slidingMenu.toggle();
    }

    // 点击手机菜单键弹出菜单

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU){
            slidingMenu.toggle(true);
        }

        return super.onKeyDown(keyCode, event);
    }
}
