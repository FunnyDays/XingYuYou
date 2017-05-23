package com.xingyuyou.xingyuyou.bean.god;

import com.xingyuyou.xingyuyou.bean.community.PostCommoBean;

import java.util.List;

/**
 * Created by 24002 on 2017/5/23.
 */

public class GodCommoBean {
    /**
     * id : 38
     * pid : 0
     * uid : 206
     * imgarr : ["http://xingyuyou.com/Public/app/replies_image/590d84ddd22bd.jpg"]
     * replies_content : 第1条评论
     * dateline : 1494058205
     * tid : 103
     * floor_num : 1forum_laud
     * nickname : 刘若男
     * head_image : http://xingyuyou.com/Uploads/Picture/2017-03-15/59118bd5e18c4.jpg
     * laud_count : 4
     * laud_status : 1
     * child : [{"id":"39","pid":"38","uid":"206","imgarr":"","replies_content":"第1条评论回复","dateline":"1494058219","tid":"103","floor_num":"0","nickname":"刘若男"},{"id":"43","pid":"38","uid":"206","imgarr":"","replies_content":"第4条评论回复","dateline":"1494210080","tid":"103","floor_num":"0","nickname":"刘若男"}]
     */

    private String id;
    private String pid;
    private String uid;
    private String replies_content;
    private String dateline;
    private String tid;
    private String floor_num;
    private String nickname;
    private String head_image;
    private String forum_laud;
    private int laud_status;
    private List<String> imgarr;
    private List<PostCommoBean.ChildBean> child;



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

    public String getForum_laud() {
        return forum_laud;
    }

    public void setForum_laud(String forum_laud) {
        this.forum_laud = forum_laud;
    }
    public int getLaud_status() {
        return laud_status;
    }

    public void setLaud_status(int laud_status) {
        this.laud_status = laud_status;
    }

    public List<String> getImgarr() {
        return imgarr;
    }

    public void setImgarr(List<String> imgarr) {
        this.imgarr = imgarr;
    }

    public List<PostCommoBean.ChildBean> getChild() {
        return child;
    }

    public void setChild(List<PostCommoBean.ChildBean> child) {
        this.child = child;
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

    @Override
    public String toString() {
        return "PostCommoBean{" +
                "id='" + id + '\'' +
                ", pid='" + pid + '\'' +
                ", uid='" + uid + '\'' +
                ", replies_content='" + replies_content + '\'' +
                ", dateline='" + dateline + '\'' +
                ", tid='" + tid + '\'' +
                ", floor_num='" + floor_num + '\'' +
                ", nickname='" + nickname + '\'' +
                ", head_image='" + head_image + '\'' +
                ", laud_count='" + forum_laud + '\'' +
                ", laud_status='" + laud_status + '\'' +
                ", imgarr=" + imgarr +
                ", child=" + child +
                '}';
    }

}
