package com.androidTest.controls;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.androidTest.R;

public class OtherButtonsActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otherbuttons);

        // RadioGroup添加事件监听
        RadioGroup radioGroup = findViewById(R.id.RadioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = group.findViewById(checkedId);
                String text = radioButton.getText().toString();
                String tag = radioButton.getTag().toString();
                Toast.makeText(OtherButtonsActivity.this,"选择tag：" + tag + "text:" + text,Toast.LENGTH_SHORT).show();

            }
        });

        // CheckBox添加点击事件
        CheckBox checkBox1 = findViewById(R.id.CheckBox1);
        CheckBox checkBox2 = findViewById(R.id.CheckBox2);
        checkBox1.setOnCheckedChangeListener(checkBoxChange);
        checkBox2.setOnCheckedChangeListener(checkBoxChange);
    }

    // ImageButton点击事件
    public void click1(View view){
        Toast.makeText(this,"ImageButton点击",Toast.LENGTH_SHORT).show();
    }

    // ToggleButton点击事件
    public void click2(View view){
        ToggleButton toggleButton = (ToggleButton) view;
        Toast.makeText(this,"当前选择了:"+toggleButton.getText(),Toast.LENGTH_SHORT).show();
    }

    // checkBox点击事件监听
    private CompoundButton.OnCheckedChangeListener checkBoxChange = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked){
                Toast.makeText(OtherButtonsActivity.this,"选择了"+buttonView.getText(),Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(OtherButtonsActivity.this,"取消选择了"+buttonView.getText(),Toast.LENGTH_SHORT).show();
            }
        }
    };
}


