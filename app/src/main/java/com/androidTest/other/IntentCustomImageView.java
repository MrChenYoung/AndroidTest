package com.androidTest.other;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

public class IntentCustomImageView extends Activity {

    private ImageView imageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 自定义图片浏览器
        imageView = new ImageView(this);

        // 设置传进来的图片
        Uri uri = getIntent().getData();
        imageView.setImageURI(uri);

        setContentView(imageView);
    }
}
