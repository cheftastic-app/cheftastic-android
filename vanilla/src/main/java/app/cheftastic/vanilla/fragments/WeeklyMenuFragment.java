package app.cheftastic.vanilla.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.cheftastic.R;
import app.cheftastic.vanilla.NutritionalInfoActivity;
import app.cheftastic.vanilla.model.WeeklyMenu;
import app.cheftastic.vanilla.widgets.ViewNutritionalInfoSummary;
import app.cheftastic.vanilla.widgets.menu.MenuWidget;
import app.cheftastic.vanilla.widgets.week.WeekWidget;

public class WeeklyMenuFragment extends Fragment implements WeekWidget.Handler, MenuWidget.Handler, WeeklyMenu.MenuGenerator.Handler, ViewNutritionalInfoSummary.Handler {

    public static final String STATE_SELECTED_DAY_ID = "state_selected_day_id";
    public static final String STATE_GENERATING_MENU = "state_generating_menu";
    public static final String STATE_GENERATING_MENU_DAY_ID = "state_generating_menu_day_id";
    public static final String STATE_GENERATING_ASSIGNED_MEALS = "state_generating_assigned_meals";
    public static final String STATE_GENERATING_MEALS_TO_ASSIGN = "state_generating_meals_to_assign";
    private static final long UNKNOWN_DAY = -1;

    private Bundle savedState;

    private ViewHolder viewHolder;
    private long savedDayId = UNKNOWN_DAY;
    private long generatingDayId = UNKNOWN_DAY;
    private WeeklyMenu.MenuGenerator menuGenerator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weekly_menu, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        viewHolder = new ViewHolder();
        viewHolder.progressContainer = (FrameLayout) getActivity().findViewById(R.id.fragment_weekly_menu_progress_bar_container);
        viewHolder.progressTextView = (TextView) getActivity().findViewById(R.id.fragment_weekly_menu_progress_text);
        viewHolder.widgetContainer = (LinearLayout) getActivity().findViewById(R.id.fragment_weekly_menu_widget_container);
        viewHolder.weekWidget = (WeekWidget) getActivity().findViewById(R.id.fragment_weekly_menu_widget_week);
        viewHolder.menuWidget = (MenuWidget) getActivity().findViewById(R.id.fragment_weekly_menu_widget_menu);
        viewHolder.viewNutritionalInfoSummary = (ViewNutritionalInfoSummary) getActivity().findViewById(R.id.fragment_weekly_menu_view_nutritional_info);

        viewHolder.weekWidget.setHandler(this);
        viewHolder.menuWidget.setHandler(this);

        if (viewHolder.viewNutritionalInfoSummary != null) {
            viewHolder.viewNutritionalInfoSummary.setHandler(this);

            if (savedState != null) {
                viewHolder.viewNutritionalInfoSummary.restoreSavedState(savedState);
            } else {
                viewHolder.viewNutritionalInfoSummary.setWeeklyMenu(viewHolder.weekWidget.getSelectedDayId());
            }
        }

        if (savedDayId != UNKNOWN_DAY) {
            restoreSavedDayId(savedDayId);
            savedDayId = UNKNOWN_DAY;
        }

        if (generatingDayId != UNKNOWN_DAY && menuGenerator != null) {
            restoreMenuGeneration(generatingDayId);
            generatingDayId = UNKNOWN_DAY;
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (menuGenerator != null) {
            menuGenerator.cancel(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (menuGenerator != null) {
            long dayId = menuGenerator.getDayId();
            menuGenerator = new WeeklyMenu.MenuGenerator(this, true, menuGenerator.getAssignedMeals(), menuGenerator.getNumberOfMealsToAssign());
            restoreMenuGeneration(dayId);
        }
    }

    public boolean isTodaySelected() {
        return (viewHolder.weekWidget != null) && viewHolder.weekWidget.isTodaySelected();
    }

    public void selectToday() {
        if (viewHolder.weekWidget != null && !isTodaySelected()) {
            viewHolder.weekWidget.selectToday();
        }
    }

    public long getSelectedDayId() {
        return viewHolder.weekWidget.getSelectedDayId();
    }

    public void setSavedDayId(long dayId) {
        savedDayId = dayId;
    }

    public Bundle getCurrentState() {
        Bundle state = new Bundle();

        state.putLong(STATE_SELECTED_DAY_ID, getSelectedDayId());

        if (isGeneratingMenu()) {
            state.putBoolean(WeeklyMenuFragment.STATE_GENERATING_MENU, true);
            state.putLong(WeeklyMenuFragment.STATE_GENERATING_MENU_DAY_ID, getGeneratingMenuDayId());

            state.putInt(WeeklyMenuFragment.STATE_GENERATING_ASSIGNED_MEALS, getAssignedMeals());
            state.putInt(WeeklyMenuFragment.STATE_GENERATING_MEALS_TO_ASSIGN, getTotalMealsToAssign());
        } else {
            state.putBoolean(WeeklyMenuFragment.STATE_GENERATING_MENU, false);
        }

        if (viewHolder.viewNutritionalInfoSummary != null) {
            state.putAll(viewHolder.viewNutritionalInfoSummary.getCurrentState());
        }

        return state;
    }

    public void restoreSavedState(Bundle savedState) {
        if (savedState.containsKey(STATE_SELECTED_DAY_ID)) {
            setSavedDayId(savedState.getLong(STATE_SELECTED_DAY_ID));
        }

        if (savedState.getBoolean(STATE_GENERATING_MENU, false)) {
            generatingDayId = savedState.getLong(STATE_GENERATING_MENU_DAY_ID);
            int mealsAlreadyAssigned = savedState.getInt(STATE_GENERATING_ASSIGNED_MEALS);
            int totalMealsToAssign = savedState.getInt(STATE_GENERATING_MEALS_TO_ASSIGN);

            menuGenerator = new WeeklyMenu.MenuGenerator(this, true, mealsAlreadyAssigned, totalMealsToAssign);
        }

        this.savedState = savedState;
    }

    private void restoreSavedDayId(long dayId) {
        viewHolder.weekWidget.restoreSavedState(dayId);
        viewHolder.menuWidget.restoreSavedState(dayId);
    }

    public boolean isGeneratingMenu() {
        return (menuGenerator != null);
    }

    public long getGeneratingMenuDayId() {
        if (menuGenerator != null) {
            return menuGenerator.getDayId();
        }

        return UNKNOWN_DAY;
    }

    public int getAssignedMeals() {
        if (menuGenerator != null) {
            return menuGenerator.getAssignedMeals();
        }

        return 0;
    }

    public int getTotalMealsToAssign() {
        if (menuGenerator != null) {
            return menuGenerator.getNumberOfMealsToAssign();
        }

        return 0;
    }

    public void addRecipe(long dayId, int mealId, int recipeId) {
        viewHolder.menuWidget.addRecipe(dayId, mealId, recipeId);
    }

    public void generateMenu(long dayId, boolean keepAssignedMeals) {
        viewHolder.widgetContainer.setVisibility(LinearLayout.GONE);
        viewHolder.progressContainer.setVisibility(FrameLayout.VISIBLE);

        onPublishProgress(0d);

        menuGenerator = new WeeklyMenu.MenuGenerator(this, keepAssignedMeals);
        menuGenerator.execute(dayId);
    }

    private void restoreMenuGeneration(long dayId) {
        viewHolder.widgetContainer.setVisibility(LinearLayout.GONE);
        viewHolder.progressContainer.setVisibility(FrameLayout.VISIBLE);

        onPublishProgress(((double) getAssignedMeals() / getTotalMealsToAssign()) * 100);

        menuGenerator.execute(dayId);
    }

    public void updateNutritionalInfoSummary() {
        if (viewHolder.viewNutritionalInfoSummary != null) {
            viewHolder.viewNutritionalInfoSummary.update();
        }
    }

    @Override
    public void onWeekWidgetSelectedDayChange(long selectedDayId) {
        viewHolder.menuWidget.setSelectedDayId(selectedDayId);

        if (viewHolder.viewNutritionalInfoSummary != null) {
            viewHolder.viewNutritionalInfoSummary.setWeeklyMenu(selectedDayId);
        }
    }

    @Override
    public void onMenuWidgetSelectedDayChange(long selectedDayId) {
        viewHolder.weekWidget.setSelectedDayId(selectedDayId);

        if (viewHolder.viewNutritionalInfoSummary != null) {
            viewHolder.viewNutritionalInfoSummary.setWeeklyMenu(selectedDayId);
        }
    }

    @Override
    public void onPublishProgress(double percentage) {
        viewHolder.progressTextView.setText((int) Math.floor(percentage) + "%");
    }

    @Override
    public void onMenuGenerated() {
        viewHolder.widgetContainer.setVisibility(LinearLayout.VISIBLE);
        viewHolder.progressContainer.setVisibility(FrameLayout.GONE);
        menuGenerator = null;

        restoreSavedDayId(this.getSelectedDayId());

        updateNutritionalInfoSummary();
    }

    @Override
    public void onClick() {
        Intent i = new Intent(getActivity().getBaseContext(), NutritionalInfoActivity.class);
        i.putExtra(NutritionalInfoActivity.ARG_DAY_OF_WEEK_ID, getSelectedDayId());
        startActivity(i);
    }

    private class ViewHolder {
        FrameLayout progressContainer;
        TextView progressTextView;
        LinearLayout widgetContainer;
        WeekWidget weekWidget;
        MenuWidget menuWidget;
        ViewNutritionalInfoSummary viewNutritionalInfoSummary;
    }
}
