package com.xingyuyou.xingyuyou.Utils.MCUtils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Base64;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by Administrator on 2017/3/27.
 */

public class HttpUtils {
    /**
     * Post网络请求
     *
     * @param url
     * @return
     */
    public static void POST(final Handler handler, String url, String body, final boolean aa) {

        RequestParams params = new RequestParams(url);
        if (aa) {
            String encodeToString =Base64.encodeToString(body.toString().getBytes(), Base64.DEFAULT);
            params.setBodyContent(encodeToString);
        } else {
            params.setBodyContent(body);
        }
        final Message msg = new Message();
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onCancelled(CancelledException arg0) {
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                Log.e("POST错误信息", arg0.toString());
                msg.what = 2;
                handler.sendMessage(msg);
            }

            @Override
            public void onFinished() {
            }

            @Override
            public void onSuccess(String json) {
                msg.what = 1;
                try {
                    if (aa) {
                        Log.e("加密返回的json", json);
                        String result = new String(Base64.decode(json, Base64.DEFAULT), "utf-8");
                        msg.obj = result;
                        handler.sendMessage(msg);
                    } else {
                        Log.e("未加密返回的json", json);
                        msg.obj = json;
                        handler.sendMessage(msg);
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
