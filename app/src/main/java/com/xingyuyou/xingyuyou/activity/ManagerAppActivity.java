package com.xingyuyou.xingyuyou.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.AppUtils;
import com.xingyuyou.xingyuyou.Utils.Loading.LoadingLayout;
import com.xingyuyou.xingyuyou.adapter.ManagerAppAdapter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class ManagerAppActivity extends AppCompatActivity {

    private RecyclerView mRvAppList;
    private Toolbar mToolbar;
    private List<AppUtils.AppInfo> appsInfo = new ArrayList<>();
    Handler hanlder = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                mLoadingLayout.setStatus(LoadingLayout.Success);//成功
                mAdapter.notifyDataSetChanged();
            }
            if (msg.what == 2) {
                mAdapter.notifyDataSetChanged();
            }
            if (msg.what == 3) {
                appsInfo.remove(mRemovePos);
                mAdapter.notifyDataSetChanged();
            }
        }
    };
    private LoadingLayout mLoadingLayout;
    private ManagerAppAdapter mAdapter;
    private UninstallReceiver mUninstallReceiver;
    private String mPackageName;
    private int mRemovePos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_app);
        mLoadingLayout = (LoadingLayout) findViewById(R.id.activity_manager_app);
        initData();
        initEmptyView();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        initToolbar();
        mRvAppList = (RecyclerView) findViewById(R.id.recyclerView);
        initRecycle();
        //监听卸载
        uninstallReceiver();
    }

    private void uninstallReceiver() {
        mUninstallReceiver = new UninstallReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
        this.registerReceiver(mUninstallReceiver, filter);
    }


    /**
     * 空布局
     */
    private void initEmptyView() {
        mLoadingLayout.setOnReloadListener(new LoadingLayout.OnReloadListener() {
            @Override
            public void onReload(View v) {
                //Toast.makeText(mActivity, "重试", Toast.LENGTH_SHORT).show();
                initEmptyView();
                initData();
            }
        });
        mLoadingLayout.setStatus(LoadingLayout.Loading);//加载中
    }

    /**
     * 获取所有App安装信息
     */
    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<AppUtils.AppInfo> appInfos = AppUtils.getAppsInfoNoSys(ManagerAppActivity.this);
                appsInfo.addAll(appInfos);
                hanlder.obtainMessage(1).sendToTarget();
                for (int i = 0; i < appsInfo.size(); i++) {
                    try {
                        getPkgSize(ManagerAppActivity.this, appsInfo.get(i).getPackageName(), i);
                        Log.e("daxiao1", "所有app的信息：" + appsInfo.get(i).toString() + "\n共有多少app" + appsInfo.size());

                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                }
                hanlder.obtainMessage(2).sendToTarget();
            }
        }).start();


    }

    private void initRecycle() {
        mRvAppList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ManagerAppAdapter(this, appsInfo);
        mAdapter.setOnItemClickLitener(new ManagerAppAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                mPackageName = appsInfo.get(position).getPackageName();
                mRemovePos = position;
                AppUtils.uninstallApp(ManagerAppActivity.this,appsInfo.get(position).getPackageName(),1);
            }
        });
        mRvAppList.setAdapter(mAdapter);

    }

    private void initToolbar() {
        mToolbar.setTitle("软件管理");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 获得App应用的大小
     *
     * @param context
     * @param pkgName
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public void getPkgSize(final Context context, String pkgName, final int i) throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {

        // getPackageSizeInfo是PackageManager中的一个private方法，所以需要通过反射的机制来调用
        Method method = PackageManager.class.getMethod("getPackageSizeInfo",
                String.class, IPackageStatsObserver.class);
        // 调用 getPackageSizeInfo 方法，需要两个参数：1、需要检测的应用包名；2、回调
                 method.invoke(context.getPackageManager(), pkgName,
                new IPackageStatsObserver.Stub() {
                    @Override
                    public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
                        // 子线程中默认无法处理消息循环，自然也就不能显示Toast，所以需要手动Looper一下
                        // Looper.prepare();
                        // 从pStats中提取各个所需数据
                      /*  Toast.makeText(context, "缓存大小=" + Formatter.formatFileSize(context, pStats.cacheSize) +
                                        "\n数据大小=" + Formatter.formatFileSize(context, pStats.dataSize) +
                                        "\n程序大小=" + Formatter.formatFileSize(context, pStats.codeSize),
                                Toast.LENGTH_LONG).show();*/
                        Log.e("daxiao2", i + Formatter.formatFileSize(context, pStats.codeSize));
                        appsInfo.get(i).setAppSize(Formatter.formatFileSize(context, pStats.codeSize));
                        // 遍历一次消息队列，弹出Toast
                        // Looper.loop();
                    }
                });
    }
    private class UninstallReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mPackageName != null && ("package:" + mPackageName).equals(intent.getDataString())) {
                Toast.makeText(context, "卸载成功", Toast.LENGTH_SHORT).show();
                 hanlder.obtainMessage(3).sendToTarget();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("result",requestCode+"----"+resultCode+"-----");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mUninstallReceiver);
    }
}
