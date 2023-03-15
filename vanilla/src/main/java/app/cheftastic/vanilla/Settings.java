package app.cheftastic.vanilla;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public abstract class Settings {
    private static final Context CONTEXT = App.getContext();
    private static final String NUMBER_OF_PEOPLE = "number_of_people";
    private static final String KEEP_SELECTED_RECIPES = "keep_selected_recipes";
    private static final String USE_ONLY_WIFI = "use_only_wifi";

    private static int numberOfPeople;
    private static boolean keepSelectedRecipes;
    private static boolean useOnlyWifi;

    private static void updateValues() {
        numberOfPeople = PreferenceManager.getDefaultSharedPreferences(CONTEXT).getInt(NUMBER_OF_PEOPLE, 1);
        keepSelectedRecipes = PreferenceManager.getDefaultSharedPreferences(CONTEXT).getBoolean(KEEP_SELECTED_RECIPES, false);
        useOnlyWifi = PreferenceManager.getDefaultSharedPreferences(CONTEXT).getBoolean(USE_ONLY_WIFI, false);
    }

    public static int getNumberOfPeople() {
        updateValues();
        return numberOfPeople;
    }

    public static boolean getKeepSelectedRecipes() {
        updateValues();
        return keepSelectedRecipes;
    }

    public static boolean getUseOnlyWifi() {
        updateValues();
        return useOnlyWifi;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void updateSummaries(PreferenceFragment pf) {
        updateValues();

        Preference p;
        p = pf.findPreference(NUMBER_OF_PEOPLE);
        if (p != null) {
            p.setSummary("Cocinaremos para " + numberOfPeople + " " + ((numberOfPeople == 1) ? "persona" : "personas"));
        }
    }

    @SuppressWarnings("deprecation")
    public static void updateSummaries(PreferenceActivity pa) {
        updateValues();

        Preference p;
        p = pa.findPreference(NUMBER_OF_PEOPLE);
        if (p != null) {
            p.setSummary("Cocinaremos para " + numberOfPeople + " " + ((numberOfPeople == 1) ? "persona" : "personas"));
        }
    }
}
