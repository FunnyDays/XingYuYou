package com.xingyuyou.xingyuyou.bean.community;

import java.util.List;

/**
 * Created by 24002 on 2017/5/8.
 */

public class PostCommoBean {

    /**
     * level : 1
     * uid : 108
     * nickname : 99
     * head_image : http://xingyuyou.com/Uploads/Picture/2017-03-15/
     * id : 58
     * tid : 102
     * replies_content : 就几级了
     * imgarr : ["http://xingyuyou.com/Public/app/replies_image/591031dd04fb1.png"]
     * dateline : 1494233565
     * floor_num : 3
     * laud_count : 0
     * laud_status :
     */

    private int level;
    private String uid;
    private String nickname;
    private String head_image;
    private String id;
    private String tid;
    private String replies_content;
    private String dateline;
    private String floor_num;
    private String laud_count;
    private String laud_status;
    private List<String> imgarr;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
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

    public String getReplies_content() {
        return replies_content;
    }

    public void setReplies_content(String replies_content) {
        this.replies_content = replies_content;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    public String getFloor_num() {
        return floor_num;
    }

    public void setFloor_num(String floor_num) {
        this.floor_num = floor_num;
    }

    public String getLaud_count() {
        return laud_count;
    }

    public void setLaud_count(String laud_count) {
        this.laud_count = laud_count;
    }

    public String getLaud_status() {
        return laud_status;
    }

    public void setLaud_status(String laud_status) {
        this.laud_status = laud_status;
    }

    public List<String> getImgarr() {
        return imgarr;
    }

    public void setImgarr(List<String> imgarr) {
        this.imgarr = imgarr;
    }

    @Override
    public String toString() {
        return "PostCommoBean{" +
                "level=" + level +
                ", uid='" + uid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", head_image='" + head_image + '\'' +
                ", id='" + id + '\'' +
                ", tid='" + tid + '\'' +
                ", replies_content='" + replies_content + '\'' +
                ", dateline='" + dateline + '\'' +
                ", floor_num='" + floor_num + '\'' +
                ", laud_count='" + laud_count + '\'' +
                ", laud_status='" + laud_status + '\'' +
                ", imgarr=" + imgarr +
                '}';
    }
}
