package com.androidtest.Demos.ContentProviderDemos;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.security.Permissions;
import java.util.ArrayList;

import helloworld.android.com.androidtest.BuildConfig;
import helloworld.android.com.androidtest.R;

public class SelectContactActivity extends Activity {

    private Button btn;
    private TextView textView;

    // 从系统通讯录选择联系人请求吗
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 2;
    private final int SELECTCONTACTREQUESTCODE = 100;
    private Uri resultUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contact);

        // 获取控件
        btn = findViewById(R.id.bt_selectContact);
        textView = findViewById(R.id.tv_selected);
    }

    // 打开通讯录
    public void openAddressBook(View view){
        Intent intent = new Intent(Intent.ACTION_PICK,ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, SELECTCONTACTREQUESTCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECTCONTACTREQUESTCODE){
            // 选择联系人完成
            if (data == null){
                Toast.makeText(getApplicationContext(),"取消选择联系人",Toast.LENGTH_SHORT).show();
            }else {
                resultUri = data.getData();

                // 检查权限
                checkPermission();
            }
        }

    }

    // 检查用户是否授予读取联系人权限
    public void checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) !=
        PackageManager.PERMISSION_GRANTED){
            //申请授权，第一个参数为要申请用户授权的权限；第二个参数为requestCode 必须大于等于0，主要用于回调的时候检测，匹配特定的onRequestPermissionsResult。
            //可以从方法名requestPermissions以及第二个参数看出，是支持一次性申请多个权限的，系统会通过对话框逐一询问用户是否授权。
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        }else {
            //如果该版本低于6.0，或者该权限已被授予，它则可以继续读取联系人
            setContact();
        }
    }

    // 请求权限结果回调
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // 用户授权
                setContact();
            }else {
                // 用户拒绝授权
                Toast.makeText(this,"请到设置里开启读取联系人权限",Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 设置选中的联系人到界面上
    public void setContact(){
        ContactInfo contactInfo = getContact(resultUri);
        String str = "姓名:" + contactInfo.getName() + "\n";
        StringBuilder stringBuilder = new StringBuilder(str);
        for(String number : contactInfo.getPhones()){
            stringBuilder.append("电话:" +  number + "\n");
        }
        textView.setText(stringBuilder.toString());
    }

    // 通过uri获取联系人信息
    public ContactInfo getContact(Uri uri){
        ContactInfo contactInfo = new ContactInfo();
        Cursor cursor = getContentResolver().query(uri,null,null,null,null);
        if (cursor.moveToFirst()){
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            if (hasPhone.endsWith("1")){
                ArrayList<String> phones = getPhones(id);
                contactInfo.setPhones(phones);
            }
            contactInfo.setName(name);
        }

        return contactInfo;
    }

    // 根据id获取电话号码
    public ArrayList<String> getPhones(String id){
        ArrayList<String> phones = new ArrayList<>();
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id,null,null);
        if(cursor != null && cursor.getColumnCount() > 0){
            while (cursor.moveToNext()){
                String phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                phones.add(phone);
            }
        }

        return phones;
    }

}
