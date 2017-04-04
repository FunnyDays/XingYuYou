package com.xingyuyou.xingyuyou.fragment;

import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.AppUtils;
import com.xingyuyou.xingyuyou.Utils.ConvertUtils;
import com.xingyuyou.xingyuyou.Utils.FileUtils;
import com.xingyuyou.xingyuyou.Utils.GlideImageLoader;
import com.xingyuyou.xingyuyou.Utils.IntentUtils;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.activity.HotGameDetailActivity;
import com.xingyuyou.xingyuyou.activity.LoginActivity;
import com.xingyuyou.xingyuyou.base.BaseFragment;
import com.xingyuyou.xingyuyou.bean.Game;
import com.xingyuyou.xingyuyou.bean.HotBannerBean;
import com.xingyuyou.xingyuyou.bean.hotgame.HotGameBean;
import com.xingyuyou.xingyuyou.download.DownloadInfo;
import com.xingyuyou.xingyuyou.download.DownloadManager;
import com.xingyuyou.xingyuyou.download.DownloadState;
import com.xingyuyou.xingyuyou.download.DownloadViewHolder;
import com.xingyuyou.xingyuyou.weight.HorizontalProgressBarWithTextProgress;
import com.xingyuyou.xingyuyou.weight.ProgressButton;
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
public class HotGameFragment extends BaseFragment {


    private ListView mListView;
    private DownloadManager downloadManager;
    private DownloadListAdapter downloadListAdapter;
    private ArrayList<Game> mGameArrayList;
    private List<HotGameBean> mHotGameList=new ArrayList<>();
    private List<HotGameBean> mGameListAdapter=new ArrayList<>();
    private List<HotBannerBean> mHotBannerGameList;
    private Banner mBanner;
    private int lastItem;
    private int  MLOADINGMORE_FLAG = 0;
    private int  PAGENUMBER = 1;
    private View mLoading;
    private TextView mLoadingText;
    private ProgressBar mPbLoading;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (msg.obj.toString().contains("{\"list\":null}")) {
                    Toast.makeText(mActivity, "已经没有更多数据", Toast.LENGTH_SHORT).show();
                    View noData = View.inflate(mActivity, R.layout.default_no_data, null);
                    mLoadingText.setText("没有更多数据");
                    mPbLoading.setVisibility(View.GONE);
                    return;
                }
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
                        Log.e("hotgame", "解析数据："+ mHotGameList.toString());
                    }
                    mGameListAdapter.addAll(mHotGameList);
                    //如果还有数据把加载更多值为0
                    MLOADINGMORE_FLAG=0;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //更新UI
                if (downloadListAdapter != null) {
                    downloadListAdapter.notifyDataSetChanged();
                }
            }
            if (msg.what == 2) {
                String response = (String) msg.obj;
                JSONObject jo = null;
                try {
                    jo = new JSONObject(response);
                    JSONArray ja = jo.getJSONArray("data");
                    Gson gson = new Gson();
                    mHotBannerGameList = gson.fromJson(ja.toString(),
                            new TypeToken<List<HotBannerBean>>() {
                            }.getType());
                    List<String> imageList = new ArrayList<>();
                    for (int i = 0; i < mHotBannerGameList.size(); i++) {
                        imageList.add(mHotBannerGameList.get(i).getData());
                    }
                    mBanner.setImages(imageList).setImageLoader(new GlideImageLoader()).start();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public static HotGameFragment newInstance(String content) {
        Bundle args = new Bundle();
        args.putString("ARGS", content);
        HotGameFragment fragment = new HotGameFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected View initView() {
        initBannerData();
        initData(PAGENUMBER);
        View view = View.inflate(mActivity, R.layout.fragment_hot_game, null);
        return view;
    }

    /**
     * 初始化数据
     */

    public void initData(int PAGENUMBER) {
        OkHttpUtils.post()//
                .addParams("limit",String.valueOf(PAGENUMBER))
                .url(XingYuInterface.GET_GAME_LIST + "/type/top")
                .tag(this)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("hot", e.toString() + ":e");
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        handler.obtainMessage(1, response).sendToTarget();
                    }
                });
    }

    private void initBannerData() {
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
       //轮播图设置
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
        //设置底部布局
        mLoading = View.inflate(mActivity, R.layout.default_loading, null);
        mLoadingText = (TextView) mLoading.findViewById(R.id.loading_text);
        mPbLoading = (ProgressBar) mLoading.findViewById(R.id.pb_loading);
        mListView.addFooterView(mLoading);
        mListView.setDividerHeight(0);
        downloadManager = DownloadManager.getInstance();
        downloadListAdapter = new DownloadListAdapter();
        mListView.setAdapter(downloadListAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mActivity,HotGameDetailActivity.class);
                intent.putExtra("game_id",mGameListAdapter.get(i-2).getId());
                intent.putExtra("game_name",mGameListAdapter.get(i-2).getGame_name());
                Log.e("game_id",mGameListAdapter.get(i-2).getId());
                startActivity(intent);
            }
        });
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                lastItem = firstVisibleItem + visibleItemCount + 1 ;

                if (lastItem==totalItemCount&&MLOADINGMORE_FLAG==0){
                    MLOADINGMORE_FLAG++;
                    PAGENUMBER++;
                    initData(PAGENUMBER);
                }
            }
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        downloadListAdapter.notifyDataSetChanged();
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
            return mGameListAdapter.size();
        }

        @Override
        public Object getItem(int i) {
            return mGameListAdapter.get(i);
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
            downloadInfo.setUrl(mGameListAdapter.get(i).getAdd_game_address());
            downloadInfo.setGameSize(mGameListAdapter.get(i).getGame_size());
            downloadInfo.setGamePicUrl(mGameListAdapter.get(i).getIcon());
            downloadInfo.setPackageName(mGameListAdapter.get(i).getGame_baoming());
            downloadInfo.setLabel(mGameListAdapter.get(i).getGame_name());
            downloadInfo.setGameIntro(mGameListAdapter.get(i).getFeatures());
            downloadInfo.setFileSavePath(FileUtils.fileSavePath + mGameListAdapter.get(i).getGame_name() + ".apk");
            downloadInfo.setAutoResume(true);
            downloadInfo.setAutoRename(false);
            for (int j = 0; j < downloadManager.getDownloadListCount(); j++) {
                if (downloadManager.getDownloadInfo(j).getLabel().equals(mGameListAdapter.get(i).getGame_name())) {
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
                            downloadInfo.getPackageName(),
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
        @ViewInject(R.id.game_intro)
        TextView gameIntro;
        @ViewInject(R.id.download_state)
        TextView state;
        @ViewInject(R.id.pb_progressbar)
        HorizontalProgressBarWithTextProgress progressBar;
        @ViewInject(R.id.bt_uninstall)
        ProgressButton stopBtn;

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
                                downloadInfo.getPackageName(),
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
                    //Toast.makeText(mActivity, "下载完成", Toast.LENGTH_SHORT).show();
                    if (AppUtils.isInstallApp(mActivity,downloadInfo.getPackageName())) {
                        stopBtn.setText("打开");
                        AppUtils.launchApp(mActivity, downloadInfo.getPackageName());
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
           // stopBtn.setProgress((int)((current*100)/total));
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
            gameSize.setText(downloadInfo.getGameSize());
            gameIntro.setText(downloadInfo.getGameIntro());
            Glide.with(mActivity).load(downloadInfo.getGamePicUrl()).into(gamePic);
            stopBtn.setProgress(downloadInfo.getProgress());
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
                    if (AppUtils.isInstallApp(mActivity,downloadInfo.getPackageName())) {
                        stopBtn.setText("打开");
                       // AppUtils.launchApp(mActivity, downloadInfo.getPackageName());
                    } else {
                        stopBtn.setText("安装");
                        //AppUtils.installApp(mActivity, downloadInfo.getFileSavePath());
                    }
                    break;
                default:
                    stopBtn.setText(x.app().getString(R.string.start));
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(mActivity);
    }
}
