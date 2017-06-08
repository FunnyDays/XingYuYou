package com.xingyuyou.xingyuyou.weight.dialog;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.xingyuyou.xingyuyou.R;
import com.xingyuyou.xingyuyou.activity.UserInfoActivity;

/**
 * Created by Administrator on 2017/3/24.
 */

public class CustomDialog {

    private Context context;
    private AlertDialog mAlertDialog;
    private ValueAnimator mValueAnimator;
    private ProgressBar mProgressBar;


    public CustomDialog(Context context) {
        this.context = context;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loding, null);
        builder.setView(view);
        mAlertDialog = builder.create();
        mAlertDialog.setCancelable(false);
    }

    /**
     * 带标题的dialog
     *
     * @param context
     * @param msg
     */
    public CustomDialog(Context context, String msg) {
        this.context = context;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loding, null);
        TextView textView = (TextView) view.findViewById(R.id.text);
        textView.setText(msg);
        builder.setView(view);
        mAlertDialog = builder.create();
        mAlertDialog.setCancelable(false);
    }

    public void showDialog() {
        mAlertDialog.show();
    }

    public void dismissDialog() {
        mAlertDialog.dismiss();
       /* if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mValueAnimator = ValueAnimator.ofFloat(0, 20);
            mValueAnimator.setDuration(2000);
            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (animation.getAnimatedValue().toString().equals("20.0"));

                }
            });
            mValueAnimator.start();

        }*/
    }

    public void CancelDialog() {
        if (mValueAnimator != null && mValueAnimator.isStarted())
            mValueAnimator.cancel();
    }

    public void ProgressDialog(Activity activity, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_progress_loding, null);
        builder.setView(view);
        mAlertDialog = builder.create();
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        tvTitle.setText(title);

    }
   public void  setProgressDialog(int progressDialog){
       mProgressBar.setProgress(progressDialog);
    }
    public void EditTextDialog(Activity activity, String title, final UserInfoActivity.EditTextValuesCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_edittext, null);
        builder.setView(view);
        mAlertDialog = builder.create();
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvTitle.setText(title);
        final EditText editText = (EditText) view.findViewById(R.id.et_text);
        Button btNegative = (Button) view.findViewById(R.id.bt_negative);
        Button btPositive = (Button) view.findViewById(R.id.bt_positive);
        btNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlertDialog.dismiss();
            }
        });
        btPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlertDialog.dismiss();
                String editTextText = editText.getText().toString().trim();
                callback.editValues(editTextText);
            }
        });
    }

    public void RadioDialog(final Activity activity, String title, final UserInfoActivity.EditTextValuesCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final View view = LayoutInflater.from(activity).inflate(R.layout.dialog_radio, null);
        builder.setView(view);
        mAlertDialog = builder.create();
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvTitle.setText(title);
        final RadioButton male = (RadioButton) view.findViewById(R.id.radioMale);
        final RadioButton female = (RadioButton) view.findViewById(R.id.radioFemale);
        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if(male.getId()==i){
                    callback.editValues(male.getText().toString());
                }
                if(female.getId()==i){
                    callback.editValues(female.getText().toString());
                }
                mAlertDialog.dismiss();
            }
        });
    }

}
