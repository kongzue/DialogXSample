package com.kongzue.dialogxsampledemo.function.searchdemo;

import com.kongzue.dialogxsampledemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2022/4/28 20:27
 */
public class DataWarehouse {
    
    public static List<FunctionBean> getFunctionDataList() {
        List<FunctionBean> dataList = new ArrayList<>();
        dataList.add(new FunctionBean("地址选择对话框", R.mipmap.ic_function));
        dataList.add(new FunctionBean("日期选择对话框", R.mipmap.ic_calendar));
        dataList.add(new FunctionBean("分享对话框", R.mipmap.ic_function));
        dataList.add(new FunctionBean("自定义联动选择", R.mipmap.ic_function));
        dataList.add(new FunctionBean("回复评论", R.mipmap.ic_function));
        dataList.add(new FunctionBean("日历单选对话框", R.mipmap.ic_calendar));
        dataList.add(new FunctionBean("日历多选对话框", R.mipmap.ic_calendar));
        dataList.add(new FunctionBean("日历多选对话框(限制3天)", R.mipmap.ic_calendar));
        dataList.add(new FunctionBean("选择文件", R.mipmap.ic_folder));
        dataList.add(new FunctionBean("选择文件夹", R.mipmap.ic_folder));
        dataList.add(new FunctionBean("多选文件", R.mipmap.ic_folder));
        dataList.add(new FunctionBean("限定jpg格式文件", R.mipmap.ic_folder));
        dataList.add(new FunctionBean("抽屉对话框", R.mipmap.ic_function));
        return dataList;
    }
}
