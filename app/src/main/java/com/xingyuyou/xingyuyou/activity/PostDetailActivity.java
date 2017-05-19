package com.xingyuyou.xingyuyou.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.sax.RootElement;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.ConvertUtils;
import com.xingyuyou.xingyuyou.Utils.IntentUtils;
import com.xingyuyou.xingyuyou.Utils.KeyboardUtils;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.SoftKeyBoart.EmotionAdapter;
import com.xingyuyou.xingyuyou.Utils.SoftKeyBoart.EmotionKeyboard;
import com.xingyuyou.xingyuyou.Utils.SoftKeyBoart.GlobalOnItemClickManager;
import com.xingyuyou.xingyuyou.Utils.SoftKeyBoart.SpanStringUtils;
import com.xingyuyou.xingyuyou.Utils.StringUtils;
import com.xingyuyou.xingyuyou.Utils.TimeUtils;
import com.xingyuyou.xingyuyou.Utils.glide.GlideCircleTransform;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.adapter.CommHotAdapter;
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
import java.io.IOException;
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
    private FrameLayout extendView, emotionView;
    private NestedScrollView contentView;
    private ImageView extendButton, emotionButton;
    private EditText edittext;
    private Button btnSend;

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
    private int PAGENUM = 1;
    private static final int REQUEST_IMAGE = 2;
    private static final int TYPE_FOOTER = 21;
    private ArrayList<String> mImageList = new ArrayList();
    private CustomDialog mDialog;
    private RecyclerView mCommoListView;
    private CommoListAdapter mCommoListAdapter;
    private ImageView mAddExpression;
    private EditText mEditTextReal;
    private LinearLayout mSelectEmotion;
    private EmotionKeyboard emotionKeyboard;
    private RadioGroup rgTipPoints;
    private RadioButton rbPoint;
    private static final int emsNumOfEveryFragment = 20;//每页的表情数量
    boolean isLoading = false;
    private LinearLayoutManager mLayoutManager;
    private RelativeLayout mTopView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        initToolBar();
        initKeyBoardView();
        initView();
        initData();
        initCommoData(PAGENUM);
    }

    //********************************************以下是初始化代码**************************************************
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

    private void initKeyBoardView() {
        contentView = (NestedScrollView) findViewById(R.id.txt_main_content);
        extendButton = (ImageView) findViewById(R.id.bt_add_image);
        emotionButton = (ImageView) findViewById(R.id.img_reply_layout_emotion);
        edittext = (EditText) findViewById(R.id.edit_text);
        edittext.addTextChangedListener(new ButtonBtnWatcher());//动态监听EditText
        btnSend = (Button) findViewById(R.id.btn_send);
        extendView = (FrameLayout) findViewById(R.id.extend_layout);
        emotionView = (FrameLayout) findViewById(R.id.emotion_layout);
        //绑定软键盘
        bindToEmotionKeyboard();
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
        mEditText = (EditText) findViewById(R.id.bottom_edit_text);
        // mEditTextReal = (EditText) findViewById(R.id.edit_text_real);
        //底部两个editText视图parent
        mLinearLayout = (LinearLayout) findViewById(R.id.ll_edit_parent);
        mLinearLayout2 = (LinearLayout) findViewById(R.id.ll_emotion_parent);
        // mSelectEmotion = (LinearLayout) findViewById(R.id.ll_select_emotion);
        mRecyclerView = (RecyclerView) findViewById(R.id.rl_all_image);
        initRecyclerView();
        mCommoListView = (RecyclerView) findViewById(R.id.rl_comm_list);
        initCommoListView();
    }

    /**
     * 获取文章内容
     */
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

    /**
     * 获取评论内容
     *
     * @param PAGENUM
     */
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

    //*********************************************以下是软键盘设置代码*****************************************************

    /**
     * 绑定软键盘
     */
    private void bindToEmotionKeyboard() {
        emotionKeyboard = EmotionKeyboard.with(this)
                .setExtendView(extendView)
                .setEmotionView(emotionView)
                .bindToContent(contentView)
                .bindToEditText(edittext)
                .bindToExtendButton(extendButton)
                .bindToEmotionButton(emotionButton)
                .build();
        setUpEmotionViewPager();
        setUpExtendView();
    }

    /**
     * 软键盘文本内容监听
     */
    class ButtonBtnWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!TextUtils.isEmpty(edittext.getText().toString())) { //有文本内容，按钮为可点击状态
                btnSend.setBackgroundResource(R.drawable.shape_button_reply_button_clickable);
                btnSend.setTextColor(getResources().getColor(R.color.light_white));
            } else { // 无文本内容，按钮为不可点击状态
                btnSend.setBackgroundResource(R.drawable.shape_button_reply_button_unclickable);
                btnSend.setTextColor(getResources().getColor(R.color.reply_button_text_disable));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }


    /**
     * 设置表情布局下的视图
     */
    private void setUpEmotionViewPager() {
        int fragmentNum;
        /*获取ems文件夹有多少个表情  减1 是因为有个删除键
                         每页20个表情  总共有length个表情
                         先判断能不能整除  判断是否有不满一页的表情
		 */
        int emsTotalNum = getSizeOfAssetsCertainFolder("ems") - 1;//表情的数量(除去删除按钮)
        if (emsTotalNum % emsNumOfEveryFragment == 0) {
            fragmentNum = emsTotalNum / emsNumOfEveryFragment;
        } else {
            fragmentNum = (emsTotalNum / emsNumOfEveryFragment) + 1;
        }
        EmotionAdapter mViewPagerAdapter = new EmotionAdapter(getSupportFragmentManager(), fragmentNum);
        ViewPager mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setCurrentItem(0);

        GlobalOnItemClickManager globalOnItemClickListener = GlobalOnItemClickManager.getInstance();
        globalOnItemClickListener.attachToEditText((EditText) findViewById(R.id.edit_text));

		/* 设置表情下的提示点 */
        setUpTipPoints(fragmentNum, mViewPager);
    }

    /**
     * 获取assets下某个指定文件夹下的文件数量
     */
    private int getSizeOfAssetsCertainFolder(String folderName) {
        int size = 0;
        try {
            size = getAssets().list(folderName).length;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 设置扩展布局下的视图
     */
    private void setUpExtendView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rl_all_image);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(PostDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        mImageAdapter = new ImageAdapter();
        mRecyclerView.setAdapter(mImageAdapter);
    }

    /**
     * @param num 提示点的数量
     */
    private void setUpTipPoints(int num, ViewPager mViewPager) {
        rgTipPoints = (RadioGroup) findViewById(R.id.rg_reply_layout);
        for (int i = 0; i < num; i++) {
            rbPoint = new RadioButton(this);
            RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(30, 30);
            lp.setMargins(10, 0, 10, 0);
            rbPoint.setLayoutParams(lp);
            rbPoint.setId(i);//为每个RadioButton设置标记
            rbPoint.setButtonDrawable(getResources().getDrawable(R.color.transparent));//设置button为@null
            rbPoint.setBackgroundResource(R.drawable.emotion_tip_points_selector);
            rbPoint.setClickable(false);
            if (i == 0) { // 第一个点默认为选中，与其他点显示颜色不同
                rbPoint.setChecked(true);
            } else {
                rbPoint.setChecked(false);
            }
            rgTipPoints.addView(rbPoint);
        }
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                rgTipPoints.check(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    //***********************************************以下是评论列表****************************************************
    private void initCommoListView() {
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mCommoListView.setLayoutManager(mLayoutManager);
        mCommoListView.setNestedScrollingEnabled(false);
        mCommoListAdapter = new CommoListAdapter();
        mCommoListView.setAdapter(mCommoListAdapter);
        contentView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (v.getMeasuredHeight() > (v.getChildAt(0).getMeasuredHeight() - scrollY) / 2) {
                    if (!isLoading) {
                        isLoading = true;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                PAGENUM++;
                                initCommoData(PAGENUM);
                                isLoading = false;
                            }
                        }, 200);
                    }
                }
            }
        });
    }

    private class CommoListAdapter extends RecyclerView.Adapter {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View layout = LayoutInflater.from(PostDetailActivity.this).inflate(R.layout.item_commo_post_list, parent, false);
            return new ItemViewHolder(layout);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (holder instanceof CommoListAdapter.ItemViewHolder) {
                if (mCommoAdapterList.get(position).getImgarr()!=null&&mCommoAdapterList.get(position).getImgarr().size() != 0&&!mCommoAdapterList.get(position).getImgarr().get(0).equals("")) {
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
                        .transform(new GlideCircleTransform(PostDetailActivity.this))
                        .into(((CommoListAdapter.ItemViewHolder) holder).mUserPhoto);
                ((CommoListAdapter.ItemViewHolder) holder).mUserName
                        .setText(mCommoAdapterList.get(position).getNickname());
                ((CommoListAdapter.ItemViewHolder) holder).mPostTime
                        .setText(TimeUtils.getFriendlyTimeSpanByNow(Long.parseLong(mCommoAdapterList.get(position).getDateline() + "000")));
                if (mCommoAdapterList.get(position).getFloor_num()!=null)
                ((CommoListAdapter.ItemViewHolder) holder).mFloorNum
                        .setText(mCommoAdapterList.get(position).getFloor_num() + "楼");
                if (mCommoAdapterList.get(position).getFloor_num()!=null)
                ((CommoListAdapter.ItemViewHolder) holder).mLoveNum
                        .setText(mCommoAdapterList.get(position).getLaud_count());
                ((CommoListAdapter.ItemViewHolder) holder).mCommoContent
                        .setText(SpanStringUtils.getEmotionContent(getApplication(),((CommoListAdapter.ItemViewHolder) holder).mCommoContent,mCommoAdapterList.get(position).getReplies_content()));
                if (mCommoAdapterList.get(position).getChild() != null && mCommoAdapterList.get(position).getChild().size() > 0) {
                    ((CommoListAdapter.ItemViewHolder) holder).mLlMoreCommoItem.setVisibility(View.VISIBLE);
                    ((CommoListAdapter.ItemViewHolder) holder).mCommo2Name
                            .setText(mCommoAdapterList.get(position).getChild().get(0).getNickname() + ":");
                    ((CommoListAdapter.ItemViewHolder) holder).mCommo2Content
                            .setText(SpanStringUtils.getEmotionContent(getApplication(),((CommoListAdapter.ItemViewHolder) holder).mCommo2Content,mCommoAdapterList.get(position).getChild().get(0).getReplies_content()));
                    ((CommoListAdapter.ItemViewHolder) holder).mCommo2Time
                            .setText(TimeUtils.getFriendlyTimeSpanByNow(Long.parseLong(mCommoAdapterList.get(position).getChild().get(0).getDateline() + "000")));
                }
                ((CommoListAdapter.ItemViewHolder) holder).mLlMoreCommoItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                            startActivityToPostReplyCommo(position);
                    }
                });
                ((CommoListAdapter.ItemViewHolder) holder).mLlCommoItemDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                            startActivityToPostReplyCommo(position);
                    }
                });

            }
        }

        private void startActivityToPostReplyCommo(int position) {
            if (mCommoAdapterList.get(position).getFloor_num()!=null){
                Gson gson = new Gson();
                String json = gson.toJson(mCommoAdapterList.get(position), PostCommoBean.class);
                Intent intent = new Intent(PostDetailActivity.this, PostReplyCommoActivity.class);
                intent.putExtra("item_list", json);
                PostDetailActivity.this.startActivity(intent);
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
            private LinearLayout mLlCommoItemDetail;
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
                mLlCommoItemDetail = (LinearLayout) itemView.findViewById(R.id.item_commo_detail);
                mLlMoreCommoItem = (LinearLayout) itemView.findViewById(R.id.ll_more_commo_item);
                mLlRootImageItem = (LinearLayout) itemView.findViewById(R.id.ll_root_image_item);
            }
        }
    }

    //*********************************************以下是赋值代码***************************************************
    private void setValues() {
        //收藏状态
        if (mPostDetailBean.getCollect_status().equals("1")){
            Drawable drawable= getResources().getDrawable(R.mipmap.ic_collect_fill);
            drawable.setBounds( 0 ,  0 , drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mCollectNum.setCompoundDrawables(null,drawable,null,null);
        }else {
            Drawable drawable= getResources().getDrawable(R.mipmap.shoucang);
            drawable.setBounds( 0 ,  0 , drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mCollectNum.setCompoundDrawables(null,drawable,null,null);
        }
        if (mPostDetailBean.getLaud_status().equals("1")){
            Drawable drawable= getResources().getDrawable(R.mipmap.ic_zan_fill);
            drawable.setBounds( 0 ,  0 , drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mJiaonangNum.setCompoundDrawables(null,drawable,null,null);
        }else {
            Drawable drawable= getResources().getDrawable(R.mipmap.ic_zan);
            drawable.setBounds( 0 ,  0 , drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mJiaonangNum.setCompoundDrawables(null,drawable,null,null);
        }
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

        mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!UserUtils.logined()){
                    IntentUtils.startActivity(PostDetailActivity.this,LoginActivity.class);
                    return;
                }
                // Toast.makeText(PostDetailActivity.this, "gaaaaa", Toast.LENGTH_SHORT).show();
                mLinearLayout.setVisibility(View.GONE);
                mLinearLayout2.setVisibility(View.VISIBLE);
                //开启软键盘
                KeyboardUtils.showSoftInput(edittext);
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendReply();
            }
        });
        mCollectNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!UserUtils.logined()){
                    IntentUtils.startActivity(PostDetailActivity.this, LoginActivity.class);
                    return;
                }
                getCollect(mPostDetailBean.getId());
                if (mPostDetailBean.getCollect_status().equals("1")){
                    mCollectNum.setText(String.valueOf((Integer.parseInt(mPostDetailBean.getPosts_collect())-1)));
                    mPostDetailBean.setPosts_collect(String.valueOf((Integer.parseInt(mPostDetailBean.getPosts_collect())-1)));
                    mPostDetailBean.setCollect_status("0");
                    Toast.makeText(PostDetailActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
                    Drawable drawable= getResources().getDrawable(R.mipmap.shoucang);
                    drawable.setBounds( 0 ,  0 , drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    mCollectNum.setCompoundDrawables(null,drawable,null,null);
                }else {
                    mCollectNum.setText(String.valueOf((Integer.parseInt(mPostDetailBean.getPosts_collect())+1)));
                    mPostDetailBean.setPosts_collect(String.valueOf((Integer.parseInt(mPostDetailBean.getPosts_collect())+1)));
                    mPostDetailBean.setCollect_status("1");
                    Toast.makeText(PostDetailActivity.this, "收藏", Toast.LENGTH_SHORT).show();
                    Drawable drawable= getResources().getDrawable(R.mipmap.ic_collect_fill);
                    drawable.setBounds( 0 ,  0 , drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    mCollectNum.setCompoundDrawables(null,drawable,null,null);

                }

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
                if (!UserUtils.logined()){
                    IntentUtils.startActivity(PostDetailActivity.this, LoginActivity.class);
                    return;
                }
                getLaud(mPostDetailBean.getId());
                if (mPostDetailBean.getLaud_status().equals("1")){
                    mJiaonangNum.setText(String.valueOf((Integer.parseInt(mPostDetailBean.getLaud_status())-1)));
                    mPostDetailBean.setLaud_status(String.valueOf((Integer.parseInt(mPostDetailBean.getLaud_status())-1)));
                    mPostDetailBean.setLaud_status("0");
                    Toast.makeText(PostDetailActivity.this, "取消点赞", Toast.LENGTH_SHORT).show();
                    Drawable drawable= getResources().getDrawable(R.mipmap.ic_zan);
                    drawable.setBounds( 0 ,  0 , drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    mJiaonangNum.setCompoundDrawables(null,drawable,null,null);
                }else {
                    mJiaonangNum.setText(String.valueOf((Integer.parseInt(mPostDetailBean.getLaud_status())+1)));
                    mPostDetailBean.setLaud_status(String.valueOf((Integer.parseInt(mPostDetailBean.getLaud_status())+1)));
                    mPostDetailBean.setLaud_status("1");
                    Toast.makeText(PostDetailActivity.this, "点赞", Toast.LENGTH_SHORT).show();
                    Drawable drawable= getResources().getDrawable(R.mipmap.ic_zan_fill);
                    drawable.setBounds( 0 ,  0 , drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    mJiaonangNum.setCompoundDrawables(null,drawable,null,null);

                }

            }
        });

    }

    /**
     * 点赞
     * @param tid
     */
    public void getLaud( String tid) {
        OkHttpUtils.post()//
                .addParams("tid",tid)
                .addParams("uid",UserUtils.getUserId())
                .url(XingYuInterface.GET_LAUD)
                .tag(this)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("hot", e.toString() + ":e");
                    }
                    @Override
                    public void onResponse(String response, int id) {
                    }
                });

    }
    /**
     * 收藏
     * @param tid
     */
    public void getCollect(final String tid) {
        OkHttpUtils.post()//
                .addParams("tid",tid)
                .addParams("uid",UserUtils.getUserId())
                .url(XingYuInterface.GET_COLLECT)
                .tag(this)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        // Log.e("hot", e.toString() + ":e");
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("weiwei","hahah"+UserUtils.getUserId()+tid+response);
                    }
                });

    }
    /**
     * 回帖
     */
    private void sendReply() {
        if (StringUtils.isEmpty(edittext.getText().toString().trim())) {
            Toast.makeText(this, "评论内容为空", Toast.LENGTH_SHORT).show();
            return;
        }
        //关闭键盘
        KeyboardUtils.hideSoftInput(this);
        mDialog = new CustomDialog(PostDetailActivity.this, "正在回帖中...");
        mDialog.showDialog();

        Map<String, String> params = new HashMap<String, String>();
        params.put("pid", "0");
        params.put("uid", UserUtils.getUserId());
        params.put("tid", mPostDetailBean.getId());
        params.put("replies_content", edittext.getText().toString().trim());
        //隐藏键盘
        emotionKeyboard.interceptBackPress();
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
                        PostCommoBean postCommoBean = new PostCommoBean();
                        postCommoBean.setNickname(UserUtils.getNickName());
                        postCommoBean.setHead_image(UserUtils.getUserPhoto());
                        postCommoBean.setDateline(String.valueOf(TimeUtils.getNowTimeMills()).substring(0, String.valueOf(TimeUtils.getNowTimeMills()).length() - 3));
                        postCommoBean.setReplies_content( edittext.getText().toString().trim());
                        mCommoAdapterList.add(postCommoBean);
                        mCommoListAdapter.notifyDataSetChanged();
                        mImageList.clear();
                        //待优化
                    }
                });
    }

    //**********************************************以下是回复图片设置代码****************************************************
    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(PostDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        mImageAdapter = new ImageAdapter();
        mRecyclerView.setAdapter(mImageAdapter);
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

    @Override
    public void onBackPressed() {
        if (!emotionKeyboard.interceptBackPress()) {
            super.onBackPressed();
        }
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


}
