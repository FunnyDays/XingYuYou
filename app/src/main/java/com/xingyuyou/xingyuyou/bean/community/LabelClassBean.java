package com.xingyuyou.xingyuyou.bean.community;

/**
 * Created by Administrator on 2017/4/20.
 */

public class LabelClassBean {

    /**
     * id : 1
     * class_name : cos
     * class_image : http://xingyuyou.com/Uploads/Picture/2017-03-15/58c89b9e9ac75.png
     */

    private String id;
    private String class_name;
    private String class_image;

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

    @Override
    public String toString() {
        return "LabelClassBean{" +
                "id='" + id + '\'' +
                ", class_name='" + class_name + '\'' +
                ", class_image='" + class_image + '\'' +
                '}';
    }
}
