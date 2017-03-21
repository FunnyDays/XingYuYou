package com.xingyuyou.xingyuyou.activity;

import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.adapter.MainContentVPAdapter;
import com.xingyuyou.xingyuyou.base.BaseActivity;
import com.xingyuyou.xingyuyou.base.BaseFragment;
import com.xingyuyou.xingyuyou.fragment.CommunityFragment;
import com.xingyuyou.xingyuyou.fragment.OneFragment;
import com.xingyuyou.xingyuyou.fragment.TwoFragment;
import com.xingyuyou.xingyuyou.fragment.UserFragment;
import com.xingyuyou.xingyuyou.weight.CustomViewPager;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    private FragmentManager supportFragmentManager  = getSupportFragmentManager();
    private ArrayList<BaseFragment> fragments;
    public static BottomNavigationBar bottomNavigationBar;
    private CustomViewPager customViewPager;
    private MainContentVPAdapter adapter;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // initToolbar();
        initView();
    }

   /* private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("星宇助手");
        mToolbar.inflateMenu(R.menu.all_tab_fragment_menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_download:
                        IntentUtils.startActivity(MainActivity.this, DownLoadActivity.class);
                        break;
                    case R.id.ab_search:
                        IntentUtils.startActivity(MainActivity.this, SearchActivity.class);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }*/

    private void initView() {
        getFragments();
        customViewPager = (CustomViewPager) findViewById(R.id.main_fragment);
        customViewPager.setAdapter(adapter);
        customViewPager.setOffscreenPageLimit(3);//设置缓存页数，缓存所有fragment
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.mipmap.bangdan, "榜单").setActiveColorResource(R.color.colorPrimary))
                .addItem(new BottomNavigationItem(R.mipmap.fenlei, "分类").setActiveColorResource(R.color.colorPrimary))
                .addItem(new BottomNavigationItem(R.mipmap.shequ, "社区").setActiveColorResource(R.color.colorPrimary))
                .addItem(new BottomNavigationItem(R.mipmap.guanli, "管理").setActiveColorResource(R.color.colorPrimary))
                .setMode(BottomNavigationBar.MODE_FIXED)//设置底部代文字显示模式。MODE_DEFAULT默认MODE_FIXED代文字MODE_SHIFTING不带文字
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)//背景模式BACKGROUND_STYLE_RIPPLE涟漪BACKGROUND_STYLE_STATIC静态
                .initialise();
        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            //当前的选中的tab
            @Override
            public void onTabSelected(int position) {
                switch (position) {
                    case 0:
                        customViewPager.setCurrentItem(0);

                        break;
                    case 1:
                        customViewPager.setCurrentItem(1);

                        break;
                    case 2:
                        customViewPager.setCurrentItem(2);

                        break;
                    case 3:
                        customViewPager.setCurrentItem(3);

                        break;
                }

            }

            //上一个选中的tab
            @Override
            public void onTabUnselected(int position) {
                Log.i("tab", "onTabUnselected position:" + position);

            }

            //当前tab被重新选中
            @Override
            public void onTabReselected(int position) {
                Log.i("tab", "onTabReselected position:" + position);
                fragments.get(position).initData();
            }
        });
    }
    private ArrayList<BaseFragment> getFragments() {
        fragments = new ArrayList<>();
        fragments.add(OneFragment.newInstance("榜单"));
        fragments.add(TwoFragment.newInstance("分类"));
        fragments.add(CommunityFragment.newInstance("社区"));
        fragments.add(UserFragment.newInstance("管理"));
        adapter = new MainContentVPAdapter(supportFragmentManager, fragments);
        return fragments;
    }
    
}
