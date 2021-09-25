package com.lljjcoder.style.citylist.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：liji on 2017/5/19 17:07
 * 邮箱：lijiwork@sina.com
 * QQ ：275137657
 */

public class CityInfoBean implements Parcelable {

    
    private String id; /*110101*/
    
    private String name; /*东城区*/
    
    private ArrayList<CityInfoBean> cityList;
    
    public ArrayList<CityInfoBean> getCityList() {
        return cityList;
    }
    
    public void setCityList(ArrayList<CityInfoBean> cityList) {
        this.cityList = cityList;
    }
    
    public CityInfoBean() {
    }
    
    public static CityInfoBean findCity(List<CityInfoBean> list, String cityName) {
        try {
            for (int i = 0; i < list.size(); i++) {
                CityInfoBean city = list.get(i);
                if (cityName.equals(city.getName()) /*|| cityName.contains(city.getName())
                        || city.getName().contains(cityName)*/) {
                    return city;
                }
            }
        }
        catch (Exception e) {
            return null;
        }
        return null;
    }


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
        dest.writeTypedList(this.cityList);
    }

    protected CityInfoBean(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.cityList = in.createTypedArrayList(CityInfoBean.CREATOR);
    }

    public static final Creator<CityInfoBean> CREATOR = new Creator<CityInfoBean>() {
        @Override
        public CityInfoBean createFromParcel(Parcel source) {
            return new CityInfoBean(source);
        }

        @Override
        public CityInfoBean[] newArray(int size) {
            return new CityInfoBean[size];
        }
    };
}
