package com.sun.toy.widget;

import android.annotation.TargetApi;
import android.app.Notification;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.sun.toy.R;
import com.sun.toy.utils.Common;

import java.util.Calendar;

/**
 * Created by hnhariat on 2016-01-06.
 */
public class CalendarItemView extends View {

    Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mPaintTextWhite = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mPaintBackground = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mPaintBackgroundToday = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mPaintBackgroundEvent = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int dayOfWeek = -1;
    private boolean isStaticText = false;
    private long millis;
    private Rect rect;
    private boolean isTouchMode;
    private int dp11;
    private int dp16;
    private boolean hasEvent = false;
    private int[] mColorEvents;
    private final float RADIUS = 100f;

    public CalendarItemView(Context context) {
        super(context);
        initialize();
    }

    public CalendarItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    private void initialize() {
        dp11 = (int) Common.dp2px(getContext(),11);
        dp16 = (int) Common.dp2px(getContext(),16);

        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(dp11);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaintTextWhite.setColor(Color.WHITE);
        mPaintTextWhite.setTextSize(dp11);
        mPaintTextWhite.setTextAlign(Paint.Align.CENTER);
        mPaintBackground.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        mPaintBackgroundToday.setColor(ContextCompat.getColor(getContext(), R.color.today));
        mPaintBackgroundEvent.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        setClickable(true);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                Log.d("hatti.onTouchEvent", event.getAction() + "");
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
//                        setBackgroundResource(R.drawable.round_default_select);
                        rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                        isTouchMode = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        if (isTouchMode) {
                            ((CalendarView) getParent()).setCurrentSelectedView(v);
                            isTouchMode = false;
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        isTouchMode = false;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (!rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                            isTouchMode = false;
                            return true;
                        }
                        break;
                }
                return false;
            }
        });
        setPadding(30, 0, 30, 0);
    }

    @Override
    public void setBackgroundResource(int resid) {
        super.setBackgroundResource(resid);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int xPos = (canvas.getWidth() / 2);
        int yPos = (int) ((canvas.getHeight() / 2) - ((mPaint.descent() + mPaint.ascent()) / 2));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);

        CalendarView calendarView = (CalendarView) getParent();
        if (calendarView.getParent() instanceof ViewPager) {
            ViewGroup parent = (ViewPager) calendarView.getParent();
            CalendarItemView tagView = (CalendarItemView) parent.getTag();

            if (!isStaticText && tagView != null && tagView.getTag() != null && tagView.getTag() instanceof Long) {
                long millis = (long) tagView.getTag();
                if (isSameDay(millis, this.millis)) {
                    RectF rectF = new RectF(xPos - dp16, getHeight() / 2 - dp16, xPos + dp16, getHeight() / 2 + dp16);
                    canvas.drawRoundRect(rectF, RADIUS, RADIUS, mPaintBackground);
                }
            }
        }

        if (!isStaticText && isToday(millis)) {
            RectF rectF = new RectF(xPos - dp16, getHeight() / 2 - dp16, xPos + dp16, getHeight() / 2 + dp16);
            canvas.drawRoundRect(rectF, RADIUS, RADIUS, mPaintBackgroundToday);
        }

        if (isStaticText) {
            // 요일 표시
            canvas.drawText(CalendarView.DAY_OF_WEEK[dayOfWeek], xPos, yPos, mPaint);
        } else {
            // 날짜 표시
            if (isToday(millis)) {
                canvas.drawText(calendar.get(Calendar.DATE) + "", xPos, yPos, mPaintTextWhite);
            } else {
                canvas.drawText(calendar.get(Calendar.DATE) + "", xPos, yPos, mPaint);
            }
        }

        if (hasEvent) {
            mPaintBackgroundEvent.setColor(getResources().getColor(mColorEvents[0]));
            RectF rectF = new RectF(xPos - 5, getHeight() / 2 + 20, xPos + 5, getHeight() / 2 + 30);
            canvas.drawRoundRect(rectF, RADIUS, RADIUS, mPaintBackgroundToday);
        }

    }

    private boolean isToday(long millis) {
        Calendar cal1 = Calendar.getInstance();
        return isSameDay(cal1.getTimeInMillis(), millis);

    }

    public void setDate(long millis) {
        this.millis = millis;
        setTag(millis);
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
        isStaticText = true;
    }

    public void setEvent(int... resid) {
        hasEvent = true;
        mColorEvents = resid;
    }
    public boolean isSameDay(long millis1, long millis2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTimeInMillis(millis1);
        cal2.setTimeInMillis(millis2);
        Log.d("hatti.calendar", "" + cal1.get(Calendar.YEAR) + "" + cal1.get(Calendar.MONTH) + "" + cal1.get(Calendar.DATE) + " VS " +
                cal2.get(Calendar.YEAR) + "" + cal2.get(Calendar.MONTH) + "" + cal2.get(Calendar.DATE));
        return (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) && cal1.get(Calendar.DATE) == cal2.get(Calendar.DATE));
    }

    public boolean isStaticText() {
        return isStaticText;
    }


}
