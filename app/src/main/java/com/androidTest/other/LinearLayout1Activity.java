package com.androidTest.other;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.androidTest.R;

public class LinearLayout1Activity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linearlayout1);
    }
}
