package com.androidtest.other;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

import helloworld.android.com.androidtest.R;

public class AssetsActivity extends Activity {
    private ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assets);
        imageView = findViewById(R.id.imageView);

    }

    // 获取assets文件夹里的文本数据
    public void getData(View view){
        try {
            InputStream inputStream = getAssets().open("data");

            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);
            String result = new String(b);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("解析assets中的文件结果为");
            builder.setMessage(result);
            builder.setPositiveButton("确定",null);
            AlertDialog dialog = builder.create();
            dialog.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 获取assets文件夹中的图片
    public void getPic(View view){
        try {
            InputStream inputStream = getAssets().open("test1.jpg");

            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
