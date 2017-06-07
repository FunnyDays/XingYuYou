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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.MCUtils.HttpUtils;
import com.xingyuyou.xingyuyou.Utils.StringUtils;
import com.xingyuyou.xingyuyou.Utils.glide.GlideCircleTransform;
import com.xingyuyou.xingyuyou.Utils.glide.GlideRoundTransform;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.adapter.CommHotAdapter;
import com.xingyuyou.xingyuyou.bean.community.TopViewRecommBean;
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

public class SearchActivity extends AppCompatActivity {
    private int PAGENUMBER = 1;
    private int GAMEPAGENUMBER = 1;
    private String KeyWords;
    private Toolbar mToolbar;
    private TextView mTextView;
    private SearchView mSearchView;
    private RecyclerView mSearchList;
    boolean isLoading = false;
    private List<GameTag> mGameTagList = new ArrayList<>();
    private List<GameTag> mGameTagAdapterList = new ArrayList<>();
    private List<GameDetailBean> mGameDetailList = new ArrayList<>();
    private List<GameDetailBean> mGameDetailAdapterList = new ArrayList<>();
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                String response = (String) msg.obj;
                JSONObject jo = null;
                if (response.contains("\"list\":null")) {
                    Toast.makeText(SearchActivity.this, "没有此游戏或更多游戏", Toast.LENGTH_SHORT).show();
                    mSearchListAdapter.notifyDataSetChanged();
                    return;
                }
                try {
                    jo = new JSONObject(response);
                    JSONArray ja = jo.getJSONArray("list");
                   // Log.e("hot", "解析数据：" + ja.toString());
                    Gson gson = new Gson();
                    mGameDetailList = gson.fromJson(ja.toString(),
                            new TypeToken<List<GameDetailBean>>() {
                            }.getType());
                    mGameDetailAdapterList.addAll(mGameDetailList);
                  //  Log.e("hot", "解析数据：" + mGameDetailAdapterList.toString());
                    mSearchListAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (msg.what == 2) {
                String response = (String) msg.obj;
                JSONObject jo = null;
                try {
                    jo = new JSONObject(response);
                    JSONArray ja = jo.getJSONArray("data");
                    Gson gson = new Gson();
                    mGameTagList = gson.fromJson(ja.toString(),
                            new TypeToken<List<GameTag>>() {
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
    private SearchListAdapter mSearchListAdapter;
    private RelativeLayout mRl_root;
    private LinearLayoutManager mLinearLayoutManager;

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
                .addParams("page", String.valueOf(PAGENUMBER))
                .url(XingYuInterface.GET_GAME_NAME_LIST)
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
                startActivity(new Intent(SearchActivity.this, GameDetailActivity.class)
                        .putExtra("game_name", mGameTagAdapterList.get(position).getGame_name().substring(0, mGameTagAdapterList.get(position).getGame_name().length() - 5))
                        .putExtra("game_id", mGameTagAdapterList.get(position).getId()));
                return true;
            }
        });
        //搜索结果列表页
        mSearchList = (RecyclerView) findViewById(R.id.rv_search_list);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mSearchList.setLayoutManager(mLinearLayoutManager);
        mSearchListAdapter = new SearchListAdapter();
        mSearchList.setAdapter(mSearchListAdapter);
        mSearchList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        //   Log.i("Main", "用户在手指离开屏幕之前，由于滑了一下，视图仍然依靠惯性继续滑动");
                        Glide.with(SearchActivity.this).pauseRequests();
                        //刷新
                        break;
                    case RecyclerView.SCROLL_STATE_IDLE:
                        //  Log.i("Main", "视图已经停止滑动");
                        Glide.with(SearchActivity.this).resumeRequests();
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        //  Log.i("Main", "手指没有离开屏幕，视图正在滑动");
                        Glide.with(SearchActivity.this).resumeRequests();
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
                    if (lastVisibleItemPosition + 1 == mSearchListAdapter.getItemCount() - 3) {
                        //  Log.e("search", "loading executed");
                        if (!isLoading) {
                            isLoading = true;
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    GAMEPAGENUMBER++;
                                    // Log.d("search", "load more completed");
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
                //跳转到查询结果详情界面
                displayResult();
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
                    mGameDetailAdapterList.clear();
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
                .addParams("game_name", query)
                .addParams("limit", String.valueOf(GAMEPAGENUMBER))
                .url(XingYuInterface.GET_GAME_LIST)
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
                        Log.e("hot", response + ":e");
                    }
                });
    }

    class GameTagAdapter extends TagAdapter {
        public GameTagAdapter(List<GameTag> datas) {
            super(datas);
        }

        @Override
        public View getView(FlowLayout parent, int position, Object o) {
            TextView tv = (TextView) LayoutInflater.from(SearchActivity.this).inflate(R.layout.tv,
                    mTagFlowLayout, false);
            tv.setText(((GameTag) o).getGame_name().substring(0, ((GameTag) o).getGame_name().length() - 5));
            return tv;
        }
    }

    class SearchListAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(SearchActivity.this).inflate(R.layout.item_seaech_game_view, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            ((ItemViewHolder) holder).gameName.setText(mGameDetailAdapterList.get(position).getGame_name());
            Glide.with(getApplication())
                    .load(mGameDetailAdapterList.get(position).getIcon())
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .transform(new GlideRoundTransform(getApplication()))
                    .into(((ItemViewHolder) holder).gamePic);
            ((ItemViewHolder) holder).rl_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(SearchActivity.this, GameDetailActivity.class)
                            .putExtra("game_name", mGameDetailAdapterList.get(position).getGame_name())
                            .putExtra("game_id", mGameDetailAdapterList.get(position).getId()));
                }
            });
        }

        @Override
        public int getItemCount() {
            return mGameDetailAdapterList.size();
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {
            private final TextView gameName;
            private final ImageView gamePic;
            private final RelativeLayout rl_item;

            public ItemViewHolder(View itemView) {
                super(itemView);
                gameName = (TextView) itemView.findViewById(R.id.game_name);
                gamePic = (ImageView) itemView.findViewById(R.id.game_pic);
                rl_item = (RelativeLayout) itemView.findViewById(R.id.rl_item);
            }
        }
    }

}
