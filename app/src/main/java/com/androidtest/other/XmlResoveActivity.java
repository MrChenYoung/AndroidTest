package com.androidtest.other;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.VoiceInteractor;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Xml;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.cert.Extension;
import java.util.ArrayList;
import java.util.List;

import helloworld.android.com.androidtest.R;

public class XmlResoveActivity extends Activity {
    private TextView nameTextView;
    private TextView ageTextView;

    private File desFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xmlresove);

        nameTextView = findViewById(R.id.nameEditText);
        ageTextView = findViewById(R.id.ageEditText);

        File dir = Environment.getExternalStorageDirectory();
        desFile = new File(dir,"person.xml");
        if (!desFile.exists()){
            try {
                desFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 保存数据
    public void saveData(View view){
        String name = nameTextView.getText().toString();
        String age = ageTextView.getText().toString();

        // 姓名和年龄不能为空
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(age)){
            Toast.makeText(this,"姓名和年龄不能为空",Toast.LENGTH_SHORT).show();
            return;
        }

        if (!desFile.exists()){
            Toast.makeText(this,"指定目录不存在",Toast.LENGTH_SHORT).show();
            return;
        }


        // 存储
        XmlSerializer serializer = Xml.newSerializer();

        // 设置输出流对象
        try {
            FileOutputStream fos = new FileOutputStream(desFile,false);
            serializer.setOutput(fos,"utf-8");

            // 文件开始
            serializer.startDocument("utf-8",true);

            serializer.startTag(null,"persons");

            // 设置person节点
            serializer.startTag(null,"person");
            serializer.attribute(null,"name",name);
            serializer.attribute(null,"age",age);
            serializer.endTag(null,"person");

            serializer.endTag(null,"persons");

            // 文件结束
            serializer.endDocument();

            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // sax方式解析xml
    public void saxResove(View view){
        XmlPullParser parser = Xml.newPullParser();

        // 初始化解析器
        try {
            FileInputStream inputStream = new FileInputStream(desFile);
            parser.setInput(inputStream,"utf-8");

            StringBuilder stringBuilder = new StringBuilder();

            int type = parser.getEventType();
            while (type != XmlPullParser.END_DOCUMENT){

                if (type == XmlPullParser.START_TAG){
                    if ("person".equals(parser.getName())){
                        stringBuilder.append("姓名:" + parser.getAttributeValue(null,"name") + "\n");
                        stringBuilder.append("年龄" + parser.getAttributeValue(null,"age") + "\n");
                    }
                }
                type = parser.next();
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("解析结果");
            builder.setMessage(stringBuilder.toString());
            builder.setPositiveButton("确定",null);

            AlertDialog dialog = builder.create();
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }



//        XmlResourceParser xmlResourceParser = getResources().getXml(R.xml.users);
//
//        StringBuilder stringBuilder = new StringBuilder();
//        try {
//            while (xmlResourceParser.getEventType() != XmlResourceParser.END_DOCUMENT){
//                if (xmlResourceParser.getEventType() == XmlResourceParser.START_TAG){
//                    if (xmlResourceParser.getName().equals("user")){
//                        String name = xmlResourceParser.getAttributeValue(null,"name");
//                        String age = xmlResourceParser.getAttributeValue(null,"age");
//                        stringBuilder.append("姓名:" + name + "\n");
//                        stringBuilder.append("年龄:" + age + "\n");
//                    }
//                }
//
//                xmlResourceParser.next();
//            }
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("解析的xml内容");
//            builder.setMessage(stringBuilder.toString());
//            builder.setPositiveButton("确定",null);
//            AlertDialog dialog = builder.create();
//            dialog.show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
