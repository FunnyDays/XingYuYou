package com.xingyuyou.xingyuyou.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
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
import com.xingyuyou.xingyuyou.Utils.glide.GlideRoundTransform;
import com.xingyuyou.xingyuyou.activity.HotGameDetailActivity;
import com.xingyuyou.xingyuyou.activity.SortGameListActivity;
import com.xingyuyou.xingyuyou.bean.hotgame.HotGameBean;
import com.xingyuyou.xingyuyou.bean.sort.GameSortBean;

import java.util.List;

/**
 * Created by Administrator on 2017/4/17.
 */

public class FenLeiGameAdapter extends RecyclerView.Adapter {
    //数据
    private List<GameSortBean> mListData;
    private Activity mActivity;

    public static final int TYPE_HEADER = 0;  //说明是带有Header的
    public static final int TYPE_FOOTER = 1;  //说明是带有Footer的
    public static final int TYPE_NORMAL = 2;  //说明是不带有header和footer的

    //HeaderView, FooterView
    private View mHeaderView;
    private View mFooterView;

    public FenLeiGameAdapter(Activity activity, List<GameSortBean> listData) {
        mListData = listData;
        mActivity=activity;
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
        View layout = LayoutInflater.from(mActivity).inflate(R.layout.item_sort_game_list_with_cover, parent, false);
        return new ItemViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(getItemViewType(position) == TYPE_NORMAL){
            if(holder instanceof ItemViewHolder) {
                ((ItemViewHolder) holder).mSortGameName.setText(mListData.get(position-1).getType_name());
                ((ItemViewHolder) holder).mSortGameIntro.setText(mListData.get(position-1).getIntroduce());
                ((ItemViewHolder) holder).mGameNum.setText("下载量："+mListData.get(position-1).getDownload_num());
                ((ItemViewHolder) holder).mGameHot.setText("热度："+mListData.get(position-1).getHot_num());
                Glide.with(mActivity)
                        .load(mListData.get(position-1).getIcon())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)

                        .into(((ItemViewHolder) holder).mSortGamePic);
                ((ItemViewHolder) holder).mItemOnclick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mActivity,SortGameListActivity.class);
                        intent.putExtra("type_id",mListData.get(position-1).getId());
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

        private  ImageView mSortGamePic;
        private  TextView mSortGameName;
        private  TextView mSortGameIntro;
        private  TextView mGameNum;
        private  TextView mGameHot;
        private  CardView mItemOnclick;

        public ItemViewHolder(View itemView) {
            super(itemView);
            if (itemView == mHeaderView) {
                return;
            }
            if (itemView == mFooterView) {
                return;
            }
            mSortGamePic = (ImageView) itemView.findViewById(R.id.iv_sort_game_pic);
            mSortGameName = (TextView) itemView.findViewById(R.id.tv_sort_game_name);
            mSortGameIntro = (TextView) itemView.findViewById(R.id.tv_sort_game_intro);
            mGameNum = (TextView) itemView.findViewById(R.id.tv_game_num);
            mGameHot = (TextView) itemView.findViewById(R.id.tv_game_hot);
            mItemOnclick = (CardView) itemView.findViewById(R.id.cv_item_onclick);
        }
    }
}
