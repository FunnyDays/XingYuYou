package com.xingyuyou.xingyuyou.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by qibin on 2015/9/22.
 */
public class FontHelper {
    public static final String FONTS_DIR = "fonts/";
    public static final String DEF_FONT = FONTS_DIR + "";

    public static final void injectFont(View rootView, Context mActivity) {
        injectFont(rootView, Typeface.createFromAsset(mActivity
                .getAssets(), DEF_FONT));
    }

    public static final void injectFont(View rootView, Typeface tf) {
        if(rootView instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) rootView;
            int count = group.getChildCount();
            for(int i=0;i<count;i++) {
                injectFont(group.getChildAt(i), tf);
            }
        }else if(rootView instanceof TextView) {
            ((TextView) rootView).setTypeface(tf);
        }
    }
}
