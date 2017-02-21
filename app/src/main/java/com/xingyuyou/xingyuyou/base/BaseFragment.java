package com.xingyuyou.xingyuyou.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {
    public Activity mActivity;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e("weiwei","onAttach");
        mActivity = getActivity();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("weiwei","onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = initView();
        Log.e("weiwei","onCreateView");
        return view;
    }



    protected abstract View initView();



    public void initData() {
        Log.e("weiwei","initData");
    }
}
