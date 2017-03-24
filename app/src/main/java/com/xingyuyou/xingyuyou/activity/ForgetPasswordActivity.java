package com.xingyuyou.xingyuyou.activity;

import android.animation.ValueAnimator;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.RegexUtils;
import com.xingyuyou.xingyuyou.Utils.StringUtils;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class ForgetPasswordActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private EditText mPhoneNumber;
    private Button mSendCode;
    private EditText mPassword;
    private EditText mAgainPassword;
    private EditText mPhoneCode;
    private Button mUpdatePassword;
    private ValueAnimator mValueAnimator;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                  /*{
                     "status": 1,
                     "return_code": "success",
                     "msg": "验证码发送成功",
                     "phone": "18291910677",
                     "code": 943145
                }*/



                String result = (String) msg.obj;
                JSONObject jsonObject = null;
                try {
                    mValueAnimator = ValueAnimator.ofFloat(60, 0);
                    mValueAnimator.setDuration(60000);
                    mValueAnimator.setInterpolator( new LinearInterpolator());
                    mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            Log.e("value", animation.getAnimatedValue().toString());
                            mSendCode.setText(Integer.valueOf(((int)(float)animation.getAnimatedValue()))+"s");
                            mSendCode.setEnabled(false);
                            if (animation.getAnimatedValue().toString().equals("0.0")){
                                mSendCode.setText("重新发送");
                                mSendCode.setEnabled(true);
                            }

                        }
                    });
                    mValueAnimator.start();
                    jsonObject = new JSONObject(result);
                    String return_msg = jsonObject.getString("msg");
                    Toast.makeText(ForgetPasswordActivity.this, return_msg, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (msg.what == 1) {
                String result = (String) msg.obj;
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    String return_msg = jsonObject.getString("msg");
                    if (jsonObject.getString("return_code").equals("success")){
                       finish();
                    }
                    Toast.makeText(ForgetPasswordActivity.this, return_msg+",请登录", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        initToolBar();
        initView();
    }
    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("更改密码");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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

        //更新密码
        mPassword = (EditText) findViewById(R.id.et_password);
        mAgainPassword = (EditText) findViewById(R.id.et_again_password);
        mPhoneCode = (EditText) findViewById(R.id.et_phone_code);
        mUpdatePassword = (Button) findViewById(R.id.bt_update_password_button);
        mUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePassword(1);
            }
        });
    }

    public void getPhoneCode(int code) {
        String phoneCodeText = mPhoneNumber.getText().toString().trim();
        if (!RegexUtils.isMobileExact(phoneCodeText)) {
            Toast.makeText(this, "请检查手机号码是否正确", Toast.LENGTH_SHORT).show();
            return;
        }

        if (RegexUtils.isMobileExact(phoneCodeText)) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("phone", String.valueOf(phoneCodeText));
                jsonObject.put("demand", String.valueOf(2));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // getPhoneCode(jsonObject.toString());
            POST(code, XingYuInterface.SEND_SMS, jsonObject.toString());
        } else {
            Toast.makeText(this, "请检查手机号码是否正确", Toast.LENGTH_SHORT).show();
        }
    }

    private void updatePassword(int code) {
        String phoneNumberText = mPhoneNumber.getText().toString().trim();
        String passwordText = mPassword.getText().toString().trim();
        String againPasswordText = mAgainPassword.getText().toString().trim();
        if (!passwordText.equals(againPasswordText)) {
            Toast.makeText(this, "密码不一致，请重新输入", Toast.LENGTH_SHORT).show();
            return;
        }
        String phoneCodeText = mPhoneCode.getText().toString().trim();
        if (StringUtils.isEmpty(passwordText)) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isEmpty(phoneCodeText)) {
            Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.e("updatepassword", "电话号码：" + phoneNumberText +
                "密码：" + passwordText + "确认密码：" + againPasswordText + "验证码：" + phoneCodeText);
        if (RegexUtils.isMobileExact(phoneNumberText) && !StringUtils.isEmpty(passwordText)
                && !StringUtils.isEmpty(againPasswordText)
                && !StringUtils.isEmpty(phoneCodeText)) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("phone", String.valueOf(phoneNumberText));
                jsonObject.put("code", String.valueOf(phoneCodeText));
                jsonObject.put("password", String.valueOf(passwordText));
                jsonObject.put("password_again", String.valueOf(againPasswordText));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            POST(code, XingYuInterface.FORGET_PASSWORD, jsonObject.toString());
        } else {
            Toast.makeText(this, "请输入完成信息", Toast.LENGTH_SHORT).show();
        }
    }

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
                    Log.e("userInfo", result);
                    if (code == 0) {
                        Toast.makeText(ForgetPasswordActivity.this, "正在发送", Toast.LENGTH_SHORT).show();
                        mHandler.obtainMessage(0, result).sendToTarget();
                    }
                    if (code == 1) {
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


}
