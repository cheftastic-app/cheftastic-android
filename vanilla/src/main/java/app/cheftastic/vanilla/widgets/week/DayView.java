package app.cheftastic.vanilla.widgets.week;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.cheftastic.R;

public class DayView extends LinearLayout
        implements View.OnClickListener,
        View.OnLongClickListener {

    private DayViewHandler handler;
    private ViewHolder view;
    private long dayId;
    private boolean isSelected;

    public DayView(Context context) {
        this(context, null, 0);
    }

    public DayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DayView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);

        this.setOnClickListener(this);
        this.setOnLongClickListener(this);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            setOrientation(LinearLayout.VERTICAL);
            inflater.inflate(R.layout.view_day, this);
        }

        view = new ViewHolder();
        view.day = (TextView) findViewById(R.id.item_day_text_view);
        view.month = (TextView) findViewById(R.id.item_month_text_view);
        view.indicator = findViewById(R.id.item_selection_indicator);

        if (getResources() != null) {
            view.indicator.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.view_day_indicator_off)));
        }
        isSelected = false;
    }

    public void setDay(int day) {
        view.day.setText(String.valueOf(day));
    }

    public void setMonth(String month) {
        view.month.setText(month);
    }

    public void setDayId(long dayId) {
        this.dayId = dayId;
    }

    public boolean setDayId(long dayId, long selectedDayId) {
        setDayId(dayId);
        setIsSelected(dayId == selectedDayId);

        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;

        if (getResources() != null) {
            if (isSelected) {
                view.indicator.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.view_day_indicator_on)));
            } else {
                view.indicator.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.view_day_indicator_off)));
            }
        }
    }

    public void setHandler(DayViewHandler handler) {
        this.handler = handler;
    }

    @Override
    public void onClick(View v) {
        if (handler != null) {
            handler.onClick(this, this.dayId, isSelected);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (handler != null) {
            handler.onLongClick();
        }
        return true;
    }

    public interface DayViewHandler {

        public void onClick(DayView dayView, long dayId, boolean selected);

        public void onLongClick();
    }

    private class ViewHolder {
        TextView day;
        TextView month;
        View indicator;
    }
}
