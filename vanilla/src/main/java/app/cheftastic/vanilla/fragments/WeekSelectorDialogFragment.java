package app.cheftastic.vanilla.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.Calendar;

import app.cheftastic.R;
import app.cheftastic.vanilla.Settings;
import app.cheftastic.vanilla.CheftasticCalendar;

public class WeekSelectorDialogFragment extends DialogFragment {

    public static final String OPTION_SELECTED_WEEK = "week_selector_dialog_fragment_option_selected_week";
    public static final String OPTION_KEEP_SELECTED_MEALS = "week_selector_dialog_fragment_option_keep_selected_meals";
    private ViewHolder viewHolder;
    private String title;
    private boolean showKeepSelectedMealsOption;
    private WeekSelectorDialogFragmentHandler handler;

    public WeekSelectorDialogFragment(String title, boolean showKeepSelectedMealsOption) {
        this.title = title;
        this.showKeepSelectedMealsOption = showKeepSelectedMealsOption;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof WeekSelectorDialogFragmentHandler) {
            handler = (WeekSelectorDialogFragmentHandler) activity;
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        viewHolder = new ViewHolder();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        dialogBuilder.setTitle(title);

        viewHolder.dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_week_picker, null);
        dialogBuilder.setView(viewHolder.dialogView);

        viewHolder.optionCurrentWeekRadioButton = (RadioButton) viewHolder.dialogView.findViewById(R.id.dialog_fragment_week_picker_radio_button_current_week);
        viewHolder.optionNextWeekRadioButton = (RadioButton) viewHolder.dialogView.findViewById(R.id.dialog_fragment_week_picker_radio_button_next_week);
        viewHolder.optionCurrentWeekTextView = (TextView) viewHolder.dialogView.findViewById(R.id.dialog_fragment_week_picker_text_view_current_week);
        viewHolder.optionNextWeekTextView = (TextView) viewHolder.dialogView.findViewById(R.id.dialog_fragment_week_picker_text_view_next_week);
        viewHolder.keepSelectedMealsCheckBox = (CheckBox) viewHolder.dialogView.findViewById(R.id.dialog_week_picker_check_box_keep_recipes);
        viewHolder.keepSelectedMealsDescriptionTextView = (TextView) viewHolder.dialogView.findViewById(R.id.dialog_week_picker_text_view_keep_recipes_description);

        viewHolder.optionCurrentWeekRadioButton.setOnClickListener(new RadioButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.optionNextWeekRadioButton.setChecked(false);
            }
        });

        viewHolder.optionNextWeekRadioButton.setOnClickListener(new RadioButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.optionCurrentWeekRadioButton.setChecked(false);
            }
        });

        viewHolder.optionCurrentWeekTextView.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.optionCurrentWeekRadioButton.setChecked(true);
                viewHolder.optionNextWeekRadioButton.setChecked(false);
            }
        });
        viewHolder.optionNextWeekTextView.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.optionCurrentWeekRadioButton.setChecked(false);
                viewHolder.optionNextWeekRadioButton.setChecked(true);
            }
        });

        viewHolder.optionCurrentWeekTextView.setOnTouchListener(new TextView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    viewHolder.optionCurrentWeekRadioButton.setPressed(true);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    viewHolder.optionCurrentWeekRadioButton.setPressed(false);
                }

                return false;
            }
        });

        viewHolder.optionCurrentWeekTextView.setOnTouchListener(new TextView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    viewHolder.optionNextWeekRadioButton.setPressed(true);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    viewHolder.optionNextWeekRadioButton.setPressed(false);
                }

                return false;
            }
        });

        CheftasticCalendar c = new CheftasticCalendar();
        viewHolder.optionCurrentWeekTextView.setText(getWeekInterval(c));
        c.nextWeek();
        viewHolder.optionNextWeekTextView.setText(getWeekInterval(c));

        viewHolder.keepSelectedMealsCheckBox.setChecked(Settings.getKeepSelectedRecipes());

        if (showKeepSelectedMealsOption) {
            viewHolder.keepSelectedMealsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        viewHolder.keepSelectedMealsDescriptionTextView.setText(R.string.meals_already_set_will_be_kept_and_the_rest_will_be_added);
                    } else {
                        viewHolder.keepSelectedMealsDescriptionTextView.setText(R.string.meals_already_set_will_be_remove_and_all_will_be_added);
                    }
                }
            });
        } else {
            viewHolder.dialogView.findViewById(R.id.dialog_fragment_week_picker_container_keep_recipes).setVisibility(View.GONE);
        }

        dialogBuilder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (handler != null && (viewHolder.optionCurrentWeekRadioButton.isChecked() || viewHolder.optionNextWeekRadioButton.isChecked())) {
                    CheftasticCalendar c = new CheftasticCalendar();
                    if (viewHolder.optionNextWeekRadioButton.isChecked()) c.nextWeek();

                    Bundle options = new Bundle();
                    options.putLong(OPTION_SELECTED_WEEK, c.getDayId());

                    if (showKeepSelectedMealsOption)
                        options.putBoolean(OPTION_KEEP_SELECTED_MEALS, viewHolder.keepSelectedMealsCheckBox.isChecked());

                    handler.onAcceptClick(options);
                }
            }
        });
        dialogBuilder.setNegativeButton(getResources().getString(R.string.cancel), null);

        return dialogBuilder.create();
    }

    private String getWeekInterval(CheftasticCalendar date) {
        CheftasticCalendar today = new CheftasticCalendar();

        String interval = "";

        if (CheftasticCalendar.weeksBetween(today, date) == 0) {
            interval += "(" + today.getDayOfWeekName().toLowerCase() + " " + today.getDayOfMonth();
        } else {
            interval += "(lunes " + date.getDayOfMonth(CheftasticCalendar.MONDAY);
        }

        if (!(CheftasticCalendar.weeksBetween(today, date) == 0 && today.get(Calendar.DAY_OF_WEEK) == CheftasticCalendar.SUNDAY)) {
            interval += " - domingo " + date.getDayOfMonth(CheftasticCalendar.SUNDAY) + ")";
        } else {
            interval += ")";
        }

        return interval;
    }

    public interface WeekSelectorDialogFragmentHandler {
        public void onAcceptClick(Bundle options);
    }

    private class ViewHolder {
        View dialogView;
        RadioButton optionCurrentWeekRadioButton;
        RadioButton optionNextWeekRadioButton;
        TextView optionCurrentWeekTextView;
        TextView optionNextWeekTextView;
        CheckBox keepSelectedMealsCheckBox;
        TextView keepSelectedMealsDescriptionTextView;
    }
}
