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
import com.xingyuyou.xingyuyou.Utils.IntentUtils;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;

public class SettingActivity extends AppCompatActivity {

    private RelativeLayout mUpdate;
    private Toolbar mToolbar;
    private Button mBtLoginOut;

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
        //检查更新
        mUpdate = (RelativeLayout) findViewById(R.id.rl_check_update);
        mUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Toast.makeText(SettingActivity.this, "已经是最新版本", Toast.LENGTH_SHORT).show();
            }
        });


    }

}
