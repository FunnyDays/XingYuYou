package com.xingyuyou.xingyuyou.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.AppUtils;
import com.xingyuyou.xingyuyou.Utils.ConvertUtils;
import com.xingyuyou.xingyuyou.Utils.FileUtils;
import com.xingyuyou.xingyuyou.Utils.GlideImageLoader;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.activity.HotGameDetailActivity;
import com.xingyuyou.xingyuyou.adapter.GameHeaderFooterAdapter;
import com.xingyuyou.xingyuyou.adapter.TestAdapter;
import com.xingyuyou.xingyuyou.base.BaseFragment;
import com.xingyuyou.xingyuyou.bean.Game;
import com.xingyuyou.xingyuyou.bean.HotBannerBean;
import com.xingyuyou.xingyuyou.bean.hotgame.HotGameBean;
import com.xingyuyou.xingyuyou.download.DownloadInfo;
import com.xingyuyou.xingyuyou.download.DownloadManager;
import com.xingyuyou.xingyuyou.download.DownloadState;
import com.xingyuyou.xingyuyou.download.DownloadViewHolder;
import com.xingyuyou.xingyuyou.weight.HorizontalProgressBarWithTextProgress;
import com.xingyuyou.xingyuyou.weight.ProgressButton;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/6/28.
 */
public class HotGameWithCoverFragment extends BaseFragment {


    private RecyclerView mRecyclerView;
    private ArrayList<Game> mGameArrayList;
    private List<HotGameBean> mHotGameList=new ArrayList<>();
    private List<HotGameBean> mGameListAdapter=new ArrayList<>();
    private List<HotBannerBean> mHotBannerGameList;
    private Banner mBanner;
    private int lastItem;
    private int  MLOADINGMORE_FLAG = 0;
    private int  PAGENUMBER = 1;
    private View mLoading;
    private TextView mLoadingText;
    private ProgressBar mPbLoading;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (msg.obj.toString().contains("{\"list\":null}")) {
                    Toast.makeText(mActivity, "已经没有更多数据", Toast.LENGTH_SHORT).show();
                    View noData = View.inflate(mActivity, R.layout.default_no_data, null);
                    mLoadingText.setText("没有更多数据");
                    mPbLoading.setVisibility(View.GONE);
                    return;
                }
                String response = (String) msg.obj;
                JSONObject jo = null;
                try {
                    jo = new JSONObject(response);
                    JSONArray ja = jo.getJSONArray("list");
                    // Log.e("hot", "解析数据："+  ja.toString());
                    Gson gson = new Gson();
                    mHotGameList = gson.fromJson(ja.toString(),
                            new TypeToken<List<HotGameBean>>() {
                            }.getType());
                    for (int i = 0; i < mHotGameList.size(); i++) {
                        Log.e("hotgame", "解析数据："+ mHotGameList.toString());
                    }
                    mGameListAdapter.addAll(mHotGameList);
                    //如果还有数据把加载更多值为0
                    MLOADINGMORE_FLAG=0;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //更新UI
                if (mGameHeaderFooterAdapter != null) {
                    mGameHeaderFooterAdapter.notifyDataSetChanged();
                }
            }
            if (msg.what == 2) {
                String response = (String) msg.obj;
                JSONObject jo = null;
                try {
                    jo = new JSONObject(response);
                    JSONArray ja = jo.getJSONArray("data");
                    Gson gson = new Gson();
                    mHotBannerGameList = gson.fromJson(ja.toString(),
                            new TypeToken<List<HotBannerBean>>() {
                            }.getType());
                    List<String> imageList = new ArrayList<>();
                    for (int i = 0; i < mHotBannerGameList.size(); i++) {
                        imageList.add(mHotBannerGameList.get(i).getData());
                    }
                   // mBanner.setImages(imageList).setImageLoader(new GlideImageLoader()).start();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    private GameHeaderFooterAdapter mGameHeaderFooterAdapter;

    public static HotGameWithCoverFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        HotGameWithCoverFragment fragment = new HotGameWithCoverFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected View initView() {
        initBannerData();
        initData(PAGENUMBER);
        View view = View.inflate(mActivity, R.layout.fragment_hot_game_cover, null);
        return view;
    }

    /**
     * 初始化数据
     */

    public void initData(int PAGENUMBER) {
        OkHttpUtils.post()//
                .addParams("limit",String.valueOf(PAGENUMBER))
                .addParams("file_type",String.valueOf("1"))
                .url(XingYuInterface.GET_GAME_LIST + "/type/top")
                .tag(this)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("hot", e.toString() + ":e");
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        handler.obtainMessage(1, response).sendToTarget();
                    }
                });
    }

    private void initBannerData() {
        OkHttpUtils.post()//
                .url(XingYuInterface.ROTATION_IMG)
                .tag(this)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("hot", e.toString() + ":e");
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("lunbo", response + "");
                        handler.obtainMessage(2, response).sendToTarget();
                    }
                });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));

        mGameHeaderFooterAdapter = new GameHeaderFooterAdapter(mActivity, mGameListAdapter);
        mRecyclerView.setAdapter(mGameHeaderFooterAdapter);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(mActivity);
    }
}
