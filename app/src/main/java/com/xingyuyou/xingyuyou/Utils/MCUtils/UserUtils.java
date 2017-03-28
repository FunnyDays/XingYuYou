package com.xingyuyou.xingyuyou.Utils.MCUtils;

import com.xingyuyou.xingyuyou.Utils.SPUtils;
import com.xingyuyou.xingyuyou.Utils.StringUtils;

/**
 * Created by Administrator on 2017/3/28.
 */

public class UserUtils {
    private static SPUtils user_data;
    public static boolean logined() {
        user_data = new SPUtils("user_data");
        String id = user_data.getString("id");
        String account = user_data.getString("account");
        String nickname = user_data.getString("nickname");
        if (StringUtils.isEmpty(id)||StringUtils.isEmpty(account)){
            return false;
        }else {
            return true;
        }
    }
    public static void LoginOut() {
        user_data = new SPUtils("user_data");
        user_data.putString("id",null);
        user_data.putString("account",null);
        user_data.putString("nickname",null);
    }
    public static void Login(String id,String account,String nickname) {
        user_data = new SPUtils("user_data");
        user_data.putString("id",id);
        user_data.putString("account",account);
        user_data.putString("nickname",nickname);
    }
}
