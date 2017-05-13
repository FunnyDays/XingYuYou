package com.xingyuyou.xingyuyou.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.SPUtils;
import com.xingyuyou.xingyuyou.Utils.StringUtils;
import com.xingyuyou.xingyuyou.Utils.glide.GlideCircleTransform;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.bean.user.UserBean;
import com.xingyuyou.xingyuyou.weight.CircleImageView;
import com.xingyuyou.xingyuyou.weight.dialog.CustomDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import net.bither.util.NativeUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import okhttp3.Call;

public class UserInfoActivity extends AppCompatActivity {
    private CustomDialog mDialog;
    private ArrayList<String> mImageList = new ArrayList();
    private static final int REQUEST_IMAGE = 2;
    private Toolbar mToolbar;
    private RelativeLayout mRlPhoto;
    private RelativeLayout mRlNickName;
    private RelativeLayout mRlUserSex;
    private String mNicknameText;
    private UserBean mUserBean;
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
                            JSONObject ja = jo.getJSONObject("data");
                            Gson gson = new Gson();
                            mUserBean = gson.fromJson(ja.toString(), UserBean.class);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    setValues();
                }
                if (msg.what == 2) {
                    String response = (String) msg.obj;
                    JSONObject jo = null;
                    try {
                        jo = new JSONObject(response);
                        String string = jo.getString("status");
                        if (string.equals("1")) {
                            String string1 = jo.getString("errorinfo");
                            Toast.makeText(UserInfoActivity.this, string1, Toast.LENGTH_SHORT).show();
                        }else if (string.equals("0")){
                            String string1 = jo.getString("errorinfo");
                            Toast.makeText(UserInfoActivity.this, string1, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    };


    private String mUserId;
    private ImageView mUserPhoto;
    private RelativeLayout mRlUserAge;
    private RelativeLayout mRlUserAddress;
    private RelativeLayout mRlUserBenming;
    private RelativeLayout mRlUserSignatures;
    private TextView mTvNickname;
    private TextView mTvUserSex;
    private TextView mTvUserAge;
    private TextView mTvUserAddress;
    private TextView mTvUserBenming;
    private TextView mTvUserSignatures;
    private TextView mTvUserDetailAddress;
    private RelativeLayout mRlUserDetailAddress;
    private CustomDialog mLoadingDialog;
    private CustomDialog mCustomDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        initView();
        initToolBar();
        initUserData();
    }

    //获取用户信息
    private void initUserData() {
        SPUtils user_data = new SPUtils("user_data");
        mNicknameText = user_data.getString("nickname");
        mUserId = user_data.getString("id");
        OkHttpUtils.post()//
                .url(XingYuInterface.GET_USER_INFO)
                .addParams("id", mUserId)
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

    //从服务器获取信息赋值到当前界面
    private void setValues() {
        if (mUserBean.getHead_image()!=null)
        Glide.with(UserInfoActivity.this)
                .load(mUserBean.getHead_image())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .transform(new GlideCircleTransform(UserInfoActivity.this))
                .into(mUserPhoto);
        if (mUserBean.getNickname()!=null)
        mTvNickname.setText(mUserBean.getNickname());
        if (mUserBean.getSex()!=null)
        mTvUserSex.setText((mUserBean.getSex().equals("0")?"男":"女"));
        if (mUserBean.getUser_age()!=null)
        mTvUserAge.setText(mUserBean.getUser_age());
        if (mUserBean.getArea()!=null)
        mTvUserAddress.setText(mUserBean.getArea());
        if (mUserBean.getHobby()!=null)
        mTvUserBenming.setText(mUserBean.getHobby());
        if (mUserBean.getExplain()!=null)
        mTvUserSignatures.setText(mUserBean.getExplain());
        if (mUserBean.getAddress_info()!=null)
        mTvUserDetailAddress.setText(mUserBean.getAddress_info());
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
        mToolbar.inflateMenu(R.menu.save_info_activity_menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ab_save:
                        saveUserData();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 保存用户信息
     */
    private void saveUserData() {
        //检查数据完成性
        if (StringUtils.isEmpty(mTvNickname.getText().toString().trim())) {
            Toast.makeText(this, "请填写昵称", Toast.LENGTH_SHORT).show();
            return;
        } if (StringUtils.isEmpty(mTvUserSex.getText().toString().trim())) {
            Toast.makeText(this, "请填写性别", Toast.LENGTH_SHORT).show();
            return;
        } if (StringUtils.isEmpty(mTvUserAge.getText().toString().trim())) {
            Toast.makeText(this, "请填写年龄", Toast.LENGTH_SHORT).show();
            return;
        } if (StringUtils.isEmpty(mTvUserAddress.getText().toString().trim())) {
            Toast.makeText(this, "请填写地区", Toast.LENGTH_SHORT).show();
            return;
        } if (StringUtils.isEmpty(mTvUserBenming.getText().toString().trim())) {
            Toast.makeText(this, "请填写本命", Toast.LENGTH_SHORT).show();
            return;
        } if (StringUtils.isEmpty(mTvUserSignatures.getText().toString().trim())) {
            Toast.makeText(this, "请填写签名", Toast.LENGTH_SHORT).show();
            return;
        }if (StringUtils.isEmpty(mTvUserDetailAddress.getText().toString().trim())) {
            Toast.makeText(this, "请填写收获地址", Toast.LENGTH_SHORT).show();
            return;
        }
        mDialog = new CustomDialog(UserInfoActivity.this, "正在上传，请稍等");
        mDialog.showDialog();

        UserUtils.setNickName(mTvNickname.getText().toString().trim());

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", mUserId);
        params.put("sex",(mTvUserSex.getText().toString().trim().equals("男")?"0":"1"));
        params.put("nickname", mTvNickname.getText().toString().trim());
        params.put("user_age", mTvUserAge.getText().toString().trim());
        params.put("explain", mTvUserSignatures.getText().toString().trim());
        params.put("hobby", mTvUserBenming.getText().toString().trim());
        params.put("area", mTvUserAddress.getText().toString().trim());
        params.put("address_info", mTvUserDetailAddress.getText().toString().trim());

        PostFormBuilder post = OkHttpUtils.post();
        if (mImageList.size()!=0){
            File file = new File(mImageList.get(0));
            if (file.exists()) {
                File file1 = new File(getExternalCacheDir() + "/tempCompress0"+ ".jpg");
                NativeUtil.compressBitmap(mImageList.get(0), file1.getAbsolutePath());
                Log.e("file1",(mTvUserSex.getText().toString().trim().equals("男")?"0":"1")+"---"+file1.getAbsolutePath()+file1.getName());
                post.addFile("img", file.getName(), file1);
            }
        }
        post.url(XingYuInterface.UPDATE_INFORMATION)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        mDialog.dismissDialog();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mDialog.dismissDialog();
                        mHandler.obtainMessage(2, response).sendToTarget();
                    }
                });
    }
    public interface EditTextValuesCallback {
        public void editValues(String result);
    }
    private void initView() {
        //初始化Dialog
        mLoadingDialog = new CustomDialog(this);
        //用户头像
        mUserPhoto = (ImageView) findViewById(R.id.iv_user_photo);
        mRlPhoto = (RelativeLayout) findViewById(R.id.rl_user_photo);
        mRlPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MultiImageSelector.create(UserInfoActivity.this)
                        .showCamera(true)
                        .single()
                        .start(UserInfoActivity.this, REQUEST_IMAGE);
               /* Glide.with(UserInfoActivity.this)
                        .load(mImageList.get(0))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .transform(new GlideCircleTransform(UserInfoActivity.this))
                        .into(mUserPhoto);*/
            }
        });
        //修改昵称
        mTvNickname = (TextView) findViewById(R.id.tv_nick_name);
        mRlNickName = (RelativeLayout) findViewById(R.id.rl_nick_name);
        mRlNickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoadingDialog.EditTextDialog(UserInfoActivity.this, "修改昵称", new EditTextValuesCallback() {
                    @Override
                    public void editValues(String result) {
                        mTvNickname.setText(result);
                    }
                });
                mLoadingDialog.showDialog();
            }
        });
        //修改性别
        mTvUserSex = (TextView) findViewById(R.id.tv_user_sex);
        mRlUserSex = (RelativeLayout) findViewById(R.id.rl_user_sex);
        mRlUserSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoadingDialog.RadioDialog(UserInfoActivity.this, "修改性别", new EditTextValuesCallback() {
                    @Override
                    public void editValues(String result) {
                        Log.e("sex",result);
                        mTvUserSex.setText(result);
                    }
                });
                mLoadingDialog.showDialog();
            }
        });
        //修改年龄
        mTvUserAge = (TextView) findViewById(R.id.tv_user_age);
        mRlUserAge = (RelativeLayout) findViewById(R.id.rl_user_age);
        mRlUserAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoadingDialog.EditTextDialog(UserInfoActivity.this, "修改年龄", new EditTextValuesCallback() {
                    @Override
                    public void editValues(String result) {
                        mTvUserAge.setText(result);
                    }
                });
                mLoadingDialog.showDialog();
            }
        });
        //修改地区
        mTvUserAddress = (TextView) findViewById(R.id.tv_user_address);
        mRlUserAddress = (RelativeLayout) findViewById(R.id.rl_user_address);
        mRlUserAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoadingDialog.EditTextDialog(UserInfoActivity.this, "修改地区", new EditTextValuesCallback() {
                    @Override
                    public void editValues(String result) {
                        mTvUserAddress.setText(result);
                    }
                });
                mLoadingDialog.showDialog();
            }
        });
        //修改本命
        mTvUserBenming = (TextView) findViewById(R.id.tv_user_benming);
        mRlUserBenming = (RelativeLayout) findViewById(R.id.rl_user_benming);
        mRlUserBenming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoadingDialog.EditTextDialog(UserInfoActivity.this, "修改本命", new EditTextValuesCallback() {
                    @Override
                    public void editValues(String result) {
                        mTvUserBenming.setText(result);
                    }
                });
                mLoadingDialog.showDialog();
            }
        });
        //修改签名
        mTvUserSignatures = (TextView) findViewById(R.id.tv_user_signatures);
        mRlUserSignatures = (RelativeLayout) findViewById(R.id.rl_user_signatures);
        mRlUserSignatures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoadingDialog.EditTextDialog(UserInfoActivity.this, "修改签名", new EditTextValuesCallback() {
                    @Override
                    public void editValues(String result) {
                        mTvUserSignatures.setText(result);
                    }
                });
                mLoadingDialog.showDialog();
            }
        });
        //修改收货地址
        mTvUserDetailAddress = (TextView) findViewById(R.id.tv_user_detail_address);
        mRlUserDetailAddress = (RelativeLayout) findViewById(R.id.rl_user_detail_address);
        mRlUserDetailAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoadingDialog.EditTextDialog(UserInfoActivity.this, "修改收货地址", new EditTextValuesCallback() {
                    @Override
                    public void editValues(String result) {
                        mTvUserDetailAddress.setText(result);
                    }
                });
                mLoadingDialog.showDialog();
            }
        });


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                mImageList.clear();
                mImageList.addAll(path);
                Glide.with(UserInfoActivity.this)
                        .load(mImageList.get(0))
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .transform(new GlideCircleTransform(UserInfoActivity.this))
                        .into(mUserPhoto);
            }
        }
    }
}
