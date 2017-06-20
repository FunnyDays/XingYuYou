/*
package com.xingyuyou.xingyuyou.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;


import com.xingyuyou.xingyuyou.DataParserBean.DataParser;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.Loading.LoadingLayout;
import com.xingyuyou.xingyuyou.Utils.NetUtils;
import com.xingyuyou.xingyuyou.activity.GameDetailActivity;
import com.xingyuyou.xingyuyou.adapter.GameAdapter;
import com.xingyuyou.xingyuyou.base.BaseFragment;
import com.xingyuyou.xingyuyou.bean.Game;
import com.xingyuyou.xingyuyou.download.DownloadInfo;
import com.xingyuyou.xingyuyou.download.DownloadManager;

import org.xutils.DbManager;

import java.util.ArrayList;

*/
/**
 * Created by Administrator on 2016/6/28.
 *//*

public class GameFragment extends BaseFragment {

    private Handler handler = new Handler();
    boolean isLoading = false;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private static final int STATE_REFRESH = 0;// 下拉刷新
    private static final int STATE_MORE = 1;// 加载更多
    private int action = STATE_REFRESH;        // 每页的数据是10条
    private int curPage = 1;        // 当前页的编号，从1开始
    private int isLoadData;
    private String lastTime;
    ArrayList<Game> arrayList = new ArrayList<Game>();
    private LinearLayoutManager linearLayoutManager;
    private GameAdapter mAdapter;
    private boolean queryDone;
    private LoadingLayout mLoadingLayout;
    private DbManager mDb;
    private DownloadManager downloadManager;
    private DownloadInfo mDownloadInfo;
    public static GameFragment newInstance(String content) {

        Bundle args = new Bundle();
        args.putString("ARGS", content);
        GameFragment fragment = new GameFragment();
        fragment.setArguments(args);
        return fragment;
    }


    */
/**
     * 初始化数据
     *//*

    @Override
    public void initData() {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    if (mAdapter!=null)
                    mAdapter.notifyDataSetChanged();
                    if (refreshLayout != null)
                    refreshLayout.setRefreshing(false);
                    if (!NetUtils.checkNetWorkIsAvailable(mActivity)) {
                        mLoadingLayout.setStatus(LoadingLayout.No_Network);//无网络
                    } else {
                        mLoadingLayout.setStatus(LoadingLayout.Success);//成功
                    }

                }
            }
        };
        new Thread() {
            @Override
            public void run() {
                if (ParserHtml(curPage, action)) {
                    handler.obtainMessage(1).sendToTarget();
                }
                else {

                }
            }
        }.start();

    }

    private Boolean ParserHtml(int page, int action) {
        if (action == STATE_REFRESH) {
            ArrayList<Game> gameArrayList1 = DataParser.getAllGame(page);
            arrayList.clear();
            arrayList.addAll(gameArrayList1);
        }
        if (action == STATE_MORE) {
            ArrayList<Game> gameArrayList1 = DataParser.getAllGame(page);
            arrayList.addAll(gameArrayList1);
        }
        if (arrayList.size() >= 10) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected View initView() {
        initData();
        View view = View.inflate(mActivity, R.layout.fragment_game, null);
        mLoadingLayout = (LoadingLayout) view.findViewById(R.id.empty_view_game);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.SwipeRefreshLayout);
        initRefreshLayout();
        recyclerView = (RecyclerView) view.findViewById(R.id.RecyclerView);
        initRecyclerView();
        initEmptyView();
        return view;
    }

    */
/**
     * 空布局
     *//*

    private void initEmptyView() {
        mLoadingLayout.setOnReloadListener(new LoadingLayout.OnReloadListener() {

            @Override
            public void onReload(View v) {
                //Toast.makeText(mActivity, "重试", Toast.LENGTH_SHORT).show();
                initEmptyView();
                initData();
            }
        });
        if (!NetUtils.checkNetWorkIsAvailable(mActivity)) {
            mLoadingLayout.setStatus(LoadingLayout.No_Network);//无网络
            return;
        }
        mLoadingLayout.setStatus(LoadingLayout.Loading);//加载中
    }

    private void initRefreshLayout() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               // arrayList.clear();
                curPage = 1;
                action = STATE_REFRESH;
                initData();

            }
        });
    }

    private void initRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(mActivity);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new GameAdapter(mActivity, arrayList);
        mAdapter.setOnItemClickLitener(new GameAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mActivity, GameDetailActivity.class);
                intent.putExtra("gameLink", arrayList.get(position).getGameDetailLink());
                intent.putExtra("gameStar", arrayList.get(position).getGameStar());
                intent.putExtra("gameName", arrayList.get(position).getGameName());
                Log.e("weiwei1", arrayList.get(position).getGameDetailLink());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(mAdapter);
        //监听手指滑动
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                switch (newState) {
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        Log.i("Main", "用户在手指离开屏幕之前，由于滑了一下，视图仍然依靠惯性继续滑动");
                        //Glide.with(mActivity).pauseRequests();
                        //刷新
                        break;
                    case RecyclerView.SCROLL_STATE_IDLE:
                        Log.i("Main", "视图已经停止滑动");
                        // Glide.with(mActivity).resumeRequests();
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        Log.i("Main", "手指没有离开屏幕，视图正在滑动");
                        // Glide.with(mActivity).resumeRequests();
                        break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                if (!recyclerView.canScrollVertically(-1)) {
                    //T.show(mActivity,"已经到第一条");
                } else if (!recyclerView.canScrollVertically(1)) {
                    //T.show(mActivity,"到了最后一条");
                } else if (dy < 0) {
                    //T.show(mActivity,"正在向上滑动");
                } else if (dy > 0) {
                    // T.show(mActivity,"正在向下滑动");
                    if (lastVisibleItemPosition + 1 == mAdapter.getItemCount() - 5) {
                        Log.e("search", "loading executed");

                        if (!isLoading) {
                            isLoading = true;
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    loadMoreData();
                                    Log.d("search", "load more completed");
                                    isLoading = false;
                                }
                            }, 500);
                        }
                    }
                }
            }
        });
    }

    */
/**
     * 加载更多
     *//*

    private void loadMoreData() {
        curPage++;
        action = STATE_MORE;
        initData();
    }

}
*/
