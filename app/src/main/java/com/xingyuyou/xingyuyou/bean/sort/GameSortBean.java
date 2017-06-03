package com.xingyuyou.xingyuyou.bean.sort;

/**
 * Created by Administrator on 2017/5/5.
 */

public class GameSortBean {

    /**
     * id : 18
     * type_name : 网络游戏
     * icon : http://xingyuyou.com/Public/app/posts_image/5909719993870.jpg
     * introduce : 超级棒的哈哈
     * download_num : 232
     * hot_num : 999
     */

    private String id;
    private String type_name;
    private String icon;
    private String cover;
    private String introduce;
    private String download_num;
    private String hot_num;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }
    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getDownload_num() {
        return download_num;
    }

    public void setDownload_num(String download_num) {
        this.download_num = download_num;
    }

    public String getHot_num() {
        return hot_num;
    }

    public void setHot_num(String hot_num) {
        this.hot_num = hot_num;
    }

    @Override
    public String toString() {
        return "GameSortBean{" +
                "id='" + id + '\'' +
                ", type_name='" + type_name + '\'' +
                ", icon='" + icon + '\'' +
                ", introduce='" + introduce + '\'' +
                ", download_num='" + download_num + '\'' +
                ", hot_num='" + hot_num + '\'' +
                '}';
    }
}
