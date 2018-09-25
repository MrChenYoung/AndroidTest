package com.androidtest.other;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import helloworld.android.com.androidtest.R;

public class InsideSaveDataActivity extends Activity {
    private EditText editText;
    private final String fileName = "data";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insidesavedata);

        editText = findViewById(R.id.editText);

        readData(null);
    }

    // 保存数据
    public void saveData(View view){
        try {
            FileOutputStream outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(editText.getText().toString().getBytes());
            outputStream.flush();
            outputStream.close();

            editText.setText("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 读取数据
    public void readData(View view){
        try {
            FileInputStream inputStream = openFileInput(fileName);
            byte[] bt = new byte[inputStream.available()];
            inputStream.read(bt);
            inputStream.close();

            editText.setText(new String(bt));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
