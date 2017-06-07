package com.xingyuyou.xingyuyou.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.ConvertUtils;
import com.xingyuyou.xingyuyou.Utils.IntentUtils;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.SoftKeyBoart.SpanStringUtils;
import com.xingyuyou.xingyuyou.Utils.TimeUtils;
import com.xingyuyou.xingyuyou.Utils.glide.GlideCircleTransform;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.activity.LoginActivity;
import com.xingyuyou.xingyuyou.bean.community.PostCommoBean;
import com.xingyuyou.xingyuyou.bean.god.GodCommoBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;

/**
 * Created by 24002 on 2017/5/23.
 */

public class PostCommoListAdapter extends BaseAdapter {
    private Activity mActivity;
    private List<PostCommoBean> mCommoBeanList;
    private String Uid = "";

    public PostCommoListAdapter(Activity activity, List<PostCommoBean> commoBeanList) {
        this.mActivity = activity;
        this.mCommoBeanList = commoBeanList;
    }

    public void setUid(String uid) {
        this.Uid = uid;
    }

    @Override
    public int getCount() {
        return mCommoBeanList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = View.inflate(mActivity, R.layout.item_commo_post_list, null);
            holder.iv_user_photo = (ImageView) view.findViewById(R.id.iv_user_photo);
            holder.iv_zan = (ImageView) view.findViewById(R.id.iv_love);
            holder.tv_user_name = (TextView) view.findViewById(R.id.tv_user_name);
            holder.tv_louzhu = (TextView) view.findViewById(R.id.tv_louzhu);
            holder.tv_zan_num = (TextView) view.findViewById(R.id.tv_love_num);
            holder.tv_reply_content = (TextView) view.findViewById(R.id.tv_commo_content);
            holder.tv_commo_time = (TextView) view.findViewById(R.id.tv_post_time);
            holder.tv_floor_num = (TextView) view.findViewById(R.id.tv_floor_num);
            holder.tv_commo2_name = (TextView) view.findViewById(R.id.tv_commo2_name);
            holder.tv_commo2_content = (TextView) view.findViewById(R.id.tv_commo2_content);
            holder.tv_commo2_time = (TextView) view.findViewById(R.id.tv_commo2_time);
            holder.ll_root_image_item = (LinearLayout) view.findViewById(R.id.ll_root_image_item);
            //更多评论
            holder.ll_more_commo_item = (LinearLayout) view.findViewById(R.id.ll_more_commo_item);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (Uid.equals(mCommoBeanList.get(i).getUid())) {
            holder.tv_louzhu.setVisibility(View.VISIBLE);
        } else {
            holder.tv_louzhu.setVisibility(View.GONE);
        }
        holder.tv_floor_num.setText(mCommoBeanList.get(i).getFloor_num() == null ? "正在抢楼" : mCommoBeanList.get(i).getFloor_num() + "楼");
        holder.tv_user_name.setText(mCommoBeanList.get(i).getNickname());
        holder.tv_zan_num.setText(mCommoBeanList.get(i).getLaud_count());
        holder.tv_reply_content.setText(SpanStringUtils.getEmotionContent(mActivity, holder.tv_reply_content, mCommoBeanList.get(i).getReplies_content()));
        holder.tv_commo_time.setText(TimeUtils.getFriendlyTimeSpanByNow(Long.parseLong(mCommoBeanList.get(i).getDateline() + "000")));
        Glide.with(mActivity)
                .load(mCommoBeanList.get(i).getHead_image())
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .transform(new GlideCircleTransform(mActivity))
                .into(holder.iv_user_photo);
        holder.ll_root_image_item.removeAllViews();
        Log.e("post", "getView: "+mCommoBeanList.get(i).toString() );
        if (!(mCommoBeanList.get(i).getImgarr() == null) &&mCommoBeanList.get(i).getImgarr().size()>=1&& !mCommoBeanList.get(i).getImgarr().get(0).toString().equals("")) {
            for (int j = 0; j < mCommoBeanList.get(i).getImgarr().size(); j++) {
                ImageView imageView = new ImageView(mActivity);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(ConvertUtils.dp2px(0), ConvertUtils.dp2px(5), ConvertUtils.dp2px(0), ConvertUtils.dp2px(20));
                imageView.setLayoutParams(lp);
                imageView.setAdjustViewBounds(true);
                Glide.with(mActivity)
                        .load(mCommoBeanList.get(i).getImgarr().get(j))
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(imageView);
                holder.ll_root_image_item.addView(imageView);
            }
        }
        //点赞
        if (mCommoBeanList.get(i).getLaud_status() != null && mCommoBeanList.get(i).getLaud_status().equals("1")) {
            holder.iv_zan.setImageResource(R.mipmap.ic_zan_fill);
        } else {
            holder.iv_zan.setImageResource(R.drawable.ic_zan);
        }
        holder.iv_zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!UserUtils.logined()) {
                    IntentUtils.startActivity(mActivity, LoginActivity.class);
                    return;
                }
                getLuad(mCommoBeanList.get(i).getId());
                if (mCommoBeanList.get(i).getLaud_status().equals("1")) {
                    holder.tv_zan_num.setText(String.valueOf((Integer.parseInt(mCommoBeanList.get(i).getLaud_status()) - 1)));
                    mCommoBeanList.get(i).setLaud_status(String.valueOf((Integer.parseInt(mCommoBeanList.get(i).getLaud_status()) - 1)));
                    mCommoBeanList.get(i).setLaud_status("0");
                    Toast.makeText(mActivity, "取消点赞", Toast.LENGTH_SHORT).show();
                    holder.iv_zan.setImageResource(R.drawable.ic_zan);
                } else {
                    holder.tv_zan_num.setText(String.valueOf((Integer.parseInt(mCommoBeanList.get(i).getLaud_status()) + 1)));
                    mCommoBeanList.get(i).setLaud_status(String.valueOf((Integer.parseInt(mCommoBeanList.get(i).getLaud_status()) + 1)));
                    mCommoBeanList.get(i).setLaud_status("1");
                    Toast.makeText(mActivity, "点赞", Toast.LENGTH_SHORT).show();
                    holder.iv_zan.setImageResource(R.mipmap.ic_zan_fill);
                }
            }
        });
        //更多评论
        if (mCommoBeanList.get(i).getChild() != null) {
        }
        if (mCommoBeanList.get(i).getChild() != null && mCommoBeanList.get(i).getChild().size() > 0) {
            holder.ll_more_commo_item.setVisibility(View.VISIBLE);
            holder.tv_commo2_name
                    .setText(mCommoBeanList.get(i).getChild().get(0).getNickname() + ":");
            holder.tv_commo2_content
                    .setText(SpanStringUtils.getEmotionContent(mActivity, holder.tv_commo2_content, mCommoBeanList.get(i).getChild().get(0).getReplies_content()));
            holder.tv_commo2_time
                    .setText(TimeUtils.getFriendlyTimeSpanByNow(Long.parseLong(mCommoBeanList.get(i).getChild().get(0).getDateline() + "000")));
        } else {
            holder.ll_more_commo_item.setVisibility(View.GONE);
        }
        return view;
    }

    /*存放控件 的ViewHolder*/
    public final class ViewHolder {
        public TextView tv_user_name;
        public TextView tv_commo2_name;
        public TextView tv_commo2_content;
        public TextView tv_commo2_time;
        public TextView tv_louzhu;
        public TextView tv_zan_num;
        public TextView tv_reply_content;
        public TextView tv_commo_time;
        public ImageView iv_user_photo;
        public ImageView iv_zan;
        public TextView tv_floor_num;
        public LinearLayout ll_root_image_item;
        public LinearLayout ll_more_commo_item;
    }

    private void getLuad(String replies_id) {
        OkHttpUtils.post()//
                .addParams("replies_id", replies_id)
                .addParams("uid", UserUtils.getUserId())
                .url(XingYuInterface.REPLIES_LAUD)
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

}
