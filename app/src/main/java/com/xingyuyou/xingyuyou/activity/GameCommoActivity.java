package com.xingyuyou.xingyuyou.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.IntentUtils;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.StringUtils;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.weight.RatingBar;
import com.xingyuyou.xingyuyou.weight.dialog.CustomDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class GameCommoActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private EditText mEditText;
    private int mMRatingCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_commo);
        initView();
        initToolbar();
    }
    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameCommoActivity.this,GameDetailActivity.class);
                intent.putExtra("game_id",getIntent().getStringExtra("game_id"));
                intent.putExtra("game_name",getIntent().getStringExtra("game_name"));
                intent.putExtra("game_cover_pic",getIntent().getStringExtra("game_cover_pic"));
                GameCommoActivity.this.startActivity(intent);
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

    private void initView() {
        mEditText = (EditText) findViewById(R.id.editText);
        RatingBar ratingBar= (RatingBar) findViewById(R.id.rb);
        ratingBar.setClickable(true);//设置可否点击
        ratingBar.setStar(0f);//设置显示的星星个数
        ratingBar.setStepSize(RatingBar.StepSize.Full);//设置每次点击增加一颗星还是半颗星
        ratingBar.setOnRatingChangeListener(new RatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(float ratingCount) {//点击星星变化后选中的个数
                mMRatingCount = (int)ratingCount;
            }
        });
    }
    private void dealEditData() {
        if (mMRatingCount==0){
            Toast.makeText(this, "心级不能为零", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isEmpty(mEditText.getText().toString().trim())){
            Toast.makeText(this, "内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        final CustomDialog customDialog = new CustomDialog(GameCommoActivity.this, "正在提交评价");
        customDialog.showDialog();
        Log.e("weiwei", "dealEditData: "+ String.valueOf(mMRatingCount));
        OkHttpUtils.post()//
                .addParams("uid", UserUtils.getUserId())
                .addParams("gid", getIntent().getStringExtra("game_id"))
                .addParams("star_num", String.valueOf(mMRatingCount))
                .addParams("comment", mEditText.getText().toString().trim())
                .url(XingYuInterface.GET_EVALUATE)
                .tag(this)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        // Log.e("hot", e.toString() + ":e");
                        customDialog.dismissDialog();
                        customDialog.CancelDialog();
                        finish();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                       // handler.obtainMessage(2, response).sendToTarget();
                        customDialog.dismissDialog();
                        customDialog.CancelDialog();
                        Intent intent = new Intent(GameCommoActivity.this,GameDetailActivity.class);
                        intent.putExtra("game_id",getIntent().getStringExtra("game_id"));
                        intent.putExtra("game_name",getIntent().getStringExtra("game_name"));
                        intent.putExtra("game_cover_pic",getIntent().getStringExtra("game_cover_pic"));
                        GameCommoActivity.this.startActivity(intent);
                        finish();

                    }
                });
    }
}
