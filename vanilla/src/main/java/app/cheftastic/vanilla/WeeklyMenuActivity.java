package app.cheftastic.vanilla;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import app.cheftastic.R;
import app.cheftastic.vanilla.fragments.NavBarFragment;
import app.cheftastic.vanilla.fragments.WeeklyMenuFragment;
import app.cheftastic.vanilla.fragments.WeekSelectorDialogFragment;

public class WeeklyMenuActivity extends AppCompatActivity
        implements WeekSelectorDialogFragment.WeekSelectorDialogFragmentHandler {

    private WeeklyMenuFragment weeklyMenuFragment;

    private static final int SELECT_RECIPE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.activity_weekly_menu);

        NavBarFragment navBarFragment = (NavBarFragment)
                getSupportFragmentManager().findFragmentById(R.id.activity_weekly_menu_fragment_nav_bar);
        navBarFragment.setup(R.id.activity_weekly_menu_fragment_nav_bar,
                (DrawerLayout) findViewById(R.id.activity_weekly_menu_drawer_layout),
                NavBarFragment.OPTION_WEEKLY_MENU);

        weeklyMenuFragment = new WeeklyMenuFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_weekly_menu_container, weeklyMenuFragment)
                .commit();

        if (savedInstanceState != null) {
            weeklyMenuFragment.restoreSavedState(savedInstanceState);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putAll(weeklyMenuFragment.getCurrentState());
    }

    @Override
    public void onBackPressed() {
        if (weeklyMenuFragment != null && weeklyMenuFragment.isTodaySelected()) {
            super.onBackPressed();
        } else if (weeklyMenuFragment != null) {
            weeklyMenuFragment.selectToday();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_CANCELED) {
            if (getResources() != null && requestCode == SELECT_RECIPE_REQUEST_CODE) {
                long dayId = data.getLongExtra(RecipeSelectorActivity.ARG_DAY_ID, App.ARG_ERROR);
                int mealId = data.getIntExtra(RecipeSelectorActivity.ARG_MEAL_ID, App.ARG_ERROR);

                weeklyMenuFragment.addRecipe(dayId, mealId, resultCode);
                updateNutritionalInfoSummary();
            }
        }
    }

    public void updateNutritionalInfoSummary() {
        if (weeklyMenuFragment != null) {
            weeklyMenuFragment.updateNutritionalInfoSummary();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_weekly_menu_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.activity_weekly_menu_actions_generate_menu:
                new WeekSelectorDialogFragment(getString(R.string.generate_menu_for), true).show(getSupportFragmentManager(), null);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onAcceptClick(Bundle options) {
        long dayId = options.getLong(WeekSelectorDialogFragment.OPTION_SELECTED_WEEK, -1);
        boolean keepSelectedMeals = options.getBoolean(WeekSelectorDialogFragment.OPTION_KEEP_SELECTED_MEALS, false);

        weeklyMenuFragment.generateMenu(dayId, keepSelectedMeals);
    }
}
