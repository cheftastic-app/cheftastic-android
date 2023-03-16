package app.cheftastic.vanilla;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TabHost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import app.cheftastic.R;
import app.cheftastic.vanilla.fragments.NavBarFragment;
import app.cheftastic.vanilla.fragments.RecipeInfoFragment;
import app.cheftastic.vanilla.fragments.RecipeListFragment;

public class RecipeSearchActivity extends AppCompatActivity
        implements RecipeListFragment.OnItemSelectedListener {

    private static final String STATE_DISPLAYING_RECIPE = "recipe_search_activity_state_displaying_recipe";

    private int[] recipeCategoryIds;

    private HorizontalScrollView tabHorizontalScrollView;
    private TabHost categoryTabs;
    private ViewPager recipeListsViewPager;
    private RecipeListsAdapter recipeListsAdapter;
    private RecipeInfoFragment recipeInfoFragment;

    private int selectedTab;
    private boolean isDisplayingRecipe;
    private boolean isDisplayingTwoColumns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe_search);

        NavBarFragment navBarFragment;
        tabHorizontalScrollView = (HorizontalScrollView) findViewById(R.id.activity_recipe_search_horizontalScrollView);
        categoryTabs = (TabHost) findViewById(R.id.activity_recipe_search_tabs);
        recipeListsViewPager = (ViewPager) findViewById(R.id.activity_recipe_search_view_pager);
        navBarFragment = (NavBarFragment) getSupportFragmentManager().findFragmentById(R.id.activity_recipe_search_fragment_nav_bar);
        selectedTab = 0;

        recipeCategoryIds = SQLiteHandler.getCategoryIdsWithRecipes();

        navBarFragment.setup(
                R.id.activity_recipe_search_fragment_nav_bar,
                (DrawerLayout) findViewById(R.id.activity_recipe_search_drawer_layout),
                NavBarFragment.OPTION_RECIPE_LIST);

        isDisplayingRecipe = savedInstanceState != null && savedInstanceState.containsKey(STATE_DISPLAYING_RECIPE) && savedInstanceState.getBoolean(STATE_DISPLAYING_RECIPE);

        isDisplayingTwoColumns = (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE &&
                getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        configureUi();

        recipeListsAdapter = new RecipeListsAdapter(getSupportFragmentManager());
        recipeListsViewPager.setAdapter(recipeListsAdapter);
        recipeListsViewPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        categoryTabs.setCurrentTab(position);

                        tabHorizontalScrollView.scrollBy(
                                (position - selectedTab) * (tabHorizontalScrollView.getWidth() / recipeListsAdapter.getCount()),
                                0);

                        selectedTab = position;
                    }
                }
        );

        tabHorizontalScrollView.setSmoothScrollingEnabled(true);
        categoryTabs.setOnTabChangedListener(
                new TabHost.OnTabChangeListener() {
                    @Override
                    public void onTabChanged(String tabId) {
                        recipeListsViewPager.setCurrentItem(categoryTabs.getCurrentTab());
                    }
                }
        );

        categoryTabs.setup();
        for (int i = 0; i < recipeListsAdapter.getCount(); i++) {
            String recipeCategoryName = SQLiteHandler.retrieveRecipeCategory(recipeCategoryIds[i]).getName();
            TabHost.TabSpec newTab = categoryTabs.newTabSpec(recipeCategoryName);
            newTab.setIndicator(recipeCategoryName);
            newTab.setContent(android.R.id.tabcontent);
            categoryTabs.addTab(newTab);
        }

        if (categoryTabs.getTabWidget() != null) {
            for (int i = 0; i < categoryTabs.getTabWidget().getTabCount(); i++) {
                categoryTabs.getTabWidget().getChildTabViewAt(i).setBackgroundResource(R.drawable.ab_tabs);
            }
        }

        recipeListsViewPager.setCurrentItem(1);
        recipeListsViewPager.setCurrentItem(0);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(STATE_DISPLAYING_RECIPE, isDisplayingRecipe);
    }

    @Override
    public void onBackPressed() {
        if (isDisplayingRecipe && !isDisplayingTwoColumns) {
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

    private void configureUi() {
        if (isDisplayingTwoColumns) {
            categoryTabs.setVisibility(TabHost.VISIBLE);

            FrameLayout recipeListContainer = (FrameLayout) findViewById(R.id.activity_recipe_search_fragment_recipe_list_container);
            if (recipeListContainer != null) {
                recipeListContainer.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 2f));
            }

            FrameLayout recipeInfoContainer = (FrameLayout) findViewById(R.id.activity_recipe_search_fragment_recipe_info_container);
            if (recipeInfoContainer != null) {
                recipeInfoContainer.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 3f));
            }
        } else if (isDisplayingRecipe) {
            categoryTabs.setVisibility(TabHost.GONE);

            FrameLayout recipeListContainer = (FrameLayout) findViewById(R.id.activity_recipe_search_fragment_recipe_list_container);
            if (recipeListContainer != null) {
                recipeListContainer.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0f));
            }

            FrameLayout recipeInfoContainer = (FrameLayout) findViewById(R.id.activity_recipe_search_fragment_recipe_info_container);
            if (recipeInfoContainer != null) {
                recipeInfoContainer.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
            }
        } else {
            categoryTabs.setVisibility(TabHost.VISIBLE);

            FrameLayout recipeListContainer = (FrameLayout) findViewById(R.id.activity_recipe_search_fragment_recipe_list_container);
            if (recipeListContainer != null) {
                recipeListContainer.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f));
            }

            FrameLayout recipeInfoContainer = (FrameLayout) findViewById(R.id.activity_recipe_search_fragment_recipe_info_container);
            if (recipeInfoContainer != null) {
                recipeInfoContainer.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0f));
            }
        }
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
                .replace(R.id.activity_recipe_search_fragment_recipe_info_container, recipeInfoFragment)
                .commit();

        isDisplayingRecipe = true;
        configureUi();
    }

    private class RecipeListsAdapter extends FragmentPagerAdapter {
        public RecipeListsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment f = new RecipeListFragment();
            Bundle args = new Bundle();
            args.putInt(RecipeListFragment.ARG_RECIPE_CATEGORY, recipeCategoryIds[i]);
            f.setArguments(args);
            return f;
        }

        @Override
        public int getCount() {
            return recipeCategoryIds.length;
        }
    }
}