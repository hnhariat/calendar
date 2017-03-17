package com.sun.toy.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;

import com.sun.toy.FrgCalendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


/**
 * Created by hnhariat on 2016-01-06.
 */
public class AdapterFrgCalendar extends FragmentStatePagerAdapter {
    private HashMap<Integer, FrgCalendar> frgMap;
    private ArrayList<Long> listMonthByMillis = new ArrayList<>();
    private int numOfMonth;
    private FrgCalendar.OnFragmentListener onFragmentListener;

    public AdapterFrgCalendar(FragmentManager fm) {
        super(fm);
        clearPrevFragments(fm);
        frgMap = new HashMap<Integer, FrgCalendar>();
    }

    private void clearPrevFragments(FragmentManager fm) {
        List<Fragment> listFragment = fm.getFragments();

        if (listFragment != null) {
            FragmentTransaction  ft = fm.beginTransaction();

            for (Fragment f : listFragment) {
                if (f instanceof FrgCalendar) {
                    ft.remove(f);
                }
            }
            ft.commitAllowingStateLoss();
        }
    }

    @Override
    public Fragment getItem(int position) {
        FrgCalendar frg = null;
        if (frgMap.size() > 0) {
            frg = frgMap.get(position);
        }
        if (frg == null) {
            frg = FrgCalendar.newInstance(position);
            frg.setOnFragmentListener(onFragmentListener);
            frgMap.put(position, frg);
        }
        frg.setTimeByMillis(listMonthByMillis.get(position));

        return frg;
    }

    @Override
    public int getCount() {
        return listMonthByMillis.size();
    }

    public void setNumOfMonth(int numOfMonth) {
        this.numOfMonth = numOfMonth;

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -numOfMonth);
        calendar.set(Calendar.DATE, 1);

        for (int i = 0; i < numOfMonth * 2 + 1; i++) {
            listMonthByMillis.add(calendar.getTimeInMillis());
            calendar.add(Calendar.MONTH, 1);
        }

        notifyDataSetChanged();
    }

    public void addNext() {
        long lastMonthMillis = listMonthByMillis.get(listMonthByMillis.size() - 1);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(lastMonthMillis);
        for (int i = 0; i < numOfMonth; i++) {
            calendar.add(Calendar.MONTH, 1);
            listMonthByMillis.add(calendar.getTimeInMillis());
        }
        notifyDataSetChanged();
    }

    public void addPrev() {
        long lastMonthMillis = listMonthByMillis.get(0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(lastMonthMillis);
        calendar.set(Calendar.DATE, 1);
        for (int i = numOfMonth; i > 0; i--) {
            calendar.add(Calendar.MONTH, -1);

            listMonthByMillis.add(0, calendar.getTimeInMillis());
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public String getMonthDisplayed(int position) {
        Calendar calendar = Calendar.getInstance();
        int yyyy = calendar.get(Calendar.YEAR);
        calendar.setTimeInMillis(listMonthByMillis.get(position));
        if (yyyy != calendar.get(Calendar.YEAR)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy. MM");
            Date date = new Date();
            date.setTime(listMonthByMillis.get(position));

            return sdf.format(date);
        } else {
            return calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
        }
    }

    public void setOnFragmentListener(FrgCalendar.OnFragmentListener onFragmentListener) {
        this.onFragmentListener = onFragmentListener;
    }
}
