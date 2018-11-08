package com.androidTest.controls;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;

import com.androidTest.R;

public class DatePickerActivity extends Activity {
    private Calendar calendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datepicker);

        // 获取当前日期
        calendar = Calendar.getInstance();

        DatePicker datePicker = findViewById(R.id.datePicker);

        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Toast.makeText(DatePickerActivity.this,year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日",Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 显示DatePickerDialog
    public void showDataPickerDialog(View view){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                DatePickerDialog.THEME_HOLO_DARK,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Toast.makeText(DatePickerActivity.this,year + "年" + (month + 1) + "月" + dayOfMonth + "日",Toast.LENGTH_SHORT).show();
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );


        // 显示
        datePickerDialog.show();
    }
}
