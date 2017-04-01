package com.xingyuyou.xingyuyou.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.IntentUtils;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.SPUtils;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.bean.MyGameGift;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class MyGiftActivity extends AppCompatActivity {

    /*
    {
    "status": -1,
    "msg": "你还没有领取礼包"
     }

     {
    "status": 1,
    "msg": [{
        "game_name": "成吉思汗(安卓版)",
        "gift_name": "新手礼包",
        "record_novice": "170322654SKTT47",
        "icon": "http://xingyuyou.com/Uploads/Picture/2017-03-22/58d1dd7f4754c.png",
        "desribe": "破旧的银票*5、世传水晶*10、还童书*5\r\n",
        "novice": 998,
        "gift_id": "15"
    }]
     }
     */


    private Toolbar mToolbar;
    private String mUserId;
    private List<MyGameGift> mList = new ArrayList();
    private List<MyGameGift> mMyGiftList;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Log.e("gift", "-------------" + msg.obj.toString());
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(msg.obj.toString());
                        String status = jsonObject.getString("status");
                        if (status.equals("-1")){
                            mTvWutu.setVisibility(View.VISIBLE);
                            Toast.makeText(MyGiftActivity.this, ""+jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                        if (status.equals("1")){

                                JSONArray ja = jsonObject.getJSONArray("msg");
                                Log.e("mygift", "解析数据："+  ja.toString());
                                Gson gson = new Gson();
                                mMyGiftList = gson.fromJson(ja.toString(),
                                        new TypeToken<List<MyGameGift>>() {
                                        }.getType());
                            mList.addAll(mMyGiftList);
                            mAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


            }

        }
    };
    private TextView mTvWutu;
    private RecyclerView mRecyclerView;
    private CommonAdapter<MyGameGift> mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_gift);
        initData();
        initView();
        initToolBar();
    }

    private void initView() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.SwipeRefreshLayout);
        initSwipeRefreshLayout();
        mTvWutu = (TextView) findViewById(R.id.tv_libao);
        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CommonAdapter<MyGameGift>(this, R.layout.item_my_gift_list, mList) {

            @Override
            protected void convert(ViewHolder holder, final MyGameGift gameGift, int position) {
                Glide.with(MyGiftActivity.this).load(gameGift.getIcon()).into((ImageView) holder.getView(R.id.game_pic));
                holder.setText(R.id.game_name, gameGift.getGame_name());
                holder.setText(R.id.game_intro, gameGift.getDesribe());
                holder.setText(R.id.game_type, gameGift.getGift_name());
                holder.setText(R.id.game_code, gameGift.getRecord_novice());
                Log.e("gameGift", gameGift.toString());
                Button button = (Button) holder.getView(R.id.bt_uninstall);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ClipboardManager systemService = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                        ClipData text = ClipData.newPlainText("text", gameGift.getRecord_novice());
                        systemService.setPrimaryClip(text);
                        Toast.makeText(MyGiftActivity.this, "已经复制到剪贴板", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        if (!UserUtils.logined())
            return;
        SPUtils user_data = new SPUtils("user_data");
        mUserId = user_data.getString("id");
        getUserGiftPac();
    }

    private void getUserGiftPac() {
        Log.e("gift", "eee");
        RequestParams params = new RequestParams(XingYuInterface.MY_GIFT);
        params.addParameter("user_id", mUserId);
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
                mHandler.obtainMessage(1, json).sendToTarget();
            }

            @Override
            public boolean onCache(String json) {
                return true;
            }
        });
    }

    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("我的礼包");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                mHandler.postDelayed(new Runnable() {
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
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });

    }

}
