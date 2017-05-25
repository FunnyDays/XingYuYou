package com.xingyuyou.xingyuyou.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.SPUtils;
import com.xingyuyou.xingyuyou.Utils.SoftKeyBoart.SpanStringUtils;
import com.xingyuyou.xingyuyou.Utils.TimeUtils;
import com.xingyuyou.xingyuyou.Utils.glide.GlideCircleTransform;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.adapter.GodCommoListAdapter;
import com.xingyuyou.xingyuyou.bean.community.PostListBean;
import com.xingyuyou.xingyuyou.bean.community.PostTopAndWellBean;
import com.xingyuyou.xingyuyou.bean.user.PrivateLetterBean;
import com.xingyuyou.xingyuyou.bean.user.UserBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class PrivateLetterActivity extends AppCompatActivity {
    private List<PrivateLetterBean> mLetterList=new ArrayList();
    private List<PrivateLetterBean> mLetterAdapterList=new ArrayList();
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.obj != null) {
                if (msg.what == 1) {
                    String response = (String) msg.obj;
                    JSONObject jo = null;
                    try {
                        jo = new JSONObject(response);
                        String string = jo.getString("status");

                        if (string.equals("1")) {
                            JSONObject ja = jo.getJSONObject("data");
                            Gson gson = new Gson();
                            PrivateLetterBean privateLetterBean = gson.fromJson(ja.toString(), PrivateLetterBean.class);
                            mLetterAdapterList.add(privateLetterBean);
                            mLetterAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    };



    private Toolbar mToolbar;
    private PrivateLetterAdapter mLetterAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_letter_detail);
        initData();
        initView();
        initToolBar();
    }

    private void initView() {
        ListView listView = (ListView) findViewById(R.id.listView);
        mLetterAdapter = new PrivateLetterAdapter();
        listView.setAdapter(mLetterAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(PrivateLetterActivity.this,PrivateLetterDetailActivity.class));
            }
        });
    }
    /**
     * 初始化数据
     */
    public void initData() {
        SPUtils user_data = new SPUtils("user_data");
        OkHttpUtils.post()//
                .addParams("uid",user_data.getString("id"))
                .url(XingYuInterface.PRIVATE_LETTER_READ)
                .tag(this)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        // Log.e("hot", e.toString() + ":e");
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        mHandler.obtainMessage(1, response).sendToTarget();
                        Log.e("onResponse", response+ ":e");
                    }
                });
    }

    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("私信");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private class PrivateLetterAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return mLetterAdapterList.size();
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
            final ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = View.inflate(PrivateLetterActivity.this, R.layout.item_private_letter_list, null);
                holder.iv_user_photo = (ImageView) view.findViewById(R.id.iv_user_photo);
                holder.tv_content = (TextView) view.findViewById(R.id.tv_content);
                holder.tv_time = (TextView) view.findViewById(R.id.tv_time);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.tv_content.setText(SpanStringUtils.getEmotionContent(PrivateLetterActivity.this, holder.tv_content, mLetterAdapterList.get(i).getContent()));
            holder.tv_time.setText(TimeUtils.getFriendlyTimeSpanByNow(Long.parseLong(mLetterAdapterList.get(i).getDateline() + "000")));
            Glide.with(getApplication())
                    .load(R.mipmap.icon)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .transform(new GlideCircleTransform(getApplication()))
                    .into(holder.iv_user_photo);
            return view;
        }
        /*存放控件 的ViewHolder*/
        public final class ViewHolder {
            public TextView tv_content;
            public TextView tv_time;
            public ImageView iv_user_photo;

        }
    }
}
