package com.kongzue.filedialog;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.kongzue.dialogx.dialogs.FullScreenDialog;
import com.kongzue.dialogx.interfaces.BaseDialog;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.filedialog.adapter.FileAdapter;
import com.kongzue.filedialog.interfaces.FileSelectCallBack;

import java.io.File;
import java.util.ArrayList;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2022/3/3 13:02
 */
public class FileDialog {
    
    public static FileDialog build() {
        return new FileDialog();
    }
    
    public static int REQUEST_PERMISSION_CODE = 9103;
    
    String[] mimeTypes;
    String[] suffixArray;
    
    enum SelectType {
        FILE,
        FOLDER
    }
    
    SelectType selectType = SelectType.FILE;
    FullScreenDialog dialog;
    
    public void selectFileByMime(String mimeType, FileSelectCallBack fileSelectCallBack) {
        selectFileByMime(new String[]{mimeType}, fileSelectCallBack);
    }
    
    public void selectFileBySuffix(String suffix, FileSelectCallBack fileSelectCallBack) {
        selectFileBySuffix(new String[]{suffix}, fileSelectCallBack);
    }
    
    public void selectFile(FileSelectCallBack fileSelectCallBack) {
        selectType = SelectType.FILE;
        readyShowDialog();
    }
    
    public void selectFileByMime(String[] mimeTypes, FileSelectCallBack fileSelectCallBack) {
        selectType = SelectType.FILE;
        this.mimeTypes = mimeTypes;
        readyShowDialog();
    }
    
    public void selectFileBySuffix(String[] suffixArray, FileSelectCallBack fileSelectCallBack) {
        selectType = SelectType.FILE;
        this.suffixArray = suffixArray;
        readyShowDialog();
    }
    
    public void selectFolder(FileSelectCallBack fileSelectCallBack) {
        selectType = SelectType.FOLDER;
        readyShowDialog();
    }
    
    private void readyShowDialog() {
        Context context = BaseDialog.getContext();
        if (!(context instanceof Activity)) {
            errorLog("请先完成 DialogX 组件的初始化，详情：https://github.com/kongzue/DialogX");
            return;
        }
        requestPermissions(context);
    }
    
    /**
     * 建议自行处理好权限问题，不要依赖 FileDialog 申请，原因是无法监听 onRequestPermissionsResult 的回调.
     * 若实在懒得自己处理，请重写 activity 的 onRequestPermissionsResult(...) 方法，
     * 并调用 FileDialog 实例的 onRequestPermissionsResult(...) 方法。
     *
     * @param context activity 上下文
     */
    private void requestPermissions(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // 先判断有没有权限
            if (Environment.isExternalStorageManager()) {
                createDialog();
            } else {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + context.getPackageName()));
                ((Activity) context).startActivityForResult(intent, REQUEST_PERMISSION_CODE);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                createDialog();
            } else {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
            }
        } else {
            createDialog();
        }
    }
    
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CODE) {
            readyShowDialog();
        }
    }
    
    String path;
    FileAdapter fileAdapter;
    
    private void createDialog() {
        dialog = FullScreenDialog.build();
        dialog.setCustomView(new OnBindView<FullScreenDialog>(R.layout.layout_dialogx_file_select) {
            
            private TextView btnCancel;
            private TextView txtDialogTitle;
            private TextView btnSelect;
            private ListView listFile;
            
            @Override
            public void onBind(FullScreenDialog dialog, View v) {
                FileDialog.this.dialog = dialog;
                
                btnCancel = v.findViewById(R.id.btn_cancel);
                txtDialogTitle = v.findViewById(R.id.txt_dialog_title);
                btnSelect = v.findViewById(R.id.btn_select);
                listFile = v.findViewById(R.id.list_file);
                
                loadFileList();
            }
            
            private void loadFileList() {
                path = Environment.getExternalStorageDirectory().getPath();
                fileAdapter = new FileAdapter((Activity) BaseDialog.getContext(), new ArrayList<String>());
                listFile.setAdapter(fileAdapter);
                listFile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String itemPath =  path + fileAdapter.getItem(position);
                        Log.d(">>>", "onItemClick: "+itemPath);
                    }
                });
                refreshFileList();
            }
            
            private void refreshFileList() {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            ArrayList<String> allFileArray = new ArrayList<String>();
                            ArrayList<String> folderArray = new ArrayList<String>();
                            ArrayList<String> fileArray = new ArrayList<String>();
                            
                            File file = new File(path);
                            File[] listFiles = file.listFiles();
                            for (File f : listFiles) {
                                if (f.isDirectory()) {
                                    folderArray.add("/" + f.getName());
                                } else {
                                    fileArray.add(f.getName());
                                }
                            }
                            allFileArray.addAll(folderArray);
                            allFileArray.addAll(fileArray);
                            ((Activity) BaseDialog.getContext()).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    fileAdapter.setFileList(allFileArray);
                                    fileAdapter.notifyDataSetChanged();
                                }
                            });
                        } catch (Exception e) {
                        
                        }
                    }
                }
                        .start();
            }
        });
        dialog.show();
    }
    
    public FullScreenDialog getDialog() {
        return dialog;
    }
    
    public FileDialog setDialog(FullScreenDialog dialog) {
        this.dialog = dialog;
        return this;
    }
    
    private void errorLog(String msg) {
        Log.e(">>>", "FileDialog: " + msg);
    }
}
