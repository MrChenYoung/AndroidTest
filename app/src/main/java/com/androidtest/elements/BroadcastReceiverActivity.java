package com.androidtest.elements;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import helloworld.android.com.androidtest.R;

public class BroadcastReceiverActivity extends AppCompatActivity {

    // 监听到的广播事件描述
    private static ListView listView;
    private ArrayAdapter<String> defaultAdapter = null;
    private static ArrayAdapter<String> adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_mine);
        listView = findViewById(R.id.listView);

        // 没有监听到广播的时候显示的adapter
        defaultAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        defaultAdapter.add("暂未监听到广播");
        listView.setAdapter(defaultAdapter);

        // 监听到广播时候显示的adapter
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
    }

    // 监听到广播事件 刷新列表
    public static void receiveCall(Context context, String message){
        if (adapter.getCount() == 0){
            listView.setAdapter(adapter);
        }

        adapter.add(message);
    }
}
