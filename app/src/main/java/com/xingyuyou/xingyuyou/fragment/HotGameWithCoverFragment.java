package com.xingyuyou.xingyuyou.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.adapter.DividerItemDecoration;
import com.xingyuyou.xingyuyou.adapter.GameHeaderFooterAdapter;
import com.xingyuyou.xingyuyou.base.BaseFragment;
import com.xingyuyou.xingyuyou.bean.Game;
import com.xingyuyou.xingyuyou.bean.HotBannerBean;
import com.xingyuyou.xingyuyou.bean.hotgame.HotGameBean;
import com.youth.banner.Banner;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/6/28.
 */
public class HotGameWithCoverFragment extends BaseFragment {


    private RecyclerView mRecyclerView;
    private List<HotGameBean> mHotGameList=new ArrayList<>();
    private List<HotGameBean> mGameAdapterList=new ArrayList<>();
    private List<HotBannerBean> mHotBannerGameList;
    private boolean IS_FIRST_INIT_DATA=true;
    private int  MLOADINGMORE_FLAG = 0;
    private int  PAGENUMBER = 1;
    boolean isLoading = false;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (msg.obj.toString().contains("{\"list\":null}")) {
                    Toast.makeText(mActivity, "已经没有更多数据", Toast.LENGTH_SHORT).show();
                    mPbNodata.setVisibility(View.GONE);
                    mTvNodata.setText("已经没有更多数据");
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

                    mGameAdapterList.addAll(mHotGameList);
                    //如果还有数据把加载更多值为0
                    MLOADINGMORE_FLAG=0;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //更新UI
                if (mGameHeaderFooterAdapter != null)
                    mGameHeaderFooterAdapter.notifyDataSetChanged();
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    private GameHeaderFooterAdapter mGameHeaderFooterAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private ProgressBar mPbNodata;
    private TextView mTvNodata;

    public static HotGameWithCoverFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        HotGameWithCoverFragment fragment = new HotGameWithCoverFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected View initView() {
        //initBannerData();
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
                       // Log.e("hot", e.toString() + ":e");
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
                       // Log.e("hot", e.toString() + ":e");
                    }
                    @Override
                    public void onResponse(String response, int id) {
                       // Log.e("lunbo", response + "");
                        handler.obtainMessage(2, response).sendToTarget();
                    }
                });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mLinearLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mGameHeaderFooterAdapter = new GameHeaderFooterAdapter(mActivity, mGameAdapterList);
        View loadingData = View.inflate(mActivity, R.layout.default_loading, null);
        mPbNodata = (ProgressBar) loadingData.findViewById(R.id.pb_loading);
        mTvNodata = (TextView) loadingData.findViewById(R.id.loading_text);
        TextView textView = new TextView(mActivity);
        textView.setText("精品游戏");
        mGameHeaderFooterAdapter.setHeaderView(textView);
        mGameHeaderFooterAdapter.setFooterView(loadingData);
        mRecyclerView.setAdapter(mGameHeaderFooterAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_SETTLING:
                     //   Log.i("Main", "用户在手指离开屏幕之前，由于滑了一下，视图仍然依靠惯性继续滑动");
                        Glide.with(mActivity).pauseRequests();
                        //刷新
                        break;
                    case RecyclerView.SCROLL_STATE_IDLE:
                      //  Log.i("Main", "视图已经停止滑动");
                        Glide.with(mActivity).resumeRequests();
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                      //  Log.i("Main", "手指没有离开屏幕，视图正在滑动");
                        Glide.with(mActivity).resumeRequests();
                        break;
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = mLinearLayoutManager.findLastVisibleItemPosition();
                if (!recyclerView.canScrollVertically(-1)) {
                    //T.show(mActivity,"已经到第一条");
                } else if (!recyclerView.canScrollVertically(1)) {
                    //T.show(mActivity,"到了最后一条");
                } else if (dy < 0) {
                    //T.show(mActivity,"正在向上滑动");
                } else if (dy > 0) {
                    // T.show(mActivity,"正在向下滑动");
                    if (lastVisibleItemPosition + 1 == mGameHeaderFooterAdapter.getItemCount() - 5) {
                      //  Log.e("search", "loading executed");
                        if (!isLoading) {
                            isLoading = true;
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    PAGENUMBER++;
                                   // Log.d("search", "load more completed");
                                    initData(PAGENUMBER);
                                    isLoading = false;
                                }
                            }, 200);
                        }
                    }
                }

            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser&&IS_FIRST_INIT_DATA) {
            initData(PAGENUMBER);
            IS_FIRST_INIT_DATA=false;
        } else {
            //不可见时不执行操作
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(mActivity);
    }
}
