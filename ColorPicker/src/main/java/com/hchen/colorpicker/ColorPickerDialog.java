package com.hchen.colorpicker;

import android.view.View;

import androidx.annotation.ColorInt;

import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.interfaces.BaseDialog;
import com.kongzue.dialogx.interfaces.OnBindView;

/**
 * @author: HChenX
 * @github: https://github.com/HChenX/
 * @createTime: 2025/08/22 15:37
 */
public class ColorPickerDialog implements OnColorValueChangedListener {
    private CharSequence title;
    private CharSequence message;
    @ColorInt
    private int value;
    private boolean isHapticFeedbackEnabled;
    private OnColorValueChangedListener listener;
    private ColorPickerView colorPickerView;
    private BottomDialog bottomDialog;

    public ColorPickerDialog() {
        colorPickerView = new ColorPickerView(BaseDialog.getTopActivity());
    }

    public static ColorPickerDialog build() {
        return new ColorPickerDialog();
    }

    public ColorPickerDialog setValue(int value) {
        this.value = value;
        return this;
    }

    public ColorPickerDialog setHapticFeedbackEnabled(boolean hapticFeedbackEnabled) {
        isHapticFeedbackEnabled = hapticFeedbackEnabled;
        return this;
    }

    public ColorPickerDialog setListener(OnColorValueChangedListener listener) {
        this.listener = listener;
        return this;
    }

    public int getValue() {
        return value;
    }

    public BottomDialog getDialog() {
        return bottomDialog;
    }

    public boolean isHapticFeedbackEnabled() {
        return isHapticFeedbackEnabled;
    }

    public void show() {
        if (bottomDialog != null && bottomDialog.isShow()) return;
        bottomDialog = BottomDialog.show(title, message, new OnBindView<BottomDialog>(colorPickerView) {
            @Override
            public void onBind(BottomDialog bottomDialog, View view) {
                colorPickerView.setValue(value);
                colorPickerView.setHapticFeedbackEnabled(isHapticFeedbackEnabled);
                colorPickerView.setListener(ColorPickerDialog.this);
            }
        }).setAllowInterceptTouch(false);
    }

    @Override
    public void onColorValueChanged(ColorPickerType type, int value) {
        if (type == ColorPickerType.FINAL_COLOR || type == ColorPickerType.INSTANT_COLOR) {
            this.value = value;
            if (listener != null) listener.onColorValueChanged(type, value);
        }
    }
}
