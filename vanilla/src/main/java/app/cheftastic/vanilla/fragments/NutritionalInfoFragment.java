package app.cheftastic.vanilla.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import app.cheftastic.R;
import app.cheftastic.vanilla.model.NutritionalGroup;
import app.cheftastic.vanilla.model.WeeklyMenu;
import app.cheftastic.vanilla.widgets.CircularGraph;

public class NutritionalInfoFragment extends Fragment {

    public static final String ARG_DAY_OF_WEEK_ID = "nutritional_info_fragment_day_of_week_id";

    private static final String STATE_PERCENTAGES_TARGET = "nutritional_info_fragment_state_percentages_target";
    private static final String STATE_PERCENTAGES_SAMPLE = "nutritional_info_fragment_state_percentages_sample";
    private static final String STATE_NUTRITIONAL_SCORE = "nutritional_info_fragment_state_nutritional_score";
    private static final String STATE_GROUP_NAMES = "nutritional_info_fragment_state_group_names";
    private static final String STATE_GROUP_DESCRIPTIONS = "nutritional_info_fragment_state_group_descriptions";

    private double[] percentagesTarget;
    private double[] percentagesSample;
    private double nutritionalScore;
    private String[] groupNames;
    private String[] groupDescriptions;

    private boolean isStateRestored = false;

    private ViewHolder viewHolder;
    private NutritionalInfoLoader nutritionalInfoLoader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        restoreSavedStated(savedInstanceState);

        return inflater.inflate(R.layout.fragment_nutritional_info, container, false);
    }

    @Override
    public void onStop() {
        super.onStop();

        if (nutritionalInfoLoader != null) {
            nutritionalInfoLoader.cancel(true);
            nutritionalInfoLoader = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        viewHolder = new ViewHolder();
        viewHolder.nutritionalGroups = new ViewHolderNutritionalGroupInfo[NutritionalGroup.getNumberOfNutritionalGroups()];
        viewHolder.loadProgress = (ProgressBar) getActivity().findViewById(R.id.fragment_nutritional_info_progress_bar);
        viewHolder.content = (LinearLayout) getActivity().findViewById(R.id.fragment_nutritional_info_container);
        viewHolder.graph = (CircularGraph) getActivity().findViewById(R.id.fragment_nutritional_info_graph);
        viewHolder.nutritionalScore = (TextView) getActivity().findViewById(R.id.fragment_nutritional_info_percentage);
        viewHolder.nutritionalScoreDescription = (TextView) getActivity().findViewById(R.id.fragment_nutritional_info_description);
        viewHolder.nutritionalGroups[0] = new ViewHolderNutritionalGroupInfo();
        viewHolder.nutritionalGroups[0].name = (TextView) getActivity().findViewById(R.id.fragment_nutritional_info_group_1_name);
        viewHolder.nutritionalGroups[0].description = (TextView) getActivity().findViewById(R.id.fragment_nutritional_info_group_1_description);
        viewHolder.nutritionalGroups[0].percentage = (TextView) getActivity().findViewById(R.id.fragment_nutritional_info_group_1_percentage);
        viewHolder.nutritionalGroups[0].deviation = (TextView) getActivity().findViewById(R.id.fragment_nutritional_info_group_1_deviation);
        viewHolder.nutritionalGroups[1] = new ViewHolderNutritionalGroupInfo();
        viewHolder.nutritionalGroups[1].name = (TextView) getActivity().findViewById(R.id.fragment_nutritional_info_group_2_name);
        viewHolder.nutritionalGroups[1].description = (TextView) getActivity().findViewById(R.id.fragment_nutritional_info_group_2_description);
        viewHolder.nutritionalGroups[1].percentage = (TextView) getActivity().findViewById(R.id.fragment_nutritional_info_group_2_percentage);
        viewHolder.nutritionalGroups[1].deviation = (TextView) getActivity().findViewById(R.id.fragment_nutritional_info_group_2_deviation);
        viewHolder.nutritionalGroups[2] = new ViewHolderNutritionalGroupInfo();
        viewHolder.nutritionalGroups[2].name = (TextView) getActivity().findViewById(R.id.fragment_nutritional_info_group_3_name);
        viewHolder.nutritionalGroups[2].description = (TextView) getActivity().findViewById(R.id.fragment_nutritional_info_group_3_description);
        viewHolder.nutritionalGroups[2].percentage = (TextView) getActivity().findViewById(R.id.fragment_nutritional_info_group_3_percentage);
        viewHolder.nutritionalGroups[2].deviation = (TextView) getActivity().findViewById(R.id.fragment_nutritional_info_group_3_deviation);
        viewHolder.nutritionalGroups[3] = new ViewHolderNutritionalGroupInfo();
        viewHolder.nutritionalGroups[3].name = (TextView) getActivity().findViewById(R.id.fragment_nutritional_info_group_4_name);
        viewHolder.nutritionalGroups[3].description = (TextView) getActivity().findViewById(R.id.fragment_nutritional_info_group_4_description);
        viewHolder.nutritionalGroups[3].percentage = (TextView) getActivity().findViewById(R.id.fragment_nutritional_info_group_4_percentage);
        viewHolder.nutritionalGroups[3].deviation = (TextView) getActivity().findViewById(R.id.fragment_nutritional_info_group_4_deviation);
        viewHolder.nutritionalGroups[4] = new ViewHolderNutritionalGroupInfo();
        viewHolder.nutritionalGroups[4].name = (TextView) getActivity().findViewById(R.id.fragment_nutritional_info_group_5_name);
        viewHolder.nutritionalGroups[4].description = (TextView) getActivity().findViewById(R.id.fragment_nutritional_info_group_5_description);
        viewHolder.nutritionalGroups[4].percentage = (TextView) getActivity().findViewById(R.id.fragment_nutritional_info_group_5_percentage);
        viewHolder.nutritionalGroups[4].deviation = (TextView) getActivity().findViewById(R.id.fragment_nutritional_info_group_5_deviation);
        viewHolder.nutritionalGroups[5] = new ViewHolderNutritionalGroupInfo();
        viewHolder.nutritionalGroups[5].name = (TextView) getActivity().findViewById(R.id.fragment_nutritional_info_group_6_name);
        viewHolder.nutritionalGroups[5].description = (TextView) getActivity().findViewById(R.id.fragment_nutritional_info_group_6_description);
        viewHolder.nutritionalGroups[5].percentage = (TextView) getActivity().findViewById(R.id.fragment_nutritional_info_group_6_percentage);
        viewHolder.nutritionalGroups[5].deviation = (TextView) getActivity().findViewById(R.id.fragment_nutritional_info_group_6_deviation);

        if (!isStateRestored) {
            if (nutritionalInfoLoader != null) {
                nutritionalInfoLoader.cancel(true);
            }

            viewHolder.content.setVisibility(LinearLayout.GONE);
            viewHolder.loadProgress.setVisibility(ProgressBar.VISIBLE);

            Bundle args = getArguments();
            if (args != null && args.containsKey(ARG_DAY_OF_WEEK_ID)) {
                nutritionalInfoLoader = new NutritionalInfoLoader();
                nutritionalInfoLoader.execute(args.getLong(ARG_DAY_OF_WEEK_ID));
            }
        } else {
            setValues();
            isStateRestored = false;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putAll(getCurrentState());
    }

    public Bundle getCurrentState() {
        Bundle state = new Bundle();

        if (nutritionalInfoLoader == null) {
            state.putDoubleArray(STATE_PERCENTAGES_TARGET, percentagesTarget);
            state.putDoubleArray(STATE_PERCENTAGES_SAMPLE, percentagesSample);
            state.putStringArray(STATE_GROUP_NAMES, groupNames);
            state.putStringArray(STATE_GROUP_DESCRIPTIONS, groupDescriptions);
            state.putDouble(STATE_NUTRITIONAL_SCORE, nutritionalScore);
        }

        return state;
    }

    public void restoreSavedStated(Bundle state) {
        if (state != null
                && state.containsKey(STATE_PERCENTAGES_TARGET)
                && state.containsKey(STATE_PERCENTAGES_SAMPLE)
                && state.containsKey(STATE_NUTRITIONAL_SCORE)
                && state.containsKey(STATE_GROUP_NAMES)
                && state.containsKey(STATE_GROUP_DESCRIPTIONS)) {

            percentagesTarget = state.getDoubleArray(STATE_PERCENTAGES_TARGET);
            percentagesSample = state.getDoubleArray(STATE_PERCENTAGES_SAMPLE);
            nutritionalScore = state.getDouble(STATE_NUTRITIONAL_SCORE);
            groupNames = state.getStringArray(STATE_GROUP_NAMES);
            groupDescriptions = state.getStringArray(STATE_GROUP_DESCRIPTIONS);
            isStateRestored = true;
        }
    }

    private void setValues() {
        viewHolder.graph.setPercentages(percentagesTarget, percentagesSample);

        viewHolder.nutritionalScore.setText(Math.round(nutritionalScore * 100) + "%");
        int nutritionalCategory = WeeklyMenu.getNutritionalScore(nutritionalScore);
        switch (nutritionalCategory) {
            case WeeklyMenu.VALUE_VERY_LOW:
                viewHolder.nutritionalScore.setTextColor(getResources().getColor(R.color.red_dark));
                viewHolder.nutritionalScoreDescription.setTextColor(getResources().getColor(R.color.red_dark));
                viewHolder.nutritionalScoreDescription.setText(getResources().getString(R.string.very_low_nutritional_scoring));
                break;

            case WeeklyMenu.VALUE_LOW:
                viewHolder.nutritionalScore.setTextColor(getResources().getColor(R.color.orange_dark));
                viewHolder.nutritionalScoreDescription.setTextColor(getResources().getColor(R.color.orange_dark));
                viewHolder.nutritionalScoreDescription.setText(getResources().getString(R.string.low_nutritional_scoring));
                break;

            case WeeklyMenu.VALUE_MEDIUM:
                viewHolder.nutritionalScore.setTextColor(getResources().getColor(R.color.yellow_dark));
                viewHolder.nutritionalScoreDescription.setTextColor(getResources().getColor(R.color.yellow_dark));
                viewHolder.nutritionalScoreDescription.setText(getResources().getString(R.string.medium_nutritional_scoring));
                break;

            case WeeklyMenu.VALUE_HIGH:
                viewHolder.nutritionalScore.setTextColor(getResources().getColor(R.color.green_dark));
                viewHolder.nutritionalScoreDescription.setTextColor(getResources().getColor(R.color.green_dark));
                viewHolder.nutritionalScoreDescription.setText(getResources().getString(R.string.high_nutritional_scoring));
                break;

            case WeeklyMenu.VALUE_VERY_HIGH:
                viewHolder.nutritionalScore.setTextColor(getResources().getColor(R.color.emerald_dark));
                viewHolder.nutritionalScoreDescription.setTextColor(getResources().getColor(R.color.emerald_dark));
                viewHolder.nutritionalScoreDescription.setText(getResources().getString(R.string.very_high_nutritional_scoring));
                break;
        }

        for (int i = 0; i < groupNames.length; i++) {
            viewHolder.nutritionalGroups[i].name.setText(groupNames[i]);
            viewHolder.nutritionalGroups[i].description.setText(groupDescriptions[i]);
            viewHolder.nutritionalGroups[i].percentage.setText(((double) Math.round(percentagesSample[i] * 1000)) / 10d + "%");
            double deviation = (Math.round(percentagesSample[i] * 1000) - percentagesTarget[i] * 1000) / 10;
            String sign = (deviation > 0d) ? "+" : "";
            viewHolder.nutritionalGroups[i].deviation.setText(sign + deviation + "%");
        }

        viewHolder.content.setVisibility(LinearLayout.VISIBLE);
        viewHolder.loadProgress.setVisibility(ProgressBar.GONE);

        if (nutritionalScore == 0d) {
            viewHolder.nutritionalScoreDescription.setVisibility(TextView.GONE);
        } else {
            viewHolder.nutritionalScoreDescription.setVisibility(TextView.VISIBLE);
        }
    }

    private class ViewHolder {
        ProgressBar loadProgress;
        LinearLayout content;
        CircularGraph graph;
        TextView nutritionalScore;
        TextView nutritionalScoreDescription;
        ViewHolderNutritionalGroupInfo[] nutritionalGroups;
    }

    private class ViewHolderNutritionalGroupInfo {
        TextView name;
        TextView description;
        TextView percentage;
        TextView deviation;
    }

    private class NutritionalInfoLoader extends AsyncTask<Long, Void, Void> {
        WeeklyMenu menu;
        NutritionalGroup[] nutritionalGroups;

        boolean loadSuccess = false;

        @Override
        protected Void doInBackground(Long... params) {
            if (params.length != 1) {
                return null;
            }
            menu = new WeeklyMenu(params[0]);

            nutritionalGroups = new NutritionalGroup[NutritionalGroup.getNumberOfNutritionalGroups()];
            for (int i = 0; i < nutritionalGroups.length; i++) {
                nutritionalGroups[i] = NutritionalGroup.retrieveNutritionalGroup(i + 1);
            }

            percentagesTarget = new double[nutritionalGroups.length];
            groupNames = new String[nutritionalGroups.length];
            groupDescriptions = new String[nutritionalGroups.length];
            percentagesSample = menu.getNutritionalInfoPercentages();
            for (int i = 0; i < percentagesTarget.length; i++) {
                percentagesTarget[i] = nutritionalGroups[i].getRecommendedPercentage();
                groupNames[i] = nutritionalGroups[i].getName();
                groupDescriptions[i] = nutritionalGroups[i].getDetails();
            }

            nutritionalScore = menu.getNutritionalScore();

            loadSuccess = true;
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (loadSuccess && !isCancelled()) {
                viewHolder.graph.setPercentagesSample(percentagesTarget);

                setValues();

                nutritionalInfoLoader = null;
            }
        }
    }
}
