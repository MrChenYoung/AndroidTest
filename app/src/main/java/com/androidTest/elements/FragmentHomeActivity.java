package com.androidTest.elements;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.androidTest.R;

public class FragmentHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_home);
    }

    // 静态添加fragment
    public void addFragmentStatic(View view){
        Intent intent = new Intent(this,FragmentAddStaticActivity.class);
        startActivity(intent);
    }

    // 动态添加fragment
    public void addFragmentDynamic(View view){
        Intent intent = new Intent(this,FragmentAddDynamicActivity.class);
        startActivity(intent);
    }

    // fragment的生命周期
    public void fragmentLifcycle(View view){
        Intent intent = new Intent(this,FragmentLifecycleActivity.class);
        startActivity(intent);
    }

    // fragment之间的通信
    public void fragmentCommunication(View view){
        Intent intent = new Intent(this,FragmentCommActivity.class);
        startActivity(intent);
    }
}
