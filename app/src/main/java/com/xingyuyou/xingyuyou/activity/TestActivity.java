package com.xingyuyou.xingyuyou.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.Utils.MCUtils.HttpUtils;
import com.xingyuyou.xingyuyou.Utils.MCUtils.UserUtils;
import com.xingyuyou.xingyuyou.Utils.net.XingYuInterface;
import com.xingyuyou.xingyuyou.bean.GameGift;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    private Button mOne;
    private HttpURLConnection mConnection;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.obj == null) return;
           Log.e("test",msg.obj.toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mOne = (Button) findViewById(R.id.one);
        mOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //getGift();
                getData();
            }
        });
    }

    private void getGift() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mid", String.valueOf(108));
            jsonObject.put("giftid", String.valueOf(11));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.POST(handler,XingYuInterface.RCEIVE_GIFT,jsonObject.toString(),false);
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
            URL url=new URL("http://download.apk8.com/d2/soft/meilijia.apk");
            HttpURLConnection conn=(HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int fileLength = -1;
            Log.e("duandian","11111");
            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                fileLength = conn.getContentLength();
                Log.e("duandian","::::"+fileLength);
            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
