package app.cheftastic.vanilla.widgets.menu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import app.cheftastic.R;
import app.cheftastic.vanilla.CheftasticCalendar;
import app.cheftastic.vanilla.model.MenuRecipe;
import app.cheftastic.vanilla.model.RecipeCategory;
import app.cheftastic.vanilla.model.DailyMenu;

public class ViewMenu extends FrameLayout
        implements ViewMenuRecipe.ViewMenuRecipeHandler {

    private DailyMenu menu;
    private ViewHolder viewHolder;

    public ViewMenu(Context context) {
        this(context, null);
    }

    public ViewMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.view_menu, this);
        }

        viewHolder = new ViewHolder();
        viewHolder.dayName = (TextView) findViewById(R.id.view_menu_day_text_view);
        viewHolder.scrollView = (ScrollView) findViewById(R.id.view_menu_scroll_view);
        viewHolder.container = (LinearLayout) findViewById(R.id.view_menu_linear_layout);
        viewHolder.progressBar = (ProgressBar) findViewById(R.id.view_menu_progress_bar);
        viewHolder.lunchFirstCourse = (ViewMenuRecipe) findViewById(R.id.view_menu_lunch_starter);
        viewHolder.lunchSecondCourse = (ViewMenuRecipe) findViewById(R.id.view_menu_lunch_principal);
        viewHolder.lunchDessert = (ViewMenuRecipe) findViewById(R.id.view_menu_lunch_dessert);
        viewHolder.dinnerFirstCourse = (ViewMenuRecipe) findViewById(R.id.view_menu_diner_starter);
        viewHolder.dinnerSecondCourse = (ViewMenuRecipe) findViewById(R.id.view_menu_diner_main);
        viewHolder.dinnerDessert = (ViewMenuRecipe) findViewById(R.id.view_menu_diner_dessert);

        viewHolder.scrollView.setSmoothScrollingEnabled(false);

        viewHolder.lunchFirstCourse.setHandler(this);
        viewHolder.lunchSecondCourse.setHandler(this);
        viewHolder.lunchDessert.setHandler(this);
        viewHolder.dinnerFirstCourse.setHandler(this);
        viewHolder.dinnerSecondCourse.setHandler(this);
        viewHolder.dinnerDessert.setHandler(this);
    }

    public void setDayId(long dayId) {
        viewHolder.updateDayId(dayId);

        viewHolder.dayName.setText(new CheftasticCalendar(dayId).getDayOfWeekName().toUpperCase());

        viewHolder.progressBar.setVisibility(ProgressBar.VISIBLE);
        viewHolder.container.setVisibility(LinearLayout.GONE);
        viewHolder.scrollView.fullScroll(ScrollView.FOCUS_UP);
    }

    public void setDayId(long dayId, DailyMenu dailyMenu) {
        this.menu = dailyMenu;

        viewHolder.updateDayId(dayId);

        setDayName(new CheftasticCalendar(dayId).getDayOfWeekName());

        if (menu != null) {
            updateView();
        } else {
            setDayId(dayId);
        }

        viewHolder.scrollView.fullScroll(ScrollView.FOCUS_UP);
    }

    public void updateView() {
        if (menu != null) {
            viewHolder.lunchFirstCourse.addRecipe(menu.getRecipe(MenuRecipe.LUNCH, RecipeCategory.FIRST_COURSE));
            viewHolder.lunchSecondCourse.addRecipe(menu.getRecipe(MenuRecipe.LUNCH, RecipeCategory.SECOND_COURSE));
            viewHolder.lunchDessert.addRecipe(menu.getRecipe(MenuRecipe.LUNCH, RecipeCategory.DESSERT));
            viewHolder.dinnerFirstCourse.addRecipe(menu.getRecipe(MenuRecipe.DINNER, RecipeCategory.FIRST_COURSE));
            viewHolder.dinnerSecondCourse.addRecipe(menu.getRecipe(MenuRecipe.DINNER, RecipeCategory.SECOND_COURSE));
            viewHolder.dinnerDessert.addRecipe(menu.getRecipe(MenuRecipe.DINNER, RecipeCategory.DESSERT));

            viewHolder.progressBar.setVisibility(ProgressBar.GONE);
            viewHolder.container.setVisibility(LinearLayout.VISIBLE);
        }
    }

    public String getDayName() {
        if (viewHolder.dayName.getText() != null) {
            return viewHolder.dayName.getText().toString();
        }

        return "";
    }

    public void setDayName(String dayName) {
        viewHolder.dayName.setText(dayName.toUpperCase());
    }

    public DailyMenu getMenu() {
        return menu;
    }

    @Override
    public void onRemoveRecipe(int mealId, int recipeCategoryId) {
        menu.removeRecipe(mealId, recipeCategoryId);
    }

    private class ViewHolder {
        TextView dayName;
        ScrollView scrollView;
        ProgressBar progressBar;
        LinearLayout container;
        ViewMenuRecipe lunchFirstCourse;
        ViewMenuRecipe lunchSecondCourse;
        ViewMenuRecipe lunchDessert;
        ViewMenuRecipe dinnerFirstCourse;
        ViewMenuRecipe dinnerSecondCourse;
        ViewMenuRecipe dinnerDessert;

        private void updateDayId(long dayId) {
            lunchFirstCourse.setDayId(dayId);
            lunchSecondCourse.setDayId(dayId);
            lunchDessert.setDayId(dayId);
            dinnerFirstCourse.setDayId(dayId);
            dinnerSecondCourse.setDayId(dayId);
            dinnerDessert.setDayId(dayId);
        }
    }
}