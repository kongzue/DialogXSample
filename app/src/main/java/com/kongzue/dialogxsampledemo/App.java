package com.kongzue.dialogxsampledemo;

import android.app.Application;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.util.views.DialogXBaseRelativeLayout;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2021/9/25 11:05
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DialogXBaseRelativeLayout.debugMode = true;
        DialogX.DEBUGMODE = BuildConfig.DEBUG;
        DialogX.init(this);
        DialogX.globalTheme = DialogX.THEME.AUTO;
    }
}
