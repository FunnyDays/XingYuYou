package com.xingyuyou.xingyuyou.activity;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.RegexUtils;
import com.xingyuyou.xingyuyou.Utils.StringUtils;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;


public class ChangePasswordActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private EditText mPhoneNumber;
    private Button mSendCode;
    private HttpUtils mHttp;
    private Button mBtRegister;
    private EditText mPhoneCode;
    private EditText mPassword;
    private ValueAnimator mValueAnimator;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {

            }
            if (msg.what == 1) {
                String result = (String) msg.obj;
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    String return_msg = jsonObject.getString("msg");
                    if (jsonObject.getString("return_code").equals("success")) {
                        finish();
                    }
                    Toast.makeText(ChangePasswordActivity.this, return_msg , Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    private EditText mEt_password_again;
    private EditText mEt_old_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initToolBar();
        initView();
    }

    private void initView() {

        mEt_old_password = (EditText) findViewById(R.id.et_old_password);

        mPassword = (EditText) findViewById(R.id.et_password);
        mEt_password_again = (EditText) findViewById(R.id.et_password_again);
        mBtRegister = (Button) findViewById(R.id.bt_register_button);
        mBtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toRegister(1);
            }
        });
    }

    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("注册");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }



    private void toRegister(int code) {
        String old_password = mEt_old_password.getText().toString().trim();
        if (StringUtils.isEmpty(old_password)) {
            Toast.makeText(this, "请输入原密码", Toast.LENGTH_SHORT).show();
            return;
        }
        String passwordText = mPassword.getText().toString().trim();
        if (StringUtils.isEmpty(passwordText)) {
            Toast.makeText(this, "请输入新密码", Toast.LENGTH_SHORT).show();
            return;
        }
        String password_again = mEt_password_again.getText().toString().trim();
        if (StringUtils.isEmpty(passwordText)) {
            Toast.makeText(this, "请再次输入新密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!passwordText.equals(password_again)) {
            Toast.makeText(this, "两次密码不一致"+passwordText+"---"+password_again, Toast.LENGTH_SHORT).show();
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", UserUtils.getUserId());
            jsonObject.put("code", "pwd");
            jsonObject.put("old_password", String.valueOf(old_password));
            jsonObject.put("password", String.valueOf(passwordText));
            jsonObject.put("password_again", String.valueOf(password_again));
            Log.e("userInfo", UserUtils.getUserId()+"----"+String.valueOf(passwordText));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        POST(XingYuInterface.USER_UPDATE_DATA, jsonObject.toString());
    }

    /**
     * Post网络请求
     *
     * @return
     */
    public void POST(String url, String body) {
        RequestParams params = new RequestParams(url);
        String encodeToString = Base64.encodeToString(body.toString().getBytes(), Base64.DEFAULT);
        params.setBodyContent(encodeToString);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onCancelled(CancelledException arg0) {
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                Log.e("POST错误信息", arg0.toString());
            }

            @Override
            public void onFinished() {
            }

            @Override
            public void onSuccess(String json) {
                try {
                    Log.e("加密返回的json", json);
                    String result = new String(Base64.decode(json, Base64.DEFAULT), "utf-8");
                    Log.e("userInfo", result);
                    mHandler.obtainMessage(1, result).sendToTarget();

                } catch (Exception e) {
                    Log.e("POST+json成功回调出错：", e.toString());
                }
            }

            @Override
            public boolean onCache(String json) {
                return true;
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
