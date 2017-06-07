package com.xingyuyou.xingyuyou.Utils;

import android.os.Looper;

/**
 * Created by 24002 on 2017/6/7.
 */

public class UIThreadUtils {
    public static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }
}
