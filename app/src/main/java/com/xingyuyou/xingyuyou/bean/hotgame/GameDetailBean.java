package com.xingyuyou.xingyuyou.bean.hotgame;

import java.util.List;

/**
 * Created by Administrator on 2017/3/21.
 */

public class GameDetailBean {

    /**
     * id : 144
     * create_time : 1491376685
     * language :
     * game_name : 萌萌找茬
     * cover : http://xingyuyou.com
     * version : 1.0.1
     * icon : http://xingyuyou.com/Uploads/Picture/2017-04-05/58e49a1de675c.png
     * game_baoming : jp.yukienterprise.mistake
     * screenshot : ["http://xingyuyou.com/Uploads/Picture/2017-04-05/58e49a214ffe2.jpg","http://xingyuyou.com/Uploads/Picture/2017-04-05/58e49a250d73f.jpg","http://xingyuyou.com/Uploads/Picture/2017-04-05/58e49a2814e2d.jpg","http://xingyuyou.com/Uploads/Picture/2017-04-05/58e49a2b08d27.jpg"]
     * game_type_name : 休闲益智
     * game_size : 22M
     * dow_num : 9
     * and_dow_address : http://xingyuyou.com
     * add_game_address : http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/a%E5%8D%95%E6%9C%BA2/%E8%90%8C%E8%90%8C%E6%89%BE%E8%8C%AC.apk
     * introduction : 你需要找到两张图之间的区别，完成关卡来解锁妹子的CG。

     图是会动的，有些不同点需要结合动图来找，比如第一关大腿根部的痣（痴汉脸）

     每一关能犯的错误是有限的，但是不同点是一样的，可以重复某关直到通关为止

     话说，只有我一个人觉得这个妹子长得像某校园里番的主角么？
     * recommend_status : 0
     * open_name : null
     */

    private String id;
    private String create_time;
    private String language;
    private String game_name;
    private String cover;
    private String version;
    private String icon;
    private String game_baoming;
    private String game_type_name;
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

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getGame_name() {
        return game_name;
    }

    public void setGame_name(String game_name) {
        this.game_name = game_name;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getVersion() {
        return version;
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

    public String getGame_baoming() {
        return game_baoming;
    }

    public void setGame_baoming(String game_baoming) {
        this.game_baoming = game_baoming;
    }

    public String getGame_type_name() {
        return game_type_name;
    }

    public void setGame_type_name(String game_type_name) {
        this.game_type_name = game_type_name;
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

    public String getAdd_game_address() {
        return add_game_address;
    }

    public void setAdd_game_address(String add_game_address) {
        this.add_game_address = add_game_address;
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
}
