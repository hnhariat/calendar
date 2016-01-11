package com.sun.toy.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
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

        // Iterate through all children, measuring them and computing our dimensions
        // from their size.
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
//                maxHeight += mDateOfWeek;
                rowCount++;
            } else {
                maxHeight = Math.max(maxHeight, child.getMeasuredHeight());
            }
            childState = combineMeasuredStates(childState, child.getMeasuredState());
        }

        // Check against our minimum height and width
        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
        maxHeight = (int) (Math.ceil((count + mDateOfWeek - 1) / 7d) * (mWidthDate * 0.75));// 요일중 일요일이 1부터 시작하므로 1을 빼줌
//        maxHeight = (int) (Math.ceil(count / 7) * mWidthDate) + mWidthDate;
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());
        int expandSpec = MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK, MeasureSpec.AT_MOST);
        // Report our final dimensions.
        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
                resolveSizeAndState(maxHeight, expandSpec, childState << MEASURED_HEIGHT_STATE_SHIFT));

//        super.onMeasure(widthMeasureSpec, expandSpec);
        LayoutParams params = getLayoutParams();
//        params.height = maxHeight;
        params.height = getMeasuredHeight();
        // super.onMeasure(widthMeasureSpec, heightSpec);
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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private int measureText(int measureSpec, int padding1, int paddind2) {
        int result = 0;
        // specMode는 현재뷰를 담고 있는 부모뷰의 스펙모드에 따른 현재 뷰의 스펙모드
        int specMode = MeasureSpec.getMode(measureSpec);
        // specSize는 현재뷰를 담고 있는 부모뷰가 줄 수 있는 공간의 최대크기
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            // Measure the text: twice text size plus padding
            result = 2 * (int) mPaint.measureText("message") + padding1 + paddind2;
            if (specMode == MeasureSpec.AT_MOST) {
                // specSize가 최대값이므로
                // result와 specSize 중 최소값을 찾는다
                result = Math.min(result, specSize);
            }
        }
        // UNSPECIFIED 이면 0을 리턴
        return result;
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
