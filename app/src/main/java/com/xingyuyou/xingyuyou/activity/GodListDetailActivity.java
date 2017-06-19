package com.xingyuyou.xingyuyou.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.IntentUtils;
import com.xingyuyou.xingyuyou.Utils.KeyboardUtils;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.SoftKeyBoart.EmotionAdapter;
import com.xingyuyou.xingyuyou.Utils.SoftKeyBoart.EmotionKeyboard;
import com.xingyuyou.xingyuyou.Utils.SoftKeyBoart.GlobalOnItemClickManager;
import com.xingyuyou.xingyuyou.Utils.StringUtils;
import com.xingyuyou.xingyuyou.Utils.TimeUtils;
import com.xingyuyou.xingyuyou.Utils.glide.GlideCircleTransform;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.adapter.GodCommoListAdapter;
import com.xingyuyou.xingyuyou.bean.community.PostCommoBean;
import com.xingyuyou.xingyuyou.bean.god.GodActivityDetailBean;
import com.xingyuyou.xingyuyou.bean.god.GodCommoBean;
import com.xingyuyou.xingyuyou.bean.god.GodDetailBean;
import com.xingyuyou.xingyuyou.bean.hotgame.GameDetailBean;
import com.xingyuyou.xingyuyou.bean.hotgame.GameDetailCommoBean;
import com.xingyuyou.xingyuyou.bean.hotgame.GameStartBean;
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

public class GodListDetailActivity extends AppCompatActivity {
    private EmotionKeyboard emotionKeyboard;
    private RadioGroup rgTipPoints;
    private RadioButton rbPoint;
    private static final int emsNumOfEveryFragment = 20;//每页的表情数量
    private FrameLayout extendView, emotionView;
    private LinearLayout contentView;
    private ImageView extendButton, emotionButton;
    private EditText edittext;
    private Button btnSend;
    private static final int REQUEST_IMAGE = 2;
    private static final int TYPE_FOOTER = 21;
    private ArrayList<String> mImageList = new ArrayList();
    private Toolbar mToolbar;
    private TextView mTv_title;
    private TextView mTv_time;
    private TextView mTv_content;
    private ImageView mIv_god_content;
    private List<GodCommoBean> mCommoList = new ArrayList<>();
    private List<GodCommoBean> mCommoAdapterList = new ArrayList<>();
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                String response = (String) msg.obj;
                JSONObject jo = null;
                try {
                    jo = new JSONObject(response);
                    String string = jo.getString("status");
                    if (string.equals("1")) {
                        JSONObject jsonObject = jo.getJSONObject("data");
                        Gson gson = new Gson();
                        mGodDetailBean = gson.fromJson(jsonObject.toString(), GodActivityDetailBean.class);
                        setValues(mGodDetailBean);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (msg.what == 2) {
                String response = (String) msg.obj;
                if (response.contains("\"data\":null")) {
                  //  Toast.makeText(GodListDetailActivity.this, "已经没有更多数据", Toast.LENGTH_SHORT).show();
                    mPbNodata.setVisibility(View.GONE);
                    mTvNodata.setText("已经没有更多数据");
                    return;
                }
                JSONObject jo = null;
                try {
                    jo = new JSONObject(response);
                    JSONArray ja = jo.getJSONArray("data");
                    Gson gson = new Gson();
                    mCommoList = gson.fromJson(ja.toString(),
                            new TypeToken<List<GodCommoBean>>() {
                            }.getType());
                    mCommoAdapterList.addAll(mCommoList);
                    isLoading = false;
                   /* if (mCommoAdapterList.size()<=20){
                        TextView textView = new TextView(GodListDetailActivity.this);
                        textView.setText("没有更多数据");
                        mListView.addFooterView(textView);
                    }*/
                    mGodCommoListAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    private GodCommoListAdapter mGodCommoListAdapter;
    private RecyclerView mRecyclerView;
    private ImageAdapter mImageAdapter;
    private EditText mEditText;
    private LinearLayout mLinearLayout;
    private LinearLayout mLinearLayout2;
    private CustomDialog mDialog;
    private GodActivityDetailBean mGodDetailBean;
    private LinearLayout mLlMoreCommoItem;
    private ListView mListView;
    private int PAGENUM = 1;
    boolean isLoading = false;
    private ProgressBar mPbNodata;
    private TextView mTvNodata;
    private TextView mCollectNum;
    private TextView mJiaonangNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_god_list_detail);
        initCommoData(1);
        initData();
        initToolBar();
        initKeyBoardView();
        initView();
    }
    /**
     * 获取评论内容
     *
     * @param PAGENUM
     */
    private void initCommoData(int PAGENUM) {
        OkHttpUtils.post()//
                .addParams("page", String.valueOf(PAGENUM))
                .addParams("tid", getIntent().getStringExtra("activity_id"))
                .addParams("uid", UserUtils.getUserId())
                .url(XingYuInterface.GET_GOD_FORUMS)
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

    private void initData() {
        OkHttpUtils.post()//
                .addParams("activity_id", getIntent().getStringExtra("activity_id"))
                .addParams("uid", UserUtils.getUserId())
                .url(XingYuInterface.GET_ACTIVITY_INFO)
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

    private void initView() {
        //底部收藏点赞数量
        mCollectNum = (TextView) findViewById(R.id.tv_collect_num);
        mJiaonangNum = (TextView) findViewById(R.id.tv_jiaonang_num);
        //底部发送
        mEditText = (EditText) findViewById(R.id.bottom_edit_text);
        mLinearLayout = (LinearLayout) findViewById(R.id.ll_edit_parent);
        mLinearLayout2 = (LinearLayout) findViewById(R.id.ll_emotion_parent);

        mListView = (ListView) findViewById(R.id.listView);
        View view = View.inflate(GodListDetailActivity.this, R.layout.part_god_activity_header, null);
        mTv_title = (TextView) view.findViewById(R.id.tv_title);
        mTv_time = (TextView) view.findViewById(R.id.tv_time);
        mTv_content = (TextView) view.findViewById(R.id.tv_content);
        mIv_god_content = (ImageView) view.findViewById(R.id.iv_god_content);
        mListView.addHeaderView(view);
        View loadingData = View.inflate(GodListDetailActivity.this, R.layout.default_loading, null);
        mPbNodata = (ProgressBar) loadingData.findViewById(R.id.pb_loading);
        mTvNodata = (TextView) loadingData.findViewById(R.id.loading_text);
        mListView.addFooterView(loadingData);
        mGodCommoListAdapter = new GodCommoListAdapter(GodListDetailActivity.this,mCommoAdapterList);
        mListView.setAdapter(mGodCommoListAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i!=0)
                startActivityToPostReplyCommo(i-1);
            }
        });
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
             if (i+i1==i2){
                 if (!isLoading) {
                     isLoading = true;
                     mHandler.postDelayed(new Runnable() {
                         @Override
                         public void run() {
                             PAGENUM++;
                             initCommoData(PAGENUM);
                         }
                     }, 100);
                 }
             }
            }
        });
    }

    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void startActivityToPostReplyCommo(int position) {
        if (mCommoAdapterList.get(position).getFloor_num()!=null){
            Gson gson = new Gson();
            String json = gson.toJson(mCommoAdapterList.get(position), GodCommoBean.class);
            Intent intent = new Intent(GodListDetailActivity.this, PostReplyCommoActivity.class);
            intent.putExtra("item_list", json);
            GodListDetailActivity.this.startActivity(intent);
        }

    }
    public void setValues(GodActivityDetailBean values) {
        //收藏状态
        if (mGodDetailBean.getCollect_status().equals("1")){
            Drawable drawable= getResources().getDrawable(R.drawable.ic_action_love);
            drawable.setBounds( 0 ,  0 , drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mCollectNum.setCompoundDrawables(null,drawable,null,null);
        }else {
            Drawable drawable= getResources().getDrawable(R.drawable.ic_action_love_empty);
            drawable.setBounds( 0 ,  0 , drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mCollectNum.setCompoundDrawables(null,drawable,null,null);
        }
        if (mGodDetailBean.getLaud_status().equals("1")){
            Drawable drawable= getResources().getDrawable(R.drawable.ic_zan_fill);
            drawable.setBounds( 0 ,  0 , drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mJiaonangNum.setCompoundDrawables(null,drawable,null,null);
        }else {
            Drawable drawable= getResources().getDrawable(R.drawable.ic_zan);
            drawable.setBounds( 0 ,  0 , drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mJiaonangNum.setCompoundDrawables(null,drawable,null,null);
        }
        mCollectNum.setText(mGodDetailBean.getPosts_collect());
        mJiaonangNum.setText(mGodDetailBean.getPosts_laud());

        mCollectNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!UserUtils.logined()){
                    IntentUtils.startActivity(GodListDetailActivity.this, LoginActivity.class);
                    return;
                }
                getCollect(mGodDetailBean.getId());
                if (mGodDetailBean.getCollect_status().equals("1")){
                    mCollectNum.setText(String.valueOf((Integer.parseInt(mGodDetailBean.getPosts_collect())-1)));
                    mGodDetailBean.setPosts_collect(String.valueOf((Integer.parseInt(mGodDetailBean.getPosts_collect())-1)));
                    mGodDetailBean.setCollect_status("0");
                    Toast.makeText(GodListDetailActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
                    Drawable drawable= getResources().getDrawable(R.drawable.ic_action_love_empty);
                    drawable.setBounds( 0 ,  0 , drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    mCollectNum.setCompoundDrawables(null,drawable,null,null);
                }else {
                    mCollectNum.setText(String.valueOf((Integer.parseInt(mGodDetailBean.getPosts_collect())+1)));
                    mGodDetailBean.setPosts_collect(String.valueOf((Integer.parseInt(mGodDetailBean.getPosts_collect())+1)));
                    mGodDetailBean.setCollect_status("1");
                    Toast.makeText(GodListDetailActivity.this, "收藏", Toast.LENGTH_SHORT).show();
                    Drawable drawable= getResources().getDrawable(R.drawable.ic_action_love);
                    drawable.setBounds( 0 ,  0 , drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    mCollectNum.setCompoundDrawables(null,drawable,null,null);
                }

            }
        });

        mJiaonangNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!UserUtils.logined()){
                    IntentUtils.startActivity(GodListDetailActivity.this, LoginActivity.class);
                    return;
                }
                getLaud(mGodDetailBean.getId());
                if (mGodDetailBean.getLaud_status().equals("1")){
                    mJiaonangNum.setText(String.valueOf((Integer.parseInt(mGodDetailBean.getLaud_status())-1)));
                    mGodDetailBean.setLaud_status(String.valueOf((Integer.parseInt(mGodDetailBean.getLaud_status())-1)));
                    mGodDetailBean.setLaud_status("0");
                    Toast.makeText(GodListDetailActivity.this, "取消点赞", Toast.LENGTH_SHORT).show();
                    Drawable drawable= getResources().getDrawable(R.drawable.ic_zan);
                    drawable.setBounds( 0 ,  0 , drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    mJiaonangNum.setCompoundDrawables(null,drawable,null,null);
                }else {
                    mJiaonangNum.setText(String.valueOf((Integer.parseInt(mGodDetailBean.getLaud_status())+1)));
                    mGodDetailBean.setLaud_status(String.valueOf((Integer.parseInt(mGodDetailBean.getLaud_status())+1)));
                    mGodDetailBean.setLaud_status("1");
                    Toast.makeText(GodListDetailActivity.this, "点赞", Toast.LENGTH_SHORT).show();
                    Drawable drawable= getResources().getDrawable(R.drawable.ic_zan_fill);
                    drawable.setBounds( 0 ,  0 , drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    mJiaonangNum.setCompoundDrawables(null,drawable,null,null);

                }

            }
        });

        mTv_title.setText(values.getSubject());
        mTv_content.setText(values.getMessage());
        mTv_time.setText(TimeUtils.getFriendlyTimeSpanByNow(Long.parseLong(values.getDateline() + "000")));
        Glide.with(getApplication())
                .load(values.getPosts_image())
                .into(mIv_god_content);
        mEditText.setFocusable(false);
        mEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!UserUtils.logined()){
                    IntentUtils.startActivity(GodListDetailActivity.this,LoginActivity.class);
                    return;
                }
                mLinearLayout.setVisibility(View.GONE);
                mLinearLayout2.setVisibility(View.VISIBLE);
                //开启软键盘
                KeyboardUtils.showSoftInput(edittext);
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
                    }
                });

    }

    //*****************************************软键盘*****************************************************
    private void initKeyBoardView() {
        contentView = (LinearLayout) findViewById(R.id.txt_main_content);
        extendButton = (ImageView) findViewById(R.id.bt_add_image);
        emotionButton = (ImageView) findViewById(R.id.img_reply_layout_emotion);
        edittext = (EditText) findViewById(R.id.edit_text);
        edittext.addTextChangedListener(new ButtonBtnWatcher());//动态监听EditText
        btnSend = (Button) findViewById(R.id.btn_send);
        extendView = (FrameLayout) findViewById(R.id.extend_layout);
        emotionView = (FrameLayout) findViewById(R.id.emotion_layout);
        //发送
        initButton();
        //绑定软键盘
        bindToEmotionKeyboard();
    }


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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(GodListDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
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
    //*****************************************回复****************************************************
    private void initButton() {
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendReply();
            }
        });
    }

    /**
     * 回帖
     */
    private void sendReply() {
       /* if (StringUtils.isEmpty(edittext.getText().toString().trim())) {
            Toast.makeText(this, "评论内容为空", Toast.LENGTH_SHORT).show();
            return;
        }*/
        //关闭键盘
        KeyboardUtils.hideSoftInput(this);
        mDialog = new CustomDialog(GodListDetailActivity.this, "正在回帖中...");
        mDialog.showDialog();

        Map<String, String> params = new HashMap<String, String>();
        params.put("pid", "0");
        params.put("uid", UserUtils.getUserId());
        params.put("tid", mGodDetailBean.getId());
        params.put("replies_content", StringUtils.isEmpty(edittext.getText().toString().trim())==true?"":edittext.getText().toString().trim());
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
                        mImageList.clear();
                        //待优化
                    }
                });
    }


    private class ImageAdapter extends RecyclerView.Adapter {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View layout = LayoutInflater.from(GodListDetailActivity.this).inflate(R.layout.item_post_commo_image, parent, false);
            return new ImageAdapter.ItemViewHolder(layout);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (getItemViewType(position) == TYPE_FOOTER) {
                ((ImageAdapter.ItemViewHolder) holder).mClosePic.setVisibility(View.GONE);
                ((ImageAdapter.ItemViewHolder) holder).mPostImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mImageList.size() >= 5) {
                            Toast.makeText(GodListDetailActivity.this, "只能发布五张图片", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        MultiImageSelector.create(GodListDetailActivity.this)
                                .showCamera(true)
                                .count(5)
                                .start(GodListDetailActivity.this, REQUEST_IMAGE);
                    }
                });
            }
            if (holder instanceof ImageAdapter.ItemViewHolder) {
                if (mImageList.size() != 0) {
                    if (getItemViewType(position) != TYPE_FOOTER) {
                        Glide.with(GodListDetailActivity.this)
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                mImageList.addAll(path);
                mImageAdapter.notifyDataSetChanged();
            }
        }
    }
    @Override
    public void onBackPressed() {
        if (!emotionKeyboard.interceptBackPress()) {
            super.onBackPressed();
        }
    }
}
