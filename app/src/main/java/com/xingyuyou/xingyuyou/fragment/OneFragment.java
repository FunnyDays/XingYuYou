package com.xingyuyou.xingyuyou.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.IntentUtils;
import com.xingyuyou.xingyuyou.activity.DownLoadActivity;
import com.xingyuyou.xingyuyou.activity.MainActivity;
import com.xingyuyou.xingyuyou.activity.SearchActivity;
import com.xingyuyou.xingyuyou.adapter.TabsViewPagerAdapter;
import com.xingyuyou.xingyuyou.base.BaseFragment;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/28.
 */
public class OneFragment extends BaseFragment {
    private Toolbar mToolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabsViewPagerAdapter mAdapter;
    private ArrayList<BaseFragment> fragments;
    public static OneFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        OneFragment fragment = new OneFragment();
        fragment.setArguments(args);
        return fragment;
    }
    /**
     * 初始化数据
     */
    @Override
    public void initData() {

    }


    @Override
    protected View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_one, null);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        initToolbar();
        //tablayout设置
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorControlNormal));
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        //viewpager适配器
        getFragments();
        mAdapter.addFragment(fragments.get(0), "热门");
        mAdapter.addFragment(fragments.get(1), "推荐");
        mAdapter.addFragment(fragments.get(2), "期待");
        viewPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.i("tab", "onTabSelected position:" + tab.getPosition());
                fragments.get(tab.getPosition()).initData();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.i("tab", "onTabUnselected position:" + tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.i("tab", "onTabReselected position:" + tab.getPosition());
            }
        });
        return view;
    }
    private void initToolbar() {
        mToolbar.setTitle("星宇助手");
        mToolbar.inflateMenu(R.menu.all_tab_fragment_menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_download:
                        IntentUtils.startActivity(mActivity, DownLoadActivity.class);
                        break;
                    case R.id.ab_search:
                        IntentUtils.startActivity(mActivity, SearchActivity.class);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private ArrayList<BaseFragment> getFragments() {
        fragments = new ArrayList<>();
        fragments.add(GameFragment.newInstance("热门"));
        fragments.add(GameFragment.newInstance("推荐"));
        fragments.add(GameFragment.newInstance("期待"));
        mAdapter = new TabsViewPagerAdapter(((MainActivity) mActivity).getSupportFragmentManager());
        return fragments;
    }

}
