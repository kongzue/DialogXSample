package com.kongzue.dialogxsampledemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.citypicker.CityPickerDialog;
import com.kongzue.dialogx.citypicker.interfaces.OnCitySelected;
import com.kongzue.dialogx.customwheelpicker.CustomWheelPickerDialog;
import com.kongzue.dialogx.customwheelpicker.interfaces.OnCustomWheelPickerSelected;
import com.kongzue.dialogx.customwheelpicker.interfaces.OnWheelChangeListener;
import com.kongzue.dialogx.datepicker.CalendarDialog;
import com.kongzue.dialogx.datepicker.DatePickerDialog;
import com.kongzue.dialogx.datepicker.interfaces.OnDateSelected;
import com.kongzue.dialogx.datepicker.interfaces.OnMultiDateSelected;
import com.kongzue.dialogx.dialogs.PopTip;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.replydialog.ReplyDialog;
import com.kongzue.dialogx.replydialog.interfaces.OnReplyButtonClickListener;
import com.kongzue.dialogx.sharedialog.ShareDialog;
import com.kongzue.dialogx.sharedialog.bean.ShareData;
import com.kongzue.dialogx.sharedialog.interfaces.OnShareClick;
import com.kongzue.dialogx.style.IOSStyle;
import com.kongzue.dialogx.style.KongzueStyle;
import com.kongzue.dialogx.style.MIUIStyle;
import com.kongzue.dialogx.style.MaterialStyle;
import com.kongzue.dialogxsampledemo.function.searchdemo.DataWarehouse;
import com.kongzue.dialogxsampledemo.function.searchdemo.FunctionBean;
import com.kongzue.dialogxsampledemo.function.searchdemo.SearchAdapter;
import com.kongzue.dialogxsampledemo.function.searchdemo.SearchListView;
import com.kongzue.drawerbox.DrawerBox;
import com.kongzue.drawerbox.DrawerBoxDialog;
import com.kongzue.filedialog.FileDialog;
import com.kongzue.filedialog.interfaces.FileSelectCallBack;
import com.kongzue.stacklabelview.StackLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    
    private RadioGroup grpStyle;
    private RadioButton rdoMaterial;
    private RadioButton rdoIos;
    private RadioButton rdoKongzue;
    private RadioButton rdoMiui;
    private RadioGroup grpTheme;
    private RadioButton rdoAuto;
    private RadioButton rdoLight;
    private RadioButton rdoDark;
    private StackLayout boxFunction;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        grpStyle = findViewById(R.id.grp_style);
        rdoMaterial = findViewById(R.id.rdo_material);
        rdoIos = findViewById(R.id.rdo_ios);
        rdoKongzue = findViewById(R.id.rdo_kongzue);
        rdoMiui = findViewById(R.id.rdo_miui);
        grpTheme = findViewById(R.id.grp_theme);
        rdoAuto = findViewById(R.id.rdo_auto);
        rdoLight = findViewById(R.id.rdo_light);
        rdoDark = findViewById(R.id.rdo_dark);
        boxFunction = findViewById(R.id.box_function);
        
        grpStyle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                DialogX.cancelButtonText = "??????";
                switch (checkedId) {
                    case R.id.rdo_material:
                        DialogX.globalStyle = MaterialStyle.style();
                        DialogX.cancelButtonText = "";
                        break;
                    case R.id.rdo_kongzue:
                        DialogX.globalStyle = KongzueStyle.style();
                        break;
                    case R.id.rdo_ios:
                        DialogX.globalStyle = IOSStyle.style();
                        break;
                    case R.id.rdo_miui:
                        DialogX.globalStyle = MIUIStyle.style();
                        break;
                }
            }
        });
        
        grpTheme.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rdo_auto:
                        DialogX.globalTheme = DialogX.THEME.AUTO;
                        break;
                    case R.id.rdo_light:
                        DialogX.globalTheme = DialogX.THEME.LIGHT;
                        break;
                    case R.id.rdo_dark:
                        DialogX.globalTheme = DialogX.THEME.DARK;
                        break;
                }
            }
        });
        
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                onDrawerShow(null);
            }
        },100);
    }
    
    String defaultProvince = "?????????", defaultCity = "?????????", defaultDistrict = "?????????";
    
    public void onCityPicker(View view) {
        CityPickerDialog.build()
                .setDefaultSelect(defaultProvince, defaultCity, defaultDistrict)
                .show(new OnCitySelected() {
                    /**
                     * ??????????????????
                     * @param text ????????????????????????????????????????????????????????????
                     * @param province ??????
                     * @param city ??????
                     * @param district ??????
                     */
                    @Override
                    public void onSelect(String text, String province, String city, String district) {
                        ((Button) view).setText(text);
                        
                        defaultProvince = province;
                        defaultCity = city;
                        defaultDistrict = district;
                    }
                });
    }
    
    int defaultYear = 2021, defaultMonth = 9, defaultDay = 15;
    
    public void onDatePicker(View view) {
        DatePickerDialog.build()
                .setMinTime(1990, 5, 20)            //???????????????????????? 1990???5???20???
                .setMaxTime(2030, 2, 10)            //???????????????????????? 2030???2???10???
                .setDefaultSelect(defaultYear, defaultMonth, defaultDay)    //????????????????????????
                .show(new OnDateSelected() {
                    /**
                     * ??????????????????
                     * @param text ????????????????????????????????????2021-9-25???
                     * @param year  ???
                     * @param month ???
                     * @param day   ???
                     */
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
        int radius = dip2px(15);        //????????????????????????????????????????????????
        List<ShareData> shareDataList = new ArrayList<>();
        shareDataList.add(new ShareData(MainActivity.this, "QQ", R.mipmap.img_qq_ios).setRadius(radius));
        shareDataList.add(new ShareData(MainActivity.this, "??????", R.mipmap.img_wechat_ios).setRadius(radius));
        shareDataList.add(new ShareData(MainActivity.this, "??????", R.mipmap.img_email_ios).setRadius(radius));
        shareDataList.add(new ShareData(MainActivity.this, "????????????", R.mipmap.img_remind_ios).setRadius(radius));
        shareDataList.add(new ShareData(MainActivity.this, "??????", R.mipmap.img_weibo_ios).setRadius(radius));
        shareDataList.add(new ShareData(MainActivity.this, "?????????", R.mipmap.img_memorandum_ios).setRadius(radius));
        
        ShareDialog.build()
                .setShareDataList(shareDataList)
                .show(new OnShareClick() {
                    /**
                     * ??????????????????
                     * @param context       ???????????????
                     * @param shareData     ??????????????? ShareData ??????
                     * @param shareButton   ????????????
                     * @param index         ????????????
                     * @return ??? true ??????????????????????????????
                     */
                    @Override
                    public boolean onClick(Context context, ShareData shareData, View shareButton, int index) {
                        Toast.makeText(MainActivity.this, shareData.getLabel() + " Click!", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });
    }
    
    int[] defaultCustomWheelSelect = new int[]{1, 3, 1};
    
    public void onCustomWheelPicker(View view) {
        CustomWheelPickerDialog.build()
                .addWheel(new String[]{"??????", "??????", "??????"})
                .addWheel(new String[]{"???1???", "???2???", "???3???", "???4???", "???5???", "???6???", "???7???", "???8???", "???9???"})
                .addWheel(new String[]{"?????????", "?????????", "?????????", "?????????", "?????????"})
                .setOnWheelChangeListener(new OnWheelChangeListener() {
                    
                    private void refreshWheelData(CustomWheelPickerDialog picker) {
                        String grade = picker.getWheelSelected(0);
                        int classIndex;
                        switch (grade) {
                            case "??????":
                                picker.setWheel(1, new String[]{
                                        "???1???", "???2???", "???3???", "???4???", "???5???", "???6???", "???7???", "???8???", "???9???"
                                });
                                classIndex = picker.getWheelSelectedIndex(1);
                                if (classIndex <= 3) {
                                    picker.setWheel(2, new String[]{
                                            "?????????", "?????????", "?????????", "?????????", "?????????"
                                    });
                                } else {
                                    picker.setWheel(2, new String[]{
                                            "?????????", "?????????", "?????????", "?????????", "?????????"
                                    });
                                }
                                break;
                            case "??????":
                                picker.setWheel(1, new String[]{
                                        "???1???", "???2???", "???3???", "???4???", "???5???", "???6???", "???7???"
                                });
                                classIndex = picker.getWheelSelectedIndex(1);
                                if (classIndex <= 3) {
                                    picker.setWheel(2, new String[]{
                                            "?????????", "?????????", "?????????"
                                    });
                                } else {
                                    picker.setWheel(2, new String[]{
                                            "?????????", "?????????", "?????????", "?????????", "?????????"
                                    });
                                }
                                break;
                            case "??????":
                                picker.setWheel(1, new String[]{
                                        "???1???", "???2???", "???3???", "???4???", "???5???"
                                });
                                classIndex = picker.getWheelSelectedIndex(1);
                                if (classIndex <= 1) {
                                    picker.setWheel(2, new String[]{
                                            "?????????", "?????????", "?????????", "?????????", "?????????"
                                    });
                                } else {
                                    picker.setWheel(2, new String[]{
                                            "???????????????", "?????????????????????", "?????????", "?????????", "?????????", "?????????", "??????", "?????????", "?????????"
                                    });
                                }
                                break;
                        }
                    }
                    
                    /**
                     * ????????????????????????
                     * @param picker            ???????????????
                     * @param wheelIndex        ???????????????????????????????????????
                     * @param originWheelData   ?????????????????????
                     * @param itemIndex         ????????????????????????
                     * @param itemText          ????????????????????????
                     */
                    @Override
                    public void onWheel(CustomWheelPickerDialog picker, int wheelIndex, String[] originWheelData, int itemIndex, String itemText) {
                        refreshWheelData(picker);
                    }
                })
                .setDefaultSelect(0, defaultCustomWheelSelect[0])
                .setDefaultSelect(1, defaultCustomWheelSelect[1])
                .setDefaultSelect(2, defaultCustomWheelSelect[2])
                .show(new OnCustomWheelPickerSelected() {
                    /**
                     * ???????????????
                     * @param picker        ???????????????
                     * @param text          ???????????????????????????????????? ???4??? ????????????
                     * @param selectedTexts ????????????????????????????????????
                     * @param selectedIndex ????????????????????????????????????
                     */
                    @Override
                    public void onSelected(CustomWheelPickerDialog picker, String text, String[] selectedTexts, int[] selectedIndex) {
                        ((Button) view).setText(text);
                        defaultCustomWheelSelect = selectedIndex;
                    }
                });
    }
    
    public void onReplyComments(View view) {
        ReplyDialog.build()
                .setTitle("?????? @Kongzue:")
                .setReplyButtonText("??????")
                .setContentHint("?????????????????????...")
                .show(new OnReplyButtonClickListener() {
                    @Override
                    public void onClick(View view, String replyText) {
                        PopTip.show("Reply: " + replyText);
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
    
    public void onCalendar(View view) {
        CalendarDialog.build()
                .setMinTime(1990, 5, 20)            //???????????????????????? 1990???5???20???
                .setMaxTime(2030, 2, 10)            //???????????????????????? 2030???2???10???
                .setDefaultSelect(defaultYear, defaultMonth, defaultDay)    //????????????????????????
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
    
    int defaultEndYear = 2021, defaultEndMonth = 9, defaultEndDay = 17;
    
    public void onCalendarMulti(View view) {
        CalendarDialog.build()
                .setMinTime(1990, 5, 20)            //???????????????????????? 1990???5???20???
                .setMaxTime(2030, 2, 10)            //???????????????????????? 2030???2???10???
                .setDefaultSelect(defaultYear, defaultMonth, defaultDay, defaultEndYear, defaultEndMonth, defaultEndDay)    //????????????????????????
                .show(new OnMultiDateSelected() {
                    @Override
                    public void onSelect(String startText, String endText, int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay) {
                        if ((startText.isEmpty() && endText.isEmpty())) {
                            return;
                        }
                        ((Button) view).setText(startText + "~" + endText);
                        
                        defaultYear = startYear;
                        defaultMonth = startMonth;
                        defaultDay = startDay;
                        
                        defaultEndYear = endYear;
                        defaultEndMonth = endMonth;
                        defaultEndDay = endDay;
                    }
                });
    }
    
    public void onCalendarMultiLimit(View view) {
        CalendarDialog.build()
                .setMinTime(1990, 5, 20)            //???????????????????????? 1990???5???20???
                .setMaxTime(2030, 2, 10)            //???????????????????????? 2030???2???10???
                //.setDefaultSelect(defaultYear, defaultMonth, defaultDay, defaultEndYear, defaultEndMonth, defaultEndDay)    //????????????????????????
                .setMaxMultiDay(3)
                .show(new OnMultiDateSelected() {
                    @Override
                    public void onSelect(String startText, String endText, int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay) {
                        if ((startText.isEmpty() && endText.isEmpty())) {
                            return;
                        }
                        ((Button) view).setText(startText + "~" + endText);
                        
                        defaultYear = startYear;
                        defaultMonth = startMonth;
                        defaultDay = startDay;
                        
                        defaultEndYear = endYear;
                        defaultEndMonth = endMonth;
                        defaultEndDay = endDay;
                    }
                });
    }
    
    FileDialog fileDialog;
    
    public void onFileSelect(View view) {
        fileDialog = FileDialog.build();
        fileDialog.selectFile(new FileSelectCallBack() {
            @Override
            public void onSelect(File file, String filePath) {
                PopTip.show("??????????????????" + filePath);
            }
        });
    }
    
    public void onFolderSelect(View view) {
        fileDialog = FileDialog.build();
        fileDialog.setPath("/storage/emulated/0/DCIM").selectFolder(new FileSelectCallBack() {
            @Override
            public void onSelect(File file, String filePath) {
                Log.i(">>>", "onSelect: "+filePath);
                PopTip.show("?????????????????????" + filePath);
            }
        });
    }
    
    public void onMultiFileSelect(View view) {
        fileDialog = FileDialog.build();
        fileDialog.setMaxSelectionNumber(3)
                .selectFile(new FileSelectCallBack() {
                    @Override
                    public void onMultiSelect(File[] file, String[] filePath) {
                        for (String path : filePath) {
                            Log.i(">>>", "???????????????: " + path);
                        }
                        PopTip.show("?????????" + filePath.length + "?????????");
                    }
                });
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (fileDialog != null) {
            fileDialog.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    
    public void onJPGFileSelect(View view) {
        fileDialog = FileDialog.build();
        fileDialog.setSuffixArray(new String[]{".jpg"})
                .selectFile(new FileSelectCallBack() {
                    @Override
                    public void onSelect(File file, String filePath) {
                        PopTip.show("??????????????????" + filePath);
                    }
                });
    }
    
    DrawerBox drawerBox;
    List<FunctionBean> allFunctionList;
    SearchAdapter searchAdapter;
    
    public void onDrawerShow(View view) {
        if (allFunctionList == null) {
            allFunctionList = DataWarehouse.getFunctionDataList();
        }
        if (drawerBox != null) {
            if (drawerBox.isFold()) {
                drawerBox.unfold();
            } else {
                drawerBox.fold();
            }
            return;
        }
        drawerBox = DrawerBox.build().show(new OnBindView<DrawerBoxDialog>(R.layout.layout_drawer) {
            
            private EditText editSearch;
            private TextView txtEmptyResult;
            private SearchListView listSearchResult;
            
            @Override
            public void onBind(DrawerBoxDialog dialog, View v) {
                editSearch = v.findViewById(R.id.edit_search);
                txtEmptyResult = v.findViewById(R.id.txt_empty_result);
                listSearchResult = v.findViewById(R.id.list_search_result);
                
                listSearchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        drawerBox.fold();
                        
                        FunctionBean selectFunction = (FunctionBean) listSearchResult.getAdapter().getItem(position);
                        String tag = selectFunction.getName();
                        View functionButton = boxFunction.findViewWithTag(tag);
                        if (functionButton != null) {
                            functionButton.callOnClick();
                        }
                    }
                });
                
                editSearch.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    
                    }
                    
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        List<FunctionBean> searchResultList = new ArrayList<>();
                        if (s.length() > 0) {
                            for (FunctionBean functionBean : allFunctionList) {
                                if (functionBean.isSame(s.toString())) {
                                    searchResultList.add(functionBean);
                                }
                            }
                        }
                        if (searchResultList.isEmpty()) {
                            txtEmptyResult.setVisibility(View.VISIBLE);
                            listSearchResult.setVisibility(View.GONE);
                        } else {
                            txtEmptyResult.setVisibility(View.GONE);
                            listSearchResult.setVisibility(View.VISIBLE);
                            listSearchResult.setAdapter(new SearchAdapter(MainActivity.this, searchResultList));
                        }
                    }
                    
                    @Override
                    public void afterTextChanged(Editable s) {
                    
                    }
                });
            }
        });
    }
    
    @Override
    protected void onResume() {
        super.onResume();
    }
}