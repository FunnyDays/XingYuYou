package com.xingyuyou.xingyuyou.Utils;

import android.util.Log;


/**
 * Created by b508a on 2015/12/28.
 */
public class LG {

    /**
     * 
     */
   public static boolean isDebug=true;
    
    public static void e(String TAG, String msg){
        if(isDebug){
            Log.e(TAG, msg+"");
        }
    }

    public static void e( String msg){
        if(isDebug){
            Log.e("weiwei", "/*\n" +
                    "                   _ooOoo_\n" +
                    "                  o8888888o\n" +
                    "                  88\" . \"88\n" +
                    "                  (| -_- |)\n" +
                    "                  O\\  =  /O\n" +
                    "               ____/`---'\\____\n" +
                    "             .'  \\\\|     |//  `.\n" +
                    "            /  \\\\|||  :  |||//  \\\n" +
                    "           /  _||||| -:- |||||-  \\\n" +
                    "           |   | \\\\\\  -  /// |   |\n" +
                    "           | \\_|  ''\\---/''  |   |\n" +
                    "           \\  .-\\__  `-`  ___/-. /\n" +
                    "         ___`. .'  /--.--\\  `. . __\n" +
                    "      .\"\" '<  `.___\\_<|>_/___.'  >'\"\".\n" +
                    "     | | :  `- \\`.;`\\ _ /`;.`/ - ` : | |\n" +
                    "     \\  \\ `-.   \\_ __\\ /__ _/   .-` /  /\n" +
                    "======`-.____`-.___\\_____/___.-`____.-'======\n" +
                    "                   `=---='\n" +
                    "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^\n" +
                    "         佛祖保佑       永无BUG\n" +
                    "*/");
            Log.e("weiwei", msg+"");
            Log.e("weiwei", "###################################");
        }
    }

    public static void d(String TAG, String msg){
        if(isDebug){
            Log.d(TAG, msg+"");
        }
    }


    /**
     * 
     *
     * 2014-5-8
     * @param clazz
     * @param msg
     */
    public static void e(Class<?> clazz, String msg){
        if(isDebug){
            Log.e(clazz.getSimpleName(), msg+"");
        }
    }
    /**
     *
     *
     * 2014-5-8
     * @param clazz
     * @param msg
     */
    public static void i(Class<?> clazz, String msg){
        if(isDebug){
            Log.i(clazz.getSimpleName(), msg + "");
        }
    }
    /**
     * 
     *
     * 2014-5-8
     * @param clazz
     * @param msg
     */
    public static void w(Class<?> clazz, String msg){
        if(isDebug){
            Log.w(clazz.getSimpleName(), msg+"");
        }
    }
}


