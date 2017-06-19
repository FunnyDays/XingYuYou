package com.xingyuyou.xingyuyou.bean.community;

import java.util.List;

/**
 * Created by Administrator on 2017/4/24.
 */

public class PostListBeanTest {

    /**
     * id : 876
     * subject : 测试
     * dateline : 1497522623
     * thumbnail_image : [{"thumbnail_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/2017-06-15/682594261bed3ae2.jpg","thumbnail_width":200,"thumbnail_height":200},{"thumbnail_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/2017-06-15/458594261bed427e.jpg","thumbnail_width":960,"thumbnail_height":952}]
     * fid : 8
     * message : www
     * posts_image : [{"posts_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/2017-06-15/594261bed3ae2.jpg","posts_image_width":200,"posts_image_height":200,"posts_image_size":21036},{"posts_image":"http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/2017-06-15/594261bed427e.jpg","posts_image_width":1000,"posts_image_height":992,"posts_image_size":243085}]
     * posts_laud : 0
     * posts_forums : 0
     * posts_collect : 0
     * uid : 105
     * nickname : 噢耶了了了
     * head_image : http://xingyuyou.com/Public/app/user_image/5937590b83b43.jpg
     * class_name : Cosplay
     * laud_status : 0
     * collect_status : 0
     * posts_class : [{"label_name":"小说"},{"label_name":"通告"},{"label_name":"番剧"}]
     */

    private String id;
    private String subject;
    private String dateline;
    private String fid;
    private String message;
    private String posts_laud;
    private String posts_forums;
    private String posts_collect;
    private String uid;
    private String nickname;
    private String head_image;
    private String class_name;
    private int laud_status;
    private int collect_status;
    private List<ThumbnailImageBean> thumbnail_image;
    private List<String> posts_image;



    private List<PostsClassBean> label_name;

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

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
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

    public List<ThumbnailImageBean> getThumbnail_image() {
        return thumbnail_image;
    }

    public void setThumbnail_image(List<ThumbnailImageBean> thumbnail_image) {
        this.thumbnail_image = thumbnail_image;
    }
    public List<PostsClassBean> getLabel_name() {
        return label_name;
    }

    public void setLabel_name(List<PostsClassBean> label_name) {
        this.label_name = label_name;
    }
    public List<String> getPosts_image() {
        return posts_image;
    }

    public void setPosts_image(List<String> posts_image) {
        this.posts_image = posts_image;
    }


    public static class ThumbnailImageBean {
        /**
         * thumbnail_image : http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/2017-06-15/682594261bed3ae2.jpg
         * thumbnail_width : 200
         * thumbnail_height : 200
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

    public static class PostsImageBean {
        /**
         * posts_image : http://xingyuhuyu1916.oss-cn-beijing.aliyuncs.com/image/2017-06-15/594261bed3ae2.jpg
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

        @Override
        public String toString() {
            return "PostsImageBean{" +
                    "posts_image='" + posts_image + '\'' +
                    ", posts_image_width=" + posts_image_width +
                    ", posts_image_height=" + posts_image_height +
                    ", posts_image_size=" + posts_image_size +
                    '}';
        }
    }

    public static class PostsClassBean {
        /**
         * label_name : 小说
         */

        private String label_name;

        public String getLabel_name() {
            return label_name;
        }

        public void setLabel_name(String label_name) {
            this.label_name = label_name;
        }

        @Override
        public String toString() {
            return "PostsClassBean{" +
                    "label_name='" + label_name + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "PostListBeanTest{" +
                "id='" + id + '\'' +
                ", subject='" + subject + '\'' +
                ", dateline='" + dateline + '\'' +
                ", fid='" + fid + '\'' +
                ", message='" + message + '\'' +
                ", posts_laud='" + posts_laud + '\'' +
                ", posts_forums='" + posts_forums + '\'' +
                ", posts_collect='" + posts_collect + '\'' +
                ", uid='" + uid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", head_image='" + head_image + '\'' +
                ", class_name='" + class_name + '\'' +
                ", laud_status=" + laud_status +
                ", collect_status=" + collect_status +
                ", thumbnail_image=" + thumbnail_image +
                ", posts_image=" + posts_image +
                ", posts_class=" + label_name +
                '}';
    }
}
