package com.xingyuyou.xingyuyou.Utils;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.bean.HotBannerBean;
import com.youth.banner.loader.ImageLoader;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/2/21.
 */

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object o, ImageView imageView) {
        Glide.with(context).load(R.mipmap.ic).into(imageView);
    }
}
