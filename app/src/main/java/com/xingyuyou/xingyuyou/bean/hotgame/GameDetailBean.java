package com.xingyuyou.xingyuyou.bean.hotgame;

import java.util.List;

/**
 * Created by Administrator on 2017/3/21.
 */

public class GameDetailBean {

    /**
     * id : 20
     * game_name : 地铁跑酷(安卓版)
     * version : 2.47.0
     * icon : http://xingyuyou.com/Uploads/Picture/2017-03-08/58bfa615a6f3c.png
     * screenshot : ["http://xingyuyou.com/Uploads/Picture/2017-03-08/58bfa6d4ac093.jpg","http://xingyuyou.com/Uploads/Picture/2017-03-08/58bfa6d8c8d0a.jpg","http://xingyuyou.com/Uploads/Picture/2017-03-08/58bfa6dd4233e.jpg","http://xingyuyou.com/Uploads/Picture/2017-03-08/58bfa6e0a0f27.jpg","http://xingyuyou.com/Uploads/Picture/2017-03-08/58bfa6e3e49d3.jpg"]
     * game_type_id : 28
     * game_size : 61.95MB
     * dow_num : 0
     * and_dow_address : http://xingyuyou.com./Uploads/SourcePack/20170308144303_918.apk
     * add_game_address : http://xingyuyou.com./Uploads/SourcePack/20170308144303_918.apk
     * introduction : 地铁跑酷是一款超炫酷的3D竖版跑酷手游，总注册用户高达2.4亿，游戏画面绚丽精致，色彩丰富让人感觉舒服，操作上非常流畅，本次版本更新来到南半球非洲国度马达加斯加，一起来畅游这自然风光美美的非洲大岛吧！破解的游戏版本是无限金币和无限钥匙，叫上小伙伴来下载随意玩吧。
     * recommend_status : 2
     * open_name : null
     */

    private String id;
    private String game_name;
    private String version;
    private String icon;
    private String cover;
    private String game_baoming;
    private String game_type_id;
    private String game_size;
    private String dow_num;
    private String and_dow_address;
    private String add_game_address;
    private String introduction;
    private String recommend_status;
    private Object open_name;
    private List<String> screenshot;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGame_name() {
        return game_name;
    }

    public void setGame_name(String game_name) {
        this.game_name = game_name;
    }
    public String getAdd_game_address() {
        return add_game_address;
    }

    public void setAdd_game_address(String add_game_address) {
        this.add_game_address = add_game_address;
    }
    public String getVersion() {
        return version;
    }

    public String getGame_baoming() {
        return game_baoming;
    }

    public void setGame_baoming(String game_baoming) {
        this.game_baoming = game_baoming;
    }
    public void setVersion(String version) {
        this.version = version;
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
    public String getGame_type_id() {
        return game_type_id;
    }

    public void setGame_type_id(String game_type_id) {
        this.game_type_id = game_type_id;
    }

    public String getGame_size() {
        return game_size;
    }

    public void setGame_size(String game_size) {
        this.game_size = game_size;
    }

    public String getDow_num() {
        return dow_num;
    }

    public void setDow_num(String dow_num) {
        this.dow_num = dow_num;
    }

    public String getAnd_dow_address() {
        return and_dow_address;
    }

    public void setAnd_dow_address(String and_dow_address) {
        this.and_dow_address = and_dow_address;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getRecommend_status() {
        return recommend_status;
    }

    public void setRecommend_status(String recommend_status) {
        this.recommend_status = recommend_status;
    }

    public Object getOpen_name() {
        return open_name;
    }

    public void setOpen_name(Object open_name) {
        this.open_name = open_name;
    }

    public List<String> getScreenshot() {
        return screenshot;
    }

    public void setScreenshot(List<String> screenshot) {
        this.screenshot = screenshot;
    }

    @Override
    public String toString() {
        return "GameDetailBean{" +
                "id='" + id + '\'' +
                ", game_name='" + game_name + '\'' +
                ", version='" + version + '\'' +
                ", icon='" + icon + '\'' +
                ", game_baoming='" + game_baoming + '\'' +
                ", game_type_id='" + game_type_id + '\'' +
                ", game_size='" + game_size + '\'' +
                ", dow_num='" + dow_num + '\'' +
                ", and_dow_address='" + and_dow_address + '\'' +
                ", add_game_address='" + add_game_address + '\'' +
                ", introduction='" + introduction + '\'' +
                ", recommend_status='" + recommend_status + '\'' +
                ", open_name=" + open_name +
                ", screenshot=" + screenshot +
                '}';
    }
}
