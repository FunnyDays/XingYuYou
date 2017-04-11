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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.IntentUtils;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.bean.GameGift;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

public class GetGamePackageActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.obj == null) return;
            if (msg.what==2){
                JSONObject jo = null;
                try {
                    jo = new JSONObject(msg.obj.toString());
                    if (jo.getString("status").equals("1")) {
                        Toast.makeText(GetGamePackageActivity.this, "领取成功，请到个人中心查看详情 ", Toast.LENGTH_SHORT).show();
                    }
                    if (jo.getString("status").equals("2")) {
                        Toast.makeText(GetGamePackageActivity.this, "您已经领取过这个礼包，请到个人中心查看详情 ", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    };
    private TextView mTextView1;
    private TextView mTextView2;
    private TextView mTextView3;
    private TextView mTextView4;
    private TextView mTextView5;
    private ImageView mImageView;
    private Button mButton;
    private RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_game_package);
        initToolBar();
        initView();
        initData();
    }

    private void initView() {
        mTextView1 = (TextView) findViewById(R.id.game_name);
        mTextView2 = (TextView) findViewById(R.id.game_intro);
        mTextView3 = (TextView) findViewById(R.id.game_type);
        mTextView4 = (TextView) findViewById(R.id.tv_gift_number);
        mTextView5 = (TextView) findViewById(R.id.game_size);
        mImageView = (ImageView) findViewById(R.id.game_pic);
        mButton = (Button) findViewById(R.id.bt_uninstall);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.rl_item);

    }

    private void initData() {
        String gameDetail = getIntent().getStringExtra("gameDetail");
        try {
            JSONObject jsonObject = new JSONObject(gameDetail);
            final JSONArray msg = jsonObject.getJSONArray("msg");
            mTextView1.setText( msg.getJSONObject(0).getString("game_name"));
            mTextView2.setText( msg.getJSONObject(0).getString("desribe"));
            mTextView3.setText( msg.getJSONObject(0).getString("giftbag_name"));
            mTextView4.setText( msg.getJSONObject(0).getString("novice"));
            mTextView5.setText( msg.getJSONObject(0).getString("game_size"));
            Glide.with(this).load(msg.getJSONObject(0).getString("icon")).into(mImageView);
            mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (UserUtils.logined()) {
                            getData(msg.getJSONObject(0).getString("gift_id"));
                        } else {
                            IntentUtils.startActivity(GetGamePackageActivity.this, LoginActivity.class);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            mRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Toast.makeText(GetGamePackageActivity.this,msg.getJSONObject(0).getString("desribe"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("领取礼包");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getData(String giftid) {
        RequestParams params = null;
        params = new RequestParams(XingYuInterface.RCEIVE_GIFT);
        params.addParameter("mid", UserUtils.getUserId());
        params.addParameter("giftid", giftid);
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
                handler.obtainMessage(2, json).sendToTarget();
            }

            @Override
            public boolean onCache(String json) {
                return true;
            }
        });
    }

}
