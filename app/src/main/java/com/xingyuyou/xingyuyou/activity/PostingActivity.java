package com.xingyuyou.xingyuyou.activity;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.IntentUtils;
import com.xingyuyou.xingyuyou.Utils.StringUtils;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.bean.community.TagBean;
import com.xingyuyou.xingyuyou.fragment.TwoFragment;
import com.xingyuyou.xingyuyou.weight.dialog.CustomDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import net.bither.util.NativeUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class PostingActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE = 2;
    private static final int TYPE_FOOTER = 21;
    private Toolbar mToolbar;
    private CustomDialog mDialog;
    private RelativeLayout mRlTagMore;
    private EditText mStTitle;
    private EditText mEtContent;
    private RecyclerView mRecyclerView;
    private ArrayList<String> mImageList = new ArrayList();
    private ImageAdapter mImageAdapter;
    private String mPostTags;
    private RelativeLayout mRlCommMore;
    private String mPostCommId;
    private Map map = new HashMap<String, String>();
    private String[] mPostTagsLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mRlTagMore = (RelativeLayout) findViewById(R.id.rl_tag_more);
        mRlCommMore = (RelativeLayout) findViewById(R.id.rl_comm_more);
        mStTitle = (EditText) findViewById(R.id.et_title);
        mEtContent = (EditText) findViewById(R.id.et_content);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        initRecyclerView();
        initToolbar();
        initTagMore();
        initCommMore();
    }

    private void initCommMore() {
        mRlCommMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.startActivity(PostingActivity.this, SelectCommTagActivity.class);
            }
        });
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mImageAdapter = new ImageAdapter();
        mRecyclerView.setAdapter(mImageAdapter);
    }

    private void initTagMore() {
        mRlTagMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtils.startActivity(PostingActivity.this, SelectTagActivity.class);
            }
        });
    }

    private void initToolbar() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mToolbar.inflateMenu(R.menu.post_activity_menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ab_send:
                        dealEditData();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (!StringUtils.isEmpty(intent.getStringExtra("PostTags"))) {
            map.put("PostTags", intent.getStringExtra("PostTags"));
            mPostTagsLists = intent.getStringArrayExtra("PostTagsList");
            TextView tv_select_tag = (TextView) findViewById(R.id.tv_select_tag);
            TextView tv_tag_one = (TextView) findViewById(R.id.tv_tag_one);
            tv_tag_one.setText("");
            TextView tv_tag_two = (TextView) findViewById(R.id.tv_tag_two);
            tv_tag_two.setText("");
            TextView tv_tag_three = (TextView) findViewById(R.id.tv_tag_three);
            tv_tag_three.setText("");
            TextView tv_tag_four = (TextView) findViewById(R.id.tv_tag_four);
            tv_tag_four.setText("");
            TextView tv_tag_five = (TextView) findViewById(R.id.tv_tag_five);
            tv_tag_five.setText("");
            for (int i = 0; i < mPostTagsLists.length; i++) {
                if (i == 0) {
                    tv_select_tag.setVisibility(View.GONE);
                    tv_tag_one.setText(mPostTagsLists[0]);
                    tv_tag_one.setVisibility(View.VISIBLE);
                }
                if (i == 1) {
                    tv_tag_two.setText(mPostTagsLists[1]);
                    tv_tag_two.setVisibility(View.VISIBLE);
                }
                if (i == 2) {
                    tv_tag_three.setText(mPostTagsLists[2]);
                    tv_tag_three.setVisibility(View.VISIBLE);
                }
                if (i == 3) {
                    tv_tag_four.setText(mPostTagsLists[3]);
                    tv_tag_four.setVisibility(View.VISIBLE);
                }
                if (i == 4) {
                    tv_tag_five.setText(mPostTagsLists[4]);
                    tv_tag_five.setVisibility(View.VISIBLE);
                }
            }
        }
        if (!StringUtils.isEmpty(intent.getStringExtra("PostCommId"))) {
            map.put("PostCommId", intent.getStringExtra("PostCommId"));
            TextView tv_tag_commu = (TextView) findViewById(R.id.tv_tag_commu);
            tv_tag_commu.setVisibility(View.VISIBLE);
            tv_tag_commu.setText(intent.getStringExtra("PostCommClassName"));
        }

    }

    /**
     * 负责处理编辑数据提交等事宜
     */
    protected void dealEditData() {
        if (StringUtils.isEmpty(mStTitle.getText().toString().trim())) {
            Toast.makeText(this, "请输入标题", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isEmpty(mEtContent.getText().toString().trim())) {
            Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
            return;
        }

        if (StringUtils.isEmpty((String) map.get("PostTags"))) {
            Toast.makeText(this, "请选择标签", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isEmpty((String) map.get("PostCommId"))) {
            Toast.makeText(this, "请选发帖社区", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mImageList.size() == 0) {
            Toast.makeText(this, "请至少选择一张图片", Toast.LENGTH_SHORT).show();
            return;
        }
        mDialog = new CustomDialog(PostingActivity.this, "正在上传，请稍等");
        mDialog.showDialog();


        Map<String, String> params = new HashMap<String, String>();
        params.put("fid", (String) map.get("PostCommId"));
        params.put("uid", "105");
        params.put("subject", mStTitle.getText().toString().trim());
        params.put("message", mEtContent.getText().toString().trim());
        params.put("tags", (String) map.get("PostTags"));

        PostFormBuilder post = OkHttpUtils.post();
        for (int i = 0; i < mImageList.size(); i++) {
            File file = new File(mImageList.get(i));
            if (file.exists()) {
                File file1 = new File(getExternalCacheDir() + "/tempCompress" + i + ".jpg");
                NativeUtil.compressBitmap(mImageList.get(i), file1.getAbsolutePath());
                String s = "posts_image";
                post.addFile(s + i, file1.getName(), file1);
            }

        }
        post.url(XingYuInterface.POST_POSTS)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        mDialog.dismissDialog();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mDialog.dismissDialog();
                        Intent intent = new Intent("updateFragment");
                        LocalBroadcastManager.getInstance(PostingActivity.this)
                                .sendBroadcast(intent);
                        Toast.makeText(PostingActivity.this, "发帖成功", Toast.LENGTH_SHORT).show();
                        PostingActivity.this.finish();
                    }
                });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                mImageList.addAll(path);
                mImageAdapter.notifyDataSetChanged();
            }
        }
    }

    private class ImageAdapter extends RecyclerView.Adapter {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View layout = LayoutInflater.from(PostingActivity.this).inflate(R.layout.item_post_image, parent, false);
            return new ItemViewHolder(layout);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (getItemViewType(position) == TYPE_FOOTER) {
                ((ItemViewHolder) holder).mClosePic.setVisibility(View.GONE);
                ((ItemViewHolder) holder).mPostImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mImageList.size() >= 5) {
                            Toast.makeText(PostingActivity.this, "只能发布五张图片", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        MultiImageSelector.create(PostingActivity.this)
                                .showCamera(true)
                                .count(5)
                                .start(PostingActivity.this, REQUEST_IMAGE);
                    }
                });
            }
            if (holder instanceof ItemViewHolder) {
                if (mImageList.size() != 0) {
                    if (getItemViewType(position) != TYPE_FOOTER) {
                        Glide.with(PostingActivity.this)
                                .load(mImageList.get(position))
                                .into(((ItemViewHolder) holder).mPostImage);
                        ((ItemViewHolder) holder).mClosePic.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mImageList.remove(position);
                                notifyDataSetChanged();
                            }
                        });
                    }
                }
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == getItemCount() - 1) {
                //最后一个,应该加载Footer
                return TYPE_FOOTER;
            }
            return super.getItemViewType(position);
        }

        @Override
        public int getItemCount() {
            return mImageList.size() == 0 ? 1 : mImageList.size() + 1;
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {

            private ImageView mClosePic;
            private ImageView mPostImage;
            public ItemViewHolder(View itemView) {
                super(itemView);
               /* if (itemView == mFooterView) {
                    return;
                }*/
                mClosePic = (ImageView) itemView.findViewById(R.id.iv_close);
                mPostImage = (ImageView) itemView.findViewById(R.id.iv_post_image);
            }
        }
    }
}
