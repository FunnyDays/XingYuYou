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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.MCUtils.HttpUtils;
import com.xingyuyou.xingyuyou.Utils.StringUtils;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.bean.community.TopViewRecommBean;
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
    private Toolbar mToolbar;
    private TextView mTextView;
    private SearchView mSearchView;
    private RecyclerView mSearchList;
    private List<GameTag> mGameTagList = new ArrayList<>();
    private List<GameTag> mGameTagAdapterList = new ArrayList<>();
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
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
                startActivity(new Intent(SearchActivity.this,GameDetailActivity.class)
                        .putExtra("game_name",mGameTagAdapterList.get(position).getGame_name().substring(0,mGameTagAdapterList.get(position).getGame_name().length()-5))
                        .putExtra("game_id",mGameTagAdapterList.get(position).getId()));
                return true;
            }
        });
        //搜索结果列表页
        mSearchList = (RecyclerView) findViewById(R.id.rv_search_list);
        mSearchList.setLayoutManager(new LinearLayoutManager(this));
        mSearchList.setAdapter(new SearchListAdapter());
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
                Toast.makeText(SearchActivity.this, "11" + query, Toast.LENGTH_SHORT).show();
                //跳转到查询结果详情界面
                displayResult();
                return false;
            }

            //搜索框内容改变的监听
            @Override
            public boolean onQueryTextChange(String newText) {
                Toast.makeText(SearchActivity.this, "22" + newText, Toast.LENGTH_SHORT).show();
                //查询并显示
                Log.e("search", newText);
                doSearchToDisplay(newText);
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
    private void doSearchToDisplay(String query) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("game_name", String.valueOf(query));
            jsonObject.put("limit", String.valueOf(1));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.POST(handler, XingYuInterface.GET_GAME_LIST, jsonObject.toString(), true);
    }

    class GameTagAdapter extends TagAdapter {
        public GameTagAdapter(List<GameTag> datas) {
            super(datas);
        }
        @Override
        public View getView(FlowLayout parent, int position, Object o) {
            TextView tv = (TextView) LayoutInflater.from(SearchActivity.this).inflate(R.layout.tv,
                    mTagFlowLayout, false);
            tv.setText(((GameTag)o).getGame_name().substring(0,((GameTag)o).getGame_name().length()-5));
            return tv;
        }
    }

    class SearchListAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(SearchActivity.this).inflate(R.layout.item_game_view, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {
            private final TextView gameIntro;
            private final TextView gameName;
            private final TextView gameSize;
            private final ImageView gamePic;

            public ItemViewHolder(View itemView) {
                super(itemView);
                gameIntro = (TextView) itemView.findViewById(R.id.game_intro);
                gameName = (TextView) itemView.findViewById(R.id.game_name);
                gameSize = (TextView) itemView.findViewById(R.id.game_size);
                gamePic = (ImageView) itemView.findViewById(R.id.game_pic);
            }
        }
    }

}
