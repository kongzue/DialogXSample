package com.kongzue.dialogx.datepicker.interfaces;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2021/9/25 13:44
 */
public abstract class OnDateSelected {
    public abstract void onSelect(String text, int year, int month, int day);
    
    public void onCancel() {
    }
}
