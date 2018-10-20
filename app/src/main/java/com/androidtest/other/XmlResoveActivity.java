package com.androidtest.other;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.VoiceInteractor;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
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
    // 姓名和年龄输入框
    private EditText editText_name;
    private EditText editText_age;

    private File desFile;

    // adapter
    private ArrayAdapter<String> adapter = null;

    // 数据
    private List<Person> data = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xmlresove);

        // 获取姓名和年龄输入框
        editText_name = findViewById(R.id.nameEditText);
        editText_age = findViewById(R.id.ageEditText);

        // 获取listview
        ListView listView = findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);

        // 获取xml文件存储路径
        File dir = Environment.getExternalStorageDirectory();
        desFile = new File(dir,"person.xml");
        if (!desFile.exists()){
            try {
                desFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 获取已经存储的数据展示到列表
        saxResove(null);
    }

    // 保存数据
    public void saveData(View view){
        String name = editText_name.getText().toString();
        String age = editText_age.getText().toString();

        // 姓名和年龄不能为空
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(age)){
            Toast.makeText(this,"姓名和年龄不能为空",Toast.LENGTH_SHORT).show();
            return;
        }

        if (!desFile.exists()){
            Toast.makeText(this,"指定目录不存在",Toast.LENGTH_SHORT).show();
            return;
        }

        // 把新数据追加到data
        Person p = new Person();
        p.setName(name);
        p.setAge(age);
        data.add(p);

        // 存储data到xml文件
        XmlSerializer serializer = Xml.newSerializer();

        // 设置输出流对象
        try {
            FileOutputStream fos = new FileOutputStream(desFile,false);
            serializer.setOutput(fos,"utf-8");

            // 文件开始
            serializer.startDocument("utf-8",true);

            serializer.startTag(null,"persons");

            for (Person person : data) {
                // 设置person节点
                serializer.startTag(null,"person");

                // name节点
                serializer.startTag(null,"name");
                serializer.text(person.getName());
                serializer.endTag(null,"name");

                // age节点
                serializer.startTag(null,"age");
                serializer.text(person.getAge());
                serializer.endTag(null,"age");

//            serializer.attribute(null,"name",name);
//            serializer.attribute(null,"age",age);
                serializer.endTag(null,"person");
            }

            serializer.endTag(null,"persons");

            // 文件结束
            serializer.endDocument();
            fos.close();

            // 刷新页面数据
            saxResove(null);

            // 提示数据存储成功
            Toast.makeText(this,"存储成功",Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // sax方式解析xml
    public void saxResove(View view){

        // 先清空数据
        adapter.clear();

        XmlPullParser parser = Xml.newPullParser();

        // 初始化解析器
        try {
            FileInputStream inputStream = new FileInputStream(desFile);
            parser.setInput(inputStream,"utf-8");

            Person p = null;
            StringBuilder stringBuilder = null;

            int type = parser.getEventType();
            while (type != XmlPullParser.END_DOCUMENT){
                if (type == XmlPullParser.START_TAG){
                    // 开始标签
                    if ("person".equals(parser.getName())){
                        p = new Person();
                        stringBuilder = new StringBuilder();
                    }else if ("name".equals(parser.getName())){
//                        stringBuilder.append("姓名:" + parser.getAttributeValue(null,"name") + "\n");
                        String name = parser.nextText();
                        stringBuilder.append("姓名:" + name + "\n");
                        p.setName(name);
                    }else if("age".equals(parser.getName())){
                        String age = parser.nextText();
                        stringBuilder.append("年龄:" + age + "\n");
                        p.setAge(age);
                    }
                }else if(type == XmlPullParser.END_TAG){
                    if ("person".equals(parser.getName())){
                        // 更新列表数据
                        adapter.add(stringBuilder.toString());
                        data.add(p);
                    }
                }

                type = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this,"解析出错",Toast.LENGTH_LONG).show();
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

class Person {
    private String name;
    private String age;

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
