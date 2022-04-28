package com.kongzue.dialogxsampledemo.function.searchdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kongzue.dialogxsampledemo.R;

import java.util.List;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2022/4/28 20:13
 */
public class SearchAdapter extends BaseAdapter {
    
    List<FunctionBean> functionBeanList;
    private LayoutInflater inflater;
    private Context context;
    
    public SearchAdapter(Context context, List<FunctionBean> functionBeanList) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.functionBeanList = functionBeanList;
    }
    
    @Override
    public int getCount() {
        return functionBeanList.size();
    }
    
    @Override
    public Object getItem(int position) {
        return functionBeanList.get(position);
    }
    
    @Override
    public long getItemId(int position) {
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FunctionBean functionBean = functionBeanList.get(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_function_search_result_item, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.imgIcon.setImageResource(functionBean.getIconRedId());
        viewHolder.txtName.setText(functionBean.getName());
        return convertView;
    }
    
    class ViewHolder {
        private ImageView imgIcon;
        private TextView txtName;
        
        public ViewHolder(View v) {
            imgIcon = v.findViewById(R.id.img_icon);
            txtName = v.findViewById(R.id.txt_name);
        }
    }
}
