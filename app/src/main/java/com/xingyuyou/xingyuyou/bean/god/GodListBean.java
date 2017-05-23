package com.xingyuyou.xingyuyou.bean.god;

/**
 * Created by 24002 on 2017/5/23.
 */

public class GodListBean {

    /**
     * id : 170
     * uid : 206
     * subject : 222
     * message : 哈哈哈哈啦啦啦啦算111
     * dateline : 1495004942
     */

    private String id;
    private String uid;
    private String subject;
    private String message;
    private String dateline;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    @Override
    public String toString() {
        return "GodListBean{" +
                "id='" + id + '\'' +
                ", uid='" + uid + '\'' +
                ", subject='" + subject + '\'' +
                ", message='" + message + '\'' +
                ", dateline='" + dateline + '\'' +
                '}';
    }
}
