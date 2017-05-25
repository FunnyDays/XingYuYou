package com.xingyuyou.xingyuyou.bean.user;

/**
 * Created by 24002 on 2017/5/24.
 */

public class PrivateLetterBean {

    /**
     * id : 4
     * pid : 0
     * uid : 105
     * content : 啦啦啦啦
     * dateline : 1494574568
     * select_status : 1
     * nickname : 噢耶了了了
     * head_image : http://xingyuyou.com/Public/app/user_image/591ba8d60ab8a.png
     */

    private String id;
    private String pid;
    private String uid;
    private String content;
    private String dateline;
    private String select_status;
    private String nickname;
    private String head_image;

    public PrivateLetterBean(String content, String head_image,String pid) {
        this.content = content;
        this.head_image = head_image;
        this.pid=pid;
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    public String getSelect_status() {
        return select_status;
    }

    public void setSelect_status(String select_status) {
        this.select_status = select_status;
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

    @Override
    public String toString() {
        return "PrivateLetterBean{" +
                "id='" + id + '\'' +
                ", pid='" + pid + '\'' +
                ", uid='" + uid + '\'' +
                ", content='" + content + '\'' +
                ", dateline='" + dateline + '\'' +
                ", select_status='" + select_status + '\'' +
                ", nickname='" + nickname + '\'' +
                ", head_image='" + head_image + '\'' +
                '}';
    }
}
