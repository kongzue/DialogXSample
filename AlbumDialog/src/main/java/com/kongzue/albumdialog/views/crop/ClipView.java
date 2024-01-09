package com.kongzue.albumdialog.views.crop;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.util.TypedValue;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.Objects;

public class ClipView extends RelativeLayout {

    private float aspectRatio = 1f;//宽高比
    private ClipZoomImageView mZoomImageView;
    private ClipImageBorderView mClipImageView;
    public Bitmap bm = null;

    /**
     * 裁剪框大小固定，可以提取为自定义属性
     */
    private int mHorizontalPadding = 20;//边距

    public ClipView(Context context) {
        super(context);
        mZoomImageView = new ClipZoomImageView(context);
        mClipImageView = new ClipImageBorderView(context);
        addView(mZoomImageView, new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT));
        addView(mClipImageView, new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT));

        mHorizontalPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mHorizontalPadding, getResources().getDisplayMetrics());
        mZoomImageView.setHorizontalPadding(mHorizontalPadding);
        mClipImageView.setHorizontalPadding(mHorizontalPadding);
    }

    public void setImage(Bitmap bitmap) {
        bm = bitmap;
        try {
            float scare = getScare(bm);
            if (scare < 1) scare = 1;
            bm = zoomBitmap(bm, (int) (bm.getWidth() / scare), (int) (bm.getHeight() / scare));
        } catch (Exception e) {

        }
        mZoomImageView.setImageBitmap(bm);
        invalidate();
    }

    private float getScare(Bitmap bm) {
        if (getHeight() <= 0) return 1f;
        return bm.getHeight() / getHeight();
    }

    /**
     * 图片反转
     */
    public void reverseBitmap() {
        float[] floats = null;
        switch (0) {
            case 0: // 水平反转
                floats = new float[]{-1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f};
                break;
            case 1: // 垂直反转
                floats = new float[]{1f, 0f, 0f, 0f, -1f, 0f, 0f, 0f, 1f};
                break;
        }

        if (floats != null) {
            Matrix matrix = new Matrix();
            matrix.setValues(floats);
            float dx = getXpoint();
            float dxx = getXRightpoint();
            float dx_should = getWidth() / 2 - (dxx - ClipImageBorderView.mHorizontalPadding - mClipImageView.mWidth / 2);

            bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
            mZoomImageView.setImageBitmap(bm);
            mZoomImageView.check_bitmap();
            mZoomImageView.moveimage(dx_should - dx, 0);


        }
    }

    /**
     * 图片旋转
     */
    public void rotateBitmap() {
        float alpha = 90;
        int width = bm.getWidth();
        int height = bm.getHeight();
        Matrix matrix = new Matrix();
        matrix.setRotate(alpha);
        // 围绕原地进行旋转
        bm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);

        float dx = getXpoint();
        float dy = getYpoint();
        float dyy = getYBottompoint();

        mZoomImageView.setImageBitmap(bm);
        float yy = ClipImageBorderView.mVerticalPadding + mClipImageView.mWidth / 2 - (getWidth() / 2 - dx) - dy;
        float xx = getWidth() / 2 - (dyy - ClipImageBorderView.mVerticalPadding - mClipImageView.mWidth / 2) - dx;
        mZoomImageView.moveimage(xx, yy);

    }


    public float getXpoint() {
        RectF rect = mZoomImageView.getMatrixRectF();
        return rect.left;
    }

    public float getXRightpoint() {
        RectF rect = mZoomImageView.getMatrixRectF();
        return rect.right;
    }

    public float getYpoint() {
        RectF rect = mZoomImageView.getMatrixRectF();
        return rect.top;
    }

    public float getYBottompoint() {
        RectF rect = mZoomImageView.getMatrixRectF();
        return rect.bottom;
    }

    /**
     * 对外公布设置边距的方法,单位为dp
     */
    public void setHorizontalPadding(int mHorizontalPadding) {
        this.mHorizontalPadding = mHorizontalPadding;
    }

    /**
     * 裁切图片
     *
     * @return
     */
    public Bitmap clip() {
        return mZoomImageView.clip();
    }

    public Bitmap getBitmap() {
        return bm;
    }

    private Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return newbmp;
    }

    public float getAspectRatio() {
        return aspectRatio;
    }

    public ClipView setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
        return this;
    }

    public void loadImage(String uri) {
        Glide.with(getContext())
                .asBitmap()
                .load(uri)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                        setImage(bitmap);
                    }
                });
    }
}
