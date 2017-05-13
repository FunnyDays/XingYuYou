package com.xingyuyou.xingyuyou.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.adapter.CommSortAdapter;
import com.xingyuyou.xingyuyou.bean.community.PostTopAndWellBean;
import com.xingyuyou.xingyuyou.bean.community.SortPostListBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Call;

public class CollectListActivity extends AppCompatActivity {
/*
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                */
/*if (msg.obj.toString().contains("\"data\":null")) {
                    Toast.makeText(CollectListActivity.this, "已经没有更多数据", Toast.LENGTH_SHORT).show();
                    mPbNodata.setVisibility(View.GONE);
                    mTvNodata.setText("已经没有更多数据");
                    return;
                }*//*

                String response = (String) msg.obj;
                JSONObject jo = null;
                try {
                    jo = new JSONObject(response);
                    JSONArray ja = jo.getJSONArray("data");
                    //   Log.e("post", "解析数据："+  ja.toString());
                    Gson gson = new Gson();
                    mPostList = gson.fromJson(ja.toString(),
                            new TypeToken<List<SortPostListBean>>() {
                            }.getType());
                    mPostAdapterList.addAll(mPostList);
                    ja = jo.getJSONArray("top_well");
                    gson = new Gson();
                    mPostTopWellList = gson.fromJson(ja.toString(),
                            new TypeToken<List<PostTopAndWellBean>>() {
                            }.getType());
                    Log.e("post", "解析数据："+  mPostTopWellList.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //更新UI
                if (mCommHotAdapter != null)
                    mCommHotAdapter.notifyDataSetChanged();
            }
        }
    };
*/
    private int  PAGENUMBER = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_list);
        initView();
        initData(PAGENUMBER);
    }
    public void initData(int PAGENUMBER) {
        OkHttpUtils.post()//
                .addParams("page",String.valueOf(PAGENUMBER))
                .addParams("fid",getIntent().getStringExtra("list_id"))
                .url(XingYuInterface.GET_POSTS_CLASS_LIST)
                .tag(this)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        // Log.e("hot", e.toString() + ":e");
                    }
                    @Override
                    public void onResponse(String response, int id) {
                       // handler.obtainMessage(1, response).sendToTarget();
                    }
                });
    }
    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_collect_list);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
       /* new CommSortAdapter(this,);
        recyclerView.setAdapter();*/
    }
}
