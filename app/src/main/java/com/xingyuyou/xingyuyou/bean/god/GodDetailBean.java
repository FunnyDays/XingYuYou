package com.xingyuyou.xingyuyou.bean.god;

/**
 * Created by 24002 on 2017/5/23.
 */

public class GodDetailBean {

    /**
     * id : 3
     * image_info : http://xingyuyou.com/Uploads/Picture/2017-05-18/591cfbe0df3d2.png
     * newest_notice : 萨达
     * god_describe : 1111111
     * dateline : 1494993133
     */

    private String id;
    private String image_info;
    private String newest_notice;
    private String god_describe;
    private String dateline;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage_info() {
        return image_info;
    }

    public void setImage_info(String image_info) {
        this.image_info = image_info;
    }

    public String getNewest_notice() {
        return newest_notice;
    }

    public void setNewest_notice(String newest_notice) {
        this.newest_notice = newest_notice;
    }

    public String getGod_describe() {
        return god_describe;
    }

    public void setGod_describe(String god_describe) {
        this.god_describe = god_describe;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    @Override
    public String toString() {
        return "GodDetailBean{" +
                "id='" + id + '\'' +
                ", image_info='" + image_info + '\'' +
                ", newest_notice='" + newest_notice + '\'' +
                ", god_describe='" + god_describe + '\'' +
                ", dateline='" + dateline + '\'' +
                '}';
    }
}
