package helloworld.android.com.androidtest;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    // 列表数据
    private String[] list = {
            "Button用法介绍",
            "listView用法",
            "Toast用法",
            "TextView用法",
            "ImageView用法",
            "EditText用法",
            "其他Button用法",
            "Spinner用法",
            "AlertDialog用法",
            "ScrollView用法",

            "UI线程阻塞解决方案",
            "Notification用法",

            "配置方式生成线性布局",
            "纯代码生成线性布局",
            "代码和xml混合使用",
            "相对布局使用",
            "帧布局使用",
            "表格布局使用"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return list.length;
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
                TextView textView;

                if (convertView == null){
                    textView = new TextView(MainActivity.this);
                    textView.setHeight(70);
                    textView.setTextSize(14);
                    textView.setGravity(Gravity.CENTER_VERTICAL);
                    textView.setPadding(10,0,0,0);
                }else {
                    textView = (TextView) convertView;
                }

                textView.setText(list[position]);
                return textView;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();

                String text = list[position];
                switch (text){
                    case "Button用法介绍":
                        // 进入按钮介绍界面
                        intent.setClass(MainActivity.this,ButtonTestActivity.class);
                        break;
                    case "listView用法":
                        // ListViewyo用法介绍
                        intent.setClass(MainActivity.this,ListViewActivity.class);
                        break;
                    case "UI线程阻塞解决方案":
                        // UI线程阻塞解决方案
                        intent.setClass(MainActivity.this,UIThreadTestActivity.class);
                        break;
                    case "Toast用法":
                        // Toast用法
                        intent.setClass(MainActivity.this,ToastTestActivity.class);
                        break;
                    case "Notification用法":
                        // Notification用法
                        intent.setClass(MainActivity.this,NotificationActivity.class);
                        break;
                    case "TextView用法":
                        // TextView用法
                        intent.setClass(MainActivity.this,TextViewActivity.class);
                        break;
                    case "ImageView用法":
                        // ImageView用法
                        intent.setClass(MainActivity.this,ImageViewActivity.class);
                        break;
                    case "EditText用法":
                        // EditText用法
                        intent.setClass(MainActivity.this,EditTextActivity.class);
                        break;
                    case "其他Button用法":
                        // 其他类型Button用法
                        intent.setClass(MainActivity.this,OtherButtonsActivity.class);
                        break;
                    case "Spinner用法":
                        // Spinner的用法
                        intent.setClass(MainActivity.this,SpinnerActivity.class);
                        break;
                    case "配置方式生成线性布局":
                        // 配置方式生成线性布局
                        intent.setClass(MainActivity.this,LinearLayout1Activity.class);
                        break;
                    case "纯代码生成线性布局":
                        // 纯代码生成线性布局
                        intent.setClass(MainActivity.this,LinearLayout2Activity.class);
                        break;
                    case "代码和xml混合使用":
                        // 代码和xml混合使用
                        intent.setClass(MainActivity.this,LinearLayout3Activity.class);
                        break;
                    case "相对布局使用":
                        // 相对布局用法
                        intent.setClass(MainActivity.this,RelativeLayoutActivity.class);
                        break;
                    case "帧布局使用":
                        // 帧布局的使用
                        intent.setClass(MainActivity.this,FrameLayoutActivity.class);
                        break;
                    case "表格布局使用":
                        intent.setClass(MainActivity.this,TableLayoutActivity.class);
                        break;
                    case "AlertDialog用法":
                        intent.setClass(MainActivity.this,AlertDialogActivity.class);
                        break;
                    case "ScrollView用法":
                        intent.setClass(MainActivity.this,ScrollViewActivity.class);
                        break;
                }

                startActivity(intent);
            }
        });

        // 跳转到指定的页面


    }

}
