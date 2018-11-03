package com.androidtest.elements;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.VoiceInteractor;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import helloworld.android.com.androidtest.R;

public class ContentProviderActivity extends Activity {

    // 内容观察者
    private ContentObserverCustom contentObserver;
    // 内容观察者消息处理
    private ContentObserverHandle handle;

    // 注册内容观察者类型
    private final int CONTENTRESORVERALL = 1;
    private final int CONTENTRESORVERQUERY = 2;
    private final int CONTENTRESORVERINSERT = 3;
    private final int CONTENTRESORVERDELETE = 4;
    private final int CONTENTRESORVERUPDATE = 5;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contentprovider);
        // 初始化内容观察者
        handle = new ContentObserverHandle(this);
        contentObserver = new ContentObserverCustom(this,handle);
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

    // 注册所有操作的内容观察者
    public void registAllObserver(View view){
        reginstObserver(CONTENTRESORVERALL);
    }

    // 注册查询数据库的观察者
    public void registQueryObserver(View view){
        reginstObserver(CONTENTRESORVERQUERY);
    }

    // 注册插入数据内容观察者
    public void registInsertObserver(View view){
        reginstObserver(CONTENTRESORVERINSERT);
    }

    // 注册删除数据内容观察者
    public void registDeleteObserver(View view){
        reginstObserver(CONTENTRESORVERDELETE);
    }

    // 注册修改数据内容观察者
    public void registUpdateObserver(View view){
        reginstObserver(CONTENTRESORVERUPDATE);
    }

    // 注册内容观察者
    public void reginstObserver(int flag){
        // 先注销以前注册的内容观察者
        unregistObserver();

        String uriString = "content://com.AndroidAssistant.provider";
        boolean notifyForDescendants = false;
        switch (flag){
            case CONTENTRESORVERALL:
                // 注册所有操作观察者
                notifyForDescendants = true;
                break;
            case CONTENTRESORVERINSERT:
                // 注册插入操作观察者
                uriString = uriString + "/insert";
                notifyForDescendants = false;
                break;
            case CONTENTRESORVERDELETE:
                // 注册删除操作观察者
                uriString = uriString + "/delete";
                notifyForDescendants = false;
                break;
            case CONTENTRESORVERUPDATE:
                // 注册修改数据库操作观察者
                uriString = uriString + "/update";
                notifyForDescendants = false;
                break;
            case CONTENTRESORVERQUERY:
                // 注册查询数据操作观察者
                uriString = uriString + "/qury";
                notifyForDescendants = false;
                break;
        }

        // 参数：
        // 参数1 要观察的内容提供者
        // 参数2 true表示观察所有的操作,uri不需要具体到某一个操作 false表示观察具体的某一个操作,uri要传具体的
        // 参数3 观察者,接受观察结果
        getContentResolver().registerContentObserver(Uri.parse(uriString),notifyForDescendants, contentObserver);
        Toast.makeText(this,"注册内容观察者成功",Toast.LENGTH_SHORT).show();
    }


    // 注销内容观察者
    public void unregistObserver(){
        getContentResolver().unregisterContentObserver(contentObserver);
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
