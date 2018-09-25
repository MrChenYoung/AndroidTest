package com.androidtest.controls;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import helloworld.android.com.androidtest.R;

public class SpinnerActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner);

        final List<String> list = new ArrayList<>();
        list.add("全国");
        list.add("北京");
        list.add("上海");
        list.add("深圳");
        list.add("广州");

        final List<String> couseList = new ArrayList<>();
        couseList.add("iOS");
        couseList.add("Android");
        couseList.add("HTML");
        couseList.add("Python");
        couseList.add("Java");
        couseList.add("PHP");
        couseList.add("nodejs");

        // 代码方式设置spinner数据
        Spinner spinner = findViewById(R.id.spinner2);
        spinner.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View textView;
                if (convertView == null){
                    textView = new TextView(SpinnerActivity.this);
                }else {
                    textView = convertView;
                }

                TextView txView = (TextView) textView;
                txView.setText(list.get(position));

                return textView;
            }
        });

        // 通过ArrayAdapt设置spinner数据
        Spinner spinner1 = findViewById(R.id.spinner3);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,couseList);
        spinner1.setAdapter(adapter);

        // 给spinner添加点击监听事件
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(SpinnerActivity.this,couseList.get(position),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(SpinnerActivity.this,"什么也没有被选中",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
