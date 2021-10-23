package com.kongzue.dialogx.datepicker.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
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
    
    boolean select;
    int section = 2;
    
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
        this.select = select;
        invalidate();
        return this;
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
        invalidate();
        return this;
    }
}
