package com.androidtest.elements;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import helloworld.android.com.androidtest.R;

public class MyActivity1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myactivity1);

        showToast("OnCreate");
    }

    // 跳转到下个页面
    public void click(View view){
        Intent intent = new Intent();
        intent.setClass(this,MyActivity2.class);

        // 设置传递的内容
        EditText editText = findViewById(R.id.editText);
        String text = editText.getText().toString();
        intent.putExtra("name",text);

        // 上个页面不需要回传数据的时候用这个
        //  startActivity(intent);

        // 上个页面需要回传数据的时候用这个
        startActivityForResult(intent,2);
    }

    // 这个页面被返回的时候调用
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 获取返回的数据
        String text = data.getStringExtra("name");
        EditText editText = findViewById(R.id.editText1);
        editText.setText(text);

    }

    @Override
    protected void onStart() {
        super.onStart();
        showToast("OnStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        showToast("OnResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        showToast("OnPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        showToast("OnStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        showToast("OnRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        showToast("OnDestroy");
    }

    // 显示Toast
    public void showToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
        System.out.println("++++++++++++" + msg);
    }
}
