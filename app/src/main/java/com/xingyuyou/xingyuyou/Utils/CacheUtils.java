package com.xingyuyou.xingyuyou.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/8/8.
 */
public class CacheUtils {
    private static SharedPreferences sSharedPreferences;
    public static void putBoolean(Context context, String key, Boolean value){
        if(sSharedPreferences==null){
            sSharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sSharedPreferences.edit().putBoolean(key,value).commit();
    }
    public static Boolean getBoolean(Context context, String key, Boolean defValue){
        if(sSharedPreferences==null){
            sSharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sSharedPreferences.getBoolean(key,defValue);
    }
    public static void putString(Context context, String key, String value){
        if(sSharedPreferences==null){
            sSharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sSharedPreferences.edit().putString(key,value).commit();
    }
    public static String getString(Context context, String key, String defValue){
        if(sSharedPreferences==null){
            sSharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sSharedPreferences.getString(key,defValue);
    }
}
