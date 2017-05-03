package com.xingyuyou.xingyuyou.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.FileUtils;
import com.xingyuyou.xingyuyou.Utils.ImageUtils;
import com.xingyuyou.xingyuyou.Utils.MCUtils.HttpUtils;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.SDCardUtils;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.bean.GameGift;
import com.xingyuyou.xingyuyou.download.DownloadHelper;

import net.bither.util.NativeUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class TestActivity extends AppCompatActivity {
    /**
     * SD卡根目录
     */
    private final String externalStorageDirectory = Environment.getExternalStorageDirectory().getPath()+"/Pictures/";
    private static final int REQUEST_IMAGE = 11;
    private Button mOne;
    private HttpURLConnection mConnection;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.obj == null) return;
            Log.e("test", msg.obj.toString());
        }
    };
    private List<String> mImageList = new ArrayList<>();
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //压缩后保存临时文件目录
        File tempFile = new File(externalStorageDirectory);
        if(!tempFile.exists()){
            tempFile.mkdirs();
        }
        setContentView(R.layout.activity_test);
        mOne = (Button) findViewById(R.id.one);
        mImageView = (ImageView) findViewById(R.id.image);
        mOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scalImage();
            }
        });
    }

    private void scalImage() {
        MultiImageSelector.create(TestActivity.this)
                .showCamera(true)
                .single()
                .start(TestActivity.this, REQUEST_IMAGE);

    }

    private void testUM() {
        UMImage thumb = new UMImage(this, R.mipmap.ic_action_all_app);
        UMWeb web = new UMWeb("http://www.xingyuyou.com");
        web.setTitle("This is music title");//标题
        web.setThumb(thumb);  //缩略图
        web.setDescription("my description");//描述
        new ShareAction(TestActivity.this).withMedia(web)
                .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN)
                .setCallback(umShareListener).open();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                File file = new File(path.get(0));
                if (file.exists()) {
                    File file1 = new File(externalStorageDirectory+"tempCompress.jpg");
                        NativeUtil.compressBitmap(path.get(0), file1.getAbsolutePath());
                        mImageView.setImageBitmap(ImageUtils.getBitmap(file1.getAbsolutePath()));
                        Log.e("tupian", "_____大小：" + FileUtils.getFileSize(path.get(0)) + "-------" + FileUtils.getFileSize(file1.getAbsolutePath()));
                }
            }
        }
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //分享开始的回调
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat", "platform" + platform);

            Toast.makeText(TestActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            //Toast.makeText(TestActivity.this,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if (t != null) {
                Log.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(TestActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };


    private void updataDown() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("game_id", String.valueOf(146));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.POST(handler, XingYuInterface.UPDATA_DOWN, jsonObject.toString(), true);
    }

    private void getGift() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mid", String.valueOf(108));
            jsonObject.put("giftid", String.valueOf(11));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.POST(handler, XingYuInterface.RCEIVE_GIFT, jsonObject.toString(), false);
    }

    private void getData() {
        RequestParams params = new RequestParams(XingYuInterface.RCEIVE_GIFT);
        params.addParameter("mid", String.valueOf(108));
        params.addParameter("giftid", String.valueOf(11));
        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onCancelled(CancelledException arg0) {

            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                Log.e("gift", arg0.toString());

            }

            @Override
            public void onFinished() {
            }

            @Override
            public void onSuccess(String json) {
                Log.e("gift", json);
                handler.obtainMessage(1, json).sendToTarget();
            }

            @Override
            public boolean onCache(String json) {
                return true;
            }
        });
    }

    private void testDuandian() {
        try {
            URL url = new URL("http://download.apk8.com/d2/soft/meilijia.apk");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int fileLength = -1;
            Log.e("duandian", "11111");
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                fileLength = conn.getContentLength();
                Log.e("duandian", "::::" + fileLength);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
