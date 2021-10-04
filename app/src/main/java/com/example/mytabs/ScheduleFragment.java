package com.example.mytabs;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;

public class ScheduleFragment extends Fragment {

    public String TAG = "JobSchedularService";
    private BroadcastReceiver receiver;
    private AlarmManager alarmManager;
    public AlarmReceiver alarmReceiver;
    private PendingIntent broadcast;
    private MaterialTimePicker picker;
    private MaterialDatePicker datePicker;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Calendar calendar;
    private String mParam1;
    private String mParam2;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    public static ScheduleFragment newInstance(String param1, String param2) {
        ScheduleFragment fragment = new ScheduleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_schedule, container, false);
        TextView selTime = v.findViewById(R.id.selectedTime);
        Button btnCancel = v.findViewById(R.id.cancelAlarmBtn);
        Button btnSetAlarm = v.findViewById(R.id.setAlarmBtn);
        Button btnSetTime = v.findViewById(R.id.selectTimeBtn);

        btnSetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selTime.getText().equals("00 : 00") && !selTime.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Please set time", Toast.LENGTH_SHORT).show();
                } else {
                    setAlarm();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (selTime.getText().equals("00 : 00") && !selTime.getText().toString().isEmpty()) {
//                    Toast.makeText(getContext(), "Please set time first", Toast.LENGTH_SHORT).show();
//                } else {
//                    cancelAlarm();
//
//                }
                Intent i = new Intent(getActivity().getApplicationContext(),Destination.class);
                startActivity(i);
            }
        });

        btnSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker = new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_12H)
                        .setHour(12)
                        .setMinute(0)
                        .setTitleText("select alarm time")
                        .build();

                picker.show(getActivity().getSupportFragmentManager(), "com.example.jobschedular.channelId");
                picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (picker.getHour() > 12) {
                            selTime.setText(String.format("%02d", (picker.getHour() - 12)) + " : " + String.format("%02d", picker.getMinute()) + " PM");
                        } else {
                            selTime.setText(String.format("%02d", (picker.getHour())) + " : " + String.format("%02d", picker.getMinute()) + " AM");
                        }

                        calendar = Calendar.getInstance();
                        int YEAR = calendar.get(Calendar.YEAR);
                        int MONTH = calendar.get(Calendar.MONTH);
                        int DAY = calendar.get(Calendar.DAY_OF_MONTH);
                        calendar.set(Calendar.YEAR, YEAR);
                        calendar.set(Calendar.MONTH, MONTH);
                        calendar.set(Calendar.DAY_OF_MONTH, DAY);
                        calendar.set(Calendar.HOUR_OF_DAY, picker.getHour());
                        calendar.set(Calendar.MINUTE, picker.getMinute());
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);
                        Toast.makeText(getContext(), "Curr time:>>>" + calendar.getTime(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });



        return v;
    }

    private void cancelAlarm() {
        Intent notificationIntent = new Intent(getContext(), AlarmReceiver.class);
        broadcast = PendingIntent.getBroadcast(getContext(), 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if(alarmManager == null){
            alarmManager   = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        }


        alarmManager.cancel(broadcast);
        Toast.makeText(this.getContext(), "alarm cancelled", Toast.LENGTH_SHORT).show();
    }

    private void setAlarm() {
        Log.d(TAG, "Alarm setting started");
        alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent(this.getContext(), AlarmReceiver.class);
        PendingIntent broadcast = PendingIntent.getBroadcast(this.getContext(), 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Calendar cal = Calendar.getInstance();
//            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),AlarmManager.INTERVAL_DAY,broadcast);
//            Log.d(TAG,calendar.getTimeInMillis()+"___"+calendar.getTime());
//            Toast.makeText(this, "Triggered at>>"+calendar.getTime(), Toast.LENGTH_SHORT).show();
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 10*1000, broadcast);

        }


        Log.d(TAG, "Alarm setting ended");
        String str = (picker.getHour() > 12) ? " PM" : " AM";
        int t = (picker.getHour() > 12) ? picker.getHour() - 12 : picker.getHour();
        Toast.makeText(this.getContext(), "alarm set at " + String.format("%02d", t) + " : " + String.format("%02d", picker.getMinute()) + str, Toast.LENGTH_SHORT).show();

    }
}