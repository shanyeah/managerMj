package com.imovie.mogic.calendar.week;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.imovie.mogic.R;

import org.joda.time.DateTime;

/**
 * Created by Jimmy on 2016/10/7 0007.
 */
public class WeekAdapter extends PagerAdapter {

    private SparseArray<WeeksView> mViews;
    private Context mContext;
    private TypedArray mArray;
    private WeekCalendarView mWeekCalendarView;
    private DateTime mStartDate;
    private int mWeekCount = 220;

    public WeekAdapter(Context context, TypedArray array, WeekCalendarView weekCalendarView) {
        mContext = context;
        mArray = array;
        mWeekCalendarView = weekCalendarView;
        mViews = new SparseArray<>();
        initStartDate();
        mWeekCount = array.getInteger(R.styleable.WeekCalendarView_week_count, 220);
    }

    private void initStartDate() {
        mStartDate = new DateTime();
        mStartDate = mStartDate.plusDays(-mStartDate.getDayOfWeek() % 7);
    }

    @Override
    public int getCount() {
        return mWeekCount;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        for (int i = 0; i < 3; i++) {
            if (position - 2 + i >= 0 && position - 2 + i < mWeekCount && mViews.get(position - 2 + i) == null) {
                instanceWeekView(position - 2 + i);
            }
        }
        container.addView(mViews.get(position));
        return mViews.get(position);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public SparseArray<WeeksView> getViews() {
        return mViews;
    }

    public int getWeekCount() {
        return mWeekCount;
    }

    public WeeksView instanceWeekView(int position) {
        WeeksView weeksView = new WeeksView(mContext, mArray, mStartDate.plusWeeks(position - mWeekCount / 2));
        weeksView.setId(position);
        weeksView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        weeksView.setOnWeekClickListener(mWeekCalendarView);
        weeksView.invalidate();
        mViews.put(position, weeksView);
        return weeksView;
    }

}
