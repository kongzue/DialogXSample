package com.kongzue.dialogx.replydialog;

import static com.kongzue.dialogx.interfaces.BaseDialog.isNull;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.interfaces.BaseDialog;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.replydialog.interfaces.OnReplyButtonClickListener;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2021/10/4 14:38
 */
public class ReplyDialog {
    
    protected BottomDialog bottomDialog;
    protected String title;
    protected String content;
    protected String contentHint;
    protected String replyButtonText;
    
    public static ReplyDialog build() {
        return new ReplyDialog();
    }
    
    public void show(OnReplyButtonClickListener onReplyButtonClickListener) {
        bottomDialog = BottomDialog.show(new OnBindView<BottomDialog>(
                isLightTheme() ? R.layout.layout_custom_reply : R.layout.layout_custom_reply_dark
        ) {
            private TextView txtDialogTitle;
            private TextView btnReplyCommit;
            private EditText editReplyCommit;
            
            @Override
            public void onBind(BottomDialog dialog, View v) {
                txtDialogTitle = v.findViewById(R.id.txt_dialog_title);
                btnReplyCommit = v.findViewById(R.id.btn_reply_commit);
                editReplyCommit = v.findViewById(R.id.edit_reply_commit);
                
                if (!isNull(title)) {
                    txtDialogTitle.setText(title);
                    txtDialogTitle.setVisibility(View.VISIBLE);
                } else {
                    txtDialogTitle.setVisibility(View.GONE);
                }
                if (!isNull(replyButtonText)) {
                    btnReplyCommit.setText(replyButtonText);
                    btnReplyCommit.setVisibility(View.VISIBLE);
                } else {
                    btnReplyCommit.setVisibility(View.GONE);
                }
                if (!isNull(content)) {
                    editReplyCommit.setText(content);
                }
                if (!isNull(contentHint)) {
                    editReplyCommit.setHint(contentHint);
                }
                
                btnReplyCommit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onReplyButtonClickListener.onClick(v,editReplyCommit.getText().toString());
                        dialog.dismiss();
                    }
                });
                
                bottomDialog = dialog;
                editReplyCommit.post(new Runnable() {
                    @Override
                    public void run() {
                        showIME(editReplyCommit);
                    }
                });
            }
        })
                .setCancelable(true)
                .setAllowInterceptTouch(false);
    }
    
    public void showIME(EditText editText) {
        if (editText == null) {
            return;
        }
        editText.requestFocus();
        editText.setFocusableInTouchMode(true);
        InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }
    
    protected boolean isLightTheme() {
        if (DialogX.globalTheme == DialogX.THEME.AUTO) {
            if (BaseDialog.getTopActivity() == null) return DialogX.globalTheme == DialogX.THEME.LIGHT;
            return (BaseDialog.getTopActivity().getApplicationContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_NO;
        }
        return DialogX.globalTheme == DialogX.THEME.LIGHT;
    }
    
    public String getTitle() {
        return title;
    }
    
    public ReplyDialog setTitle(String title) {
        this.title = title;
        return this;
    }
    
    public String getContent() {
        return content;
    }
    
    public ReplyDialog setContent(String content) {
        this.content = content;
        return this;
    }
    
    public String getReplyButtonText() {
        return replyButtonText;
    }
    
    public ReplyDialog setReplyButtonText(String replyButtonText) {
        this.replyButtonText = replyButtonText;
        return this;
    }
    
    public BottomDialog getBottomDialog() {
        return bottomDialog;
    }
    
    public String getContentHint() {
        return contentHint;
    }
    
    public ReplyDialog setContentHint(String contentHint) {
        this.contentHint = contentHint;
        return this;
    }
    
    public BottomDialog getDialog() {
        return bottomDialog;
    }
}
