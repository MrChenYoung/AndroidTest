package com.androidtest.controls;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import helloworld.android.com.androidtest.R;

public class ToastTestActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toast);

    }

    // 系统默认的Toast(偏下)
    public void click1(View view){
        Toast toast = Toast.makeText(this,"系统默认Toast",Toast.LENGTH_SHORT);
        toast.show();
    }

    // 偏上Toast
    public void click2(View view){
        Toast toast = Toast.makeText(this,"偏上Toast",Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP,0,0);
        toast.show();
    }

    // 偏左
    public void click3(View v){
        Toast toast = Toast.makeText(this,"偏左Toast",Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.LEFT,0,0);
        toast.show();
    }

    // 偏下Toast（比默认的更靠下）
    public void click4(View view){
        Toast toast = Toast.makeText(this,"偏下Toast",Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM,0,0);
        toast.show();
    }

    // 偏右Toast
    public void click5(View view){
        Toast toast = Toast.makeText(this,"偏右Toast",Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.RIGHT,0,0);
        toast.show();
    }

    // 带有图片的Toast(自定义Toast)
    public void click6(View view){

        Toast toast = new Toast(this);

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.book);
        toast.setView(imageView);
        toast.show();
    }
}
