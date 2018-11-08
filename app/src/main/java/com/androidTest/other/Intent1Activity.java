package com.androidTest.other;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.androidTest.R;

public class Intent1Activity extends Activity {

    private EditText receiveText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent1);

        // 获取上个页面传过来的数据
        receiveText = findViewById(R.id.tv_receive);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        int age = intent.getIntExtra("age",0);
        receiveText.setText("姓名:" + name + "年龄:" + age);
    }

    // 返回
    public void back(View view){
        // 返回 把receiveText里面的文字带回上个页面
        // 传数据
        Intent intent = getIntent();
        intent.putExtra("result",receiveText.getText().toString());
        setResult(1,intent);

        finish();
    }
}
