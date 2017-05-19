package com.xingyuyou.xingyuyou.bean.community;

import java.util.List;

/**
 * Created by Administrator on 2017/4/24.
 */

public class PostCollectListBean {

    /**
     * id : 169
     * subject : 第九条数据
     * message : 测试
     * posts_image : ["http://xingyuyou.com/Public/app/posts_image/591c208ec33d1.jpg"]
     * dateline : 1495015566
     * posts_laud : 0
     * posts_forums : 30
     * posts_collect : 0
     * uid : 105
     * nickname : 噢耶了了了
     * head_image : http://xingyuyou.com/Public/app/user_image/591ba8d60ab8a.png
     * laud_status : 0
     * collect_status : 0
     * posts_class : [{"label_name":"好贵"},{"label_name":"复古风格"},{"label_name":"搜索"},{"label_name":"方法"}]
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
         * label_name : 好贵
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
