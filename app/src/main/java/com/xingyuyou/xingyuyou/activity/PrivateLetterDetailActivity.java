package com.xingyuyou.xingyuyou.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.SPUtils;
import com.xingyuyou.xingyuyou.Utils.SoftKeyBoart.EmotionAdapter;
import com.xingyuyou.xingyuyou.Utils.SoftKeyBoart.EmotionKeyboard;
import com.xingyuyou.xingyuyou.Utils.SoftKeyBoart.GlobalOnItemClickManager;
import com.xingyuyou.xingyuyou.Utils.SoftKeyBoart.SpanStringUtils;
import com.xingyuyou.xingyuyou.Utils.StringUtils;
import com.xingyuyou.xingyuyou.Utils.TimeUtils;
import com.xingyuyou.xingyuyou.Utils.glide.GlideCircleTransform;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.bean.user.PrivateLetterBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class PrivateLetterDetailActivity extends AppCompatActivity {
    private List<PrivateLetterBean> mLetterList = new ArrayList();
    private List<PrivateLetterBean> mLetterAdapterList = new ArrayList();
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.obj != null) {
                if (msg.what == 1) {
                    String response = (String) msg.obj;

                    JSONObject jo = null;
                    try {
                        jo = new JSONObject(response);
                        String string = jo.getString("status");
                        if (string.equals("1")) {
                            JSONArray ja = jo.getJSONArray("data");
                            Gson gson = new Gson();
                            mLetterList = gson.fromJson(ja.toString(),
                                    new TypeToken<List<PrivateLetterBean>>() {
                                    }.getType());
                            mLetterAdapterList.addAll(mLetterList);
                            mLetterAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    };

    private EmotionKeyboard emotionKeyboard;
    private RadioGroup rgTipPoints;
    private RadioButton rbPoint;
    private static final int emsNumOfEveryFragment = 20;//每页的表情数量
    private FrameLayout extendView, emotionView;
    private LinearLayout contentView;
    private ImageView extendButton, emotionButton;
    private EditText edittext;
    private Button btnSend;
    private Toolbar mToolbar;
    private PrivateLetterAdapter mLetterAdapter;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_letter);
        initView();
        initData();
        initKeyBoardView();
        initToolBar();
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.listView);
        mListView.setDivider(null);
        mLetterAdapter = new PrivateLetterAdapter();
        mListView.setAdapter(mLetterAdapter);

    }

    /**
     * 初始化数据
     */
    public void initData() {
        SPUtils user_data = new SPUtils("user_data");
        OkHttpUtils.post()//
                .addParams("uid", user_data.getString("id"))
                .url(XingYuInterface.PRIVATE_LETTER_LIST)
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
                        Log.e("onResponse", response + ":e");
                    }
                });
    }

    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("私信");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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

    private void sendReply() {
        OkHttpUtils.post()//
                .url(XingYuInterface.PRIVATE_LETTER_ADD)
                .addParams("uid", UserUtils.getUserId())
                .addParams("content", edittext.getText().toString().trim())
                .tag(this)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        PrivateLetterBean privateLetterBean = new PrivateLetterBean(edittext.getText().toString().trim(), UserUtils.getUserPhoto(),"1");
                        mLetterAdapterList.add(privateLetterBean);
                        mLetterAdapter.notifyDataSetChanged();
                        mListView.smoothScrollToPosition(mLetterAdapterList.size());
                    }
                });
    }

    private class PrivateLetterAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mLetterAdapterList.size();
        }

        @Override
        public int getItemViewType(int position) {
            return Integer.parseInt(mLetterAdapterList.get(position).getPid()) == 0 ? 1 : 0;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ZeroViewHolder zeroViewHolder;
            OneViewHolder oneViewHolder;
            switch (getItemViewType(i)) {
                case 0:
                    if (view==null){
                        view = View.inflate(PrivateLetterDetailActivity.this, R.layout.item_private_letter_com_list, null);
                        zeroViewHolder = new ZeroViewHolder();
                        zeroViewHolder.iv_user_photo = (ImageView) view.findViewById(R.id.iv_user_photo);
                        zeroViewHolder.tv_content = (TextView) view.findViewById(R.id.tv_content);
                        view.setTag(zeroViewHolder);
                    }else {
                        zeroViewHolder =(ZeroViewHolder) view.getTag();
                    }
                    zeroViewHolder.tv_content.setText(SpanStringUtils.getEmotionContent(PrivateLetterDetailActivity.this, zeroViewHolder.tv_content, mLetterAdapterList.get(i).getContent()));
                    Glide.with(getApplication())
                            .load(mLetterAdapterList.get(i).getHead_image())
                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                            .transform(new GlideCircleTransform(getApplication()))
                            .into(zeroViewHolder.iv_user_photo);
                    break;
                case 1:
                    if (view==null){
                        view = View.inflate(PrivateLetterDetailActivity.this, R.layout.item_private_letter_send_list, null);
                        oneViewHolder= new OneViewHolder();
                        oneViewHolder.iv_user_photo = (ImageView) view.findViewById(R.id.iv_user_photo);
                        oneViewHolder.tv_content = (TextView) view.findViewById(R.id.tv_content);
                        view.setTag(oneViewHolder);
                    }else {
                        oneViewHolder = (OneViewHolder) view.getTag();
                    }
                    oneViewHolder.tv_content.setText(SpanStringUtils.getEmotionContent(PrivateLetterDetailActivity.this, oneViewHolder.tv_content, mLetterAdapterList.get(i).getContent()));
                    Glide.with(getApplication())
                            .load(mLetterAdapterList.get(i).getHead_image())
                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                            .transform(new GlideCircleTransform(getApplication()))
                            .into(oneViewHolder.iv_user_photo);
                    break;
            }
            return view;
        }
        class ZeroViewHolder {
            TextView tv_content;
            ImageView iv_user_photo;
        }
        class OneViewHolder {
            TextView tv_content;
            ImageView iv_user_photo;
        }

    }
    @Override
    public void onBackPressed() {
        if (!emotionKeyboard.interceptBackPress()) {
            super.onBackPressed();
        }
    }
}
