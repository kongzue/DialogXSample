package com.kongzue.dialogxsampledemo.function.searchdemo;

import java.util.Locale;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2022/4/28 20:14
 */
public class FunctionBean {
    
    public FunctionBean(String name) {
        this.name = name;
    }
    
    public FunctionBean(String name, int iconRedId) {
        this.name = name;
        this.iconRedId = iconRedId;
    }
    
    String name;
    int iconRedId;
    
    public String getName() {
        return name;
    }
    
    public FunctionBean setName(String name) {
        this.name = name;
        return this;
    }
    
    public int getIconRedId() {
        return iconRedId;
    }
    
    public FunctionBean setIconRedId(int iconRedId) {
        this.iconRedId = iconRedId;
        return this;
    }
    
    public boolean isSame(String inputText) {
        if (name == null) return false;
        return name.toLowerCase(Locale.ROOT).contains(inputText.trim().toLowerCase(Locale.ROOT));
    }
}
