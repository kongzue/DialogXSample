package com.kongzue.dialogxsampledemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kongzue.dialogx.citypicker.CityPickerDialog;
import com.kongzue.dialogx.citypicker.interfaces.OnCitySelected;
import com.kongzue.dialogx.customwheelpicker.CustomWheelPickerDialog;
import com.kongzue.dialogx.customwheelpicker.interfaces.OnCustomWheelPickerSelected;
import com.kongzue.dialogx.customwheelpicker.interfaces.OnWheelChangeListener;
import com.kongzue.dialogx.datepicker.DatePickerDialog;
import com.kongzue.dialogx.datepicker.interfaces.OnDateSelected;
import com.kongzue.dialogx.sharedialog.ShareDialog;
import com.kongzue.dialogx.sharedialog.bean.ShareData;
import com.kongzue.dialogx.sharedialog.interfaces.OnShareClick;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    
    String defaultProvince = "陕西省", defaultCity = "西安市", defaultDistrict = "未央区";
    
    public void onCityPicker(View view) {
        CityPickerDialog.build()
                .setDefaultSelect(defaultProvince, defaultCity, defaultDistrict)
                .show(new OnCitySelected() {
                    @Override
                    public void onSelect(String text, String province, String city, String district) {
                        ((Button) view).setText(text);
                        
                        defaultProvince = province;
                        defaultCity = city;
                        defaultDistrict = district;
                    }
                });
    }
    
    int defaultYear = 2021, defaultMonth = 9, defaultDay = 25;
    
    public void onDatePicker(View view) {
        DatePickerDialog.build()
                .setDefaultSelect(defaultYear, defaultMonth, defaultDay)
                .show(new OnDateSelected() {
                    @Override
                    public void onSelect(String text, int year, int month, int day) {
                        ((Button) view).setText(text);
                        
                        defaultYear = year;
                        defaultMonth = month;
                        defaultDay = day;
                    }
                });
    }
    
    public void onShare(View view) {
        int radius = dip2px(15);        //图标自动圆角，可选项，不必须设置
        List<ShareData> shareDataList = new ArrayList<>();
        shareDataList.add(new ShareData(MainActivity.this, "QQ", R.mipmap.img_qq_ios).setRadius(radius));
        shareDataList.add(new ShareData(MainActivity.this, "微信", R.mipmap.img_wechat_ios).setRadius(radius));
        shareDataList.add(new ShareData(MainActivity.this, "邮件", R.mipmap.img_email_ios).setRadius(radius));
        shareDataList.add(new ShareData(MainActivity.this, "待办事项", R.mipmap.img_remind_ios).setRadius(radius));
        shareDataList.add(new ShareData(MainActivity.this, "微博", R.mipmap.img_weibo_ios).setRadius(radius));
        shareDataList.add(new ShareData(MainActivity.this, "备忘录", R.mipmap.img_memorandum_ios).setRadius(radius));
        
        ShareDialog.build()
                .setShareDataList(shareDataList)
                .show(new OnShareClick() {
                    @Override
                    public boolean onClick(Context context, ShareData shareData, View shareButton, int index) {
                        Toast.makeText(MainActivity.this, shareData.getLabel() + " Click!", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });
    }
    
    public void onCustomWheelPicker(View view) {
        CustomWheelPickerDialog.build()
                .addWheel(new String[]{"小学", "初中", "高中"})
                .addWheel(new String[]{"小1班", "小2班", "小3班", "小4班", "小5班", "小6班", "小7班", "小8班", "小9班"})
                .addWheel(new String[]{"语文组", "数学组", "英语组", "美术组", "音乐组"})
                .setOnWheelChangeListener(new OnWheelChangeListener() {
                    
                    private void refreshWheelData(CustomWheelPickerDialog picker) {
                        String grade = picker.getWheelSelected(0);
                        int classIndex;
                        switch (grade) {
                            case "小学":
                                picker.setWheel(1, new String[]{
                                        "小1班", "小2班", "小3班", "小4班", "小5班", "小6班", "小7班", "小8班", "小9班"
                                });
                                classIndex = picker.getWheelSelectedIndex(1);
                                if (classIndex <= 3) {
                                    picker.setWheel(2, new String[]{
                                            "语文组", "数学组", "英语组", "美术组", "音乐组"
                                    });
                                } else {
                                    picker.setWheel(2, new String[]{
                                            "篮球组", "古筝组", "书法组", "武术组", "舞蹈组"
                                    });
                                }
                                break;
                            case "初中":
                                picker.setWheel(1, new String[]{
                                        "初1班", "初2班", "初3班", "初4班", "初5班", "初6班", "初7班"
                                });
                                classIndex = picker.getWheelSelectedIndex(1);
                                if (classIndex <= 1) {
                                    picker.setWheel(2, new String[]{
                                            "奥数组", "声乐组", "实验组"
                                    });
                                } else {
                                    picker.setWheel(2, new String[]{
                                            "化学组", "生物组", "物理组", "艺术组", "美术组"
                                    });
                                }
                                break;
                            case "高中":
                                picker.setWheel(1, new String[]{
                                        "高1班", "高2班", "高3班", "高4班", "高5班"
                                });
                                picker.setWheel(2, new String[]{
                                        "学术研究社", "社会问题研究会", "文艺社", "棋艺社", "摄影社", "歌咏队", "剧团", "篮球队", "足球队"
                                });
                                break;
                        }
                    }
                    
                    @Override
                    public void onWheel(CustomWheelPickerDialog picker, int wheelIndex, String[] originWheelData, int itemIndex, String itemText) {
                        refreshWheelData(picker);
                    }
                })
                .show(new OnCustomWheelPickerSelected() {
                    @Override
                    public void onSelected(CustomWheelPickerDialog picker, String text, String[] selectedTexts, int[] selectedIndex) {
                        ((Button) view).setText(text);
                    }
                });
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_github:
                try {
                    Uri uri = Uri.parse("https://github.com/kongzue/DialogXSimple");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } catch (Exception e) {
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    private int dip2px(int dpValue) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}