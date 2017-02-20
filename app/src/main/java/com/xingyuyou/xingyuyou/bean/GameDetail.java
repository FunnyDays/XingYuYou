package com.xingyuyou.xingyuyou.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/22.
 */
public class GameDetail {
    String gameIcon;
    String gameName;
    String gameEnName;
    String gameDetailIntro;

    public ArrayList<String> getGamerecommendName() {
        return gamerecommendName;
    }

    public void setGamerecommendName(ArrayList<String> gamerecommendName) {
        this.gamerecommendName = gamerecommendName;
    }

    public ArrayList<String> getGameRecommendImage() {
        return gameRecommendImage;
    }

    public void setGameRecommendImage(ArrayList<String> gameRecommendImage) {
        this.gameRecommendImage = gameRecommendImage;
    }

    ArrayList<String> gameRecommendImage;
    ArrayList<String> gamerecommendName;
    ArrayList<String> gamePic;
    ArrayList<String> gameAllIntro;

    public String getGameDetailIntro() {
        return gameDetailIntro;
    }
    public ArrayList<String> getGameAllIntro() {
        return gameAllIntro;
    }

    public void setGameDetailIntro(String gameDetailIntro) {
        this.gameDetailIntro = gameDetailIntro;
    }

    public void setGameAllIntro(ArrayList<String> gameAllIntro) {
        this.gameAllIntro = gameAllIntro;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameEnName() {
        return gameEnName;
    }

    public void setGameEnName(String gameEnName) {
        this.gameEnName = gameEnName;
    }

    public String getGameIcon() {
        return gameIcon;
    }

    public void setGameIcon(String gameIcon) {
        this.gameIcon = gameIcon;
    }

    public ArrayList<String> getGamePic() {
        return gamePic;
    }

    public void setGamePic(ArrayList<String> gamePic) {
        this.gamePic = gamePic;
    }



}
