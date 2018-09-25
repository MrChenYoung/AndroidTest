package com.androidtest.other;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import helloworld.android.com.androidtest.R;

public class JsonResoveActivity extends Activity {

    private EditText nameEdit;
    private EditText ageEdit;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private final String ROOTKEY = "students";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_resove);

        nameEdit = findViewById(R.id.editText_name);
        ageEdit = findViewById(R.id.editText_age);
        listView = findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,new ArrayList<String>());
        listView.setAdapter(arrayAdapter);

        // 读取数据
        readClick(null);
    }

    // 存储为json数据
    public void saveClick(View view){
        // 判断输入的内容是否为空
        if (TextUtils.isEmpty(nameEdit.getText().toString()) || TextUtils.isEmpty(ageEdit.getText().toString())){
            Toast.makeText(this,"姓名和年龄不能为空",Toast.LENGTH_SHORT).show();
            return;
        }

        File path = getStoragePath();
        if (path == null) {
            Toast.makeText(this,"路径不存在",Toast.LENGTH_LONG).show();
            return;
        }

        JSONArray array = null;
        JSONObject root = null;
        if (path.length() > 0){
            // 已经有数据，追加数据
            try {
                FileInputStream fis = new FileInputStream(path);
                byte[] bytes = new byte[fis.available()];
                fis.read(bytes);
                fis.close();
                String result = new String(bytes);

                root = new JSONObject(result);
                array = root.getJSONArray(ROOTKEY);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else {
            // 没有数据,新增数据
            root = new JSONObject();
            array = new JSONArray();
        }

        if (array == null || root == null){
            Toast.makeText(this,"读取json失败",Toast.LENGTH_SHORT).show();
            return;
        }else {
            JSONObject sub = new JSONObject();
            try {
                sub.put("name",nameEdit.getText().toString());
                sub.put("age",ageEdit.getText().toString());

                array.put(sub);
                root.put(ROOTKEY,array);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        // 数据写入硬盘
        try {
            FileOutputStream outputStream = new FileOutputStream(path);
            outputStream.write(root.toString().getBytes());
            outputStream.close();

            Toast.makeText(this,"数据存储成功",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 读取json数据
    public void readClick(View view){
        File path = getStoragePath();

        if (path == null) return;

        try {
            FileInputStream fis = new FileInputStream(path);
            byte[] bytes = new byte[fis.available()];
            fis.read(bytes);
            fis.close();
            String result = new String(bytes);

            JSONObject root = new JSONObject(result);
            JSONArray array = root.getJSONArray(ROOTKEY);

            arrayAdapter.clear();
            for (int i = 0; i < array.length(); i++){
                JSONObject object = array.getJSONObject(i);
                arrayAdapter.add("姓名:" + object.get("name") + "\n" + "年龄:" + object.get("age"));
            }

            // 刷新数据
            arrayAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 获取存储路径
    public File getStoragePath(){
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Toast.makeText(this,"sd卡不可用",Toast.LENGTH_SHORT).show();
            return null;
        }

        File dir = Environment.getExternalStorageDirectory();
        File path = new File(dir,"students.json");
        if (!path.exists()){
            try {
                boolean success = path.createNewFile();
                if (!success){
                    Toast.makeText(this,"创建文件失败",Toast.LENGTH_SHORT).show();
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return path;
    }
}
