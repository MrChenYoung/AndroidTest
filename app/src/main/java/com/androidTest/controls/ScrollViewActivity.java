package com.androidTest.controls;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidTest.R;

public class ScrollViewActivity extends Activity {
    private int index = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrollview);
        addNewViews();

        ScrollView scrollView = findViewById(R.id.scrollview);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ScrollView scroll = (ScrollView)v;
                int H = scroll.getHeight();
                int linearH = scroll.getChildAt(0).getMeasuredHeight();
                int scrollY = scroll.getScrollY();

                if (scrollY + H == linearH){
                    Toast.makeText(ScrollViewActivity.this,"已经到底了",Toast.LENGTH_SHORT).show();
                    addNewViews();
                }

                return false;
            }
        });
    }

    // 增加子试图
    public void addNewViews(){
        LinearLayout linearLayout = findViewById(R.id.linearLayout);

        int count = index;
        index+=30;
        for (;count < index; count++){
            TextView textView = new TextView(this);
            textView.setText("当前在" + count + "行");
            textView.setPadding(10,5,0,5);
            linearLayout.addView(textView);
        }
    }
}

