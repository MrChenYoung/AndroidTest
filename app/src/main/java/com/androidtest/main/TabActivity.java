package com.androidtest.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import helloworld.android.com.androidtest.R;

/**
 * Created by mrchen on 2018/9/25.
 */

public class TabActivity extends LinearLayout implements AdapterView.OnItemClickListener {
    private Context context;
    private List<String> lists = new ArrayList<>();
    private List<String> classLists = new ArrayList<>();

    public TabActivity(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        String[] strings = new String[0];
        String[] classes = new String[0];
        switch (this.getId()){
            case R.id.tab1:
                strings = getResources().getStringArray(R.array.controls);
                classes = getResources().getStringArray(R.array.controls_activity);
                break;
            case R.id.tab2:
                strings = getResources().getStringArray(R.array.element);
                classes = getResources().getStringArray(R.array.element_classes);
                break;
            case R.id.tab3:
                strings = getResources().getStringArray(R.array.third);
                classes = getResources().getStringArray(R.array.third_classes);
                break;
            case R.id.tab4:
                strings = getResources().getStringArray(R.array.demos);
                classes = getResources().getStringArray(R.array.demos_classes);
                break;
            case R.id.tab5:
                strings = getResources().getStringArray(R.array.other);
                classes = getResources().getStringArray(R.array.other_classes);
                break;
        }

        lists = Arrays.asList(strings);
        classLists = Arrays.asList(classes);
    }


    // 加载布局完成
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.setBackgroundColor(Color.parseColor("#F5F5F5"));

        ListView listView = new ListView(this.context);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,lists);
        listView.setAdapter(adapter);
        this.addView(listView);
        listView.setOnItemClickListener(this);
    }

    // 点击每一行
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String className = classLists.get(position);

        try {
            Intent intent = new Intent(context,Class.forName(className));
            context.startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(context,"1111",Toast.LENGTH_LONG).show();
        }
    }
}
