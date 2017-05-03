package com.xingyuyou.xingyuyou.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.GlideImageLoader;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.bean.HotBannerBean;
import com.xingyuyou.xingyuyou.bean.community.TagBean;
import com.xingyuyou.xingyuyou.bean.hotgame.HotGameBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelector;
import okhttp3.Call;

public class SelectTagActivity extends AppCompatActivity {

    private AutoCompleteTextView mEtSelectTag;
    private RecyclerView mRlRootView;
    private ListView mListView;
    private Toolbar mToolbar;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                String response = (String) msg.obj;
                JSONObject jo = null;
                try {
                    jo = new JSONObject(response);
                    JSONArray ja = jo.getJSONArray("data");
                    Log.e("weiwei", "解析数据：" + ja.toString());
                    Gson gson = new Gson();
                    mTagList = gson.fromJson(ja.toString(),
                            new TypeToken<List<TagBean>>() {
                            }.getType());
                    mTagListAdapter.addAll(mTagList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //更新UI
                if (mTagAdapter != null) {
                    mTagAdapter.notifyDataSetChanged();
                }
            }
            if (msg.what == 2) {

            }
        }
    };
    private List<TagBean> mTagList = new ArrayList();
    private List<TagBean> mTagListAdapter = new ArrayList();
    private List<TagBean> mSelectTagList = new ArrayList();
    private List<TagBean> mFilteredArrayList = new ArrayList();
    private List<TagBean> mPublishArrayList = new ArrayList();
    private TagAdapter mTagAdapter;
    private SelectTagAdapter mSelectTagAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_tag);
        initToolbar();
        initView();
        initData();
    }

    private void initData() {
        OkHttpUtils.post()//
                .url(XingYuInterface.POPULAR_TAGS)
                .tag(this)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("weiwei", e.toString() + ":e");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        handler.obtainMessage(1, response).sendToTarget();
                        Log.e("weiwei", response + ":rrrrrrre");
                    }
                });
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {
        mEtSelectTag = (AutoCompleteTextView) findViewById(R.id.et_select_tags);

        mListView = (ListView) findViewById(R.id.list_view);
        initListView();
        mRlRootView = (RecyclerView) findViewById(R.id.rl_root_tags);
        initRecycleView();
    }

    private void initRecycleView() {
        mRlRootView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mSelectTagAdapter = new SelectTagAdapter();
        mRlRootView.setAdapter(mSelectTagAdapter);
    }

    private void initListView() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mSelectTagList.size()>=5){
                    Toast.makeText(SelectTagActivity.this, "只能选择5个标签", Toast.LENGTH_SHORT).show();
                    return;
                }
                mSelectTagList.add(mTagListAdapter.get(position));
                mSelectTagAdapter.notifyDataSetChanged();
            }
        });
        mTagAdapter = new TagAdapter();
        mEtSelectTag.setAdapter(mTagAdapter);//这个是搜索的
        mListView.setAdapter(mTagAdapter);
    }


    private class TagAdapter extends BaseAdapter implements Filterable {
        private ArrayFilter mFilter;

        @Override
        public int getCount() {
            return mTagListAdapter.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(SelectTagActivity.this).inflate(R.layout.item_simple_list, null);
                holder.title = (TextView) convertView.findViewById(R.id.tv_class_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.title.setText(mTagListAdapter.get(position).getLabel_name());
            return convertView;
        }

        @Override
        public Filter getFilter() {
            if (mFilter == null) {
                mFilter = new ArrayFilter();
            }
            return mFilter;
        }
        private class ArrayFilter extends Filter {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                for (Iterator<TagBean> iterator = mTagListAdapter.iterator(); iterator.hasNext();) {
                    TagBean tagBean = iterator.next();
                    Log.e("weiwei","---> name=" + tagBean.getLabel_name());
                    if (tagBean.getLabel_name().contains(constraint)) {
                        mFilteredArrayList.add(tagBean);
                    }
                }
                filterResults.values = mFilteredArrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mTagListAdapter = (List<TagBean>) results.values;
                if (results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }

            }
        }
    }

    public final class ViewHolder {
        public TextView title;
    }

    private class SelectTagAdapter extends RecyclerView.Adapter {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View layout = LayoutInflater.from(SelectTagActivity.this).inflate(R.layout.item_post_tag, parent, false);
            return new SelectTagAdapter.ItemViewHolder(layout);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (holder instanceof SelectTagAdapter.ItemViewHolder) {
                if (mSelectTagList.size() != 0) {
                    ((ItemViewHolder) holder).mPostTag.setText(mSelectTagList.get(position).getLabel_name());
                    ((SelectTagAdapter.ItemViewHolder) holder).mClosePic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mSelectTagList.remove(position);
                            notifyDataSetChanged();
                        }
                    });
                }
            }
        }

        @Override
        public int getItemCount() {
            return mSelectTagList.size();
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {

            private ImageView mClosePic;
            private TextView mPostTag;


            public ItemViewHolder(View itemView) {
                super(itemView);
                mClosePic = (ImageView) itemView.findViewById(R.id.iv_close);
                mPostTag = (TextView) itemView.findViewById(R.id.tv_post_tag);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(this);
    }


}
