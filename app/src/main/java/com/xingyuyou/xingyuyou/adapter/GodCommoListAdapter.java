package com.xingyuyou.xingyuyou.adapter;

import android.app.Activity;
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
import com.xingyuyou.xingyuyou.bean.community.PostCommoBean;
import com.xingyuyou.xingyuyou.bean.god.GodCommoBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;

/**
 * Created by 24002 on 2017/5/23.
 */

public class GodCommoListAdapter extends BaseAdapter {
    private Activity mActivity;
    private List<GodCommoBean> mCommoBeanList;
    public GodCommoListAdapter(Activity activity, List<GodCommoBean> commoBeanList) {
        this.mActivity=activity;
        this.mCommoBeanList=commoBeanList;
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
        if(view == null)
        {
            holder = new ViewHolder();
            view = View.inflate(mActivity, R.layout.item_commo_post_list, null);
            holder.iv_user_photo = (ImageView)view.findViewById(R.id.iv_user_photo);
            holder.iv_zan = (ImageView)view.findViewById(R.id.iv_love);
            holder.tv_user_name = (TextView)view.findViewById(R.id.tv_user_name);
            holder.tv_zan_num = (TextView)view.findViewById(R.id.tv_love_num);
            holder.tv_reply_content = (TextView)view.findViewById(R.id.tv_commo_content);
            holder.tv_commo_time = (TextView)view.findViewById(R.id.tv_post_time);
            holder.tv_floor_num = (TextView)view.findViewById(R.id.tv_floor_num);
            view.setTag(holder);
        }else
        {
            holder = (ViewHolder)view.getTag();
        }
        holder.tv_floor_num.setText(mCommoBeanList.get(i).getFloor_num()+"楼");
        holder.tv_user_name.setText(mCommoBeanList.get(i).getNickname());
        holder.tv_zan_num.setText(mCommoBeanList.get(i).getForum_laud());
        holder.tv_reply_content.setText(mCommoBeanList.get(i).getReplies_content());
        holder.tv_commo_time.setText(TimeUtils.getFriendlyTimeSpanByNow(Long.parseLong(mCommoBeanList.get(i).getDateline() + "000")));
        Glide.with(mActivity)
                .load(mCommoBeanList.get(i).getHead_image())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(new GlideCircleTransform(mActivity))
                .into(holder.iv_user_photo);
        //点赞
        if (mCommoBeanList.get(i).getLaud_status()==1) {
            holder.iv_zan.setImageResource(R.mipmap.ic_zan_fill);
        } else {
            holder.iv_zan.setImageResource(R.mipmap.ic_zan);
        }
        holder.iv_zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!UserUtils.logined()) {
                    IntentUtils.startActivity(mActivity, LoginActivity.class);
                    return;
                }
                getLuad(mCommoBeanList.get(i).getId());
                if (mCommoBeanList.get(i).getLaud_status() ==1) {
                    holder.tv_zan_num.setText(String.valueOf((Integer.parseInt(mCommoBeanList.get(i).getForum_laud()) - 1)));
                    mCommoBeanList.get(i).setForum_laud(String.valueOf((Integer.parseInt(mCommoBeanList.get(i).getForum_laud()) - 1)));
                    mCommoBeanList.get(i).setLaud_status(0);
                    Toast.makeText(mActivity, "取消点赞", Toast.LENGTH_SHORT).show();
                    holder.iv_zan.setImageResource(R.mipmap.ic_zan);
                } else {
                    holder.tv_zan_num.setText(String.valueOf((Integer.parseInt(mCommoBeanList.get(i).getForum_laud()) + 1)));
                    mCommoBeanList.get(i).setForum_laud(String.valueOf((Integer.parseInt(mCommoBeanList.get(i).getForum_laud()) + 1)));
                    mCommoBeanList.get(i).setLaud_status(1);
                    Toast.makeText(mActivity, "点赞", Toast.LENGTH_SHORT).show();
                    holder.iv_zan.setImageResource(R.mipmap.ic_zan_fill);
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
        public TextView tv_floor_num;
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
