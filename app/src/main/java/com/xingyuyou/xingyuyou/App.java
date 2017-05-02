package com.xingyuyou.xingyuyou;

import android.app.Application;
import android.content.Context;
import android.os.Debug;

import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.xingyuyou.xingyuyou.Utils.Loading.LoadingLayout;
import com.xingyuyou.xingyuyou.Utils.Utils;
import com.xingyuyou.xingyuyou.service.InitializeService;
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
        //intentservice初始化
        InitializeService.start(this);

    }
}
