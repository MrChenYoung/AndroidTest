package helloworld.android.com.androidtest;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

public class SharedPreferencesActivity extends Activity {
    private EditText nameEditText;
    private EditText codeEditText;
    private SharedPreferences sp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharedpreferences);

        // 初始化sp
        sp = getSharedPreferences("myPreferences", Context.MODE_PRIVATE);

        nameEditText = findViewById(R.id.editText1);
        codeEditText = findViewById(R.id.editText2);

        readData(null);

    }

    // 存储数据
    public void saveData(View view){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("name",nameEditText.getText().toString());
        editor.putString("code",codeEditText.getText().toString());

        nameEditText.setText("");
        codeEditText.setText("");

        editor.commit();
    }

    // 读取数据
    public void readData(View view){

        String name = sp.getString("name","");
        String code = sp.getString("code","");

        nameEditText.setText(name);
        codeEditText.setText(code);
    }
}
