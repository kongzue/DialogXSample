package com.kongzue.dialogx.sharedialog.interfaces;

import android.content.Context;
import android.view.View;

import com.kongzue.dialogx.sharedialog.bean.ShareData;
import com.kongzue.dialogx.sharedialog.view.DialogXShareButton;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2021/9/26 15:03
 */
public interface OnShareClick {
    
    boolean onClick(Context context, ShareData shareData, View shareButton, int index);
}
