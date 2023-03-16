package app.cheftastic.vanilla;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import app.cheftastic.R;
import app.cheftastic.vanilla.fragments.RecipeInfoFragment;

public class RecipeInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe_info);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        int recipeId = i.getIntExtra(RecipeInfoFragment.ARG_RECIPE_ID, RecipeInfoFragment.ERROR_RECIPE_ID);

        if (recipeId == RecipeInfoFragment.ERROR_RECIPE_ID) {
            finish();
        } else {
            RecipeInfoFragment recipeInfoFragment = new RecipeInfoFragment();
            Bundle args = new Bundle();
            args.putInt(RecipeInfoFragment.ARG_RECIPE_ID, recipeId);
            recipeInfoFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_recipe_info_fragment_recipe_info_container, recipeInfoFragment)
                    .commit();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

    @Override
    public boolean onNavigateUp() {
        return onSupportNavigateUp();
    }
}
