package com.xingyuyou.xingyuyou.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.AppUtils;
import com.xingyuyou.xingyuyou.Utils.ConvertUtils;
import com.xingyuyou.xingyuyou.Utils.FileUtils;
import com.xingyuyou.xingyuyou.Utils.GlideImageLoader;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.base.BaseFragment;
import com.xingyuyou.xingyuyou.bean.Game;
import com.xingyuyou.xingyuyou.bean.HotBannerBean;
import com.xingyuyou.xingyuyou.bean.hotgame.HotGameBean;
import com.xingyuyou.xingyuyou.download.DownloadInfo;
import com.xingyuyou.xingyuyou.download.DownloadManager;
import com.xingyuyou.xingyuyou.download.DownloadState;
import com.xingyuyou.xingyuyou.download.DownloadViewHolder;
import com.xingyuyou.xingyuyou.weight.HorizontalProgressBarWithTextProgress;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/6/28.
 */
public class NewGameFragment extends BaseFragment {

    private Banner mBanner;
    private ListView mListView;
    private DownloadManager downloadManager;
    private DownloadListAdapter downloadListAdapter;
    private ArrayList<Game> mGameArrayList;
    private List<HotGameBean> mHotGameList=new ArrayList<>();
    private List<HotBannerBean> mHotBannerGameList;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                String response = (String) msg.obj;
                JSONObject jo = null;
                try {
                    jo = new JSONObject(response);
                    JSONArray ja = jo.getJSONArray("list");
                    // Log.e("hot", "解析数据："+  ja.toString());
                    Gson gson = new Gson();
                    mHotGameList = gson.fromJson(ja.toString(),
                            new TypeToken<List<HotGameBean>>() {
                            }.getType());

                    for (int i = 0; i < mHotGameList.size(); i++) {
                        Log.e("hot", "解析数据：" + mHotGameList.get(i).toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //更新UI
                if (downloadListAdapter != null) {
                    downloadListAdapter.notifyDataSetChanged();
                }


            }if (msg.what == 2) {
                String response = (String) msg.obj;
                JSONObject jo = null;
                try {
                    jo = new JSONObject(response);
                    JSONArray ja = jo.getJSONArray("data");
                    // Log.e("hot", "解析数据："+  ja.toString());
                    Gson gson = new Gson();
                    mHotBannerGameList = gson.fromJson(ja.toString(),
                            new TypeToken<List<HotBannerBean>>() {
                            }.getType());
                    List<String> imageList = new ArrayList<>();
                    for (int i = 0; i < mHotBannerGameList.size(); i++) {
                        Log.e("lunbo",mHotBannerGameList.get(i).getData());
                        imageList.add(mHotBannerGameList.get(i).getData());
                    }
                    mBanner.setImages(imageList).setImageLoader(new GlideImageLoader()).start();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };



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
        Log.e("hot", "121第一次初始化数据");
       // mGameArrayList = new ArrayList<>();
        OkHttpUtils.post()//
                .url(XingYuInterface.GET_GAME_LIST + "/type/new")
                .tag(this)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("hot", e.toString() + ":e");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //Log.e("hot", response + "");
                        handler.obtainMessage(1, response).sendToTarget();
                    }
                });
        OkHttpUtils.post()//
                .url(XingYuInterface.ROTATION_IMG)
                .tag(this)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("hot", e.toString() + ":e");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("lunbo", response + "");
                        handler.obtainMessage(2, response).sendToTarget();
                    }
                });

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mListView.setNestedScrollingEnabled(true);
        }
        View headerViewOne = View.inflate(mActivity, R.layout.carousel_figure_header_view, null);
        View headerViewTwo = View.inflate(mActivity, R.layout.item_listview_header_view_four_icon, null);
        //四个引导图标
        ImageView imageViewOne = (ImageView) headerViewTwo.findViewById(R.id.image_one);
        ImageView imageViewTwo = (ImageView) headerViewTwo.findViewById(R.id.image_two);
        ImageView imageViewThree = (ImageView) headerViewTwo.findViewById(R.id.image_three);
        ImageView imageViewFour = (ImageView) headerViewTwo.findViewById(R.id.image_four);
        imageViewOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mActivity, "第一个引导", Toast.LENGTH_SHORT).show();
            }
        });
        imageViewTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mActivity, "第二个引导", Toast.LENGTH_SHORT).show();
            }
        });
        imageViewThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mActivity, "第三个引导", Toast.LENGTH_SHORT).show();
            }
        });
        imageViewFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mActivity, "第四个引导", Toast.LENGTH_SHORT).show();
            }
        });
        //轮播图数据
        ArrayList<Object> imageList = new ArrayList<>();
        imageList.add("1");
        imageList.add("2");
        imageList.add("3");
        ArrayList<String> titlesList = new ArrayList<>();
        titlesList.add("标题1");
        titlesList.add("标题2");
        titlesList.add("标题3");
        //Banner
        //Banner banner = new Banner(mActivity);
        mBanner = (Banner) headerViewOne.findViewById(R.id.banner);
        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Toast.makeText(mActivity, "第一张图片" + position, Toast.LENGTH_SHORT).show();
            }
        });
        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, ConvertUtils.dp2px(200));
        mBanner.setLayoutParams(layoutParams);
        //设置banner样式
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置指示器位置（当banner模式中有指示器时）
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        //设置标题集合（当banner样式有显示title时）
        //banner.setBannerTitles(titlesList);
        //设置图片
        //mBanner.setImages(imageList).setImageLoader(new GlideImageLoader()).start();

        //添加头布局
        mListView.addHeaderView(mBanner);
        mListView.addHeaderView(headerViewTwo);
        mListView.setDividerHeight(0);
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
            return mHotGameList.size();
        }

        @Override
        public Object getItem(int i) {
            return mHotGameList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            DownloadItemViewHolder holder = null;
            DownloadInfo downloadInfo = null;
            downloadInfo = new DownloadInfo();
            downloadInfo.setUrl(mHotGameList.get(i).getAdd_game_address());
            downloadInfo.setGameSize(mHotGameList.get(i).getGame_size());
            downloadInfo.setGameIntro(mHotGameList.get(i).getFeatures());
            downloadInfo.setGamePicUrl(mHotGameList.get(i).getIcon());
            downloadInfo.setLabel(mHotGameList.get(i).getGame_name());
            downloadInfo.setFileSavePath(FileUtils.fileSavePath + mHotGameList.get(i).getGame_name() + ".apk");
            downloadInfo.setAutoResume(true);
            downloadInfo.setAutoRename(false);
            for (int j = 0; j < downloadManager.getDownloadListCount(); j++) {
                if (downloadManager.getDownloadInfo(j).getLabel().equals(mHotGameList.get(i).getGame_name())) {
                    downloadInfo = downloadManager.getDownloadInfo(j);
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
                            downloadInfo.getGameSize(),
                            downloadInfo.getGameIntro(),
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
        @ViewInject(R.id.game_size)
        TextView gameSize;
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
                                downloadInfo.getGameSize(),
                                downloadInfo.getGameIntro(),
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
                    if (AppUtils.isInstallApp(mActivity, "cn.mljia.o2o")) {
                        stopBtn.setText("打开");
                        AppUtils.launchApp(mActivity, "cn.mljia.o2o");
                    } else {
                        stopBtn.setText("安装");
                        AppUtils.installApp(mActivity, downloadInfo.getFileSavePath());
                    }
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
            gameSize.setText(downloadInfo.getGameSize());
            label.setText(downloadInfo.getLabel());
            //state.setText(downloadInfo.getState().toString());
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
                    //cn.mljia.o2o
                    stopBtn.setText("完成");
                    if (AppUtils.isInstallApp(mActivity, "cn.mljia.o2o")) {
                        stopBtn.setText("打开");
                        //AppUtils.launchApp(mActivity,"cn.mljia.o2o");
                    } else {
                        stopBtn.setText("安装");
                        //AppUtils.installApp(mActivity,downloadInfo.getFileSavePath());
                    }
                    break;
                default:
                    stopBtn.setText(x.app().getString(R.string.start));
                    break;
            }
        }
    }
}
