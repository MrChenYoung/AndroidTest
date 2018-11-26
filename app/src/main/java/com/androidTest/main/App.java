package com.androidTest.main;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import java.lang.ref.WeakReference;

/**
 * Author：mengyuan
 * Date  : 2017/7/27下午2:56
 * E-Mail:mengyuanzz@126.com
 * Desc  :
 */

public class App extends Application {

    // 上下文
    public static Context context;

    //获取当前显示的activity(为避免内存泄漏使用弱引用)
    public static WeakReference<Activity> mCurrentActivity;

    @Override
    public void onCreate() {
        super.onCreate();

        // 获取上下文
        context = getApplicationContext();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

                }

                @Override
                public void onActivityStarted(Activity activity) {

                }

                @Override
                public void onActivityResumed(Activity activity) {
                    // 获取当前显示的activity
                    mCurrentActivity = new WeakReference<>(activity);
                }

                @Override
                public void onActivityPaused(Activity activity) {

                }

                @Override
                public void onActivityStopped(Activity activity) {

                }

                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

                }

                @Override
                public void onActivityDestroyed(Activity activity) {

                }
            });
        }
    }
}
