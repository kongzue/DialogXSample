package com.kongzue.dialogx.sharedialog.bean;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.kongzue.dialogx.interfaces.BaseDialog;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2021/9/26 13:07
 */
public class ShareData {
    
    private String label;
    private Drawable icon;
    private int radius;         //圆角裁剪（像素）
    
    public ShareData(String label, Drawable icon) {
        this.label = label;
        this.icon = icon;
    }
    
    public ShareData(Context context, String label, int iconResId) {
        this.label = label;
        this.icon = AppCompatResources.getDrawable(context, iconResId);
    }
    
    public ShareData(String label, Bitmap icon) {
        this.label = label;
        this.icon = new BitmapDrawable(icon);
    }
    
    public String getLabel() {
        return label;
    }
    
    public Drawable getIcon() {
        if (radius != 0) {
            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(BaseDialog.getTopActivity().getResources(), drawableToBitmap(icon));
            roundedBitmapDrawable.setCornerRadius(radius);
            return roundedBitmapDrawable;
        }
        return icon;
    }
    
    private Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }
    
    public ShareData setLabel(String label) {
        this.label = label;
        return this;
    }
    
    public ShareData setIcon(Drawable icon) {
        this.icon = icon;
        return this;
    }
    
    public ShareData setIcon(Context context, int iconResId) {
        this.icon = AppCompatResources.getDrawable(context, iconResId);
        return this;
    }
    
    public ShareData setIcon(Bitmap icon) {
        this.icon = new BitmapDrawable(icon);
        return this;
    }
    
    public int getRadius() {
        return radius;
    }
    
    public ShareData setRadius(int radius) {
        this.radius = radius;
        return this;
    }
}
