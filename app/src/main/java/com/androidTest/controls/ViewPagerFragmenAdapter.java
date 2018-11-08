package com.androidTest.controls;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class ViewPagerFragmenAdapter extends FragmentPagerAdapter {
    private List<Fragment> datas;

    public ViewPagerFragmenAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        datas=list;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Fragment getItem(int position) {
        return datas.get(position);
    }
}
