package com.lljjcoder.style.cityjd;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kongzue.dialogx.citypicker.R;
import com.lljjcoder.bean.ProvinceBean;

import java.util.List;

import static com.lljjcoder.style.cityjd.JDConst.INDEX_INVALID;

/**
 * 作者：liji on 2018/1/29 17:01
 * 邮箱：lijiwork@sina.com
 * QQ ：275137657
 */

public class ProvinceAdapter extends BaseAdapter {

    Context context;

    List<ProvinceBean> mProList;

    private int provinceIndex = INDEX_INVALID;

    public ProvinceAdapter(Context context, List<ProvinceBean> mProList) {
        this.context = context;
        this.mProList = mProList;
    }

    public void updateSelectedPosition(int index) {
        this.provinceIndex = index;
    }

    public int getSelectedPosition() {
        return this.provinceIndex;
    }

    @Override
    public int getCount() {
        return mProList.size();
    }

    @Override
    public ProvinceBean getItem(int position) {
        return mProList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return Long.parseLong(mProList.get(position).getId());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pop_jdcitypicker_item, parent, false);

            holder = new Holder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.selectImg = (ImageView) convertView.findViewById(R.id.selectImg);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        ProvinceBean item = getItem(position);
        holder.name.setText(item.getName());

        boolean checked = provinceIndex != INDEX_INVALID && mProList.get(provinceIndex).getName().equals(item.getName());
        holder.name.setEnabled(!checked);
        holder.selectImg.setVisibility(checked ? View.VISIBLE : View.GONE);


        return convertView;
    }


    class Holder {
        TextView name;
        ImageView selectImg;
    }
}
