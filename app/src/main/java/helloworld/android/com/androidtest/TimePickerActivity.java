package helloworld.android.com.androidtest;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class TimePickerActivity extends Activity {
    private Calendar calendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timepicker);

        calendar = Calendar.getInstance();

        TimePicker timePicker = findViewById(R.id.timePicker);
        // 设置成24小时制
        timePicker.setIs24HourView(true);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                Toast.makeText(TimePickerActivity.this,hourOfDay + ":" + minute,Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 显示showTimePickerDialog
    public void showTimePickerDialog(View view){

        TimePickerDialog timePickerDialog = new TimePickerDialog(TimePickerActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Toast.makeText(TimePickerActivity.this,hourOfDay + ":" + minute,Toast.LENGTH_SHORT).show();
            }
        },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true);
        timePickerDialog.show();

    }
}
