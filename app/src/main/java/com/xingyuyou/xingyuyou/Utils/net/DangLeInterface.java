package com.xingyuyou.xingyuyou.Utils.net;


import com.xingyuyou.xingyuyou.Utils.LG;

/**
 * Created by Administrator on 2016/11/11.
 */

public class DangLeInterface {
    public static final int STATUS_SUCCESS = 0;
    public static final int STATUS_FAILED = 1;
    /**
     * dangle网络连接地址
     */
    public static final String START = "http://android.d.cn/game/list_5_1_0_0_0_0_0_0_0_0_0_";
    public static final String EDN = "_0.html";

    /**
     * 全部游戏列表
     */
    public static final String ALLGAMELIST = START + "1";

    /**
     * 获取游戏连接
     * @param i 游戏页数 开始页数为1
     * @return  返回带页数的link
     */
    public static String getGameInterface(int i){
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append(START).append(String.valueOf(i)).append(EDN);
        LG.e(stringBuilder.toString());
        return stringBuilder.toString();
    }
}
