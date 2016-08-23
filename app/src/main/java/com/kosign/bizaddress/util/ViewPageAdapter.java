package com.kosign.bizaddress.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Hyeongpil on 2016. 7. 27..
 */
public class ViewPageAdapter extends FragmentPagerAdapter {
    private List<Fragment> items;
    private List<String> titles;

    public ViewPageAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
        super(fm);
        if (fragments == null) throw new IllegalArgumentException("Data Must Not be Null");
        this.items = fragments;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return items.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }
}