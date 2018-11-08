package com.androidTest.other;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.androidTest.R;

public class SqliteActivity extends Activity {

    // 数据库工具实例
    private SqliteMyDataBase db;

    // 姓名和年龄输入框
    private EditText nameEditText;
    private EditText ageEditText;
    private SimpleCursorAdapter adapter;
    private Cursor cursor;
    private ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite);

        // 获取姓名和年龄输入框
        nameEditText = findViewById(R.id.editText1);
        ageEditText = findViewById(R.id.editText2);

        // 设置列表
        listView = findViewById(R.id.listView);
        adapter = new SimpleCursorAdapter(this,R.layout.activity_sqlitelistitem,cursor,new String[]{"name","age"},new int[]{R.id.nameTextView,R.id.ageTextView});
        listView.setAdapter(adapter);

        // 数据库工具实例
        db = new SqliteMyDataBase(this);

        // 查询已经存储的数据
        selectData(null);
    }

    // 增加记录
    public void addData(View view){
        if (!isInfCorrCect()){
            return;
        }

        SQLiteDatabase dataBase = db.getWritableDatabase();
        String name = nameEditText.getText().toString();
        String age = ageEditText.getText().toString();

        // 方式一 sql命令
//        dataBase.execSQL("insert into person(name,age) values (?,?)",new String[]{name,age});

        // 方式二  官方封装API
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",nameEditText.getText().toString());
        contentValues.put("age",ageEditText.getText().toString());
        long rowID = dataBase.insert("person",null,contentValues);
        if (rowID > 0){
            Toast.makeText(this,"添加数据成功",Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this,"添加数据失败",Toast.LENGTH_LONG).show();
        }

        // 关闭数据库
        dataBase.close();

        nameEditText.setText("");
        ageEditText.setText("");

        // 重新查询数据，刷新列表
        selectData(null);
    }

    // 删除记录
    public void deleteData(View view){
        String name = nameEditText.getText().toString();
        String age = ageEditText.getText().toString();

        SQLiteDatabase database = db.getWritableDatabase();

        // 方式一 sql语句
//        database.execSQL("delete from person where name=? and age=?",new String[]{name,age});

        // 方式二 官方API 返回删除的条数
        int num = database.delete("person","name=? and age=?",new String[]{name,age});
        if (num > 0){
            Toast.makeText(this,"删除成功",Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this,"未找到匹配项,删除失败",Toast.LENGTH_LONG).show();
        }
        // 关闭数据库
        database.close();

        // 清空输入框
        nameEditText.setText("");
        ageEditText.setText("");

        // 重新查询数据，刷新列表
        selectData(null);
    }

    // 修改记录
    public void updateData(View view){
        final SQLiteDatabase dataBase = db.getWritableDatabase();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("确定要修改的信息");

        // 获取自定义alertView
        View view1 = getLayoutInflater().inflate(R.layout.activity_sqlite_alert,null);
        builder.setView(view1);
        final EditText ed_name = view1.findViewById(R.id.ed_name);
        final EditText ed_age = view1.findViewById(R.id.ed_age);

        final String newName = nameEditText.getText().toString();
        final String newAge = ageEditText.getText().toString();

        // 设置alert按钮
        builder.setNegativeButton("取消",null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 修改数据
                String name = ed_name.getText().toString();
                String age = ed_age.getText().toString();

                // 方式一 sql语句
//                String sql = "";
//                String[] args = null;
//                if (TextUtils.isEmpty(name) && !TextUtils.isEmpty(age)){
//                    sql = "update person set age=? where name=? and age=?";
//                    args = new String[]{newAge,name,age};
//                }else if (TextUtils.isEmpty(age) && !TextUtils.isEmpty(name)){
//                    sql = "update person set name=? where name=? and age=?";
//                    args = new String[]{newName,name,age};
//                }else {
//                    sql = "update person set name=?,age=? where name=? and age=?";
//                    args = new String[]{newName,newAge,name,age};
//                }
//                dataBase.execSQL(sql,args);

                // 方式二 官方API
                String sql = "";
                String[] args = null;
                if (TextUtils.isEmpty(name) && !TextUtils.isEmpty(age)){
                    sql = "age=?";
                    args = new String[]{age};
                }else if (TextUtils.isEmpty(age) && !TextUtils.isEmpty(name)){
                    sql = "name=?";
                    args = new String[]{name};
                }else {
                    sql = "name=? and age=?";
                    args = new String[]{name,age};
                }
                ContentValues contentValues = new ContentValues();
                contentValues.put("name",newName);
                contentValues.put("age",newAge);
                int num = dataBase.update("person",contentValues,sql,args);
                if (num > 0){
                    Toast.makeText(SqliteActivity.this,"修改记录成功",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(SqliteActivity.this,"未找到要修改的记录",Toast.LENGTH_LONG).show();
                    return;
                }

                // 清空输入框
                nameEditText.setText("");
                ageEditText.setText("");

                // 关闭数据库
                dataBase.close();

                // 重新查询数据，更新列表数据
                selectData(null);
            }
        });

        builder.show();
    }

    // 查询记录
    public void selectData(View view){
        SQLiteDatabase database = db.getReadableDatabase();

        // 从数据库查询数据

        // 方式一  使用sql命令
//        String sql = "";
//        String[] args = null;
//        String nameValue = nameEditText.getText().toString();
//        String ageValue = ageEditText.getText().toString();
//        boolean nameEmpty = TextUtils.isEmpty(nameValue);
//        boolean ageEmpty = TextUtils.isEmpty(ageValue);
//        if (!nameEmpty && !ageEmpty){
//            sql = "select * from person where name=? and age=?";
//            args = new String[]{nameValue,ageValue};
//        }else if (nameEmpty && !ageEmpty){
//            sql = "select * from person where age=?";
//            args = new String[]{ageValue};
//        }else if (!nameEmpty && ageEmpty){
//            sql = "select * from person where name=?";
//            args = new String[]{nameValue};
//        }else {
//            sql = "select * from person";
//        }
//        cursor = database.rawQuery(sql,args);

        // 方式二 使用官方封装好的
        String sql = "";
        String[] args = null;
        String nameValue = nameEditText.getText().toString();
        String ageValue = ageEditText.getText().toString();
        boolean nameEmpty = TextUtils.isEmpty(nameValue);
        boolean ageEmpty = TextUtils.isEmpty(ageValue);
        if (!nameEmpty && !ageEmpty){
            sql = "name=? and age=?";
            args = new String[]{nameValue,ageValue};
        }else if (nameEmpty && !ageEmpty){
            sql = "age=?";
            args = new String[]{ageValue};
        }else if (!nameEmpty && ageEmpty){
            sql = "name=?";
            args = new String[]{nameValue};
        }else {
            sql = null;
        }
        cursor = database.query("person",null,sql,args,null,null,null);

        // 更新列表数据
        adapter.changeCursor(cursor);

        // 查询到的行数
        int rowCount = cursor.getCount();
        if (cursor == null || rowCount <= 0){
            Toast.makeText(this,"未查询到结果",Toast.LENGTH_SHORT).show();
            return;
        }

        // 打印查询结果
        StringBuilder builder = new StringBuilder();
        while (cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String age = cursor.getString(cursor.getColumnIndex("age"));

            builder.append(name + "\n");
            builder.append(age + "\n");
        }
        Log.v("vTag",builder.toString());

        // 关闭数据库
        database.close();
    }

    // 判断姓名和年龄是否都为空
    public boolean isInfCorrCect() {
        String name = nameEditText.getText().toString();
        String age = ageEditText.getText().toString();

        if (TextUtils.isEmpty(name) && TextUtils.isEmpty(age)){
            Toast.makeText(this,"姓名和年龄至少填写一项",Toast.LENGTH_LONG).show();
            return false;
        }else {
            return true;
        }
    }
}
