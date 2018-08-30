package helloworld.android.com.androidtest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

public class AlertDialogActivity extends Activity {
    private String[] cities = {
            "北京",
            "上海",
            "深圳",
            "广州",
            "杭州"
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alertdialog);

    }

    // 普通提示框
    public void click1(View view){
        // 创建AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 设置内容
        builder.setTitle("普通提示框");
        builder.setMessage("提示内容。。。。。。");

        // 添加按钮
        builder.setPositiveButton("满意", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(AlertDialogActivity.this,"非常满意",Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNeutralButton("一般", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(AlertDialogActivity.this,"一般",Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("不满意", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(AlertDialogActivity.this,"不满意",Toast.LENGTH_SHORT).show();
            }
        });

        // 创建dialog
        AlertDialog dialog = builder.create();

        // 显示
        dialog.show();
    }

    // 列表提示框
    public void click2(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("请选择城市");

        builder.setItems(cities, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(AlertDialogActivity.this,cities[which],Toast.LENGTH_SHORT).show();
            }
        });

        builder.setPositiveButton("确定",null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // 单选提示框
    public void click3(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择一个城市");

        builder.setSingleChoiceItems(cities, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(AlertDialogActivity.this,cities[which],Toast.LENGTH_SHORT).show();
            }
        });

        builder.setPositiveButton("确定",null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // 多选提示框
    public void click4(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择喜欢的城市");

        builder.setMultiChoiceItems(cities, new boolean[]{false,true,false,false,false}, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked){
                    Toast.makeText(AlertDialogActivity.this,cities[which],Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setPositiveButton("确定",null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // 自定义Dialog
    public void click5(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("登录注册");

        View dialogContent = this.getLayoutInflater().inflate(R.layout.activity_customdialog,null);
        builder.setView(dialogContent);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
