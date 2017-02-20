package com.xingyuyou.xingyuyou.fragment;

import android.os.Bundle;
import android.view.View;

import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.base.BaseFragment;

/**
 * Created by Administrator on 2016/6/28.
 */
public class OneFragment extends BaseFragment {



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

        return view;
    }



}
