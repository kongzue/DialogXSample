package com.kongzue.dialogx.citypicker.interfaces;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2021/9/25 11:13
 */
public abstract class OnCitySelected {
    public abstract void onSelect(String text, String province, String city, String district);
    
    public void onCancel() {
    }
}
