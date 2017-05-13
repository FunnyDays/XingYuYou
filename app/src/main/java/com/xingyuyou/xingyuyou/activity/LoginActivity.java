package com.xingyuyou.xingyuyou.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.IntentUtils;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.StringUtils;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.weight.dialog.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

public class LoginActivity extends AppCompatActivity {

    private EditText mUserName;
    private EditText mPassword;
    private Button mLogin;
    private HttpUtils mHttp;
    private Button mButtonRegister;
    private Toolbar mToolbar;
    private Button mForgetPassword;
    private CustomDialog mLoadingDialog;
    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==0){
                mLoadingDialog.dismissDialog();
                String result = (String) msg.obj;
                JSONObject jsonObject = null;
                try {
                    Log.e("value", result.toString());
                    jsonObject = new JSONObject(result);
                    Toast.makeText(LoginActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    if (!jsonObject.getString("status").equals("1"))
                        return;
                    JSONObject list = jsonObject.getJSONObject("list");
                    UserUtils.Login(list.getString("id"),list.getString("account"),list.getString("nickname"));
                    mLoadingDialog.CancelDialog();
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initToolBar();
        mUserName = (EditText) findViewById(R.id.et_username);
        mPassword = (EditText) findViewById(R.id.et_password);
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

    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("登录");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
            toLoginPOST(0,XingYuInterface.USER_LOGIN,jsonObject.toString());
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
                Log.e("POST错误信息", arg0.toString());
                mLoadingDialog.dismissDialog();
            }

            @Override
            public void onFinished() {

                Log.e("POST错误信息","onFinished" );
            }

            @Override
            public void onSuccess(String json) {
                try {
                    String result = new String(android.util.Base64.decode(json, android.util.Base64.DEFAULT), "utf-8");
                    if (code==0){
                        mHandler.obtainMessage(0, result).sendToTarget();
                    }
                } catch (Exception e) {
                    Log.e("login+json成功回调出错：", e.toString());
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
