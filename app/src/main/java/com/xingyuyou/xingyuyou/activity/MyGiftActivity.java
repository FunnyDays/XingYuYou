package com.xingyuyou.xingyuyou.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.IntentUtils;
import com.xingyuyou.xingyuyou.Utils.MCUtils.HttpUtils;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.SPUtils;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.HashMap;

import okhttp3.Call;

public class MyGiftActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private String mUserId;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==1){
                Log.e("gift", "-------------" + msg.obj.toString());
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_gift);
        initData();
        initToolBar();
    }

    private void initData() {
        if (!UserUtils.logined())
            return;
        SPUtils user_data = new SPUtils("user_data");
        mUserId = user_data.getString("id");
        TestXutils();
    }

    private void TestXutils() {
        Log.e("gift", "eee");
        RequestParams params = new RequestParams(XingYuInterface.MY_GIFT);
        params.addParameter("user_id", mUserId);
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
                mHandler.obtainMessage(1,json).sendToTarget();
            }
            @Override
            public boolean onCache(String json) {
                return true;
            }
        });
    }

    private void TestPost() {
        OkHttpUtils.post()//
                .url(XingYuInterface.MY_GIFT)
                .addParams("user_id", mUserId)
                .tag(this)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("gift", e.toString() + ":e");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("gift", response + "");

                    }
                });
    }

    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("我的礼包");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


}
