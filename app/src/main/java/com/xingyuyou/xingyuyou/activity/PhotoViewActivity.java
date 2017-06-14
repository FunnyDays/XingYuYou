package com.xingyuyou.xingyuyou.activity;

import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.FileUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;

import okhttp3.Call;
import okhttp3.Request;

public class PhotoViewActivity extends AppCompatActivity {

    private ViewPager mPager;
    private TextView mTv_image_number;
    private TextView mTv_image_real;
    private ImageView mIv_save_image;
    private int mPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        mTv_image_number = (TextView) findViewById(R.id.tv_image_number);
        mTv_image_real = (TextView) findViewById(R.id.tv_image_real);
        mTv_image_real.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PhotoViewActivity.this, "已是原图", Toast.LENGTH_SHORT).show();
            }
        });
        //保存图片
        mIv_save_image = (ImageView) findViewById(R.id.iv_save_image);
        mIv_save_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadImage();
            }
        });
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
                PhotoView view = new PhotoView(PhotoViewActivity.this);
                view.enable();
                view.setScaleType(ImageView.ScaleType.FIT_CENTER);
                ViewTarget<PhotoView, GlideDrawable> viewTarget = new ViewTarget<PhotoView, GlideDrawable>(view) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        this.view.setImageDrawable(resource.getCurrent());
                    }
                };
                Glide.with(PhotoViewActivity.this).load(getIntent()
                        .getStringArrayListExtra("picsLink")
                        .get(position))
                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
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
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPosition = position;
                mTv_image_number.setText((position + 1)
                        + "/" +
                        getIntent().getStringArrayListExtra("picsLink").size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mPager.setCurrentItem(getIntent().getIntExtra("position", 0));
    }

    private void downloadImage() {
        Toast.makeText(PhotoViewActivity.this, "下载开始", Toast.LENGTH_SHORT).show();
        OkHttpUtils//
                .get()//
                .url(getIntent()
                        .getStringArrayListExtra("picsLink")
                        .get(mPosition))//
                .build()//
                .execute(new FileCallBack(FileUtils.imageSavePath,getIntent()
                        .getStringArrayListExtra("picsLink")
                        .get(mPosition).substring(getIntent()
                                .getStringArrayListExtra("picsLink")
                                .get(mPosition).length()-10,getIntent()
                                .getStringArrayListExtra("picsLink")
                                .get(mPosition).length()))//
                {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(PhotoViewActivity.this, "下载出错", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(File response, int id) {

                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        Toast.makeText(PhotoViewActivity.this, "下载完成，目录:" + FileUtils.imageSavePath, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
