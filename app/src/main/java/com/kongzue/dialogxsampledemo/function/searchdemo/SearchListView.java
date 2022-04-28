package com.kongzue.dialogxsampledemo.function.searchdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import com.kongzue.dialogx.interfaces.ScrollController;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2022/4/28 20:09
 */
public class SearchListView extends ListView implements ScrollController {
    public SearchListView(Context context) {
        super(context);
    }
    
    public SearchListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public SearchListView(Context context, AttributeSet attrs, int defStyleAttr) {
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
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
