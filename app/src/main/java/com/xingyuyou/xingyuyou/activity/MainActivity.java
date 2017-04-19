package com.xingyuyou.xingyuyou.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.umeng.socialize.UMShareAPI;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.PermissionsChecker;
import com.xingyuyou.xingyuyou.adapter.MainContentVPAdapter;
import com.xingyuyou.xingyuyou.base.BaseActivity;
import com.xingyuyou.xingyuyou.base.BaseFragment;
import com.xingyuyou.xingyuyou.fragment.CommunityFragment;
import com.xingyuyou.xingyuyou.fragment.GiftsPackageFragment;
import com.xingyuyou.xingyuyou.fragment.OneFragment;
import com.xingyuyou.xingyuyou.fragment.TwoFragment;
import com.xingyuyou.xingyuyou.fragment.UserFragment;
import com.xingyuyou.xingyuyou.weight.CustomViewPager;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{
    private FragmentManager supportFragmentManager  = getSupportFragmentManager();
    private ArrayList<BaseFragment> fragments;
    public static BottomNavigationBar bottomNavigationBar;
    private CustomViewPager customViewPager;
    private MainContentVPAdapter adapter;
    private static final int REQUEST_CODE = 0; // 请求码
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPermissionsChecker = new PermissionsChecker(this);
        initView();
    }


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

    @Override protected void onResume() {
        super.onResume();
        // 缺少权限时, 进入权限配置页面
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //进入到这里代表没有权限.
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                //已经禁止提示了
                Toast.makeText(MainActivity.this, "您已禁止该权限，需要重新开启。", Toast.LENGTH_SHORT).show();
            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);

            }
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE:
                if(grantResults.length >0 &&grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    //用户同意授权

                }else{
                    //用户拒绝授权
                   // Toast.makeText(this, "您已拒绝访问sd卡权限，会导致无法下载游戏", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    private long firstTime=0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK && event.getAction()==KeyEvent.ACTION_DOWN){
            if (System.currentTimeMillis()-firstTime>2000){
                Toast.makeText(MainActivity.this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
                firstTime=System.currentTimeMillis();
            }else{
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.post) {
            Toast.makeText(this, "ahhaha"+id, Toast.LENGTH_SHORT).show();
        } else if (id == R.id.see) {
            Toast.makeText(this, "ahhaha"+id, Toast.LENGTH_SHORT).show();

        } else if (id == R.id.collect) {
            Toast.makeText(this, "ahhaha"+id, Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_share) {
            Toast.makeText(this, "ahhaha"+id, Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_send) {
            Toast.makeText(this, "ahhaha"+id, Toast.LENGTH_SHORT).show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
