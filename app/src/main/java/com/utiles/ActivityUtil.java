package com.utiles;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;

public class ActivityUtil {

    /**
     * 获取当前显示的activity的名字(包名+类名)
     * @param context 上下文
     * @return
     */
    public static String getCurrentActivityClassName(Context context){
        ComponentName componentName = getCurrentActivity(context);

        return componentName.getClassName();
    }

    /**
     * 获取当前包名
     * @param context 上下文
     * @return
     */
    public static String getPackageName(Context context){
        ComponentName componentName = getCurrentActivity(context);

        return componentName.getPackageName();
    }

    /**
     * 获取当前显示activity的shortName
     * @param context 上下文
     * @return
     */
    public static String getCurrentActivityShortName(Context context){
        ComponentName componentName = getCurrentActivity(context);

        // 获取当前显示activity的名字
        String shortClassName = componentName.getShortClassName();

        return shortClassName;
    }

    public static String getCurrentActivityName(Context context){
        ComponentName componentName = getCurrentActivity(context);

        return componentName.getClass().toString();
    }

    /**
     * 获取当前显示的activity （返回的是ComponentName类型）
     * @param context 上下文
     * @return
     */
    public static ComponentName getCurrentActivity(Context context){
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.RunningTaskInfo info = activityManager.getRunningTasks(1).get(0);

        ComponentName componentName = info.topActivity;

        return componentName;
    }
}
