package com.kongzue.dialogx.datepicker;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.datepicker.interfaces.OnDateSelected;
import com.kongzue.dialogx.datepicker.view.ArrayWheelAdapter;
import com.kongzue.dialogx.datepicker.view.OnWheelChangedListener;
import com.kongzue.dialogx.datepicker.view.WheelView;
import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.interfaces.BaseDialog;
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
    
    protected int selectYearIndex, selectMonthIndex, selectDayIndex;
    protected BottomDialog bottomDialog;
    protected int maxYear = 2100, minYear = 1900;
    protected int maxYearMonth = 12, minYearMonth = 1;
    protected int maxYearDay = 30, minYearDay = 1;
    protected String yearLabel = "年", monthLabel = "月", dayLabel = "日";
    protected String title;
    
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
    private TextView txtDialogTitle;
    
    public DatePickerDialog show(OnDateSelected onDateSelected) {
        bottomDialog = BottomDialog.show(new OnBindView<BottomDialog>(R.layout.dialogx_date_picker) {
            
            private LinearLayout llTitleBackground;
            private LinearLayout llTitle;
            private WheelView idYear;
            private WheelView idMonth;
            private WheelView idDay;
            
            @Override
            public void onBind(BottomDialog dialog, View v) {
                bottomDialog = dialog;
                txtDialogTitle = v.findViewById(R.id.txt_dialog_title);
                llTitleBackground = v.findViewById(R.id.ll_title_background);
                llTitle = v.findViewById(R.id.ll_title);
                idYear = v.findViewById(R.id.id_year);
                idMonth = v.findViewById(R.id.id_month);
                idDay = v.findViewById(R.id.id_day);
                
                txtDialogTitle.setTextColor(dialog.getResources().getColor(dialog.isLightTheme() ? R.color.black : R.color.white));
                txtDialogTitle.getPaint().setFakeBoldText(true);
                if (title != null) txtDialogTitle.setText(title);
                
                idYear.addChangingListener(new OnWheelChangedListener() {
                    @Override
                    public void onChanged(WheelView wheelView, int oldValue, int newValue) {
                        selectYearIndex = newValue;
                        initMonth();
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
            }
            
            private void initYear() {
                yearList = new ArrayList<>();
                for (int i = minYear; i <= maxYear; i++) {
                    yearList.add(i + yearLabel);
                }
                ArrayWheelAdapter<String> yearAdapter = new ArrayWheelAdapter<String>(BottomDialog.getTopActivity(), yearList);
                yearAdapter.setItemResource(R.layout.default_item_date);
                yearAdapter.setItemTextResource(R.id.default_item_date_name_tv);
                yearAdapter.setTextColor(bottomDialog.getResources().getColor(bottomDialog.isLightTheme() ? R.color.black60 : R.color.white70));
                idYear.setViewAdapter(yearAdapter);
                idYear.setCurrentItem(selectYearIndex < yearList.size() ? selectYearIndex : 0);
            }
            
            private void initMonth() {
                monthList = new ArrayList<>();
                if (selectYearIndex == 0) {
                    //min
                    for (int i = Math.max(minYearMonth, 1); i <= 12; i++) {
                        monthList.add(i + monthLabel);
                    }
                } else if (selectYearIndex == yearList.size() - 1) {
                    //max
                    for (int i = 1; i <= (maxYearMonth == 0 ? 12 : Math.min(12, maxYearMonth)); i++) {
                        monthList.add(i + monthLabel);
                    }
                } else {
                    for (int i = 1; i <= 12; i++) {
                        monthList.add(i + monthLabel);
                    }
                }
                ArrayWheelAdapter<String> monthAdapter = new ArrayWheelAdapter<String>(BottomDialog.getTopActivity(), monthList);
                monthAdapter.setItemResource(R.layout.default_item_date);
                monthAdapter.setItemTextResource(R.id.default_item_date_name_tv);
                monthAdapter.setTextColor(bottomDialog.getResources().getColor(bottomDialog.isLightTheme() ? R.color.black60 : R.color.white70));
                idMonth.setViewAdapter(monthAdapter);
                idMonth.setCurrentItem(selectMonthIndex < monthList.size() ? selectMonthIndex : 0);
            }
            
            private void initDays() {
                if (yearList == null || monthList == null) return;
                int year = 0;
                int month = 0;
                int minDay = 1;
                int maxDay = 0;
                if (selectYearIndex == 0 && selectMonthIndex == 0) {
                    //min
                    year = minYear + idYear.getCurrentItem();
                    month = 1 + idMonth.getCurrentItem();
                    maxDay = getLastDayOfMonth(year, month);
                    minDay = minYearDay == 0 ? 1 : Math.max(1, minYearDay);
                } else if (selectYearIndex == yearList.size() - 1 && selectMonthIndex == monthList.size() - 1) {
                    //max
                    year = minYear + idYear.getCurrentItem();
                    month = 1 + idMonth.getCurrentItem();
                    maxDay = maxYearDay == 0 ? getLastDayOfMonth(year, month) : Math.min(getLastDayOfMonth(year, month), maxYearDay);
                } else {
                    year = minYear + idYear.getCurrentItem();
                    month = 1 + idMonth.getCurrentItem();
                    maxDay = getLastDayOfMonth(year, month);
                }
                if (maxDay != 0) {
                    dayList = new ArrayList<>();
                    for (int i = minDay; i <= maxDay; i++) {
                        dayList.add(i + dayLabel);
                    }
                    ArrayWheelAdapter<String> dayAdapter = new ArrayWheelAdapter<String>(BottomDialog.getTopActivity(), dayList);
                    dayAdapter.setItemResource(R.layout.default_item_date);
                    dayAdapter.setItemTextResource(R.id.default_item_date_name_tv);
                    dayAdapter.setTextColor(bottomDialog.getResources().getColor(bottomDialog.isLightTheme() ? R.color.black60 : R.color.white70));
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
                    int year = minYear + selectYearIndex;
                    int month = 1 + selectMonthIndex;
                    int day = 1 + selectDayIndex;
                    onDateSelected.onSelect(year + "-" + format(month) + "-" + format(day),
                            year,
                            month,
                            day
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
                    int year = minYear + selectYearIndex;
                    int month = 1 + selectMonthIndex;
                    int day = 1 + selectDayIndex;
                    onDateSelected.onSelect(year + "-" + format(month) + "-" + format(day),
                            year,
                            month,
                            day
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
    
    public int getMaxYearMonth() {
        return maxYearMonth;
    }
    
    public DatePickerDialog setMaxYearMonth(int maxYearMonth) {
        this.maxYearMonth = maxYearMonth;
        return this;
    }
    
    public int getMinYearMonth() {
        return minYearMonth;
    }
    
    public DatePickerDialog setMinYearMonth(int minYearMonth) {
        this.minYearMonth = minYearMonth;
        return this;
    }
    
    public int getMaxYearDay() {
        return maxYearDay;
    }
    
    public DatePickerDialog setMaxYearDay(int maxYearDay) {
        this.maxYearDay = maxYearDay;
        return this;
    }
    
    public int getMinYearDay() {
        return minYearDay;
    }
    
    public DatePickerDialog setMinYearDay(int minYearDay) {
        this.minYearDay = minYearDay;
        return this;
    }
    
    public DatePickerDialog setMinTime(int minYear, int minMonth, int minDay) {
        this.minYear = minYear;
        this.minYearMonth = minMonth;
        this.minYearDay = minDay;
        return this;
    }
    
    public DatePickerDialog setMaxTime(int maxYear, int maxMonth, int maxDay) {
        this.maxYear = maxYear;
        this.maxYearMonth = maxMonth;
        this.maxYearDay = maxDay;
        return this;
    }
    
    
    public String getYearLabel() {
        return yearLabel;
    }
    
    public DatePickerDialog setYearLabel(String yearLabel) {
        this.yearLabel = yearLabel;
        return this;
    }
    
    public String getMonthLabel() {
        return monthLabel;
    }
    
    public DatePickerDialog setMonthLabel(String monthLabel) {
        this.monthLabel = monthLabel;
        return this;
    }
    
    public String getDayLabel() {
        return dayLabel;
    }
    
    public DatePickerDialog setDayLabel(String dayLabel) {
        this.dayLabel = dayLabel;
        return this;
    }
    
    public DatePickerDialog setTitle(String title) {
        this.title = title;
        if (txtDialogTitle != null) {
            txtDialogTitle.setText(title);
        }
        return this;
    }
    
    public DatePickerDialog setTitle(int resId) {
        this.title = BaseDialog.getTopActivity().getString(resId);
        if (txtDialogTitle != null) {
            txtDialogTitle.setText(title);
        }
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
    
    boolean doubleDateFormat;
    
    public boolean isDoubleDateFormat() {
        return doubleDateFormat;
    }
    
    public DatePickerDialog setDoubleDateFormat(boolean doubleDateFormat) {
        this.doubleDateFormat = doubleDateFormat;
        return this;
    }
    
    private String format(int day) {
        if (isDoubleDateFormat()) {
            if (day < 10) {
                return "0" + day;
            }
        }
        return String.valueOf(day);
    }
    
    public BottomDialog getDialog() {
        return bottomDialog;
    }
}
