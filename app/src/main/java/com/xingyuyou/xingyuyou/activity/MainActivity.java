package com.xingyuyou.xingyuyou.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.umeng.socialize.UMShareAPI;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.AppUtils;
import com.xingyuyou.xingyuyou.Utils.IntentUtils;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.PermissionsChecker;
import com.xingyuyou.xingyuyou.Utils.SPUtils;
import com.xingyuyou.xingyuyou.Utils.TimeUtils;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.adapter.MainContentVPAdapter;
import com.xingyuyou.xingyuyou.base.BaseActivity;
import com.xingyuyou.xingyuyou.base.BaseFragment;
import com.xingyuyou.xingyuyou.fragment.CommunityFragmentCopy;
import com.xingyuyou.xingyuyou.fragment.GodFragment;
import com.xingyuyou.xingyuyou.fragment.OneFragment;
import com.xingyuyou.xingyuyou.fragment.ThreeFragment;
import com.xingyuyou.xingyuyou.fragment.TwoFragment;
import com.xingyuyou.xingyuyou.fragment.UserFragment;
import com.xingyuyou.xingyuyou.weight.CustomViewPager;
import com.xingyuyou.xingyuyou.weight.ProgressButton;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import okhttp3.Call;

public class MainActivity extends BaseActivity {
    private FragmentManager supportFragmentManager = getSupportFragmentManager();
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
    private String mAppDownload;
    private String mVersionText;
    private int mVersionCode;
    private String mUpdateInfo;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if (jsonObject.getString("status").equals("1")) {
                        JSONObject object = jsonObject.getJSONObject("msg");

                        mAppDownload = object.getString("app_download");
                        mVersionText = object.getString("version");
                        mUpdateInfo = object.getString("app_name");
                        if (Integer.valueOf(mVersionText) > mVersionCode) {
                            ToUpadte();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (msg.what == 2) {
                mPopWindow.showAtLocation(customViewPager, Gravity.CENTER, 0, 0);
            }
        }
    };
    private TextView mTvUpdateInfo;
    private ProgressButton mBtUpdate;
    private AlertDialog mAlertDialog;
    private SPUtils mConfig_def;
    private PopupWindow mPopWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mVersionCode = AppUtils.getAppVersionCode(this);
        mPermissionsChecker = new PermissionsChecker(this);
        initView();
        checkUpdate();
       // firstStartForDay();
    }

    private void firstStartForDay() {
        mConfig_def = new SPUtils("config_def");
        String sameDayTime = mConfig_def.getString("sameDayTime", "777");
        if (TimeUtils.isSameDay(sameDayTime)) {
           // Toast.makeText(this, "同一天", Toast.LENGTH_SHORT).show();
        } else {
            mConfig_def.putBoolean("isSig", false);
            View popupView = LayoutInflater.from(this).inflate(R.layout.popup_signatures_layout, null);
            mPopWindow = new PopupWindow(popupView,
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
            mPopWindow.setBackgroundDrawable(getResources().getDrawable(R.color.custom_gray));
            mHandler.sendEmptyMessageDelayed(2, 2000);
            //签到按钮
            Button btSig = (Button) popupView.findViewById(R.id.bt_sig);
            btSig.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (UserUtils.logined()) {
                        sigDay();
                    } else {
                        IntentUtils.startActivity(MainActivity.this, LoginActivity.class);
                    }
                }
            });
            //取消签到
            ImageView ivClose = (ImageView) popupView.findViewById(R.id.iv_close);
            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPopWindow.dismiss();
                }
            });
        }

    }

    private void sigDay() {
        OkHttpUtils.post()//
                .url(XingYuInterface.USER_SIGN)
                .addParams("uid", UserUtils.getUserId())
                .tag(this)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mPopWindow.dismiss();
                        Toast.makeText(MainActivity.this, "签到成功", Toast.LENGTH_SHORT).show();
                        mConfig_def.putBoolean("isSig", true);
                    }
                });
    }


    private void initView() {
        getFragments();
        customViewPager = (CustomViewPager) findViewById(R.id.main_fragment);
        customViewPager.setAdapter(adapter);
        customViewPager.setOffscreenPageLimit(3);//设置缓存页数，缓存所有fragment
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.mipmap.youxi_app, "游戏").setActiveColorResource(R.color.colorPrimary))
                .addItem(new BottomNavigationItem(R.mipmap.shequ_app, "社区").setActiveColorResource(R.color.colorPrimary))
                .addItem(new BottomNavigationItem(R.mipmap.shenshe_app, "神社").setActiveColorResource(R.color.colorPrimary))
                .addItem(new BottomNavigationItem(R.mipmap.fenlei_app, "分类").setActiveColorResource(R.color.colorPrimary))
                .setMode(BottomNavigationBar.MODE_FIXED)//设置底部代文字显示模式。MODE_DEFAULT默认MODE_FIXED代文字MODE_SHIFTING不带文字.setInactiveIcon(getResources().getDrawable(R.drawable.ic_info_black_24dp)))
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
        fragments.add(OneFragment.newInstance("游戏"));
        fragments.add(TwoFragment.newInstance("社区"));
        fragments.add(GodFragment.newInstance("神社"));
        fragments.add(ThreeFragment.newInstance("分类"));
        adapter = new MainContentVPAdapter(supportFragmentManager, fragments);
        return fragments;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 缺少权限时, 进入权限配置页面
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //进入到这里代表没有权限.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //已经禁止提示了
                Toast.makeText(MainActivity.this, "您已禁止该权限，需要重新开启。", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //用户同意授权

                } else {
                    //用户拒绝授权
                    // Toast.makeText(this, "您已拒绝访问sd卡权限，会导致无法下载游戏", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private long firstTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - firstTime > 2000) {
                Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = System.currentTimeMillis();
            } else {
              //  mConfig_def.putString("sameDayTime", TimeUtils.getNowTimeString());
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

    private void checkUpdate() {
        OkHttpUtils.post()//
                .url(XingYuInterface.ABOUT_US)
                .tag(this)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("hot", e.toString() + ":e");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mHandler.obtainMessage(1, response).sendToTarget();
                        Log.e("xiazai", response + ":e");
                    }
                });
    }

    private void ToUpadte() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_update_app, null);
        builder.setView(view);
        mTvUpdateInfo = (TextView) view.findViewById(R.id.tv_update_info);
        mTvUpdateInfo.setText(mUpdateInfo);
        mBtUpdate = (ProgressButton) view.findViewById(R.id.bt_update);
        mBtUpdate.setTag(0);
        mBtUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((int) mBtUpdate.getTag() == 0) {
                    mBtUpdate.setTag(1);
                    toDownload();
                    Toast.makeText(MainActivity.this, "开始下载", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "正在下载，请稍后", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mAlertDialog = builder.create();
        mAlertDialog.setCancelable(false);
        mAlertDialog.show();
    }

    private void toDownload() {
        OkHttpUtils//
                .get()//
                .url(mAppDownload)//
                .build()//http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/%E5%8D%95%E6%9C%BA/Iter.apk
                .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), "xingyu.apk")//
                {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(MainActivity.this, "下载出错", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {

                        mBtUpdate.setProgress((int) (progress * 100));
                    }

                    @Override
                    public void onResponse(File response, int id) {
                    }

                    @Override
                    public void onAfter(int id) {
                        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/xingyu.apk";
                        AppUtils.installApp(MainActivity.this, path);
                        Toast.makeText(MainActivity.this, "下载完成", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
