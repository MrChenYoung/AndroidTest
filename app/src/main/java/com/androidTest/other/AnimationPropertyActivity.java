package com.androidTest.other;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.androidTest.R;

public class AnimationPropertyActivity extends AppCompatActivity {

    private ImageView iv_animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_property);

        iv_animation = findViewById(R.id.iv_animation);
//        iv_animation.setRotationX();
    }

    // 透明度动画
    public void alphaAnimation(View view){
        ObjectAnimator animator = ObjectAnimator.ofFloat(iv_animation,"alpha",0,1);
        animator.setDuration(2000);
        animator.start();
    }

    // 旋转动画
    public void rotationAnimation(View view){
        ObjectAnimator animator = ObjectAnimator.ofFloat(iv_animation,"rotationY",0,180,90,360);
        animator.setDuration(2000);
        animator.start();
    }

    // 移动动画
    public void translateAnimation(View view){
        ObjectAnimator animator = ObjectAnimator.ofFloat(iv_animation,"translationY",20,200,100,500);
        animator.setDuration(2000);
        animator.start();
    }

    // 缩放动画
    public void scaleAnimation(View view){
        ObjectAnimator animator = ObjectAnimator.ofFloat(iv_animation,"scaleY",0.5f,2,1,2);
        animator.setDuration(2000);
        animator.start();
    }

    // 混合动画
    public void multiAnimation(View view){
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(iv_animation,"alpha",0,1);
        ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(iv_animation,"rotationY",0,180,90,360);
        ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(iv_animation,"translationY",20,200,100,500);
        ObjectAnimator scaleAnimator = ObjectAnimator.ofFloat(iv_animation,"scaleY",0.5f,2,1,2);
        set.setDuration(3000);
        set.setTarget(iv_animation);
        // 动画逐个执行
        set.playSequentially(alphaAnimator,rotationAnimator,translationAnimator,scaleAnimator);
        // 动画一起执行
//        set.playTogether(alphaAnimator,rotationAnimator,translationAnimator,scaleAnimator);
        set.start();
    }

    // xml配置透明度动画
    public void alphaAniWithXml(View view){
        ObjectAnimator animator = (ObjectAnimator) AnimatorInflater.loadAnimator(this,R.animator.alpha);
        animator.setTarget(iv_animation);
        animator.start();
    }

    // xml配置旋转动画 相对于自己的中心点旋转360度
    public void rotationAniWithXml(View view){
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.rotate);
        iv_animation.startAnimation(animation);
    }

    // xml配置移动动画
    public void translateAniWithXml(View view){
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.translate);
        iv_animation.startAnimation(animation);
    }

    // xml配置缩放动画
    public void scaleAniWithXml(View view){
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.scale);
        iv_animation.startAnimation(animation);
    }

    // xml配置混合动画
    public void multiAniWithXml(View view){
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.set);
        iv_animation.startAnimation(animation);
    }

}
