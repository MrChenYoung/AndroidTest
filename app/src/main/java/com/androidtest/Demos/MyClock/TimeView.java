package com.androidtest.Demos.MyClock;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.logging.LogRecord;

import helloworld.android.com.androidtest.R;

public class TimeView extends LinearLayout {

    private TextView textView;
    private MyHandle handler = new MyHandle();

    public TimeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        textView = findViewById(R.id.textView);
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);

        if (visibility == View.VISIBLE){
            // 显示
            handler.sendEmptyMessage(0);
        }else {
            // 消失
            handler.removeMessages(0);
        }
    }

    // 显示当前时间
    private void showTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        // 年月日
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.format("%02d",calendar.get(Calendar.MONTH) + 1);
        String day = String.format("%02d",calendar.get(Calendar.DAY_OF_MONTH));

        // 时分秒
        String hour = String.format("%02d",calendar.get(Calendar.HOUR_OF_DAY));
        String minute = String.format("%02d",calendar.get(Calendar.MINUTE));
        String second = String.format("%02d",calendar.get(Calendar.SECOND));

        String result = year + "年" + month + "月" + day + "日" + "\n" + hour + ":" + minute + ":" + second;
        textView.setText(result);
    }

    private class MyHandle extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            showTime();

            if (getVisibility() == VISIBLE){
                handler.sendEmptyMessageDelayed(0,1000);
            }
        }
    }

}
