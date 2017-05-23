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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.TimeUtils;
import com.xingyuyou.xingyuyou.Utils.glide.GlideCircleTransform;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.adapter.CommSortAdapter;
import com.xingyuyou.xingyuyou.bean.community.SortPostListBean;
import com.xingyuyou.xingyuyou.bean.god.GodBean;
import com.xingyuyou.xingyuyou.bean.god.GodDetailBean;
import com.xingyuyou.xingyuyou.bean.god.GodListBean;
import com.xingyuyou.xingyuyou.weight.infiniteViewPager.ImagePagerAdapter;
import com.xingyuyou.xingyuyou.weight.infiniteViewPager.InfinitePagerAdapter;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

import static com.xingyuyou.xingyuyou.Utils.net.XingYuInterface.GET_GODINFO;

public class GodDeatilActivity extends AppCompatActivity {
    private List<GodListBean> mGodList = new ArrayList<>();
    private List<GodListBean> mGodListDatas = new ArrayList<>();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
               /* if (msg.obj.toString().contains("\"data\":null")) {
                    Toast.makeText(mActivity, "已经没有更多数据", Toast.LENGTH_SHORT).show();
                    mPbNodata.setVisibility(View.GONE);
                    mTvNodata.setText("已经没有更多数据");
                    return;
                }*/
                String response = (String) msg.obj;
                JSONObject jo = null;
                try {
                    jo = new JSONObject(response);
                    String string = jo.getString("status");
                    if (string.equals("1")) {
                        JSONObject jsonObject = jo.getJSONObject("data");
                        //Log.e("hot", "解析数据："+  ja.toString());
                        Gson gson = new Gson();
                        GodDetailBean godDetailBean = gson.fromJson(jsonObject.toString(), GodDetailBean.class);
                        setValues(godDetailBean);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (msg.what == 2) {
                String response = (String) msg.obj;
                JSONObject jo = null;
                try {
                    jo = new JSONObject(response);
                    String string = jo.getString("status");
                    if (string.equals("1")) {
                        JSONArray ja = jo.getJSONArray("data");
                        Gson gson = new Gson();
                        mGodList = gson.fromJson(ja.toString(),
                                new TypeToken<List<GodListBean>>() {
                                }.getType());
                        mGodListDatas.addAll(mGodList);
                        mGodListAdapter.notifyDataSetChanged();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    boolean isLoading = false;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private GodListAdapter mGodListAdapter;

    private void setValues(GodDetailBean godDetailBean) {
        mTv_god_des.setText(godDetailBean.getGod_describe());
        Glide.with(getApplication()).load(godDetailBean.getImage_info()).into(mIv_god);
    }

    private Toolbar mToolbar;
    private int PAGENUMBER = 1;
    private TextView mTv_god_des;
    private ImageView mIv_god;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_god_deatil);
        initGodData();
        initData(1);
        initToolBar();
        initView();
    }

    private void initView() {
        mTv_god_des = (TextView) findViewById(R.id.tv_god_des);
        mIv_god = (ImageView) findViewById(R.id.iv_god);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLinearLayoutManager = new LinearLayoutManager(GodDeatilActivity.this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mGodListAdapter = new GodListAdapter();
        mRecyclerView.setAdapter(mGodListAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    if (lastVisibleItemPosition + 1 == mGodListAdapter.getItemCount() - 5) {
                        //  Log.e("search", "loading executed");
                        if (!isLoading) {
                            isLoading = true;
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    PAGENUMBER++;
                                    // Log.d("search", "load more completed");
                                    initData(PAGENUMBER);
                                    isLoading = false;
                                }
                            }, 200);
                        }
                    }
                }

            }
        });
    }

    public void initData(int PAGENUMBER) {
        OkHttpUtils.post()//
                .addParams("page", String.valueOf(PAGENUMBER))
                .addParams("god_id", getIntent().getStringExtra("gid"))
                .url(XingYuInterface.GET_ACTIVITY_LIST)
                .tag(this)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        // Log.e("hot", e.toString() + ":e");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mHandler.obtainMessage(2, response).sendToTarget();
                    }
                });
    }


    private void initGodData() {
        OkHttpUtils.post()//
                .addParams("god_id", getIntent().getStringExtra("gid"))
                .url(XingYuInterface.GET_GODINFO)
                .tag(this)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        // Log.e("hot", e.toString() + ":e");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mHandler.obtainMessage(1, response).sendToTarget();
                    }
                });
    }

    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("神社");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private class GodListAdapter extends RecyclerView.Adapter {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View layout = LayoutInflater.from(GodDeatilActivity.this).inflate(R.layout.item_god_activity_list, parent, false);
            return new ItemViewHolder(layout);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (mGodListDatas.size() != 0) {
                Glide.with(getApplication())
                        .load(R.mipmap.icon)
                        .transform(new GlideCircleTransform(getApplication()))
                        .dontAnimate()
                        .into(((ItemViewHolder) holder).mPhoto);
                ((ItemViewHolder) holder).mTv_title.setText(mGodListDatas.get(position).getSubject());
                ((ItemViewHolder) holder).tv_content.setText(mGodListDatas.get(position).getMessage());
                ((ItemViewHolder) holder).tv_time.setText(TimeUtils.getFriendlyTimeSpanByNow(Long.parseLong(mGodListDatas.get(position).getDateline() + "000")));
                ((ItemViewHolder) holder).item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(GodDeatilActivity.this,GodListDetailActivity.class)
                                .putExtra("activity_id",mGodListDatas.get(position).getId()));
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return mGodListDatas.size();
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {

            private final ImageView mPhoto;
            private final TextView mTv_title;
            private final TextView tv_content;
            private final TextView tv_time;
            private final RelativeLayout item;

            public ItemViewHolder(View itemView) {
                super(itemView);
                mPhoto = (ImageView) itemView.findViewById(R.id.civ_user_photo);
                mTv_title = (TextView) itemView.findViewById(R.id.tv_title);
                tv_content = (TextView) itemView.findViewById(R.id.tv_content);
                tv_time = (TextView) itemView.findViewById(R.id.tv_time);
                item = (RelativeLayout) itemView.findViewById(R.id.item);
            }
        }
    }
}
