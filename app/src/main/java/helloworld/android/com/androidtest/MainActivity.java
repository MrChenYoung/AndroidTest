package helloworld.android.com.androidtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // 进入按钮介绍界面
    public void click1(View v){
        Intent intent = new Intent();
        intent.setClass(this,ButtonTestActivity.class);
        startActivity(intent);
    }

    // UI线程阻塞解决方案
    public void click2(View v){
        Intent intent = new Intent();
        intent.setClass(this,UIThreadTestActivity.class);
        startActivity(intent);
    }

    // Toast用法
    public void click3(View view){
        Intent intent = new Intent();
        intent.setClass(this,ToastTestActivity.class);
        startActivity(intent);
    }

    // Notification用法
    public void click4(View view){
        Intent intent = new Intent();
        intent.setClass(this,NotificationActivity.class);
        startActivity(intent);
    }

    // TextView用法
    public void click5(View view){
        Intent intent = new Intent();
        intent.setClass(this,TextViewActivity.class);
        startActivity(intent);
    }

    // ImageView用法
    public void click6(View view){
        Intent intent = new Intent();
        intent.setClass(this,ImageViewActivity.class);
        startActivity(intent);
    }

    // EditText用法
    public void click7(View view){
        Intent intent = new Intent();
        intent.setClass(this,EditTextActivity.class);
        startActivity(intent);
    }

    // 其他类型Button用法
    public void click8(View v){
        Intent intent = new Intent();
        intent.setClass(this,OtherButtonsActivity.class);
        startActivity(intent);
    }

    // Spinner的用法
    public void click9(View view){
        Intent intent = new Intent();
        intent.setClass(this,SpinnerActivity.class);
        startActivity(intent);
    }

    // 配置方式生成线性布局
    public void click10(View view){
        Intent intent = new Intent();
        intent.setClass(this,LinearLayout1Activity.class);
        startActivity(intent);
    }
}
