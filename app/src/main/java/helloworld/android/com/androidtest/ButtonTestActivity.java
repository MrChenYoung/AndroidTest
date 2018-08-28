package helloworld.android.com.androidtest;

import android.app.Activity;
import android.app.Notification;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ButtonTestActivity extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button);

        // 2.内部类添加点击事件
        Button btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(new MyClickListener());

        // 3.匿名内部类添加点击事件
        Button btn3 = findViewById(R.id.button3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ButtonTestActivity.this,"匿名内部类方式添加点击事件",Toast.LENGTH_SHORT).show();
            }
        });

        // 4.实现OnClickListener接口方式添加点击事件
        Button btn4 = findViewById(R.id.button4);
        btn4.setOnClickListener(this);

        // 给按钮添加长按手势
        Button btn5 = findViewById(R.id.button5);
        btn5.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            // 返回false事件会往下传递,返回true事件不会往下传递
            public boolean onLongClick(View v) {
                Toast.makeText(ButtonTestActivity.this,"按钮被长按了",Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // 给按钮添加触摸事件
        Button btn6 = findViewById(R.id.button6);
        btn6.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN){
                    Toast.makeText(ButtonTestActivity.this,"按钮被按下",Toast.LENGTH_SHORT).show();
                }else if (action == MotionEvent.ACTION_MOVE){
                    Toast.makeText(ButtonTestActivity.this,"手势移动了",Toast.LENGTH_SHORT).show();
                }else if (action==MotionEvent.ACTION_UP){
                    Toast.makeText(ButtonTestActivity.this,"按钮被松开",Toast.LENGTH_SHORT).show();
                }else {

                }
                return false;
            }
        });

        // 给按钮添加键盘事件
        Button btn7 = findViewById(R.id.button7);
        btn7.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Toast.makeText(ButtonTestActivity.this,"当前点击按钮编码是:" + keyCode,Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // 给按钮添加焦点事件
        Button btn8 = findViewById(R.id.button8);
        btn8.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String msg = hasFocus ? "按钮获得焦点" : "按钮失去焦点";
                Toast.makeText(ButtonTestActivity.this,msg,Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 给按钮添加点击事件
    // 1.在配置文件中设置
    public void click1(View v){
        Toast.makeText(this,"在配置文件中配置按钮点击事件",Toast.LENGTH_SHORT).show();
    }

    // 重写onClick方法
    public void onClick(View v){
        Toast.makeText(this,"实现OnClickListener接口方式添加点击事件",Toast.LENGTH_SHORT).show();
    }

    // 内部类
    class MyClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Toast.makeText(ButtonTestActivity.this,"内部类方式添加点击事件",Toast.LENGTH_SHORT).show();
        }
    }
}
