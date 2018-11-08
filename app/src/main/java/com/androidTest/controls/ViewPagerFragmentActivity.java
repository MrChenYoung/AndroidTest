package com.androidTest.controls;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.BezierPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

import com.androidTest.R;

public class ViewPagerFragmentActivity extends AppCompatActivity {
    private List<Fragment> frags;
    private List<String> titles;
    private ViewPager pager;
    private MagicIndicator indicator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpagerfragment);

        initData();
        initPager();
        initIndicator();

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                indicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                indicator.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                indicator.onPageScrollStateChanged(state);
            }
        });
        ViewPagerHelper.bind(indicator, pager);
    }

    private void initPager() {
        pager=(ViewPager) findViewById(R.id.frag_pager);
        ViewPagerFragmenAdapter adapter=new ViewPagerFragmenAdapter(getSupportFragmentManager(), frags);
        pager.setAdapter(adapter);
    }

    private void initIndicator() {
        indicator=(MagicIndicator) findViewById(R.id.top_indicator);
        CommonNavigator navigator=new CommonNavigator(this);
        navigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return frags.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int i) {
                SimplePagerTitleView titleView=new SimplePagerTitleView(context);
                titleView.setText(titles.get(i));
                titleView.setTextSize(18);
                titleView.setNormalColor(Color.LTGRAY);
                titleView.setSelectedColor(Color.WHITE);
                titleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pager.setCurrentItem(i);
                    }
                });
                return titleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                BezierPagerIndicator pagerIndicator=new BezierPagerIndicator(context);
                pagerIndicator.setColors(Color.MAGENTA, Color.YELLOW, Color.CYAN, Color.GREEN);
                return pagerIndicator;
            }
        });
        indicator.setNavigator(navigator);
    }

    private void initData() {
        frags=new ArrayList<>();
        frags.add(ViewPagerFragment.newInstance(R.layout.activity_viewpagerfragment_first));
        frags.add(ViewPagerFragment.newInstance(R.layout.activity_viewpagerfragment_second));
        frags.add(ViewPagerFragment.newInstance(R.layout.activity_viewpagerfragment_third));

        titles=new ArrayList<>();
        titles.add("First");
        titles.add("Second");
        titles.add("Third");
    }
}
