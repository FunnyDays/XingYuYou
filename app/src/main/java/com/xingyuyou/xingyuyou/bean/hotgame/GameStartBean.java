package com.xingyuyou.xingyuyou.bean.hotgame;

/**
 * Created by 24002 on 2017/5/19.
 */

public class GameStartBean {

    /**
     * sum_star : 30
     * one_star : 0
     * two_star : 0
     * three_star : 0
     * four_star : 5
     * five_star : 2
     */

    private String sum_star;
    private String one_star;
    private String two_star;
    private String three_star;
    private String four_star;
    private String five_star;

    public String getSum_star() {
        return sum_star;
    }

    public void setSum_star(String sum_star) {
        this.sum_star = sum_star;
    }

    public String getOne_star() {
        return one_star;
    }

    public void setOne_star(String one_star) {
        this.one_star = one_star;
    }

    public String getTwo_star() {
        return two_star;
    }

    public void setTwo_star(String two_star) {
        this.two_star = two_star;
    }

    public String getThree_star() {
        return three_star;
    }

    public void setThree_star(String three_star) {
        this.three_star = three_star;
    }

    public String getFour_star() {
        return four_star;
    }

    public void setFour_star(String four_star) {
        this.four_star = four_star;
    }

    public String getFive_star() {
        return five_star;
    }

    public void setFive_star(String five_star) {
        this.five_star = five_star;
    }

    @Override
    public String toString() {
        return "GameStartBean{" +
                "sum_star='" + sum_star + '\'' +
                ", one_star='" + one_star + '\'' +
                ", two_star='" + two_star + '\'' +
                ", three_star='" + three_star + '\'' +
                ", four_star='" + four_star + '\'' +
                ", five_star='" + five_star + '\'' +
                '}';
    }
}
