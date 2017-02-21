package com.xingyuyou.xingyuyou.fragment;

import android.os.Bundle;
import android.view.View;

import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.GlideImageLoader;
import com.xingyuyou.xingyuyou.base.BaseFragment;
import com.youth.banner.Banner;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/28.
 */
public class OnlineGameFragment extends BaseFragment {



    public static OnlineGameFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        OnlineGameFragment fragment = new OnlineGameFragment();
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
        View view = View.inflate(mActivity, R.layout.fragment_online_game, null);
        Banner banner = (Banner) view.findViewById(R.id.banner);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        ArrayList<Object> imageList = new ArrayList<>();
        imageList.add("1");
        imageList.add("2");
        imageList.add("3");
        banner.setImages(imageList);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
        return view;
    }

}
