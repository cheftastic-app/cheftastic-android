package app.cheftastic.vanilla;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import app.cheftastic.R;
import app.cheftastic.vanilla.exceptions.ArgumentExpectedException;
import app.cheftastic.vanilla.fragments.RecipeInfoFragment;
import app.cheftastic.vanilla.fragments.RecipeListFragment;

public class RecipeSelectorActivity extends ActionBarActivity
        implements RecipeListFragment.OnItemSelectedListener {

    public static final String ARG_DAY_ID = "recipe_selector_activity_arg_day_id";
    public static final String ARG_MEAL_ID = "recipe_selector_activity_arg_meal_id";
    public static final String ARG_RECIPE_CATEGORY_ID = "recipe_selector_activity_arg_recipe_category_id";
    private static final String STATE_DISPLAYING_RECIPE = "recipe_selector_activity_state_displaying_recipe";
    ActionMode mActionMode;
    private boolean isDisplayingRecipe;
    private boolean isDisplayingTwoColumns;
    private boolean isBackPressed = false;
    private RecipeListFragment recipeListFragment;
    private RecipeInfoFragment recipeInfoFragment;
    private long dayId;
    private int mealId;
    private int categoryRecipeId;
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.setTitle("¿Agregar esta receta?");
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;

            if (isBackPressed) {
                isBackPressed = false;
            } else {
                Intent i = new Intent();
                i.putExtra(ARG_DAY_ID, dayId);
                i.putExtra(ARG_MEAL_ID, mealId);
                i.putExtra(ARG_RECIPE_CATEGORY_ID, categoryRecipeId);
                setResult(recipeInfoFragment.getRecipeId(), i);
                finish();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe_picker);

        isDisplayingRecipe = savedInstanceState != null && savedInstanceState.containsKey(STATE_DISPLAYING_RECIPE) && savedInstanceState.getBoolean(STATE_DISPLAYING_RECIPE);

        isDisplayingTwoColumns = (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE &&
                getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        Bundle intentExtras = getIntent().getExtras();
        if (intentExtras == null || !intentExtras.containsKey(ARG_DAY_ID) || !intentExtras.containsKey(ARG_MEAL_ID) || !intentExtras.containsKey(ARG_RECIPE_CATEGORY_ID)) {
            try {
                throw new ArgumentExpectedException("Se debe indicar el día, la comida y la categoría de plato a cargar en el listado");
            } catch (ArgumentExpectedException e) {
                e.printStackTrace();
                finish();
            }
            finish();
        } else {
            dayId = intentExtras.getLong(ARG_DAY_ID);
            mealId = intentExtras.getInt(ARG_MEAL_ID);
            categoryRecipeId = intentExtras.getInt(ARG_RECIPE_CATEGORY_ID);

            recipeListFragment = new RecipeListFragment();
            Bundle args = new Bundle();
            args.putInt(RecipeListFragment.ARG_RECIPE_CATEGORY, categoryRecipeId);
            recipeListFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_recipe_picker_fragment_recipe_list_container, recipeListFragment)
                    .commit();
        }

        this.setResult(RESULT_CANCELED);

        configureUi();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_DISPLAYING_RECIPE, isDisplayingRecipe);
    }

    @Override
    public void onBackPressed() {
        if (isDisplayingRecipe) {
            isDisplayingRecipe = false;
            configureUi();

            if (recipeInfoFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .remove(recipeInfoFragment)
                        .commit();

                recipeInfoFragment = null;
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigateUp() {
        return onSupportNavigateUp();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void configureUi() {
        if (isDisplayingTwoColumns) {
            FrameLayout recipeListContainer = (FrameLayout) findViewById(R.id.activity_recipe_picker_fragment_recipe_list_container);
            if (recipeListContainer != null) {
                recipeListContainer.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 2f));
            }

            FrameLayout recipeInfoContainer = (FrameLayout) findViewById(R.id.activity_recipe_picker_fragment_recipe_info_container);
            if (recipeInfoContainer != null) {
                recipeInfoContainer.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 3f));
            }
        } else if (isDisplayingRecipe) {
            FrameLayout recipeListContainer = (FrameLayout) findViewById(R.id.activity_recipe_picker_fragment_recipe_list_container);
            if (recipeListContainer != null) {
                recipeListContainer.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0f));
            }

            FrameLayout recipeInfoContainer = (FrameLayout) findViewById(R.id.activity_recipe_picker_fragment_recipe_info_container);
            if (recipeInfoContainer != null) {
                recipeInfoContainer.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
            }
        } else {
            FrameLayout recipeListContainer = (FrameLayout) findViewById(R.id.activity_recipe_picker_fragment_recipe_list_container);
            if (recipeListContainer != null) {
                recipeListContainer.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
            }

            FrameLayout recipeInfoContainer = (FrameLayout) findViewById(R.id.activity_recipe_picker_fragment_recipe_info_container);
            if (recipeInfoContainer != null) {
                recipeInfoContainer.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0f));
            }
        }

        if (isDisplayingRecipe) {
            if (mActionMode == null)
                mActionMode = startSupportActionMode(mActionModeCallback);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (mActionMode != null) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                isBackPressed = true;
                mActionMode.finish();
                onBackPressed();
                return true;
            }
        }

        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onItemSelected(int id) {
        if (recipeInfoFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(recipeInfoFragment)
                    .commit();
        }

        recipeInfoFragment = new RecipeInfoFragment();
        Bundle args = new Bundle();
        args.putInt(RecipeInfoFragment.ARG_RECIPE_ID, id);
        recipeInfoFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_recipe_picker_fragment_recipe_info_container, recipeInfoFragment)
                .commit();

        isDisplayingRecipe = true;
        configureUi();
    }
}