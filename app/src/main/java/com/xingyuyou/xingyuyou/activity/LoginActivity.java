package com.xingyuyou.xingyuyou.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.MCUtils.Base64;
import com.xingyuyou.xingyuyou.Utils.MCUtils.Base64Util;
import com.xingyuyou.xingyuyou.Utils.MCUtils.RequestParamUtil;
import com.xingyuyou.xingyuyou.Utils.StringUtils;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class LoginActivity extends AppCompatActivity {

    private EditText mUserName;
    private EditText mPassword;
    private Button mRegister;
    private HttpUtils mHttp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mUserName = (EditText) findViewById(R.id.et_username);
        mPassword = (EditText) findViewById(R.id.et_password);
        mRegister = (Button) findViewById(R.id.bt_register_in_button);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserInfo();
            }
        });
    }

    public void getUserInfo() {
        String usernameText = mUserName.getText().toString().trim();
        String passwordText = mPassword.getText().toString().trim();
        if ((!StringUtils.isEmpty(usernameText)) && (!StringUtils.isEmpty(passwordText))) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("account", String.valueOf(usernameText));
            params.put("password", String.valueOf(passwordText));
          /*  params.put("promote_id", String.valueOf("7"));
            params.put("promote_account", String.valueOf("安卓客户端注册"));
            params.put("game_id", String.valueOf("32"));
            params.put("game_name", String.valueOf("星宇游"));
            params.put("game_appid", String.valueOf("E32CF36D312E548A5"));*/
            Log.e("zhuce", usernameText + "-------------" + passwordText);
            String param = RequestParamUtil.getRequestParamString(params);

            //溪谷注册方式
            //toXiGuRegister(param);
            //登录测试
            toLogin(params);
        } else {
            Toast.makeText(this, "请输入完成信息", Toast.LENGTH_SHORT).show();
        }
    }

    private void toXiGuRegister(String param) {
        RequestParams params = new RequestParams();
        try {
            params.setBodyEntity(new StringEntity(param.toString()));
        } catch (UnsupportedEncodingException e) {
            params = null;
        }
        mHttp = new HttpUtils();
        mHttp.send(HttpRequest.HttpMethod.POST, XingYuInterface.USER_LOGIN, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Toast.makeText(LoginActivity.this, "注册者成功", Toast.LENGTH_SHORT).show();
                Log.e("zhuce", getResponse(responseInfo).toString() + ":e");
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(LoginActivity.this, "注册出错", Toast.LENGTH_SHORT).show();
                Log.e("zhuce", msg.toString() + ":e");
            }
        });
    }

    private void toRegister(Map<String, String> params) {

        OkHttpUtils.post()//
                .url(XingYuInterface.USER_REGISTER)
                .tag(this)//
                .params(params)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                        Toast.makeText(LoginActivity.this, "注册出错", Toast.LENGTH_SHORT).show();
                        Log.e("zhuce", e.toString() + ":e");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        Log.e("zhuce", response + "");
                    }
                });
    }

    private void toLogin(Map<String, String> params) {
        OkHttpUtils.post()//
                .url(XingYuInterface.USER_LOGIN)
                .tag(this)//
                .params(params)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                        Toast.makeText(LoginActivity.this, "注册出错", Toast.LENGTH_SHORT).show();
                        Log.e("zhuce", e.toString() + ":e");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        Log.e("zhuce", response + "");
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(this);//取消以Activity.this作为tag的请求
    }

    public static String getResponse(final ResponseInfo<String> responseInfo) {
        String response = null;
//				responseInfo.result.trim().replaceAll(" ", "");
        try {
            response = new String(Base64.decode(responseInfo.result.trim()), "utf-8");
        } catch (Exception e2) {
            Log.e("zhuce", "第一次base64解码出错了");
            try {
                response = Base64Util.getDecodeStr(response);
            } catch (Exception e) {
                Log.e("zhuce", "第二次base64解码出错了");
            }
        } finally {
            Log.e("zhuce" + "==" + "ReautestUtil", "onSuccess:" + response);
        }
        return response;
    }
}
