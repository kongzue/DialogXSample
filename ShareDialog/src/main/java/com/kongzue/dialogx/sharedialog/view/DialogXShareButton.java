package com.kongzue.dialogx.sharedialog.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kongzue.dialogx.util.views.MaxRelativeLayout;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2021/9/26 14:59
 */
public class DialogXShareButton extends androidx.appcompat.widget.AppCompatImageView {
    public DialogXShareButton(@NonNull Context context) {
        super(context);
    }
    
    public DialogXShareButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    
    public DialogXShareButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    
    public void setFilter() {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            drawable = getBackground();
        }
        if (drawable != null) {
            drawable.setColorFilter(Color.parseColor("#DDDDDD"), PorterDuff.Mode.MULTIPLY);
        }
    }
    
    public void removeFilter() {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            drawable = getBackground();
        }
        if (drawable != null) {
            drawable.clearColorFilter();
        }
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setFilter();
                if (dialogContentView != null) {
                    dialogContentY = dialogContentView.getY();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                removeFilter();
                if (dialogContentView != null) {
                    float touchDeltaY = Math.abs(dialogContentY - dialogContentView.getY());
                    if (touchDeltaY > 10) {
                        return true;
                    }
                }
                break;
        }
        ((View) getParent()).onTouchEvent(event);
        return true;
    }
    
    boolean isDialogTouchMove;
    View dialogContentView;
    float dialogContentY;
    
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        View rootView = getRootView();
        View bkgView = rootView.findViewById(com.kongzue.dialogx.R.id.bkg);
        if (bkgView instanceof MaxRelativeLayout) {
            dialogContentView = bkgView;
        }
    }
}
