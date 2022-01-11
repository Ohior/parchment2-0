package com.example.patchment20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;


public class AlarmActivity extends AppCompatActivity {

    private static final String DB_KEY = "alarm_key";
    TimePicker timepicker_id;
    EditText alarm_date_et_id;
    SwitchCompat alarm_save_toggle_id;
    TextView alarm_time_tv_id;
    TextView alarm_date_tv_id;
    private String toggle_btn;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        alarm_date_tv_id = findViewById(R.id.alarm_date_tv_id);
        alarm_time_tv_id = findViewById(R.id.alarm_time_tv_id);

        timepicker_id = findViewById(R.id.timepicker_id);
        alarm_save_toggle_id = findViewById(R.id.alarm_save_toggle_id);

        checkForNullity();
        toggleButton();
    }

    private void checkForNullity() {
        if (MyDatabase.getString(AlarmActivity.this, DB_KEY) != null){
            String tg = MyDatabase.getString(AlarmActivity.this, DB_KEY);
            alarm_save_toggle_id.setChecked(tg.equals("true"));
        }
        if (MyDatabase.getString(AlarmActivity.this, "alarm_date") != null){
            alarm_date_tv_id.setText(MyDatabase.getString(AlarmActivity.this, "alarm_date"));
        }
        if (MyDatabase.getString(AlarmActivity.this, "alarm_time") != null){
            alarm_time_tv_id.setText(MyDatabase.getString(AlarmActivity.this, "alarm_time"));
        }
    }

    private void toggleButton() {
        alarm_save_toggle_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                if (alarm_save_toggle_id.isChecked()){
                    toggle_btn = "true";
                    Toast.makeText(AlarmActivity.this, "Alarm enabled", Toast.LENGTH_SHORT).show();
                    long time;

                    Calendar calendar = Calendar.getInstance();
                    int date = getAlarmDate(calendar);

                    // calender is called to get current time in hour and minute
//                    calendar.set(Calendar.HOUR_OF_DAY, timepicker_id.getCurrentHour());
//                    calendar.set(Calendar.MINUTE, timepicker_id.getCurrentMinute());
                    calendar.set(
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            date,
                            timepicker_id.getCurrentHour(),
                            timepicker_id.getCurrentMinute(), 0
                    );

                    time = (calendar.getTimeInMillis() - (calendar.getTimeInMillis() % 60000));
                    if (System.currentTimeMillis() > time) {
                        // setting time as AM and PM
                        if (Calendar.AM_PM == 0)
                            time = time + (1000 * 60 * 60 * 12);
                        else
                            time = time + (1000 * 60 * 60 * 24);
                    }
                    //populate the alarm textview
                    alarm_time_tv_id.setText(new StringBuffer(calendar.get(Calendar.HOUR)+":"+ calendar.get(Calendar.MINUTE)));
                    MyDatabase.setString(AlarmActivity.this, "alarm_time", calendar.get(Calendar.HOUR)+":"+ calendar.get(Calendar.MINUTE));

                    alarm_date_tv_id.setText(String.valueOf(date));
                    MyDatabase.setString(AlarmActivity.this, "alarm_date", String.valueOf(date));


                    // using intent i have class AlarmReceiver class which inherits
                    // BroadcastReceiver
                    Intent intent = new Intent(AlarmActivity.this, AlarmBroadcastReceiver.class);
                    // we call broadcast using pendingIntent
                    pendingIntent = PendingIntent.getBroadcast(AlarmActivity.this, 0, intent, 0);
                    // Alarm rings continuously until toggle button is turned off
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60000, pendingIntent);
                    // alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (time * 1000), pendingIntent);
                    intent.putExtra("toggle", toggle_btn);
                }else {
                    Log.e("onClick: ","UN CHECKED" );
                    //stop the alarm and future event
                    Intent intent = new Intent(AlarmActivity.this, AlarmPlayingService.class);
                    toggle_btn = "false";
                    alarmManager.cancel(pendingIntent);
                    if (pendingIntent != null)pendingIntent.cancel();
                    stopService(intent);
                    Toast.makeText(AlarmActivity.this, "Alarm disabled", Toast.LENGTH_SHORT).show();
                }
                MyDatabase.setString(AlarmActivity.this, DB_KEY, toggle_btn);
            }
        });
    }

    private int getAlarmDate(Calendar cal) {
        alarm_date_et_id = findViewById(R.id.alarm_date_et_id);
        if (alarm_date_et_id.getText().toString().isEmpty() ||
                Integer.parseInt(alarm_date_et_id.getText().toString()) < cal.get(Calendar.DATE) ||
                Integer.parseInt(alarm_date_et_id.getText().toString()) > cal.getActualMaximum(Calendar.DAY_OF_MONTH))
            return cal.get(Calendar.DAY_OF_MONTH);
        return Integer.parseInt(alarm_date_et_id.getText().toString());
    }

}
