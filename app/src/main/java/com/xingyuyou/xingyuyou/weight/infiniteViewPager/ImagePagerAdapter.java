package com.xingyuyou.xingyuyou.weight.infiniteViewPager;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xingyuyou.xingyuyou.R;

/**
 * Created by twiceYuan on 9/13/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 */
public class ImagePagerAdapter extends PagerAdapter {

    int[] imgRes = {
            R.mipmap.al,
            R.mipmap.dj,
            R.mipmap.al,

    };

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ImageView view = new ImageView(container.getContext());
        view.setScaleType(ImageView.ScaleType.FIT_XY);

        view.setImageResource(imgRes[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return imgRes.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }
}
