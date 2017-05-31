package com.xingyuyou.xingyuyou.bean.god;

/**
 * Created by 24002 on 2017/5/23.
 */

public class GodActivityDetailBean {

    /**
     * id : 170
     * subject : 222
     * posts_image : http://xingyuyou.com/Uploads/Picture/2017-05-18/591cfbe8aaeae.jpg
     * message : 哈哈哈哈啦啦啦啦算111一
     * dateline : 1495004942
     * posts_laud : 1
     * posts_forums : 83
     * posts_collect : 0
     * laud_status : 1
     * collect_status : 0
     */

    private String id;
    private String subject;
    private String posts_image;
    private String message;
    private String dateline;
    private String posts_laud;
    private String posts_forums;
    private String posts_collect;
    private String laud_status;
    private String collect_status;

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

    public String getPosts_image() {
        return posts_image;
    }

    public void setPosts_image(String posts_image) {
        this.posts_image = posts_image;
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

    public String getLaud_status() {
        return laud_status;
    }

    public void setLaud_status(String laud_status) {
        this.laud_status = laud_status;
    }

    public String getCollect_status() {
        return collect_status;
    }

    public void setCollect_status(String collect_status) {
        this.collect_status = collect_status;
    }

    @Override
    public String toString() {
        return "GodActivityDetailBean{" +
                "id='" + id + '\'' +
                ", subject='" + subject + '\'' +
                ", posts_image='" + posts_image + '\'' +
                ", message='" + message + '\'' +
                ", dateline='" + dateline + '\'' +
                ", posts_laud='" + posts_laud + '\'' +
                ", posts_forums='" + posts_forums + '\'' +
                ", posts_collect='" + posts_collect + '\'' +
                ", laud_status='" + laud_status + '\'' +
                ", collect_status='" + collect_status + '\'' +
                '}';
    }
}
