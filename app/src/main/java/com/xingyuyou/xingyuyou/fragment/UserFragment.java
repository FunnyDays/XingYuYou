package com.xingyuyou.xingyuyou.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        //游戏破解
        mPoJie = (ImageView) view.findViewById(R.id.image_two);
        mPoJie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mActivity, "功能暂未开放", Toast.LENGTH_SHORT).show();
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
                IntentUtils.startActivity(mActivity, FeedBackActivity.class);
            }
        });
        //关于星宇
        mAboutXingYu = (ImageView) view.findViewById(R.id.image_seven);
        mAboutXingYu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.startActivity(mActivity, AboutActivity.class);
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
        //实用工具
        mTool = (ImageView) view.findViewById(R.id.image_nine);
        mTool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mActivity, "测试", Toast.LENGTH_SHORT).show();
                IntentUtils.startActivity(mActivity, TestActivity.class);
            }
        });

        return view;
    }


}
