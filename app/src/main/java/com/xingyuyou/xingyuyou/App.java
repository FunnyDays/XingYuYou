package com.xingyuyou.xingyuyou;

import android.app.Application;
import android.content.Context;

import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.xingyuyou.xingyuyou.Utils.Loading.LoadingLayout;
import com.xingyuyou.xingyuyou.Utils.Utils;
import com.zhy.http.okhttp.OkHttpUtils;

import org.xutils.x;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2017/2/20.
 */

public class App extends Application {
    public static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext=this;
        //空布局初始化
        initLoadView();
        //下载初始化
        x.Ext.init(this);
        //工具类初始化
        Utils.init(this);
        //网络初始化
        initOkhttp();
        //友盟分享
        youmeng();
    }

    private void youmeng() {
        UMShareAPI.get(this);
        PlatformConfig.setWeixin("","");
        PlatformConfig.setQQZone("1106012303","hgqXkjzu7Mq1USGL");
        PlatformConfig.setSinaWeibo("3073251384 ","8304b645771ea95644c209ed5e6b9558","http://www.xingyuyou.com");
    }

    /**
     * 配置网络请求
     */
    private void initOkhttp() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(30000L, TimeUnit.MILLISECONDS)
                .readTimeout(30000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();
        OkHttpUtils.initClient(okHttpClient);
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
