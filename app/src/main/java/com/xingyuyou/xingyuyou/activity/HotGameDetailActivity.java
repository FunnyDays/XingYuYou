package com.xingyuyou.xingyuyou.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.adapter.GameDetailPicAdapter;
import com.xingyuyou.xingyuyou.bean.hotgame.GameDetailBean;
import com.xingyuyou.xingyuyou.bean.hotgame.HotGameBean;
import com.xingyuyou.xingyuyou.weight.ProgressButton;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class HotGameDetailActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private Intent mIntent;
    private ImageView mGameIcon;
    private TextView mGameName;
    private TextView mGameType;
    private TextView mGameVersion;
    private TextView mGameSize;
    private TextView mGameIntro;
    private List<GameDetailBean> mGameDetailList=null;
    ArrayList<String> gamePics=new ArrayList<>();
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                String response = (String) msg.obj;
                JSONObject jo = null;
                mGameDetailList=new ArrayList<>();
                try {
                    jo = new JSONObject(response);
                    JSONArray ja = jo.getJSONArray("list");
                    // Log.e("hot", "解析数据："+  ja.toString());
                    Gson gson = new Gson();
                    mGameDetailList= gson.fromJson(ja.toString(),
                            new TypeToken<List<GameDetailBean>>() {
                            }.getType());
                    setValues();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    private RecyclerView mRecyclerView;
    private GameDetailPicAdapter mGameDetailPicAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_game_detail);
        mIntent = getIntent();
        initToolBar();
        initView();
        initData();
    }

    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(mIntent.getStringExtra("game_name"));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initData() {
        OkHttpUtils.post()//
                .url(XingYuInterface.GET_GAME_DETAILS)
                .tag(this)//
                .addParams("game_id", mIntent.getStringExtra("game_id"))
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("detail", e.toString() + ":e");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("game_id", response + "");
                        mHandler.obtainMessage(1, response).sendToTarget();
                    }
                });
    }

    private void initView() {
        mGameIcon = (ImageView) findViewById(R.id.iv_game_pic);
        mGameName = (TextView) findViewById(R.id.tv_game_name);
        mGameType = (TextView) findViewById(R.id.tv_game_type);
        mGameVersion = (TextView) findViewById(R.id.tv_game_version);
        mGameSize = (TextView) findViewById(R.id.tv_game_size);
        mGameIntro = (TextView) findViewById(R.id.tv_content);

        //游戏介绍图片
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mGameDetailPicAdapter = new GameDetailPicAdapter(this, gamePics);
        mGameDetailPicAdapter.setOnItemClickLitener(new GameDetailPicAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
               /* startActivity(new Intent(Game10Activity.this, imageDetailPagerActivity.class)
                        .putStringArrayListExtra("gameLink", gamePics));*/
            }
        });
        mRecyclerView.setAdapter(mGameDetailPicAdapter);
        //下载按钮
        ProgressButton progressButton = (ProgressButton) findViewById(R.id.bt_bottom_install);
    }
    private void setValues() {
        Glide.with(this).load(mGameDetailList.get(0).getIcon()).into(mGameIcon);
        mGameName.setText(mGameDetailList.get(0).getGame_name());
        mGameType.setText(mGameDetailList.get(0).getGame_type_id());
        mGameVersion.setText(mGameDetailList.get(0).getVersion());
        mGameSize.setText(mGameDetailList.get(0).getGame_size());
        mGameIntro.setText(mGameDetailList.get(0).getIntroduction());

        //游戏介绍图

        gamePics.addAll(mGameDetailList.get(0).getScreenshot());
        mGameDetailPicAdapter.notifyDataSetChanged();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
