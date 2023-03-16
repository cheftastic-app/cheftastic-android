package app.cheftastic.vanilla;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import app.cheftastic.R;
import app.cheftastic.vanilla.fragments.NutritionalInfoFragment;

public class NutritionalInfoActivity extends AppCompatActivity {
    public static final String ARG_DAY_OF_WEEK_ID = "nutritional_info_activity_day_of_week_id";
    NutritionalInfoFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() == null || getIntent().getExtras() == null || !getIntent().getExtras().containsKey(ARG_DAY_OF_WEEK_ID)) {
            super.onBackPressed();
        }

        setContentView(R.layout.activity_nutritional_info);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle args = new Bundle();
        args.putLong(NutritionalInfoFragment.ARG_DAY_OF_WEEK_ID, getIntent().getExtras().getLong(ARG_DAY_OF_WEEK_ID));
        fragment = new NutritionalInfoFragment();
        fragment.setArguments(args);
        fragment.restoreSavedStated(savedInstanceState);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_nutritional_info_container_fragment, fragment)
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putAll(fragment.getCurrentState());
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
