package com.xingyuyou.xingyuyou.bean;

/**
 * Created by Administrator on 2017/3/27.
 */

public class MyGameGift {


    /**
     * game_name : 成吉思汗(安卓版)
     * gift_name : 新手礼包
     * record_novice : 170322654SKTT47
     * icon : http://xingyuyou.com/Uploads/Picture/2017-03-22/58d1dd7f4754c.png
     * desribe : 破旧的银票*5、世传水晶*10、还童书*5

     * novice : 998
     * gift_id : 15
     */

    private String game_name;
    private String gift_name;
    private String record_novice;
    private String icon;
    private String desribe;
    private int novice;
    private String gift_id;

    public String getGame_name() {
        return game_name;
    }

    public void setGame_name(String game_name) {
        this.game_name = game_name;
    }

    public String getGift_name() {
        return gift_name;
    }

    public void setGift_name(String gift_name) {
        this.gift_name = gift_name;
    }

    public String getRecord_novice() {
        return record_novice;
    }

    public void setRecord_novice(String record_novice) {
        this.record_novice = record_novice;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDesribe() {
        return desribe;
    }

    public void setDesribe(String desribe) {
        this.desribe = desribe;
    }

    public int getNovice() {
        return novice;
    }

    public void setNovice(int novice) {
        this.novice = novice;
    }

    public String getGift_id() {
        return gift_id;
    }

    public void setGift_id(String gift_id) {
        this.gift_id = gift_id;
    }

    @Override
    public String toString() {
        return "MyGameGift{" +
                "game_name='" + game_name + '\'' +
                ", gift_name='" + gift_name + '\'' +
                ", record_novice='" + record_novice + '\'' +
                ", icon='" + icon + '\'' +
                ", desribe='" + desribe + '\'' +
                ", novice=" + novice +
                ", gift_id='" + gift_id + '\'' +
                '}';
    }
}
