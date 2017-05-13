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
        if (StringUtils.isEmpty(id) || StringUtils.isEmpty(account)) {
            return false;
        } else {
            return true;
        }
    }

    public static void LoginOut() {
        user_data = new SPUtils("user_data");
        user_data.putString("id", null);
        user_data.putString("account", null);
        user_data.putString("nickname", null);
    }

    public static void Login(String id, String account, String nickname) {
        user_data = new SPUtils("user_data");
        user_data.putString("id", id);
        user_data.putString("account", account);
        user_data.putString("nickname", nickname);
    }

    public static String getUserId() {
        if (logined()) {
            user_data = new SPUtils("user_data");
            String id = user_data.getString("id");
            return id;
        }
        return "未登录";
    }

    public static String getUserAccount() {
        if (logined()) {
            user_data = new SPUtils("user_data");
            String account = user_data.getString("account");
            return account;
        }
        return "未登录";
    }

    public static void setNickName(String nickName) {
        if (logined()) {
            user_data = new SPUtils("user_data");
            user_data.putString("nickname", nickName);
        }
    }
    public static String getNickName() {
        if (logined()) {
            user_data = new SPUtils("user_data");
            String nickname = user_data.getString("nickname");
            return nickname;
        }
        return "请在个人信息中设置";
    }

    public static void setUserPhoto(String userPhoto) {
        if (logined()) {
            user_data = new SPUtils("user_data");
            user_data.putString("userPhoto", userPhoto);
        }
    }

    public static String getUserPhoto() {
        if (logined()) {
            user_data = new SPUtils("user_data");
            String userPhoto = user_data.getString("userPhoto");
            return userPhoto;
        }
        return "未登录";
    }
}
