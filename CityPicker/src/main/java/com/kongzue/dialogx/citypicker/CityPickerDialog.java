package com.kongzue.dialogx.citypicker;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.citypicker.interfaces.OnCitySelected;
import com.kongzue.dialogx.dialogs.BottomDialog;
import com.kongzue.dialogx.interfaces.BaseDialog;
import com.kongzue.dialogx.interfaces.DialogLifecycleCallback;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialogx.style.MaterialStyle;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityParseHelper;
import com.lljjcoder.style.citypickerview.widget.wheel.OnWheelChangedListener;
import com.lljjcoder.style.citypickerview.widget.wheel.WheelView;
import com.lljjcoder.style.citypickerview.widget.wheel.adapters.ArrayWheelAdapter;

import java.util.List;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2021/9/25 10:58
 */
public class CityPickerDialog {
    
    private int selectProvinceIndexCache = 0, selectCityIndexCache = 0, selectAreaIndexCache = 0;
    private CityParseHelper parseHelper;
    private int selectProvinceIndex = 0, selectCityIndex = 0, selectAreaIndex = 0;
    private BottomDialog bottomDialog;
    
    public CityPickerDialog setDefaultSelect(String province, String city, String district) {
        initParseHelper();
        List<ProvinceBean> provinceList = parseHelper.getProvinceBeenArray();
        if (provinceList != null) {
            for (ProvinceBean pb : provinceList) {
                if (TextUtils.equals(pb.getName(), province)) {
                    selectProvinceIndexCache = provinceList.indexOf(pb);
                    
                    List<CityBean> cityBeanArrayList = parseHelper.getPro_CityMap().get(pb.getName());
                    for (CityBean cb : cityBeanArrayList) {
                        if (TextUtils.equals(cb.getName(), city)) {
                            selectCityIndexCache = cityBeanArrayList.indexOf(cb);
                            
                            List<DistrictBean> areaList = (List) parseHelper.getCity_DisMap().get(pb.getName() + cb.getName());
                            for (DistrictBean db : areaList) {
                                if (TextUtils.equals(db.getName(), district)) {
                                    selectAreaIndexCache = areaList.indexOf(db);
                                }
                            }
                        }
                    }
                }
            }
        }
        return this;
    }
    
    public CityPickerDialog cleanDefaultSelect() {
        selectProvinceIndexCache = 0;
        selectCityIndexCache = 0;
        selectAreaIndexCache = 0;
        return this;
    }
    
    private void initParseHelper() {
        if (parseHelper == null) {
            parseHelper = new CityParseHelper();
            if (parseHelper.getProvinceBeanArrayList().isEmpty()) {
                parseHelper.initData(BottomDialog.getContext());
            }
        }
    }
    
    public static CityPickerDialog build() {
        return new CityPickerDialog();
    }
    
    public CityPickerDialog show(OnCitySelected onCitySelected) {
        bottomDialog = BottomDialog.show(new OnBindView<BottomDialog>(R.layout.dialogx_city_picker) {
            
            private TextView txtDialogTitle;
            private LinearLayout llTitleBackground;
            private LinearLayout llTitle;
            private WheelView idProvince;
            private WheelView idCity;
            private WheelView idDistrict;
            
            @Override
            public void onBind(BottomDialog dialog, View v) {
                bottomDialog = dialog;
                txtDialogTitle = v.findViewById(R.id.txt_dialog_title);
                llTitleBackground = v.findViewById(R.id.ll_title_background);
                llTitle = v.findViewById(R.id.ll_title);
                idProvince = v.findViewById(R.id.id_province);
                idCity = v.findViewById(R.id.id_city);
                idDistrict = v.findViewById(R.id.id_district);
                
                txtDialogTitle.setTextColor(dialog.getResources().getColor(dialog.isLightTheme() ? R.color.black : R.color.white));
                txtDialogTitle.getPaint().setFakeBoldText(true);
                
                selectProvinceIndex = selectProvinceIndexCache;
                selectCityIndex = selectCityIndexCache;
                selectAreaIndex = selectAreaIndexCache;
                
                initParseHelper();
                
                idProvince.addChangingListener(new OnWheelChangedListener() {
                    @Override
                    public void onChanged(WheelView wheelView, int oldValue, int newValue) {
                        selectProvinceIndex = newValue;
                        initCity();
                    }
                });
                idCity.addChangingListener(new OnWheelChangedListener() {
                    @Override
                    public void onChanged(WheelView wheelView, int oldValue, int newValue) {
                        selectCityIndex = newValue;
                        initArea();
                    }
                });
                idDistrict.addChangingListener(new OnWheelChangedListener() {
                    @Override
                    public void onChanged(WheelView wheelView, int oldValue, int newValue) {
                        selectAreaIndex = newValue;
                        DistrictBean mDistrictBean = (DistrictBean) ((List) parseHelper.getCity_DisMap().get(parseHelper.getProvinceBean().getName() + parseHelper.getCityBean().getName())).get(newValue);
                        parseHelper.setDistrictBean(mDistrictBean);
                    }
                });
                
                initProvince();
            }
            
            private List<ProvinceBean> provinceList;
            
            private void initProvince() {
                //加载省份
                provinceList = parseHelper.getProvinceBeenArray();
                ArrayWheelAdapter arrayWheelAdapter = new ArrayWheelAdapter(BottomDialog.getContext(), provinceList);
                arrayWheelAdapter.setItemResource(R.layout.default_item_city);
                arrayWheelAdapter.setItemTextResource(R.id.default_item_city_name_tv);
                arrayWheelAdapter.setTextColor(bottomDialog.getResources().getColor(bottomDialog.isLightTheme() ? R.color.black60 : R.color.white70));
                idProvince.setViewAdapter(arrayWheelAdapter);
                idProvince.setCurrentItem(selectProvinceIndex < provinceList.size() ? selectProvinceIndex : 0);
                
                initCity();
            }
            
            private void initCity() {
                //加载城市
                ProvinceBean selectProvince = provinceList.get(idProvince.getCurrentItem());
                parseHelper.setProvinceBean(selectProvince);
                List<CityBean> cityList = parseHelper.getPro_CityMap().get(selectProvince.getName());
                if (cityList != null) {
                    ArrayWheelAdapter cityWheelAdapter = new ArrayWheelAdapter(BottomDialog.getContext(), cityList);
                    cityWheelAdapter.setItemResource(R.layout.default_item_city);
                    cityWheelAdapter.setItemTextResource(R.id.default_item_city_name_tv);
                    cityWheelAdapter.setTextColor(bottomDialog.getResources().getColor(bottomDialog.isLightTheme() ? R.color.black60 : R.color.white70));
                    idCity.setViewAdapter(cityWheelAdapter);
                    idCity.setCurrentItem(selectCityIndex < cityList.size() ? selectCityIndex : 0);
                    initArea();
                }
            }
            
            private void initArea() {
                //加载区
                CityBean selectCity = (CityBean) ((List) parseHelper.getPro_CityMap().get(parseHelper.getProvinceBean().getName())).get(idCity.getCurrentItem());
                parseHelper.setCityBean(selectCity);
                List<DistrictBean> areaList = (List) parseHelper.getCity_DisMap().get(parseHelper.getProvinceBean().getName() + selectCity.getName());
                if (areaList == null) {
                    return;
                }
                ArrayWheelAdapter areaWheelAdapter = new ArrayWheelAdapter(BottomDialog.getContext(), areaList);
                areaWheelAdapter.setItemResource(R.layout.default_item_city);
                areaWheelAdapter.setItemTextResource(R.id.default_item_city_name_tv);
                areaWheelAdapter.setTextColor(bottomDialog.getResources().getColor(bottomDialog.isLightTheme() ? R.color.black60 : R.color.white70));
                idDistrict.setViewAdapter(areaWheelAdapter);
                idDistrict.setCurrentItem(selectAreaIndex < areaList.size() ? selectAreaIndex : 0);
                
                DistrictBean selectDistrict = null;
                if (areaList.size() > 0) {
                    selectDistrict = (DistrictBean) areaList.get(selectAreaIndex);
                }
                parseHelper.setDistrictBean(selectDistrict);
            }
        })
                .setCancelable(true)
                .setAllowInterceptTouch(false)
                .setDialogLifecycleCallback(new DialogLifecycleCallback<BottomDialog>() {
                    @Override
                    public void onDismiss(BottomDialog dialog) {
                        super.onDismiss(dialog);
                        parseHelper = null;
                    }
                });
        if (DialogX.globalStyle instanceof MaterialStyle) {
            bottomDialog.setOkButton(R.string.dialogx_city_picker_ok_button, new OnDialogButtonClickListener<BottomDialog>() {
                @Override
                public boolean onClick(BottomDialog baseDialog, View v) {
                    
                    selectProvinceIndexCache = selectProvinceIndex;
                    selectCityIndexCache = selectCityIndex;
                    selectAreaIndexCache = selectAreaIndex;
                    
                    onCitySelected.onSelect(parseHelper.getProvinceBean().getName() +
                                    parseHelper.getCityBean().getName() +
                                    parseHelper.getDistrictBean().getName(),
                            parseHelper.getProvinceBean().getName(),
                            parseHelper.getCityBean().getName(),
                            parseHelper.getDistrictBean().getName()
                    );
                    return false;
                }
            }).setCancelButton(R.string.dialogx_city_picker_dialog_cancel, new OnDialogButtonClickListener<BottomDialog>() {
                @Override
                public boolean onClick(BottomDialog baseDialog, View v) {
                    onCitySelected.onCancel();
                    return false;
                }
            });
        } else {
            bottomDialog.setCancelButton(R.string.dialogx_city_picker_ok_button, new OnDialogButtonClickListener<BottomDialog>() {
                @Override
                public boolean onClick(BottomDialog baseDialog, View v) {
                    
                    selectProvinceIndexCache = selectProvinceIndex;
                    selectCityIndexCache = selectCityIndex;
                    selectAreaIndexCache = selectAreaIndex;
                    
                    onCitySelected.onSelect(parseHelper.getProvinceBean().getName() +
                                    parseHelper.getCityBean().getName() +
                                    parseHelper.getDistrictBean().getName(),
                            parseHelper.getProvinceBean().getName(),
                            parseHelper.getCityBean().getName(),
                            parseHelper.getDistrictBean().getName()
                    );
                    return false;
                }
            });
        }
        return this;
    }
    
    public BottomDialog getDialog() {
        return bottomDialog;
    }
}
