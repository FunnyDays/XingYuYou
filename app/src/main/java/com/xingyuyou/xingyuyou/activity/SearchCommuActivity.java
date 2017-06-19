package com.xingyuyou.xingyuyou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.StringUtils;
import com.xingyuyou.xingyuyou.Utils.glide.GlideRoundTransform;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.adapter.CommHotAdapter;
import com.xingyuyou.xingyuyou.bean.community.PostListBean;
import com.xingyuyou.xingyuyou.bean.community.PostListBeanTest;
import com.xingyuyou.xingyuyou.bean.community.SearchPopularTags;
import com.xingyuyou.xingyuyou.bean.hotgame.GameDetailBean;
import com.xingyuyou.xingyuyou.bean.hotgame.GameTag;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class SearchCommuActivity extends AppCompatActivity {
    private static boolean CLEAR_DATA = false;
    private List mPostList = new ArrayList();
    private List<PostListBeanTest> mPostAdapterList=new ArrayList();
    private int PAGENUMBER = 1;
    boolean isLoading = false;
    private int GAMEPAGENUMBER = 1;
    private ProgressBar mPbNodata;
    private TextView mTvNodata;
    private String KeyWords;
    private Toolbar mToolbar;
    private TextView mTextView;
    private SearchView mSearchView;
    private RecyclerView mSearchList;
    private List<SearchPopularTags> mGameTagList = new ArrayList<>();
    private List<SearchPopularTags> mGameTagAdapterList = new ArrayList<>();
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (msg.obj.toString().contains("\"data\":null")) {
                    Toast.makeText(SearchCommuActivity.this, "已经没有更多数据", Toast.LENGTH_SHORT).show();
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
                    mPostAdapterList.addAll(mPostList);
                    if (mPostAdapterList.size()<=20){
                        mPbNodata.setVisibility(View.GONE);
                        mTvNodata.setText("已经没有更多数据");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //更新UI
                if (mAdapter != null)
                    mAdapter.notifyDataSetChanged();
            }
            if (msg.what == 2) {
                if (msg.obj.toString().contains("\"data\":null")) {
                    Toast.makeText(SearchCommuActivity.this, "已经没有更多数据", Toast.LENGTH_SHORT).show();
                    return;
                }
                String response = (String) msg.obj;
                JSONObject jo = null;
                try {
                    jo = new JSONObject(response);
                    JSONArray ja = jo.getJSONArray("data");
                    Gson gson = new Gson();
                    mGameTagList = gson.fromJson(ja.toString(),
                            new TypeToken<List<SearchPopularTags>>() {
                            }.getType());
                    mGameTagAdapterList.clear();
                    mGameTagAdapterList.addAll(mGameTagList);
                    mGameTagAdapter.notifyDataChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    private TagFlowLayout mTagFlowLayout;
    private GameTagAdapter mGameTagAdapter;
    private TextView mTvChangeData;
    private RelativeLayout mRl_root;
    private LinearLayoutManager mLinearLayoutManager;
    private CommHotAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initData(PAGENUMBER);
        initToolbar();
        initView();
    }

    private void initData(int PAGENUMBER) {
        //获取游戏标签
        OkHttpUtils.post()//
                .url(XingYuInterface.POPULAR_TAGS)
                .addParams("page", String.valueOf(PAGENUMBER))
                .tag(this)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        // Log.e("hot", e.toString() + ":e");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        handler.obtainMessage(2, response).sendToTarget();
                    }
                });
    }

    private void initView() {
        //更换tag游戏
        mRl_root = (RelativeLayout) findViewById(R.id.rl_root);
        mTvChangeData = (TextView) findViewById(R.id.tv_change_data);
        mTvChangeData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PAGENUMBER++;
                initData(PAGENUMBER);
            }
        });
        //游戏tag
        mTagFlowLayout = (TagFlowLayout) findViewById(R.id.id_flowlayout);
        mGameTagAdapter = new GameTagAdapter(mGameTagAdapterList);
        mTagFlowLayout.setAdapter(mGameTagAdapter);
        mTagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                startActivity(new Intent(SearchCommuActivity.this, SearchCommuListActivity.class)
                        .putExtra("bid", mGameTagAdapterList.get(position).getId()));
                return true;
            }
        });
        //底部布局
        View loadingData = View.inflate(SearchCommuActivity.this, R.layout.default_loading, null);
        mPbNodata = (ProgressBar) loadingData.findViewById(R.id.pb_loading);
        mTvNodata = (TextView) loadingData.findViewById(R.id.loading_text);
        //搜索结果列表页
        mSearchList = (RecyclerView) findViewById(R.id.rv_search_list);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mSearchList.setLayoutManager(mLinearLayoutManager);
        mAdapter = new CommHotAdapter(7,SearchCommuActivity.this, mPostAdapterList);
        mAdapter.setHeaderView(new View(SearchCommuActivity.this));
        mAdapter.setFooterView(loadingData);
        mSearchList.setAdapter(mAdapter);
        mSearchList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = mLinearLayoutManager.findLastVisibleItemPosition();
                if (!recyclerView.canScrollVertically(-1)) {
                } else if (!recyclerView.canScrollVertically(1)) {
                } else if (dy < 0) {
                } else if (dy > 0) {
                    if (lastVisibleItemPosition + 1 == mAdapter.getItemCount() - 5) {
                        //  Log.e("search", "loading executed");
                        if (!isLoading) {
                            isLoading = true;
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    GAMEPAGENUMBER++;
                                    doSearchToDisplay(KeyWords,GAMEPAGENUMBER);
                                    isLoading = false;
                                }
                            }, 200);
                        }
                    }
                }

            }
        });
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.ab_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(item);
        //展开搜索界面
        mSearchView.setIconified(false);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //点击搜索按钮的监听
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            //搜索框内容改变的监听
            @Override
            public boolean onQueryTextChange(String newText) {
                KeyWords=newText;
                if (StringUtils.isEmpty(newText)) {
                    mRl_root.setVisibility(View.VISIBLE);
                    mSearchList.setVisibility(View.GONE);
                } else {
                    mRl_root.setVisibility(View.GONE);
                    mSearchList.setVisibility(View.VISIBLE);
                    //查询并显示
                    mPostAdapterList.clear();
                    mAdapter.notifyDataSetChanged();
                    GAMEPAGENUMBER=1;
                    doSearchToDisplay(KeyWords,GAMEPAGENUMBER);
                }
                return true;
            }
        });
        return true;
    }

    /**
     * 跳转到查询详情界面
     */
    private void displayResult() {

    }

    /**
     * 查询后台所有相关游戏并展示出来
     */
    private void doSearchToDisplay(String query,int GAMEPAGENUMBER) {
        //获取游戏标签
        OkHttpUtils.post()//
                .addParams("keyword", query)
                .addParams("type", "5")
                .addParams("bid", "1")
                .addParams("attribute", "1")
                .addParams("fid", "1")
                .addParams("uid", UserUtils.getUserId())
                .addParams("page", String.valueOf(GAMEPAGENUMBER))
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

    class GameTagAdapter extends TagAdapter {
        public GameTagAdapter(List<SearchPopularTags> datas) {
            super(datas);
        }

        @Override
        public View getView(FlowLayout parent, int position, Object o) {
            TextView tv = (TextView) LayoutInflater.from(SearchCommuActivity.this).inflate(R.layout.tv,
                    mTagFlowLayout, false);
            tv.setText(((SearchPopularTags) o).getLabel_name());
            return tv;
        }
    }



}
