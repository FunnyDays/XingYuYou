package com.xingyuyou.xingyuyou.Utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xingyuyou.xingyuyou.R;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by Administrator on 2017/2/21.
 */

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object o, ImageView imageView) {
        Glide.with(context).load(R.mipmap.ic).into(imageView);
    }
}
