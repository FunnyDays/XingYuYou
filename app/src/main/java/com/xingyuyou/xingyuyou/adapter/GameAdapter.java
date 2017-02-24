package com.xingyuyou.xingyuyou.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.FileUtils;
import com.xingyuyou.xingyuyou.activity.Game10Activity;
import com.xingyuyou.xingyuyou.bean.Game;
import com.xingyuyou.xingyuyou.download.DownloadInfo;
import com.xingyuyou.xingyuyou.download.DownloadManager;
import com.xingyuyou.xingyuyou.download.DownloadState;
import com.xingyuyou.xingyuyou.download.DownloadViewHolder;
import com.xingyuyou.xingyuyou.weight.ProgressButton;

import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/8.
 */

public class GameAdapter extends RecyclerView.Adapter {
    private DbManager mDb;
    private DownloadManager downloadManager;
    private DownloadInfo mDownloadInfo;
    private static final int TYPE_ONE = 1;
    private static final int TYPE_FOOTER = 0;
    private ArrayList<Game> arrayList;
    private Activity mActivity;
    private String gameNameTitle;
    private ProgressButton mGameDownLoad;

    public GameAdapter(Activity mActivity, ArrayList<Game> arrayList) {
        this.arrayList = arrayList;
        this.mActivity = mActivity;
        // 获取下载信息
        //getDownloadInfo();
    }

    /**
     * ItemClick的回调接口
     */
    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_FOOTER:
                view = LayoutInflater.from(mActivity).inflate(R.layout.item_foot, parent, false);
                return new FootViewHolder(view);
            case TYPE_ONE:
                view = LayoutInflater.from(mActivity).inflate(R.layout.item_game_view, parent, false);
                return new ItemViewHolder(view);
        }

        return null;
    }

    /**
     * 获取下载信息状态
     */
    private void getDownloadInfo() {
        downloadManager = DownloadManager.getInstance();
        for (int i = 0; i < downloadManager.getDownloadListCount(); i++) {
            for (int j = 0; j < arrayList.size(); j++) {
                if (downloadManager.getDownloadInfo(i).getLabel().equals(arrayList.get(j).getGameName())){
                    arrayList.get(j).setGameDownState(String.valueOf(downloadManager.getDownloadInfo(i).getState().value()));
                }
            }
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        Log.e("recycle", "position:" + position);
        if (arrayList.size() == 0) {

        } else if (holder instanceof ItemViewHolder) {
            gameNameTitle = arrayList.get(position).getGameName();
            Log.e("download", "下载列表适配器里面的---->" + gameNameTitle);
            ((ItemViewHolder) holder).gameEdition.setText(arrayList.get(position).getGameEdition());
            ((ItemViewHolder) holder).gameIntro.setText(arrayList.get(position).getGameIntro());
            ((ItemViewHolder) holder).gameName.setText(arrayList.get(position).getGameName());
            ((ItemViewHolder) holder).gameSize.setText(arrayList.get(position).getGameSize());
            ((ItemViewHolder) holder).gameRatingBar.setRating(arrayList.get(position).getGameStar());
            ((ItemViewHolder) holder).gameDownLoad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((ItemViewHolder) holder).gameDownLoad.setText("已加入");
                   //notifyItemChanged(position);
                  /*  try {
                        downloadManager.startDownload(
                                "http://download.apk8.com/d2/soft/meilijia.apk",
                                arrayList.get(position).getGamePic(),
                                arrayList.get(position).getGameName(),
                                FileUtils.fileSavePath+gameNameTitle+".apk",
                                true,
                                false,
                                null);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }*/
                }
            });
            Glide.with(mActivity)
                    .load(arrayList.get(position).getGamePic())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(((ItemViewHolder) holder).gamePic);

        }


        //如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(holder.itemView, position);
                }
            });

        }

    }


    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ONE;
        }

    }

    @Override
    public int getItemCount() {

        return arrayList.size() == 0 ? 0 : arrayList.size() + 1;
    }

    class FootViewHolder extends RecyclerView.ViewHolder {

        public FootViewHolder(View view) {
            super(view);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private final TextView gameEdition;
        private final TextView gameIntro;
        private final TextView gameName;
        private final TextView gameSize;
        private final ImageView gamePic;
        private final RatingBar gameRatingBar;
        private final ProgressButton gameDownLoad;

        public ItemViewHolder(View itemView) {
            super(itemView);
            Log.e("recycle", "此位置positionviewholder被创建");
            gameEdition = (TextView) itemView.findViewById(R.id.game_edition);
            gameIntro = (TextView) itemView.findViewById(R.id.game_intro);
            gameName = (TextView) itemView.findViewById(R.id.game_name);
            gameSize = (TextView) itemView.findViewById(R.id.game_size);
            gamePic = (ImageView) itemView.findViewById(R.id.game_pic);
            gameRatingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            gameDownLoad = (ProgressButton) itemView.findViewById(R.id.bt_uninstall);
        }
    }
}
