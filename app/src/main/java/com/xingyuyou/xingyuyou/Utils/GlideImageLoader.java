package com.xingyuyou.xingyuyou.Utils;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import com.xingyuyou.xingyuyou.R;

import com.xingyuyou.xingyuyou.bean.HotBannerBean;
import com.youth.banner.loader.ImageLoader;


/**
 * Created by Administrator on 2017/2/21.
 */

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Log.e("lunbo", path.toString());
        Glide.with(context).load((String) path).into(imageView);
    }
}
