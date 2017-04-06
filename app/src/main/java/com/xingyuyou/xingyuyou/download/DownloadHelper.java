package com.xingyuyou.xingyuyou.download;

import android.os.Handler;
import android.util.Log;

import com.xingyuyou.xingyuyou.Utils.MCUtils.HttpUtils;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/4/6.
 */

public class DownloadHelper {
    public static void updataDown(String id) {
        OkHttpUtils.post()//
                .addParams("game_id",id)
                .url(XingYuInterface.UPDATA_DOWN)
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("onError", e.toString() + ":e");
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("onResponse", response + "");
                    }
                });
    }


}
