package app.cheftastic.vanilla.widgets;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import app.cheftastic.R;
import app.cheftastic.vanilla.CheftasticCalendar;
import app.cheftastic.vanilla.model.NutritionalGroup;
import app.cheftastic.vanilla.model.WeeklyMenu;

public class ViewNutritionalInfoSummary extends FrameLayout {
    private static final String STATE_PERCENTAGES_TARGET = "view_nutritional_info_summary_state_percentages_target";
    private static final String STATE_PERCENTAGES_SAMPLE = "view_nutritional_info_summary_state_percentages_sample";
    private static final String STATE_NUTRITIONAL_SCORE = "view_nutritional_info_summary_state_nutritional_score";
    private static final String STATE_GROUP_NAMES = "view_nutritional_info_summary_state_group_names";
    private static final String STATE_GRAPH_INITIALIZED = "view_nutritional_info_summary_state_graph_initialized";
    NutritionalInfoLoader nutritionalInfoLoader;
    Handler handler;
    private ViewHolder viewHolder;
    private boolean isGraphInitialized = false;
    private double[] percentagesTarget;
    private double[] percentagesSample;
    private double nutritionalScore;
    private String[] groupNames;
    private long dayOfWeekId = 0l;

    public ViewNutritionalInfoSummary(Context context) {
        this(context, null, 0);
    }

    public ViewNutritionalInfoSummary(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewNutritionalInfoSummary(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_nutritional_info_summary, this);

        if (findViewById(R.id.view_nutritional_info_summary_graph) != null) {
            viewHolder = new ViewHolderLargeLand();
            ViewHolderLargeLand viewHolderLargeLand = (ViewHolderLargeLand) viewHolder;
            viewHolderLargeLand.contentContainer = findViewById(R.id.view_nutritional_info_summary_container_content);
            viewHolderLargeLand.graph = (CircularGraph) findViewById(R.id.view_nutritional_info_summary_graph);
            viewHolderLargeLand.nutritionalGroups = new NutritionalGroupInfoViewHolder[NutritionalGroup.getNumberOfNutritionalGroups()];
            viewHolderLargeLand.nutritionalGroups[0] = new NutritionalGroupInfoViewHolder();
            viewHolderLargeLand.nutritionalGroups[0].name = (TextView) findViewById(R.id.view_nutritional_info_summary_group_1_name);
            viewHolderLargeLand.nutritionalGroups[0].percentage = (TextView) findViewById(R.id.view_nutritional_info_summary_group_1_percentage);
            viewHolderLargeLand.nutritionalGroups[0].deviation = (TextView) findViewById(R.id.view_nutritional_info_summary_group_1_deviation);
            viewHolderLargeLand.nutritionalGroups[1] = new NutritionalGroupInfoViewHolder();
            viewHolderLargeLand.nutritionalGroups[1].name = (TextView) findViewById(R.id.view_nutritional_info_summary_group_2_name);
            viewHolderLargeLand.nutritionalGroups[1].percentage = (TextView) findViewById(R.id.view_nutritional_info_summary_group_2_percentage);
            viewHolderLargeLand.nutritionalGroups[1].deviation = (TextView) findViewById(R.id.view_nutritional_info_summary_group_2_deviation);
            viewHolderLargeLand.nutritionalGroups[2] = new NutritionalGroupInfoViewHolder();
            viewHolderLargeLand.nutritionalGroups[2].name = (TextView) findViewById(R.id.view_nutritional_info_summary_group_3_name);
            viewHolderLargeLand.nutritionalGroups[2].percentage = (TextView) findViewById(R.id.view_nutritional_info_summary_group_3_percentage);
            viewHolderLargeLand.nutritionalGroups[2].deviation = (TextView) findViewById(R.id.view_nutritional_info_summary_group_3_deviation);
            viewHolderLargeLand.nutritionalGroups[3] = new NutritionalGroupInfoViewHolder();
            viewHolderLargeLand.nutritionalGroups[3].name = (TextView) findViewById(R.id.view_nutritional_info_summary_group_4_name);
            viewHolderLargeLand.nutritionalGroups[3].percentage = (TextView) findViewById(R.id.view_nutritional_info_summary_group_4_percentage);
            viewHolderLargeLand.nutritionalGroups[3].deviation = (TextView) findViewById(R.id.view_nutritional_info_summary_group_4_deviation);
            viewHolderLargeLand.nutritionalGroups[4] = new NutritionalGroupInfoViewHolder();
            viewHolderLargeLand.nutritionalGroups[4].name = (TextView) findViewById(R.id.view_nutritional_info_summary_group_5_name);
            viewHolderLargeLand.nutritionalGroups[4].percentage = (TextView) findViewById(R.id.view_nutritional_info_summary_group_5_percentage);
            viewHolderLargeLand.nutritionalGroups[4].deviation = (TextView) findViewById(R.id.view_nutritional_info_summary_group_5_deviation);
            viewHolderLargeLand.nutritionalGroups[5] = new NutritionalGroupInfoViewHolder();
            viewHolderLargeLand.nutritionalGroups[5].name = (TextView) findViewById(R.id.view_nutritional_info_summary_group_6_name);
            viewHolderLargeLand.nutritionalGroups[5].percentage = (TextView) findViewById(R.id.view_nutritional_info_summary_group_6_percentage);
            viewHolderLargeLand.nutritionalGroups[5].deviation = (TextView) findViewById(R.id.view_nutritional_info_summary_group_6_deviation);

            for (NutritionalGroupInfoViewHolder nutritionalGroupInfo : viewHolderLargeLand.nutritionalGroups) {
                nutritionalGroupInfo.percentage.setVisibility(TextView.GONE);
                nutritionalGroupInfo.deviation.setVisibility(TextView.GONE);
            }
        } else {
            viewHolder = new ViewHolder();
        }

        viewHolder.container = findViewById(R.id.view_nutritional_info_summary_container);
        viewHolder.nutritionalScore = (TextView) findViewById(R.id.view_nutritional_info_percentage);
        viewHolder.initialLoadProgressBar = (ProgressBar) findViewById(R.id.view_nutritional_info_progress_bar);
        viewHolder.secondaryProgressBar = (ProgressBar) findViewById(R.id.view_nutritional_info_summary_secondary_progress_bar);

        viewHolder.nutritionalScore.setVisibility(TextView.GONE);

        viewHolder.initialLoadProgressBar.setVisibility(ProgressBar.VISIBLE);

        if (viewHolder instanceof ViewHolderLargeLand) {
            ((ViewHolderLargeLand) viewHolder).contentContainer.setVisibility(GONE);
        }

        if (viewHolder.container != null) {
            viewHolder.container.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (handler != null) {
                        handler.onClick();
                    }
                }
            });
        }
    }

    public void setHandler(Handler handler) {
        if (handler != null) {
            this.handler = handler;
        }
    }

    private void setValues() {
        if (percentagesTarget == null || percentagesSample == null || groupNames == null) {
            return;
        }

        int nutritionalScore = WeeklyMenu.getNutritionalScore(this.nutritionalScore);
        viewHolder.nutritionalScore.setText(Math.round(this.nutritionalScore * 100) + "%");
        viewHolder.nutritionalScore.setVisibility(TextView.VISIBLE);
        if (getResources() != null) {
            switch (nutritionalScore) {
                case WeeklyMenu.VALUE_VERY_LOW:
                    viewHolder.nutritionalScore.setTextColor(getResources().getColor(R.color.red_dark));
                    break;

                case WeeklyMenu.VALUE_LOW:
                    viewHolder.nutritionalScore.setTextColor(getResources().getColor(R.color.orange_dark));
                    break;

                case WeeklyMenu.VALUE_MEDIUM:
                    viewHolder.nutritionalScore.setTextColor(getResources().getColor(R.color.yellow_dark));
                    break;

                case WeeklyMenu.VALUE_HIGH:
                    viewHolder.nutritionalScore.setTextColor(getResources().getColor(R.color.green_dark));
                    break;

                case WeeklyMenu.VALUE_VERY_HIGH:
                    viewHolder.nutritionalScore.setTextColor(getResources().getColor(R.color.emerald_dark));
                    break;
            }
        }

        if (viewHolder instanceof ViewHolderLargeLand) {
            ViewHolderLargeLand viewHolderLargeLand = (ViewHolderLargeLand) viewHolder;
            viewHolder.nutritionalScore.setText(Math.round(this.nutritionalScore * 100) + "%");
            viewHolderLargeLand.nutritionalScore.setVisibility(TextView.VISIBLE);
            if (getResources() != null) {
                switch (nutritionalScore) {
                    case WeeklyMenu.VALUE_VERY_LOW:
                        viewHolder.nutritionalScore.setTextColor(getResources().getColor(R.color.red_dark));
                        break;

                    case WeeklyMenu.VALUE_LOW:
                        viewHolder.nutritionalScore.setTextColor(getResources().getColor(R.color.orange_dark));
                        break;

                    case WeeklyMenu.VALUE_MEDIUM:
                        viewHolder.nutritionalScore.setTextColor(getResources().getColor(R.color.yellow_dark));
                        break;

                    case WeeklyMenu.VALUE_HIGH:
                        viewHolder.nutritionalScore.setTextColor(getResources().getColor(R.color.green_dark));
                        break;

                    case WeeklyMenu.VALUE_VERY_HIGH:
                        viewHolder.nutritionalScore.setTextColor(getResources().getColor(R.color.emerald_dark));
                        break;
                }
            }

            if (!isGraphInitialized) {
                viewHolderLargeLand.graph.setPercentages(percentagesTarget, percentagesTarget);
                isGraphInitialized = true;
            }
            viewHolderLargeLand.graph.setPercentages(percentagesTarget, percentagesSample);

            for (int i = 0; i < groupNames.length; i++) {
                viewHolderLargeLand.nutritionalGroups[i].name.setText(groupNames[i]);
                viewHolderLargeLand.nutritionalGroups[i].percentage.setText(((double) Math.round(percentagesSample[i] * 1000)) / 10d + "%");
                double deviation = (Math.round(percentagesSample[i] * 1000) - percentagesTarget[i] * 1000) / 10;
                String sign = (deviation > 0d) ? "+" : "";
                viewHolderLargeLand.nutritionalGroups[i].deviation.setText(sign + deviation + "%");

                viewHolderLargeLand.nutritionalGroups[i].percentage.setVisibility(TextView.VISIBLE);
                viewHolderLargeLand.nutritionalGroups[i].deviation.setVisibility(TextView.VISIBLE);
            }
        }
    }

    public void setWeeklyMenu(long dayOfWeekId) {
        if (this.dayOfWeekId == 0 || CheftasticCalendar.weeksBetween(this.dayOfWeekId, dayOfWeekId) != 0) {
            if (nutritionalInfoLoader != null) {
                nutritionalInfoLoader.cancel(true);
            }
            nutritionalInfoLoader = new NutritionalInfoLoader();
            nutritionalInfoLoader.execute(dayOfWeekId);
        }

        this.dayOfWeekId = dayOfWeekId;
    }

    public void update() {
        if (nutritionalInfoLoader != null) {
            nutritionalInfoLoader.cancel(true);
        }
        nutritionalInfoLoader = new NutritionalInfoLoader();
        nutritionalInfoLoader.execute(dayOfWeekId);
    }

    public Bundle getCurrentState() {
        Bundle state = new Bundle();

        if (nutritionalInfoLoader == null) {
            state.putDoubleArray(STATE_PERCENTAGES_TARGET, percentagesTarget);
            state.putDoubleArray(STATE_PERCENTAGES_SAMPLE, percentagesSample);
            state.putStringArray(STATE_GROUP_NAMES, groupNames);
            state.putDouble(STATE_NUTRITIONAL_SCORE, nutritionalScore);
            state.putBoolean(STATE_GRAPH_INITIALIZED, isGraphInitialized);
        }

        return state;
    }

    public void restoreSavedState(Bundle savedState) {
        if (savedState != null
                && savedState.containsKey(STATE_PERCENTAGES_TARGET)
                && savedState.containsKey(STATE_PERCENTAGES_SAMPLE)
                && savedState.containsKey(STATE_NUTRITIONAL_SCORE)
                && savedState.containsKey(STATE_GROUP_NAMES)
                && savedState.containsKey(STATE_GRAPH_INITIALIZED)) {

            percentagesTarget = savedState.getDoubleArray(STATE_PERCENTAGES_TARGET);
            percentagesSample = savedState.getDoubleArray(STATE_PERCENTAGES_SAMPLE);
            nutritionalScore = savedState.getDouble(STATE_NUTRITIONAL_SCORE);
            groupNames = savedState.getStringArray(STATE_GROUP_NAMES);
            isGraphInitialized = savedState.getBoolean(STATE_GRAPH_INITIALIZED);

            viewHolder.initialLoadProgressBar.setVisibility(ProgressBar.GONE);

            if (viewHolder instanceof ViewHolderLargeLand) {
                ((ViewHolderLargeLand) viewHolder).contentContainer.setVisibility(View.VISIBLE);
            }

            setValues();
        }
    }

    public interface Handler {

        public void onClick();

    }

    private class ViewHolder {
        View container;
        TextView nutritionalScore;
        ProgressBar initialLoadProgressBar;
        ProgressBar secondaryProgressBar;
    }

    private class ViewHolderLargeLand extends ViewHolder {
        View contentContainer;
        CircularGraph graph;
        NutritionalGroupInfoViewHolder[] nutritionalGroups;
    }

    private class NutritionalGroupInfoViewHolder {
        TextView name;
        TextView percentage;
        TextView deviation;
    }

    private class NutritionalInfoLoader extends AsyncTask<Long, Void, Void> {
        WeeklyMenu menu;
        NutritionalGroup[] nutritionalGroups;

        boolean success = false;

        @Override
        protected Void doInBackground(Long... params) {
            if (params.length != 1) {
                return null;
            }
            menu = new WeeklyMenu(params[0]);
            percentagesSample = menu.getNutritionalInfoPercentages();
            nutritionalScore = menu.getNutritionalScore();

            if (nutritionalGroups == null) {
                nutritionalGroups = new NutritionalGroup[NutritionalGroup.getNumberOfNutritionalGroups()];
                for (int i = 0; i < nutritionalGroups.length; i++) {
                    nutritionalGroups[i] = NutritionalGroup.retrieveNutritionalGroup(i + 1);
                }

                percentagesTarget = new double[nutritionalGroups.length];
                groupNames = new String[nutritionalGroups.length];
                for (int i = 0; i < percentagesTarget.length; i++) {
                    percentagesTarget[i] = nutritionalGroups[i].getRecommendedPercentage();
                    groupNames[i] = nutritionalGroups[i].getName();
                }
            }

            success = true;
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (viewHolder.initialLoadProgressBar.getVisibility() != ProgressBar.VISIBLE) {
                viewHolder.secondaryProgressBar.setVisibility(ProgressBar.VISIBLE);

                if (viewHolder instanceof ViewHolderLargeLand) {
                    viewHolder.nutritionalScore.setVisibility(TextView.GONE);
                }
            } else {
                viewHolder.secondaryProgressBar.setVisibility(ProgressBar.GONE);
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (success && !isCancelled()) {
                setValues();

                if (viewHolder.initialLoadProgressBar.getVisibility() == ProgressBar.VISIBLE) {
                    if (viewHolder instanceof ViewHolderLargeLand) {
                        ((ViewHolderLargeLand) viewHolder).contentContainer.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.nutritionalScore.setVisibility(TextView.VISIBLE);
                    }

                    viewHolder.initialLoadProgressBar.setVisibility(ProgressBar.GONE);
                }

                viewHolder.secondaryProgressBar.setVisibility(ProgressBar.GONE);

                if (viewHolder instanceof ViewHolderLargeLand) {
                    viewHolder.nutritionalScore.setVisibility(TextView.VISIBLE);
                }

                nutritionalInfoLoader = null;
            }
        }
    }
}
