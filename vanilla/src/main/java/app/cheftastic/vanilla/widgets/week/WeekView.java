package app.cheftastic.vanilla.widgets.week;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import app.cheftastic.R;
import app.cheftastic.vanilla.CheftasticCalendar;

public class WeekView extends FrameLayout
        implements DayView.DayViewHandler {

    protected DayView selectedDayView;
    protected long selectedDayId;
    private WeekViewHandler handler;
    private ViewHolder viewHolder;
    private CheftasticCalendar calendar;

    public WeekView(Context context) {
        this(context, null);
    }

    public WeekView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeekView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        calendar = new CheftasticCalendar(0);
        selectedDayId = calendar.getDayId();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.view_week, this);
        }

        viewHolder = new ViewHolder();
        viewHolder.monday = (DayView) findViewById(R.id.item_week_day_1);
        viewHolder.tuesday = (DayView) findViewById(R.id.item_week_day_2);
        viewHolder.wednesday = (DayView) findViewById(R.id.item_week_day_3);
        viewHolder.thursday = (DayView) findViewById(R.id.item_week_day_4);
        viewHolder.friday = (DayView) findViewById(R.id.item_week_day_5);
        viewHolder.saturday = (DayView) findViewById(R.id.item_week_day_6);
        viewHolder.sunday = (DayView) findViewById(R.id.item_week_day_7);
        viewHolder.monday.setHandler(this);
        viewHolder.tuesday.setHandler(this);
        viewHolder.wednesday.setHandler(this);
        viewHolder.thursday.setHandler(this);
        viewHolder.friday.setHandler(this);
        viewHolder.saturday.setHandler(this);
        viewHolder.sunday.setHandler(this);

        updateViewHolder();
    }

    public void setSelectedDay(long selectedDayId, DayView selectedDayView) {
        this.selectedDayId = selectedDayId;
        this.selectedDayView = selectedDayView;
    }

    private void updateViewHolder() {
        viewHolder.monday.setDay(calendar.getDayOfMonth(CheftasticCalendar.MONDAY));
        viewHolder.monday.setMonth(calendar.getMonthNameAbbreviation(CheftasticCalendar.MONDAY));
        if (viewHolder.monday.setDayId(calendar.getDayOfWeekId(CheftasticCalendar.MONDAY), selectedDayId)) {
            selectedDayView = viewHolder.monday;
        }

        viewHolder.tuesday.setDay(calendar.getDayOfMonth(CheftasticCalendar.TUESDAY));
        viewHolder.tuesday.setMonth(calendar.getMonthNameAbbreviation(CheftasticCalendar.TUESDAY));
        if (viewHolder.tuesday.setDayId(calendar.getDayOfWeekId(CheftasticCalendar.TUESDAY), selectedDayId)) {
            selectedDayView = viewHolder.tuesday;
        }

        viewHolder.wednesday.setDay(calendar.getDayOfMonth(CheftasticCalendar.WEDNESDAY));
        viewHolder.wednesday.setMonth(calendar.getMonthNameAbbreviation(CheftasticCalendar.WEDNESDAY));
        if (viewHolder.wednesday.setDayId(calendar.getDayOfWeekId(CheftasticCalendar.WEDNESDAY), selectedDayId)) {
            selectedDayView = viewHolder.wednesday;
        }

        viewHolder.thursday.setDay(calendar.getDayOfMonth(CheftasticCalendar.THURSDAY));
        viewHolder.thursday.setMonth(calendar.getMonthNameAbbreviation(CheftasticCalendar.THURSDAY));
        if (viewHolder.thursday.setDayId(calendar.getDayOfWeekId(CheftasticCalendar.THURSDAY), selectedDayId)) {
            selectedDayView = viewHolder.thursday;
        }

        viewHolder.friday.setDay(calendar.getDayOfMonth(CheftasticCalendar.FRIDAY));
        viewHolder.friday.setMonth(calendar.getMonthNameAbbreviation(CheftasticCalendar.FRIDAY));
        if (viewHolder.friday.setDayId(calendar.getDayOfWeekId(CheftasticCalendar.FRIDAY), selectedDayId)) {
            selectedDayView = viewHolder.friday;
        }

        viewHolder.saturday.setDay(calendar.getDayOfMonth(CheftasticCalendar.SATURDAY));
        viewHolder.saturday.setMonth(calendar.getMonthNameAbbreviation(CheftasticCalendar.SATURDAY));
        if (viewHolder.saturday.setDayId(calendar.getDayOfWeekId(CheftasticCalendar.SATURDAY), selectedDayId)) {
            selectedDayView = viewHolder.saturday;
        }

        viewHolder.sunday.setDay(calendar.getDayOfMonth(CheftasticCalendar.SUNDAY));
        viewHolder.sunday.setMonth(calendar.getMonthNameAbbreviation(CheftasticCalendar.SUNDAY));
        if (viewHolder.sunday.setDayId(calendar.getDayOfWeekId(CheftasticCalendar.SUNDAY), selectedDayId)) {
            selectedDayView = viewHolder.sunday;
        }

        if (handler != null && selectedDayView != null) {
            handler.onSelectedDayChange(selectedDayView, selectedDayId);
        }
    }

    public void setSelectedWeek(int relativeWeek) {
        calendar.previousWeek(relativeWeek);

        updateViewHolder();
    }

    public void setSelection(long selectedDayId, int relativeWeek) {
        this.selectedDayId = selectedDayId;
        setSelectedWeek(relativeWeek);
    }

    public void setHandler(WeekViewHandler handler) {
        this.handler = handler;
    }

    @Override
    public void onClick(DayView dayView, long dayId, boolean selected) {
        if (!selected) {
            if (selectedDayView != null) {
                selectedDayView.setIsSelected(false);
            }
            selectedDayView = dayView;
            dayView.setIsSelected(true);

            if (handler != null) {
                handler.onSelectedDayChange(dayView, dayId);
            }
        }
    }

    @Override
    public void onLongClick() {
    }

    public interface WeekViewHandler {

        public void onSelectedDayChange(DayView selectedDayView, long selectedDayId);

    }

    private class ViewHolder {
        DayView monday;
        DayView tuesday;
        DayView wednesday;
        DayView thursday;
        DayView friday;
        DayView saturday;
        DayView sunday;
    }
}
