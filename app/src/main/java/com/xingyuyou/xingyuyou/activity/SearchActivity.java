package com.xingyuyou.xingyuyou.activity;

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

import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.MCUtils.HttpUtils;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;

import org.json.JSONException;
import org.json.JSONObject;

public class SearchActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextView mTextView;
    private SearchView mSearchView;
    private RecyclerView mSearchList;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.e("search",msg.obj.toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initToolbar();
        initView();
    }

    private void initView() {
        //搜索结果列表页
        mSearchList = (RecyclerView) findViewById(R.id.rv_search_list);
        mSearchList.setLayoutManager(new LinearLayoutManager(this));
        mSearchList.setAdapter(new SearchListAdapter());
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
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
                Toast.makeText(SearchActivity.this, "" + query, Toast.LENGTH_SHORT).show();
                //跳转到查询结果详情界面
                displayResult();
                return false;
            }

            //搜索框内容改变的监听
            @Override
            public boolean onQueryTextChange(String newText) {
                Toast.makeText(SearchActivity.this, "" + newText, Toast.LENGTH_SHORT).show();
                //查询并显示
                Log.e("search",newText);
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
        HttpUtils.POST(handler, XingYuInterface.GET_GAME_LIST,jsonObject.toString(),false);
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
