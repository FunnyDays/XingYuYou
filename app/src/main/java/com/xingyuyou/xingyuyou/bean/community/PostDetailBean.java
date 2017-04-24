package com.xingyuyou.xingyuyou.bean.community;

/**
 * Created by Administrator on 2017/4/24.
 */

public class PostDetailBean {

    /**
     * id : 1
     * subject : 111111111
     * message : sadasdasdas
     * posts_image : http://xingyuyou.com/Uploads/Picture/2017-03-15/
     * dateline : 1492655334
     * nickname : 13240412886
     * head_image : http://xingyuyou.com/Uploads/Picture/2017-03-15/
     */

    private String id;
    private String subject;
    private String message;
    private String posts_image;
    private String dateline;
    private String nickname;
    private String head_image;

    public PostDetailBean() {
    }

    public PostDetailBean(String id, String subject, String message, String posts_image, String dateline, String nickname, String head_image) {
        this.id = id;
        this.subject = subject;
        this.message = message;
        this.posts_image = posts_image;
        this.dateline = dateline;
        this.nickname = nickname;
        this.head_image = head_image;
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

    public String getPosts_image() {
        return posts_image;
    }

    public void setPosts_image(String posts_image) {
        this.posts_image = posts_image;
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

    @Override
    public String toString() {
        return "PostDetailBean{" +
                "id='" + id + '\'' +
                ", subject='" + subject + '\'' +
                ", message='" + message + '\'' +
                ", posts_image='" + posts_image + '\'' +
                ", dateline='" + dateline + '\'' +
                ", nickname='" + nickname + '\'' +
                ", head_image='" + head_image + '\'' +
                '}';
    }
}
