package com.xingyuyou.xingyuyou.activity;

import android.os.Handler;
import android.os.Message;
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
import com.xingyuyou.xingyuyou.Utils.StringUtils;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;


public class RegisterActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private EditText mPhoneNumber;
    private Button mSendCode;
    private HttpUtils mHttp;
    private Button mBtRegister;
    private EditText mPhoneCode;
    private EditText mPassword;
    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==0){
                String result = (String) msg.obj;
                Log.e("userInfo","00000000处理数据"+result);
            }if (msg.what==1){
                String result = (String) msg.obj;
                Log.e("userInfo","111111111处理数据"+result);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initToolBar();
        initView();
    }

    private void initView() {
        //发送验证码
        mPhoneNumber = (EditText) findViewById(R.id.et_phone_number);
        mSendCode = (Button) findViewById(R.id.bt_send_code);
        mSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPhoneCode(0);
            }
        });

        //注册
        mPhoneNumber = (EditText) findViewById(R.id.et_phone_number);
        mPassword = (EditText) findViewById(R.id.et_password);
        mPhoneCode = (EditText) findViewById(R.id.et_phone_code);
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

    /**
     * 发送验证码
     */
    public void getPhoneCode(int code) {
        String phoneCodeText = mPhoneNumber.getText().toString().trim();
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
            POST(code,XingYuInterface.SEND_SMS,jsonObject.toString());
        } else {
            Toast.makeText(this, "请检查手机号码是否正确", Toast.LENGTH_SHORT).show();
        }
    }
    private void toRegister(int code) {
        String phoneNumberText = mPhoneNumber.getText().toString().trim();
        String passwordText = mPassword.getText().toString().trim();
        String phoneCodeText = mPhoneCode.getText().toString().trim();
        Log.e("phonecode", "电话号码："+phoneNumberText+
                "密码："+passwordText+"验证码："+phoneCodeText);
        if (RegexUtils.isMobileExact(phoneNumberText)&& !StringUtils.isEmpty(passwordText)
                && !StringUtils.isEmpty(phoneCodeText)) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("account", String.valueOf(phoneNumberText));
                jsonObject.put("vcode", String.valueOf(phoneCodeText));
                jsonObject.put("password", String.valueOf(passwordText));
                jsonObject.put("nickname", String.valueOf("昵称"));
                jsonObject.put("sex", String.valueOf(0));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            POST(code,XingYuInterface.USER_REGISTER,jsonObject.toString());
        } else {
            Toast.makeText(this, "请输入完成信息", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Post网络请求
     *
     * @return
     */
    public void POST(final int code, String url, String body) {
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
                    Log.e("userInfo",result);
                    if (code==0){
                        mHandler.obtainMessage(0, result).sendToTarget();
                    }
                    if (code==1){
                        mHandler.obtainMessage(1, result).sendToTarget();
                    }

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
