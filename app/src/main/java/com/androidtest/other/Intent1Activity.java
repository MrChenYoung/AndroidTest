package com.androidtest.other;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import helloworld.android.com.androidtest.R;

public class Intent1Activity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent1);

    }
}