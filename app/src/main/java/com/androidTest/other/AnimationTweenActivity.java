package com.androidTest.other;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidTest.R;

public class AnimationTweenActivity extends AppCompatActivity {

    private ImageView iv_animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_tween);

        // 动画imageView
        iv_animation = findViewById(R.id.iv_animation);
    }

    // 透明度动画
    public void alphaAnimation(View view){
        // 透明度从0变成1
        AlphaAnimation animation = new AlphaAnimation(0,1);
        // 动画时间2秒
        animation.setDuration(2000);
        // 调用start函数之后等待开始运行的时间，单位为毫秒
        animation.setStartOffset(1000);
        // 重复1次
        animation.setRepeatCount(1);
        // 重复类型有两个值，reverse表示倒序回放，restart表示从头播放
        animation.setRepeatMode(Animation.REVERSE);
        // 控件动画结束时是否保持动画最后的状态
        animation.setFillAfter(false);
        // 控件动画结束时是否还原到开始动画前的状态
        animation.setFillBefore(true);
        // 开始动画
        iv_animation.startAnimation(animation);

        // 给动画设置监听
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Toast.makeText(AnimationTweenActivity.this,"动画开始",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Toast.makeText(AnimationTweenActivity.this,"动画结束",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                Toast.makeText(AnimationTweenActivity.this,"通话重复",Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 旋转动画
    public void rotationAnimation(View view){
        // 指定特定中心点旋转
//        RotateAnimation rotateAnimation = new RotateAnimation(0,360,50,10);
        // 指定围绕自己中心旋转
        RotateAnimation rotateAnimation = new RotateAnimation(0,360,RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setDuration(1000);
        iv_animation.startAnimation(rotateAnimation);
    }

    // 移动动画
    public void translateAnimation(View view){
        TranslateAnimation animation = new TranslateAnimation(0,100,0,200);
        animation.setDuration(1000);
        iv_animation.startAnimation(animation);
    }

    // 缩放动画
    public void scaleAnimation(View view){

        // 相对于自身左上角缩放
//        ScaleAnimation animation = new ScaleAnimation(0,1,0,1);

        // 指定缩放的中心点
//        ScaleAnimation animation = new ScaleAnimation(0,1,0,1,500,20);

        // 指定围绕自身中心点缩放
        ScaleAnimation animation = new ScaleAnimation(0,1,0,1, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);

        animation.setDuration(1000);
        iv_animation.startAnimation(animation);
    }

    // 混合动画
    public void multiAnimation(View view){
        AnimationSet set = new AnimationSet(true);
//        set.setDuration(1000);

        // 添加透明度动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setDuration(1000);
        set.addAnimation(alphaAnimation);

        // 添加旋转动画
        RotateAnimation rotateAnimation = new RotateAnimation(0,360,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setDuration(2000);
        set.addAnimation(rotateAnimation);

        // 添加移动动画
        TranslateAnimation translateAnimation = new TranslateAnimation(0,200,0,200);
        translateAnimation.setDuration(2000);
        set.addAnimation(translateAnimation);

        // 添加缩放动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(0,1,0,1,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(2000);
        set.addAnimation(scaleAnimation);

        // 开始动画
        iv_animation.startAnimation(set);
    }

    // xml配置透明度动画
    public void alphaAniWithXml(View view){
        Animation alpha = AnimationUtils.loadAnimation(this,R.anim.alpha);
        iv_animation.startAnimation(alpha);
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
