package com.lljjcoder.citywheel;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lljjcoder.Constant;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.utils.utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 省市区解析辅助类
 * 作者：liji on 2017/11/4 10:09
 * 邮箱：lijiwork@sina.com
 * QQ ：275137657
 */

public class CityParseHelper {

    /**
     * 省份数据
     */
    private ArrayList<ProvinceBean> mProvinceBeanArrayList = new ArrayList<>();

    /**
     * 城市数据
     */
    private ArrayList<ArrayList<CityBean>> mCityBeanArrayList;

    /**
     * 地区数据
     */
    private ArrayList<ArrayList<ArrayList<DistrictBean>>> mDistrictBeanArrayList;

    private List<ProvinceBean> mProvinceBeenArray;

    private ProvinceBean mProvinceBean;

    private CityBean mCityBean;

    private DistrictBean mDistrictBean;

    //    private CityConfig config;

    /**
     * key - 省 value - 市
     */
    private Map<String, List<CityBean>> mPro_CityMap = new HashMap<String, List<CityBean>>();

    /**
     * key - 市 values - 区
     */
    private Map<String, List<DistrictBean>> mCity_DisMap = new HashMap<String, List<DistrictBean>>();

    /**
     * key - 区 values - 邮编
     */
    private Map<String, DistrictBean> mDisMap = new HashMap<String, DistrictBean>();

    public ArrayList<ProvinceBean> getProvinceBeanArrayList() {
        return mProvinceBeanArrayList;
    }

    public void setProvinceBeanArrayList(ArrayList<ProvinceBean> provinceBeanArrayList) {
        mProvinceBeanArrayList = provinceBeanArrayList;
    }

    public ArrayList<ArrayList<CityBean>> getCityBeanArrayList() {
        return mCityBeanArrayList;
    }

    public void setCityBeanArrayList(ArrayList<ArrayList<CityBean>> cityBeanArrayList) {
        mCityBeanArrayList = cityBeanArrayList;
    }

    public ArrayList<ArrayList<ArrayList<DistrictBean>>> getDistrictBeanArrayList() {
        return mDistrictBeanArrayList;
    }

    public void setDistrictBeanArrayList(ArrayList<ArrayList<ArrayList<DistrictBean>>> districtBeanArrayList) {
        mDistrictBeanArrayList = districtBeanArrayList;
    }

    public List<ProvinceBean> getProvinceBeenArray() {
        return mProvinceBeenArray;
    }

    public void setProvinceBeenArray(List<ProvinceBean> provinceBeenArray) {
        mProvinceBeenArray = provinceBeenArray;
    }

    public ProvinceBean getProvinceBean() {
        return mProvinceBean;
    }

    public void setProvinceBean(ProvinceBean provinceBean) {
        mProvinceBean = provinceBean;
    }

    public CityBean getCityBean() {
        return mCityBean;
    }

    public void setCityBean(CityBean cityBean) {
        mCityBean = cityBean;
    }

    public DistrictBean getDistrictBean() {
        return mDistrictBean;
    }

    public void setDistrictBean(DistrictBean districtBean) {
        mDistrictBean = districtBean;
    }

    public Map<String, List<CityBean>> getPro_CityMap() {
        return mPro_CityMap;
    }

    public void setPro_CityMap(Map<String, List<CityBean>> pro_CityMap) {
        mPro_CityMap = pro_CityMap;
    }

    public Map<String, List<DistrictBean>> getCity_DisMap() {
        return mCity_DisMap;
    }

    public void setCity_DisMap(Map<String, List<DistrictBean>> city_DisMap) {
        mCity_DisMap = city_DisMap;
    }

    public Map<String, DistrictBean> getDisMap() {
        return mDisMap;
    }

    public void setDisMap(Map<String, DistrictBean> disMap) {
        mDisMap = disMap;
    }

    public CityParseHelper() {

    }

    /**
     * 初始化数据，解析json数据
     */
    public void initData(Context context) {

        String cityJson = utils.getJson(context, Constant.CITY_DATA);
        Type type = new TypeToken<ArrayList<ProvinceBean>>() {
        }.getType();

        mProvinceBeanArrayList = new Gson().fromJson(cityJson, type);

        if (mProvinceBeanArrayList == null || mProvinceBeanArrayList.isEmpty()) {
            return;
        }

        mCityBeanArrayList = new ArrayList<>(mProvinceBeanArrayList.size());
        mDistrictBeanArrayList = new ArrayList<>(mProvinceBeanArrayList.size());

        //*/ 初始化默认选中的省、市、区，默认选中第一个省份的第一个市区中的第一个区县
        if (mProvinceBeanArrayList != null && !mProvinceBeanArrayList.isEmpty()) {
            mProvinceBean = mProvinceBeanArrayList.get(0);
            List<CityBean> cityList = mProvinceBean.getCityList();
            if (cityList != null && !cityList.isEmpty() && cityList.size() > 0) {
                mCityBean = cityList.get(0);
                List<DistrictBean> districtList = mCityBean.getCityList();
                if (districtList != null && !districtList.isEmpty() && districtList.size() > 0) {
                    mDistrictBean = districtList.get(0);
                }
            }
        }

        //省份数据
        mProvinceBeenArray = new ArrayList<ProvinceBean>();

        for (int p = 0; p < mProvinceBeanArrayList.size(); p++) {

            //遍历每个省份
            ProvinceBean itemProvince = mProvinceBeanArrayList.get(p);

            //每个省份对应下面的市
            ArrayList<CityBean> cityList = itemProvince.getCityList();

            //当前省份下面的所有城市

//            List<CityBean> cityNames = new ArrayList<>();

            //遍历当前省份下面城市的所有数据
            for (int j = 0; j < cityList.size(); j++) {
//                cityNames[j] = cityList.get(j);

                //当前省份下面每个城市下面再次对应的区或者县
                List<DistrictBean> districtList = cityList.get(j).getCityList();
                if (districtList == null) {
                    break;
                }
//                DistrictBean[] distrinctArray = new DistrictBean[districtList.size()];

                for (int k = 0; k < districtList.size(); k++) {

                    // 遍历市下面所有区/县的数据
                    DistrictBean districtModel = districtList.get(k);

                    //存放 省市区-区 数据
                    mDisMap.put(itemProvince.getName() + cityList.get(j).getName() + districtList.get(k).getName(),
                            districtModel);

//                    districtList.add(districtModel);

                }
                // 市-区/县的数据，保存到mDistrictDatasMap
                mCity_DisMap.put(itemProvince.getName() + cityList.get(j).getName(), districtList);

            }

            // 省-市的数据，保存到mCitisDatasMap
            mPro_CityMap.put(itemProvince.getName(), cityList);

            mCityBeanArrayList.add(cityList);

            //只有显示三级联动，才会执行
            ArrayList<ArrayList<DistrictBean>> array2DistrictLists = new ArrayList<>(cityList.size());

            for (int c = 0; c < cityList.size(); c++) {
                CityBean cityBean = cityList.get(c);
                array2DistrictLists.add(cityBean.getCityList());
            }
            mDistrictBeanArrayList.add(array2DistrictLists);

            //            }
            mProvinceBeenArray.add(p, itemProvince);
            //赋值所有省份的名称
//            mProvinceBeenArray[p] = ;

        }

    }

}
