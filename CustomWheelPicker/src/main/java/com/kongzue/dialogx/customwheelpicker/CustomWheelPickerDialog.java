package com.kongzue.dialogx.customwheelpicker;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.customwheelpicker.interfaces.OnCustomWheelPickerSelected;
import com.kongzue.dialogx.customwheelpicker.interfaces.OnWheelChangeListener;
import com.kongzue.dialogx.customwheelpicker.view.ArrayWheelAdapter;
import com.kongzue.dialogx.customwheelpicker.view.OnWheelChangedListener;
import com.kongzue.dialogx.customwheelpicker.view.WheelView;
import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.interfaces.BaseDialog;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialogx.style.MaterialStyle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2021/9/29 16:48
 */
public class CustomWheelPickerDialog {
    
    protected BottomDialog bottomDialog;
    protected List<String[]> wheelDataList;
    protected List<WheelView> wheelViewList;
    protected Map<Integer, Integer> defaultSelect;
    protected OnWheelChangeListener onWheelChangeListener;
    protected String title;
    
    private TextView txtDialogTitle;
    private LinearLayout boxWheel;
    
    public static CustomWheelPickerDialog build() {
        return new CustomWheelPickerDialog();
    }
    
    public void show(OnCustomWheelPickerSelected onCustomWheelPickerSelected) {
        bottomDialog = BottomDialog.show(new OnBindView<BottomDialog>(R.layout.dialogx_custom_wheel_picker) {
            
            @Override
            public void onBind(BottomDialog dialog, View v) {
                bottomDialog = dialog;
                txtDialogTitle = v.findViewById(R.id.txt_dialog_title);
                boxWheel = v.findViewById(R.id.box_wheel);
    
                txtDialogTitle.setTextColor(dialog.getResources().getColor(dialog.isLightTheme() ? R.color.black : R.color.white));
                txtDialogTitle.getPaint().setFakeBoldText(true);
                if (title != null) txtDialogTitle.setText(title);
                
                refreshUI();
                
                for (WheelView wheelView : wheelViewList) {
                    if (defaultSelect != null) {
                        Integer selectIndex = defaultSelect.get(wheelViewList.indexOf(wheelView));
                        if (selectIndex != null) {
                            wheelView.setCurrentItem(selectIndex);
                        }
                    }
                }
            }
        })
                .setCancelable(true)
                .setAllowInterceptTouch(false);
        if (DialogX.globalStyle instanceof MaterialStyle) {
            bottomDialog.setOkButton(R.string.dialogx_custom_wheel_picker_ok_button, new OnDialogButtonClickListener<BottomDialog>() {
                @Override
                public boolean onClick(BottomDialog baseDialog, View v) {
                    String text = "";
                    if (wheelViewList == null || wheelViewList.isEmpty()) {
                        onCustomWheelPickerSelected.onSelected(CustomWheelPickerDialog.this, text, new String[]{}, new int[]{});
                        return false;
                    }
                    String[] selectedTexts = new String[wheelDataList.size()];
                    int[] selectedIndex = new int[wheelDataList.size()];
                    for (int i = 0; i < wheelDataList.size(); i++) {
                        text = text.isEmpty() ? getWheelSelected(i) : text + " " + getWheelSelected(i);
                        selectedTexts[i] = getWheelSelected(i);
                        selectedIndex[i] = getWheelSelectedIndex(i);
                    }
                    onCustomWheelPickerSelected.onSelected(CustomWheelPickerDialog.this, text, selectedTexts, selectedIndex);
                    return false;
                }
            }).setCancelButton(R.string.dialogx_custom_wheel_picker_dialog_cancel, new OnDialogButtonClickListener<BottomDialog>() {
                @Override
                public boolean onClick(BottomDialog baseDialog, View v) {
                    onCustomWheelPickerSelected.onCancel();
                    return false;
                }
            });
        } else {
            bottomDialog.setCancelButton(R.string.dialogx_custom_wheel_picker_ok_button, new OnDialogButtonClickListener<BottomDialog>() {
                @Override
                public boolean onClick(BottomDialog baseDialog, View v) {
                    String text = "";
                    if (wheelViewList == null || wheelViewList.isEmpty()) {
                        onCustomWheelPickerSelected.onSelected(CustomWheelPickerDialog.this, text, new String[]{}, new int[]{});
                        return false;
                    }
                    String[] selectedTexts = new String[wheelDataList.size()];
                    int[] selectedIndex = new int[wheelDataList.size()];
                    for (int i = 0; i < wheelDataList.size(); i++) {
                        text = text.isEmpty() ? getWheelSelected(i) : text + " " + getWheelSelected(i);
                        selectedTexts[i] = getWheelSelected(i);
                        selectedIndex[i] = getWheelSelectedIndex(i);
                    }
                    onCustomWheelPickerSelected.onSelected(CustomWheelPickerDialog.this, text, selectedTexts, selectedIndex);
                    return false;
                }
            });
        }
    }
    
    public CustomWheelPickerDialog addWheel(String[] wheelList) {
        if (wheelDataList == null) {
            wheelDataList = new ArrayList<>();
        }
        wheelDataList.add(wheelList);
        refreshUI();
        return this;
    }
    
    public CustomWheelPickerDialog setWheel(int index, String[] wheelList) {
        if (wheelDataList == null) {
            wheelDataList = new ArrayList<>();
        }
        if (index >= wheelDataList.size()) {
            for (int i = wheelDataList.size(); i <= index; i++) {
                wheelDataList.add(new String[0]);
            }
        }
        wheelDataList.set(index, wheelList);
        refreshUI();
        return this;
    }
    
    public String[] getWheel(int index) {
        if (wheelDataList == null) {
            wheelDataList = new ArrayList<>();
        }
        if (index <= wheelDataList.size()) {
            for (int i = wheelDataList.size(); i <= index; i++) {
                wheelDataList.add(new String[0]);
            }
        }
        return wheelDataList.get(index);
    }
    
    public String getWheelSelected(int index) {
        if (wheelDataList == null) {
            wheelDataList = new ArrayList<>();
        }
        if (index <= wheelDataList.size()) {
            for (int i = wheelDataList.size(); i <= index; i++) {
                wheelDataList.add(new String[0]);
            }
        }
        return wheelDataList.get(index)[wheelViewList.get(index).getCurrentItem()];
    }
    
    public int getWheelSelectedIndex(int index) {
        if (wheelViewList == null) {
            wheelViewList = new ArrayList<>();
        }
        if (index >= wheelViewList.size()) return 0;
        return wheelViewList.get(index).getCurrentItem();
    }
    
    protected void refreshUI() {
        if (boxWheel != null) {
            if (wheelViewList == null) {
                wheelViewList = new ArrayList<>();
            }
            if (wheelDataList == null) {
                wheelDataList = new ArrayList<>();
            }
            if (wheelViewList.size() != wheelDataList.size()) {
                if (wheelViewList.size() < wheelDataList.size()) {
                    for (int i = wheelViewList.size(); i < wheelDataList.size(); i++) {
                        WheelView wheelView = new WheelView(boxWheel.getContext());
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        lp.weight = 1;
                        wheelViewList.add(wheelView);
                        boxWheel.addView(wheelView, lp);
                    }
                } else {
                    for (int i = wheelViewList.size() - 1; i >= wheelDataList.size(); i--) {
                        boxWheel.removeView(wheelViewList.get(i));
                    }
                }
            }
            for (WheelView wheelView : wheelViewList) {
                ArrayWheelAdapter<String> customAdapter = new ArrayWheelAdapter<String>(BottomDialog.getContext(),
                        Arrays.asList(wheelDataList.get(wheelViewList.indexOf(wheelView))));
                customAdapter.setItemResource(R.layout.default_item_custom_wheel);
                customAdapter.setItemTextResource(R.id.default_item_date_name_tv);
                customAdapter.setTextColor(bottomDialog.getResources().getColor(bottomDialog.isLightTheme() ? R.color.black60 : R.color.white70));
                wheelView.setViewAdapter(customAdapter);
                
                wheelView.setChangingListener(new OnWheelChangedListener() {
                    @Override
                    public void onChanged(WheelView wheel, int oldValue, int newValue) {
                        if (getOnWheelChangeListener() != null) {
                            getOnWheelChangeListener().onWheel(CustomWheelPickerDialog.this,
                                    wheelViewList.indexOf(wheelView),
                                    wheelDataList.get(wheelViewList.indexOf(wheelView)),
                                    newValue, wheelDataList.get(wheelViewList.indexOf(wheelView))[newValue]
                            );
                        }
                    }
                });
            }
        }
    }
    
    public OnWheelChangeListener getOnWheelChangeListener() {
        return onWheelChangeListener;
    }
    
    public CustomWheelPickerDialog setOnWheelChangeListener(OnWheelChangeListener onWheelChangeListener) {
        this.onWheelChangeListener = onWheelChangeListener;
        return this;
    }
    
    public CustomWheelPickerDialog setDefaultSelect(Map<Integer, Integer> defaultSelect) {
        this.defaultSelect = defaultSelect;
        if (wheelViewList != null) {
            Set<Integer> keySet = defaultSelect.keySet();
            for (Integer key : keySet) {
                if (key < wheelViewList.size()) {
                    int selection = defaultSelect.get(key);
                    wheelViewList.get(key).setCurrentItem(selection);
                }
            }
        }
        return this;
    }
    
    public CustomWheelPickerDialog setDefaultSelect(int wheelIndex, int selectItemIndex) {
        if (defaultSelect == null) {
            defaultSelect = new HashMap<>();
        }
        defaultSelect.put(wheelIndex, selectItemIndex);
        if (wheelViewList != null) {
            Set<Integer> keySet = defaultSelect.keySet();
            for (Integer key : keySet) {
                if (key < wheelViewList.size()) {
                    int selection = defaultSelect.get(key);
                    wheelViewList.get(key).setCurrentItem(selection);
                }
            }
        }
        return this;
    }
    
    public CustomWheelPickerDialog setTitle(String title) {
        this.title = title;
        if (txtDialogTitle != null) {
            txtDialogTitle.setText(title);
        }
        return this;
    }
    
    public CustomWheelPickerDialog setTitle(int resId) {
        this.title = BaseDialog.getContext().getString(resId);
        if (txtDialogTitle != null) {
            txtDialogTitle.setText(title);
        }
        return this;
    }
    
    public BottomDialog getDialog() {
        return bottomDialog;
    }
}
