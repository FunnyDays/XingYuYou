package com.xingyuyou.xingyuyou.bean.community;

import java.io.Serializable;

/**
 * Created by 24002 on 2017/6/19.
 */

public class ThumbnailImageBean implements Serializable {
    /**
     * thumbnail_image : http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/2017-06-19/8175947869079876.jpg
     * thumbnail_width : 960
     * thumbnail_height : 952
     */

    private String thumbnail_image;
    private int thumbnail_width;
    private int thumbnail_height;

    public String getThumbnail_image() {
        return thumbnail_image;
    }

    public void setThumbnail_image(String thumbnail_image) {
        this.thumbnail_image = thumbnail_image;
    }

    public int getThumbnail_width() {
        return thumbnail_width;
    }

    public void setThumbnail_width(int thumbnail_width) {
        this.thumbnail_width = thumbnail_width;
    }

    public int getThumbnail_height() {
        return thumbnail_height;
    }

    public void setThumbnail_height(int thumbnail_height) {
        this.thumbnail_height = thumbnail_height;
    }

    @Override
    public String toString() {
        return "ThumbnailImageBean{" +
                "thumbnail_image='" + thumbnail_image + '\'' +
                ", thumbnail_width=" + thumbnail_width +
                ", thumbnail_height=" + thumbnail_height +
                '}';
    }
}
