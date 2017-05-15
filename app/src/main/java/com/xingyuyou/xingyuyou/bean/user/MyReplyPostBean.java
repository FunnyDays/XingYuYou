package com.xingyuyou.xingyuyou.bean.user;

/**
 * Created by 24002 on 2017/5/9.
 */

public class MyReplyPostBean {


    /**
     * id : 18
     * uid : 105
     * re_conenct : 原帖：第二条数据
     * conenct : 呵呵
     * floor : 3
     * dateline : 1494583835
     * nickname : 噢耶了了了
     * head_image : http://xingyuyou.com/Public/app/user_image/59100e5191d7c.jpg
     */

    private String id;
    private String tid;
    private String uid;
    private String re_conenct;
    private String conenct;
    private String floor;
    private String dateline;
    private String nickname;
    private String head_image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRe_conenct() {
        return re_conenct;
    }

    public void setRe_conenct(String re_conenct) {
        this.re_conenct = re_conenct;
    }

    public String getConenct() {
        return conenct;
    }

    public void setConenct(String conenct) {
        this.conenct = conenct;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHead_image() {
        return head_image;
    }

    public void setHead_image(String head_image) {
        this.head_image = head_image;
    }
}
