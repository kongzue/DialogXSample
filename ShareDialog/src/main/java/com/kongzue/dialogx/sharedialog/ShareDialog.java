package com.kongzue.dialogx.sharedialog;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.interfaces.BaseDialog;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.sharedialog.bean.ShareData;
import com.kongzue.dialogx.sharedialog.interfaces.OnShareClick;
import com.kongzue.dialogx.sharedialog.view.TableLayout;

import java.util.List;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2021/9/26 11:28
 */
public class ShareDialog {
    
    protected BottomDialog bottomDialog;
    protected List<ShareData> shareDataList;
    protected String title;
    
    public static ShareDialog build() {
        return new ShareDialog();
    }
    
    private TextView txtDialogTitle;
    
    public ShareDialog show(OnShareClick onShareClick) {
        bottomDialog = BottomDialog.show(new OnBindView<BottomDialog>(R.layout.dialogx_share) {
            
            private TableLayout boxTable;
            
            @Override
            public void onBind(BottomDialog dialog, View v) {
                txtDialogTitle = v.findViewById(R.id.txt_dialog_title);
                boxTable = v.findViewById(R.id.box_table);
                
                txtDialogTitle.setTextColor(dialog.getResources().getColor(dialog.isLightTheme() ? com.kongzue.dialogx.R.color.black : com.kongzue.dialogx.R.color.white));
                txtDialogTitle.getPaint().setFakeBoldText(true);
                if (title != null) txtDialogTitle.setText(title);
                
                if (shareDataList != null) {
                    for (ShareData shareData : shareDataList) {
                        View shareButton = dialog.createView(R.layout.item_dialogx_share_button);
                        
                        ImageView imgIcon = shareButton.findViewById(R.id.img_icon);
                        TextView txtLabel = shareButton.findViewById(R.id.txt_label);
    
                        txtLabel.setTextColor(dialog.getResources().getColor(dialog.isLightTheme() ? com.kongzue.dialogx.R.color.black : com.kongzue.dialogx.R.color.white));
                        
                        imgIcon.setImageDrawable(shareData.getIcon());
                        txtLabel.setText(shareData.getLabel());
                        
                        boxTable.addView(shareButton);
                        
                        shareButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (onShareClick != null) {
                                    if (!onShareClick.onClick(v.getContext(), shareData, shareButton, shareDataList.indexOf(shareData))) {
                                        dialog.dismiss();
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
        return this;
    }
    
    public BottomDialog getBottomDialog() {
        return bottomDialog;
    }
    
    public List<ShareData> getShareDataList() {
        return shareDataList;
    }
    
    public ShareDialog setShareDataList(List<ShareData> shareDataList) {
        this.shareDataList = shareDataList;
        return this;
    }
    
    public ShareDialog setTitle(String title) {
        this.title = title;
        if (txtDialogTitle != null) {
            txtDialogTitle.setText(title);
        }
        return this;
    }
    
    public ShareDialog setTitle(int resId) {
        this.title = BaseDialog.getTopActivity().getString(resId);
        if (txtDialogTitle != null) {
            txtDialogTitle.setText(title);
        }
        return this;
    }
    
    public BottomDialog getDialog() {
        return bottomDialog;
    }
}
