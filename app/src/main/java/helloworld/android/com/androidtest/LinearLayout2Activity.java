package helloworld.android.com.androidtest;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LinearLayout2Activity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 纯代码创建线性布局
        LinearLayout linearLayout = new LinearLayout(this);

        // 设置垂直布局
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        linearLayout.setLayoutParams(params);

        // 与当前activity关联
        setContentView(linearLayout);

        // 添加子试图
        TextView textView = new TextView(this);
        textView.setText("子试图1");
        linearLayout.addView(textView);

        TextView textView1 = new TextView(this);
        textView1.setText("子试图2");
        linearLayout.addView(textView1);
    }
}
