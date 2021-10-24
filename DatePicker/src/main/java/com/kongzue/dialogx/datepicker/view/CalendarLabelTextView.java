package com.kongzue.dialogx.datepicker.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.kongzue.dialogx.datepicker.R;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2021/10/22 11:33
 */
public class CalendarLabelTextView extends androidx.appcompat.widget.AppCompatTextView {
    
    boolean debug;
    boolean select;
    int section = 2;
    boolean isLight;
    boolean today;
    
    public CalendarLabelTextView(Context context) {
        super(context);
    }
    
    public CalendarLabelTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    
    public CalendarLabelTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    
    public boolean isSelect() {
        return select;
    }
    
    public CalendarLabelTextView setSelect(boolean select) {
        if (debug){
            try{
                throw new RuntimeException();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        this.select = select;
        refreshStatus();
        invalidate();
        return this;
    }
    
    private void refreshStatus() {
        if (select) {
            setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            setTextColor(getResources().getColor(R.color.white));
        } else {
            if (today) {
                setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                setTextColor(getResources().getColor(R.color.dialogXCalendarToday));
            } else {
                setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                setTextColor(getResources().getColor(
                        isLight() ? R.color.black : R.color.white)
                );
            }
        }
    }
    
    Paint bkgPaint;
    
    @Override
    protected void onDraw(Canvas canvas) {
        if (select) {
            int size = Math.min(getWidth(), getHeight());
            if (bkgPaint == null) {
                bkgPaint = new Paint();
                bkgPaint.setAntiAlias(true);
                bkgPaint.setColor(getResources().getColor(R.color.dialogXCalendarSelected));
                bkgPaint.setStyle(Paint.Style.FILL);
            }
            switch (section) {
                case 0:
                    canvas.drawRect(0, 0, getWidth(), getHeight(), bkgPaint);
                    break;
                case 1:
                    canvas.drawCircle(getWidth() / 2, getHeight() / 2, size / 2, bkgPaint);
                    canvas.drawRect(0, 0, getWidth() / 2, getHeight(), bkgPaint);
                    break;
                case -1:
                    canvas.drawCircle(getWidth() / 2, getHeight() / 2, size / 2, bkgPaint);
                    canvas.drawRect(getWidth() / 2, 0, getWidth(), getHeight(), bkgPaint);
                    break;
                default:
                    canvas.drawCircle(getWidth() / 2, getHeight() / 2, size / 2, bkgPaint);
                    break;
            }
        }
        super.onDraw(canvas);
    }
    
    public int getSection() {
        return section;
    }
    
    public CalendarLabelTextView setSection(int section) {
        this.section = section;
        refreshStatus();
        invalidate();
        return this;
    }
    
    public boolean isLight() {
        return isLight;
    }
    
    public CalendarLabelTextView setLight(boolean light) {
        isLight = light;
        refreshStatus();
        return this;
    }
    
    public boolean isToday() {
        return today;
    }
    
    public CalendarLabelTextView setToday(boolean today) {
        this.today = today;
        refreshStatus();
        invalidate();
        return this;
    }
    
    public CalendarLabelTextView setDebug(boolean debug) {
        this.debug = debug;
        return this;
    }
}
