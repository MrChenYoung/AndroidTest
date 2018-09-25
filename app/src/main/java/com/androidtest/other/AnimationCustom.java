package com.androidtest.other;

import android.view.animation.Animation;
import android.view.animation.Transformation;

public class AnimationCustom extends Animation {
    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);

        // 动画执行以前调用，可以获取动画控件的大小
    }

    // 动画过程中持续执行的方法
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        // interpolatedTime的值从0到1持续变化

        // 左右摇摆
        // 30个周期
        // 50像素的振幅
        t.getMatrix().setTranslate((float) Math.sin(interpolatedTime * 30) * 50,0);

    }
}
