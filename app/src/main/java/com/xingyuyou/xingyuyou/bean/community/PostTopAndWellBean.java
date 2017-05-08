package com.xingyuyou.xingyuyou.bean.community;

/**
 * Created by 24002 on 2017/5/6.
 */

public class PostTopAndWellBean {

    /**
     * id : 105
     * subject : 不可抗力
     * is_top : 0
     * is_well : 1
     */

    private String id;
    private String subject;
    private String is_top;
    private String is_well;

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

    @Override
    public String toString() {
        return "PostTopAndWellBean{" +
                "id='" + id + '\'' +
                ", subject='" + subject + '\'' +
                ", is_top='" + is_top + '\'' +
                ", is_well='" + is_well + '\'' +
                '}';
    }
}
