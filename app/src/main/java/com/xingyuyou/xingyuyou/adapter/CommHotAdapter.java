package com.xingyuyou.xingyuyou.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.TimeUtils;
import com.xingyuyou.xingyuyou.activity.HotGameDetailActivity;
import com.xingyuyou.xingyuyou.activity.PostDetailActivity;
import com.xingyuyou.xingyuyou.bean.community.LabelClassBean;
import com.xingyuyou.xingyuyou.bean.community.PostBean;
import com.xingyuyou.xingyuyou.bean.hotgame.HotGameBean;

import java.util.List;

/**
 * Created by Administrator on 2017/4/21.
 */
public class CommHotAdapter extends RecyclerView.Adapter {
    //数据
    private List<PostBean> mListData;
    private Activity mActivity;

    public static final int TYPE_HEADER = 0;  //说明是带有Header的
    public static final int TYPE_FOOTER = 1;  //说明是带有Footer的
    public static final int TYPE_NORMAL = 2;  //说明是不带有header和footer的

    //HeaderView, FooterView
    private View mHeaderView;
    private View mFooterView;

    public CommHotAdapter(Activity activity, List<PostBean> listData) {
        mListData = listData;
        mActivity=activity;
    }
    public void setDatas(List<PostBean> listData) {
        mListData = listData;
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
        View layout = LayoutInflater.from(mActivity).inflate(R.layout.item_comm_list, parent, false);
        return new ItemViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(getItemViewType(position) == TYPE_NORMAL){
            if(holder instanceof ItemViewHolder) {
                ((ItemViewHolder) holder).mUserName.setText(mListData.get(position-1).getNickname());
                ((ItemViewHolder) holder).mPostTime.setText(TimeUtils.getFriendlyTimeSpanByNow(Long.parseLong(mListData.get(position-1).getDateline()+"000")));
                ((ItemViewHolder) holder).mPostName.setText(mListData.get(position-1).getSubject());
                ((ItemViewHolder) holder).mPostContent.setText(mListData.get(position-1).getMessage());
                ((ItemViewHolder) holder).mCollectNum.setText(mListData.get(position-1).getPosts_collect());
                ((ItemViewHolder) holder).mCommNum.setText(mListData.get(position-1).getPosts_forums());
                ((ItemViewHolder) holder).mJiaoNangNum.setText(mListData.get(position-1).getPosts_laud());

                Glide.with(mActivity)
                        .load(mListData.get(position-1).getPosts_image())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(((ItemViewHolder) holder).mPostCover0);
                ((ItemViewHolder) holder).mLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mActivity,PostDetailActivity.class);
                        intent.putExtra("post_id",mListData.get(position-1).getId());
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

        private ImageView mPostCover0;
        private ImageView mPostCover1;
        private ImageView mPostCover2;
        private TextView mPostName;
        private TextView mCollectNum;
        private TextView mCommNum;
        private TextView mJiaoNangNum;
        private TextView mPostContent;
        private TextView mUserName;
        private TextView mPostTime;
        private LinearLayout mLinearLayout;

        public ItemViewHolder(View itemView) {
            super(itemView);
            //如果是headerview或者是footerview,直接返回
            if (itemView == mHeaderView) {
                return;
            }
            if (itemView == mFooterView) {
                return;
            }
            mPostCover0 = (ImageView) itemView.findViewById(R.id.iv_post_cover0);
            mPostCover1 = (ImageView) itemView.findViewById(R.id.iv_post_cover1);
            mPostCover2 = (ImageView) itemView.findViewById(R.id.iv_post_cover2);
            mPostName = (TextView) itemView.findViewById(R.id.tv_post_name);
            mPostContent = (TextView) itemView.findViewById(R.id.tv_post_content);
            mUserName = (TextView) itemView.findViewById(R.id.tv_user_name);
            mPostTime = (TextView) itemView.findViewById(R.id.tv_post_time);
            mCollectNum = (TextView) itemView.findViewById(R.id.tv_collect_num);
            mCommNum = (TextView) itemView.findViewById(R.id.tv_comm_num);
            mJiaoNangNum = (TextView) itemView.findViewById(R.id.tv_jiaonang_num);
            mLinearLayout = (LinearLayout) itemView.findViewById(R.id.LinearLayout);
        }
    }
}
