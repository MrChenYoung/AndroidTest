package com.androidtest.other;

import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import helloworld.android.com.androidtest.R;

public class AnimationFrameActivity extends AppCompatActivity {

    private ImageView imageView;
    private AnimationDrawable animationDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_frame);
        // 动画imageView
        imageView = findViewById(R.id.imageView);
        imageView.setBackgroundResource(R.drawable.frame_anim);
        animationDrawable = (AnimationDrawable) imageView.getBackground();
    }

    // 开启动画
    public void startAnimation(View view){
        if (!animationDrawable.isRunning()){
            animationDrawable.start();
        }else {
            Toast.makeText(this,"动画播放中，不能重复开启",Toast.LENGTH_SHORT).show();
        }
    }

    // 停止动画
    public void stopAnimation(View view){
        if (animationDrawable.isRunning()){
            animationDrawable.stop();
        }else {
            Toast.makeText(this,"动画已经停止,请先开启动画",Toast.LENGTH_SHORT).show();
        }
    }
}
