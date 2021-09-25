package com.kongzue.dialogxsimpledemo;

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

public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    
    public void onCityPicker(View view) {
        CityPickerDialog.build()
                .setDefaultSelect("陕西省", "西安市", "未央区")
                .show(new OnCitySelected() {
                    @Override
                    public void onSuccess(String text, String province, String city, String district) {
                        ((Button) view).setText(text);
                    }
                });
    }
    
    public void onDatePicker(View view) {
    
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
        switch (item.getItemId()){
            case R.id.action_github:
                try {
                    Uri uri = Uri.parse("https://github.com/kongzue/DialogXSimple");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }catch (Exception e){}
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}