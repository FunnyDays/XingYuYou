package com.xingyuyou.xingyuyou.fragment;

import android.os.Bundle;
import android.view.View;

import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.base.BaseFragment;

/**
 * Created by Administrator on 2016/6/28.
 */
public class FourFragment extends BaseFragment {



    public static FourFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        FourFragment fragment = new FourFragment();
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
        View view = View.inflate(mActivity, R.layout.fragment_four, null);
        return view;
    }



}
