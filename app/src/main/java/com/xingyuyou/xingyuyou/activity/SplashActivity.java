package com.xingyuyou.xingyuyou.activity;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.AppUtils;
import com.xingyuyou.xingyuyou.Utils.IntentUtils;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.weight.ProgressButton;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import okhttp3.Call;
import okhttp3.Request;

public class SplashActivity extends AppCompatActivity {

    /**
     * {
     * "status": 1,
     * "msg": {
     * "qq": "765161218",
     * "weixin": "",
     * "qq_group": "",
     * "network": "www.xingyuyou.com",
     * "icon": null,
     * "version": "1.0",
     * "app_download": "www。百度",
     * "app_name": "星宇游app",
     * "app_welcome": "http://xingyuyou.com/Uploads/Picture/2017-02-16/58a5020da2ba7.jpg",
     * "about_ico": "http://xingyuyou.com/Uploads/Picture/2017-02-16/58a5020da2ba7.jpg"
     * }
     * }
     */
   /* Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                try {
                    JSONObject jsonObject = new JSONObject(msg.obj.toString());
                    if (jsonObject.getString("status").equals("1")) {
                        JSONObject object = jsonObject.getJSONObject("msg");
                        mQq = object.getString("qq");
                        mNetwork = object.getString("network");
                        mAppDownload = object.getString("app_download");
                        mVersionText = object.getString("version");
                        mUpdateInfo = object.getString("app_name");
                        mText.setText("QQ:" + mQq + "\n" + "官网：" + mNetwork);
                        mVersion.setText(mVersionText);
                        if (Integer.valueOf(mVersionText) > mVersionCode) {
                          //  ToUpadte();

                        } else {
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                  //  IntentUtils.startActivityAndFinish(SplashActivity.this, MainActivity.class);
                                }
                            }, 1000);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        IntentUtils.startActivityAndFinish(SplashActivity.this, MainActivity.class);
        Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
       new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
     /*   mVersionCode = AppUtils.getAppVersionCode(this);
        Log.e("banben", mVersionCode + "版本号");
        mText = (TextView) findViewById(R.id.text);
        mVersion = (TextView) findViewById(R.id.version);
        checkUpdate();*/
    }

   /* private void ToUpadte() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_update_app, null);
        builder.setView(view);
        mTvUpdateInfo = (TextView) view.findViewById(R.id.tv_update_info);
        mTvUpdateInfo.setText(mUpdateInfo);
        mBtUpdate = (ProgressButton) view.findViewById(R.id.bt_update);
        mBtUpdate.setTag(0);
        mBtUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((int)mBtUpdate.getTag()==0){
                    mBtUpdate.setTag(1);
                    toDownload();
                    Toast.makeText(SplashActivity.this, "开始下载", Toast.LENGTH_SHORT).show();
                }else
                {
                    Toast.makeText(SplashActivity.this, "正在下载，请稍后", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mAlertDialog = builder.create();
        mAlertDialog.setCancelable(false);
        mAlertDialog.show();
    }*/

  /*  private void toDownload() {
        OkHttpUtils//
                .get()//
                .url(mAppDownload)//
                .build()//http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/%E5%8D%95%E6%9C%BA/Iter.apk
                .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), "xingyu.apk")//
                {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(SplashActivity.this, "下载出错", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {

                        mBtUpdate.setProgress((int)(progress*100));
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        Log.e("dizhi", "onResponse :" + response.getAbsolutePath());
                    }

                    @Override
                    public void onAfter(int id) {
                        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/xingyu.apk";
                        Log.e("dizhi", "onResponse :" + path);
                        AppUtils.installApp(SplashActivity.this,path);
                        Toast.makeText(SplashActivity.this, "下载完成", Toast.LENGTH_SHORT).show();
                    }
                });
    }*/

  /*  */

    /**
     * 检查更新 XingYuInterface.ABOUT_US
     *//*
    private void checkUpdate() {
        OkHttpUtils.post()//
                .url(XingYuInterface.ABOUT_US)
                .tag(this)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("hot", e.toString() + ":e");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mHandler.obtainMessage(1, response).sendToTarget();
                        Log.e("xiazai", response + ":e");
                    }
                });
    }*/
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
