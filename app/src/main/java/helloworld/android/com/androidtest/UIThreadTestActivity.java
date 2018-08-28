package helloworld.android.com.androidtest;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class UIThreadTestActivity extends Activity {
    private Button btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uithread);

        // 开始执行动画
        startAnimation();
    }

    // 设置按钮开始动画移动
    public void  startAnimation(){
        Button animationBtn = findViewById(R.id.button1);

        float originX = animationBtn.getX();
        float originY = animationBtn.getY();
        TranslateAnimation animation = new TranslateAnimation(originX,originX + 400,originY,originY);
        // 重复100次动画
        animation.setRepeatCount(100);
        // 每次动画执行时间 2s
        animation.setDuration(2000);
        // 按钮添加动画
        animationBtn.setAnimation(animation);
    }

    // 阻塞主线程
    public void blockMainThread(View v){
        // 在子线程中修改UI控件内容 方案1
        changeUI1(v);

        // 在子线程中修改UI控件内容 方案2(官方推荐)
//        btn = (Button)v;
//        new DownloadImage().execute();
    }

    // 在子线程中修改UI控件内容 方案1
    public void changeUI1(final View v){
        // 解决阻塞 创建新线程执行耗时操作
        new Thread(){
            @Override
            public void run() {
                super.run();

                // 线程睡5s
                SystemClock.sleep(2000); // 也可以用Thread.sleep(5000);

                // 在子线程修改UI界面 方案1 通过post方法
                v.post(new Runnable() {
                    @Override
                    public void run() {
                        Button btn = (Button) v;
                        btn.setText("子线程执行结束");
                    }
                });
            }
        }.start();
    }

    // 在子线程中修改UI控件内容 方案2
    private class DownloadImage extends AsyncTask<String, Void, Void>{
        @Override
        protected Void doInBackground(String... strings) {
            // 线程睡2s
            SystemClock.sleep(2000);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            // 修改ui
            btn.setText("子线程执行结束");
        }
    }
}

