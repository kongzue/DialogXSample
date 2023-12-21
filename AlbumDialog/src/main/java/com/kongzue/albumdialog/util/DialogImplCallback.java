package com.kongzue.albumdialog.util;

import com.kongzue.dialogx.interfaces.BaseDialog;

public interface DialogImplCallback<D extends BaseDialog> {
    void onDialogCreated(D dialog);
}
