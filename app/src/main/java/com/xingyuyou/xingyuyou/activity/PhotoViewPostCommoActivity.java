package com.xingyuyou.xingyuyou.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.google.gson.reflect.TypeToken;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.ConvertUtils;
import com.xingyuyou.xingyuyou.Utils.FileUtils;
import com.xingyuyou.xingyuyou.bean.community.PostCommoBean;
import com.xingyuyou.xingyuyou.bean.community.PostDetailBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class PhotoViewPostCommoActivity extends AppCompatActivity {

    private ViewPager mPager;
    private TextView mTv_image_number;
    private List<PostCommoBean> mCommoList = new ArrayList<>();
    private List<String> mCommoThumbImageList = new ArrayList<>();
    private List<String> mCommoImageList = new ArrayList<>();
    private List<Integer> mCommoImageSizeList = new ArrayList<>();
    private ImageView mIv_save_image;
    private int mPosition = 0;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };
    private PostCommoBean.ThumbnailImageBean mPostDetailBean;
    private int mLastIndexOf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        //获取信息
        initData();
        mTv_image_number = (TextView) findViewById(R.id.tv_image_number);

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
        mPager.setAdapter(pagerAdapter);
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPosition = position;
                mTv_image_number.setText((position + 1)
                        + "/" +
                        mCommoThumbImageList.size());

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mPager.setCurrentItem(mLastIndexOf);

    }

    private void initData() {
        JSONObject jo = null;
        try {
            jo = new JSONObject(getIntent().getStringExtra("postDetailCommoBean"));
            JSONArray ja = jo.getJSONArray("data");
            Gson gson = new Gson();
            mCommoList = gson.fromJson(ja.toString(),
                    new TypeToken<List<PostCommoBean>>() {
                    }.getType());
            for (int i = 0; i < mCommoList.size(); i++) {
                if (mCommoList.get(i).getThumbnail_image() != null && mCommoList.get(i).getThumbnail_image().size() != 0) {
                    for (int j = 0; j < mCommoList.get(i).getThumbnail_image().size(); j++) {
                        mCommoImageList.add(mCommoList.get(i).getImgarr().get(j).getPosts_image());
                        mCommoImageSizeList.add(mCommoList.get(i).getImgarr().get(j).getPosts_image_size());
                        mCommoThumbImageList.add(mCommoList.get(i).getThumbnail_image().get(j).getThumbnail_image());
                    }
                }
            }
           // Log.e("postiii", "position_i:"+getIntent().getIntExtra("position_i",0)+"position_j"+getIntent().getIntExtra("position_j",0));
            mLastIndexOf = mCommoThumbImageList.lastIndexOf(mCommoList.get(getIntent().getIntExtra("position_i",0))
                   .getThumbnail_image().get(getIntent().getIntExtra("position_j",0)).getThumbnail_image());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void downloadImage() {
        Toast.makeText(PhotoViewPostCommoActivity.this, "下载开始", Toast.LENGTH_SHORT).show();
        OkHttpUtils//
                .get()//
                .url(mCommoThumbImageList.get(mPosition))//
                .build()//
                .execute(new FileCallBack(FileUtils.imageSavePath, mCommoThumbImageList.get(mPosition).substring(
                        mCommoThumbImageList.get(mPosition).length() - 15,
                        mCommoThumbImageList.get(mPosition).length()))//
                {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(PhotoViewPostCommoActivity.this, "下载出错", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(File response, int id) {

                    }

                    @Override
                    public void inProgress(final float progress, long total, int id) {
                        super.inProgress(progress, total, id);

                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        Toast.makeText(PhotoViewPostCommoActivity.this, "下载完成，目录:" + FileUtils.imageSavePath, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    PagerAdapter pagerAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return mCommoThumbImageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = LayoutInflater.from(PhotoViewPostCommoActivity.this).inflate(R.layout.item_photo_view_pager, null);
            final PhotoView photoView = (PhotoView) view.findViewById(R.id.PhotoView);
            final TextView mTv_image_real = (TextView) view.findViewById(R.id.tv_image_real);
            photoView.enable();
            photoView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ViewTarget<PhotoView, GlideDrawable> viewTarget = new ViewTarget<PhotoView, GlideDrawable>(photoView) {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    this.view.setImageDrawable(resource.getCurrent());
                }
            };
            Glide.with(PhotoViewPostCommoActivity.this)
                    .load(mCommoThumbImageList.get(position))
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(viewTarget);


            mTv_image_real.setText("查看原图(" +
                    ConvertUtils.byte2FitMemorySize(mCommoImageSizeList.get(position))
                    + ")");
            mTv_image_real.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    downloadImage(position,view,photoView);
                }
            });
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    };

   private void downloadImage(final int position, final View view, final PhotoView photoView) {
        OkHttpUtils//
                .get()//
                .url(mCommoImageList.get(position))//
                .build()//
                .execute(new FileCallBack(FileUtils.imageSavePath, mCommoImageList.get(position).substring(
                        mCommoImageList.get(position).length() - 15,
                        mCommoImageList.get(position).length()))//
                {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(PhotoViewPostCommoActivity.this, "下载出错", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(File response, int id) {

                    }

                    @Override
                    public void inProgress(final float progress, long total, int id) {
                        super.inProgress(progress, total, id);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                ((TextView)view).setText(((int)(progress*100))+"%");
                            }
                        });
                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        ViewTarget<PhotoView, GlideDrawable> viewTarget = new ViewTarget<PhotoView, GlideDrawable>(photoView) {
                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                this.view.setImageDrawable(resource.getCurrent());
                            }
                        };
                        Glide.with(PhotoViewPostCommoActivity.this)
                                .load(FileUtils.imageSavePath+ mCommoImageList.get(position).substring(
                                        mCommoImageList.get(position).length() - 15,
                                        mCommoImageList.get(position).length()))
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .into(viewTarget);
                        view.setVisibility(View.INVISIBLE);
                    }
                });
    }
}
