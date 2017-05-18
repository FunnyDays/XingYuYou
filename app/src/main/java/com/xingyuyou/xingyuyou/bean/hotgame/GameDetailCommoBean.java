package com.xingyuyou.xingyuyou.bean.hotgame;

/**
 * Created by 24002 on 2017/5/18.
 */

public class GameDetailCommoBean {

    /**
     * id : 7
     * gid : 18
     * star_num : 4
     * comment : 啊啊啊啊
     * laud_num : 0
     * dateline : 1494388552
     * uid : 201
     * nickname : 625678
     * head_image : http://xingyuyou.com/Public/app/user_image/59100e5191d7c.jpg
     * laud_status : 0
     */

    private String id;
    private String gid;
    private String star_num;
    private String comment;
    private String laud_num;
    private String dateline;
    private String uid;
    private String nickname;
    private String head_image;
    private int laud_status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getStar_num() {
        return star_num;
    }

    public void setStar_num(String star_num) {
        this.star_num = star_num;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getLaud_num() {
        return laud_num;
    }

    public void setLaud_num(String laud_num) {
        this.laud_num = laud_num;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public int getLaud_status() {
        return laud_status;
    }

    public void setLaud_status(int laud_status) {
        this.laud_status = laud_status;
    }

    @Override
    public String toString() {
        return "GameDetailCommoBean{" +
                "id='" + id + '\'' +
                ", gid='" + gid + '\'' +
                ", star_num='" + star_num + '\'' +
                ", comment='" + comment + '\'' +
                ", laud_num='" + laud_num + '\'' +
                ", dateline='" + dateline + '\'' +
                ", uid='" + uid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", head_image='" + head_image + '\'' +
                ", laud_status=" + laud_status +
                '}';
    }
}
