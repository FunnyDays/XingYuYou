package com.xingyuyou.xingyuyou.activity;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.ConvertUtils;
import com.xingyuyou.xingyuyou.Utils.IntentUtils;
import com.xingyuyou.xingyuyou.Utils.StringUtils;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.weight.RichTextEditor;
import com.xingyuyou.xingyuyou.weight.dialog.LoadingDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.common.util.FileUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class PostingActivity extends AppCompatActivity {


    private Toolbar mToolbar;
    private LoadingDialog mDialog;
    private RelativeLayout mRlTagMore;
    private EditText mStTitle;
    private EditText mEtContent;
    private RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mRlTagMore = (RelativeLayout) findViewById(R.id.rl_tag_more);
        mStTitle = (EditText) findViewById(R.id.et_title);
        mEtContent = (EditText) findViewById(R.id.et_content);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        initRecyclerView();
        initToolbar();
        initTagMore();
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        ImageAdapter imageAdapter = new ImageAdapter();
        mRecyclerView.setAdapter(imageAdapter);
    }

    /**
     *   MultiImageSelector.create(PostingActivity.this)
     .showCamera(true)
     .single()
     .start(PostingActivity.this, 11);
     */


    private void initTagMore() {
        mRlTagMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PostingActivity.this, "heheh", Toast.LENGTH_SHORT).show();
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
                        mDialog = new LoadingDialog(PostingActivity.this, "正在上传，请稍等");
                        mDialog.showDialog();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 负责处理编辑数据提交等事宜
     */
    protected void dealEditData(List<RichTextEditor.EditData> editList) {
        if (StringUtils.isEmpty(mStTitle.getText().toString().trim())) {
            Toast.makeText(this, "请输入标题", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isEmpty(mEtContent.getText().toString().trim())) {
            Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("label_name", "zhangsan");
        map1.put("id", 24);
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("label_name", "lisi");
        map2.put("id", 25);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list.add(map1);
        list.add(map2);
        JSONArray array = new JSONArray(list);


        ArrayList<String> tags = new ArrayList<>();
        tags.add("cos");
        tags.add("动漫");
        tags.add("鸭子");
        tags.add("苹果");
        Map<String, String> params = new HashMap<String, String>();
        params.put("fid", "1");
        params.put("uid", "108");
        params.put("subject", "标题");
        params.put("message", "内容");
        params.put("tags", array.toString());
        Log.e("fabubbs", "test" + tags.toString());
        PostFormBuilder post = OkHttpUtils.post();
        for (int i = 0; i < editList.size() - 1; i++) {
            File file = new File(editList.get(i).imagePath);
            Log.d("fabubbs", "文件名称：" + file.getName() +
                    "-----路径：" + file.getAbsolutePath() + "-----大小：" + file.length() + "标签：" + tags.toString());
            String s = "posts_image";
            post.addFile(s + i, file.getName(), file);
        }
        post.url(XingYuInterface.POST_POSTS)
                .params(params)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        Log.e("fabubbs", e.getMessage() + " 失败id:" + id);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("fabubbs", " 成功id:" + id + response);
                    }
                });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("fabubbs", resultCode +"----"+ requestCode+"----"+ data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT));
    }

    private class ImageAdapter extends RecyclerView.Adapter {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }
}
