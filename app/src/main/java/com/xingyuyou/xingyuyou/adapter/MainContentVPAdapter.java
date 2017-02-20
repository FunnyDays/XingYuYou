package com.xingyuyou.xingyuyou.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.xingyuyou.xingyuyou.base.BaseFragment;

import java.util.ArrayList;


/**
 * Created by Administrator on 2016/10/11.
 */
public class MainContentVPAdapter extends FragmentPagerAdapter {
    private ArrayList<BaseFragment> fragments;
    public MainContentVPAdapter(FragmentManager fm, ArrayList<BaseFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
