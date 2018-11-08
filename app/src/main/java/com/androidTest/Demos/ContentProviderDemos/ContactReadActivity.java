package com.androidTest.Demos.ContentProviderDemos;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import com.androidTest.R;

public class ContactReadActivity extends AppCompatActivity {

    // 列表
    private ListView listView;
    // adapter
    private ContactsAdapter adapter = new ContactsAdapter();
    // 数据集合
    private ArrayList<ContactInfo> contactsList = new ArrayList<>();
    // 没有联系人信息的时候展示的视图
    private TextView emptyView;
    // 加载过程中的蒙版
    private RelativeLayout cover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_read);

        // 获取子控件
        listView = findViewById(R.id.listView);
        cover = findViewById(R.id.cover);
        emptyView = findViewById(R.id.emptyView);
        listView.setAdapter(adapter);

        // 注册内容观察者，联系人信息发生改变的时候刷新
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        getContentResolver().registerContentObserver(uri,false,new ContactContentObserver(new Handler()));
    }

    @Override
    protected void onStart() {
        super.onStart();

        // 加载联系人
        refreshList();
    }

    // 重新获取联系人,刷新列表
    public void refreshList(){
        // 显示蒙版
        cover.setVisibility(View.VISIBLE);

        // 起一个新线程加载联系人
        new Thread(){
            @Override
            public void run() {
                super.run();
                contactsList = new ContactsUtils(ContactReadActivity.this).getAllContacts();

                // 主线程更新界面
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (contactsList.size() > 0){
                            // 刷新列表
                            adapter.notifyDataSetChanged();

                            listView.setVisibility(View.VISIBLE);
                            emptyView.setVisibility(View.GONE);
                        }else {
                            listView.setVisibility(View.GONE);
                            emptyView.setVisibility(View.VISIBLE);
                        }

                        // 蒙版消失
                        cover.setVisibility(View.GONE);
                    }
                });
            }
        }.start();
    }

    // 自定义adapter
    private class ContactsAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return contactsList.size();
        }

        @Override
        public Object getItem(int position) {
            return contactsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView == null){
                view = getLayoutInflater().inflate(R.layout.contacts_item,null);
            }else {
                view = convertView;
            }

            // 赋值数据
            ContactInfo contactInfo = contactsList.get(position);

            TextView nameTextView = view.findViewById(R.id.tv_name);
            // 设置粗体
            nameTextView.getPaint().setFakeBoldText(true);
            TextView phoneTextView = view.findViewById(R.id.tv_phone);

            // 设置姓名和电话号
            nameTextView.setText(contactInfo.getName() == null ? "空" : contactInfo.getName());
            ArrayList<String> phones = contactInfo.getPhones();
            phoneTextView.setText(phones.size() > 0 ? phones.get(0) : "空");

            return view;
        }
    }

    // 内容观察者，观察联系人变化
    private class ContactContentObserver extends ContentObserver{
        public ContactContentObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);

            // 刷新列表
            refreshList();
        }
    }
}
