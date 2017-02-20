package com.xingyuyou.xingyuyou.Utils;


import com.xingyuyou.xingyuyou.Utils.net.XunleicangInterface;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by LG on 2016/11/27.
 */

public class UrlUtil  {
    public static String getClassesUrl(int id, int page) {//http://www.xunleicang.com/index.php?s=vod-show-id-10-mcid--lz--area--year--letter--order-addtime-picm-1-p-2.html
        return XunleicangInterface.HOST + "index.php?s=vod-show-id-" + id + "-mcid--lz--area--year--letter--order-addtime-picm-1-p-" + page + ".html";
    }

    public static String getClassesUrl(String keyWords, int page) throws UnsupportedEncodingException {//http://www.xunleicang.com/index.php?s=vod-show-id-10-mcid--lz--area--year--letter--order-addtime-picm-1-p-2.html
        return "http://www.xunleicang.com/vod-search-id-" + URLEncoder.encode(keyWords, "UTF-8") + "-p-" + page + ".html";
    }
}
