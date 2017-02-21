package com.xingyuyou.xingyuyou.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.xingyuyou.xingyuyou.base.BaseFragment;

import java.util.List;

/**
 * Created by LG on 2016/11/27.
 */

public class MainVPAdapter extends FragmentPagerAdapter {
    private List<BaseFragment> mFragments;
    private List<String> keySet;
    public MainVPAdapter(FragmentManager fm, List<BaseFragment> fragments, List<String> keySet) {
        super(fm);
        mFragments = fragments;
        this.keySet = keySet;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return keySet.get(position);
    }
}
