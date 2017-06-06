package com.xingyuyou.xingyuyou.weight.infiniteViewPager;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.glide.GlideRoundTransform;
import com.xingyuyou.xingyuyou.adapter.GodAdapter;
import com.xingyuyou.xingyuyou.bean.god.GodBean;

import java.util.List;

/**
 * Created by twiceYuan on 9/13/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 */
public class ImagePagerAdapter extends PagerAdapter {
    private List<GodBean> imgRes;
    private Activity mActivity;
    public ImagePagerAdapter(List<GodBean> imgRes, Activity activity) {
        this.imgRes = imgRes;
        this.mActivity=activity;
    }


    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ImageView view = new ImageView(container.getContext());
        view.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(mActivity)
                .load(imgRes.get(position).getGod_image())
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(view);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return imgRes.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }
}
