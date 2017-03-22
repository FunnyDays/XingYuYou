package com.xingyuyou.xingyuyou.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;

import com.xingyuyou.xingyuyou.R;

import com.xingyuyou.xingyuyou.Utils.RegexUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;


public class RegisterActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private EditText mPhoneCode;
    private Button mSendCode;
    private HttpUtils mHttp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initToolBar();
        initView();
    }

    private void initView() {
        mPhoneCode = (EditText) findViewById(R.id.et_phone_number);
        mSendCode = (Button) findViewById(R.id.bt_send_code);
        mSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPhoneCode();
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

    /**
     * 发送验证码
     */
    public void getPhoneCode() {
        String phoneCodeText = mPhoneCode.getText().toString().trim();
        Log.e("phonecode",phoneCodeText+ "：电话号码");
        if (RegexUtils.isMobileExact(phoneCodeText)) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("phone", String.valueOf(phoneCodeText));
                jsonObject.put("demand", String.valueOf(1));
            } catch (JSONException e) {
                e.printStackTrace();
            }

           // getPhoneCode(jsonObject.toString());
            POST(jsonObject.toString());
        } else {
            Toast.makeText(this, "请检查手机号码是否正确", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Post网络请求
     *
     * @return
     */
    public static void POST( String body) {
        RequestParams params = new RequestParams("http://xingyuyou.com/app.php/User/send_sms");
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



    private void getPhoneCode(String param) {
        RequestParams requestParams = new RequestParams("http://xingyuyou.com/app.php/User/send_sms");
        requestParams.setBodyContent(Base64.encodeToString(param.getBytes(),0));
        x.http().post(requestParams, new Callback.CacheCallback<Object>() {
            @Override
            public boolean onCache(Object result) {
                Log.e("register","onCache"+result.toString());
                return false;
            }

            @Override
            public void onSuccess(Object result) {
                Log.e("register","onSuccess"+result.toString());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("register","onError"+ex.toString()+"isOnCallback:"+isOnCallback);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.e("register","onCancelled");
            }

            @Override
            public void onFinished() {
                Log.e("register","onFinished");
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
