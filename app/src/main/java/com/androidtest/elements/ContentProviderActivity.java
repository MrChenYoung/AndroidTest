package com.androidtest.elements;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import helloworld.android.com.androidtest.R;

public class ContentProviderActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contentprovider);
    }

    // 通过内容提供者查询辅助项目里面的私有数据库
    public void contentProviderQuery(View view){
        Uri uri = Uri.parse("content://com.AndroidAssistant.provider/qury");
        Cursor cursor = getContentResolver().query(uri,null,null,null,null);
        // 遍历查询结果
        if (cursor != null && cursor.getCount() > 0){
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("查询结果:\n");
            while (cursor.moveToNext()){
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String age = cursor.getString(cursor.getColumnIndex("age"));
                stringBuilder.append("姓名:" + name + "  " + "年龄:" + age + "\n");
            }

            cursor.close();

            // 查询结束
            new AlertDialog.Builder(this)
                    .setMessage(stringBuilder.toString())
                    .setPositiveButton("确定",null)
                    .show();
        }else {
            Toast.makeText(this,"链接远端数据库失败,请安装AndroidAssistant项目并运行",Toast.LENGTH_SHORT).show();
        }
    }

    // 通过内容提供者在远程数据库里插入一条数据
    public void contentProviderInsert(View view){
        Uri uri = Uri.parse("content://com.AndroidAssistant.provider/insert");
        ContentValues values = new ContentValues();
        values.put("name","曹操");
        values.put("age","50");
        Uri result = getContentResolver().insert(uri,values);
        long resultNum = Long.parseLong(result.toString());
        if (resultNum == -1){
            // 插入失败
            Toast.makeText(this,"插入失败",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this,"插入成功",Toast.LENGTH_SHORT).show();
        }
    }

    // 通过内容提供者修改远程数据库数据
    public void contentProviderUpdate(View view){
        Uri uri = Uri.parse("content://com.AndroidAssistant.provider/update");
        ContentValues values = new ContentValues();
        values.put("name","刘备");
        values.put("age","40");
        String where = "name = ?";
        String[] args = new String[]{"曹操"};
        int result = getContentResolver().update(uri,values,where,args);
        if (result == -1){
            // 修改失败
            Toast.makeText(this,"修改数据失败",Toast.LENGTH_SHORT).show();
        }else if (result == 0) {
            // 修改失败
            Toast.makeText(this,"未找到要修改的记录",Toast.LENGTH_SHORT).show();
        }else {
            // 修改成功
            Toast.makeText(this,"修改成功",Toast.LENGTH_SHORT).show();
        }
    }

    // 通过内容提供者删除远程数据库数据
    public void contentProviderDelete(View view){
        Uri uri = Uri.parse("content://com.AndroidAssistant.provider/delete");
        int result = getContentResolver().delete(uri,"name=?",new String[]{"刘备"});
        if (result == -1){
            // 删除失败
            Toast.makeText(this,"删除失败",Toast.LENGTH_SHORT).show();
        }else if(result == 0) {
            // 未找到要删除的记录
            Toast.makeText(this,"未找到要删除的记录",Toast.LENGTH_SHORT).show();
        }else {
            // 删除成功
            Toast.makeText(this,"删除成功",Toast.LENGTH_SHORT).show();
        }
    }

    // 获取联系人列表
    public void getContacts(View view){
        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);

        StringBuilder builder = new StringBuilder();
        while (cursor.moveToNext()){
            String s1 = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            builder.append(s1);
            builder.append("\r\n");
        }

        EditText editText = findViewById(R.id.editText);
        editText.setText(builder.toString());
    }
}
