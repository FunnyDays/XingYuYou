package com.xingyuyou.xingyuyou.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.IntentUtils;
import com.xingyuyou.xingyuyou.activity.AboutActivity;
import com.xingyuyou.xingyuyou.activity.DownLoadActivity;
import com.xingyuyou.xingyuyou.activity.FeedBackActivity;
import com.xingyuyou.xingyuyou.activity.LoginActivity;
import com.xingyuyou.xingyuyou.activity.TestActivity;
import com.xingyuyou.xingyuyou.activity.UninstallAppActivity;
import com.xingyuyou.xingyuyou.activity.SettingActivity;
import com.xingyuyou.xingyuyou.base.BaseFragment;

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
        //登录
        mUserPhoto = (ImageView) view.findViewById(R.id.photo);
        mUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.startActivity(mActivity, LoginActivity.class);
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
