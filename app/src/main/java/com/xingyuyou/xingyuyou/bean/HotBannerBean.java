package com.xingyuyou.xingyuyou.bean;

/**
 * Created by Administrator on 2017/3/15.
 */

public class HotBannerBean {

    /**
     * data : http://xingyuyou.com/Uploads/Picture/2017-03-06/58bcc66106b1a.jpg
     * url : www.xingyuyou.com
     */

    private String data;
    private String url;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "HotBannerBean{" +
                "data='" + data + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
