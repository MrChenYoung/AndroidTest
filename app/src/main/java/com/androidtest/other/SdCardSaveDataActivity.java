package com.androidtest.other;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import helloworld.android.com.androidtest.R;

public class SdCardSaveDataActivity extends Activity {
    private EditText editText;
    private File dataFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdcardsavedata);

        editText = findViewById(R.id.editText);

        File baseDir = Environment.getExternalStorageDirectory();
        dataFile = new File(baseDir,"data.txt");

        // 如果文件不存在 创建
        if (!dataFile.exists()){
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        readData(null);
    }

    // 存数据
    public void saveData(View view){
        try {

            FileOutputStream outputStream = new FileOutputStream(dataFile);
            outputStream.write(editText.getText().toString().getBytes());
            outputStream.flush();
            outputStream.close();

            editText.setText("");

            Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this,"保存失败",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    // 读数据
    public void readData(View view){
        try {
            FileInputStream inputStream = new FileInputStream(dataFile);

            byte[] bt = new byte[inputStream.available()];
            inputStream.read(bt);
            inputStream.close();

            editText.setText(new String(bt));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
