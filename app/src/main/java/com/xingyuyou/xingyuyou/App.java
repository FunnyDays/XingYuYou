package com.xingyuyou.xingyuyou;

import android.app.Application;

import com.xingyuyou.xingyuyou.Utils.Loading.LoadingLayout;

import org.xutils.x;

/**
 * Created by Administrator on 2017/2/20.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //空布局初始化
        initLoadView();
        //下载初始化
        x.Ext.init(this);
    }
    private void initLoadView() {
        LoadingLayout.getConfig()
                .setErrorText("出错啦~请稍后重试！")
                .setEmptyText("抱歉，暂无数据")
                .setNoNetworkText("无网络连接，请检查您的网络···")
                .setErrorImage(R.mipmap.error)
                .setEmptyImage(R.mipmap.empty)
                .setNoNetworkImage(R.mipmap.no_network)
                .setAllTipTextColor(R.color.colorAccent)
                .setAllTipTextSize(14)
                .setReloadButtonText("点我重试哦")
                .setReloadButtonTextSize(14)
                .setReloadButtonTextColor(R.color.colorAccent)
                .setReloadButtonWidthAndHeight(150,40);
    }
}
