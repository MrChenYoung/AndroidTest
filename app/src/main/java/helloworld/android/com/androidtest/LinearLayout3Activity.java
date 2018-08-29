package helloworld.android.com.androidtest;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class LinearLayout3Activity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 代码和xml混合

        // 创建线性布局
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        // 添加从xml配置好的字试图
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_linearlayoutinclude,null);
        linearLayout.addView(view);

        setContentView(linearLayout);
    }
}

