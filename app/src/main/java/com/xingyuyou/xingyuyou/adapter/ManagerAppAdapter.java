package com.xingyuyou.xingyuyou.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.AppUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/12/22.
 */
public class ManagerAppAdapter extends RecyclerView.Adapter {
    private Activity mActivity;
    private List<AppUtils.AppInfo> mList;
    boolean mState = false;

    public ManagerAppAdapter(Activity activity, List list) {
        mActivity = activity;
        mList = list;
    }

    /**
     * recyclevivew的点击接口回调
     */
    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener onItemClickLitener) {
        this.mOnItemClickLitener = onItemClickLitener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.item_manager_app_list, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).mAppName.setText(mList.get(position).getName());
            if (mList.get(position).getAppSize() != null) {
                ((ItemViewHolder) holder).mAppSize.setText(mList.get(position).getAppSize());
            }
            ((ItemViewHolder) holder).mAppVersion.setText("版本："+ String.valueOf(mList.get(position).getVersionName()));
            //Glide.with(mActivity).load(mList.get(position).getIcon()).into(((ItemViewHolder) holder).mAppIcon);
            ((ItemViewHolder) holder).mAppIcon.setImageDrawable(mList.get(position).getIcon());
            //给每个Item的CheckBox设置tag防止布局错乱

           if (mList.get(position).isChecked()){
             //  ((ItemViewHolder) holder).mCheckBox.set
           }

            ((ItemViewHolder) holder).mRlSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mList.get(position).isChecked()) {
                        mList.get(position).setChecked(false);
                       // ((ItemViewHolder) holder).mCheckBox.setChecked(false);
                    } else {
                        mList.get(position).setChecked(true);
                        //((ItemViewHolder) holder).mCheckBox.setChecked(true);
                    }
                }
            });
            if (mOnItemClickLitener != null){
                ((ItemViewHolder)holder).mBtUninstall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickLitener.onItemClick(((ItemViewHolder)holder).mBtUninstall,position);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView mAppName;
        private final TextView mAppSize;
        private final TextView mAppVersion;
        private final ImageView mAppIcon;
        private final Button mBtUninstall;
        private final LinearLayout mRlSelect;
     /*   private final ImageView mCheckBox;*/

        public ItemViewHolder(View view) {
            super(view);
            mAppIcon = (ImageView) view.findViewById(R.id.app_pic);
            mAppName = (TextView) view.findViewById(R.id.app_name);
            mAppSize = (TextView) view.findViewById(R.id.app_size);
            mAppVersion = (TextView) view.findViewById(R.id.app_version);
            mBtUninstall = (Button) view.findViewById(R.id.bt_uninstall);
           /* mCheckBox = (ImageView) view.findViewById(R.id.iv_select);*/
            mRlSelect = (LinearLayout) view.findViewById(R.id.rl_select);
        }
    }

}
