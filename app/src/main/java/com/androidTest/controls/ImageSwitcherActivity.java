package com.androidTest.controls;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.androidTest.R;

public class ImageSwitcherActivity extends Activity {
    private boolean showFirstImage = true;
    private ImageSwitcher imageSwitcher;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageswitcher);

        imageSwitcher = findViewById(R.id.imageSwitcher);

        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                return new ImageView(ImageSwitcherActivity.this);
            }
        });
        imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,android.R.anim.fade_in));
        imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,android.R.anim.fade_out));

        imageSwitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFirstImage = !showFirstImage;

                changeImage();
            }
        });

        changeImage();
    }

    // 切换图片
    private void changeImage(){
        if (showFirstImage){
            imageSwitcher.setImageResource(R.drawable.a1);
        }else {
            imageSwitcher.setImageResource(R.drawable.a2);
        }
    }
}
