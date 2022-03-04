package com.kongzue.filedialog.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kongzue.filedialog.FileDialog;
import com.kongzue.filedialog.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2022/3/3 14:48
 */
public class FileAdapter extends BaseAdapter {
    
    private ArrayList<String> fileList;
    private LayoutInflater layoutInflater;
    private Activity activity;
    private FileDialog dialog;
    
    public FileAdapter(FileDialog dialog, Activity activity, ArrayList<String> fileList) {
        super();
        layoutInflater = activity.getLayoutInflater();
        this.dialog = dialog;
        this.activity = activity;
        this.fileList = fileList;
    }
    
    @Override
    public int getCount() {
        return fileList.size();
    }
    
    public void setFileList(ArrayList<String> fileList) {
        this.fileList = fileList;
    }
    
    public ArrayList<String> getFileList() {
        return this.fileList;
    }
    
    @Override
    public Object getItem(int position) {
        return fileList.get(position);
    }
    
    @Override
    public long getItemId(int position) {
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_dialogx_file_list, null);
        }
        if (convertView.getTag() instanceof ViewHolder) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        String fileName = fileList.get(position);
        boolean isFolder = fileName.startsWith("/");
        String itemPath = dialog.getPath() + (isFolder ? "" : "/") + fileName;
        
        viewHolder.txtFileName.getPaint().setFakeBoldText(false);
        if ("...".equals(fileName)) {
            viewHolder.txtFileName.setText("...");
            viewHolder.imgIcon.setImageResource(R.drawable.ic_dialogx_filedialog_back);
            viewHolder.btnHaveChild.setVisibility(View.GONE);
        } else {
            viewHolder.txtFileName.setText(fileName.substring(1));
            if (fileName.startsWith("/")) {
                viewHolder.imgIcon.setImageResource(R.drawable.ic_dialogx_filedialog_folder);
                viewHolder.btnHaveChild.setVisibility(View.VISIBLE);
            } else {
                if (dialog.getSelectPathList() != null && dialog.getSelectPathList().contains(itemPath)) {
                    viewHolder.imgIcon.setImageResource(R.drawable.ic_dialogx_filedialog_select);
                    viewHolder.txtFileName.getPaint().setFakeBoldText(true);
                } else {
                    viewHolder.imgIcon.setImageResource(R.drawable.ic_dialogx_filedialog_file);
                }
                viewHolder.btnHaveChild.setVisibility(View.GONE);
            }
        }
        
        if ((dialog.getMimeTypes() != null && dialog.getMimeTypes().length != 0) ||
                dialog.getSuffixArray() != null && dialog.getSuffixArray().length != 0) {
            if ("...".equals(fileName) || isFolder) {
                convertView.setAlpha(1f);
            } else {
                if (isSelectMimeType(itemPath) || isSelectSuffix(itemPath)) {
                    convertView.setAlpha(1f);
                } else {
                    convertView.setAlpha(0.3f);
                }
            }
        }
        
        return convertView;
    }
    
    private boolean isSelectSuffix(String itemPath) {
        if (dialog.getSuffixArray() != null && dialog.getSuffixArray().length != 0) {
            for (String suffix : dialog.getSuffixArray()) {
                if (itemPath.toLowerCase().endsWith(suffix.toLowerCase())) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean isSelectMimeType(String itemPath) {
        if (dialog.getMimeTypes() != null && dialog.getMimeTypes().length != 0) {
            for (String mime : dialog.getMimeTypes()) {
                if (mime.toUpperCase().equals(getMimeType(new File(itemPath)).toUpperCase())) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private String getMimeType(File file) {
        String suffix = getSuffix(file);
        if (suffix == null) {
            return "file/*";
        }
        String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(suffix);
        if (type != null || !type.isEmpty()) {
            return type;
        }
        return "file/*";
    }
    
    private String getSuffix(File file) {
        if (file == null || !file.exists() || file.isDirectory()) {
            return null;
        }
        String fileName = file.getName();
        if (fileName.equals("") || fileName.endsWith(".")) {
            return null;
        }
        int index = fileName.lastIndexOf(".");
        if (index != -1) {
            return fileName.substring(index + 1).toLowerCase(Locale.US);
        } else {
            return null;
        }
    }
    
    class ViewHolder {
        
        ImageView imgIcon;
        TextView txtFileName;
        ImageView btnHaveChild;
        
        public ViewHolder(View convertView) {
            imgIcon = convertView.findViewById(R.id.img_icon);
            txtFileName = convertView.findViewById(R.id.txt_fileName);
            btnHaveChild = convertView.findViewById(R.id.btn_have_child);
        }
    }
}
