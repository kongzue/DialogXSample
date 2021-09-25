package com.lljjcoder.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 作者：liji on 2017/7/24 06:42
 * 邮箱：lijiwork@sina.com
 * QQ ：275137657
 */

public class utils {
    
    String cityJsonStr = "";
    
    //读取方法
    public static String getJson(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
    
    public static void setBackgroundAlpha(Context mContext, float bgAlpha) {
//        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
//        lp.alpha = bgAlpha;
//        ((Activity) mContext).getWindow().setAttributes(lp);

        if (bgAlpha == 1f) {
            clearDim((Activity) mContext);
        }else{
            applyDim((Activity) mContext, bgAlpha);
        }
    }

    private static void applyDim(Activity activity, float bgAlpha) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            ViewGroup parent = (ViewGroup) activity.getWindow().getDecorView().getRootView();
            //activity跟布局
//        ViewGroup parent = (ViewGroup) parent1.getChildAt(0);
            Drawable dim = new ColorDrawable(Color.BLACK);
            dim.setBounds(0, 0, parent.getWidth(), parent.getHeight());
            dim.setAlpha((int) (255 * bgAlpha));
            ViewGroupOverlay overlay = parent.getOverlay();
            overlay.add(dim);
        }
    }

    private static void clearDim(Activity activity) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            ViewGroup parent = (ViewGroup) activity.getWindow().getDecorView().getRootView();
            //activity跟布局
//        ViewGroup parent = (ViewGroup) parent1.getChildAt(0);
            ViewGroupOverlay overlay = parent.getOverlay();
            overlay.clear();
        }
    }
}
