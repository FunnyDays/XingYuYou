package com.xingyuyou.xingyuyou.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.MCUtils.HttpUtils;
import com.xingyuyou.xingyuyou.Utils.SPUtils;
import com.xingyuyou.xingyuyou.Utils.StringUtils;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;

import org.json.JSONException;
import org.json.JSONObject;

public class UserInfoActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RelativeLayout mRlPhoto;
    private RelativeLayout mRlNickName;
    private RelativeLayout mRlUserSex;
    private AlertDialog mAlertDialog;
    private TextView mTvNickname;
    private String mNicknameText;
    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.obj != null) {
                Log.e("login", "-------------" + msg.obj.toString());
            }

        }
    };
    private String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        initData();
        initToolBar();
        initDialog();
        initView();
    }
    //获取用户信息
    private void initData() {
        SPUtils user_data = new SPUtils("user_data");
        mNicknameText = user_data.getString("nickname");
        mUserId = user_data.getString("id");
    }

    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("个人信息");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {
        mTvNickname = (TextView) findViewById(R.id.tv_nick_name);
        mTvNickname.setText(mNicknameText);

        mRlPhoto = (RelativeLayout) findViewById(R.id.rl_user_photo);
        mRlPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(UserInfoActivity.this, "该功能暂未开放", Toast.LENGTH_SHORT).show();
            }
        });
        mRlNickName = (RelativeLayout) findViewById(R.id.rl_nick_name);
        mRlNickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlertDialog.show();
            }
        });
        mRlUserSex = (RelativeLayout) findViewById(R.id.rl_user_sex);
        mRlUserSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(UserInfoActivity.this, "该功能暂未开放", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoActivity.this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_edittext, null);
        builder.setView(view);
        mAlertDialog = builder.create();
        final EditText editText = (EditText)view.findViewById(R.id.et_text);
        Button btNegative = (Button)view.findViewById(R.id.bt_negative);
        Button btPositive = (Button)view.findViewById(R.id.bt_positive);
        btNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlertDialog.dismiss();
            }
        });
        btPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlertDialog.dismiss();
                Editable editTextText = editText.getText();
                mTvNickname.setText(editTextText);

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("user_id", String.valueOf(mUserId));
                   // jsonObject.put("code", String.valueOf(2));
                    jsonObject.put("nickname", String.valueOf(editTextText));
                   // jsonObject.put("password_again", String.valueOf("qqqqqq"));
                    Log.e("login",mUserId+editTextText);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (StringUtils.isEmpty(editTextText)) return;
                HttpUtils.POST(mHandler, XingYuInterface.USER_UPDATE_DATA,jsonObject.toString(),true);
            }
        });




    }


}
