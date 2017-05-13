package com.xingyuyou.xingyuyou.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.AppUtils;
import com.xingyuyou.xingyuyou.Utils.IntentUtils;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;

public class SettingActivity extends AppCompatActivity {

    private RelativeLayout mUpdate;
    private Toolbar mToolbar;
    private Button mBtLoginOut;
    private RelativeLayout mFeedBack;
    private RelativeLayout mCleanCache;
    private RelativeLayout mVersionCode;
    private RelativeLayout mUserRule;
    private RelativeLayout mAboutUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_setting);
        initView();
    }

    private void initView() {

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("设置");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //个人资料
        mUpdate = (RelativeLayout) findViewById(R.id.rl_user_info);
        mUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserUtils.logined()) {
                    IntentUtils.startActivity(SettingActivity.this, UserInfoActivity.class);
                } else {
                    IntentUtils.startActivity(SettingActivity.this, LoginActivity.class);
                }
            }
        });
        //反馈
        mFeedBack = (RelativeLayout) findViewById(R.id.rl_feed_back);
        mFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserUtils.logined()) {
                    IntentUtils.startActivity(SettingActivity.this, FeedBackActivity.class);
                } else {
                    IntentUtils.startActivity(SettingActivity.this, LoginActivity.class);
                }
            }
        });
        //清楚缓存
        mCleanCache = (RelativeLayout) findViewById(R.id.rl_clean_cache);
        mCleanCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SettingActivity.this, "缓存清除成功", Toast.LENGTH_SHORT).show();
            }
        });
        //检查版本号
        mVersionCode = (RelativeLayout) findViewById(R.id.rl_version_code);
        mVersionCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Toast.makeText(SettingActivity.this, "当前版本号是："+AppUtils.getAppVersionName(SettingActivity.this), Toast.LENGTH_LONG).show();
            }
        });
        //用户公约
        mUserRule = (RelativeLayout) findViewById(R.id.rl_user_rule);
        mUserRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.startActivity(SettingActivity.this, ExemptionActivity.class);
            }
        });
        //检查更新
        mUpdate = (RelativeLayout) findViewById(R.id.rl_check_update);
        mUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Toast.makeText(SettingActivity.this, "已经是最新版本", Toast.LENGTH_SHORT).show();
            }
        });
        //关于我们
        mAboutUs = (RelativeLayout) findViewById(R.id.rl_about_us);
        mAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.startActivity(SettingActivity.this, AboutActivity.class);
            }
        });
        //退出登录
        mBtLoginOut = (Button) findViewById(R.id.bt_login_out);
        mBtLoginOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserUtils.LoginOut();
                finish();
                Toast.makeText(SettingActivity.this, "退出登录成功", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
