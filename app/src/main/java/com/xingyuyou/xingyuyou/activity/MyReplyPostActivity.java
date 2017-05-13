package com.xingyuyou.xingyuyou.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.DiffCallBack;
import com.xingyuyou.xingyuyou.Utils.IntentUtils;
import com.xingyuyou.xingyuyou.Utils.SPUtils;
import com.xingyuyou.xingyuyou.Utils.TimeUtils;
import com.xingyuyou.xingyuyou.Utils.glide.GlideCircleTransform;
import com.xingyuyou.xingyuyou.Utils.glide.GlideRoundTransform;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.adapter.CommHotAdapter;
import com.xingyuyou.xingyuyou.adapter.FenLeiGameAdapter;
import com.xingyuyou.xingyuyou.bean.community.LabelClassBean;
import com.xingyuyou.xingyuyou.bean.user.MyReplyPostBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class MyReplyPostActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private MyReplyPostAdapter mPostAdapter;
    private String mUserId;
    private int PAGENUMBER=1;
    boolean isLoading = false;
    private List<MyReplyPostBean> mMyReplyPostList=new ArrayList<>();
    private List<MyReplyPostBean> mDatas=new ArrayList<>();
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (msg.obj.toString().contains("\"data\":null")) {
                    Toast.makeText(MyReplyPostActivity.this, "已经没有更多数据", Toast.LENGTH_SHORT).show();
                    mPbNodata.setVisibility(View.GONE);
                    mTvNodata.setText("已经没有更多数据");
                    return;
                }
                String response = (String) msg.obj;
                JSONObject jo = null;
                try {
                    jo = new JSONObject(response);
                    String string = jo.getString("status");
                    if (string.equals("1")){
                        JSONArray ja = jo.getJSONArray("data");
                        //Log.e("hot", "解析数据："+  ja.toString());
                        Gson gson = new Gson();
                        mMyReplyPostList = gson.fromJson(ja.toString(),
                                new TypeToken<List<MyReplyPostBean>>() {
                                }.getType());
                        mDatas.addAll(mMyReplyPostList);
                    }
                    if (mMyReplyPostList.size()<20){
                        Toast.makeText(MyReplyPostActivity.this, "已经没有更多数据", Toast.LENGTH_SHORT).show();
                        mPbNodata.setVisibility(View.GONE);
                        mTvNodata.setText("已经没有更多数据");
                    }
                    if (mPostAdapter!=null)
                        mPostAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    private ProgressBar mPbNodata;
    private TextView mTvNodata;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reply_post);
        initView();
        initToolBar();
        initData(1);
    }

    private void initData(int PAGENUMBER) {
        SPUtils user_data = new SPUtils("user_data");
        mUserId = user_data.getString("id");
        OkHttpUtils.post()//
                .url(XingYuInterface.MY_REPLIES_LIST)
                .addParams("uid", mUserId)
                .addParams("page", String.valueOf(PAGENUMBER))
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

    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("我的回帖");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {
        View loadingData = View.inflate(MyReplyPostActivity.this, R.layout.default_loading, null);
        mPbNodata = (ProgressBar) loadingData.findViewById(R.id.pb_loading);
        mTvNodata = (TextView) loadingData.findViewById(R.id.loading_text);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mPostAdapter = new MyReplyPostAdapter();
        mPostAdapter.setHeaderView(new View(MyReplyPostActivity.this));
        mPostAdapter.setFooterView(loadingData);
        mRecyclerView.setAdapter(mPostAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = mLinearLayoutManager.findLastVisibleItemPosition();
                if (!recyclerView.canScrollVertically(-1)) {
                } else if (!recyclerView.canScrollVertically(1)) {
                } else if (dy < 0) {
                } else if (dy > 0) {
                    if (lastVisibleItemPosition + 1 == mPostAdapter.getItemCount() - 4) {
                        if (!isLoading) {
                            isLoading = true;
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    PAGENUMBER++;
                                    Log.e("post","现在的数据是："+PAGENUMBER);
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

    private class MyReplyPostAdapter extends RecyclerView.Adapter{
        public static final int TYPE_HEADER = 0;  //说明是带有Header的
        public static final int TYPE_FOOTER = 1;  //说明是带有Footer的
        public static final int TYPE_NORMAL = 2;  //说明是不带有header和footer的

        //HeaderView, FooterView
        private View mHeaderView;
        private View mFooterView;

        //HeaderView和FooterView的get和set函数
        public View getHeaderView() {
            return mHeaderView;
        }

        public void setHeaderView(View headerView) {
            mHeaderView = headerView;
            notifyItemInserted(0);
        }

        public View getFooterView() {
            return mFooterView;
        }

        public void setFooterView(View footerView) {
            mFooterView = footerView;
            notifyItemInserted(getItemCount() - 1);
        }
        @Override
        public int getItemViewType(int position) {
            if (mHeaderView == null && mFooterView == null) {
                return TYPE_NORMAL;
            }
            if (position == 0) {
                //第一个item应该加载Header
                return TYPE_HEADER;
            }
            if (position == getItemCount() - 1) {
                //最后一个,应该加载Footer
                return TYPE_FOOTER;
            }
            return TYPE_NORMAL;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (mHeaderView != null && viewType == TYPE_HEADER) {
                return new ItemViewHolder(mHeaderView);
            }
            if (mFooterView != null && viewType == TYPE_FOOTER) {
                return new ItemViewHolder(mFooterView);
            }
            View layout = LayoutInflater.from(MyReplyPostActivity.this).inflate(R.layout.item_my_reply_list, parent, false);
            return new ItemViewHolder(layout);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if(getItemViewType(position) == TYPE_NORMAL){
                if(holder instanceof ItemViewHolder) {
                    ((ItemViewHolder) holder).mTvUserName.setText(mDatas.get(position-1).getNickname());
                    ((ItemViewHolder) holder).mTvReplyContent.setText(mDatas.get(position-1).getConenct());
                    ((ItemViewHolder) holder).mTvReplyTitle.setText(mDatas.get(position-1).getRe_conenct());
                    ((ItemViewHolder) holder).mTvReplyTime.setText(TimeUtils.getFriendlyTimeSpanByNow(Long.parseLong(mDatas.get(position - 1).getDateline() + "000")));
                    ((ItemViewHolder) holder).mTvReplyFloor.setText(mDatas.get(position-1).getFloor()+"楼");
                    Glide.with(getApplication())
                            .load(mDatas.get(position-1).getHead_image())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .transform(new GlideCircleTransform(MyReplyPostActivity.this))
                            .into(((ItemViewHolder) holder).mIvUserPhoto);
                    ((ItemViewHolder) holder).mItemOnclick.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MyReplyPostActivity.this, PostDetailActivity.class);
                            intent.putExtra("post_id", mDatas.get(position - 1).getId());
                            MyReplyPostActivity.this.startActivity(intent);
                        }
                    });
                    return;
                }
                return;
            }else if(getItemViewType(position) == TYPE_HEADER){
                return;
            }else{
                return;
            }
        }

        @Override
        public int getItemCount() {
            if (mHeaderView == null && mFooterView == null) {
                return mDatas.size();
            } else if (mHeaderView == null && mFooterView != null) {
                return mDatas.size() + 1;
            } else if (mHeaderView != null && mFooterView == null) {
                return mDatas.size() + 1;
            } else {
                return mDatas.size() + 2;
            }
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {

            private  ConstraintLayout mItemOnclick;
            private  ImageView mIvUserPhoto;
            private  TextView mTvUserName;
            private  TextView mTvReplyContent;
            private  TextView mTvReplyTitle;
            private  TextView mTvReplyTime;
            private  TextView mTvReplyFloor;

            public ItemViewHolder(View itemView) {
                super(itemView);
                if (itemView == mHeaderView) {
                    return;
                }
                if (itemView == mFooterView) {
                    return;
                }
                mItemOnclick = (ConstraintLayout) itemView.findViewById(R.id.cl_item_onclick);
                mIvUserPhoto = (ImageView) itemView.findViewById(R.id.iv_user_photo);
                mTvUserName = (TextView) itemView.findViewById(R.id.tv_user_name);
                mTvReplyFloor = (TextView) itemView.findViewById(R.id.tv_reply_floor);
                mTvReplyTitle = (TextView) itemView.findViewById(R.id.tv_reply_title);
                mTvReplyContent = (TextView) itemView.findViewById(R.id.tv_reply_content);
                mTvReplyTime = (TextView) itemView.findViewById(R.id.tv_reply_time);
            }
        }
    }
}
