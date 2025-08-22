package com.kongzue.dialogxsampledemo;

import static com.kongzue.dialogx.dialogs.PopTip.tip;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.hchen.colorpicker.ColorPickerDialog;
import com.kongzue.albumdialog.PhotoAlbumDialog;
import com.kongzue.albumdialog.util.DialogImplCallback;
import com.kongzue.albumdialog.util.SelectPhotoCallback;
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
import com.kongzue.dialogx.dialogs.FullScreenDialog;
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
import com.kongzue.dialogx.util.views.DialogXBaseRelativeLayout;
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

        DialogXBaseRelativeLayout.debugMode = true;

        grpStyle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                DialogX.cancelButtonText = "取消";
                if (checkedId == R.id.rdo_material) {
                    DialogX.globalStyle = MaterialStyle.style();
                    DialogX.cancelButtonText = "";
                } else if (checkedId == R.id.rdo_kongzue) {
                    DialogX.globalStyle = KongzueStyle.style();
                } else if (checkedId == R.id.rdo_ios) {
                    DialogX.globalStyle = IOSStyle.style();
                } else if (checkedId == R.id.rdo_miui) {
                    DialogX.globalStyle = MIUIStyle.style();
                }
            }
        });

        grpTheme.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rdo_auto)
                    DialogX.globalTheme = DialogX.THEME.AUTO;
                else if (checkedId == R.id.rdo_light)
                    DialogX.globalTheme = DialogX.THEME.LIGHT;
                else if (checkedId == R.id.rdo_dark)
                    DialogX.globalTheme = DialogX.THEME.DARK;
            }
        });

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                onDrawerShow(null);
            }
        }, 100);
    }

    String defaultProvince = "陕西省", defaultCity = "西安市", defaultDistrict = "未央区";

    public void onCityPicker(View view) {
        CityPickerDialog.build()
            .setDefaultSelect(defaultProvince, defaultCity, defaultDistrict)
            .show(new OnCitySelected() {
                /**
                 * 此处为回调，
                 * @param text 直接返回文本，例如“陕西省西安市未央区”
                 * @param province 为省
                 * @param city 为市
                 * @param district 为区
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
            .setMinTime(1990, 5, 20)            // 指定最小可选日期 1990年5月20日
            .setMaxTime(2030, 2, 10)            // 指定最大可选日期 2030年2月10日
            .setDefaultSelect(defaultYear, defaultMonth, defaultDay)    // 设置默认选中日期
            .show(new OnDateSelected() {
                /**
                 * 此处为回调，
                 * @param text 直接返回默认文本，例如“2021-9-25”
                 * @param year  年
                 * @param month 月
                 * @param day   日
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
        int radius = dip2px(15);        // 图标自动圆角，可选项，不必须设置
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
                /**
                 * 此处为回调，
                 * @param context       返回上下文
                 * @param shareData     返回点击的 ShareData 实例
                 * @param shareButton   返回按钮
                 * @param index         返回索引
                 * @return 为 true 时，不自动关闭对话框
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
                            if (classIndex <= 3) {
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
                            classIndex = picker.getWheelSelectedIndex(1);
                            if (classIndex <= 1) {
                                picker.setWheel(2, new String[]{
                                    "语文组", "数学组", "英语组", "美术组", "音乐组"
                                });
                            } else {
                                picker.setWheel(2, new String[]{
                                    "学术研究社", "社会问题研究会", "文艺社", "棋艺社", "摄影社", "歌咏队", "剧团", "篮球队", "足球队"
                                });
                            }
                            break;
                    }
                }

                /**
                 * 当滚轮滑动时触发
                 * @param picker            滑动对话框
                 * @param wheelIndex        当前是第几个列表项触发滑动
                 * @param originWheelData   原始列表项数据
                 * @param itemIndex         已选中数据的索引
                 * @param itemText          已选中数据的内容
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
                 * 当确认后，
                 * @param picker        滑动对话框
                 * @param text          返回默认文本，例如“初中 初4班 声乐组”
                 * @param selectedTexts 选中的每个列表项文本集合
                 * @param selectedIndex 选中的每个列表项索引集合
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
            .setTitle("回复 @Kongzue:")
            .setReplyButtonText("发送")
            .setContentHint("请输入回复内容...")
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
        if (item.getItemId() == R.id.action_github) {
            try {
                Uri uri = Uri.parse("https://github.com/kongzue/DialogXSimple");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            } catch (Exception e) {
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private int dip2px(int dpValue) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void onCalendar(View view) {
        CalendarDialog.build()
            .setMinTime(1990, 5, 20)            // 指定最小可选日期 1990年5月20日
            .setMaxTime(2030, 2, 10)            // 指定最大可选日期 2030年2月10日
            .setDefaultSelect(defaultYear, defaultMonth, defaultDay)    // 设置默认选中日期
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
            .setMinTime(1990, 5, 20)            // 指定最小可选日期 1990年5月20日
            .setMaxTime(2030, 2, 10)            // 指定最大可选日期 2030年2月10日
            .setDefaultSelect(defaultYear, defaultMonth, defaultDay, defaultEndYear, defaultEndMonth, defaultEndDay)    // 设置默认选中日期
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
            .setMinTime(1990, 5, 20)            // 指定最小可选日期 1990年5月20日
            .setMaxTime(2030, 2, 10)            // 指定最大可选日期 2030年2月10日
            //.setDefaultSelect(defaultYear, defaultMonth, defaultDay, defaultEndYear, defaultEndMonth, defaultEndDay)    //设置默认选中日期
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
                PopTip.show("选择的文件：" + filePath);
            }
        });
    }

    public void onFolderSelect(View view) {
        fileDialog = FileDialog.build();
        fileDialog.setPath("/storage/emulated/0/DCIM").selectFolder(new FileSelectCallBack() {
            @Override
            public void onSelect(File file, String filePath) {
                Log.i(">>>", "onSelect: " + filePath);
                PopTip.show("选择的文件夹：" + filePath);
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
                        Log.i(">>>", "选中的文件: " + path);
                    }
                    PopTip.show("选中了" + filePath.length + "个文件");
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
                    PopTip.show("选择的文件：" + filePath);
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

    public void onPhotoAlbumShow(View view) {
        PhotoAlbumDialog.build()
            .setCompressQuality(90)     // 开启压缩并指定质量 90%
            .setCompressPhoto(false)    // 是否压缩（开启回调格式为 jpg，不开启回调格式为 png）
            .setMaxSelectPhotoCount(3)  // 最多选择三张照片
            .setClip(true)              // 开启裁剪模式
            .setMaxSize(1000)           // 最高分辨率 1000（宽或高大于 1000会被等比缩小到 1000内）
            .setMaxWidth(1000)          // 最大宽度
            .setMaxHeight(1000)         // 最大高度
            .setCallback(new SelectPhotoCallback() {

                // 多张模式回调（二者其一任选）
                @Override
                public void selectedPhotos(List<String> selectedPhotos) {
                    tip("已选择 " + selectedPhotos.size() + " 张照片");
                }

                // 单张模式回调（二者其一任选）
                @Override
                public void selectedPhoto(String selectedPhotos) {
                    super.selectedPhoto(selectedPhotos);
                }
            })
            .setDialogDialogImplCallback(new DialogImplCallback<FullScreenDialog>() {
                @Override
                public void onDialogCreated(FullScreenDialog dialog) {
                    dialog.setRadius(dip2px(25));
                }
            })
            .show(this);
    }

    public void onColorPickerShow(View view) {
        ColorPickerDialog.build().setValue(Color.parseColor("#099CFC")).setHapticFeedbackEnabled(true).show();
    }
}