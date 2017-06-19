package com.xingyuyou.xingyuyou.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.IntentUtils;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.glide.GlideCircleTransform;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.adapter.CommHotAdapter;
import com.xingyuyou.xingyuyou.adapter.CommSortAdapter;
import com.xingyuyou.xingyuyou.bean.community.PostListBean;
import com.xingyuyou.xingyuyou.bean.community.PostListBeanTest;
import com.xingyuyou.xingyuyou.bean.community.PostTopAndWellBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class PostClassListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private int  PAGENUMBER = 1;
    private List<PostListBeanTest> mPostList=new ArrayList();
    private List<PostTopAndWellBean> mPostTopWellList=new ArrayList();
    private List mPostAdapterList=new ArrayList();
    boolean isLoading = false;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (msg.obj.toString().contains("\"data\":null")) {
                    Toast.makeText(PostClassListActivity.this, "已经没有更多数据", Toast.LENGTH_SHORT).show();
                    mPbNodata.setVisibility(View.GONE);
                    mTvNodata.setText("已经没有更多数据");
                    return;
                }
                String response = (String) msg.obj;
                JSONObject jo = null;
                try {
                    jo = new JSONObject(response);
                    JSONArray ja = jo.getJSONArray("data");
                 //   Log.e("post", "解析数据："+  ja.toString());
                    Gson gson = new Gson();
                    mPostList = gson.fromJson(ja.toString(),
                            new TypeToken<List<PostListBeanTest>>() {
                            }.getType());

                    ja = jo.getJSONArray("top_well");
                    gson = new Gson();
                    mPostTopWellList = gson.fromJson(ja.toString(),
                            new TypeToken<List<PostTopAndWellBean>>() {
                            }.getType());
                    mPostAdapterList.addAll(mPostTopWellList);
                    mPostAdapterList.addAll(mPostList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //更新UI
                if (mCommHotAdapter != null)
                    mCommHotAdapter.notifyDataSetChanged();
            }
        }
    };
    private CommSortAdapter mCommHotAdapter;
    private ProgressBar mPbNodata;
    private TextView mTvNodata;
    private SwipeRefreshLayout mRefreshLayout;
    private LinearLayoutManager mLinearLayoutManager;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_class_list);
        initView();
        initToolBar();
        initSwipeRefreshLayout();
        initData(1);
        updateStatus();
    }
    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void initView() {
        //发帖
        ImageView floatingActionButton = (ImageView) findViewById(R.id.fab_add_comment);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserUtils.logined()) {
                    IntentUtils.startActivity(PostClassListActivity.this, PostingActivity.class);
                } else {
                    IntentUtils.startActivity(PostClassListActivity.this, LoginActivity.class);
                }
            }
        });
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mCommHotAdapter = new CommSortAdapter(this, mPostAdapterList);
        //底部布局
        View loadingData = View.inflate(this, R.layout.default_loading, null);
        mPbNodata = (ProgressBar) loadingData.findViewById(R.id.pb_loading);
        mTvNodata = (TextView) loadingData.findViewById(R.id.loading_text);
        //头布局
        View headerView = View.inflate(this, R.layout.header_sort_post_list, null);
        ImageView ivBg = (ImageView) headerView.findViewById(R.id.iv_bg);
        ImageView ivLabel = (ImageView) headerView.findViewById(R.id.iv_class_label);
        TextView tvPostNum = (TextView) headerView.findViewById(R.id.tv_post_num);
        TextView tvPostDes = (TextView) headerView.findViewById(R.id.tv_post_describe);
        TextView tv_class_name = (TextView) headerView.findViewById(R.id.tv_class_name);
        Glide.with(getApplication())
                .load(getIntent().getStringExtra("class_virtual_image"))
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(ivBg);
        Glide.with(getApplication())
                .load(getIntent().getStringExtra("class_head_image"))
                .transform(new GlideCircleTransform(PostClassListActivity.this))
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(ivLabel);
        tvPostNum.setText("帖子："+getIntent().getStringExtra("posts_num"));
        tvPostDes.setText(getIntent().getStringExtra("describe"));
        tv_class_name.setText(getIntent().getStringExtra("posts_class_name"));

        mCommHotAdapter.setHeaderView(headerView);
        mCommHotAdapter.setFooterView(loadingData);
        mRecyclerView.setAdapter(mCommHotAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        //   Log.i("Main", "用户在手指离开屏幕之前，由于滑了一下，视图仍然依靠惯性继续滑动");
                        Glide.with(getApplication()).pauseRequests();
                        //刷新
                        break;
                    case RecyclerView.SCROLL_STATE_IDLE:
                        //  Log.i("Main", "视图已经停止滑动");
                        Glide.with(getApplication()).resumeRequests();
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        //  Log.i("Main", "手指没有离开屏幕，视图正在滑动");
                        Glide.with(getApplication()).resumeRequests();
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
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });

    }
    /**
     * 初始化数据
     */
    public void initData(int PAGENUMBER) {
        OkHttpUtils.post()//
                .addParams("page",String.valueOf(PAGENUMBER))
                .addParams("type", "2")
                .addParams("fid",getIntent().getStringExtra("list_id"))
                .addParams("uid",UserUtils.getUserId())
                .addParams("keyword", "1")
                .addParams("bid", "1")
                .addParams("attribute", "1")
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
    }

    private void updateStatus() {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager
                .getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("SortPostUpdateCollectStatus");
        intentFilter.addAction("SortPostUpdateZanStatus");
        BroadcastReceiver br1 = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("SortPostUpdateCollectStatus")) {
                    if (intent.getIntExtra("cancelCollect", 0) == 0) {
                        ((PostListBeanTest)mPostAdapterList.get(intent.getIntExtra("position", 0) - 1)).setCollect_status(0);
                        ((PostListBeanTest)mPostAdapterList.get(intent.getIntExtra("position", 0) - 1))
                                .setPosts_collect(String.valueOf((Integer.parseInt(
                                        ((PostListBeanTest) mPostAdapterList.get(intent.getIntExtra("position", 0) - 1)).getPosts_collect())) - 1));
                    } else {
                        ((PostListBeanTest)mPostAdapterList.get(intent.getIntExtra("position", 0) - 1)).setCollect_status(1);
                        ((PostListBeanTest)mPostAdapterList.get(intent.getIntExtra("position", 0) - 1))
                                .setPosts_collect(String.valueOf((Integer.parseInt(
                                        ((PostListBeanTest)mPostAdapterList.get(intent.getIntExtra("position", 0) - 1)).getPosts_collect())) +1));
                    }
                }
                if (intent.getAction().equals("SortPostUpdateZanStatus")) {
                    if (intent.getIntExtra("cancelZan", 0) == 0) {
                        ((PostListBeanTest)mPostAdapterList.get(intent.getIntExtra("position", 0) - 1)).setLaud_status(0);
                        ((PostListBeanTest) mPostAdapterList.get(intent.getIntExtra("position", 0) - 1))
                                .setPosts_laud(String.valueOf((Integer.parseInt(
                                        ((PostListBeanTest) mPostAdapterList.get(intent.getIntExtra("position", 0) - 1)).getPosts_laud())) - 1));
                    } else {
                        ((PostListBeanTest)mPostAdapterList.get(intent.getIntExtra("position", 0) - 1)).setLaud_status(1);
                        ((PostListBeanTest)mPostAdapterList.get(intent.getIntExtra("position", 0) - 1))
                                .setPosts_laud(String.valueOf((Integer.parseInt(
                                        ((PostListBeanTest) mPostAdapterList.get(intent.getIntExtra("position", 0) - 1)).getPosts_laud())) +1));
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
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
