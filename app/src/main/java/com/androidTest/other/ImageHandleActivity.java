package com.androidTest.other;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.androidTest.R;

public class ImageHandleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_handle);
    }

    // 创建原图的副本
    public void copyImage(View view){
        Intent intent = new Intent(this,ImageCopyActivity.class);
        startActivity(intent);
    }
}
