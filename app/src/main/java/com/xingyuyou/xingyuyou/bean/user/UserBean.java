package com.xingyuyou.xingyuyou.bean.user;

/**
 * Created by 24002 on 2017/5/9.
 */

public class UserBean {


    /**
     * id : 206
     * nickname : 1111111
     * sex : 0
     * user_age :
     * area :
     * explain :
     * hobby :
     * user_integral : 35
     * head_image : http://xingyuyou.com/Public/app/user_image/58d1d25c1bdf2.jpg
     * address_info :
     */

    private String id;
    private String nickname;
    private String sex;
    private String user_age;
    private String area;
    private String explain;
    private String hobby;
    private String user_integral;
    private String head_image;
    private String address_info;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUser_age() {
        return user_age;
    }

    public void setUser_age(String user_age) {
        this.user_age = user_age;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getUser_integral() {
        return user_integral;
    }

    public void setUser_integral(String user_integral) {
        this.user_integral = user_integral;
    }

    public String getHead_image() {
        return head_image;
    }

    public void setHead_image(String head_image) {
        this.head_image = head_image;
    }

    public String getAddress_info() {
        return address_info;
    }

    public void setAddress_info(String address_info) {
        this.address_info = address_info;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "id='" + id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sex='" + sex + '\'' +
                ", user_age='" + user_age + '\'' +
                ", area='" + area + '\'' +
                ", explain='" + explain + '\'' +
                ", hobby='" + hobby + '\'' +
                ", user_integral='" + user_integral + '\'' +
                ", head_image='" + head_image + '\'' +
                ", address_info='" + address_info + '\'' +
                '}';
    }
}
