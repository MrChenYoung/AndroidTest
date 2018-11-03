package com.androidtest.Demos.ContentProviderDemos;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ContactsUtils {

    // 上下文
    private Context context;

    public ContactsUtils(Context context){
        this.context = context;
    }

    // 获取所有的联系人
    public ArrayList<ContactInfo> getAllContacts(){
        ArrayList<ContactInfo> contacts = new ArrayList<>();

        // 获取所有的联系人id
        ArrayList<String> contactsId = getContactIds();

        // 通过联系人id获取联系人信息
        for(int i = 0; i < contactsId.size(); i++){
            String contId = contactsId.get(i);
            ContactInfo contactInfo = getContactInfo(contId);
            contacts.add(contactInfo);
        }

        return contacts;
    }

    // 从raw_contacts表中获取所有联系人的id
    private ArrayList<String> getContactIds(){
        ArrayList<String> arrayList = new ArrayList<>();

        // 用内容解析者从raw_contacts表中获取id
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Cursor cursor = context.getContentResolver().query(uri,new String[]{"contact_id"},null,null,null);
        if (cursor != null && cursor.getCount() > 0){

            while (cursor.moveToNext()){
                // 获取联系人id
                String contactId = cursor.getString(cursor.getColumnIndex("contact_id"));
                // 已经删除的联系人contactId会被置空,所以这要判断一下
                if (contactId != null){
                    arrayList.add(contactId);
                }
            }
        }

        return arrayList;
    }

    // 根据联系人id获取联系人信息
    private ContactInfo getContactInfo(String contactId){
        ContactInfo contactInfo = new ContactInfo();

        // 从data表中根据联系人id查询联系人信息
        Uri uri = Uri.parse("content://com.android.contacts/data");
        Cursor cursor = context.getContentResolver().query(uri,null,"contact_id=?",new String[]{contactId},null);

        if (cursor != null && cursor.getCount() > 0){
            // 所有的电话号码
            ArrayList<String> phones = new ArrayList<>();

            while (cursor.moveToNext()){
                // 数据
                String data = cursor.getString(cursor.getColumnIndex("data1"));
                // 类型
                String mimetype = cursor.getString(cursor.getColumnIndex("mimetype"));

                Log.e("tag","类型=====:" + mimetype + " 值===:" + data);

                if (mimetype.equals("vnd.android.cursor.item/phone_v2")){
                    // 手机号码
                    phones.add(data);
                }else if (mimetype.equals("vnd.android.cursor.item/name")){
                    // 联系人姓名
                    contactInfo.setName(data);
                }else if (mimetype.equals("vnd.android.cursor.item/email_v2")){
                    // email
                    contactInfo.setEmail(data);
                }else if (mimetype.equals("vnd.android.cursor.item/organization")){
                    // 公司
                    contactInfo.setOrganization(data);
                }else if (mimetype.equals("vnd.android.cursor.item/website")){
                    // 网址
                    contactInfo.setWebsite(data);
                }else if (mimetype.equals("vnd.android.cursor.item/nickname")){
                    // 昵称
                    contactInfo.setNickName(data);
                }else if (mimetype.equals("vnd.android.cursor.item/postal-address_v2")){
                    // 住址
                    contactInfo.setAddress(data);
                }
            }
            contactInfo.setPhones(phones);
        }

        return contactInfo;
    }
}
