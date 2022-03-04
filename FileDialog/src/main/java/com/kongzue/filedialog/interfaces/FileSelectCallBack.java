package com.kongzue.filedialog.interfaces;

import java.io.File;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2022/3/3 13:04
 */
public abstract class FileSelectCallBack {
    
    public void onSelect(File file,String filePath){};
    
    public void onMultiSelect(File[] file,String[] filePath){};
    
}
