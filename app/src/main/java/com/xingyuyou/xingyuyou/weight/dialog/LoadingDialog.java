package com.xingyuyou.xingyuyou.weight.dialog;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.xingyuyou.xingyuyou.R;

/**
 * Created by Administrator on 2017/3/24.
 */

public class LoadingDialog {

    private Context context;
    private AlertDialog mAlertDialog;
    private ValueAnimator mValueAnimator;


    public LoadingDialog(Context context) {
        this.context = context;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loding, null);
        builder.setView(view);
        mAlertDialog = builder.create();
        mAlertDialog.setCancelable(false);

    }

    public void showDialog() {
        mAlertDialog.show();
    }

    public void dismissDialog() {
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mValueAnimator = ValueAnimator.ofFloat(0, 20);
            mValueAnimator.setDuration(2000);
            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Log.e("value", animation.getAnimatedValue().toString());
                    if (animation.getAnimatedValue().toString().equals("20.0"))
                        mAlertDialog.dismiss();
                }
            });
            mValueAnimator.start();

        }
    }
    public void CancelDialog(){
        if (mValueAnimator!=null&&mValueAnimator.isStarted())
        mValueAnimator.cancel();
    }

}
