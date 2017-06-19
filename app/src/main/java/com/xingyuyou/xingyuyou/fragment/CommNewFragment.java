package com.xingyuyou.xingyuyou.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.glide.GlideRoundTransform;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.activity.PostDetailActivity;
import com.xingyuyou.xingyuyou.adapter.CommHotAdapter;
import com.xingyuyou.xingyuyou.base.BaseFragment;
import com.xingyuyou.xingyuyou.bean.community.PostListBean;
import com.xingyuyou.xingyuyou.bean.community.PostListBeanTest;
import com.xingyuyou.xingyuyou.bean.community.TopViewRecommBean;
import com.xingyuyou.xingyuyou.weight.WrapContentLinearLayoutManager;
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
public class CommNewFragment extends BaseFragment {

    private static boolean CLEAR_DATA = false;
    private RecyclerView mRecyclerView;
    private boolean IS_FIRST_INIT_DATA = true;
    private int PAGENUMBER = 1;
    private List mPostList = new ArrayList();
    private List<PostListBeanTest> mPostAdapterList = new ArrayList();
    boolean isLoading = false;
    private List<TopViewRecommBean> mRecommList;
    private List<TopViewRecommBean> mRecommAdapterList = new ArrayList<>();
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (msg.obj.toString().contains("\"data\":null")) {
                    Toast.makeText(mActivity, "已经没有更多数据", Toast.LENGTH_SHORT).show();
                    mPbNodata.setVisibility(View.GONE);
                    mTvNodata.setText("已经没有更多数据");
                    return;
                }
                String response = (String) msg.obj;
                JSONObject jo = null;
                try {
                    jo = new JSONObject(response);
                    JSONArray ja = jo.getJSONArray("data");
                    Gson gson = new Gson();
                    mPostList = gson.fromJson(ja.toString(),
                            new TypeToken<List<PostListBeanTest>>() {
                            }.getType());
                    if (CLEAR_DATA == true) {
                        mPostAdapterList.clear();
                        CLEAR_DATA = false;
                    }
                    mPostAdapterList.addAll(mPostList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //更新UI
                if (mCommHotAdapter != null)
                    mCommHotAdapter.notifyDataSetChanged();
            }
            if (msg.what == 3) {
                String response = (String) msg.obj;
                JSONObject jo = null;
                try {
                    jo = new JSONObject(response);
                    JSONArray ja = jo.getJSONArray("data");
                    Gson gson = new Gson();
                    mRecommList = gson.fromJson(ja.toString(),
                            new TypeToken<List<TopViewRecommBean>>() {
                            }.getType());
                    mRecommAdapterList.addAll(mRecommList);
                    setValues();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    private CommHotAdapter mCommHotAdapter;
    private ProgressBar mPbNodata;
    private TextView mTvNodata;
    private SwipeRefreshLayout mRefreshLayout;
    private LinearLayoutManager mLinearLayoutManager;
    private ImageView mIvOne;
    private ImageView mIvTwo;
    private ImageView mIvThree;
    private ImageView mIvFour;

    public static CommNewFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        CommNewFragment fragment = new CommNewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void initSwipeRefreshLayout() {
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(true);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });
        //下拉刷新
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                PAGENUMBER = 1;
                CLEAR_DATA = true;
                initData(PAGENUMBER);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });

    }
    private void updatePost() {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager
                .getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("updateFragment");
        BroadcastReceiver br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.setRefreshing(true);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                PAGENUMBER = 1;
                                CLEAR_DATA = true;
                                initData(PAGENUMBER);
                                mRefreshLayout.setRefreshing(false);
                            }
                        }, 200);
                    }
                });
            }

        };
        localBroadcastManager.registerReceiver(br, intentFilter);
    }
    /**
     * 初始化数据
     */
    public void initData(int PAGENUMBER) {
        OkHttpUtils.post()//
                .addParams("page", String.valueOf(PAGENUMBER))
                .addParams("type", "1")
                .addParams("attribute", getArguments().getString("ARGS"))
                .addParams("uid", UserUtils.getUserId())
                .addParams("fid","1")
                .addParams("keyword", "1")
                .addParams("bid", "1")
                .url(XingYuInterface.GET_POSTS_LIST)
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

        OkHttpUtils.post()//
                .addParams("type", getArguments().getString("ARGS"))
                .url(XingYuInterface.GET_RECOMMEND)
                .tag(this)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        // Log.e("hot", e.toString() + ":e");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        handler.obtainMessage(3, response).sendToTarget();
                    }
                });
    }

    private void setValues() {
        Glide.with(mActivity).load(mRecommAdapterList.get(0).getRe_image())
                .into(mIvOne);
        Glide.with(mActivity).load(mRecommAdapterList.get(1).getRe_image())
                .transform(new GlideRoundTransform(mActivity,5)).into(mIvTwo);
        Glide.with(mActivity).load(mRecommAdapterList.get(2).getRe_image())
                .transform(new GlideRoundTransform(mActivity,5)).into(mIvThree);
        Glide.with(mActivity).load(mRecommAdapterList.get(3).getRe_image())
                .transform(new GlideRoundTransform(mActivity,5)).into(mIvFour);
        mIvOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, PostDetailActivity.class);
                intent.putExtra("post_id", mRecommAdapterList.get(0).getTid());
                mActivity.startActivity(intent);
            }
        });
        mIvTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, PostDetailActivity.class);
                intent.putExtra("post_id", mRecommAdapterList.get(1).getTid());
                mActivity.startActivity(intent);
            }
        });
        mIvThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, PostDetailActivity.class);
                intent.putExtra("post_id", mRecommAdapterList.get(2).getTid());
                mActivity.startActivity(intent);
            }
        });
        mIvFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, PostDetailActivity.class);
                intent.putExtra("post_id", mRecommAdapterList.get(3).getTid());
                mActivity.startActivity(intent);
            }
        });
    }

    @Override
    protected View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_comm_class, null);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        initSwipeRefreshLayout();
        updatePost();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateStatus();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mLinearLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mCommHotAdapter = new CommHotAdapter(3,mActivity, mPostAdapterList);
        View loadingData = View.inflate(mActivity, R.layout.default_loading, null);
        mPbNodata = (ProgressBar) loadingData.findViewById(R.id.pb_loading);
        mTvNodata = (TextView) loadingData.findViewById(R.id.loading_text);

        //头布局
        View headerView = View.inflate(mActivity, R.layout.part_comm_header, null);
        mIvOne = (ImageView) headerView.findViewById(R.id.iv_one);
        mIvTwo = (ImageView) headerView.findViewById(R.id.iv_two);
        mIvThree = (ImageView) headerView.findViewById(R.id.iv_three);
        mIvFour = (ImageView) headerView.findViewById(R.id.iv_four);
        mCommHotAdapter.setHeaderView(headerView);
        mCommHotAdapter.setFooterView(loadingData);
        mRecyclerView.setAdapter(mCommHotAdapter);
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
                    if (lastVisibleItemPosition + 1 == mCommHotAdapter.getItemCount() - 5) {
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
        if (isVisibleToUser && IS_FIRST_INIT_DATA) {
            initData(PAGENUMBER);
            IS_FIRST_INIT_DATA = false;
        } else {
            //不可见时不执行操作
        }
    }
    private void updateStatus() {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager
                .getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("NewFragmentUpdateCollectStatus");
        intentFilter.addAction("NewFragmentUpdateZanStatus");
        BroadcastReceiver br1 = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("NewFragmentUpdateCollectStatus")) {
                    if (intent.getIntExtra("cancelCollect", 0) == 0) {
                        mPostAdapterList.get(intent.getIntExtra("position", 0) - 1).setCollect_status(0);
                        mPostAdapterList.get(intent.getIntExtra("position", 0) - 1)
                                .setPosts_collect(String.valueOf((Integer.parseInt(
                                        mPostAdapterList.get(intent.getIntExtra("position", 0) - 1).getPosts_collect())) - 1));
                    } else {
                        mPostAdapterList.get(intent.getIntExtra("position", 0) - 1).setCollect_status(1);
                        mPostAdapterList.get(intent.getIntExtra("position", 0) - 1)
                                .setPosts_collect(String.valueOf((Integer.parseInt(
                                        mPostAdapterList.get(intent.getIntExtra("position", 0) - 1).getPosts_collect())) +1));
                    }
                }
                if (intent.getAction().equals("NewFragmentUpdateZanStatus")) {
                    if (intent.getIntExtra("cancelZan", 0) == 0) {
                        mPostAdapterList.get(intent.getIntExtra("position", 0) - 1).setLaud_status(0);
                        mPostAdapterList.get(intent.getIntExtra("position", 0) - 1)
                                .setPosts_laud(String.valueOf((Integer.parseInt(
                                        mPostAdapterList.get(intent.getIntExtra("position", 0) - 1).getPosts_laud())) - 1));
                    } else {
                        mPostAdapterList.get(intent.getIntExtra("position", 0) - 1).setLaud_status(1);
                        mPostAdapterList.get(intent.getIntExtra("position", 0) - 1)
                                .setPosts_laud(String.valueOf((Integer.parseInt(
                                        mPostAdapterList.get(intent.getIntExtra("position", 0) - 1).getPosts_laud())) +1));
                    }
                }
                mCommHotAdapter.notifyItemChanged(intent.getIntExtra("position", 0));
            }

        };
        localBroadcastManager.registerReceiver(br1, intentFilter);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(mActivity);
    }
}
