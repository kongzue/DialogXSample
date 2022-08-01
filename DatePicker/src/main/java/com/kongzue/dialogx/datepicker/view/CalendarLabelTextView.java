package com.kongzue.dialogx.datepicker.view;

import static com.kongzue.dialogx.interfaces.BaseDialog.isNull;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;

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
    boolean isLight;
    boolean today;
    String label;
    
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
    
    int textColor;
    
    @Override
    public void setTextColor(int color) {
        super.setTextColor(color);
        textColor=color;
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
        if (!isNull(label)) {
            drawLabels(canvas);
        }
    }
    
    Paint labelPaint;
    Rect labelRect;
    int labelTextSize = dip2px(10);
    
    private void drawLabels(Canvas canvas) {
        setPadding(0, 0, 0, dip2px(15));
        
        if (labelPaint == null) {
            labelPaint = new Paint();
        }
        labelPaint.setAntiAlias(true);
        labelPaint.setColor(textColor);
        labelPaint.setTextSize(labelTextSize);
        
        if (labelRect == null) {
            labelRect = new Rect();
        }
        labelPaint.getTextBounds(label, 0, label.length(), labelRect);
        
        canvas.drawText(label, getWidth() / 2 - labelRect.width() / 2, getHeight() - labelTextSize, labelPaint);
    }
    
    private static int dip2px(float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
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
    
    public String getLabel() {
        return label;
    }
    
    public CalendarLabelTextView setLabel(String label) {
        this.label = label;
        invalidate();
        return this;
    }
}
