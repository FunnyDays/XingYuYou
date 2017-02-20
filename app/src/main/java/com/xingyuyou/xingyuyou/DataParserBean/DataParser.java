package com.xingyuyou.xingyuyou.DataParserBean;


import android.util.Log;


import com.xingyuyou.xingyuyou.Utils.net.DangLeInterface;
import com.xingyuyou.xingyuyou.bean.Game;
import com.xingyuyou.xingyuyou.bean.GameDetail;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/11/11.
 */

public class DataParser {
    public static DataParser getInstance() {
        return new DataParser();
    }

    /**
     * 获取游戏所有详情（游戏名称、游戏介绍、游戏介绍图片）
     *
     * @param link 游戏详情地址
     * @return
     */
    public static GameDetail getGameDetail(String link) {
        GameDetail gameDetail = new GameDetail();
        ArrayList<String> picArrayList = new ArrayList<>();
        ArrayList<String> gameIntroArrayList = new ArrayList<>();
        ArrayList<String> gameRecomendImg = new ArrayList<>();
        ArrayList<String> gameRecomendText = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(link).get();
            //获取游戏icon
            String appIcon = doc.select("div.de-head-l").select("img").attr("src");
            gameDetail.setGameIcon(appIcon);
            //获取游戏名称（英文和中文）de-app-des
            String appName = doc.select("h1.notag").text();
            String appEnName = doc.select("h2.de-app-en.notag-h2").text();
            gameDetail.setGameName(appName);
            gameDetail.setGameEnName(appEnName);
            //获取游戏图片链接
            Elements element1 = doc.select("div.gameimg-screen").select("img");
            for (int i = 0; i < element1.size(); i++) {
                picArrayList.add(element1.get(i).attr("src"));
                Log.e("weiwei", "游戏名称：" + element1.size() + "图片的链接地址" + picArrayList.get(i));
            }
            gameDetail.setGamePic(picArrayList);
            //获取游戏详细（类型、）信息
            Elements li = doc.select("ul.de-game-info.clearfix").select("li");
            for (int i = 0; i < li.size(); i++) {
                gameIntroArrayList.add(li.get(i).text());
                Log.e("weiwei", "数量一共是" + li.size() + "----" + li.get(i).text());
            }
            gameDetail.setGameAllIntro(gameIntroArrayList);
            //获取游戏详细介绍
            Elements elements = doc.select("div.de-intro-inner");
            gameDetail.setGameDetailIntro(elements.text());
            //游戏推荐
            Elements select = doc.select("a.de-set-icon").select("img");
            for (int i = 0; i < 3; i++) {
                String src = select.get(i).attr("o-src");
                String alt = select.get(i).attr("alt");
                gameRecomendImg.add(src);
                gameRecomendText.add(alt);
            }
            gameDetail.setGameRecommendImage(gameRecomendImg);
            gameDetail.setGamerecommendName(gameRecomendText);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return gameDetail;
    }

    /**
     * 获取游戏下载链接
     */
    public static String getGameDownLoadLink(String link) {
        final String[] mPkgUrl = new String[1];
        final int[] lastIndexOf = {link.lastIndexOf("/")};
        String substring = link.substring(lastIndexOf[0]);
        int i1 = substring.indexOf(".");
        String s = substring.substring(0, i1);
        // LG.e(s + "haha");
        OkHttpUtils.get().url("http://android.d.cn/rm/red/1" + s).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }
            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray pkgs = jsonObject.getJSONArray("pkgs");
                    mPkgUrl[0] = pkgs.getJSONObject(0).getString("pkgUrl");
                    Log.e("xiazai",pkgs.toString()+ mPkgUrl[0] +"");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        return mPkgUrl[0];
    }

    /**
     * 获取30条游戏列表信息
     *
     * @param j
     * @return
     */
    public static ArrayList<Game> getAllGame(int j) {
        ArrayList<Game> gameArrayList = new ArrayList<>();
        Game game;
        try {
            Document doc = Jsoup.connect(DangLeInterface.getGameInterface(j)).get();
            Elements element = doc.select("div.list-in");

            for (int i = 0; i < element.size(); i++) {
                game = new Game();
                //获取游戏名称
                String name = element.get(i).select("div.list-left").select("a").attr("title");
                //获取游戏链接
                String link = element.get(i).select("div.list-left").select("a").attr("href");
                //获取游戏图标
                String pic = element.get(i).select("div.list-left").select("a").select("img").attr("o-src");
                //获取星星数量
                String starts = element.get(i).select("div.list-left").select(".stars.iconSprite").attr("class");
                //获取游戏版本
                String edtion = element.get(i).select("div.list-right").select("p.down-ac").text();
                //获取游戏介绍
                String intro = element.get(i).select("div.list-right").select("p.g-intro").text();
                //获取游戏大小
                String size = element.get(i).select("div.list-right").select("p.g-detail").text();
                game.setGameEdition(edtion);
                game.setGameSize(size);
                game.setGameIntro(intro);
                game.setGameStar(Float.valueOf(starts.substring(starts.length() - 1)));
                game.setGameName(name);
                game.setGamePic(pic);
                game.setGameDetailLink(link);
                gameArrayList.add(game);

                // Log.e("weiwei", "游戏名称：" + gameArrayList.get(i).getGameName() + "游戏的链接地址" + gameArrayList.get(i).getGameDetailLink());
            }


            // Log.e("weiwei", "游戏名称：" + element.size() + "图片的链接地址");


        } catch (IOException e) {
            e.printStackTrace();
        }
        return gameArrayList;
    }


}
