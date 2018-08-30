package helloworld.android.com.androidtest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListViewActivity extends Activity {
    private String[] listData = {
            "iOS",
            "Android",
            "HTML",
            "Python"
    };

    private String[] selectData = {
            "ArrayAdapter设置ListView",
            "BaseAdapter设置ListView",
            "SimpleAdapter设置ListView"
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        // 获取listView
        final ListView listView = findViewById(R.id.ListView);

        // 设置ListView头部
        TextView header = new TextView(this);
        header.setText("列表头部");
        header.setTextColor(Color.GRAY);
        header.setGravity(Gravity.CENTER);
        listView.addHeaderView(header);

        // 设置ListView尾部
        TextView footer = new TextView(this);
        footer.setText("列表尾部");
        footer.setTextColor(Color.GRAY);
        footer.setGravity(Gravity.CENTER);
        listView.addFooterView(footer);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("选择类型");
        builder.setItems(selectData, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        setWithArrayAdaptor(listView);
                        break;
                    case 1:
                        setWithBaseAdapter(listView);
                        break;
                    case 2:
                        setWithSimpleAdapter(listView);
                }
            }
        });


        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // 使用ArrayAdapter设置ListView数据
    public void setWithArrayAdaptor(ListView listView){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,listData);
        listView.setAdapter(adapter);
    }

    // 使用BaseAdapter设置ListView数据
    public void setWithBaseAdapter(ListView listView){
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return listData.length;
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
                    textView = new TextView(ListViewActivity.this);
                }else {
                    textView = (TextView) convertView;
                }

                textView.setText(listData[position]);
                return textView;
            }
        });
    }

    // 使用SimpleAdapter设置ListView
    public void setWithSimpleAdapter(ListView listView){
        List<Map<String,String>> data = new ArrayList<>();
        for (int i = 0; i < listData.length; i++){
            Map<String,String> map = new HashMap<>();
            map.put("name",listData[i]);
            data.add(map);
        }

        SimpleAdapter adapter = new SimpleAdapter(
                this,
                data,
                R.layout.activity_listitem,
                new String[]{"name"},
                new int[]{R.id.textView}
                );
        listView.setAdapter(adapter);

    }
}

