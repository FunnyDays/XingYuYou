package com.xingyuyou.xingyuyou.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.ConvertUtils;
import com.xingyuyou.xingyuyou.Utils.GlideImageLoader;
import com.xingyuyou.xingyuyou.Utils.IntentUtils;
import com.xingyuyou.xingyuyou.activity.PostingActivity;
import com.xingyuyou.xingyuyou.adapter.DividerItemDecoration;
import com.xingyuyou.xingyuyou.base.BaseFragment;
import com.youth.banner.Banner;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/6/28.
 */
public class CommunityFragment extends BaseFragment {


    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private Handler handler=new Handler();
    private List<String> mDatas = new ArrayList<>();
    private CommonAdapter<String> mAdapter;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private FloatingActionButton mFabPost;

    public static CommunityFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        CommunityFragment fragment = new CommunityFragment();
        fragment.setArguments(args);
        return fragment;
    }


    /**
     * 初始化数据
     */
    @Override
    public void initData() {
            for (int i = 'A'; i <= 'z'; i++)
            {
                mDatas.add((char) i + "");
            }
    }
    @Override
    protected View initView() {
        initData();
        View view = View.inflate(mActivity, R.layout.fragment_community, null);
        mFabPost = (FloatingActionButton) view.findViewById(R.id.fab_add_comment);
        mFabPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.startActivity(mActivity, PostingActivity.class);
            }
        });
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
