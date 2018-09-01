package helloworld.android.com.androidtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MyActivity2 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myactivity2);


        // 把上个页面传递过来的内容展示
        EditText editText = findViewById(R.id.editText);

        Intent intent = getIntent();
        String text = intent.getStringExtra("name");
        editText.setText(text);
    }

    // 点击返回上一个页面
    public void back(View view){
        EditText editText = findViewById(R.id.editText1);

        // 传递数据
        Intent intent = getIntent();
        intent.putExtra("name",editText.getText().toString());
        this.setResult(1,intent);

        // 当前页面消失
        this.finish();
    }

}
