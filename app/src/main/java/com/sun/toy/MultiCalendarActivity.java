package com.sun.toy;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.sun.toy.adapters.AdapterFrgCalendar;
import com.sun.toy.adapters.AdapterRcvSimple;
import com.sun.toy.view.Builder;
import com.sun.toy.view.SimpleViewBinder;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.sun.toy.R.id.rcv;

public class MultiCalendarActivity extends BaseActivity implements FrgCalendar.OnFragmentListener {

    private static final int COUNT_PAGE = 12;
    ViewPager pager;
    private AdapterFrgCalendar adapter;
    private AdapterRcvSimple adapterHourLine;
    private RecyclerView rcv;
    private ArrayList mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_calendar);

        initialize();
    }

    @Override
    public void initData() {
        super.initData();
        mList = new ArrayList();
        mList.add("06:00 wakeup");
        mList.add("07:00 breakfast");
        mList.add("08:00 go to office");
        mList.add("09:00 work ");
        mList.add("12:00 lunch");
        mList.add("13:00 work");
        mList.add("14:00 sleep");
        mList.add("18:00 get off work");
        mList.add("16:30 dinner");
        mList.add("20:00 sleep");
    }

    @Override
    public void initView() {
        super.initView();
        pager = (ViewPager) findViewById(R.id.pager);
    }

    public void initControl() {
        adapter = new AdapterFrgCalendar(getSupportFragmentManager());
        pager.setAdapter(adapter);
        adapter.setOnFragmentListener(this);
        adapter.setNumOfMonth(COUNT_PAGE);
        pager.setCurrentItem(COUNT_PAGE);
        String title = adapter.getMonthDisplayed(COUNT_PAGE);
        getSupportActionBar().setTitle(title);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                String title = adapter.getMonthDisplayed(position);
                getSupportActionBar().setTitle(title);

                if (position == 0) {
                    adapter.addPrev();
                    pager.setCurrentItem(COUNT_PAGE, false);
                } else if (position == adapter.getCount() - 1) {
                    adapter.addNext();
                    pager.setCurrentItem(adapter.getCount() - (COUNT_PAGE + 1), false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        adapterHourLine = new AdapterRcvSimple(R.layout.item_rcv_simple);
        SimpleViewBinder.RecyclerViewBuilder builder =  new SimpleViewBinder.RecyclerViewBuilder(getWindow()).setAdapter(adapterHourLine, getSupportFragmentManager()).setList(mList);
        rcv = builder.build();
    }

    @Override
    public void onFragmentListener(View view) {
        resizeHeight(view);
    }

    public void resizeHeight(View mRootView) {

        if (mRootView.getHeight() < 1) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = pager.getLayoutParams();
        if (layoutParams.height <= 0) {
            layoutParams.height = mRootView.getHeight();
            pager.setLayoutParams(layoutParams);
            return;
        }
        ValueAnimator anim = ValueAnimator.ofInt(pager.getLayoutParams().height, mRootView.getHeight());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = pager.getLayoutParams();
                layoutParams.height = val;
                pager.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(200);
        anim.start();
    }
}
