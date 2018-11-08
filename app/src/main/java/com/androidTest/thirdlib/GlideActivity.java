package com.androidTest.thirdlib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.androidTest.R;
import com.bumptech.glide.Glide;

public class GlideActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glide);

        // 找到图片
        imageView = findViewById(R.id.imageView);
    }

    // 加载图片
    public void loadImage(View view){
        String url = "http://pic6.nipic.com/20100417/4578581_140045259657_2.jpg";
        Glide.with(this).load(url).into(imageView);
    }
}
