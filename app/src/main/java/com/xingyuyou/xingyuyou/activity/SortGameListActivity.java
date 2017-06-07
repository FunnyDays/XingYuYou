package com.xingyuyou.xingyuyou.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.AppUtils;
import com.xingyuyou.xingyuyou.Utils.ConvertUtils;
import com.xingyuyou.xingyuyou.Utils.FileUtils;
import com.xingyuyou.xingyuyou.Utils.glide.GlideRoundTransform;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.bean.community.LabelClassBean;
import com.xingyuyou.xingyuyou.bean.sort.GameSortBean;
import com.xingyuyou.xingyuyou.bean.sort.SortListGameBean;
import com.xingyuyou.xingyuyou.download.DownloadInfo;
import com.xingyuyou.xingyuyou.download.DownloadManager;
import com.xingyuyou.xingyuyou.download.DownloadState;
import com.xingyuyou.xingyuyou.download.DownloadViewHolder;
import com.xingyuyou.xingyuyou.fragment.NewGameFragment;
import com.xingyuyou.xingyuyou.weight.HorizontalProgressBarWithTextProgress;
import com.xingyuyou.xingyuyou.weight.ProgressButton;
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

public class SortGameListActivity extends AppCompatActivity {
    private View mLoading;
    private TextView mLoadingText;
    private ProgressBar mPbLoading;
    private int  PAGENUMBER = 1;
    private int lastItem;
    private int  MLOADINGMORE_FLAG = 0;
    private DownloadManager downloadManager;
    private DownloadListAdapter downloadListAdapter;
    private List<SortListGameBean> mGameSortBeanList = new ArrayList<>();
    private List<SortListGameBean> mDatas = new ArrayList<>();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (msg.obj.toString().contains("{\"list\":null}")) {
                    Toast.makeText(SortGameListActivity.this, "已经没有更多数据", Toast.LENGTH_SHORT).show();
                    View noData = View.inflate(SortGameListActivity.this, R.layout.default_no_data, null);
                    mLoadingText.setText("没有更多数据");
                    mPbLoading.setVisibility(View.GONE);
                    return;
                }
                String response = (String) msg.obj;
                JSONObject jo = null;
                try {
                    jo = new JSONObject(response);
                    JSONArray ja = jo.getJSONArray("list");
                    Gson gson = new Gson();
                    mGameSortBeanList = gson.fromJson(ja.toString(),
                            new TypeToken<List<SortListGameBean>>() {
                            }.getType());
                    mDatas.addAll(mGameSortBeanList);
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
        }
    };
    private ListView mListView;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_game_list);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
       // mToolbar.setTitle("关于星宇");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initView();
        initData(PAGENUMBER);
    }

    private void  initData(int PAGENUMBER){
        OkHttpUtils.post()//
                .url(XingYuInterface.GAME_CATEGORY)
                .tag(this)//
                .addParams("type_id", getIntent().getStringExtra("type_id"))
                .addParams("limit", String.valueOf(PAGENUMBER))
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mHandler.obtainMessage(1, response).sendToTarget();
                        // Log.e("weiwei", "response解析数据："+  response);
                    }
                });
    }

    private void initView() {
        mListView = (ListView)findViewById(R.id.lv_download);
        downloadManager = DownloadManager.getInstance();
        downloadListAdapter = new DownloadListAdapter();

        //头布局
        ImageView imageView = new ImageView(SortGameListActivity.this);
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        imageView.setLayoutParams(lp);
        Glide.with(getApplication())
                .load(getIntent().getStringExtra("icon"))
                .into(imageView);
        mListView.addHeaderView(imageView);

        //设置底部布局
        mLoading = View.inflate(SortGameListActivity.this, R.layout.default_loading, null);
        mLoadingText = (TextView) mLoading.findViewById(R.id.loading_text);
        mPbLoading = (ProgressBar) mLoading.findViewById(R.id.pb_loading);
        mListView.addFooterView(mLoading);

        mListView.setDividerHeight(0);
        mListView.setAdapter(downloadListAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0){
                    return;
                }
                Intent intent = new Intent(SortGameListActivity.this,GameDetailActivity.class);
                intent.putExtra("game_id",mDatas.get(i-1).getId());
                intent.putExtra("game_name",mDatas.get(i-1).getGame_name());
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
    protected void onResume() {
        super.onResume();
        downloadListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(this);
    }
    private class DownloadListAdapter extends BaseAdapter {

        private Context mContext;
        private final LayoutInflater mInflater;
        private DownloadListAdapter() {
            mContext = SortGameListActivity.this;
            mInflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public Object getItem(int i) {
            return mDatas.get(i);
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
            downloadInfo.setUrl(mDatas.get(i).getAdd_game_address());
            downloadInfo.setGameSize(mDatas.get(i).getGame_size());
            downloadInfo.setGameIntro(mDatas.get(i).getFeatures());
            downloadInfo.setGamePicUrl(mDatas.get(i).getIcon());
            downloadInfo.setPackageName(mDatas.get(i).getGame_baoming());
            downloadInfo.setLabel(mDatas.get(i).getGame_name());
            downloadInfo.setFileSavePath(FileUtils.fileSavePath + mDatas.get(i).getGame_name() + ".apk");
            downloadInfo.setAutoResume(true);
            downloadInfo.setAutoRename(false);
            for (int j = 0; j < downloadManager.getDownloadListCount(); j++) {
                if (downloadManager.getDownloadInfo(j).getLabel().equals(mDatas.get(i).getGame_name())) {
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
        @ViewInject(R.id.download_state)
        TextView state;
        @ViewInject(R.id.game_intro)
        TextView gameIntro;
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
                    if (AppUtils.isInstallApp(SortGameListActivity.this,downloadInfo.getPackageName())) {
                        stopBtn.setText("打开");
                        AppUtils.launchApp(SortGameListActivity.this, downloadInfo.getPackageName());
                    } else {
                        stopBtn.setText("安装");
                        AppUtils.installApp(SortGameListActivity.this, downloadInfo.getFileSavePath());
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
            AppUtils.installApp(SortGameListActivity.this, downloadInfo.getFileSavePath());
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
            gameIntro.setText(downloadInfo.getGameIntro());
            Glide.with(getApplicationContext()).load(downloadInfo.getGamePicUrl()).transform(new GlideRoundTransform(SortGameListActivity.this,5)).into(gamePic);
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
                    if (AppUtils.isInstallApp(SortGameListActivity.this,downloadInfo.getPackageName())) {
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
}
