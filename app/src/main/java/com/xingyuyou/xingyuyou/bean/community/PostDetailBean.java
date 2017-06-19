package com.xingyuyou.xingyuyou.bean.community;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/24.
 */

public class PostDetailBean implements Serializable {

    /**
     * id : 885
     * posts_image : [{"posts_image":"2017-06-19/5947869079876.jpg","posts_image_width":1000,"posts_image_height":992,"posts_image_size":243085},{"posts_image":"2017-06-19/5947869084b97.jpg","posts_image_width":1250,"posts_image_height":8725,"posts_image_size":7009481}]
     * subject : aaaaaaaaa
     * thumbnail_image : [{"thumbnail_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/2017-06-19/8175947869079876.jpg","thumbnail_width":960,"thumbnail_height":952},{"thumbnail_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/2017-06-19/9765947869084b97.jpg","thumbnail_width":960,"thumbnail_height":6700}]
     * message : www
     * dateline : 1497859739
     * posts_laud : 0
     * posts_forums : 0
     * posts_collect : 0
     * nickname : 噢耶了了了
     * head_image : http://xingyuyou.com/Public/app/user_image/5937590b83b43.jpg
     * uid : 105
     * posts_reset_image : [{"posts_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/2017-06-19/5947869079876.jpg","posts_image_width":1000,"posts_image_height":992,"posts_image_size":243085},{"posts_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/2017-06-19/5947869084b97.jpg","posts_image_width":1250,"posts_image_height":8725,"posts_image_size":7009481}]
     * laud_status : 0
     * collect_status : 0
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
    private String laud_status;
    private String collect_status;
    private List<PostsImageBean> posts_image;
    private ArrayList<ThumbnailImageBean> thumbnail_image;
    private ArrayList<PostsResetImageBean> posts_reset_image;

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

    public List<PostsImageBean> getPosts_image() {
        return posts_image;
    }

    public void setPosts_image(List<PostsImageBean> posts_image) {
        this.posts_image = posts_image;
    }

    public ArrayList<ThumbnailImageBean> getThumbnail_image() {
        return thumbnail_image;
    }

    public void setThumbnail_image(ArrayList<ThumbnailImageBean> thumbnail_image) {
        this.thumbnail_image = thumbnail_image;
    }

    public ArrayList<PostsResetImageBean> getPosts_reset_image() {
        return posts_reset_image;
    }

    public void setPosts_reset_image(ArrayList<PostsResetImageBean> posts_reset_image) {
        this.posts_reset_image = posts_reset_image;
    }

    public static class PostsImageBean {
        /**
         * posts_image : 2017-06-19/5947869079876.jpg
         * posts_image_width : 1000
         * posts_image_height : 992
         * posts_image_size : 243085
         */

        private String posts_image;
        private int posts_image_width;
        private int posts_image_height;
        private int posts_image_size;

        public String getPosts_image() {
            return posts_image;
        }

        public void setPosts_image(String posts_image) {
            this.posts_image = posts_image;
        }

        public int getPosts_image_width() {
            return posts_image_width;
        }

        public void setPosts_image_width(int posts_image_width) {
            this.posts_image_width = posts_image_width;
        }

        public int getPosts_image_height() {
            return posts_image_height;
        }

        public void setPosts_image_height(int posts_image_height) {
            this.posts_image_height = posts_image_height;
        }

        public int getPosts_image_size() {
            return posts_image_size;
        }

        public void setPosts_image_size(int posts_image_size) {
            this.posts_image_size = posts_image_size;
        }
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
                ", laud_status='" + laud_status + '\'' +
                ", collect_status='" + collect_status + '\'' +
                ", posts_image=" + posts_image +
                ", thumbnail_image=" + thumbnail_image +
                ", posts_reset_image=" + posts_reset_image +
                '}';
    }
}
