package com.xingyuyou.xingyuyou.fragment;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.IntentUtils;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.glide.GlideCircleTransform;
import com.xingyuyou.xingyuyou.activity.DownLoadActivity;
import com.xingyuyou.xingyuyou.activity.MainActivity;
import com.xingyuyou.xingyuyou.activity.SearchActivity;
import com.xingyuyou.xingyuyou.activity.ManagementActivity;
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
    private ImageView mIvManage;
    private TextView mTvUserAccount;

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
        mAdapter.addFragment(fragments.get(0), "推荐");
        mAdapter.addFragment(fragments.get(1), "精品");
        mAdapter.addFragment(fragments.get(2), "最新");
        viewPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(viewPager);

        //登陆账号设置
        mTvUserAccount = (TextView) view.findViewById(R.id.tv_user_account);
        mTvUserAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.startActivity(mActivity,ManagementActivity.class);
                mActivity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        mIvManage = (ImageView) view.findViewById(R.id.iv_manage);
        mIvManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.startActivity(mActivity,ManagementActivity.class);
                mActivity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        //设置Nva
       /* DrawerLayout drawer = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                mActivity, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
       // toggle.syncState();

        NavigationView navigationView = (NavigationView) view.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) mActivity);

*/
        return view;
    }
    private void initToolbar() {
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
        fragments.add(RecommendWithCoverFragment.newInstance("推荐"));
        fragments.add(HotGameWithCoverFragment.newInstance("精品"));
        fragments.add(NewGameFragment.newInstance("最新"));
        mAdapter = new TabsViewPagerAdapter(((MainActivity) mActivity).getSupportFragmentManager());
        return fragments;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (UserUtils.logined()){
            mTvUserAccount.setText(UserUtils.getNickName());
            Glide.with(mActivity)
                    .load(UserUtils.getUserPhoto())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transform(new GlideCircleTransform(mActivity))
                    .dontAnimate()
                    .into(mIvManage);
        }else {
            mTvUserAccount.setText("未登陆");
            Glide.with(mActivity)
                    .load(R.drawable.ic_user_defalut)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transform(new GlideCircleTransform(mActivity))
                    .dontAnimate()
                    .into(mIvManage);
        }
    }
}
