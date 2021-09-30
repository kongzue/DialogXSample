package com.kongzue.dialogx.customwheelpicker.interfaces;

import com.kongzue.dialogx.customwheelpicker.CustomWheelPickerDialog;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2021/9/30 8:41
 */
public abstract class OnCustomWheelPickerSelected {
    
    public abstract void onSelected(CustomWheelPickerDialog picker, String text, String[] selectedTexts, int[] selectedIndex);
    
    public void onCancel() {
    }
}
