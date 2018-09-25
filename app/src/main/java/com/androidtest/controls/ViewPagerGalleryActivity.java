package com.androidtest.controls;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import com.utiles.ImageUtils;
import helloworld.android.com.androidtest.R;

public class ViewPagerGalleryActivity extends Activity {

    private List<View> views;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager_gallery);

        // 加载每个页面
        loadViews();

        ViewPagerAdapter adapter = new ViewPagerAdapter(views,this);

        final ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

        // 设置3D画廊效果
        viewPager.setPageMargin(20);
        // 设置一页一屏显示的页数
        viewPager.setOffscreenPageLimit(3);
        viewPager.setPageTransformer(true,new ViewPagerGalleryTransformer());

        FrameLayout layout = findViewById(R.id.frameLayout);
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return viewPager.dispatchTouchEvent(event);
            }
        });
    }

    // 加载每个页面
    public void loadViews(){
        views = new ArrayList<>();
        views.add(getView(R.layout.activity_viewpager_item1,R.drawable.guide_1));
        views.add(getView(R.layout.activity_viewpager_item2,R.drawable.guide_2));
        views.add(getView(R.layout.activity_viewpager_item3,R.drawable.guide_3));
    }

    // 设置每个页面的图片,生成倒影
    public View getView(int layoutId,int imageId){
        View view = View.inflate(this,layoutId,null);
        ImageView imageView = view.findViewById(R.id.imageView);
        imageView.setImageBitmap(ImageUtils.getReverseBitmapById(this,imageId,0.2f));
        return view;
    }
}
