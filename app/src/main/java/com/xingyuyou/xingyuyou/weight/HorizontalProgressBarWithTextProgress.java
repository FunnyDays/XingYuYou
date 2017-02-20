package com.xingyuyou.xingyuyou.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

import com.xingyuyou.xingyuyou.R;


/**
 * Created by Administrator on 2016/12/29.
 */

public class HorizontalProgressBarWithTextProgress extends ProgressBar {
    private static final int DEFAULT_TEXT_SIZE = 10;
    private static final int DEFAULT_TEXT_COLOR = 0xFFFC00D1;
    private static final int DEFAULT_TEXT_OFFSET = 10;
    private static final int DEFAULT_COLOR_UNREACH = 0xFFD3D6DA;
    private static final int DEFAULT_HEIGHT_UNREACH = 2;
    private static final int DEFAULT_COLOR_REACH = DEFAULT_TEXT_COLOR;
    private static final int DEFAULT_HEIGHT_REACH = 2;

    private int mTextSize = sp2px(DEFAULT_TEXT_SIZE);
    private int mTextColor = DEFAULT_TEXT_COLOR;
    private int mTextOffset = dp2px(DEFAULT_TEXT_OFFSET);
    private int mUnreachColor = DEFAULT_COLOR_UNREACH;
    private int mUnreachHeight = dp2px(DEFAULT_HEIGHT_UNREACH);
    private int mReachColor = DEFAULT_COLOR_REACH;
    private int mReachHeight = dp2px(DEFAULT_HEIGHT_REACH);

    private Paint mPaint = new Paint();
    private int mRealWidth;//onMesure赋值onDraw使用

    public HorizontalProgressBarWithTextProgress(Context context) {
        this(context, null);
    }

    public HorizontalProgressBarWithTextProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalProgressBarWithTextProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainStyleAttrs(attrs);
    }

    /**
     * 获取自定义属性
     *
     * @param attrs
     */
    private void obtainStyleAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs,
                R.styleable.HorizontalProgressBarWithTextProgress);
        mTextSize = (int) typedArray.getDimension(
                R.styleable.HorizontalProgressBarWithTextProgress_progress_text_size, mTextSize);
        mTextColor = typedArray.getColor(
                R.styleable.HorizontalProgressBarWithTextProgress_progress_text_color, mTextColor);
        mTextOffset = (int) typedArray.getDimension(
                R.styleable.HorizontalProgressBarWithTextProgress_progress_text_offset, mTextOffset);
        mReachColor = typedArray.getColor(
                R.styleable.HorizontalProgressBarWithTextProgress_progress_reach_color, mReachColor);
        mReachHeight = (int) typedArray.getDimension(
                R.styleable.HorizontalProgressBarWithTextProgress_progress_reach_height, mReachHeight);
        mUnreachColor = typedArray.getColor(
                R.styleable.HorizontalProgressBarWithTextProgress_progress_unreach_color, mUnreachColor);
        mUnreachHeight = (int) typedArray.getDimension(
                R.styleable.HorizontalProgressBarWithTextProgress_progress_unreach_height, mUnreachHeight);

        typedArray.recycle();
        mPaint.setTextSize(mTextSize);
    }

    private int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal,
                getResources().getDisplayMetrics());
    }

    private int sp2px(int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal,
                getResources().getDisplayMetrics());
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthsize = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = measureHeight(heightMeasureSpec);
        setMeasuredDimension(widthsize, measureHeight);
        mRealWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(getPaddingLeft(), getHeight() / 2);
        boolean noNeedUnRech = false;

        //draw reach bar
        String text=getProgress()+"%";
        int textWidth= (int) mPaint.measureText(text);

        float radio = getProgress() * 1.0f / getMax();
        float progressX=radio*mRealWidth;
        if (progressX+textWidth>mRealWidth){
            progressX=mRealWidth-textWidth;
            noNeedUnRech=true;
        }
        float endX=progressX-mTextOffset/2;
        if (endX>0){
            mPaint.setColor(mReachColor);
            mPaint.setStrokeWidth(mReachHeight);
            canvas.drawLine(0,0,endX,0,mPaint);
        }
        //draw text
        mPaint.setColor(mTextColor);
        int y=(int)(-(mPaint.descent()+mPaint.ascent())/2);
        canvas.drawText(text,progressX,y,mPaint);
        //draw unreach bar
        if (!noNeedUnRech){
            float start=progressX+mTextOffset/2+textWidth;
            mPaint.setColor(mUnreachColor);
            mPaint.setStrokeWidth(mUnreachHeight);
            canvas.drawLine(start,0,mRealWidth,0,mPaint);
        }
        canvas.restore();
    }

    private int measureHeight(int heightMeasureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);

        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            int textHeight = (int) (mPaint.descent() - mPaint.ascent());
            result = getPaddingBottom() + getPaddingTop()
                    + Math.max(Math.max(mReachHeight, mUnreachHeight), Math.abs(textHeight));
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;
    }
}
