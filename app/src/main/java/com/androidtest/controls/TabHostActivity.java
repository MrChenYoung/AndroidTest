package com.androidtest.controls;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import helloworld.android.com.androidtest.R;

public class TabHostActivity extends AppCompatActivity {

    private String[] tags = {"tab_home","tab_nearby","tab_mine"};
    private String[] titles = {"首页","附近","我的"};
    private int[] normalImages = {R.drawable.pfb_tabbar_homepage_2x,R.drawable.pfb_tabbar_merchant_2x,R.drawable.pfb_tabbar_mine_2x};
    private int[] selectedImages = {R.drawable.pfb_tabbar_homepage_selected_2x,R.drawable.pfb_tabbar_merchant_selected_2x,R.drawable.pfb_tabbar_mine_selected_2x};
    private Intent[] intents = new Intent[3];
    private TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_host);

        // 初始化子页面
        initIntents();

        // 初始化标签栏
        initTabHost(savedInstanceState);

    }

    // 初始化标签栏
    public void initTabHost(Bundle savedInstanceState){
        tabHost = findViewById(R.id.tabHost);
        LocalActivityManager manager = new LocalActivityManager(this, false);
        //通过管理者保存当前页面状态
        manager.dispatchCreate(savedInstanceState);
        //将管理者类对象添加至TabHost
        tabHost.setup(manager);

        TabWidget widget = findViewById(android.R.id.tabs);
        // 去掉标签中间的竖线
//        widget.setDividerDrawable(null);

        for (int i = 0; i < titles.length; i++) {
            LayoutInflater inflater = this.getLayoutInflater();
            View view = inflater.inflate(R.layout.activity_tab_host_item,null);
            ImageView imageView = view.findViewById(R.id.imageview);
            TextView textView = view.findViewById(R.id.textview);

            textView.setText(titles[i]);
            imageView.setImageResource(normalImages[i]);

            if (i == 0){
                imageView.setImageResource(selectedImages[i]);
                textView.setTextColor(Color.GREEN);
            }else {
                textView.setTextColor(Color.LTGRAY);
            }

            TabHost.TabSpec spec = tabHost.newTabSpec(tags[i]);
            spec.setIndicator(view);
            spec.setContent(intents[i]);
            tabHost.addTab(spec);
        }

        // 添加标签切换监听事件
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                for (int i = 0; i < titles.length; i++) {
                    View view = tabHost.getTabWidget().getChildTabViewAt(i);
                    TextView textView = view.findViewById(R.id.textview);
                    ImageView imageView = view.findViewById(R.id.imageview);
                    imageView.setImageResource(normalImages[i]);
                    textView.setTextColor(Color.LTGRAY);
                }

                if (tabHost.getCurrentTabTag().equals(tabId)){
                    View view = tabHost.getCurrentTabView();
                    ImageView imageView = view.findViewById(R.id.imageview);
                    TextView textView = view.findViewById(R.id.textview);
                    imageView.setImageResource(selectedImages[tabHost.getCurrentTab()]);
                    textView.setTextColor(Color.GREEN);
                }
            }
        });
    }

    // 初始化子页面
    public void initIntents(){
        intents[0] = new Intent(this,TabHostHomeItemActivity.class);
        intents[1] = new Intent(this,TabHostNearItemActivity.class);
        intents[2] = new Intent(this,TabHostMineItemActivity.class);
    }
}
