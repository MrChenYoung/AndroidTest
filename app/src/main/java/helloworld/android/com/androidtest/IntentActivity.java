package helloworld.android.com.androidtest;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import java.io.File;

public class IntentActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent);

    }

    // 跳转
    public void click(View view){


        // 显式Intent
//        Intent intent = new Intent();
//        intent.setComponent(new ComponentName("helloworld.android.com.androidtest","helloworld.android.com.androidtest.Intent1Activity"));


        // 隐式Intent
        Intent intent = new Intent("helloworld.android.com.androidtest.MyIntent");
        startActivity(intent);
    }

    // 打开图片浏览器
    public void click2(View view){
        File file = new File("/mnt/sdcard/Download/swiper1.jpg");

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),"image/*");

        startActivity(intent);
    }

    // 打开打电话界面
    public void click3(View view){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("tel:110"));
        startActivity(intent);
    }

    // 打开网页
    public void click4(View view){
        Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.baidu.com"));
        startActivity(intent);
    }

}

