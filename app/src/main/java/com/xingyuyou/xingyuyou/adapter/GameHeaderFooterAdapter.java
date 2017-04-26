package com.xingyuyou.xingyuyou.adapter;

import android.app.Activity;
import android.content.Context;
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
import com.xingyuyou.xingyuyou.bean.hotgame.HotGameBean;

import java.util.List;

/**
 * Created by Administrator on 2017/4/17.
 */

public class GameHeaderFooterAdapter extends RecyclerView.Adapter {
    //数据
    private List<HotGameBean> mListData;
    private Activity mActivity;

    public static final int TYPE_HEADER = 0;  //说明是带有Header的
    public static final int TYPE_FOOTER = 1;  //说明是带有Footer的
    public static final int TYPE_NORMAL = 2;  //说明是不带有header和footer的

    //HeaderView, FooterView
    private View mHeaderView;
    private View mFooterView;

    public GameHeaderFooterAdapter(Activity activity, List<HotGameBean> listData) {
        mListData = listData;
        mActivity=activity;
        Log.e("test111",mListData.toString());
    }

    //HeaderView和FooterView的get和set函数
    public View getHeaderView() {
        return mHeaderView;
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public View getFooterView() {
        return mFooterView;
    }

    public void setFooterView(View footerView) {
        mFooterView = footerView;
        notifyItemInserted(getItemCount() - 1);
    }



    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null && mFooterView == null) {
            return TYPE_NORMAL;
        }
        if (position == 0) {
            //第一个item应该加载Header
            return TYPE_HEADER;
        }
        if (position == getItemCount() - 1) {
            //最后一个,应该加载Footer
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER) {
            return new ItemViewHolder(mHeaderView);
        }
        if (mFooterView != null && viewType == TYPE_FOOTER) {
            return new ItemViewHolder(mFooterView);
        }
        View layout = LayoutInflater.from(mActivity).inflate(R.layout.item_game_cover_list, parent, false);
        return new ItemViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(getItemViewType(position) == TYPE_NORMAL){
            if(holder instanceof ItemViewHolder) {
                ((ItemViewHolder) holder).mGameName.setText(mListData.get(position-1).getGame_name());
                ((ItemViewHolder) holder).mGameFeature.setText(mListData.get(position-1).getFeatures());
                Glide.with(mActivity)
                        .load(mListData.get(position-1).getIcon())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontAnimate()
                        .into(((ItemViewHolder) holder).mGamePic);
                Glide.with(mActivity)
                        .load(mListData.get(position-1).getCover())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontAnimate()
                        .placeholder(R.drawable.shape_rectangle_cover)

                        .into(((ItemViewHolder) holder).mGameCover);

                ((ItemViewHolder) holder).mGameCover.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mActivity,HotGameDetailActivity.class);
                        intent.putExtra("game_id",mListData.get(position-1).getId());
                        intent.putExtra("game_name",mListData.get(position-1).getGame_name());
                        mActivity.startActivity(intent);
                    }
                });
                return;
            }
            return;
        }else if(getItemViewType(position) == TYPE_HEADER){
            return;
        }else{
            return;
        }

    }

    @Override
    public int getItemCount() {
        if (mHeaderView == null && mFooterView == null) {
            return mListData.size();
        } else if (mHeaderView == null && mFooterView != null) {
            return mListData.size() + 1;
        } else if (mHeaderView != null && mFooterView == null) {
            return mListData.size() + 1;
        } else {
            return mListData.size() + 2;
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private  ImageView mGamePic;
        private  ImageView mGameCover;
        private  TextView mGameName;
        private  TextView mGameFeature;

        public ItemViewHolder(View itemView) {
            super(itemView);
            //如果是headerview或者是footerview,直接返回
            if (itemView == mHeaderView) {
                return;
            }
            if (itemView == mFooterView) {
                return;
            }
            mGamePic = (ImageView) itemView.findViewById(R.id.iv_game_pic);
            mGameCover = (ImageView) itemView.findViewById(R.id.iv_game_cover);
            mGameName = (TextView) itemView.findViewById(R.id.tv_game_name);
            mGameFeature = (TextView) itemView.findViewById(R.id.tv_game_feature);
        }
    }
}
