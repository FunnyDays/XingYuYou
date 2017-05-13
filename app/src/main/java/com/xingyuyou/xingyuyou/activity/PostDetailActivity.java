package com.xingyuyou.xingyuyou.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.ConvertUtils;
import com.xingyuyou.xingyuyou.Utils.KeyboardUtils;
import com.xingyuyou.xingyuyou.Utils.StringUtils;
import com.xingyuyou.xingyuyou.Utils.TimeUtils;
import com.xingyuyou.xingyuyou.Utils.glide.GlideCircleTransform;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.base.BaseActivity;
import com.xingyuyou.xingyuyou.bean.community.PostCommoBean;
import com.xingyuyou.xingyuyou.bean.community.PostDetailBean;
import com.xingyuyou.xingyuyou.bean.community.PostListBean;
import com.xingyuyou.xingyuyou.bean.community.TopViewRecommBean;
import com.xingyuyou.xingyuyou.weight.dialog.CustomDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import net.bither.util.NativeUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import okhttp3.Call;

public class PostDetailActivity extends BaseActivity {


    private List<PostCommoBean> mCommoList = new ArrayList<>();
    private List<PostCommoBean> mCommoAdapterList = new ArrayList<>();

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                String response = (String) msg.obj;
                JSONObject jo = null;
                try {
                    jo = new JSONObject(response);
                    JSONObject ja = jo.getJSONObject("data");
                    Gson gson = new Gson();
                    mPostDetailBean = gson.fromJson(ja.toString(), PostDetailBean.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setValues();
            }
            if (msg.what == 2) {
                String response = (String) msg.obj;
                JSONObject jo = null;
                try {
                    jo = new JSONObject(response);
                    JSONArray ja = jo.getJSONArray("data");
                    Gson gson = new Gson();
                    mCommoList = gson.fromJson(ja.toString(),
                            new TypeToken<List<PostCommoBean>>() {
                            }.getType());
                    mCommoAdapterList.addAll(mCommoList);
                    mCommoListAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    };
    private PostDetailBean mPostDetailBean;
    private TextView mTvContent;
    private LinearLayout mRootImage;
    private Toolbar mToolbar;
    private ImageView mIvUserPhoto;
    private ImageView mAddImage;
    private TextView mUserName;
    private TextView mPostTime;
    private TextView mCollectNum;
    private TextView mCommNum;
    private TextView mJiaonangNum;
    private EditText mEditText;
    private LinearLayout mLinearLayout;
    private LinearLayout mLinearLayout2;
    private Button mButton;
    private LinearLayout mEditParent;
    private RecyclerView mRecyclerView;
    private ImageAdapter mImageAdapter;
    private static final int PAGENUM = 1;
    private static final int REQUEST_IMAGE = 2;
    private static final int TYPE_FOOTER = 21;
    private ArrayList<String> mImageList = new ArrayList();
    private CustomDialog mDialog;
    private RecyclerView mCommoListView;
    private CommoListAdapter mCommoListAdapter;
    private ImageView mAddExpression;
    private EditText mEditTextReal;
    private LinearLayout mSelectEmotion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        initToolBar();
        initView();
        initData();
        initCommoData(PAGENUM);
    }


    private void initCommoData(int PAGENUM) {
        OkHttpUtils.post()//
                .addParams("page", String.valueOf(PAGENUM))
                .addParams("tid", getIntent().getStringExtra("post_id"))
                .url(XingYuInterface.GET_FORUMS_LIST)
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

    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.inflateMenu(R.menu.post_share_menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ab_share:
                        Toast.makeText(PostDetailActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {
        mTvContent = (TextView) findViewById(R.id.tv_content);
        mUserName = (TextView) findViewById(R.id.tv_user_name);
        mPostTime = (TextView) findViewById(R.id.tv_post_time);
        mCollectNum = (TextView) findViewById(R.id.tv_collect_num);
        mCommNum = (TextView) findViewById(R.id.tv_comm_num);
        mJiaonangNum = (TextView) findViewById(R.id.tv_jiaonang_num);
        mRootImage = (LinearLayout) findViewById(R.id.ll_root_image);
        mIvUserPhoto = (ImageView) findViewById(R.id.civ_user_photo);
        mAddImage = (ImageView) findViewById(R.id.bt_add_image);
        mAddExpression = (ImageView) findViewById(R.id.bt_add_expression);
        mEditText = (EditText) findViewById(R.id.edit_text);
        mEditTextReal = (EditText) findViewById(R.id.edit_text_real);
        mLinearLayout = (LinearLayout) findViewById(R.id.ll_edit_parent);
        mLinearLayout2 = (LinearLayout) findViewById(R.id.ll_send_parent);
        mSelectEmotion = (LinearLayout) findViewById(R.id.ll_select_emotion);
        mButton = (Button) findViewById(R.id.bt_send_comm);
        mRecyclerView = (RecyclerView) findViewById(R.id.rl_all_image);
        initRecyclerView();
        mCommoListView = (RecyclerView) findViewById(R.id.rl_comm_list);
        initCommoListView();
    }

    private void initCommoListView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        linearLayoutManager.setAutoMeasureEnabled(true);
        mCommoListView.setLayoutManager(linearLayoutManager);
        mCommoListView.setHasFixedSize(true);
        mCommoListView.setNestedScrollingEnabled(false);
        mCommoListAdapter = new CommoListAdapter();
        mCommoListView.setAdapter(mCommoListAdapter);
    }

    @Override
    public void initData() {
        OkHttpUtils.post()//
                .addParams("pid", getIntent().getStringExtra("post_id"))
                .url(XingYuInterface.GET_POSTS_INFO)
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

    private void setValues() {
        Glide.with(getApplication())
                .load(mPostDetailBean.getHead_image())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(new GlideCircleTransform(PostDetailActivity.this))
                .into(mIvUserPhoto);
        mToolbar.setTitle(mPostDetailBean.getSubject());
        mUserName.setText(mPostDetailBean.getNickname());
        mPostTime.setText(TimeUtils.getFriendlyTimeSpanByNow(Long.parseLong(mPostDetailBean.getDateline() + "000")));
        mTvContent.setText(mPostDetailBean.getMessage());
        mCollectNum.setText(mPostDetailBean.getPosts_collect());
        mCommNum.setText(mPostDetailBean.getPosts_forums());
        mJiaonangNum.setText(mPostDetailBean.getPosts_laud());
        for (int i = 0; i < mPostDetailBean.getPosts_image().size(); i++) {
            ImageView imageView = new ImageView(PostDetailActivity.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(ConvertUtils.dp2px(10), ConvertUtils.dp2px(10), ConvertUtils.dp2px(10), ConvertUtils.dp2px(10));
            imageView.setLayoutParams(lp);
            imageView.setAdjustViewBounds(true);
            Glide.with(PostDetailActivity.this)
                    .load(mPostDetailBean.getPosts_image().get(i))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
            mRootImage.addView(imageView);
        }
        mEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(PostDetailActivity.this, "gagagaga", Toast.LENGTH_SHORT).show();
            }
        });
        mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
               // Toast.makeText(PostDetailActivity.this, "gaaaaa", Toast.LENGTH_SHORT).show();
                mLinearLayout.setVisibility(View.GONE);
                mLinearLayout2.setVisibility(View.VISIBLE);
                //开启软键盘
                KeyboardUtils.showSoftInput(mEditTextReal);
            }
        });
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StringUtils.isEmpty(mEditTextReal.getText().toString().trim())) {
                    Toast.makeText(PostDetailActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
                    return;
                }
                sendReply();
                int visibility = mRecyclerView.getVisibility();
                switch (visibility) {
                    case View.GONE:
                        break;
                    case View.VISIBLE:
                        mImageList.clear();
                        mRecyclerView.setVisibility(View.GONE);
                        break;
                }
            }
        });
        mCollectNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PostDetailActivity.this, "收藏", Toast.LENGTH_SHORT).show();

            }
        });
        mCommNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Toast.makeText(PostDetailActivity.this, "评论", Toast.LENGTH_SHORT).show();

            }
        });
        mJiaonangNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PostDetailActivity.this, "点赞", Toast.LENGTH_SHORT).show();

            }
        });
        mAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int visibility = mRecyclerView.getVisibility();
                switch (visibility) {
                    case View.GONE:
                        mRecyclerView.setVisibility(View.VISIBLE);
                        break;
                    case View.VISIBLE:
                        mRecyclerView.setVisibility(View.GONE);
                        mImageList.clear();
                        break;
                }
            }
        });
       /* mAddExpression.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int visibility = mSelectEmotion.getVisibility();
                switch (visibility) {
                    case View.GONE:
                        mSelectEmotion.setVisibility(View.VISIBLE);
                        KeyboardUtils.hideSoftInput(PostDetailActivity.this);
                        break;
                    case View.VISIBLE:
                        mSelectEmotion.setVisibility(View.GONE);
                        KeyboardUtils.showSoftInput(mEditTextReal);
                        //mImageList.clear();
                        break;
                }

            }
        });*/
    }

    /**
     * 回帖
     */
    private void sendReply() {
        mDialog = new CustomDialog(PostDetailActivity.this, "正在回帖中...");
        mDialog.showDialog();

        Map<String, String> params = new HashMap<String, String>();
        params.put("pid", "0");
        params.put("uid", "105");
        params.put("tid", mPostDetailBean.getId());
        params.put("replies_content", mEditTextReal.getText().toString().trim());

        PostFormBuilder post = OkHttpUtils.post();
        for (int i = 0; i < mImageList.size(); i++) {
            File file = new File(mImageList.get(i));
            if (file.exists()) {
                File file1 = new File(getExternalCacheDir() + "tempCompress" + i + ".jpg");
                NativeUtil.compressBitmap(mImageList.get(i), file1.getAbsolutePath());
                String s = "replies_image";
                post.addFile(s + i, file.getName(), file1);
            }

        }
        post.url(XingYuInterface.REPLIES)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        mDialog.dismissDialog();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mDialog.dismissDialog();
                        initCommoData(PAGENUM);
                    }
                });
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(PostDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        mImageAdapter = new ImageAdapter();
        mRecyclerView.setAdapter(mImageAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                mImageList.addAll(path);
                mImageAdapter.notifyDataSetChanged();
            }
        }
    }

    private class ImageAdapter extends RecyclerView.Adapter {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View layout = LayoutInflater.from(PostDetailActivity.this).inflate(R.layout.item_post_commo_image, parent, false);
            return new ItemViewHolder(layout);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (getItemViewType(position) == TYPE_FOOTER) {
                ((ImageAdapter.ItemViewHolder) holder).mClosePic.setVisibility(View.GONE);
                ((ImageAdapter.ItemViewHolder) holder).mPostImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mImageList.size() >= 5) {
                            Toast.makeText(PostDetailActivity.this, "只能发布五张图片", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        MultiImageSelector.create(PostDetailActivity.this)
                                .showCamera(true)
                                .single()
                                .start(PostDetailActivity.this, REQUEST_IMAGE);
                    }
                });
            }
            if (holder instanceof ImageAdapter.ItemViewHolder) {
                if (mImageList.size() != 0) {
                    if (getItemViewType(position) != TYPE_FOOTER) {
                        Glide.with(PostDetailActivity.this)
                                .load(mImageList.get(position))
                                .into(((ImageAdapter.ItemViewHolder) holder).mPostImage);
                        ((ImageAdapter.ItemViewHolder) holder).mClosePic.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mImageList.remove(position);
                                notifyDataSetChanged();
                            }
                        });
                    }
                }
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == getItemCount() - 1) {
                //最后一个,应该加载Footer
                return TYPE_FOOTER;
            }
            return super.getItemViewType(position);
        }

        @Override
        public int getItemCount() {
            return mImageList.size() == 0 ? 1 : mImageList.size() + 1;
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {

            private ImageView mClosePic;
            private ImageView mPostImage;

            public ItemViewHolder(View itemView) {
                super(itemView);
                mClosePic = (ImageView) itemView.findViewById(R.id.iv_close);
                mPostImage = (ImageView) itemView.findViewById(R.id.iv_post_image);
            }
        }
    }

    private class CommoListAdapter extends RecyclerView.Adapter {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View layout = LayoutInflater.from(PostDetailActivity.this).inflate(R.layout.item_commo_post_list, parent, false);
            return new ItemViewHolder(layout);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof CommoListAdapter.ItemViewHolder) {
                if (mCommoAdapterList.get(position).getImgarr().size()!=0){
                    for (int i = 0; i < mCommoAdapterList.get(position).getImgarr().size(); i++) {
                        ImageView imageView = new ImageView(PostDetailActivity.this);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        lp.setMargins(ConvertUtils.dp2px(10), 0, ConvertUtils.dp2px(10), ConvertUtils.dp2px(5));
                        imageView.setLayoutParams(lp);
                        imageView.setAdjustViewBounds(true);
                        Glide.with(PostDetailActivity.this)
                                .load(mCommoAdapterList.get(position).getImgarr().get(i))
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(imageView);
                        ((CommoListAdapter.ItemViewHolder) holder).mLlRootImageItem.addView(imageView);
                    }
                }
                Glide.with(PostDetailActivity.this)
                        .load(mCommoAdapterList.get(position).getHead_image())
                        .into(((CommoListAdapter.ItemViewHolder) holder).mUserPhoto);
                ((CommoListAdapter.ItemViewHolder) holder).mUserName
                        .setText(mCommoAdapterList.get(position).getNickname());
                ((CommoListAdapter.ItemViewHolder) holder).mPostTime
                        .setText(TimeUtils.getFriendlyTimeSpanByNow(Long.parseLong(mCommoAdapterList.get(position).getDateline() + "000")));
                ((CommoListAdapter.ItemViewHolder) holder).mFloorNum
                        .setText(mCommoAdapterList.get(position).getFloor_num()+"楼");
                ((CommoListAdapter.ItemViewHolder) holder).mLoveNum
                        .setText(mCommoAdapterList.get(position).getLaud_count());
                ((CommoListAdapter.ItemViewHolder) holder).mCommoContent
                        .setText(mCommoAdapterList.get(position).getReplies_content());
                if (mCommoAdapterList.get(position).getChild()!=null&&mCommoAdapterList.get(position).getChild().size() > 0) {
                    ((CommoListAdapter.ItemViewHolder) holder).mLlMoreCommoItem.setVisibility(View.VISIBLE);
                    ((CommoListAdapter.ItemViewHolder) holder).mCommo2Name
                            .setText(mCommoAdapterList.get(position).getChild().get(0).getNickname()+":");
                    ((CommoListAdapter.ItemViewHolder) holder).mCommo2Content
                            .setText(mCommoAdapterList.get(position).getChild().get(0).getReplies_content());
                    ((CommoListAdapter.ItemViewHolder) holder).mCommo2Time
                            .setText(TimeUtils.getFriendlyTimeSpanByNow(Long.parseLong(mCommoAdapterList.get(position).getChild().get(0).getDateline() + "000")));
                }
                ((CommoListAdapter.ItemViewHolder) holder).mLlMoreCommoItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(PostDetailActivity.this, "新评论界面", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return mCommoAdapterList.size();
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {

            private ImageView mUserPhoto;
            private ImageView mLove;
            private TextView mUserName;
            private TextView mPostTime;
            private TextView mFloorNum;
            private TextView mLoveNum;
            private TextView mCommoContent;
            private TextView mCommo2Name;
            private TextView mCommo2Content;
            private TextView mCommo2Time;
            private LinearLayout mLlMoreCommoItem;
            private LinearLayout mLlRootImageItem;


            public ItemViewHolder(View itemView) {
                super(itemView);
                mUserPhoto = (ImageView) itemView.findViewById(R.id.iv_user_photo);
                mLove = (ImageView) itemView.findViewById(R.id.iv_love);
                mUserName = (TextView) itemView.findViewById(R.id.tv_user_name);
                mPostTime = (TextView) itemView.findViewById(R.id.tv_post_time);
                mFloorNum = (TextView) itemView.findViewById(R.id.tv_floor_num);
                mLoveNum = (TextView) itemView.findViewById(R.id.tv_love_num);
                mCommoContent = (TextView) itemView.findViewById(R.id.tv_commo_content);
                mCommo2Name = (TextView) itemView.findViewById(R.id.tv_commo2_name);
                mCommo2Content = (TextView) itemView.findViewById(R.id.tv_commo2_content);
                mCommo2Time = (TextView) itemView.findViewById(R.id.tv_commo2_time);
                mLlMoreCommoItem = (LinearLayout) itemView.findViewById(R.id.ll_more_commo_item);
                mLlRootImageItem = (LinearLayout) itemView.findViewById(R.id.ll_root_image_item);
            }
        }
    }
}
