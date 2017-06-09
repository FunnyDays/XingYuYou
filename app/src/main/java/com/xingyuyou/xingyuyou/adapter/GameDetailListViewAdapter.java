package com.xingyuyou.xingyuyou.adapter;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
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
import com.xingyuyou.xingyuyou.bean.hotgame.GameDetailCommoBean;
import com.xingyuyou.xingyuyou.weight.CollapsedTextView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;

/**
 * Created by 24002 on 2017/5/18.
 */

public class GameDetailListViewAdapter extends BaseAdapter {
    private Activity mActivity;
    private List<GameDetailCommoBean> mList;
    private boolean mState = false;
    public GameDetailListViewAdapter(Activity activity, List<GameDetailCommoBean> list) {
        mActivity = activity;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
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
        if(view == null)
        {
            holder = new ViewHolder();
            view = View.inflate(mActivity,R.layout.item_game_commo_list, null);
            holder.iv_user_photo = (ImageView)view.findViewById(R.id.iv_user_photo);
            holder.iv_zan = (ImageView)view.findViewById(R.id.iv_zan);
            holder.iv_game_intro_more = (ImageView)view.findViewById(R.id.iv_game_intro_more);
            holder.tv_user_name = (TextView)view.findViewById(R.id.tv_user_name);
            holder.tv_zan_num = (TextView)view.findViewById(R.id.tv_zan_num);
            holder.tv_reply_content = (TextView)view.findViewById(R.id.tv_reply_content);
            holder.tv_commo_time = (TextView)view.findViewById(R.id.tv_commo_time);
            holder.rb_score = (RatingBar)view.findViewById(R.id.rb_score);
            view.setTag(holder);
        }else
        {
            holder = (ViewHolder)view.getTag();
        }
        holder.tv_user_name.setText(mList.get(i).getNickname());
        holder.tv_zan_num.setText(mList.get(i).getLaud_num());
        holder.tv_reply_content.setText(mList.get(i).getComment());
        holder.tv_commo_time.setText(TimeUtils.getFriendlyTimeSpanByNow(Long.parseLong(mList.get(i).getDateline() + "000")));
        Glide.with(mActivity)
                .load(mList.get(i).getHead_image())
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .transform(new GlideCircleTransform(mActivity))
                .into(holder.iv_user_photo);
        holder.rb_score.setRating(Integer.parseInt(mList.get(i).getStar_num()));
        //点赞
        if (mList.get(i).getLaud_status() == 1) {
            holder.iv_zan.setImageResource(R.drawable.ic_zan_fill);
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
                getLuad(mList.get(i).getId());
                if (mList.get(i).getLaud_status() == 1) {
                    holder.tv_zan_num.setText(String.valueOf((Integer.parseInt(mList.get(i).getLaud_num()) - 1)));
                    mList.get(i).setLaud_num(String.valueOf((Integer.parseInt(mList.get(i).getLaud_num()) - 1)));
                    mList.get(i).setLaud_status(0);
                    Toast.makeText(mActivity, "取消点赞", Toast.LENGTH_SHORT).show();
                    holder.iv_zan.setImageResource(R.drawable.ic_zan);
                } else {
                    holder.tv_zan_num.setText(String.valueOf((Integer.parseInt(mList.get(i).getLaud_num()) + 1)));
                    mList.get(i).setLaud_num(String.valueOf((Integer.parseInt(mList.get(i).getLaud_num()) + 1)));
                    mList.get(i).setLaud_status(1);
                    Toast.makeText(mActivity, "点赞", Toast.LENGTH_SHORT).show();
                    holder.iv_zan.setImageResource(R.drawable.ic_zan_fill);
                }
            }
        });

        //介绍文字动画控制
        holder.iv_game_intro_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mState) {
                    mState = false;
                    holder.tv_reply_content.setEllipsize(TextUtils.TruncateAt.END);
                    holder.tv_reply_content.setMaxLines(2);
                    ObjectAnimator animator3 = ObjectAnimator.ofFloat(holder.iv_game_intro_more, "rotation", 180f, 360f);
                    animator3.setDuration(500).start();
                } else {
                    mState = true;
                    holder.tv_reply_content.setEllipsize(null);
                    holder.tv_reply_content.setMaxLines(Integer.MAX_VALUE);
                    ObjectAnimator animator3 = ObjectAnimator.ofFloat(holder.iv_game_intro_more, "rotation", 0f, 180f);
                    animator3.setDuration(500).start();
                }
            }
        });
        holder.tv_reply_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mState) {
                    mState = false;
                    holder.tv_reply_content.setEllipsize(TextUtils.TruncateAt.END);
                    holder.tv_reply_content.setMaxLines(2);
                    ObjectAnimator animator3 = ObjectAnimator.ofFloat(holder.iv_game_intro_more, "rotation", 180f, 360f);
                    animator3.setDuration(500).start();
                } else {
                    mState = true;
                    holder.tv_reply_content.setEllipsize(null);
                    holder.tv_reply_content.setMaxLines(Integer.MAX_VALUE);
                    ObjectAnimator animator3 = ObjectAnimator.ofFloat(holder.iv_game_intro_more, "rotation", 0f, 180f);
                    animator3.setDuration(500).start();
                }
            }
        });
        return view;
    }
    /*存放控件 的ViewHolder*/
    public final class ViewHolder {
        public TextView tv_user_name;
        public TextView tv_zan_num;
        public TextView tv_reply_content;
        public TextView tv_commo_time;
        public ImageView iv_user_photo;
        public ImageView iv_zan;
        public ImageView iv_game_intro_more;
        public RatingBar rb_score;
    }
    private void getLuad(String eid) {
        OkHttpUtils.post()//
                .addParams("eid",eid)
                .addParams("uid",UserUtils.getUserId())
                .url(XingYuInterface.GET_EVALUATE_LAUD)
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
