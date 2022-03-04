package com.kongzue.filedialog.util;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.kongzue.dialogx.interfaces.ScrollController;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2022/3/3 23:45
 */
public class FileListView extends ListView implements ScrollController {
    public FileListView(Context context) {
        super(context);
    }
    
    public FileListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public FileListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    
    boolean lockScroll;
    
    @Override
    public boolean isLockScroll() {
        return lockScroll;
    }
    
    public void lockScroll(boolean lockScroll) {
        this.lockScroll = lockScroll;
    }
    
    @Override
    public int getScrollDistance() {
        View c = getChildAt(0);
        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = getFirstVisiblePosition();
        int top = c.getTop();
        int scrollY = -top + firstVisiblePosition * c.getHeight();
        return scrollY;
    }
    
    @Override
    public boolean isCanScroll() {
        return true;
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (lockScroll) {
            return false;
        }
        return super.onTouchEvent(ev);
    }
}
