package com.xingyuyou.xingyuyou.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.adapter.CommClassAdapter;
import com.xingyuyou.xingyuyou.base.BaseFragment;

/**
 * Created by Administrator on 2016/6/28.
 */
public class CommClassFragment extends BaseFragment {


    private RecyclerView mRecyclerView;

    public static CommClassFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        CommClassFragment fragment = new CommClassFragment();
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
        View view = View.inflate(mActivity, R.layout.fragment_comm_class, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        new CommClassAdapter();
        mRecyclerView.setAdapter(null);
    }
}
