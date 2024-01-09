package com.kongzue.albumdialog.views.crop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2018/8/12 16:26
 */
public class ClipZoomImageView extends androidx.appcompat.widget.AppCompatImageView implements OnScaleGestureListener, OnTouchListener, ViewTreeObserver.OnGlobalLayoutListener {

    public Bitmap bm = null;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    ClipImageBorderView.isshowline = false;
                    ((ClipView) getParent()).invalidate();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    public static float SCALE_MAX = 4.0f;
    private static float SCALE_MID = 2.0f;

    public static float initScale = 1.0f;
    public static float now_scale;
    private boolean once = true;

    /**
     * 用于存放矩阵的9个值
     */
    private final float[] matrixValues = new float[9];

    /**
     * 缩放的手势检测
     */
    private ScaleGestureDetector mScaleGestureDetector = null;
    private final Matrix mScaleMatrix = new Matrix();

    /**
     * 用于双击检测
     */
    private GestureDetector mGestureDetector;
    private boolean isAutoScale;

    private int mTouchSlop;

    private float mLastX;
    private float mLastY;

    private boolean isCanDrag;
    private int lastPointerCount;

    public ClipZoomImageView(Context context) {
        this(context, null);
    }

    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        this.bm = bm;
    }

    public ClipZoomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setScaleType(ScaleType.MATRIX);
        mGestureDetector = new GestureDetector(
                context,
                new SimpleOnGestureListener() {
                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        if (isAutoScale == true)
                            return true;

                        float x = e.getX();
                        float y = e.getY();
                        if (getScale() < SCALE_MID) {
                            ClipZoomImageView.this.postDelayed(
                                    new AutoScaleRunnable(SCALE_MID, x, y), 16);
                            isAutoScale = true;
                        } else {
                            ClipZoomImageView.this.postDelayed(
                                    new AutoScaleRunnable(initScale, x, y), 16);
                            isAutoScale = true;
                        }

                        return true;
                    }
                }
        );
        mScaleGestureDetector = new ScaleGestureDetector(context, this);
        this.setOnTouchListener(this);
    }

    /**
     * 自动缩放的任务、已经进行注释
     */
    private class AutoScaleRunnable implements Runnable {

        static final float BIGGER = 1.07f;
        static final float SMALLER = 0.93f;
        private float mTargetScale;
        private float tmpScale;

        /**
         * 缩放的中心
         */
        private float x;
        private float y;

        /**
         * 传入目标缩放值，根据目标值与当前值，判断应该放大还是缩小
         *
         * @param targetScale
         */
        public AutoScaleRunnable(float targetScale, float x, float y) {
            this.mTargetScale = targetScale;
            this.x = x;
            this.y = y;
            if (getScale() < mTargetScale) {
                tmpScale = BIGGER;
            } else {
                tmpScale = SMALLER;
            }

        }

        @Override
        public void run() {
            // 进行缩放
            mScaleMatrix.postScale(tmpScale, tmpScale, x, y);
            checkBorder();
            setImageMatrix(mScaleMatrix);
            final float currentScale = getScale();
            // 如果值在合法范围内，继续缩放
            if (((tmpScale > 1f) && (currentScale < mTargetScale))
                    || ((tmpScale < 1f) && (mTargetScale < currentScale))) {
                ClipZoomImageView.this.postDelayed(this, 16);
            } else
            // 设置为目标的缩放比例
            {
                final float deltaScale = mTargetScale / currentScale;
                mScaleMatrix.postScale(deltaScale, deltaScale, x, y);
                checkBorder();
                setImageMatrix(mScaleMatrix);
                isAutoScale = false;
            }

        }
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float scale = getScale();
        float scaleFactor = detector.getScaleFactor();

        if (getDrawable() == null)
            return true;

        /**
         * 缩放的范围控制
         */
        if ((scale < SCALE_MAX && scaleFactor > 1.0f) || (scale > initScale && scaleFactor < 1.0f)) {
            /*
             * 最大值最小值判断
             */
            if (scaleFactor * scale < initScale) {
                scaleFactor = (initScale / scale);
            }
            if (scaleFactor * scale > SCALE_MAX) {
                scaleFactor = SCALE_MAX / scale;
            }
            /**
             * 设置缩放比例
             */
            mScaleMatrix.postScale(scaleFactor, scaleFactor,
                    detector.getFocusX(), detector.getFocusY()
            );

            now_scale = scaleFactor * scale;
            setImageMatrix(mScaleMatrix);
        }
        return true;

    }

    /**
     * 用于旋转图片时控制图片的位置范围
     */
    public void check_bitmap() {
        checkBorder();
        setImageMatrix(mScaleMatrix);

    }

    /**
     * 根据当前图片的Matrix获得图片的范围
     *
     * @return
     */
    public RectF getMatrixRectF() {
        Matrix matrix = mScaleMatrix;
        RectF rect = new RectF();
        Drawable d = getDrawable();

        if (null != d) {
            rect.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            matrix.mapRect(rect);
        }
        return rect;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
    }


    /**
     * 移动view
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        mScaleGestureDetector.onTouchEvent(event);

        float x = 0, y = 0;
        // 拿到触摸点的个数
        final int pointerCount = event.getPointerCount();
        // 得到多个触摸点的x与y均值
        for (int i = 0; i < pointerCount; i++) {
            x += event.getX(i);
            y += event.getY(i);
        }
        x = x / pointerCount;
        y = y / pointerCount;

        /**
         * 每当触摸点发生变化时，重置mLasX , mLastY
         */
        if (pointerCount != lastPointerCount) {
            isCanDrag = false;
            mLastX = x;
            mLastY = y;
        }

        lastPointerCount = pointerCount;
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (ClipImageBorderView.isshowline == false) {
                    ClipImageBorderView.isshowline = true;
                    ((ClipView) getParent()).invalidate();
                }
                float dx = x - mLastX;
                float dy = y - mLastY;
                if (!isCanDrag) {
                    isCanDrag = isCanDrag(dx, dy);
                }
                if (isCanDrag) {
                    if (getDrawable() != null) {

                        RectF rectF = getMatrixRectF(); //根据当前图片的Matrix获得图片的范围
                        // 如果宽度小于屏幕宽度，则禁止左右移动
                        if (rectF.width() <= getWidth() - mHorizontalPadding * 2) {
                            dx = 0;
                        }
                        // 如果高度小于屏幕高度，则禁止上下移动
                        if (rectF.height() <= getHeight() - mVerticalPadding * 2) {
                            dy = 0;
                        }
                        mScaleMatrix.postTranslate(dx, dy);
                        checkBorder();
                        setImageMatrix(mScaleMatrix);
                    }
                }
                mLastX = x;
                mLastY = y;
                break;

            case MotionEvent.ACTION_UP:
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessageDelayed(msg, 1000);
            case MotionEvent.ACTION_DOWN:
                ClipImageBorderView.isshowline = true;
                ((ClipView) getParent()).invalidate();
            case MotionEvent.ACTION_CANCEL:
                lastPointerCount = 0;
                break;
        }
        return true;
    }

    public void moveimage(float dx, float dy) {
        mScaleMatrix.postTranslate(dx, dy);
        setImageMatrix(mScaleMatrix);
    }

    /**
     * 获得当前的缩放比例
     *
     * @return
     */
    public final float getScale() {
        mScaleMatrix.getValues(matrixValues);
        return matrixValues[Matrix.MSCALE_X];
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeGlobalOnLayoutListener(this);
    }

    /**
     * 水平方向与View的边距
     */
    private int mHorizontalPadding;
    /**
     * 垂直方向与View的边距
     */
    public static int mVerticalPadding;

    @Override
    public void onGlobalLayout() {
        if (once) {
            Drawable d = getDrawable();
            if (d == null)
                return;

            mVerticalPadding = (int) ((getHeight() - (getWidth() - 2 * mHorizontalPadding) * getAspectRatio()) / 2.0);

            float scale = 1.0f;
            int width = getWidth();
            int height = getHeight();
            // 拿到图片的宽和高
            int dw = d.getIntrinsicWidth();
            int dh = d.getIntrinsicHeight();

            float scaleW = (getWidth() * 1.0f - mHorizontalPadding * 2) / dw;
            float scaleH = (getHeight() * 1.0f - mVerticalPadding * 2) / dh;
            scale = Math.max(scaleW, scaleH);

            now_scale = initScale = scale;
            SCALE_MID = initScale * 2;
            SCALE_MAX = initScale * 4;
            mScaleMatrix.postTranslate((int) ((width - dw) / 2.0), (int) (height - dh) / 2);
            mScaleMatrix.postScale(scale, scale, (int) (getWidth() / 2.0),
                    (int) (getHeight() / 2.0)
            );
            // 图片移动至屏幕中心
            setImageMatrix(mScaleMatrix);
            once = false;
        }

    }

    /**
     * 剪切图片，返回剪切后的bitmap对象
     *
     * @return
     */
    public Bitmap clip() {
        if (bm==null) return null;
        RectF rectF = getMatrixRectF();

        float scare = getScale();

        int image_left = (int) ((mHorizontalPadding - rectF.left) / scare);
        int image_right = (int) ((getWidth() - mHorizontalPadding - rectF.left) / scare);

        int width = image_right - image_left;
        int image_top = (int) ((mVerticalPadding - rectF.top) / scare);
        if (bm.getHeight() < (width + image_top)) {
            width = bm.getHeight() - image_top;
        }

        int height = (int) (width * getAspectRatio());

        if ((image_top + height) > bm.getHeight()) image_top = bm.getHeight() - height;

        if (image_left < 0) image_left = 0;
        if (image_top < 0) image_top = 0;
        if (image_left > bm.getWidth()) image_left = 0;
        if (image_top > bm.getHeight()) image_top = 0;
        if (width <= 0) width = bm.getWidth();
        if (height <= 0) height = bm.getHeight();

        Bitmap bitmap = Bitmap.createBitmap(bm, image_left, image_top, width, height);
        return bitmap;
    }


    /**
     * 边界检测
     */
    public void checkBorder() {

        RectF rect = getMatrixRectF();
        float deltaX = 0;
        float deltaY = 0;
        int width = getWidth();
        int height = getHeight();

        // 如果宽或高大于屏幕，则控制范围 ; 这里的0.001是因为精度丢失会产生问题，但是误差一般很小，所以我们直接加了一个0.01
        if (rect.width() + 0.001 >= width - 2 * mHorizontalPadding) {
            if (rect.left > mHorizontalPadding) {
                deltaX = -rect.left + mHorizontalPadding;
            }
            if (rect.right < width - mHorizontalPadding) {
                deltaX = width - mHorizontalPadding - rect.right;
            }
        }
        if (rect.height() + 2.01 >= height - 2 * mVerticalPadding) {
            if (rect.top > mVerticalPadding) {
                deltaY = -rect.top + mVerticalPadding;
            }

            if (rect.bottom < height - mVerticalPadding) {
                deltaY = height - mVerticalPadding - rect.bottom;
            }
        }
        mScaleMatrix.postTranslate(deltaX, deltaY);

    }

    /**
     * 是否是拖动行为
     *
     * @param dx
     * @param dy
     * @return
     */
    private boolean isCanDrag(float dx, float dy) {
        return Math.sqrt((dx * dx) + (dy * dy)) >= mTouchSlop;
    }

    public void setHorizontalPadding(int mHorizontalPadding) {
        this.mHorizontalPadding = mHorizontalPadding;
    }

    public float getAspectRatio() {
        return ((ClipView) getParent()).getAspectRatio();
    }
}
