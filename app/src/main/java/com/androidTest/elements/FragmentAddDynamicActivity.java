package com.androidTest.elements;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.androidTest.R;

public class FragmentAddDynamicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_add_dynamic);

        // 获取fragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();

        // 开启fragment事物
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // 替换当前界面为fragment
        transaction.replace(R.id.main_view,new FragmentDynamic());

        // 提交事物
        transaction.commit();
    }
}
