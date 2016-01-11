package com.sun.toy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sun.toy.widget.CalendarItemView;
import com.sun.toy.widget.CalendarView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendarView = (CalendarView)findViewById(R.id.calendarview);

        initCalendar();
        calendarView.setDate(System.currentTimeMillis());
    }

    private void initCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, 1);
        // last day of this month
        int maxDateOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        // the first Day of week this month
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        for (int i = 0; i < maxDateOfMonth; i++) {
            CalendarItemView child = new CalendarItemView(this);
            child.setDate(calendar.getTimeInMillis());
            if (i < 7) {
                child.setDayOfWeek(i);
            } else {
                calendar.add(Calendar.DATE, 1);
            }
            calendarView.addView(child);
        }
    }
}
