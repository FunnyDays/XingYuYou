package com.xingyuyou.xingyuyou.bean.community;

import java.util.List;

/**
 * Created by Administrator on 2017/4/24.
 */

public class PostListBean {
    /**
     * id : 114
     * subject : 来啊的呵呵呵二儿女
     * message : ing滴滴滴额度逆袭耳钉
     * posts_image : ["http://xingyuyou.com/Public/app/posts_image/590c41f892101.jpg","http://xingyuyou.com/Public/app/posts_image/590c41f89247b.jpg","http://xingyuyou.com/Public/app/posts_image/590c41f892967.jpg","http://xingyuyou.com/Public/app/posts_image/590c41f892e3b.jpg","http://xingyuyou.com/Public/app/posts_image/590c41f89325e.jpg"]
     * dateline : 1493975544
     * posts_laud : 0
     * posts_forums : 0
     * posts_collect : 0
     * is_top : 0
     * is_well : 0
     * uid : 108
     * nickname : 99
     * head_image : http://xingyuyou.com/Uploads/Picture/2017-03-15/
     * laud_status : 0
     * collect_status : 0
     * posts_class : [{"label_name":"复古风格"},{"label_name":"方法"},{"label_name":"呵呵"}]
     */

    private String id;
    private String subject;
    private String message;
    private String dateline;
    private String posts_laud;
    private String posts_forums;
    private String posts_collect;
    private String is_top;
    private String is_well;
    private String uid;
    private String nickname;
    private String head_image;
    private String class_name;
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
    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }
    public String getPosts_collect() {
        return posts_collect;
    }

    public void setPosts_collect(String posts_collect) {
        this.posts_collect = posts_collect;
    }

    public String getIs_top() {
        return is_top;
    }

    public void setIs_top(String is_top) {
        this.is_top = is_top;
    }

    public String getIs_well() {
        return is_well;
    }

    public void setIs_well(String is_well) {
        this.is_well = is_well;
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
         * label_name : 复古风格
         */

        private String label_name;

        public String getLabel_name() {
            return label_name;
        }

        public void setLabel_name(String label_name) {
            this.label_name = label_name;
        }
    }

    @Override
    public String toString() {
        return "SortPostListBean{" +
                "id='" + id + '\'' +
                ", subject='" + subject + '\'' +
                ", message='" + message + '\'' +
                ", dateline='" + dateline + '\'' +
                ", posts_laud='" + posts_laud + '\'' +
                ", posts_forums='" + posts_forums + '\'' +
                ", posts_collect='" + posts_collect + '\'' +
                ", is_top='" + is_top + '\'' +
                ", is_well='" + is_well + '\'' +
                ", uid='" + uid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", head_image='" + head_image + '\'' +
                ", class_name='" + class_name + '\'' +
                ", laud_status=" + laud_status +
                ", collect_status=" + collect_status +
                ", posts_image=" + posts_image +
                ", posts_class=" + posts_class +
                '}';
    }
}
