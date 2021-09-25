package com.kongzue.dialogxsampledemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.kongzue.dialogx.citypicker.CityPickerDialog;
import com.kongzue.dialogx.citypicker.interfaces.OnCitySelected;
import com.kongzue.dialogx.datepicker.DatePickerDialog;
import com.kongzue.dialogx.datepicker.interfaces.OnDateSelected;

public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    
    String defaultProvince = "陕西省", defaultCity = "西安市", defaultDistrict = "未央区";
    
    public void onCityPicker(View view) {
        CityPickerDialog.build()
                .setDefaultSelect(defaultProvince, defaultCity, defaultDistrict)
                .show(new OnCitySelected() {
                    @Override
                    public void onSelect(String text, String province, String city, String district) {
                        ((Button) view).setText(text);
                        
                        defaultProvince = province;
                        defaultCity = city;
                        defaultDistrict = district;
                    }
                });
    }
    
    int defaultYear = 2021, defaultMonth = 9, defaultDay = 25;
    
    public void onDatePicker(View view) {
        DatePickerDialog.build()
                .setDefaultSelect(defaultYear, defaultMonth, defaultDay)
                .show(new OnDateSelected() {
                    @Override
                    public void onSelect(String text, int year, int month, int day) {
                        ((Button) view).setText(text);
                        
                        defaultYear = year;
                        defaultMonth = month;
                        defaultDay = day;
                    }
                });
    }
    
    public void onShare(View view) {
    
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_github:
                try {
                    Uri uri = Uri.parse("https://github.com/kongzue/DialogXSimple");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } catch (Exception e) {
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}