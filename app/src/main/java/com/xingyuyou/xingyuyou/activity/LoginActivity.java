package com.xingyuyou.xingyuyou.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.IntentUtils;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.StringUtils;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.bean.community.TagBean;
import com.xingyuyou.xingyuyou.weight.dialog.CustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText mUserName;
    private EditText mPassword;
    private Button mLogin;
    private HttpUtils mHttp;
    private Button mButtonRegister;
    private Toolbar mToolbar;
    private Button mForgetPassword;
    private CustomDialog mLoadingDialog;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                mLoadingDialog.dismissDialog();
                String result = (String) msg.obj;
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    Toast.makeText(LoginActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    if (!jsonObject.getString("status").equals("1"))
                        return;
                    JSONObject list = jsonObject.getJSONObject("list");
                    UserUtils.Login(list.getString("id"), list.getString("account"), list.getString("nickname"), list.getString("head_image"));
                    mLoadingDialog.CancelDialog();
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    private UMShareAPI mShareAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initThreeLogin();
        initToolBar();
        mUserName = (EditText) findViewById(R.id.et_username);
        mUserName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    Drawable drawable = getResources().getDrawable(R.drawable.ic_login_user);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    mUserName.setCompoundDrawables(drawable, null, null, null);
                    mUserName.setHintTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    Drawable drawable = getResources().getDrawable(R.drawable.ic_login_user_gery);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    mUserName.setCompoundDrawables(drawable, null, null, null);
                    mUserName.setHintTextColor(getResources().getColor(R.color.grey));
                }
            }
        });
        mPassword = (EditText) findViewById(R.id.et_password);
        mPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    Drawable drawable = getResources().getDrawable(R.drawable.ic_login_password);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    mPassword.setCompoundDrawables(drawable, null, null, null);
                    mPassword.setHintTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    Drawable drawable = getResources().getDrawable(R.drawable.ic_login_password_grey);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    mPassword.setCompoundDrawables(drawable, null, null, null);
                    mPassword.setHintTextColor(getResources().getColor(R.color.grey));
                }
            }
        });
        mLogin = (Button) findViewById(R.id.bt_login_button);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserInfo();
            }
        });
        mForgetPassword = (Button) findViewById(R.id.bt_forgetPassword);
        mForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.startActivity(LoginActivity.this, ForgetPasswordActivity.class);
            }
        });
        mButtonRegister = (Button) findViewById(R.id.bt_register);
        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.startActivity(LoginActivity.this, RegisterActivity.class);
            }
        });

    }

    private void initThreeLogin() {
        mShareAPI = UMShareAPI.get(this);
        Button qq = (Button) findViewById(R.id.bt_login_qq);
        Button wx = (Button) findViewById(R.id.bt_login_wx);
        Button xl = (Button) findViewById(R.id.bt_login_xl);
        qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShareAPI.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.QQ, umAuthListener);
            }
        });
        wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShareAPI.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);
            }
        });
        xl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShareAPI.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.SINA, umAuthListener);
            }
        });
    }

    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //授权开始的回调
            Log.e("qqlogin", "onStart: ");
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            Toast.makeText(getApplicationContext(), "Authorize succeed", Toast.LENGTH_SHORT).show();
            Log.e("qqlogin", "onComplete: " + data.toString());
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(getApplicationContext(), "Authorize fail", Toast.LENGTH_SHORT).show();
            Log.e("qqlogin", "onError: " + t.toString());
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(getApplicationContext(), "Authorize cancel", Toast.LENGTH_SHORT).show();
            Log.e("qqlogin", "onCancel: ");
        }
    };

    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("登录");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mToolbar.inflateMenu(R.menu.forget_password_activity_menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ab_send:
                        IntentUtils.startActivity(LoginActivity.this, ForgetPasswordActivity.class);
                        break;
                }
                return false;
            }
        });
    }

    public void getUserInfo() {
        String usernameText = mUserName.getText().toString().trim();
        String passwordText = mPassword.getText().toString().trim();
        if ((!StringUtils.isEmpty(usernameText)) && (!StringUtils.isEmpty(passwordText))) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("account", String.valueOf(usernameText));
                jsonObject.put("password", String.valueOf(passwordText));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //账号登录
            toLoginPOST(0, XingYuInterface.USER_LOGIN, jsonObject.toString());
        } else {
            Toast.makeText(this, "请输入完整登录信息", Toast.LENGTH_SHORT).show();
        }
    }

    public void toLoginPOST(final int code, String url, String body) {
        mLoadingDialog = new CustomDialog(this);
        mLoadingDialog.showDialog();
        org.xutils.http.RequestParams params = new org.xutils.http.RequestParams(url);
        String encodeToString = android.util.Base64.encodeToString(body.toString().getBytes(), android.util.Base64.DEFAULT);
        params.setBodyContent(encodeToString);
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onCancelled(CancelledException arg0) {

            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                mLoadingDialog.dismissDialog();
            }

            @Override
            public void onFinished() {
                Log.e("POST错误信息", "onFinished");
            }

            @Override
            public void onSuccess(String json) {
                try {
                    String result = new String(android.util.Base64.decode(json, android.util.Base64.DEFAULT), "utf-8");
                    if (code == 0) {
                        // Log.e("login", "onSuccess: "+result );
                        mHandler.obtainMessage(0, result).sendToTarget();
                    }
                } catch (Exception e) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }
}
