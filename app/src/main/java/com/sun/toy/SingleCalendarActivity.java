package com.sun.toy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sun.toy.widget.CalendarItemView;
import com.sun.toy.widget.CalendarView;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SingleCalendarActivity extends AppCompatActivity {

    @Bind(R.id.calendarview)
    CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_calendar);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
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
