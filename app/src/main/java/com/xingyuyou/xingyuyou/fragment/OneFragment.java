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
        mAdapter.addFragment(fragments.get(2), "最新");
        viewPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }
    private void initToolbar() {
        mToolbar.setTitle("星宇游");
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
        fragments.add(HotGameFragment.newInstance("热门"));
        fragments.add(RecommendGameFragment.newInstance("推荐"));
        fragments.add(NewGameFragment.newInstance("最新"));
        mAdapter = new TabsViewPagerAdapter(((MainActivity) mActivity).getSupportFragmentManager());
        return fragments;
    }

}
