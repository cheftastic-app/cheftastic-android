package app.cheftastic.vanilla.widgets.week;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import app.cheftastic.vanilla.CheftasticCalendar;

public class WeekWidget extends ViewPager
        implements WeekView.WeekViewHandler {

    private final int PREVIOUS_PAGE = 0;
    private final int CURRENT_PAGE = 1;
    private final int NEXT_PAGE = 2;

    private final int NUM_PAGES = 3;

    private int selectedPage = CURRENT_PAGE;
    private int selectedWeed = 0;
    private long selectedDayId;
    private WeekView[] weekViews;
    private Handler handler;

    public WeekWidget(Context context) {
        this(context, null);
    }

    public WeekWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeekWidget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);

        weekViews = new WeekView[NUM_PAGES];
        weekViews[PREVIOUS_PAGE] = new WeekView(getContext());
        weekViews[CURRENT_PAGE] = new WeekView(getContext());
        weekViews[NEXT_PAGE] = new WeekView(getContext());

        weekViews[PREVIOUS_PAGE].setHandler(this);
        weekViews[CURRENT_PAGE].setHandler(this);
        weekViews[NEXT_PAGE].setHandler(this);

        weekViews[PREVIOUS_PAGE].setSelectedWeek(selectedWeed - 1);
        weekViews[CURRENT_PAGE].setSelectedWeek(selectedWeed);
        weekViews[NEXT_PAGE].setSelectedWeek(selectedWeed + 1);

        this.setAdapter(new WeekWidgetPagerAdapter());

        setOnPageChangeListener(
                new OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageSelected(int position) {
                        selectedPage = position;
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                        if (state == ViewPager.SCROLL_STATE_IDLE) {
                            if (selectedPage == PREVIOUS_PAGE) {
                                selectedWeed--;

                                weekViews[NEXT_PAGE].setSelectedWeek(selectedWeed + 1);
                                weekViews[CURRENT_PAGE].setSelectedWeek(selectedWeed);
                                setCurrentItem(CURRENT_PAGE, false);
                                weekViews[PREVIOUS_PAGE].setSelectedWeek(selectedWeed - 1);
                            } else if (selectedPage == NEXT_PAGE) {
                                selectedWeed++;

                                weekViews[PREVIOUS_PAGE].setSelectedWeek(selectedWeed - 1);
                                weekViews[CURRENT_PAGE].setSelectedWeek(selectedWeed);
                                setCurrentItem(CURRENT_PAGE, false);
                                weekViews[NEXT_PAGE].setSelectedWeek(selectedWeed + 1);
                            }
                        }
                    }
                }
        );

        setCurrentItem(CURRENT_PAGE);
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public long getSelectedDayId() {
        return selectedDayId;
    }

    public boolean isTodaySelected() {
        return (selectedWeed == 0 && selectedDayId == new CheftasticCalendar().getDayId());
    }

    public void setSelectedDayId(long selectedDay) {
        int previousSelectedWeek = selectedWeed;
        int currentSelectedWeek = new CheftasticCalendar(selectedDay).getRelativeWeek();

        this.selectedDayId = selectedDay;
        this.selectedWeed = currentSelectedWeek;
        weekViews[PREVIOUS_PAGE].setSelection(selectedDay, selectedWeed - 1);
        weekViews[CURRENT_PAGE].setSelection(selectedDay, selectedWeed);
        weekViews[NEXT_PAGE].setSelection(selectedDay, selectedWeed + 1);

        if (previousSelectedWeek < currentSelectedWeek) {
            selectedWeed = currentSelectedWeek - 1;
            weekViews[NEXT_PAGE].setSelection(selectedDay, selectedWeed + 1);
            setCurrentItem(NEXT_PAGE, true);
        } else if (previousSelectedWeek > currentSelectedWeek) {
            selectedWeed = currentSelectedWeek + 1;
            weekViews[PREVIOUS_PAGE].setSelection(selectedDay, selectedWeed - 1);
            setCurrentItem(PREVIOUS_PAGE, true);
        }
    }

    public void selectToday() {
        setSelectedDayId(new CheftasticCalendar().getDayId());
    }

    public void restoreSavedState(long dayId) {
        this.selectedDayId = dayId;
        this.selectedWeed = new CheftasticCalendar(selectedDayId).getRelativeWeek();

        weekViews[PREVIOUS_PAGE].setSelection(selectedDayId, selectedWeed - 1);
        weekViews[CURRENT_PAGE].setSelection(selectedDayId, selectedWeed);
        weekViews[NEXT_PAGE].setSelection(selectedDayId, selectedWeed + 1);
        setCurrentItem(CURRENT_PAGE, false);
    }

    @Override
    public void onSelectedDayChange(DayView selectedDayView, long selectedDayId) {
        this.selectedDayId = selectedDayId;

        for (WeekView v : weekViews) {
            v.setSelectedDay(selectedDayId, selectedDayView);
        }

        if (handler != null) {
            handler.onWeekWidgetSelectedDayChange(selectedDayId);
        }
    }

    public interface Handler {

        public void onWeekWidgetSelectedDayChange(long selectedDayId);

    }

    private class WeekWidgetPagerAdapter extends PagerAdapter {

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(weekViews[position]);
            return weekViews[position];
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }
    }
}