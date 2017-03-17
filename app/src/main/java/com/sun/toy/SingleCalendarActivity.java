package com.sun.toy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sun.toy.widget.CalendarItemView;
import com.sun.toy.widget.CalendarView;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SingleCalendarActivity extends BaseActivity {


    private CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_calendar);
        initialize();
    }

    public void initView() {
        calendarView = (CalendarView) findViewById(R.id.calendarview);
        calendarView.setDate(System.currentTimeMillis());
        initCalendar();
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
