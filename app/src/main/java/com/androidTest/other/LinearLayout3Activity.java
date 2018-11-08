package com.androidTest.other;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.androidTest.R;

public class LinearLayout3Activity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 代码和xml混合

        // 创建线性布局
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        // 添加从xml配置好的字试图

        // 获取打气筒方式

        // 方式一
//        View view = View.inflate(this,R.layout.activity_linearlayoutinclude,null);

        // 方式二
//        View view = LayoutInflater.from(this).inflate(R.layout.activity_linearlayoutinclude,null);

        // 方式三
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_linearlayoutinclude,null);



        linearLayout.addView(view);

        setContentView(linearLayout);
    }
}

