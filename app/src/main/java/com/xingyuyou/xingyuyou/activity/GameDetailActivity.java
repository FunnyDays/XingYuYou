package com.xingyuyou.xingyuyou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.AppUtils;
import com.xingyuyou.xingyuyou.Utils.FileUtils;
import com.xingyuyou.xingyuyou.Utils.IntentUtils;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.TimeUtils;
import com.xingyuyou.xingyuyou.Utils.glide.GlideCircleTransform;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.adapter.GameDetailListViewAdapter;
import com.xingyuyou.xingyuyou.adapter.GameDetailPicAdapter;
import com.xingyuyou.xingyuyou.bean.community.PostListBean;
import com.xingyuyou.xingyuyou.bean.hotgame.GameDetailBean;
import com.xingyuyou.xingyuyou.bean.hotgame.GameDetailCommoBean;
import com.xingyuyou.xingyuyou.bean.hotgame.GameStartBean;
import com.xingyuyou.xingyuyou.download.DownloadHelper;
import com.xingyuyou.xingyuyou.download.DownloadInfo;
import com.xingyuyou.xingyuyou.download.DownloadManager;
import com.xingyuyou.xingyuyou.download.DownloadState;
import com.xingyuyou.xingyuyou.download.DownloadViewHolder;
import com.xingyuyou.xingyuyou.weight.ProgressButton;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import okhttp3.Call;

public class GameDetailActivity extends AppCompatActivity {


    private Toolbar mToolbar;
    private Intent mIntent;
    private ImageView mGameIcon;
    private TextView mGameName;
    private TextView mGameType;
    private TextView mGameVersion;
    private TextView mGameSize;
    private TextView mGameIntro;
    private List<GameDetailBean> mGameDetailList = null;
    ArrayList<String> gamePics = new ArrayList<>();
    private List<GameDetailCommoBean> mCommoList = new ArrayList<>();
    private List<GameDetailCommoBean> mHotCommoList = new ArrayList<>();
    private List<GameDetailCommoBean> mCommoAdapterList = new ArrayList<>();
    private List<GameDetailCommoBean> mHotCommoAdapterList = new ArrayList<>();
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                String response = (String) msg.obj;
                JSONObject jo = null;
                mGameDetailList = new ArrayList<>();
                try {
                    jo = new JSONObject(response);
                    JSONArray ja = jo.getJSONArray("list");
                    // Log.e("hot", "解析数据："+  ja.toString());
                    Gson gson = new Gson();
                    mGameDetailList = gson.fromJson(ja.toString(),
                            new TypeToken<List<GameDetailBean>>() {
                            }.getType());
                    setValues();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (msg.what == 2) {
                final String response = (String) msg.obj;
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("-1")) {
                        mBtPackage.setBackgroundResource(R.drawable.button_gray_bg);
                        mBtPackage.setTextColor(getResources().getColor(R.color.darker_gray));
                        mBtPackage.setEnabled(false);
                    } else if (jsonObject.getString("status").equals("1")) {
                        mBtPackage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(GameDetailActivity.this, GetGamePackageActivity.class);
                                intent.putExtra("gameDetail", response);
                                startActivity(intent);
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (msg.what == 3) {
                String response = (String) msg.obj;
                JSONObject jo = null;
                try {
                    jo = new JSONObject(response);
                    JSONArray ja = jo.getJSONArray("nicedata");
                    Gson gson = new Gson();
                    mHotCommoList = gson.fromJson(ja.toString(),
                            new TypeToken<List<GameDetailCommoBean>>() {
                            }.getType());
                    mHotCommoAdapterList.addAll(mHotCommoList);
                    JSONArray ja1 = jo.getJSONArray("data");
                    mCommoList = gson.fromJson(ja1.toString(),
                            new TypeToken<List<GameDetailCommoBean>>() {
                            }.getType());
                    mCommoAdapterList.addAll(mCommoList);
                    mListViewAdapter.notifyDataSetChanged();

                    JSONObject star = jo.getJSONObject("star");
                    mGameStartBean = gson.fromJson(star.toString(), GameStartBean.class);

                    setCommoValues();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private RecyclerView mRecyclerView;
    private GameDetailPicAdapter mGameDetailPicAdapter;
    private ProgressButton mBtInstallGame;
    private DownloadInfo mDownloadInfo;
    private DbManager mDb;
    private String mGameNameTitle;
    private DownloadItemViewHolder mViewHolder;
    private DownloadManager mDownloadManager;
    private TextView mDownNumber;
    private Button mBtPackage;
    private ImageView mGameCoverIcon;
    private ListView mListView;
    private ProgressBar mPbSss;
    private ProgressBar mPbSsr;
    private ProgressBar mPbSr;
    private ProgressBar mPbR;
    private ProgressBar mPbN;
    private TextView mNoData;
    private GameDetailListViewAdapter mListViewAdapter;
    private GameStartBean mGameStartBean;
    private Button mCommoGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail_listview);
        mIntent = getIntent();
        initToolBar();
        initData();
        initView();
        getDownloadInfo();
        initDownload();
        initButtonDownload();

    }

    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(mIntent.getStringExtra("game_name"));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initData() {
        mGameNameTitle = getIntent().getStringExtra("game_name");
        OkHttpUtils.post()//
                .url(XingYuInterface.GET_GAME_DETAILS)
                .tag(this)//
                .addParams("game_id", mIntent.getStringExtra("game_id"))
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mHandler.obtainMessage(1, response).sendToTarget();
                    }
                });
        //礼包详情
        OkHttpUtils.post()//
                .url(XingYuInterface.GAME_GIFT_LIST)
                .tag(this)//
                .addParams("game_id", mIntent.getStringExtra("game_id"))
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mHandler.obtainMessage(2, response).sendToTarget();
                    }
                });
        //游戏评分和游戏评价
        OkHttpUtils.post()//
                .url(XingYuInterface.GET_EVALUATE_LIST)
                .tag(this)//
                .addParams("uid", UserUtils.getUserId())
                .addParams("gid", mIntent.getStringExtra("game_id"))
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mHandler.obtainMessage(3, response).sendToTarget();
                    }
                });
    }

    private void initView() {
        //listview
        mListView = (ListView) findViewById(R.id.listView);
        View view = View.inflate(GameDetailActivity.this, R.layout.part_game_detail_header, null);
        mListView.addHeaderView(view);

        mListViewAdapter = new GameDetailListViewAdapter(GameDetailActivity.this, mCommoAdapterList);
        mListView.setAdapter(mListViewAdapter);

        mGameCoverIcon = (ImageView) view.findViewById(R.id.iv_game_cover_pic);
        mGameVersion = (TextView) view.findViewById(R.id.tv_game_version);
        mGameSize = (TextView) view.findViewById(R.id.tv_game_size);
        mGameIntro = (TextView) view.findViewById(R.id.tv_content);
        mDownNumber = (TextView) view.findViewById(R.id.tv_down_number);
        mNoData = (TextView) view.findViewById(R.id.tv_no_data);
        //礼包按钮
        mBtPackage = (Button) view.findViewById(R.id.bt_get_package);
        //游戏介绍图片
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mGameDetailPicAdapter = new GameDetailPicAdapter(this, gamePics);
        mGameDetailPicAdapter.setOnItemClickLitener(new GameDetailPicAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
               /* startActivity(new Intent(Game10Activity.this, imageDetailPagerActivity.class)
                        .putStringArrayListExtra("gameLink", gamePics));*/
            }
        });
        mRecyclerView.setAdapter(mGameDetailPicAdapter);

        //游戏评分
        mPbSss = (ProgressBar) view.findViewById(R.id.pb_sss);
        mPbSsr = (ProgressBar) view.findViewById(R.id.pb_ssr);
        mPbSr = (ProgressBar) view.findViewById(R.id.pb_sr);
        mPbR = (ProgressBar) view.findViewById(R.id.pb_r);
        mPbN = (ProgressBar) view.findViewById(R.id.pb_n);


        //下载按钮
        mBtInstallGame = (ProgressButton) findViewById(R.id.bt_bottom_install);
        //游戏评论
        mCommoGame = (Button) findViewById(R.id.bt_commo_game);
        mCommoGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameDetailActivity.this, GameCommoActivity.class);
                intent.putExtra("gid", mIntent.getStringExtra("game_id"));
                startActivity(intent);

            }
        });
    }

    private void setValues() {
        Glide.with(this).load(getIntent().getStringExtra("game_cover_pic")).into(mGameCoverIcon);
        //mGameName.setText(mGameDetailList.get(0).getGame_name());
        // mGameType.setText(mGameDetailList.get(0).getGame_type_id());
        mGameVersion.setText("版本：" + mGameDetailList.get(0).getVersion());
        mGameSize.setText("大小：" + mGameDetailList.get(0).getGame_size());
        mGameIntro.setText(mGameDetailList.get(0).getIntroduction());
        mDownNumber.setText("下载次数：" + mGameDetailList.get(0).getDow_num());

        //游戏介绍图
        gamePics.addAll(mGameDetailList.get(0).getScreenshot());
        mGameDetailPicAdapter.notifyDataSetChanged();


    }

    private void setCommoValues() {

        //游戏评分
        mPbSss.setMax(Integer.parseInt(mGameStartBean.getSum_star()));
        mPbSsr.setMax(Integer.parseInt(mGameStartBean.getSum_star()));
        mPbSr.setMax(Integer.parseInt(mGameStartBean.getSum_star()));
        mPbR.setMax(Integer.parseInt(mGameStartBean.getSum_star()));
        mPbN.setMax(Integer.parseInt(mGameStartBean.getSum_star()));
        mPbSss.setProgress(Integer.parseInt(mGameStartBean.getOne_star()));
        mPbSsr.setProgress(Integer.parseInt(mGameStartBean.getTwo_star()));
        mPbSr.setProgress(Integer.parseInt(mGameStartBean.getThree_star()));
        mPbR.setProgress(Integer.parseInt(mGameStartBean.getFour_star()));
        mPbN.setProgress(Integer.parseInt(mGameStartBean.getFive_star()));

        //热门评论
        if (mHotCommoAdapterList.size() != 0) {
            mNoData.setVisibility(View.GONE);
            for (int i = 0; i < (mHotCommoAdapterList.size()>2?2:mHotCommoAdapterList.size()); i++) {
                View commo = View.inflate(GameDetailActivity.this, R.layout.item_game_commo_list, null);
                ImageView iv_user_photo = (ImageView) commo.findViewById(R.id.iv_user_photo);
                ImageView iv_zan = (ImageView) commo.findViewById(R.id.iv_zan);
                TextView tv_user_name = (TextView) commo.findViewById(R.id.tv_user_name);
                TextView tv_zan_num = (TextView) commo.findViewById(R.id.tv_zan_num);
                TextView tv_reply_content = (TextView) commo.findViewById(R.id.tv_reply_content);
                TextView tv_commo_time = (TextView) commo.findViewById(R.id.tv_commo_time);
                RatingBar rb_score = (RatingBar) commo.findViewById(R.id.rb_score);
                Glide.with(getApplication())
                        .load(mHotCommoAdapterList.get(i).getHead_image())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .transform(new GlideCircleTransform(getApplication()))
                        .into(iv_user_photo);
                tv_user_name.setText(mHotCommoAdapterList.get(i).getNickname());
                tv_zan_num.setText(mHotCommoAdapterList.get(i).getLaud_num());
                tv_reply_content.setText(mHotCommoAdapterList.get(i).getComment());
                tv_commo_time.setText(TimeUtils.getFriendlyTimeSpanByNow(Long.parseLong(mHotCommoAdapterList.get(i).getDateline() + "000")));
                rb_score.setRating(Integer.parseInt(mHotCommoAdapterList.get(i).getStar_num()));
                mListView.addHeaderView(commo);
            }
        }
        View view = View.inflate(GameDetailActivity.this, R.layout.part_game_detail_commo_header, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_player_no_data);
        if (mCommoAdapterList.size() != 0) {
            textView.setVisibility(View.GONE);
        }
        mListView.addHeaderView(view);
    }

    /**
     * 获取下载信息状态
     */
    private void getDownloadInfo() {
        mDownloadManager = DownloadManager.getInstance();
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                .setDbName("download")
                .setDbVersion(1);
        mDb = x.getDb(daoConfig);
        try {
            mDownloadInfo = mDb.selector(DownloadInfo.class)
                    .where("label", "=", mGameNameTitle)
                    .and("fileSavePath", "=", FileUtils.fileSavePath + mGameNameTitle + ".apk")
                    .findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (mDownloadInfo != null) {
        }

    }

    private void initDownload() {
        if (mDownloadInfo != null) {
            mViewHolder = new DownloadItemViewHolder(null, mDownloadInfo);
            mViewHolder.refresh();
            if (mDownloadInfo.getState().value() < DownloadState.FINISHED.value()) {
                try {
                    mDownloadManager.startDownload(
                            mDownloadInfo.getUrl(), mDownloadInfo.getGamePicUrl(), mDownloadInfo.getPackageName(), mDownloadInfo.getLabel(), mDownloadInfo.getGameSize(), mDownloadInfo.getGameIntro(),
                            mDownloadInfo.getFileSavePath(), mDownloadInfo.isAutoResume(), mDownloadInfo.isAutoRename(), mViewHolder);
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
            mBtInstallGame.setTag(1);
        } else {
            mBtInstallGame.setTag(0);
        }
    }

    private void initButtonDownload() {
        mBtInstallGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 防止开启多个异步线程
                if ((Integer) mBtInstallGame.getTag() == 0) {
                    //向服务器传输下载数据
                    DownloadHelper.updataDown(mIntent.getStringExtra("game_id"));
                    //初始化下载信息
                    DownloadInfo downloadInfo = new DownloadInfo();
                    downloadInfo.setUrl(mGameDetailList.get(0).getAdd_game_address());
                    downloadInfo.setGamePicUrl(mGameDetailList.get(0).getIcon());
                    downloadInfo.setPackageName(mGameDetailList.get(0).getGame_baoming());
                    downloadInfo.setAutoResume(true);
                    downloadInfo.setAutoRename(false);
                    downloadInfo.setLabel(mGameDetailList.get(0).getGame_name());
                    downloadInfo.setFileSavePath(FileUtils.fileSavePath + mGameNameTitle + ".apk");
                    mViewHolder = new DownloadItemViewHolder(null, downloadInfo);
                    mViewHolder.toggleEvent(null);
                    mBtInstallGame.setTag(1);
                } else {
                    mViewHolder.toggleEvent(null);
                }
            }
        });
    }

    public class DownloadItemViewHolder extends DownloadViewHolder {


        public DownloadItemViewHolder(View view, DownloadInfo downloadInfo) {
            super(view, downloadInfo);
            refresh();
        }


        private void toggleEvent(View view) {
            DownloadState state = downloadInfo.getState();
            switch (state) {
                case WAITING:
                case STARTED:
                    mDownloadManager.stopDownload(downloadInfo);
                    break;
                case ERROR:
                case STOPPED:
                    try {
                        mDownloadManager.startDownload(
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
                    if (AppUtils.isInstallApp(GameDetailActivity.this, downloadInfo.getPackageName())) {
                        mBtInstallGame.setText("打开");
                        AppUtils.launchApp(GameDetailActivity.this, downloadInfo.getPackageName());
                    } else {
                        mBtInstallGame.setText("安装");
                        AppUtils.installApp(GameDetailActivity.this, downloadInfo.getFileSavePath());
                    }
                    break;
                default:
                    break;
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
            AppUtils.installApp(GameDetailActivity.this, downloadInfo.getFileSavePath());
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
            //gameEnName.setText(downloadInfo.getLabel()+"---"+downloadInfo.getState().toString()+"---"+downloadInfo.getProgress());
            mBtInstallGame.setProgress(downloadInfo.getProgress());
            mBtInstallGame.setVisibility(View.VISIBLE);
            mBtInstallGame.setText(x.app().getString(R.string.stop));
            DownloadState state = downloadInfo.getState();
            switch (state) {
                case WAITING:
                case STARTED:
                    mBtInstallGame.setText(x.app().getString(R.string.stop));
                    break;
                case ERROR:
                case STOPPED:
                    mBtInstallGame.setText(x.app().getString(R.string.start));
                    break;
                case FINISHED:
                    mBtInstallGame.setText("下载完成");
                    if (AppUtils.isInstallApp(GameDetailActivity.this, downloadInfo.getPackageName())) {
                        mBtInstallGame.setText("打开");
                        // AppUtils.launchApp(HotGameDetailActivity.this, downloadInfo.getPackageName());
                    } else {
                        mBtInstallGame.setText("安装");
                        //AppUtils.installApp(HotGameDetailActivity.this, downloadInfo.getFileSavePath());
                    }
                    break;
                default:
                    mBtInstallGame.setText(x.app().getString(R.string.start));
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
