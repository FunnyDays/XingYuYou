package com.xingyuyou.xingyuyou.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.IntentUtils;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.SPUtils;

public class ManagementActivity extends AppCompatActivity {
    private RelativeLayout mSetting;
    private RelativeLayout mUnInstall;
    private ImageView mUserPhoto;
    private RelativeLayout mFeedBack;
    private RelativeLayout mGameUpdate;
    private RelativeLayout mPoJie;
    private RelativeLayout mTool;
    private RelativeLayout mGameDownload;
    private RelativeLayout mAppShare;
    private RelativeLayout mAboutXingYu;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);
        initToolBar();
        initData();
    }
    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("管理");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void initData() {
        //显示用户信息
        if (UserUtils.logined()) {
            SPUtils user_data = new SPUtils("user_data");
            String id = user_data.getString("id");
            String account = user_data.getString("account");
            String nickname = user_data.getString("nickname");
            TextView tvUserAccountName = (TextView) findViewById(R.id.user_account_name);
            TextView tvNickName = (TextView) findViewById(R.id.user_nickname);
            tvUserAccountName.setText(account);
            tvNickName.setText(nickname);
        }

        //登录
        mUserPhoto = (ImageView) findViewById(R.id.user_photo);
        mUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserUtils.logined()) {
                    IntentUtils.startActivity(ManagementActivity.this, UserInfoActivity.class);
                } else {
                    IntentUtils.startActivity(ManagementActivity.this, LoginActivity.class);
                }
            }
        });
        //游戏更新
        /*mGameUpdate = (ImageView) findViewById(R.id.image_one);
        mGameUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //IntentUtils.startActivity(ManagementActivity.this, UninstallAppActivity.class);
                Toast.makeText(ManagementActivity.this, "功能暂未开放", Toast.LENGTH_SHORT).show();
            }
        });*/
        //加入官群
        mPoJie = (RelativeLayout) findViewById(R.id.rl_eight);
        mPoJie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinQQGroup("YzjlZwrRfUeZN0jnoSF47Kfuz_f2pDXp");
            }
        });
        //游戏卸载
        mUnInstall = (RelativeLayout) findViewById(R.id.rl_two);
        mUnInstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.startActivity(ManagementActivity.this, UninstallAppActivity.class);
            }
        });
        //软件设置
        mSetting = (RelativeLayout) findViewById(R.id.rl_three);
        mSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.startActivity(ManagementActivity.this, SettingActivity.class);
            }
        });
        //反馈建议
        mFeedBack = (RelativeLayout) findViewById(R.id.rl_four);
        mFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.startActivity(ManagementActivity.this, FeedBackActivity.class);
            }
        });
        //应用分享
        mAppShare = (RelativeLayout) findViewById(R.id.rl_five);
        mAppShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareUM();
            }
        });
        //关于星宇
        mAboutXingYu = (RelativeLayout) findViewById(R.id.rl_six);
        mAboutXingYu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.startActivity(ManagementActivity.this, AboutActivity.class);
            }
        });
        //下载管理
        mGameDownload = (RelativeLayout) findViewById(R.id.rl_seven);
        mGameDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.startActivity(ManagementActivity.this, DownLoadActivity.class);
            }
        });
        //我的礼包
        mTool = (RelativeLayout) findViewById(R.id.rl_nine);
        mTool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!UserUtils.logined()) {
                    IntentUtils.startActivity(ManagementActivity.this, LoginActivity.class);
                } else {
                    IntentUtils.startActivity(ManagementActivity.this, MyGiftActivity.class);
                }
            }
        });
    }

    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }

    private void shareUM() {
        UMImage thumb =  new UMImage(ManagementActivity.this, R.mipmap.ic);
        UMWeb web = new UMWeb("http://www.xingyuyou.com");
        web.setTitle("人生如戏，全靠游戏");//标题
        web.setThumb(thumb);  //缩略图
        web.setDescription("一个二次元的世界");//描述
        new ShareAction(ManagementActivity.this).withMedia(web)
                .setDisplayList(SHARE_MEDIA.SINA,SHARE_MEDIA.QQ,SHARE_MEDIA.WEIXIN)
                .setCallback(umShareListener).open();
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //分享开始的回调
        }
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat","platform"+platform);

            Toast.makeText(ManagementActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(ManagementActivity.this,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if(t!=null){
                Log.d("throw","throw:"+t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(ManagementActivity.this,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

}
