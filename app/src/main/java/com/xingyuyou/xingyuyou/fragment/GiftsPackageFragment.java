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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.ConvertUtils;
import com.xingyuyou.xingyuyou.Utils.GlideImageLoader;
import com.xingyuyou.xingyuyou.Utils.IntentUtils;
import com.xingyuyou.xingyuyou.Utils.MCUtils.HttpUtils;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.activity.HotGameDetailActivity;
import com.xingyuyou.xingyuyou.activity.LoginActivity;
import com.xingyuyou.xingyuyou.adapter.DividerItemDecoration;
import com.xingyuyou.xingyuyou.base.BaseFragment;
import com.xingyuyou.xingyuyou.bean.GameGift;
import com.youth.banner.Banner;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


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

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.obj == null)return;
            Log.e("search", msg.obj.toString());
            JSONObject jo = null;
            try {
                jo = new JSONObject(msg.obj.toString());
                JSONArray ja = jo.getJSONArray("list");
                // Log.e("hot", "解析数据："+  ja.toString());
                Gson gson = new Gson();
                mGameGiftList = gson.fromJson(ja.toString(),
                        new TypeToken<List<GameGift>>() {
                        }.getType());
                setValues();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };


    /**
     * 展示数据
     */
    private void setValues() {
        if (mGameGiftList == null)
            return;
       /* for (GameGift gameGift : mGameGiftList) {
            Log.e("gameGift",gameGift.toString());
        }*/
        //mDatas.addAll(mGameGiftList);
        mAdapter.notifyDataSetChanged();
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
        for (int i = 0; i < 9; i++)
        {
            GameGift gameGift = new GameGift();
            gameGift.setGame_name(i+":id游戏");
            mDatas.add(gameGift);
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("file_type", String.valueOf(1));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.POST(handler, XingYuInterface.GET_GIFT_LIST, jsonObject.toString(), false);

    }

    @Override
    protected View initView() {
        initData();
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
            protected void convert(ViewHolder holder, GameGift gameGift, int position) {
                Glide.with(mActivity).load(gameGift.getIcon()).into((ImageView) holder.getView(R.id.game_pic));
                holder.setText(R.id.game_name, gameGift.getGame_name());
                holder.setText(R.id.game_intro, gameGift.getDesribe());
                holder.setText(R.id.game_type, gameGift.getGiftbag_name());
                holder.setText(R.id.game_size, gameGift.getGame_size());
                holder.setText(R.id.tv_gift_number, "剩余数量：" + gameGift.getNovice());
                Log.e("gameGift", gameGift.toString());
            }

            @Override
            public void onViewHolderCreated(ViewHolder holder, View itemView) {
                super.onViewHolderCreated(holder, itemView);
                Button button = (Button) holder.getView(R.id.bt_uninstall);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (UserUtils.logined()) {
                            Toast.makeText(mActivity, "领取成功，请到个人中心查看详情 ", Toast.LENGTH_SHORT).show();
                        }else {
                            IntentUtils.startActivity(mActivity, LoginActivity.class);
                        }
                    }
                });
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
        //加载更多
        LoadMore();
        return view;
    }

    private void LoadMore() {
        mLoadMoreWrapper = new LoadMoreWrapper(mAdapter);
        mLoadMoreWrapper.setLoadMoreView(R.layout.default_loading);
        mLoadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener()
        {
            @Override
            public void onLoadMoreRequested()
            {
                for (int i = 0; i < 9; i++)
                {
                    GameGift gameGift = new GameGift();
                    gameGift.setGame_name(i+":id游戏");
                    mDatas.add(gameGift);
                }
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        for (int i = 0; i < 9; i++)
                        {
                            GameGift gameGift = new GameGift();
                            gameGift.setGame_name(i+":id游戏");
                            mDatas.add(gameGift);
                        }
                        mLoadMoreWrapper.notifyDataSetChanged();

                    }
                }, 3000);
                Toast.makeText(mActivity, "加载更多", Toast.LENGTH_SHORT).show();
            }
        });
        mRecyclerView.setAdapter(mLoadMoreWrapper);
    }

    private void initHeaderAndFooter() {
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mAdapter);

        Banner banner = new Banner(mActivity);
        FrameLayout.LayoutParams layoutParams = new Banner.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ConvertUtils.dp2px(200));
        banner.setLayoutParams(layoutParams);
        banner.setImages(mDatas).setImageLoader(new GlideImageLoader()).start();
        mHeaderAndFooterWrapper.addHeaderView(banner);

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

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });

    }


}
