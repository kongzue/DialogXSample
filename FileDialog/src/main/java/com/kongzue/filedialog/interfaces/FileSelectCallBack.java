package com.kongzue.filedialog.interfaces;

import java.io.File;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2022/3/3 13:04
 */
public interface FileSelectCallBack {
    
    void onSelect(File file,String filePath);
    
}
