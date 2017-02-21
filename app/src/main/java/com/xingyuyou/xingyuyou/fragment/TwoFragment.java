package com.xingyuyou.xingyuyou.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.IntentUtils;
import com.xingyuyou.xingyuyou.activity.DownLoadActivity;
import com.xingyuyou.xingyuyou.activity.MainActivity;
import com.xingyuyou.xingyuyou.activity.SearchActivity;
import com.xingyuyou.xingyuyou.adapter.MainVPAdapter;
import com.xingyuyou.xingyuyou.adapter.TabsViewPagerAdapter;
import com.xingyuyou.xingyuyou.base.BaseFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2016/6/28.
 */
public class TwoFragment extends BaseFragment {
    private TabLayout mTab;
    private Toolbar mToolbar;
    private ViewPager mContent;
    private List<BaseFragment> mFragments;
    public Map<String, Integer> mClasses = new HashMap<String, Integer>();
    private List<String> mTitles;
    private boolean isHide;
    public static TwoFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        TwoFragment fragment = new TwoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_two, null);
    }

    @Override
    protected View initView() {
        return null;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        minitView();
        minitData();
    }

    private void minitView() {
        mToolbar = (Toolbar) getView().findViewById(R.id.toolbar);
        initToolbar();
        mTab = (TabLayout) getView().findViewById(R.id.tabs);
        mContent = (ViewPager) getView().findViewById(R.id.viewpager);
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
    private void minitData() {
        mFragments = new ArrayList<BaseFragment>();
        mTitles = new ArrayList<String>();
        OnlineGameFragment hf = new OnlineGameFragment();
        AloneGameFragment hf1 = new AloneGameFragment();
        mFragments.add(hf);
        mFragments.add(hf1);
        mTitles.add("网游");
        mTitles.add("单机");
        mContent.setAdapter(new MainVPAdapter(getChildFragmentManager(), mFragments, mTitles));
        mTab.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorControlNormal));
        mTab.setTabMode(TabLayout.MODE_FIXED);
        mTab.setupWithViewPager(mContent);
        /*mTab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mFragments.get(tab.getPosition()).initData();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        })*/;
    }


}
