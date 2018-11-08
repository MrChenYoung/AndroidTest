package com.androidTest.elements;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.androidTest.R;

public class ActivityTestPageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_test_page);
    }

    // 点击返回上一个页面
    public void back(View view){
        // 当前页面消失
        this.finish();
    }

}
