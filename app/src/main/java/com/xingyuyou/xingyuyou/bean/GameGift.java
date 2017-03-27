package com.xingyuyou.xingyuyou.bean;

/**
 * Created by Administrator on 2017/3/27.
 */

public class GameGift {


    /**
     * giftid : 14
     * game_id : 34
     * game_name : 钢铁雄狮(安卓版)
     * giftbag_name : 新手礼包
     * icon : http://xingyuyou.com/Uploads/Picture/2017-03-23/58d39094bcc44.png
     * novice : 1001
     * game_size : 186.3MB
     * desribe : 金币*10000 普通技能书*20 普通军牌*4
     */

    private String giftid;
    private String game_id;
    private String game_name;
    private String giftbag_name;
    private String icon;
    private int novice;
    private String game_size;
    private String desribe;

    public String getGiftid() {
        return giftid;
    }

    public void setGiftid(String giftid) {
        this.giftid = giftid;
    }

    public String getGame_id() {
        return game_id;
    }

    public void setGame_id(String game_id) {
        this.game_id = game_id;
    }

    public String getGame_name() {
        return game_name;
    }

    public void setGame_name(String game_name) {
        this.game_name = game_name;
    }

    public String getGiftbag_name() {
        return giftbag_name;
    }

    public void setGiftbag_name(String giftbag_name) {
        this.giftbag_name = giftbag_name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getNovice() {
        return novice;
    }

    public void setNovice(int novice) {
        this.novice = novice;
    }

    public String getGame_size() {
        return game_size;
    }

    public void setGame_size(String game_size) {
        this.game_size = game_size;
    }

    public String getDesribe() {
        return desribe;
    }

    public void setDesribe(String desribe) {
        this.desribe = desribe;
    }

    @Override
    public String toString() {
        return "GameGift{" +
                "giftid='" + giftid + '\'' +
                ", game_id='" + game_id + '\'' +
                ", game_name='" + game_name + '\'' +
                ", giftbag_name='" + giftbag_name + '\'' +
                ", icon='" + icon + '\'' +
                ", novice=" + novice +
                ", game_size='" + game_size + '\'' +
                ", desribe='" + desribe + '\'' +
                '}';
    }
}
