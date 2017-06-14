package com.xingyuyou.xingyuyou.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;
import com.xingyuyou.xingyuyou.R;

public class PhotoViewCommuActivity extends AppCompatActivity {

    private ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setPageMargin((int) (getResources().getDisplayMetrics().density * 15));

        mPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return getIntent().getStringArrayListExtra("picsLink").size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                PhotoView view = new PhotoView(PhotoViewCommuActivity.this);
                view.enable();
                view.setScaleType(ImageView.ScaleType.FIT_CENTER);
                ViewTarget<PhotoView, GlideDrawable> viewTarget = new ViewTarget<PhotoView, GlideDrawable>(view) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        this.view.setImageDrawable(resource.getCurrent());
                    }
                };
                Glide.with(PhotoViewCommuActivity.this).load(getIntent()
                        .getStringArrayListExtra("picsLink")
                        .get(position))
                        .override(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(viewTarget);
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });
        mPager.setCurrentItem(getIntent().getIntExtra("position",0));
    }
}
