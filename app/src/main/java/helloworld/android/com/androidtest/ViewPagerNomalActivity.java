package helloworld.android.com.androidtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerNomalActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpagenomal);

        // 设置三个页面
        List<View> views = new ArrayList<>();
        views.add(View.inflate(this,R.layout.activity_viewpager_item1,null));
        views.add(View.inflate(this,R.layout.activity_viewpager_item2,null));
        views.add(View.inflate(this,R.layout.activity_viewpager_item3,null));

        // 设置adapter
        ViewPager viewPager = findViewById(R.id.viewPager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(views,this);
        viewPager.setAdapter(adapter);

        // 设置viewPager的style
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        if (type.equals("scal")){
            viewPager.setPageTransformer(true,new ViewPagerScalTransformer());
        }else if(type.equals("rotate")) {
            viewPager.setPageTransformer(true,new ViewPagerRotateTransformer());
        }

        // 添加监听事件
        final int[] dots = {R.id.dot1,R.id.dot2,R.id.dot3};
        final ImageView[] dotViews = new ImageView[dots.length];
        for (int i = 0; i < dots.length; i++) {
            ImageView imageView = findViewById(dots[i]);
            dotViews[i] = imageView;
        }

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                for (int j = 0; j < dots.length; j++){
                    ImageView imageView = findViewById(dots[i]);
                    if (i == j){
                        dotViews[j].setImageResource(R.drawable.login_point_selected);
                    }else {
                        dotViews[j].setImageResource(R.drawable.login_point);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }
}
