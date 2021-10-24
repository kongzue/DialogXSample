package com.kongzue.dialogx.datepicker.interfaces;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2021/10/24 10:05
 */
public abstract class OnMultiDateSelected {
    public abstract void onSelect(String startText, String endText, int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay);
    
    public void onCancel() {
    }
}
