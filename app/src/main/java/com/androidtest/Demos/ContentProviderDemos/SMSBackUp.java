package com.androidtest.Demos.ContentProviderDemos;

import android.database.ContentObservable;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import helloworld.android.com.androidtest.R;

public class SMSBackUp extends AppCompatActivity {

    // 没有短信是提示view
    private TextView emptyView;

    private ListView listView;
    private ArrayList<SMSInfo> smsLists = new ArrayList<>();
    private SMSAdapter adapter = new SMSAdapter();
    private RelativeLayout cover;
    private SMSContentObserver contentObserver = new SMSContentObserver(new Handler());
    private ArrayList<ContactInfo> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smsback_up);

        emptyView = findViewById(R.id.emptyView);
        cover = findViewById(R.id.cover);

        listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);

        // 注册短信内容观察者
        Uri uri = Uri.parse("content://sms/");
        getContentResolver().registerContentObserver(uri,true,contentObserver);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 获取手机联系人
        getContacts();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(contentObserver);
    }

    // 获取手机所有的短信
    // person 如果发件人在通讯录显示姓名，如果不在为null
    // address 发送短信电话号码
    // body 短信内容
    // date 短信发送日期
    // type 短信类型 1接收到的 2发出的
    public void getSms(){
        smsLists.clear();
        adapter.notifyDataSetChanged();

        cover.setVisibility(View.VISIBLE);
        new Thread(){
            @Override
            public void run() {
                super.run();

                Uri uri = Uri.parse("content://sms/");
                Cursor cursor = getContentResolver().query(uri,new String[]{"address","person","body","date","type"},null,null,null);
                if (cursor != null && cursor.getCount() > 0){
                    while (cursor.moveToNext()){
                        String address = cursor.getString(cursor.getColumnIndex("address"));
                        String person = cursor.getString(cursor.getColumnIndex("person"));
                        String body = cursor.getString(cursor.getColumnIndex("body"));
                        Long date = cursor.getLong(cursor.getColumnIndex("date"));
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                        String formateDate = dateFormat.format(new Date(date));
                        int type = cursor.getInt(cursor.getColumnIndex("type"));

                        if (address.equals("10086")){
                            address = "中国移动客服";
                        }else if (address.equals("10001")){
                            address = "中国电信客服";
                        }

                        //从通讯根据手机号查找姓名
                        for (int i = 0; i < contacts.size();i++){
                            ContactInfo contactInfo = contacts.get(i);
                            ArrayList<String> phones = contactInfo.getPhones();
                            for (String phone : phones){
                                if (phone.endsWith(address)){
                                    address = contactInfo.getName();
                                }
                            }
                        }

                        SMSInfo info = new SMSInfo();
                        info.setAddress(address);
                        info.setPerson(person);
                        info.setBody(body);
                        info.setDate(formateDate);
                        info.setType(type);
                        smsLists.add(info);

                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 刷新表格数据
                            cover.setVisibility(View.GONE);
                            emptyView.setVisibility(View.GONE);
                            listView.setVisibility(View.VISIBLE);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            cover.setVisibility(View.GONE);
                            emptyView.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.GONE);
                        }
                    });
                }
            }
        }.start();
    }

    // 自定义adapter
    private class SMSAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return smsLists.size();
        }

        @Override
        public Object getItem(int position) {
            return smsLists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView == null){
                view = getLayoutInflater().inflate(R.layout.sms_backup_item,null);
            }else {
                view = convertView;
            }

            // 设置短信地址，内容和时间
            TextView tv_address = view.findViewById(R.id.tv_address);
            TextView tv_body = view.findViewById(R.id.tv_body);
            TextView tv_date = view.findViewById(R.id.tv_date);
            ImageView icon = view.findViewById(R.id.icon);


            SMSInfo info = smsLists.get(position);
            int imageId = info.getType() == 1 ? R.drawable.sms_receive : R.drawable.sms_send;
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),imageId);
            icon.setImageBitmap(bitmap);
//            String title = info.getPerson() == null ? info.getAddress() : info.getPerson();
            String title = info.getAddress();
            tv_address.setText(title);
            tv_body.setText(info.getBody());
            tv_date.setText(info.getDate());

            return view;
        }
    }

    // 获取手机通讯录联系人
    public void getContacts(){
        cover.setVisibility(View.VISIBLE);
        new Thread(){
            @Override
            public void run() {
                super.run();
                contacts = new ContactsUtils(getApplicationContext()).getAllContacts();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cover.setVisibility(View.GONE);
                        // 获取短信，刷新数据
                        getSms();
                    }
                });
            }
        }.start();
    }

    // 内容观察者
    private class SMSContentObserver extends ContentObserver{
        public SMSContentObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);

            getSms();
        }
    }
}
