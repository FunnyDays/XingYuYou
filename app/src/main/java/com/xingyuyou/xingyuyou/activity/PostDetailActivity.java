package com.xingyuyou.xingyuyou.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.xingyuyou.xingyuyou.Utils.TimeUtils;
import com.xingyuyou.xingyuyou.Utils.glide.GlideCircleTransform;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.adapter.CommHotAdapter;
import com.xingyuyou.xingyuyou.base.BaseActivity;
import com.xingyuyou.xingyuyou.bean.community.PostBean;
import com.xingyuyou.xingyuyou.bean.community.PostDetailBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class PostDetailActivity extends BaseActivity {

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
                    //Log.e("post", "解析数据：" + ja.toString());
                    Gson gson = new Gson();
                    mPostDetailBean = gson.fromJson(ja.toString(), PostDetailBean.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setValues();
            }
        }
    };
    private PostDetailBean mPostDetailBean;
    private TextView mTvContent;
    private LinearLayout mRootImage;
    private Toolbar mToolbar;
    private ImageView mIvUserPhoto;
    private TextView mUserName;
    private TextView mPostTime;
    private TextView mCollectNum;
    private TextView mCommNum;
    private TextView mJiaonangNum;
    private EditText mEditText;
    private LinearLayout mLinearLayout;
    private Button mButton;
    private LinearLayout mEditParent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        initToolBar();
        initView();
        initData();
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
        mEditText = (EditText) findViewById(R.id.edit_text);
        mLinearLayout = (LinearLayout) findViewById(R.id.ll_root_bottom);
        mButton = (Button) findViewById(R.id.bt_send_comm);
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
        Glide.with(PostDetailActivity.this)
                .load(mPostDetailBean.getHead_image())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(new GlideCircleTransform(PostDetailActivity.this))
                .dontAnimate()
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
                mLinearLayout.setVisibility(View.GONE);
                mButton.setVisibility(View.VISIBLE);
            }
        });
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PostDetailActivity.this, "发送", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(PostDetailActivity.this, "评论", Toast.LENGTH_SHORT).show();

            }
        });
        mJiaonangNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PostDetailActivity.this, "点赞", Toast.LENGTH_SHORT).show();

            }
        });
    }

}
