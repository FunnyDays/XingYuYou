package com.xingyuyou.xingyuyou.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
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
import com.google.gson.Gson;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.ConstUtils;
import com.xingyuyou.xingyuyou.Utils.ConvertUtils;
import com.xingyuyou.xingyuyou.Utils.FileUtils;
import com.xingyuyou.xingyuyou.bean.community.PostDetailBean;
import com.xingyuyou.xingyuyou.bean.community.PostsResetImageBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import okhttp3.Call;

public class PhotoViewPostDetailActivity extends AppCompatActivity {

    private ViewPager mPager;
    private TextView mTv_image_number;
    private TextView mTv_image_real;
    private ImageView mIv_save_image;
    private int mPosition = 0;
    private PostDetailBean mPostDetailBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        //获取信息
        initData();
        mTv_image_number = (TextView) findViewById(R.id.tv_image_number);
        mTv_image_real = (TextView) findViewById(R.id.tv_image_real);
        mTv_image_real.setText("查看原图("+mPostDetailBean.getPosts_reset_image().get(1).getPosts_image_size()+")");
        mTv_image_real.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PhotoViewPostDetailActivity.this, "已是原图", Toast.LENGTH_SHORT).show();
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
                return mPostDetailBean.getThumbnail_image().size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                PhotoView view = new PhotoView(PhotoViewPostDetailActivity.this);
                view.enable();
                view.setScaleType(ImageView.ScaleType.FIT_CENTER);
                ViewTarget<PhotoView, GlideDrawable> viewTarget = new ViewTarget<PhotoView, GlideDrawable>(view) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        this.view.setImageDrawable(resource.getCurrent());
                    }
                };
                Glide.with(PhotoViewPostDetailActivity.this)
                        .load(mPostDetailBean.getThumbnail_image().get(position).getThumbnail_image())
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
                        mPostDetailBean.getThumbnail_image().size());
                mTv_image_real.setText("查看原图("+
                        ConvertUtils.byte2FitMemorySize( mPostDetailBean.getPosts_reset_image()
                                .get(position).getPosts_image_size())
                       +")");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mPager.setCurrentItem(getIntent().getIntExtra("position", 0));
        mTv_image_real.setText("查看原图("+mPostDetailBean.getPosts_reset_image()
                .get(getIntent().getIntExtra("position", 0)).getPosts_image_size()+")");
    }

    private void initData() {
        JSONObject jo = null;
        try {
            jo = new JSONObject(getIntent().getStringExtra("postDetailBean"));
            JSONObject ja = jo.getJSONObject("data");
            Gson gson = new Gson();
            mPostDetailBean = gson.fromJson(ja.toString(), PostDetailBean.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void downloadImage() {
        Toast.makeText(PhotoViewPostDetailActivity.this, "下载开始", Toast.LENGTH_SHORT).show();
        OkHttpUtils//
                .get()//
                .url( mPostDetailBean.getThumbnail_image()
                        .get(mPosition).getThumbnail_image())//
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
                        Toast.makeText(PhotoViewPostDetailActivity.this, "下载出错", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(File response, int id) {

                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        Toast.makeText(PhotoViewPostDetailActivity.this, "下载完成，目录:" + FileUtils.imageSavePath, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
