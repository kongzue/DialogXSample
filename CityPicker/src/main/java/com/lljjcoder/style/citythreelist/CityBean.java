package com.lljjcoder.style.citythreelist;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者：liji on 2018/3/20 10:57
 * 邮箱：lijiwork@sina.com
 * QQ ：275137657
 */

public class CityBean implements Parcelable {
    private String id; /*110101*/
    
    private String name; /*东城区*/
    
    public String getId() {
        return id == null ? "" : id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name == null ? "" : name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
    }
    
    public CityBean() {
    }
    
    protected CityBean(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
    }
    
    public static final Parcelable.Creator<CityBean> CREATOR = new Parcelable.Creator<CityBean>() {
        @Override
        public CityBean createFromParcel(Parcel source) {
            return new CityBean(source);
        }
        
        @Override
        public CityBean[] newArray(int size) {
            return new CityBean[size];
        }
    };
}
