package com.xingyuyou.xingyuyou.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.IntentUtils;
import com.xingyuyou.xingyuyou.activity.ManagerAppActivity;
import com.xingyuyou.xingyuyou.activity.SettingActivity;
import com.xingyuyou.xingyuyou.base.BaseFragment;

/**
 * Created by Administrator on 2016/6/28.
 */
public class UserFragment extends BaseFragment {

    private ImageView mSetting;
    private ImageView mUnInstall;

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
        //软件设置
        mSetting = (ImageView) view.findViewById(R.id.image_four);
        mSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.startActivity(mActivity, SettingActivity.class);

            }
        });
        //游戏卸载
        mUnInstall = (ImageView) view.findViewById(R.id.image_three);
        mUnInstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.startActivity(mActivity,ManagerAppActivity.class);
            }
        });
        return view;
    }



}
