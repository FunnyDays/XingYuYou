package com.xingyuyou.xingyuyou.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.DiffCallBack;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.adapter.CommHeaderFooterAdapter;
import com.xingyuyou.xingyuyou.bean.community.LabelClassBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class SelectCommTagActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private SelectCommTagAdapter mCommTagAdapter;
    private List<LabelClassBean> mDatas = new ArrayList<>();
    private List<LabelClassBean> mLabelClassList=new ArrayList<>();
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                String response = (String) msg.obj;
                Log.e("weiwei",response);
                JSONObject jo = null;
                try {
                    jo = new JSONObject(response);
                    String string = jo.getString("status");
                    if (string.equals("1")){
                        JSONArray ja = jo.getJSONArray("data");
                        Gson gson = new Gson();
                        mLabelClassList = gson.fromJson(ja.toString(),
                                new TypeToken<List<LabelClassBean>>() {
                                }.getType());
                        mDatas.addAll(mLabelClassList);
                        mCommTagAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_comm_tag);
        initData();
        initRecycleView();
        initToolbar();
    }

    private void initData() {
        OkHttpUtils.post()//
                .url(XingYuInterface.GET_LABEL_CLASS)
                .tag(this)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        mHandler.obtainMessage(1, response).sendToTarget();
                    }
                });
    }

    private void initRecycleView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mCommTagAdapter = new SelectCommTagAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mCommTagAdapter);
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private class SelectCommTagAdapter extends RecyclerView.Adapter {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View layout = LayoutInflater.from(SelectCommTagActivity.this).inflate(R.layout.item_comm_tag_list, parent, false);
            return new ItemViewHolder(layout);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (holder instanceof ItemViewHolder) {
                ((ItemViewHolder) holder).mGameName.setText(mDatas.get(position).getClass_name());
                Glide.with(SelectCommTagActivity.this)
                        .load(mDatas.get(position).getClass_image())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(((ItemViewHolder) holder).mGamePic);
                ((ItemViewHolder) holder).mRelativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(SelectCommTagActivity.this, PostingActivity.class);
                        intent.putExtra("PostCommId", mDatas.get(position).getId());
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }
        class ItemViewHolder extends RecyclerView.ViewHolder {
            private ImageView mGamePic;
            private TextView mGameName;
            private RelativeLayout mRelativeLayout;

            public ItemViewHolder(View itemView) {
                super(itemView);
                mGameName = (TextView) itemView.findViewById(R.id.tv_class_name);
                mGamePic = (ImageView) itemView.findViewById(R.id.iv_class_image);
                mRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.rl_root_comm_tags);
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
