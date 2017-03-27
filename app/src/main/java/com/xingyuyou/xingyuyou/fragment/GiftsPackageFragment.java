package com.xingyuyou.xingyuyou.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.ConvertUtils;
import com.xingyuyou.xingyuyou.Utils.GlideImageLoader;
import com.xingyuyou.xingyuyou.Utils.MCUtils.HttpUtils;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.adapter.DividerItemDecoration;
import com.xingyuyou.xingyuyou.base.BaseFragment;
import com.xingyuyou.xingyuyou.bean.GameGift;
import com.xingyuyou.xingyuyou.bean.hotgame.GameDetailBean;
import com.youth.banner.Banner;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

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
    private List<String> mDatas = new ArrayList<>();
    private CommonAdapter<String> mAdapter;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private List<GameGift> mGameGiftList =null;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.e("search",msg.obj.toString());
            JSONObject jo = null;
            mGameGiftList =new ArrayList<>();
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
        if (mGameGiftList==null)
            return;
        for (GameGift gameGift : mGameGiftList) {
            Log.e("gameGift",gameGift.toString());
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
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("file_type", String.valueOf(1));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.POST(handler, XingYuInterface.GET_GIFT_LIST,jsonObject.toString(),false);

    }
    @Override
    protected View initView() {
        initData();
        View view = View.inflate(mActivity, R.layout.fragment_gift, null);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.SwipeRefreshLayout);
        initSwipeRefreshLayout();
        mRecyclerView = (RecyclerView)view.findViewById(R.id.id_recyclerview);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL_LIST));

        mAdapter = new CommonAdapter<String>(mActivity, R.layout.item_list, mDatas)
        {
            @Override
            protected void convert(ViewHolder holder, String s, int position)
            {
                holder.setText(R.id.game_name, s + "宣传视频 : " + holder.getAdapterPosition() + " , " + holder.getLayoutPosition());
            }
        };
        initHeaderAndFooter();
        mRecyclerView.setAdapter(mHeaderAndFooterWrapper);

        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Toast.makeText(mActivity, "pos = " + position+ConvertUtils.dp2px(200), Toast.LENGTH_SHORT).show();

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        return view;
    }

    private void initHeaderAndFooter()
    {
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
