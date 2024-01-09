package com.kongzue.albumdialog.views.crop;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * 中间显示的正方形框
 */
public class ClipImageBorderView extends View {
    public static int xxx;
    public static int yyy;

    public static boolean isshowline = false;

    /**
     * 水平方向与View的边距
     */
    public static int mHorizontalPadding = 0;
    /**
     * 垂直方向与View的边距
     */
    public static int mVerticalPadding = 0;
    /**
     * 绘制的矩形的宽度
     */
    public static int mWidth = 0;
    public static int mHeight = 0;
    /**
     * 边框的颜色，默认为白色
     */
    private int mBorderColor = Color.parseColor("#FFFFFF");
    /**
     * 边框的宽度 单位dp
     */
    private int mBorderWidth = 1;

    private Paint mPaint;

    public ClipImageBorderView(Context context) {
        this(context, null);
    }

    public ClipImageBorderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClipImageBorderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mBorderWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mBorderWidth, getResources().getDisplayMetrics());
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 计算距离屏幕垂直边界 的边距
        mWidth = getWidth() - 2 * mHorizontalPadding;
        mVerticalPadding = (int) ((getHeight() - mWidth * getAspectRatio()) / 2.0);         //白描模式矩形高宽比例1.22
        mHeight = getHeight() - mVerticalPadding * 2;

        mPaint.setColor(0xE6ffffff);
        mPaint.setStyle(Paint.Style.FILL);
        // 绘制左边1,阴影效果
        canvas.drawRect(0, 0, mHorizontalPadding, getHeight(), mPaint);
        // 绘制右边2
        canvas.drawRect(getWidth() - mHorizontalPadding, 0, getWidth(),
                getHeight(), mPaint);
        // 绘制上边3
        canvas.drawRect(mHorizontalPadding, 0, getWidth() - mHorizontalPadding, mVerticalPadding, mPaint);
        // 绘制下边4
        canvas.drawRect(mHorizontalPadding, getHeight() - mVerticalPadding, getWidth() - mHorizontalPadding, getHeight(), mPaint);
        // 绘制外边框
        mPaint.setColor(mBorderColor);
        mPaint.setStrokeWidth(mBorderWidth + 5);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(mHorizontalPadding, mVerticalPadding, getWidth() - mHorizontalPadding, getHeight() - mVerticalPadding, mPaint);
        xxx = getWidth() / 2;
        yyy = mVerticalPadding + mWidth / 2;
    }

    public void showline(Canvas canvas) {
        // 得到网格坐标参数
        float upStart_x1, upStart_y1, upStart_x2, upStart_y2;
        float downStart_x1, downStart_y1, downStart_x2, downStart_y2;
        float leftStart_x1, leftStart_y1, leftStart_x2, leftStart_y2;
        float rightStart_x1, rightStart_y1, rightStart_x2, rightStart_y2;

        //上沿两个点的坐标
        upStart_x1 = mWidth / 3 + mHorizontalPadding;
        upStart_y1 = mVerticalPadding;
        upStart_x2 = mWidth / 3 * 2 + mHorizontalPadding;
        upStart_y2 = mVerticalPadding;

        //下沿两个点的坐标
        downStart_x1 = mWidth / 3 + mHorizontalPadding;
        downStart_y1 = getHeight() - mVerticalPadding;
        downStart_x2 = mWidth / 3 * 2 + mHorizontalPadding;
        downStart_y2 = getHeight() - mVerticalPadding;

        // 左边两个点的坐标
        leftStart_x1 = mHorizontalPadding;
        leftStart_y1 = mVerticalPadding + mHeight / 3;
        leftStart_x2 = mHorizontalPadding;
        leftStart_y2 = mVerticalPadding + mHeight / 3 * 2;

        // 右边两个点的坐标
        rightStart_x1 = mHorizontalPadding + mWidth;
        rightStart_y1 = mVerticalPadding + mHeight / 3;
        rightStart_x2 = mHorizontalPadding + mWidth;
        rightStart_y2 = mVerticalPadding + mHeight / 3 * 2;

        // 绘制网格
        mPaint.setStrokeWidth(mBorderWidth);
        canvas.drawLine(upStart_x1, upStart_y1, downStart_x1, downStart_y1, mPaint);
        canvas.drawLine(upStart_x2, upStart_y2, downStart_x2, downStart_y2, mPaint);
        canvas.drawLine(leftStart_x1, leftStart_y1, rightStart_x1, rightStart_y1, mPaint);
        canvas.drawLine(leftStart_x2, leftStart_y2, rightStart_x2, rightStart_y2, mPaint);
    }

    public void setHorizontalPadding(int mHorizontalPadding) {
        this.mHorizontalPadding = mHorizontalPadding;
    }

    public float getAspectRatio() {
        return ((ClipView) getParent()).getAspectRatio();
    }
}

