package com.xingyuyou.xingyuyou.bean.community;

import java.util.List;

/**
 * Created by Administrator on 2017/4/24.
 */

public class PostBean {

    /**
     * id : 102
     * subject : 啊饿死了
     * message : 就几级了
     * posts_image : ["http://xingyuyou.com/Public/app/posts_image/5909719993870.jpg"]
     * dateline : 1493791129
     * nickname : 1111111
     * head_image : http://xingyuyou.com/Uploads/Picture/2017-03-15/
     */

    private String id;
    private String subject;
    private String message;
    private String dateline;
    private String nickname;
    private String head_image;
    private List<String> posts_image;

    public PostBean(String id, String subject, String message, String dateline, String nickname, String head_image, List<String> posts_image) {
        this.id = id;
        this.subject = subject;
        this.message = message;
        this.dateline = dateline;
        this.nickname = nickname;
        this.head_image = head_image;
        this.posts_image = posts_image;
    }

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

    public List<String> getPosts_image() {
        return posts_image;
    }

    public void setPosts_image(List<String> posts_image) {
        this.posts_image = posts_image;
    }

    @Override
    public String toString() {
        return "PostBean{" +
                "id='" + id + '\'' +
                ", subject='" + subject + '\'' +
                ", message='" + message + '\'' +
                ", dateline='" + dateline + '\'' +
                ", nickname='" + nickname + '\'' +
                ", head_image='" + head_image + '\'' +
                ", posts_image=" + posts_image +
                '}';
    }
}
