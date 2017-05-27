package com.xingyuyou.xingyuyou.bean.community;

/**
 * Created by Administrator on 2017/4/20.
 */

public class LabelClassBean {


    /**
     * id : 2
     * class_name : 美女
     * class_image : http://xingyuyou.com/Uploads/Picture/2017-03-22/58d1d25c1bdf2.jpg
     * describe : 驱蚊器好看
     * posts_num : 0
     */

    private String id;
    private String class_name;
    private String class_image;
    private String describe;
    private String posts_num;
    private String class_head_image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getClass_image() {
        return class_image;
    }

    public void setClass_image(String class_image) {
        this.class_image = class_image;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getPosts_num() {
        return posts_num;
    }

    public void setPosts_num(String posts_num) {
        this.posts_num = posts_num;
    }

    public String getClass_head_image() {
        return class_head_image;
    }

    public void setClass_head_image(String class_head_image) {
        this.class_head_image = class_head_image;
    }

    @Override
    public String toString() {
        return "LabelClassBean{" +
                "id='" + id + '\'' +
                ", class_name='" + class_name + '\'' +
                ", class_image='" + class_image + '\'' +
                ", describe='" + describe + '\'' +
                ", posts_num='" + posts_num + '\'' +
                '}';
    }
}
