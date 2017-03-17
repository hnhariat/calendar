/*
   Copyright 2016 Bang Sunje

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.sun.toy.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.sun.toy.R;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by hnhariat on 2015-10-15.
 */
public class CalendarView extends ViewGroup {
    private final int mScreenWidth;
    private final int mWidthDate;
    private long mMillis;
    /**
     * 1일의 요일
     */
    private int mDateOfWeek;
    /**
     * 해당월의 마지막 날짜
     */
    private int mMaxtDateOfMonth;

    private int mDefaultTextSize = 40;

    private int mTextColor = Color.BLUE;

    private Paint mPaint = makePaint(mTextColor);
    private Paint mTestPaint = makePaint(mTextColor);

    public CalendarItemView mCurrentSelectedView = null;

    public static String[] DAY_OF_WEEK = null;

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        mWidthDate = mScreenWidth / 7;
        DAY_OF_WEEK = getResources().getStringArray(R.array.day_of_week);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        // Measurement will ultimately be computing these values.
        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;
        int mLeftWidth = 0;
        int rowCount = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mMillis);

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);

            if (child.getVisibility() == GONE)
                continue;

            // Measure the child.
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            maxWidth += Math.max(maxWidth, child.getMeasuredWidth());
            mLeftWidth += child.getMeasuredWidth();

            if ((mLeftWidth / mScreenWidth) > rowCount) {
                maxHeight += child.getMeasuredHeight();
                rowCount++;
            } else {
                maxHeight = Math.max(maxHeight, child.getMeasuredHeight());
            }
            childState = combineMeasuredStates(childState, child.getMeasuredState());
        }

        maxHeight = (int) (Math.ceil((count + mDateOfWeek - 1) / 7d) * (mWidthDate * 0.75));// 요일중 일요일이 1부터 시작하므로 1을 빼줌
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());
        int expandSpec = MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK, MeasureSpec.AT_MOST);

        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
                resolveSizeAndState(maxHeight, expandSpec, childState << MEASURED_HEIGHT_STATE_SHIFT));

        LayoutParams params = getLayoutParams();
        params.height = getMeasuredHeight();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {


        final int count = getChildCount();
        int curWidth, curHeight, curLeft, curTop, maxHeight;

        final int childLeft = this.getPaddingLeft();
        final int childTop = this.getPaddingTop();
        final int childRight = this.getMeasuredWidth() - this.getPaddingRight();
        final int childBottom = this.getMeasuredHeight() - this.getPaddingBottom();
        final int childWidth = childRight - childLeft;
        final int childHeight = childBottom - childTop;

        maxHeight = 0;
        curLeft = childLeft;
        curTop = childTop;

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);

            if (child.getVisibility() == GONE)
                return;

            child.measure(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.AT_MOST));
            curWidth = mWidthDate;
            curHeight = (int) (mWidthDate * 0.75);

            if (curLeft + curWidth >= childRight) {
                curLeft = childLeft;
                curTop += maxHeight;
                maxHeight = 0;
            }
            if (i == 7) {
                curLeft = (mDateOfWeek - 1) * curWidth;
            }
            child.layout(curLeft, curTop, curLeft + curWidth, curTop + curHeight);

            if (maxHeight < curHeight) {
                maxHeight = curHeight;
            }
            curLeft += curWidth;
        }
    }

    public void setDate(long millis) {
        mMillis = millis;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        calendar.set(Calendar.DATE, 1);

        mDateOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        mMaxtDateOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        invalidate();
    }

    // TODO : touch로 scale in/out 기능 추가
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private Paint makePaint(int color) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(color);
        p.setTextSize(mDefaultTextSize);
        return p;
    }

    public void initCalendar(int dayOfWeek, int maxDateOfMonth) {
        mDateOfWeek = dayOfWeek;
        mMaxtDateOfMonth = maxDateOfMonth;
    }

    public void setCurrentSelectedView(View view) {
        if (getParent() instanceof ViewPager) {
            ViewPager pager = (ViewPager) getParent();
            View tagView = (View) pager.getTag();
            if (tagView != null) {
                long time = (long) tagView.getTag();
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(time);
                for (int i = 0; i < pager.getChildCount(); i++) {
                    for (int j = 0; j < getChildCount(); j++) {
                        CalendarItemView child = (CalendarItemView) ((CalendarView) pager.getChildAt(i)).getChildAt(j);
                        if (child == null) {
                            continue;
                        }
                        if (child.isStaticText()) {
                            continue;
                        }
                        if (child.isSameDay((Long) child.getTag(), (Long) tagView.getTag())) {
                            child.invalidate();
                            break;
                        }
                    }
                }
            }
            if (tagView == view) {
                pager.setTag(null);
                return;
            }
            long time = (long) view.getTag();
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(time);
            pager.setTag(view);
            view.invalidate();
        }
    }
}
