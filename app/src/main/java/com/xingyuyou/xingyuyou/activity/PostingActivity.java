package com.xingyuyou.xingyuyou.activity;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.weight.RichTextEditor;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import org.xutils.common.util.FileUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PostingActivity extends AppCompatActivity {

    /*
    * {
    "status": 1,
    "errorinfo": "获取社区分类列表成功",
    "data": [
        {
            "id": "1",
            "class_name": "cos",
            "class_image": "http://xingyuyou.com/Uploads/Picture/2017-03-15/58c89b9e9ac75.png"
        },
        {
            "id": "2",
            "class_name": "美女",
            "class_image": "http://xingyuyou.com/Uploads/Picture/2017-03-22/58d1d25c1bdf2.jpg"
        }
    ]
}
{
    "status": 1,
    "errorinfo": "获取社区分类列表成功",
    "data": null
}

    * */


    private static final int REQUEST_CODE_PICK_IMAGE = 1023;
    private static final int REQUEST_CODE_CAPTURE_CAMEIA = 1022;
    private RichTextEditor editor;
    private View btn1, btn2, btn3;
    private View.OnClickListener btnListener;

    private static final File PHOTO_DIR = new File(
            Environment.getExternalStorageDirectory() + "/DCIM/Camera");
    private File mCurrentPhotoFile;// 照相机拍照得到的图片

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting);
        editor = (RichTextEditor) findViewById(R.id.richEditor);
        btnListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                editor.hideKeyBoard();
                if (v.getId() == btn1.getId()) {
                    // 打开系统相册
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");// 相片类型
                    startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
                } else if (v.getId() == btn2.getId()) {
                    // 打开相机
                    openCamera();
                } else if (v.getId() == btn3.getId()) {
                    List<RichTextEditor.EditData> editList = editor.buildEditData();
                    // 下面的代码可以上传、或者保存，请自行实现
                    dealEditData(editList);
                }
            }
        };

        btn1 = findViewById(R.id.button1);
        btn2 = findViewById(R.id.button2);
        btn3 = findViewById(R.id.button3);

        btn1.setOnClickListener(btnListener);
        btn2.setOnClickListener(btnListener);
        btn3.setOnClickListener(btnListener);
    }

    /**
     * 负责处理编辑数据提交等事宜，请自行实现
     */
    protected void dealEditData(List<RichTextEditor.EditData> editList) {

        PostFormBuilder post = OkHttpUtils.post();
        for (int i = 0; i < editList.size()-1; i++) {
            File file = new File(editList.get(i).imagePath);
            Log.d("RichEditor", "文件名称：" + file.getName() +
                    "-----路径：" + file.getAbsolutePath() + "-----大小：" + file.length());
            String s = "posts_image";
            post.addFile(s+i, file.getName(), file);
        }
        post.url("http://xingyuyou.com/app.php/Community/post_posts")
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

    protected void openCamera() {
        try {
            // Launch camera to take photo for selected contact
            PHOTO_DIR.mkdirs();// 创建照片的存储目录
            mCurrentPhotoFile = new File(PHOTO_DIR, getPhotoFileName());// 给新照的照片文件命名
            final Intent intent = getTakePickIntent(mCurrentPhotoFile);
            startActivityForResult(intent, REQUEST_CODE_CAPTURE_CAMEIA);
        } catch (ActivityNotFoundException e) {
        }
    }

    public static Intent getTakePickIntent(File f) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        return intent;
    }

    /**
     * 用当前时间给取得的图片命名
     */
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date) + ".jpg";
    }

    //:http://xingyuyou.com/app.php/Community/post_posts
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_PICK_IMAGE) {
            Uri uri = data.getData();
            insertBitmap(getRealFilePath(uri));
        } else if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA) {
            insertBitmap(mCurrentPhotoFile.getAbsolutePath());
        }
    }

    /**
     * 添加图片到富文本剪辑器
     *
     * @param imagePath
     */
    private void insertBitmap(String imagePath) {
        editor.insertImage(imagePath);
    }

    /**
     * 根据Uri获取图片文件的绝对路径
     */
    public String getRealFilePath(final Uri uri) {
        if (null == uri) {
            return null;
        }

        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = getContentResolver().query(uri,
                    new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
}
