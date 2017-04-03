package com.xingyuyou.xingyuyou.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.IntentUtils;
import com.xingyuyou.xingyuyou.Utils.MCUtils.HttpUtils;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.activity.HotGameDetailActivity;
import com.xingyuyou.xingyuyou.activity.LoginActivity;
import com.xingyuyou.xingyuyou.base.BaseFragment;
import com.xingyuyou.xingyuyou.bean.GameGift;
import com.xingyuyou.xingyuyou.bean.HotBannerBean;
import com.youth.banner.Banner;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


/**
 * Created by Administrator on 2016/6/28.
 */
public class GiftsPackageFragment extends BaseFragment {


    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private Toolbar mToolbar;
    private List<GameGift> mDatas = new ArrayList<>();
    private CommonAdapter<GameGift> mAdapter;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private List<GameGift> mGameGiftList = new ArrayList<>();
    private LoadMoreWrapper mLoadMoreWrapper;
    private int mPageNumber = 1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.obj == null) return;
            if (msg.what==2){
                JSONObject jo = null;
                try {
                    jo = new JSONObject(msg.obj.toString());
                    if (jo.getString("status").equals("1")) {
                        Toast.makeText(mActivity, "领取成功，请到个人中心查看详情 ", Toast.LENGTH_SHORT).show();
                    }
                    if (jo.getString("status").equals("2")) {
                        Toast.makeText(mActivity, "您已经领取过这个礼包，请到个人中心查看详情 ", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            if (msg.what==1){
                if (msg.obj.toString().contains("{\"list\":null}")) {
                    mLoadingText.setText("没有更多数据");
                    mPbLoading.setVisibility(View.GONE);
                    Toast.makeText(mActivity, "已经没有更多数据", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.e("gift2", msg.obj.toString());
                JSONObject jo = null;
                try {
                    jo = new JSONObject(msg.obj.toString());
                    JSONArray ja = jo.getJSONArray("list");
                    Gson gson = new Gson();
                    mGameGiftList = gson.fromJson(ja.toString(),
                            new TypeToken<List<GameGift>>() {
                            }.getType());
                    setValues();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    private View mLoading;
    private TextView mLoadingText;
    private ProgressBar mPbLoading;


    /**
     * 展示数据
     */
    private void setValues() {
        if (mPageNumber == 1) {
            mPageNumber++;
            mDatas.clear();
        }
        if (mGameGiftList == null || mGameGiftList.size() == 0)
            return;
       /* for (GameGift gameGift : mGameGiftList) {
            Log.e("gameGift",gameGift.toString());
        }*/
        mDatas.addAll(mGameGiftList);
        if (mDatas.size() <= 10) {
            mLoadMoreWrapper.notifyDataSetChanged();

        } else {
            mLoadMoreWrapper.notifyDataSetChanged();

        }
    }

    public static GiftsPackageFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        GiftsPackageFragment fragment = new GiftsPackageFragment();
        fragment.setArguments(args);
        return fragment;
    }


    /**
     * 初始化数据
     */
    @Override
    public void initData() {
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
    protected View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_gift, null);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mToolbar.setTitle("领取礼包");
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.SwipeRefreshLayout);
        initSwipeRefreshLayout();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.id_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL_LIST));

        mAdapter = new CommonAdapter<GameGift>(mActivity, R.layout.item_gift_list, mDatas) {
            @Override
            protected void convert(ViewHolder holder, final GameGift gameGift, final int position) {
                Glide.with(mActivity).load(gameGift.getIcon()).into((ImageView) holder.getView(R.id.game_pic));
                holder.setText(R.id.game_name, gameGift.getGame_name());
                holder.setText(R.id.game_intro, gameGift.getDesribe());
                holder.setText(R.id.game_type, gameGift.getGiftbag_name());
                holder.setText(R.id.game_size, gameGift.getGame_size());
                holder.setText(R.id.tv_gift_number, "剩余数量：" + gameGift.getNovice());
                Log.e("gameGift", gameGift.toString());
                Button button = (Button) holder.getView(R.id.bt_uninstall);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (UserUtils.logined()) {
                            getData(2,gameGift.getGiftid());
                        } else {
                            IntentUtils.startActivity(mActivity, LoginActivity.class);
                        }
                    }
                });
            }

            @Override
            public void onViewHolderCreated(ViewHolder holder, View itemView) {
                super.onViewHolderCreated(holder, itemView);

            }
        };

        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(mActivity, HotGameDetailActivity.class);
                intent.putExtra("game_id", mDatas.get(position).getGame_id());
                intent.putExtra("game_name", mDatas.get(position).getGame_name());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        //头布局
       // initHeaderAndFooter();
        //加载更多
        LoadMore();
        return view;
    }

    /**
     * 领取礼包 GET_GIFT_LIST
     */
    private void getGift(String params) {
        HttpUtils.POST(handler,XingYuInterface.GET_GIFT_LIST,params,false);
    }

    private void LoadMore() {
        mLoadMoreWrapper = new LoadMoreWrapper(mAdapter);
        mLoading = View.inflate(mActivity, R.layout.default_loading, null);
        //设置底部布局
        mLoadingText = (TextView) mLoading.findViewById(R.id.loading_text);
        mPbLoading = (ProgressBar) mLoading.findViewById(R.id.pb_loading);
        mLoadMoreWrapper.setLoadMoreView(mLoading);
        mLoadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getData(1,null);
                mPageNumber++;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //mLoadMoreWrapper.notifyDataSetChanged();
                    }
                }, 3000);

            }
        });
        mRecyclerView.setAdapter(mLoadMoreWrapper);
    }

    private void initHeaderAndFooter() {
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mAdapter);
        //轮播图设置
        mHeaderAndFooterWrapper.addHeaderView(null);

    }

    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });
        //下拉刷新
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPageNumber = 1;
                getData(1,null);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });

    }

    private void getData(final int type, String giftid) {
        RequestParams params=null;
        if (type==2){
            params = new RequestParams(XingYuInterface.RCEIVE_GIFT);
            params.addParameter("mid", UserUtils.getUserId());
            params.addParameter("giftid", giftid);
        }else {
            params = new RequestParams(XingYuInterface.GET_GIFT_LIST);
            params.addParameter("limit", mPageNumber);
        }
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onCancelled(CancelledException arg0) {
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                Log.e("gift", arg0.toString());

            }

            @Override
            public void onFinished() {
            }

            @Override
            public void onSuccess(String json) {
                Log.e("gift", json);
                handler.obtainMessage(type, json).sendToTarget();
            }

            @Override
            public boolean onCache(String json) {
                return true;
            }
        });
    }

}
