package com.xingyuyou.xingyuyou.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.activity.HotGameDetailActivity;
import com.xingyuyou.xingyuyou.activity.PostClassListActivity;
import com.xingyuyou.xingyuyou.activity.PostDetailActivity;
import com.xingyuyou.xingyuyou.bean.community.LabelClassBean;
import com.xingyuyou.xingyuyou.bean.hotgame.HotGameBean;

import java.util.List;

/**
 * Created by Administrator on 2017/4/17.
 */

public class CommHeaderFooterAdapter extends RecyclerView.Adapter {
    //数据
    private List<LabelClassBean> mListData;
    private Activity mActivity;

    public CommHeaderFooterAdapter(Activity activity, List<LabelClassBean> listData) {
        mListData = listData;
        mActivity=activity;
    }

    public void setDatas(List<LabelClassBean> listData) {
        mListData = listData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(mActivity).inflate(R.layout.item_community_list, parent, false);
        return new ItemViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).mGameName.setText(mListData.get(position).getClass_name());
            Glide.with(mActivity)
                    .load(mListData.get(position).getClass_image())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(((ItemViewHolder) holder).mGamePic);
            ((ItemViewHolder) holder).mGamePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mActivity,PostClassListActivity.class);
                    intent.putExtra("list_id",mListData.get(position).getId());
                    intent.putExtra("class_image",mListData.get(position).getClass_image());
                    intent.putExtra("describe",mListData.get(position).getDescribe());
                    intent.putExtra("posts_num",mListData.get(position).getPosts_num());
                    mActivity.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
            return mListData != null ? mListData.size() : 0;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private  ImageView mGamePic;
        private  TextView mGameName;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mGameName = (TextView) itemView.findViewById(R.id.tv_class_name);
            mGamePic = (ImageView) itemView.findViewById(R.id.iv_class_image);
        }
    }
}
