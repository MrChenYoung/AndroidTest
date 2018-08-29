package helloworld.android.com.androidtest;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

public class TextViewActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_textview);

        // 设置成为第一焦点
        TextView textView = findViewById(R.id.textView1);
        textView.requestFocus();

    }
}