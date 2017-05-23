package com.xingyuyou.xingyuyou.bean.god;

/**
 * Created by 24002 on 2017/5/23.
 */

public class GodBean {

    /**
     * id : 3
     * god_image : http://xingyuyou.com
     */

    private String id;
    private String god_image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGod_image() {
        return god_image;
    }

    public void setGod_image(String god_image) {
        this.god_image = god_image;
    }

    @Override
    public String toString() {
        return "GodBean{" +
                "id='" + id + '\'' +
                ", god_image='" + god_image + '\'' +
                '}';
    }
}
