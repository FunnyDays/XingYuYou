package com.xingyuyou.xingyuyou.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.xingyuyou.xingyuyou.R;


import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/22.
 */
public class GameDetailPicAdapter extends RecyclerView.Adapter {
    private Activity mActivity;
    private ArrayList<String> arrayList;


    public GameDetailPicAdapter(Activity mActivity, ArrayList<String> arrayList) {
        this.mActivity = mActivity;
        this.arrayList = arrayList;
    }

    /**
     * ItemClick的回调接口
     */
    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public int getItemViewType(int position) {
        final int[] type = new int[]{1};
        if (arrayList.size() > 1) {
            Glide.with(mActivity).load(arrayList.get(0)).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    int width = resource.getWidth();
                    int height = resource.getHeight();
                    if (width > height) {
                        type[0] = 0;
                    }else {
                        type[0] = 1;
                    }
                }
            });
        }
        return type[0];
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
      /*  switch (viewType) {
            case 0:
                view = LayoutInflater.from(mActivity).inflate(R.layout.item_viewpager_image_horizontal, parent, false);
                return new ItemViewHolder(view);
           *//* case 1:
                view = LayoutInflater.from(mActivity).inflate(R.layout.item_viewpager_image_vertical, parent, false);
                return new ItemViewHolder(view);*//*
        }*/
        view = LayoutInflater.from(mActivity).inflate(R.layout.item_viewpager_image_horizontal, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (arrayList.size() == 0) {

        } else if (holder instanceof ItemViewHolder) {
            Glide.with(mActivity)
                    .load(arrayList.get(position))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(((ItemViewHolder) holder).imageView);

        }
        //如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            ((ItemViewHolder) holder).imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(((ItemViewHolder) holder).imageView, position);

                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return arrayList==null?0:arrayList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
        }
    }
}
