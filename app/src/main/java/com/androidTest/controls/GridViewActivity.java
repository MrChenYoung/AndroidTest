package com.androidTest.controls;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidTest.R;

public class GridViewActivity extends Activity {
    private String[] listData = {
            "iOS",
            "Android",
            "HTML",
            "Python"
    };

    private String[] selectData = {
            "BaseAdapter设置GridView",
            //"ArrayAdapter设置GridView",
            //"SimpleAdapter设置GridView"
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gridview);

        final GridView gridView = findViewById(R.id.gridview);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择GridView类型");
        builder.setItems(selectData, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        setWithBaseAdapter(gridView);
                        break;
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    // 使用BaseAdapter设置GridView数据
    public void setWithBaseAdapter(GridView gridView){
        gridView.setAdapter(new BaseAdapter() {
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
                ViewGroup viewGroup;

                if (convertView == null){
                    viewGroup = (ViewGroup) getLayoutInflater().inflate(R.layout.activity_gridviewitem,null);
                }else {
                    viewGroup = (ViewGroup) convertView;
                }

                ImageView imageView = viewGroup.findViewById(R.id.imageView);
                TextView textView = viewGroup.findViewById(R.id.textView);
                imageView.setImageResource(R.drawable.book);
                textView.setText(listData[position]);

                return viewGroup;
            }
        });
    }

    // 使用ArrayAdapter设置GridView数据
    public void setWithArrayAdapter(GridView gridView){

//        gridView.setAdapter(new Arra);
    }
}
