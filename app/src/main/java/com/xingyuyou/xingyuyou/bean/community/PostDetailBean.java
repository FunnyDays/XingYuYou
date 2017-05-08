package com.xingyuyou.xingyuyou.bean.community;

import java.util.List;

/**
 * Created by Administrator on 2017/4/24.
 */

public class PostDetailBean {


    /**
     * id : 103
     * subject : 测量了了了
     * message : 好了了了了
     * posts_image : ["http://xingyuyou.com/Public/app/posts_image/590973907cdaa.png","http://xingyuyou.com/Public/app/posts_image/590973907d1c4.png"]
     * dateline : 1493791632
     * posts_laud : 0
     * posts_forums : 0
     * posts_collect : 0
     * nickname : 1111111
     * head_image : http://xingyuyou.com/Uploads/Picture/2017-03-15/
     * uid : 206
     */

    private String id;
    private String subject;
    private String message;
    private String dateline;
    private String posts_laud;
    private String posts_forums;
    private String posts_collect;
    private String nickname;
    private String head_image;
    private String uid;
    private List<String> posts_image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    public String getPosts_laud() {
        return posts_laud;
    }

    public void setPosts_laud(String posts_laud) {
        this.posts_laud = posts_laud;
    }

    public String getPosts_forums() {
        return posts_forums;
    }

    public void setPosts_forums(String posts_forums) {
        this.posts_forums = posts_forums;
    }

    public String getPosts_collect() {
        return posts_collect;
    }

    public void setPosts_collect(String posts_collect) {
        this.posts_collect = posts_collect;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<String> getPosts_image() {
        return posts_image;
    }

    public void setPosts_image(List<String> posts_image) {
        this.posts_image = posts_image;
    }

    @Override
    public String toString() {
        return "PostDetailBean{" +
                "id='" + id + '\'' +
                ", subject='" + subject + '\'' +
                ", message='" + message + '\'' +
                ", dateline='" + dateline + '\'' +
                ", posts_laud='" + posts_laud + '\'' +
                ", posts_forums='" + posts_forums + '\'' +
                ", posts_collect='" + posts_collect + '\'' +
                ", nickname='" + nickname + '\'' +
                ", head_image='" + head_image + '\'' +
                ", uid='" + uid + '\'' +
                ", posts_image=" + posts_image +
                '}';
    }
}
