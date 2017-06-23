package com.xingyuyou.xingyuyou.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.IntentUtils;
import com.xingyuyou.xingyuyou.Utils.MCUtils.HttpUtils;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.SPUtils;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.activity.AboutActivity;
import com.xingyuyou.xingyuyou.activity.DownLoadActivity;
import com.xingyuyou.xingyuyou.activity.FeedBackActivity;
import com.xingyuyou.xingyuyou.activity.LoginActivity;
import com.xingyuyou.xingyuyou.activity.MyGiftActivity;
import com.xingyuyou.xingyuyou.activity.TestActivity;
import com.xingyuyou.xingyuyou.activity.UninstallAppActivity;
import com.xingyuyou.xingyuyou.activity.SettingActivity;
import com.xingyuyou.xingyuyou.activity.UserInfoActivity;
import com.xingyuyou.xingyuyou.base.BaseFragment;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/6/28.
 */
public class UserFragment extends BaseFragment {

    private ImageView mSetting;
    private ImageView mUnInstall;
    private ImageView mUserPhoto;
    private ImageView mFeedBack;
    private ImageView mGameUpdate;
    private ImageView mPoJie;
    private ImageView mTool;
    private ImageView mGameDownload;
    private ImageView mAppShare;
    private ImageView mAboutXingYu;
    private TextView mTvAccountName;
    private TextView mTvUserName;

    public static UserFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        UserFragment fragment = new UserFragment();
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
        View view = View.inflate(mActivity, R.layout.fragment_user, null);
        //显示用户信息
        if (UserUtils.logined()) {
            SPUtils user_data = new SPUtils("user_data");
            String id = user_data.getString("id");
            String account = user_data.getString("account");
            String nickname = user_data.getString("nickname");
            TextView tvUserAccountName = (TextView) view.findViewById(R.id.user_account_name);
            TextView tvNickName = (TextView) view.findViewById(R.id.user_nickname);
            tvUserAccountName.setText(account);
            tvNickName.setText(nickname);
        }

        //登录
        mUserPhoto = (ImageView) view.findViewById(R.id.user_photo);
        mUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserUtils.logined()) {
                    IntentUtils.startActivity(mActivity, UserInfoActivity.class);
                } else {
                    IntentUtils.startActivity(mActivity, LoginActivity.class);
                }
            }
        });
        //游戏更新
        mGameUpdate = (ImageView) view.findViewById(R.id.image_one);
        mGameUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //IntentUtils.startActivity(mActivity, UninstallAppActivity.class);
                Toast.makeText(mActivity, "功能暂未开放", Toast.LENGTH_SHORT).show();
            }
        });
        //加入官群
        mPoJie = (ImageView) view.findViewById(R.id.image_two);
        mPoJie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinQQGroup("YzjlZwrRfUeZN0jnoSF47Kfuz_f2pDXp");
            }
        });
        //游戏卸载
        mUnInstall = (ImageView) view.findViewById(R.id.image_three);
        mUnInstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.startActivity(mActivity, UninstallAppActivity.class);
            }
        });
        //软件设置
        mSetting = (ImageView) view.findViewById(R.id.image_four);
        mSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.startActivity(mActivity, SettingActivity.class);
            }
        });
        //反馈建议
        mFeedBack = (ImageView) view.findViewById(R.id.image_five);
        mFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.startActivity(mActivity, FeedBackActivity.class);
            }
        });
        //应用分享
        mAppShare = (ImageView) view.findViewById(R.id.image_six);
        mAppShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareUM();
            }
        });
        //关于星宇
        mAboutXingYu = (ImageView) view.findViewById(R.id.image_seven);
        mAboutXingYu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.startActivity(mActivity, TestActivity.class);
            }
        });
        //下载管理
        mGameDownload = (ImageView) view.findViewById(R.id.image_eight);
        mGameDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.startActivity(mActivity, DownLoadActivity.class);
            }
        });
        //我的礼包
        mTool = (ImageView) view.findViewById(R.id.image_nine);
        mTool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!UserUtils.logined()) {
                    IntentUtils.startActivity(mActivity, LoginActivity.class);
                } else {
                    IntentUtils.startActivity(mActivity, MyGiftActivity.class);
                }
            }
        });

        return view;
    }
    /****************
     *
     * 发起添加群流程。群号：官方游戏交流群(614848840) 的 key 为： YzjlZwrRfUeZN0jnoSF47Kfuz_f2pDXp
     * 调用 joinQQGroup(YzjlZwrRfUeZN0jnoSF47Kfuz_f2pDXp) 即可发起手Q客户端申请加群 官方游戏交流群(614848840)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     ******************/
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
        UMImage thumb =  new UMImage(mActivity, R.mipmap.icon);
        UMWeb web = new UMWeb("http://www.xingyuyou.com");
        web.setTitle("人生如戏，全靠游戏");//标题
        web.setThumb(thumb);  //缩略图
        web.setDescription("一个二次元的世界");//描述
        new ShareAction(mActivity).withMedia(web)
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

            Toast.makeText(mActivity, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(mActivity,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if(t!=null){
                Log.d("throw","throw:"+t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(mActivity,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };


}
