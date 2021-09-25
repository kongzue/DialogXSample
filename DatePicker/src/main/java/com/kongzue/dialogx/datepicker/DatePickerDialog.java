package com.kongzue.dialogx.datepicker;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.datepicker.interfaces.OnDateSelected;
import com.kongzue.dialogx.datepicker.view.ArrayWheelAdapter;
import com.kongzue.dialogx.datepicker.view.OnWheelChangedListener;
import com.kongzue.dialogx.datepicker.view.WheelView;
import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialogx.style.MaterialStyle;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2021/9/25 12:37
 */
public class DatePickerDialog {
    
    private int selectYearIndex, selectMonthIndex, selectDayIndex;
    private BottomDialog bottomDialog;
    private int maxYear = 2100, minYear = 1900;
    
    public static DatePickerDialog build() {
        return new DatePickerDialog();
    }
    
    public DatePickerDialog setDefaultSelect(int year, int month, int day) {
        selectMonthIndex = month - 1;
        for (int i = minYear; i <= maxYear; i++) {
            if (i == year) {
                selectYearIndex = i - minYear;
                for (int d = 1; d <= getLastDayOfMonth(year, month); d++) {
                    if (d == day) {
                        selectDayIndex = d - 1;
                        break;
                    }
                }
            }
        }
        return this;
    }
    
    public DatePickerDialog cleanDefaultSelect() {
        selectYearIndex = 0;
        selectMonthIndex = 0;
        selectDayIndex = 0;
        return this;
    }
    
    List<String> yearList;
    List<String> monthList;
    List<String> dayList;
    
    public DatePickerDialog show(OnDateSelected onDateSelected) {
        bottomDialog = BottomDialog.show(new OnBindView<BottomDialog>(R.layout.dialogx_date_picker) {
            
            private TextView txtDialogTitle;
            private LinearLayout llTitleBackground;
            private LinearLayout llTitle;
            private WheelView idYear;
            private WheelView idMonth;
            private WheelView idDay;
            
            @Override
            public void onBind(BottomDialog dialog, View v) {
                txtDialogTitle = v.findViewById(R.id.txt_dialog_title);
                llTitleBackground = v.findViewById(R.id.ll_title_background);
                llTitle = v.findViewById(R.id.ll_title);
                idYear = v.findViewById(R.id.id_year);
                idMonth = v.findViewById(R.id.id_month);
                idDay = v.findViewById(R.id.id_day);
                
                txtDialogTitle.getPaint().setFakeBoldText(true);
                
                idYear.addChangingListener(new OnWheelChangedListener() {
                    @Override
                    public void onChanged(WheelView wheelView, int oldValue, int newValue) {
                        selectYearIndex = newValue;
                        initDays();
                    }
                });
                
                idMonth.addChangingListener(new OnWheelChangedListener() {
                    @Override
                    public void onChanged(WheelView wheelView, int oldValue, int newValue) {
                        selectMonthIndex = newValue;
                        initDays();
                    }
                });
                
                idDay.addChangingListener(new OnWheelChangedListener() {
                    @Override
                    public void onChanged(WheelView wheelView, int oldValue, int newValue) {
                        selectDayIndex = newValue;
                    }
                });
                initYear();
                initMonth();
            }
            
            private void initYear() {
                yearList = new ArrayList<>();
                for (int i = minYear; i <= maxYear; i++) {
                    yearList.add(String.valueOf(i));
                }
                ArrayWheelAdapter<String> yearAdapter = new ArrayWheelAdapter<String>(BottomDialog.getContext(), yearList);
                yearAdapter.setItemResource(R.layout.default_item_date);
                yearAdapter.setItemTextResource(R.id.default_item_date_name_tv);
                idYear.setViewAdapter(yearAdapter);
                idYear.setCurrentItem(selectYearIndex < yearList.size() ? selectYearIndex : 0);
            }
            
            private void initMonth() {
                monthList = new ArrayList<>();
                for (int i = 1; i <= 12; i++) {
                    monthList.add(String.valueOf(i));
                }
                ArrayWheelAdapter<String> monthAdapter = new ArrayWheelAdapter<String>(BottomDialog.getContext(), monthList);
                monthAdapter.setItemResource(R.layout.default_item_date);
                monthAdapter.setItemTextResource(R.id.default_item_date_name_tv);
                idMonth.setViewAdapter(monthAdapter);
                idMonth.setCurrentItem(selectMonthIndex < monthList.size() ? selectMonthIndex : 0);
            }
            
            private void initDays() {
                if (yearList == null || monthList == null) return;
                int year = Integer.parseInt(yearList.get(idYear.getCurrentItem()));
                int month = Integer.parseInt(monthList.get(idMonth.getCurrentItem()));
                int maxDay = getLastDayOfMonth(year, month);
                if (maxDay != 0) {
                    dayList = new ArrayList<>();
                    for (int i = 1; i <= maxDay; i++) {
                        dayList.add(String.valueOf(i));
                    }
                    ArrayWheelAdapter<String> dayAdapter = new ArrayWheelAdapter<String>(BottomDialog.getContext(), dayList);
                    dayAdapter.setItemResource(R.layout.default_item_date);
                    dayAdapter.setItemTextResource(R.id.default_item_date_name_tv);
                    idDay.setViewAdapter(dayAdapter);
                    idDay.setCurrentItem(selectDayIndex < dayList.size() ? selectDayIndex : 0);
                }
            }
        })
                .setCancelable(true)
                .setAllowInterceptTouch(false);
        if (DialogX.globalStyle instanceof MaterialStyle) {
            bottomDialog.setOkButton(R.string.dialogx_date_picker_ok_button, new OnDialogButtonClickListener<BottomDialog>() {
                @Override
                public boolean onClick(BottomDialog baseDialog, View v) {
                    onDateSelected.onSelect(yearList.get(selectYearIndex) + "-" + monthList.get(selectMonthIndex) + "-" + dayList.get(selectDayIndex),
                            Integer.parseInt(yearList.get(selectYearIndex)),
                            Integer.parseInt(monthList.get(selectMonthIndex)),
                            Integer.parseInt(dayList.get(selectDayIndex))
                    );
                    return false;
                }
            }).setCancelButton(R.string.dialogx_date_picker_dialog_cancel, new OnDialogButtonClickListener<BottomDialog>() {
                @Override
                public boolean onClick(BottomDialog baseDialog, View v) {
                    onDateSelected.onCancel();
                    return false;
                }
            });
        } else {
            bottomDialog.setCancelButton(R.string.dialogx_date_picker_ok_button, new OnDialogButtonClickListener<BottomDialog>() {
                @Override
                public boolean onClick(BottomDialog baseDialog, View v) {
                    onDateSelected.onSelect(yearList.get(selectYearIndex) + "-" + monthList.get(selectMonthIndex) + "-" + dayList.get(selectDayIndex),
                            Integer.parseInt(yearList.get(selectYearIndex)),
                            Integer.parseInt(monthList.get(selectMonthIndex)),
                            Integer.parseInt(dayList.get(selectDayIndex))
                    );
                    return false;
                }
            });
        }
        return this;
    }
    
    public BottomDialog getBottomDialog() {
        return bottomDialog;
    }
    
    public int getMaxYear() {
        return maxYear;
    }
    
    public int getMinYear() {
        return minYear;
    }
    
    public DatePickerDialog setMaxYear(int maxYear) {
        this.maxYear = maxYear;
        return this;
    }
    
    public DatePickerDialog setMinYear(int minYear) {
        this.minYear = minYear;
        return this;
    }
    
    private int getLastDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, 0);
        return cal.get(Calendar.DAY_OF_MONTH);
    }
}
