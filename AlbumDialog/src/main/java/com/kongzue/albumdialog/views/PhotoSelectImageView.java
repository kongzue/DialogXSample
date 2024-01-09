package com.kongzue.albumdialog.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kongzue.albumdialog.R;

public class PhotoSelectImageView extends androidx.appcompat.widget.AppCompatImageView {

    private Bitmap selectFlagBitmap;
    private Rect selectFlagRect;
    private int borderColor;
    private Float borderWidth;
    private Paint paint;

    boolean selected;

    public PhotoSelectImageView(@NonNull Context context) {
        super(context);
        init();
    }

    public PhotoSelectImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PhotoSelectImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (paint == null) {
            borderColor = getResources().getColor(R.color.albumDefaultThemeDeep);
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(borderColor);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(getBorderWidth());

            selectFlagRect = new Rect((int) (getBorderWidth()), (int) (getBorderWidth()), dip2px(30), dip2px(30));
            selectFlagBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.album_dialog_img_selected);

            Bitmap tintedBitmap = Bitmap.createBitmap(selectFlagBitmap.getWidth(), selectFlagBitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(tintedBitmap);
            Paint paint = new Paint();
            paint.setColorFilter(new PorterDuffColorFilter(getResources().getColor(R.color.albumDefaultThemeDeep), PorterDuff.Mode.SRC_IN));

            Paint backgroundPaint = new Paint();
            backgroundPaint.setColor(Color.WHITE);
            backgroundPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(selectFlagBitmap.getWidth() / 2, selectFlagBitmap.getHeight() / 2, selectFlagBitmap.getWidth() / 3, backgroundPaint);
            canvas.drawBitmap(selectFlagBitmap, 0, 0, paint);
            selectFlagBitmap = tintedBitmap;
        }
    }

    private float getBorderWidth() {
        return borderWidth == null ? dip2px(4) : borderWidth;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (selected) {
            if (radius == 0) {
                canvas.drawRect(0 + getBorderWidth() / 2, 0 + getBorderWidth() / 2, getWidth() - getBorderWidth() / 2, getHeight() - getBorderWidth() / 2, paint);
            } else {
                RectF rect = new RectF(0 + getBorderWidth() / 2, 0 + getBorderWidth() / 2, getWidth() - getBorderWidth() / 2, getHeight() - getBorderWidth() / 2);
                canvas.drawRoundRect(rect, radius, radius, paint);
            }
            canvas.drawBitmap(selectFlagBitmap, null, selectFlagRect, paint);
        }
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    public PhotoSelectImageView setSelectState(boolean selected) {
        this.selected = selected;
        if (selected) {
            setPadding((int) getBorderWidth(), (int) getBorderWidth(), (int) getBorderWidth(), (int) getBorderWidth());
        } else {
            setPadding(0, 0, 0, 0);
        }
        invalidate();
        return this;
    }

    private int dip2px(float dpValue) {
        return (int) (0.5f + dpValue * Resources.getSystem().getDisplayMetrics().density);
    }

    int radius;

    public void setRadius(int r) {
        setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), r);
            }
        });
        setClipToOutline(true);
        radius = r;
        invalidate();
    }

    public PhotoSelectImageView setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    public PhotoSelectImageView setBorderWidth(Float borderWidth) {
        this.borderWidth = borderWidth;
        return this;
    }
}
