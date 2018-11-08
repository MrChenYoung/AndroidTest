package com.androidTest.main;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.androidTest.R;

public class MainActivity extends AppCompatActivity implements TabHost.OnTabChangeListener {
    // tabItem标记集
    private String[] tags = {"tag0","tag1","tag2","tag3","tag4"};
    // tabItem标题集
    private String[] tabTitles = {"基础控件","四大组件","第三方组件","Demos","其他"};
    // tabItem未选中图标集
    private int[] normalImages = {R.drawable.controls_nomal,R.drawable.element_normal,R.drawable.third_normal,R.drawable.demos_normal,R.drawable.other_normal};
    // tabItem选中图标集
    private int[] selectedImages = {R.drawable.controls_selected,R.drawable.element_selected,R.drawable.third_selected,R.drawable.demos_selected,R.drawable.other_selected};
    // tabItem布局集
    private int[] items = {R.id.tab1,R.id.tab2,R.id.tab3,R.id.tab4,R.id.tab5};

    // tabItem选中和未选中文字颜色
    private int normalColor = Color.parseColor("#b3b1c1");
    private int selectedColor = Color.parseColor("#9370DB");
    private TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化tabHost
        initTabHost();
    }

    // 初始化tabHost
    public void initTabHost(){
        tabHost = findViewById(R.id.tabHost);
        tabHost.setup();

        LayoutInflater inflater = getLayoutInflater().from(this);
        for (int i = 0; i < tags.length; i++) {
            TabHost.TabSpec spec = tabHost.newTabSpec(tags[i]);

            View view = inflater.inflate(R.layout.activity_tab_host_item,null);
            ImageView imageView = view.findViewById(R.id.imageview);
            TextView textView = view.findViewById(R.id.textview);
            imageView.setImageResource(normalImages[i]);
            textView.setText(tabTitles[i]);
            textView.setTextColor(normalColor);

            if (i == 0){
                imageView.setImageResource(selectedImages[i]);
                textView.setTextColor(selectedColor);
            }

            spec.setIndicator(view);
            spec.setContent(items[i]);
            tabHost.addTab(spec);
        }

        // 给tabHost添加点击事件
        tabHost.setOnTabChangedListener(this);
    }

    // 切换tabItem
    @Override
    public void onTabChanged(String tabId) {
        // 重置所有选项的状态
        for (int i = 0; i < tags.length; i++) {
            View view = tabHost.getTabWidget().getChildTabViewAt(i);
            ImageView imageView = view.findViewById(R.id.imageview);
            TextView textView = view.findViewById(R.id.textview);
            imageView.setImageResource(normalImages[i]);
            textView.setTextColor(normalColor);
        }

        View currentView = tabHost.getCurrentTabView();
        ImageView currentImgV = currentView.findViewById(R.id.imageview);
        TextView textView = currentView.findViewById(R.id.textview);
        currentImgV.setImageResource(selectedImages[tabHost.getCurrentTab()]);
        textView.setTextColor(selectedColor);
    }

    // 手机后退按键被点击
    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("温馨提示");
        builder.setMessage("确定退出应用吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.super.onBackPressed();
            }
        });
        builder.setNegativeButton("取消",null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
