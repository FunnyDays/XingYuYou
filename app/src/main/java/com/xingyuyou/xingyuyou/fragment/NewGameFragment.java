package com.xingyuyou.xingyuyou.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.FileUtils;
import com.xingyuyou.xingyuyou.activity.DownLoadActivity;
import com.xingyuyou.xingyuyou.base.BaseFragment;
import com.xingyuyou.xingyuyou.bean.ApkModel;
import com.xingyuyou.xingyuyou.bean.Game;
import com.xingyuyou.xingyuyou.download.DownloadInfo;
import com.xingyuyou.xingyuyou.download.DownloadManager;
import com.xingyuyou.xingyuyou.download.DownloadState;
import com.xingyuyou.xingyuyou.download.DownloadViewHolder;
import com.xingyuyou.xingyuyou.weight.HorizontalProgressBarWithTextProgress;

import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/28.
 */
public class NewGameFragment extends BaseFragment {


    private ListView mListView;
    private DownloadManager downloadManager;
    private DownloadListAdapter downloadListAdapter;
    private ArrayList<Game> mGameArrayList;

    public static NewGameFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        NewGameFragment fragment = new NewGameFragment();
        fragment.setArguments(args);
        return fragment;
    }


    /**
     * 初始化数据
     */
    @Override
    public void initData() {
        Log.e("Zifragment", "11第一次初始化数据");
        mGameArrayList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            Game game = new Game();
            game.setGamePic("http://pic3.apk8.com/small2/14325422596306671.png");
            game.setGameName("美丽加"+i);
            game.setGameDownloadUrl("http://download.apk8.com/d2/soft/meilijia.apk");
            mGameArrayList.add(game);
        }
    }
    @Override
    protected View initView() {
        initData();
        View view = View.inflate(mActivity, R.layout.fragment_new_game, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mListView = (ListView) view.findViewById(R.id.lv_download);
        downloadManager = DownloadManager.getInstance();
        downloadListAdapter = new DownloadListAdapter();
        mListView.setAdapter(downloadListAdapter);
    }

    private class DownloadListAdapter extends BaseAdapter {

        private Context mContext;
        private final LayoutInflater mInflater;

        private DownloadListAdapter() {
            mContext = mActivity;
            mInflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            return mGameArrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return mGameArrayList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
           DownloadItemViewHolder holder = null;
            DownloadInfo downloadInfo=null;
            downloadInfo=new DownloadInfo();
            downloadInfo.setUrl(mGameArrayList.get(i).getGameDownloadUrl());
            downloadInfo.setGamePicUrl( mGameArrayList.get(i).getGamePic());
            downloadInfo.setLabel( mGameArrayList.get(i).getGameName());
            downloadInfo.setFileSavePath(FileUtils.fileSavePath+mGameArrayList.get(i).getGameName()+".apk");
            downloadInfo.setAutoResume(true);
            downloadInfo.setAutoRename(false);
            for (int j = 0; j < downloadManager.getDownloadListCount(); j++) {
                if (downloadManager.getDownloadInfo(j).getLabel().equals(mGameArrayList.get(i).getGameName())) {
                    downloadInfo=downloadManager.getDownloadInfo(j);
                }else {

                }

            }

            if (view == null) {
                view = mInflater.inflate(R.layout.item_new_game_view, null);
                holder = new DownloadItemViewHolder(view, downloadInfo);
                view.setTag(holder);
                holder.refresh();
            } else {
                holder = (DownloadItemViewHolder) view.getTag();
                holder.update(downloadInfo);
            }

            if (downloadInfo.getState().value() < DownloadState.FINISHED.value()) {
                try {
                    downloadManager.startDownload(
                            downloadInfo.getUrl(),
                            downloadInfo.getGamePicUrl(),
                            downloadInfo.getLabel(),
                            downloadInfo.getFileSavePath(),
                            downloadInfo.isAutoResume(),
                            downloadInfo.isAutoRename(),
                            holder);
                } catch (DbException ex) {
                    Toast.makeText(x.app(), "添加下载失败", Toast.LENGTH_LONG).show();
                }
            }

            return view;
        }
    }
    public class DownloadItemViewHolder extends DownloadViewHolder {
        @ViewInject(R.id.game_name)
        TextView label;
        @ViewInject(R.id.game_pic)
        ImageView gamePic;
        @ViewInject(R.id.download_state)
        TextView state;
        @ViewInject(R.id.pb_progressbar)
        HorizontalProgressBarWithTextProgress progressBar;
        @ViewInject(R.id.bt_uninstall)
        Button stopBtn;

        public DownloadItemViewHolder(View view, DownloadInfo downloadInfo) {
            super(view, downloadInfo);
            refresh();
        }

        @Event(R.id.bt_uninstall)
        private void toggleEvent(View view) {
            DownloadState state = downloadInfo.getState();
            switch (state) {
                case WAITING:
                case STARTED:
                    downloadManager.stopDownload(downloadInfo);
                    break;
                case ERROR:
                case STOPPED:
                    try {
                        downloadManager.startDownload(
                                downloadInfo.getUrl(),
                                downloadInfo.getGamePicUrl(),
                                downloadInfo.getLabel(),
                                downloadInfo.getFileSavePath(),
                                downloadInfo.isAutoResume(),
                                downloadInfo.isAutoRename(),
                                this);
                    } catch (DbException ex) {
                        Toast.makeText(x.app(), "添加下载失败", Toast.LENGTH_LONG).show();
                    }
                    break;
                case FINISHED:
                    Toast.makeText(x.app(), "已经下载完成", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }

        @Event(R.id.download_remove_btn)
        private void removeEvent(View view) {
            try {
                downloadManager.removeDownload(downloadInfo);
                downloadListAdapter.notifyDataSetChanged();
            } catch (DbException e) {
                Toast.makeText(x.app(), "移除任务失败", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void update(DownloadInfo downloadInfo) {
            super.update(downloadInfo);
            Log.e("wei", "update");
            refresh();
        }

        @Override
        public void onWaiting() {
            Log.e("wei", "onWaiting");
            refresh();
        }

        @Override
        public void onStarted() {
            Log.e("wei", "onStarted");
            refresh();
        }

        @Override
        public void onLoading(long total, long current) {
            Log.e("wei", "onLoading" + "total:" + total + "current" + current);
            refresh();
        }

        @Override
        public void onSuccess(File result) {
            Log.e("wei", "onSuccess");
            refresh();
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            Log.e("wei", "onError");
            refresh();
        }

        @Override
        public void onCancelled(Callback.CancelledException cex) {
            Log.e("wei", "onCancelled");
            refresh();
        }

        public void refresh() {
            label.setText(downloadInfo.getLabel());
            state.setText(downloadInfo.getState().toString());
            Glide.with(mActivity).load(downloadInfo.getGamePicUrl()).into(gamePic);
            progressBar.setProgress(downloadInfo.getProgress());
            stopBtn.setVisibility(View.VISIBLE);
            stopBtn.setText(x.app().getString(R.string.stop));
            DownloadState state = downloadInfo.getState();
            switch (state) {
                case WAITING:
                case STARTED:
                    stopBtn.setText(x.app().getString(R.string.stop));
                    break;
                case ERROR:
                case STOPPED:
                    stopBtn.setText(x.app().getString(R.string.start));
                    break;
                case FINISHED:
                    stopBtn.setText("完成");
                    break;
                default:
                    stopBtn.setText(x.app().getString(R.string.start));
                    break;
            }
        }
    }
}
