package com.androidtest.Demos.ContentProviderDemos;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import helloworld.android.com.androidtest.R;

public class SelectContactActivity extends AppCompatActivity {

    private Button btn;
    private TextView textView;

    // 从系统通讯录选择联系人请求吗
    private final int SELECTCONTACTREQUESTCODE = 100;

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
        Uri uri = Uri.parse("content://contacts/people");
        Intent intent = new Intent(Intent.ACTION_PICK,uri);
        startActivityForResult(intent, SELECTCONTACTREQUESTCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECTCONTACTREQUESTCODE){
            // 选择联系人完成
            if (data == null){
                Toast.makeText(getApplicationContext(),"取消选择",Toast.LENGTH_SHORT).show();
            }else {
                Uri uri = data.getData();
                Toast.makeText(getApplicationContext(),"选中了:" + uri.toString(),Toast.LENGTH_SHORT).show();
            }
        }
    }

//    private String[] getPhoneContacts(Uri uri){
//        String[] contact=new String[2];
//        ContentResolver cr = getContentResolver();
//        //取得联系人中第一项的光标
//        Cursor cursor=cr.query(uri,null,null,null,null);
//        if(cursor!=null)
//        {
//            cursor.moveToFirst();
//            //取得联系人姓名
//            int nameFieldColumnIndex=cursor.getColumnIndex(Contens.Contacts.DISPLAY_NAME);
//            contact[0]=cursor.getString(nameFieldColumnIndex);
//            //取得电话号码
//            String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
//            Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
//                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);
//            if(phone != null){
//                phone.moveToFirst();
//                contact[1] = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//            }
//            phone.close();
//            cursor.close();
//        }
//        else
//        {
//            return null;
//        }
//        return contact;
//    }
}
