package com.xingyuyou.xingyuyou.bean.hotgame;

/**
 * Created by Administrator on 2017/3/15.
 */

public class HotGameBean {
    public HotGameBean() {
    }

    public HotGameBean(String id, String game_name, String icon, String game_size, String game_type_id, String game_baoming, String dow_num, String features, String introduction, String and_dow_address, String add_game_address, String recommend_status) {
        this.id = id;
        this.game_name = game_name;
        this.icon = icon;
        this.game_size = game_size;
        this.game_type_id = game_type_id;
        this.game_baoming = game_baoming;
        this.dow_num = dow_num;
        this.features = features;
        this.introduction = introduction;
        this.and_dow_address = and_dow_address;
        this.add_game_address = add_game_address;
        this.recommend_status = recommend_status;
    }

    /**
     * id : 20
     * game_name : 地铁跑酷(安卓版)
     * icon : http://xingyuyou.com/Uploads/Picture/2017-03-08/58bfa615a6f3c.png
     * game_size : 61.95MB
     * game_type_id : 5
     * dow_num : 0
     * introduction : 地铁跑酷是一款超炫酷的3D竖版跑酷手游，总注册用户高达2.4亿，游戏画面绚丽精致，色彩丰富让人感觉舒服，操作上非常流畅，本次版本更新来到南半球非洲国度马达加斯加，一起来畅游这自然风光美美的非洲大岛吧！破解的游戏版本是无限金币和无限钥匙，叫上小伙伴来下载随意玩吧。
     * and_dow_address : http://xingyuyou.com./Uploads/SourcePack/20170308144303_918.apk
     * add_game_address : http://xingyuyou.com./Uploads/SourcePack/20170308144303_918.apk
     * recommend_status : 2
     */

    private String id;
    private String game_name;
    private String icon;
    private String game_size;
    private String game_type_id;
    private String game_baoming;
    private String dow_num;
    private String features;
    private String introduction;
    private String and_dow_address;
    private String add_game_address;
    private String recommend_status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGame_name() {
        return game_name;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public void setGame_name(String game_name) {
        this.game_name = game_name;
    }
    public String getGame_baoming() {
        return game_baoming;
    }

    public void setGame_baoming(String game_baoming) {
        this.game_baoming = game_baoming;
    }
    public String getAdd_game_address() {
        return add_game_address;
    }

    public void setAdd_game_address(String add_game_address) {
        this.add_game_address = add_game_address;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getGame_size() {
        return game_size;
    }

    public void setGame_size(String game_size) {
        this.game_size = game_size;
    }

    public String getGame_type_id() {
        return game_type_id;
    }

    public void setGame_type_id(String game_type_id) {
        this.game_type_id = game_type_id;
    }

    public String getDow_num() {
        return dow_num;
    }

    public void setDow_num(String dow_num) {
        this.dow_num = dow_num;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getAnd_dow_address() {
        return and_dow_address;
    }

    public void setAnd_dow_address(String and_dow_address) {
        this.and_dow_address = and_dow_address;
    }

    public String getRecommend_status() {
        return recommend_status;
    }

    public void setRecommend_status(String recommend_status) {
        this.recommend_status = recommend_status;
    }

    @Override
    public String toString() {
        return "HotGameBean{" +
                "id='" + id + '\'' +
                ", game_name='" + game_name + '\'' +
                ", icon='" + icon + '\'' +
                ", game_size='" + game_size + '\'' +
                ", game_type_id='" + game_type_id + '\'' +
                ", game_baoming='" + game_baoming + '\'' +
                ", dow_num='" + dow_num + '\'' +
                ", features='" + features + '\'' +
                ", introduction='" + introduction + '\'' +
                ", and_dow_address='" + and_dow_address + '\'' +
                ", add_game_address='" + add_game_address + '\'' +
                ", recommend_status='" + recommend_status + '\'' +
                '}';
    }
}
