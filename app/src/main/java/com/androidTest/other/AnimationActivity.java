package com.androidTest.other;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.androidTest.R;

public class AnimationActivity extends Activity {

    private LinearLayout rootView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        // 获取布局
        rootView = findViewById(R.id.linearLayout);

        // 为当前布局设置动画
        layoutAnimation();
    }

    // 补间动画
    public void tweenAnimation(View view){
        Intent intent = new Intent(this,AnimationTweenActivity.class);
        startActivity(intent);
    }

    // 属性动画
    public void propertyAnimation(View view){
        Intent intent = new Intent(this,AnimationPropertyActivity.class);
        startActivity(intent);
    }

    // 帧动画
    public void frameAnimation(View view){
        Intent intent = new Intent(this,AnimationFrameActivity.class);
        startActivity(intent);
    }

    // 自定义动画
    public void customAnimation(View view){
        AnimationCustom animationCustom = new AnimationCustom();
        animationCustom.setDuration(1000);
        view.startAnimation(animationCustom);
    }

    // 布局动画
    public void layoutAnimation(){

        ScaleAnimation scaleAnimation = new ScaleAnimation(0,1,0,1);
        scaleAnimation.setDuration(2000);

        // 0.5s延迟
        LayoutAnimationController layoutAnimationController = new LayoutAnimationController(scaleAnimation,0.5f);
        // 动画顺序,ORDER_NORMAL默认顺序从上往下,ORDER_RANDOM随机顺序,ORDER_REVERSE从下往上
//        layoutAnimationController.setOrder(LayoutAnimationController.ORDER_REVERSE);
        rootView.setLayoutAnimation(layoutAnimationController);
    }

    // 布局内容改变动画,设置布局animateLayoutChanges属性为true即可
    public void layoutChangeAnimation(View view){
        // 添加一个按钮
        Button btn = new Button(this);
        btn.setText("点击删除");
        btn.setOnClickListener(myClickListener);
        rootView.addView(btn);

        // 自定义布局内容改变时候的动画
//        TranslateAnimation translateAnimation = new TranslateAnimation(0,-200,0,0);
//        translateAnimation.setDuration(1000);
//
//        LayoutAnimationController animationController = new LayoutAnimationController(translateAnimation,0);
//
//        rootView.setLayoutAnimation(animationController);
    }

    private View.OnClickListener myClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            rootView.removeView(v);
        }
    };

    // 给ListView添加动画
    public void addAnimationToListView(View view){
        Intent intent = new Intent();
        intent.setClass(this,AnimationListView.class);
        startActivity(intent);
    }
}
