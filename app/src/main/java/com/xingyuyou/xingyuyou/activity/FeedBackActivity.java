package com.xingyuyou.xingyuyou.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.SPUtils;
import com.xingyuyou.xingyuyou.Utils.StringUtils;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.bean.user.UserBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

public class FeedBackActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private EditText mEtContactWay;
    private EditText mEtContact;
    private Button mButton;
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
                            Toast.makeText(FeedBackActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        initView();
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("应用反馈");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mEtContactWay = (EditText) findViewById(R.id.et_contact_way);
        mEtContact = (EditText) findViewById(R.id.et_content);
        mButton = (Button) findViewById(R.id.bt_commit);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();
            }
        });
    }

    private void submitData() {
        if (StringUtils.isEmpty(mEtContactWay.getText().toString().trim())) {
            Toast.makeText(this, "请填写上您的联系方式", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isEmpty(mEtContact.getText().toString().trim())) {
            Toast.makeText(this, "请填写上您的建议或意见", Toast.LENGTH_SHORT).show();
            return;
        }
        SPUtils user_data = new SPUtils("user_data");
        OkHttpUtils.post()//
                .url(XingYuInterface.USER_FEEDBACK)
                .addParams("uid", user_data.getString("id"))
                .addParams("contact_method", mEtContactWay.getText().toString().trim())
                .addParams("feedback_info", mEtContact.getText().toString().trim())
                .addParams("errorinfo", "无")
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
}
