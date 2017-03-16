package com.xingyuyou.xingyuyou.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.xingyuyou.xingyuyou.R;

import java.net.HttpURLConnection;
import java.net.URL;

public class TestActivity extends AppCompatActivity {

    private Button mOne;
    private HttpURLConnection mConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mOne = (Button) findViewById(R.id.one);
        mOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testDuandian();
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
