package com.xingyuyou.xingyuyou.bean.community;

import java.util.List;

/**
 * Created by Administrator on 2017/4/24.
 */

public class PostDetailBean {


    /**
     * id : 83
     * subject : 49
     * message : 来咯弄
     * posts_image : ["http://xingyuyou.com/Public/app/posts_image/59082c4b9ec96.jpg","http://xingyuyou.com/Public/app/posts_image/59082c4b9ee34.jpg"]
     * dateline : 1493707880
     * nickname : 99
     * head_image : null
     */

    private String id;
    private String subject;
    private String message;
    private String dateline;
    private String nickname;
    private Object head_image;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Object getHead_image() {
        return head_image;
    }

    public void setHead_image(Object head_image) {
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
        return "PostDetailBean{" +
                "id='" + id + '\'' +
                ", subject='" + subject + '\'' +
                ", message='" + message + '\'' +
                ", dateline='" + dateline + '\'' +
                ", nickname='" + nickname + '\'' +
                ", head_image=" + head_image +
                ", posts_image=" + posts_image +
                '}';
    }
}
