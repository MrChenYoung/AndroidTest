package helloworld.android.com.androidtest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

public class XmlResoveActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xmlresove);

    }

    // sax方式解析xml
    public void saxResove(View view){
        XmlResourceParser xmlResourceParser = getResources().getXml(R.xml.users);

        StringBuilder stringBuilder = new StringBuilder();
        try {
            while (xmlResourceParser.getEventType() != XmlResourceParser.END_DOCUMENT){
                if (xmlResourceParser.getEventType() == XmlResourceParser.START_TAG){
                    if (xmlResourceParser.getName().equals("user")){
                        String name = xmlResourceParser.getAttributeValue(null,"name");
                        String age = xmlResourceParser.getAttributeValue(null,"age");
                        stringBuilder.append("姓名:" + name + "\n");
                        stringBuilder.append("年龄:" + age + "\n");
                    }
                }

                xmlResourceParser.next();
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("解析的xml内容");
            builder.setMessage(stringBuilder.toString());
            builder.setPositiveButton("确定",null);
            AlertDialog dialog = builder.create();
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
