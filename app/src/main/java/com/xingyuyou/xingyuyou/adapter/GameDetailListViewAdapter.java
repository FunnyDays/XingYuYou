package com.xingyuyou.xingyuyou.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.TimeUtils;
import com.xingyuyou.xingyuyou.Utils.glide.GlideCircleTransform;
import com.xingyuyou.xingyuyou.bean.hotgame.GameDetailCommoBean;

import java.util.List;

/**
 * Created by 24002 on 2017/5/18.
 */

public class GameDetailListViewAdapter extends BaseAdapter {
    private Activity mActivity;
    private List<GameDetailCommoBean> mList;

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
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null)
        {
            holder = new ViewHolder();
            view = View.inflate(mActivity,R.layout.item_game_commo_list, null);
            holder.iv_user_photo = (ImageView)view.findViewById(R.id.iv_user_photo);
            holder.iv_zan = (ImageView)view.findViewById(R.id.iv_zan);
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
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(new GlideCircleTransform(mActivity))
                .into(holder.iv_user_photo);
        holder.rb_score.setRating(4);
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
        public RatingBar rb_score;
    }
}
