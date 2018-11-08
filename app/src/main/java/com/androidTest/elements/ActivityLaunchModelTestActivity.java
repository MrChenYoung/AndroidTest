package com.androidTest.elements;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.androidTest.R;

public class ActivityLaunchModelTestActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_top;

    public static final String LAUNCHMODELKEY = "model";
    public static final String LAUNCHMODELSTANDAED = "standard";
    public static final String LAUNCHMODELSINGLETOP = "singleTop";
    public static final String LAUNCHMODELSINGLETASK = "singleTask";
    public static final String LAUNCHMODELSINGLEINSTANCE = "singleInstance";

    private String model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_launch_model_test);

        // 找到指定的控件
        tv_top = findViewById(R.id.tv_top);
        Button bt_openLaunchModel = findViewById(R.id.bt_openLaunchModel);
        bt_openLaunchModel.setOnClickListener(this);

        // 获取传过来的启动模式
        Intent intent = getIntent();
        model = intent.getStringExtra(LAUNCHMODELKEY);
        tv_top.setText(model + "启动模式测试页面");
        bt_openLaunchModel.setText("启动" + model + "模式activity");
    }

    @Override // // 启动指定模式的模式activity
    public void onClick(View v) {
        Intent intent = null;
        switch (model){
            case LAUNCHMODELSTANDAED:
                // 标准启动模式
                intent = new Intent(this,ActivityStandardActivity.class);
                break;
            case LAUNCHMODELSINGLETOP:
                // singleTop启动模式
                intent = new Intent(this,ActivitySingleTopActivity.class);
                break;
            case LAUNCHMODELSINGLETASK:
                // singleTask启动模式
                intent = new Intent(this,ActivitySingleTaskActivity.class);
                break;
            case LAUNCHMODELSINGLEINSTANCE:
                // singleInstance启动模式
                intent = new Intent(this,ActivitySingleInstanceActivity.class);
                break;
        }
        startActivity(intent);
    }

    // 启动测试activity
    public void openTestActivity(View view){
        Intent intent = new Intent(this,ActivityLaunchModelTestActivity.class);
        intent.putExtra(LAUNCHMODELKEY,model);
        startActivity(intent);
    }
}
