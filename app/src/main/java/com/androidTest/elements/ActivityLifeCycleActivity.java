package com.androidTest.elements;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidTest.R;

public class ActivityLifeCycleActivity extends Activity {

    private LinearLayout mainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_lifecycle);
        mainView = findViewById(R.id.mainView);

        addSubView("调用了OnCreate方法");
        showToast("调用了OnCreate方法");
    }

    @Override // activity可见的时候调用
    protected void onStart() {
        super.onStart();

        addSubView("调用了OnStart方法");
        showToast("调用了OnStart方法");
    }

    @Override // 当activity上控件获取焦点的时候调用
    protected void onResume() {
        super.onResume();

        addSubView("调用了OnResume方法");
        showToast("用了OnResume方法");
    }

    @Override // 当activity上控件失去焦点的时候调用
    protected void onPause() {
        super.onPause();

        addSubView("调用了OnPause方法");
        showToast("调用了OnPause方法");
    }

    @Override // 当activity不可见的时候调用
    protected void onStop() {
        super.onStop();

        addSubView("调用了OnStop方法");
        showToast("调用了OnStop方法");
    }

    @Override // 当activity重新显示的时候调用
    protected void onRestart() {
        super.onRestart();

        addSubView("调用了OnRestart方法");
        showToast("调用了OnRestart方法");
    }

    @Override // 当activity销毁的时候调用
    protected void onDestroy() {
        super.onDestroy();

        addSubView("调用了OnDestroy方法");
        showToast("调用了OnDestroy方法");
    }

    // 跳转到下个页面
    public void nextPage(View view){
        Intent intent = new Intent(this,ActivityTestPageActivity.class);
        startActivity(intent);
    }

    // 调用一个生命周期的方法就添加一个指示视图
    public void addSubView(String text){
        View view = getLayoutInflater().inflate(R.layout.activity_activity_sub,null);
        TextView textView = view.findViewById(R.id.tv_name);
        textView.setText(text);
        mainView.addView(view);
    }

    // 显示Toast
    public void showToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}
