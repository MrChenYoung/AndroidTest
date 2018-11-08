package com.androidTest.controls;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.Toast;

import com.androidTest.R;

public class EditTextActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edittext);

        // 对输入的文字过滤 方式1
        EditText editText = findViewById(R.id.editText1);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 输入的内容改变以前
                Toast toast = Toast.makeText(EditTextActivity.this,"文字改变以前:"+s,Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP,0,0);
                toast.show();

                Log.i("内容改变以前:"+s,"1111");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 输入内容改变
                Toast toast = Toast.makeText(EditTextActivity.this,"文字改变:"+s,Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 输入内容改变以后
                Toast toast = Toast.makeText(EditTextActivity.this,"输入内容改变以后:"+s,Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.LEFT,0,0);
                toast.show();
            }
        });

        // 对输入的文字过滤 方式2
        EditText editText2 = findViewById(R.id.editText2);
        editText2.setFilters(new InputFilter[]{
                // 限制最多输入5个字符
                new InputFilter.LengthFilter(5),
                // 输入的字母全部转换成大写
                new InputFilter.AllCaps(),
                // 自定义过滤器
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                        Toast toast = Toast.makeText(EditTextActivity.this,"输入的内容为:"+dest,Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                        return null;
                    }
                }
        });
    }
}
