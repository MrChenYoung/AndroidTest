package helloworld.android.com.androidtest;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

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

    // 透明度动画
    public void alphaAnimation(View view){
        AlphaAnimation animation = new AlphaAnimation(0,1);
        animation.setDuration(2000);
        view.startAnimation(animation);
    }

    // 旋转动画
    public void rotationAnimation(View view){
        // 指定特定中心点旋转
//        RotateAnimation rotateAnimation = new RotateAnimation(0,360,50,10);
        // 指定围绕自己中心旋转
        RotateAnimation rotateAnimation = new RotateAnimation(0,360,RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setDuration(1000);
        view.startAnimation(rotateAnimation);
    }

    // 移动动画
    public void translateAnimation(View view){
        TranslateAnimation animation = new TranslateAnimation(0,100,0,200);
        animation.setDuration(1000);
        view.startAnimation(animation);
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
        view.startAnimation(animation);
    }

    // 混合动画
    public void multiAnimation(View view){
        AnimationSet set = new AnimationSet(true);
        set.setDuration(1000);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        set.addAnimation(alphaAnimation);

        TranslateAnimation translateAnimation = new TranslateAnimation(0,200,0,200);
        set.addAnimation(translateAnimation);
        view.startAnimation(set);
    }

    // 监听动画事件
    public void addAnimationListener(View view){
        RotateAnimation rotateAnimation = new RotateAnimation(0,360,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setDuration(2000);
        view.startAnimation(rotateAnimation);

        // 添加监听
        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Toast.makeText(AnimationActivity.this,"动画开始",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Toast.makeText(AnimationActivity.this,"动画结束",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                Toast.makeText(AnimationActivity.this,"动画重复",Toast.LENGTH_SHORT).show();
            }
        });
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
