package com.kongzue.dialogx.customwheelpicker.interfaces;

import com.kongzue.dialogx.customwheelpicker.CustomWheelPickerDialog;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2021/9/29 17:21
 */
public interface OnWheelChangeListener {
    void onWheel(CustomWheelPickerDialog picker,int wheelIndex,String[] originWheelData,int itemIndex,String itemText);
}
