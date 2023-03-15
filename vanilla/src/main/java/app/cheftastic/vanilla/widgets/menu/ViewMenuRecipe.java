package app.cheftastic.vanilla.widgets.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import app.cheftastic.R;
import app.cheftastic.vanilla.CheftasticCalendar;
import app.cheftastic.vanilla.SQLiteHandler;
import app.cheftastic.vanilla.RecipeInfoActivity;
import app.cheftastic.vanilla.RecipeSelectorActivity;
import app.cheftastic.vanilla.WeeklyMenuActivity;
import app.cheftastic.vanilla.fragments.RecipeInfoFragment;
import app.cheftastic.vanilla.model.MenuRecipe;

public class ViewMenuRecipe extends FrameLayout {

    private static final int SELECT_RECIPE_REQUEST_CODE = 1;
    private MenuRecipe menuRecipe;
    private int recipeCategoryId;
    private String recipeCategoryName = "";
    private int mealId;
    private long dayId = 0;
    private ViewHolder viewHolder;
    private ViewMenuRecipeHandler handler;

    public ViewMenuRecipe(Context context) {
        this(context, null, 0);
    }

    public ViewMenuRecipe(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewMenuRecipe(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        viewHolder = new ViewHolder();

        if (context.getTheme() != null) {
            TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ViewMenuRecipe, 0, 0);

            if (attributes != null
                    && attributes.hasValue(R.styleable.ViewMenuRecipe_categoryId)
                    && attributes.hasValue(R.styleable.ViewMenuRecipe_categoryName)
                    && attributes.hasValue(R.styleable.ViewMenuRecipe_mealId)
                    ) {
                recipeCategoryId = attributes.getInt(R.styleable.ViewMenuRecipe_categoryId, 0);
                recipeCategoryName = attributes.getString(R.styleable.ViewMenuRecipe_categoryName);
                mealId = attributes.getInt(R.styleable.ViewMenuRecipe_mealId, 0);
                attributes.recycle();
            }
        }

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.list_item_view_menu, this);

            viewHolder.text = (TextView) findViewById(R.id.list_item_view_menu_recipe_name);
            viewHolder.button = (Button) findViewById(R.id.list_item_view_menu_add_button);

            setNoRecipeLayout();
        }
    }

    public void setDayId(long dayId) {
        this.dayId = dayId;
    }

    public void setHandler(ViewMenuRecipeHandler handler) {
        this.handler = handler;
    }

    public void addRecipe(MenuRecipe menuRecipe) {
        if (menuRecipe != null) {
            if (menuRecipe.getRecipeCategoryId() == recipeCategoryId && menuRecipe.getMealId() == mealId && menuRecipe.getDayId() == dayId) {
                this.menuRecipe = menuRecipe;

                if (this.menuRecipe.isRecipeAssigned())
                    setRecipeLayout();
                else {
                    setNoRecipeLayout();
                }
            }
        } else {
            removeRecipe();
        }
    }

    private void removeRecipe() {
        menuRecipe = null;
        setNoRecipeLayout();
    }

    private void deleteRecipe() {
        if (handler != null) {
            handler.onRemoveRecipe(menuRecipe.getMealId(), menuRecipe.getRecipeCategoryId());
        }

        removeRecipe();
        SQLiteHandler.deleteMenuRecipe(dayId, mealId, recipeCategoryId);
    }

    private void setNoRecipeLayout() {
        if (menuRecipe == null || !menuRecipe.isRecipeAssigned()) {
            viewHolder.text.setText(recipeCategoryName);
            viewHolder.text.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            if (getContext() != null) {
                viewHolder.text.setTextAppearance(getContext(), R.style.TextViewMenuRecipe_NoSelected);
            }

            if (isPreviousDay()) {
                viewHolder.button.setVisibility(Button.GONE);
            } else {
                viewHolder.button.setVisibility(Button.VISIBLE);
                viewHolder.button.setBackgroundResource(R.drawable.btn_add);
                viewHolder.button.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getContext() != null) {
                            Intent i = new Intent(getContext(), RecipeSelectorActivity.class);
                            i.putExtra(RecipeSelectorActivity.ARG_DAY_ID, dayId);
                            i.putExtra(RecipeSelectorActivity.ARG_MEAL_ID, mealId);
                            i.putExtra(RecipeSelectorActivity.ARG_RECIPE_CATEGORY_ID, recipeCategoryId);

                            if (getContext() instanceof Activity && getResources() != null) {
                                ((Activity) getContext()).startActivityForResult(i, SELECT_RECIPE_REQUEST_CODE);
                            }
                        }
                    }
                });
            }

        } else {
            setRecipeLayout();
        }
    }

    private void setRecipeLayout() {
        if (menuRecipe != null && menuRecipe.isRecipeAssigned()) {
            viewHolder.text.setText(menuRecipe.getRecipe().getName());
            viewHolder.text.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getContext() != null) {
                        Intent i = new Intent(getContext(), RecipeInfoActivity.class);
                        i.putExtra(RecipeInfoFragment.ARG_RECIPE_ID, menuRecipe.getRecipe().getId());
                        getContext().startActivity(i);
                    }

                }
            });
            if (getContext() != null) {
                if (!isPreviousDay()) {
                    viewHolder.text.setTextAppearance(getContext(), R.style.TextViewMenuRecipe_RecipeSelected);
                } else {
                    viewHolder.text.setTextAppearance(getContext(), R.style.TextViewMenuRecipe_RecipeSelected_PreviousDay);
                }
            }

            if (isPreviousDay()) {
                viewHolder.button.setVisibility(Button.GONE);
            } else {
                viewHolder.button.setVisibility(Button.VISIBLE);
                viewHolder.button.setBackgroundResource(R.drawable.btn_options);
                viewHolder.button.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getContext() != null) {
                            PopupMenu menu = new PopupMenu(getContext(), v);

                            menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    switch (item.getItemId()) {
                                        case R.id.menu_recipe_option_change:
                                            if (getContext() != null) {
                                                Intent i = new Intent(getContext(), RecipeSelectorActivity.class);
                                                i.putExtra(RecipeSelectorActivity.ARG_DAY_ID, dayId);
                                                i.putExtra(RecipeSelectorActivity.ARG_MEAL_ID, mealId);
                                                i.putExtra(RecipeSelectorActivity.ARG_RECIPE_CATEGORY_ID, recipeCategoryId);

                                                if (getContext() instanceof Activity && getResources() != null) {
                                                    ((Activity) getContext()).startActivityForResult(i, SELECT_RECIPE_REQUEST_CODE);
                                                }
                                            }
                                            return true;
                                        case R.id.menu_recipe_option_remove:
                                            if (getContext() instanceof WeeklyMenuActivity) {
                                                ((WeeklyMenuActivity) getContext()).updateNutritionalInfoSummary();
                                            }
                                            deleteRecipe();
                                            return true;
                                    }

                                    return false;
                                }
                            });

                            MenuInflater menuInflater = menu.getMenuInflater();
                            menuInflater.inflate(R.menu.menu_recipe_option_menu, menu.getMenu());
                            menu.show();
                        }
                    }
                });
            }

        } else {
            setNoRecipeLayout();
        }
    }

    private boolean isPreviousDay() {
        return dayId < new CheftasticCalendar().getDayId();
    }

    public interface ViewMenuRecipeHandler {

        public void onRemoveRecipe(int mealId, int recipeCategoryId);

    }

    private class ViewHolder {
        TextView text;
        Button button;
    }
}
