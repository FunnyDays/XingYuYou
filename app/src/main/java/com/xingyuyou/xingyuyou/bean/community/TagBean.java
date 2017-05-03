package com.xingyuyou.xingyuyou.bean.community;

/**
 * Created by Administrator on 2017/5/3.
 */

public class TagBean {

    /**
     * id : 6
     * label_name : 强吻
     */

    private String id;
    private String label_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel_name() {
        return label_name;
    }

    public void setLabel_name(String label_name) {
        this.label_name = label_name;
    }

    @Override
    public String toString() {
        return "TagBean{" +
                "id='" + id + '\'' +
                ", label_name='" + label_name + '\'' +
                '}';
    }
}
