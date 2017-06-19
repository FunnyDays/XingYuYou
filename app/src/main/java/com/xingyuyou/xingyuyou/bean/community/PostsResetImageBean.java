package com.xingyuyou.xingyuyou.bean.community;

import java.io.Serializable;

/**
 * Created by 24002 on 2017/6/19.
 */

public class PostsResetImageBean implements Serializable{
    /**
     * posts_image : http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/2017-06-19/5947869079876.jpg
     * posts_image_width : 1000
     * posts_image_height : 992
     * posts_image_size : 243085
     */

    private String posts_image;
    private int posts_image_width;
    private int posts_image_height;
    private int posts_image_size;

    public String getPosts_image() {
        return posts_image;
    }

    public void setPosts_image(String posts_image) {
        this.posts_image = posts_image;
    }

    public int getPosts_image_width() {
        return posts_image_width;
    }

    public void setPosts_image_width(int posts_image_width) {
        this.posts_image_width = posts_image_width;
    }

    public int getPosts_image_height() {
        return posts_image_height;
    }

    public void setPosts_image_height(int posts_image_height) {
        this.posts_image_height = posts_image_height;
    }

    public int getPosts_image_size() {
        return posts_image_size;
    }

    public void setPosts_image_size(int posts_image_size) {
        this.posts_image_size = posts_image_size;
    }

    @Override
    public String toString() {
        return "PostsResetImageBean{" +
                "posts_image='" + posts_image + '\'' +
                ", posts_image_width=" + posts_image_width +
                ", posts_image_height=" + posts_image_height +
                ", posts_image_size=" + posts_image_size +
                '}';
    }
}
