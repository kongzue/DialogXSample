package com.kongzue.dialogxsimpledemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kongzue.dialogx.citypicker.CityPickerDialog;
import com.kongzue.dialogx.citypicker.interfaces.OnCitySelected;

public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    
    public void onCityPicker(View view) {
        CityPickerDialog.build()
                .setDefaultSelect("陕西省","西安市","未央区")
                .show(new OnCitySelected() {
            @Override
            public void onSuccess(String text, String province, String city, String district) {
                ((Button) view).setText(text);
            }
        });
    }
}