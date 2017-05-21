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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.IntentUtils;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.TimeUtils;
import com.xingyuyou.xingyuyou.Utils.glide.GlideCircleTransform;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.activity.LoginActivity;
import com.xingyuyou.xingyuyou.activity.PostDetailActivity;
import com.xingyuyou.xingyuyou.bean.community.PostListBean;
import com.xingyuyou.xingyuyou.bean.community.SortPostListBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/4/21.
 */
public class CommHotAdapter extends RecyclerView.Adapter {
    //数据
    private List<PostListBean> mListData;
    private Activity mActivity;

    public static final int TYPE_HEADER = 0;  //说明是带有Header的
    public static final int TYPE_FOOTER = 1;  //说明是带有Footer的
    public static final int TYPE_NORMAL = 2;  //说明是不带有header和footer的
    public static final int TYPE_ONE_PIC = 3;  //一张图
    public static final int TYPE_TWO_PIC = 4;  //二张图
    public static final int TYPE_THREE_PIC = 5;  //三张图

    //HeaderView, FooterView
    private View mHeaderView;
    private View mFooterView;

    public CommHotAdapter(Activity activity, List<PostListBean> listData) {
        mListData = listData;
        mActivity = activity;
    }

    public void setDatas(List<PostListBean> listData) {
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

        if (mListData.get(position - 1).getPosts_image().size() == 1) {
            return TYPE_ONE_PIC;
        }
        if (mListData.get(position - 1).getPosts_image().size() == 2) {
            return TYPE_TWO_PIC;
        }
        if (mListData.get(position - 1).getPosts_image().size() >= 3) {
            return TYPE_THREE_PIC;
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
        switch (viewType) {
            case 3:
                View layout = LayoutInflater.from(mActivity).inflate(R.layout.item_comm_list, parent, false);
                return new ItemViewHolder(layout);
            case 4:
                View layout1 = LayoutInflater.from(mActivity).inflate(R.layout.item_comm_list1, parent, false);
                return new ItemViewHolder(layout1);
            case 5:
                View layout2 = LayoutInflater.from(mActivity).inflate(R.layout.item_comm_list2, parent, false);
                return new ItemViewHolder(layout2);
        }
        return null;
    }

    @Override

    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ItemViewHolder && (getItemViewType(position) == TYPE_ONE_PIC
                || getItemViewType(position) == TYPE_TWO_PIC
                || getItemViewType(position) == TYPE_THREE_PIC)) {

            if (mListData.get(position - 1).getCollect_status()==1){
                ((ItemViewHolder) holder).mPostCollect.setImageResource(R.mipmap.ic_collect_fill);
            }else {
                ((ItemViewHolder) holder).mPostCollect.setImageResource(R.mipmap.shoucang);
            }
            if (mListData.get(position - 1).getLaud_status()==1){
                ((ItemViewHolder) holder).mPostLuad.setImageResource(R.mipmap.ic_zan_fill);
            }else {
                ((ItemViewHolder) holder).mPostLuad.setImageResource(R.mipmap.ic_zan);
            }
            ((ItemViewHolder) holder).mUserName.setText(mListData.get(position - 1).getNickname());
            ((ItemViewHolder) holder).mPostTime.setText(TimeUtils.getFriendlyTimeSpanByNow(Long.parseLong(mListData.get(position - 1).getDateline() + "000")));
            ((ItemViewHolder) holder).mPostName.setText(mListData.get(position - 1).getSubject());
            ((ItemViewHolder) holder).mPostContent.setText(mListData.get(position - 1).getMessage());
            ((ItemViewHolder) holder).mCollectNum.setText(mListData.get(position - 1).getPosts_collect());
            ((ItemViewHolder) holder).mCommNum.setText(mListData.get(position - 1).getPosts_forums());
            ((ItemViewHolder) holder).mJiaoNangNum.setText(mListData.get(position - 1).getPosts_laud());
            Glide.with(mActivity)
                    .load(mListData.get(position - 1).getHead_image())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transform(new GlideCircleTransform(mActivity))
                    .into(((ItemViewHolder) holder).mUserPhoto);
            Glide.with(mActivity)
                    .load(mListData.get(position - 1).getPosts_image().get(0))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(((ItemViewHolder) holder).mPostCover0);
            if (getItemViewType(position) == TYPE_TWO_PIC || getItemViewType(position) == TYPE_THREE_PIC) {
                Glide.with(mActivity)
                        .load(mListData.get(position - 1).getPosts_image().get(1))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(((ItemViewHolder) holder).mPostCover1);
            }
            if (getItemViewType(position) == TYPE_THREE_PIC) {
                Glide.with(mActivity)
                        .load(mListData.get(position - 1).getPosts_image().get(2))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(((ItemViewHolder) holder).mPostCover2);
            }
            ((ItemViewHolder) holder).mLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mActivity, PostDetailActivity.class);
                    intent.putExtra("post_id", mListData.get(position - 1).getId());
                    mActivity.startActivity(intent);
                }
            });
            ((ItemViewHolder) holder).mRlCollect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!UserUtils.logined()){
                        IntentUtils.startActivity(mActivity, LoginActivity.class);
                        return;
                    }
                    getCollect(mListData.get(position - 1).getId());
                    if (mListData.get(position - 1).getCollect_status()==1){
                        ((ItemViewHolder) holder).mCollectNum.setText(String.valueOf((Integer.parseInt(mListData.get(position - 1).getPosts_collect())-1)));
                        mListData.get(position - 1).setPosts_collect(String.valueOf((Integer.parseInt(mListData.get(position - 1).getPosts_collect())-1)));
                        mListData.get(position - 1).setCollect_status(0);
                        Toast.makeText(mActivity, "取消收藏", Toast.LENGTH_SHORT).show();
                        ((ItemViewHolder) holder).mPostCollect.setImageResource(R.mipmap.shoucang);
                    }else {
                        ((ItemViewHolder) holder).mCollectNum.setText(String.valueOf((Integer.parseInt(mListData.get(position - 1).getPosts_collect())+1)));
                        mListData.get(position - 1).setPosts_collect(String.valueOf((Integer.parseInt(mListData.get(position - 1).getPosts_collect())+1)));
                        mListData.get(position - 1).setCollect_status(1);
                        Toast.makeText(mActivity, "收藏成功", Toast.LENGTH_SHORT).show();
                        ((ItemViewHolder) holder).mPostCollect.setImageResource(R.mipmap.ic_collect_fill);

                    }
                }
            });
            ((ItemViewHolder) holder).mRlComm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mActivity, PostDetailActivity.class);
                    intent.putExtra("post_id", mListData.get(position - 1).getId());
                    mActivity.startActivity(intent);
                }
            });
            ((ItemViewHolder) holder).mRlJiaonang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!UserUtils.logined()){
                        IntentUtils.startActivity(mActivity, LoginActivity.class);
                        return;
                    }
                    getLaud(mListData.get(position - 1).getId());
                    if (mListData.get(position - 1).getLaud_status()==1){
                        ((ItemViewHolder) holder).mJiaoNangNum.setText(String.valueOf((Integer.parseInt(mListData.get(position - 1).getPosts_laud())-1)));
                        mListData.get(position - 1).setPosts_laud(String.valueOf((Integer.parseInt(mListData.get(position - 1).getPosts_laud())-1)));
                        mListData.get(position - 1).setLaud_status(0);
                        Toast.makeText(mActivity, "取消点赞", Toast.LENGTH_SHORT).show();
                        ((ItemViewHolder) holder).mPostLuad.setImageResource(R.mipmap.ic_zan);
                    }else {
                        ((ItemViewHolder) holder).mJiaoNangNum.setText(String.valueOf((Integer.parseInt(mListData.get(position - 1).getPosts_laud())+1)));
                        mListData.get(position - 1).setPosts_laud(String.valueOf((Integer.parseInt(mListData.get(position - 1).getPosts_laud())+1)));
                        mListData.get(position - 1).setLaud_status(1);
                        Toast.makeText(mActivity, "点赞成功", Toast.LENGTH_SHORT).show();
                        ((ItemViewHolder) holder).mPostLuad.setImageResource(R.mipmap.ic_zan_fill);

                    }
                }
            });
        } else if (getItemViewType(position) == TYPE_HEADER) {
            return;
        } else {
            return;
        }

    }
    public void getCollect( String tid) {
        OkHttpUtils.post()//
                .addParams("tid",tid)
                .addParams("uid",UserUtils.getUserId())
                .url(XingYuInterface.GET_COLLECT)
                .tag(this)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        // Log.e("hot", e.toString() + ":e");
                    }
                    @Override
                    public void onResponse(String response, int id) {
                    }
                });

    }
    public void getLaud( String tid) {
        OkHttpUtils.post()//
                .addParams("tid",tid)
                .addParams("uid",UserUtils.getUserId())
                .url(XingYuInterface.GET_LAUD)
                .tag(this)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("hot", e.toString() + ":e");
                    }
                    @Override
                    public void onResponse(String response, int id) {
                    }
                });

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
        private ImageView mPostCollect;
        private ImageView mPostCommo;
        private ImageView mPostLuad;
        private ImageView mUserPhoto;
        private TextView mPostName;
        private TextView mCollectNum;
        private TextView mCommNum;
        private TextView mJiaoNangNum;
        private TextView mPostContent;
        private TextView mUserName;
        private TextView mPostTime;
        private LinearLayout mLinearLayout;
        private RelativeLayout mRlCollect;
        private RelativeLayout mRlComm;
        private RelativeLayout mRlJiaonang;

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
            mPostCollect = (ImageView) itemView.findViewById(R.id.iv_post_collect);
            mPostCommo = (ImageView) itemView.findViewById(R.id.iv_post_comm);
            mPostLuad = (ImageView) itemView.findViewById(R.id.iv_post_jiaonang);
            mUserPhoto = (ImageView) itemView.findViewById(R.id.civ_user_photo);
            mPostName = (TextView) itemView.findViewById(R.id.tv_post_name);
            mPostContent = (TextView) itemView.findViewById(R.id.tv_post_content);
            mUserName = (TextView) itemView.findViewById(R.id.tv_user_name);
            mPostTime = (TextView) itemView.findViewById(R.id.tv_post_time);
            mCollectNum = (TextView) itemView.findViewById(R.id.tv_collect_num);
            mCommNum = (TextView) itemView.findViewById(R.id.tv_comm_num);
            mJiaoNangNum = (TextView) itemView.findViewById(R.id.tv_jiaonang_num);
            mLinearLayout = (LinearLayout) itemView.findViewById(R.id.LinearLayout);
            mRlCollect = (RelativeLayout) itemView.findViewById(R.id.rl_collect);
            mRlComm = (RelativeLayout) itemView.findViewById(R.id.rl_comm);
            mRlJiaonang = (RelativeLayout) itemView.findViewById(R.id.rl_jiaonang);
        }
    }
}
