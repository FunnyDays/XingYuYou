package com.xingyuyou.xingyuyou.bean.user;

import java.util.List;

/**
 * Created by 24002 on 2017/5/9.
 */

public class MyReplyPostBean {

    /**
     * id : 138
     * subject : 好了了了了了
     * message : 啊啊啊啊
     * posts_image : ["http://xingyuyou.com/Public/app/posts_image/5911806c48ec7.jpg"]
     * dateline : 1494319212
     * posts_laud : 0
     * posts_forums : 0
     * posts_collect : 0
     * uid : 105
     * nickname : 呵呵
     * head_image : http://xingyuyou.com/Uploads/Picture/2017-03-15/59118beabd82e.jpg
     * laud_status : 0
     * collect_status : 0
     * posts_class : [{"label_name":"方法"},{"label_name":"是的"}]
     */

    private String id;
    private String subject;
    private String message;
    private String dateline;
    private String posts_laud;
    private String posts_forums;
    private String posts_collect;
    private String uid;
    private String nickname;
    private String head_image;
    private int laud_status;
    private int collect_status;
    private List<String> posts_image;
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

    public int getCollect_status() {
        return collect_status;
    }

    public void setCollect_status(int collect_status) {
        this.collect_status = collect_status;
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
        /**
         * label_name : 方法
         */

        private String label_name;

        public String getLabel_name() {
            return label_name;
        }

        public void setLabel_name(String label_name) {
            this.label_name = label_name;
        }
    }
}
