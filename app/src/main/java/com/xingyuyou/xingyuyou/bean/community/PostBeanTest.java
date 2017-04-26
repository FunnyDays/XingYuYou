package com.xingyuyou.xingyuyou.bean.community;

import java.util.List;

/**
 * Created by Administrator on 2017/4/24.
 */

public class PostBeanTest {

    /**
     * id : 64
     * subject : 1111111111
     * message : 阿萨德黄金卡时间看到驱蚊器无阿达
     * posts_image : ["http://xingyuyou.com/Public/app/posts_image/1231231239b7a.jpg"]
     * dateline : 1493170261
     * posts_laud : 0
     * posts_forums : 0
     * posts_collect : 0
     * nickname : 13240412886
     * head_image : http://xingyuyou.com/Uploads/Picture/2017-03-15/58d1d25c1bdf2.jpg
     * posts_class : [{"label_name":"li啊啊"},{"label_name":"lisi"},{"label_name":"li啊啊"}]
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
    private List<String> posts_image;
    /**
     * label_name : li啊啊
     */

    private List<PostsClassBean> posts_class;

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

    public List<String> getPosts_image() {
        return posts_image;
    }

    public void setPosts_image(List<String> posts_image) {
        this.posts_image = posts_image;
    }

    public List<PostsClassBean> getPosts_class() {
        return posts_class;
    }

    public void setPosts_class(List<PostsClassBean> posts_class) {
        this.posts_class = posts_class;
    }

    public static class PostsClassBean {
        private String label_name;

        public String getLabel_name() {
            return label_name;
        }

        public void setLabel_name(String label_name) {
            this.label_name = label_name;
        }
    }
}
