package com.xingyuyou.xingyuyou.bean.community;

import java.util.List;

/**
 * Created by 24002 on 2017/5/8.
 */

public class PostCommoBean {

    /**
     * id : 2774
     * pid : 0
     * uid : 105
     * imgarr : [{"posts_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/2017-06-19/59479c21283c4.jpg","posts_image_width":200,"posts_image_height":200,"posts_image_size":21036},{"posts_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/2017-06-19/59479c2128b3e.jpg","posts_image_width":1000,"posts_image_height":992,"posts_image_size":243085}]
     * replies_content : 1111111
     * dateline : 1497865249
     * tid : 887
     * floor_num : 2
     * forum_laud : 0
     * thumbnail_image : [{"thumbnail_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/2017-06-19/18459479c2128b3e.jpg","thumbnail_width":960,"thumbnail_height":952},{"thumbnail_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/2017-06-19/18459479c2128b3e.jpg","thumbnail_width":960,"thumbnail_height":952}]
     * nickname : 噢耶了了了
     * head_image : http://xingyuyou.com/Public/app/user_image/5937590b83b43.jpg
     * laud_count : 0
     * laud_status : 0
     */

    private String id;
    private String pid;
    private String uid;
    private String replies_content;
    private String dateline;
    private String tid;
    private String floor_num;
    private String forum_laud;
    private String nickname;
    private String head_image;
    private String laud_count;
    private int laud_status;
    private List<ImgarrBean> imgarr;
    private List<ThumbnailImageBean> thumbnail_image;

    public List<ChildBean> getChild() {
        return child;
    }

    public void setChild(List<ChildBean> child) {
        this.child = child;
    }

    private List<ChildBean> child;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getReplies_content() {
        return replies_content;
    }

    public void setReplies_content(String replies_content) {
        this.replies_content = replies_content;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getFloor_num() {
        return floor_num;
    }

    public void setFloor_num(String floor_num) {
        this.floor_num = floor_num;
    }

    public String getForum_laud() {
        return forum_laud;
    }

    public void setForum_laud(String forum_laud) {
        this.forum_laud = forum_laud;
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

    public String getLaud_count() {
        return laud_count;
    }

    public void setLaud_count(String laud_count) {
        this.laud_count = laud_count;
    }

    public int getLaud_status() {
        return laud_status;
    }

    public void setLaud_status(int laud_status) {
        this.laud_status = laud_status;
    }

    public List<ImgarrBean> getImgarr() {
        return imgarr;
    }

    public void setImgarr(List<ImgarrBean> imgarr) {
        this.imgarr = imgarr;
    }

    public List<ThumbnailImageBean> getThumbnail_image() {
        return thumbnail_image;
    }

    public void setThumbnail_image(List<ThumbnailImageBean> thumbnail_image) {
        this.thumbnail_image = thumbnail_image;
    }

    public static class ImgarrBean {
        /**
         * posts_image : http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/2017-06-19/59479c21283c4.jpg
         * posts_image_width : 200
         * posts_image_height : 200
         * posts_image_size : 21036
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

    public static class ThumbnailImageBean {
        /**
         * thumbnail_image : http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/2017-06-19/18459479c2128b3e.jpg
         * thumbnail_width : 960
         * thumbnail_height : 952
         */

        private String thumbnail_image;
        private int thumbnail_width;
        private int thumbnail_height;

        public String getThumbnail_image() {
            return thumbnail_image;
        }

        public void setThumbnail_image(String thumbnail_image) {
            this.thumbnail_image = thumbnail_image;
        }

        public int getThumbnail_width() {
            return thumbnail_width;
        }

        public void setThumbnail_width(int thumbnail_width) {
            this.thumbnail_width = thumbnail_width;
        }

        public int getThumbnail_height() {
            return thumbnail_height;
        }

        public void setThumbnail_height(int thumbnail_height) {
            this.thumbnail_height = thumbnail_height;
        }
    }
    public static class ChildBean {
        /**
         * id : 39
         * pid : 38
         * uid : 206
         * imgarr :
         * replies_content : 第1条评论回复
         * dateline : 1494058219
         * tid : 103
         * floor_num : 0
         * nickname : 刘若男
         */

        private String id;
        private String pid;
        private String uid;
        private String imgarr;
        private String replies_content;
        private String dateline;
        private String tid;
        private String floor_num;
        private String nickname;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getImgarr() {
            return imgarr;
        }

        public void setImgarr(String imgarr) {
            this.imgarr = imgarr;
        }

        public String getReplies_content() {
            return replies_content;
        }

        public void setReplies_content(String replies_content) {
            this.replies_content = replies_content;
        }

        public String getDateline() {
            return dateline;
        }

        public void setDateline(String dateline) {
            this.dateline = dateline;
        }

        public String getTid() {
            return tid;
        }

        public void setTid(String tid) {
            this.tid = tid;
        }

        public String getFloor_num() {
            return floor_num;
        }

        public void setFloor_num(String floor_num) {
            this.floor_num = floor_num;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }
}
