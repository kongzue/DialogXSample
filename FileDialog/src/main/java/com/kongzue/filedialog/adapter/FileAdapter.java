package com.kongzue.filedialog.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kongzue.filedialog.R;

import java.util.ArrayList;

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
    
    public FileAdapter(Activity activity, ArrayList<String> fileList) {
        super();
        layoutInflater = activity.getLayoutInflater();
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
        viewHolder.txtFileName.setText(fileList.get(position).substring(1));
        if (fileList.get(position).startsWith("/")){
            viewHolder.imgIcon.setImageResource(R.drawable.ic_dialogx_filedialog_folder);
            viewHolder.btnHaveChild.setVisibility(View.VISIBLE);
        }else{
            viewHolder.imgIcon.setImageResource(R.drawable.ic_dialogx_filedialog_file);
            viewHolder.btnHaveChild.setVisibility(View.GONE);
        }
        return convertView;
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
