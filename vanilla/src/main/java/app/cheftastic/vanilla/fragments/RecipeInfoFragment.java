package app.cheftastic.vanilla.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.Iterator;

import app.cheftastic.R;
import app.cheftastic.vanilla.Settings;
import app.cheftastic.vanilla.SQLiteHandler;
import app.cheftastic.vanilla.model.IngredientAmount;
import app.cheftastic.vanilla.model.Recipe;
import app.cheftastic.vanilla.model.RecipeStep;

public class RecipeInfoFragment extends Fragment {

    public static final String ARG_RECIPE_ID = "recipe_info_fragment_recipe_id";
    public static final int ERROR_RECIPE_ID = -1;

    private LayoutInflater inflater;
    private int recipeId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        return inflater.inflate(R.layout.fragment_recipe_info, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        new RecipeLoader().execute((Void) null);

        RatingBar userScoreRatingBar = (RatingBar) getView().findViewById(R.id.fragment_recipe_info_user_rating_bar);

        userScoreRatingBar.setOnRatingBarChangeListener(
                new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        if (fromUser && SQLiteHandler.updateUserScore(rating, recipeId)) {
                            Toast.makeText(getActivity(), "Puntuación actualizada", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    public int getRecipeId() {
        return recipeId;
    }

    private static class ViewHolderRecipe {
        TextView name;
        TextView description;
        ImageView glutenFreeIcon;
        ImageView vegetarianIcon;
        TextView cookTime;
        TextView difficulty;
        TextView serveHot;
        RatingBar userScore;
        TextView numberOfPeople;
        LinearLayout ingredients;
        LinearLayout steps;
    }

    private static class ViewHolderIngredientAmount {
        TextView ingredientName;
        TextView nutritionalGroup;
        LinearLayout ingredientAmountContainer;
        TextView amount;
        TextView unitOfMeasurement;
    }

    private static class ViewHolderRecipeStep {
        TextView description;
        LinearLayout durationContainer;
        TextView duration;
        TextView unitOfTime;
    }

    private class RecipeLoader extends AsyncTask<Void, Recipe, Void> {

        @Override
        protected void onPreExecute() {
            getView().findViewById(R.id.fragment_recipe_info_container).setVisibility(View.INVISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            Bundle args = getArguments();
            if (args != null && args.containsKey(ARG_RECIPE_ID)) {
                recipeId = args.getInt(ARG_RECIPE_ID);
                Recipe r = SQLiteHandler.retrieveRecipe(recipeId);
                publishProgress(r);
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Recipe... values) {
            super.onProgressUpdate(values);

            Recipe r = values[0];
            if (r != null) {
                ViewHolderRecipe recipeHolder = new ViewHolderRecipe();
                recipeHolder.name = (TextView) getView().findViewById(R.id.fragment_recipe_info_name);
                recipeHolder.description = (TextView) getView().findViewById(R.id.fragment_recipe_info_description);
                recipeHolder.glutenFreeIcon = (ImageView) getView().findViewById(R.id.fragment_recipe_info_gluten_free_icon);
                recipeHolder.vegetarianIcon = (ImageView) getView().findViewById(R.id.fragment_recipe_info_vegetarian_icon);
                recipeHolder.cookTime = (TextView) getView().findViewById(R.id.fragment_recipe_info_cook_time);
                recipeHolder.difficulty = (TextView) getView().findViewById(R.id.fragment_recipe_info_difficulty);
                recipeHolder.serveHot = (TextView) getView().findViewById(R.id.fragment_recipe_info_serve_hot);
                recipeHolder.userScore = (RatingBar) getView().findViewById(R.id.fragment_recipe_info_user_rating_bar);
                recipeHolder.numberOfPeople = (TextView) getView().findViewById(R.id.fragment_recipe_info_pax);
                recipeHolder.ingredients = (LinearLayout) getView().findViewById(R.id.fragment_recipe_info_ingredient_list);
                recipeHolder.steps = (LinearLayout) getView().findViewById(R.id.fragment_recipe_info_steps);
                getView().setTag(recipeHolder);

                int numberOfPeople = Settings.getNumberOfPeople();
                recipeHolder.name.setText(r.getName());
                recipeHolder.description.setText(r.getDescription());
                recipeHolder.glutenFreeIcon.setVisibility((r.getIsGlutenFree()) ? ImageView.VISIBLE : ImageView.GONE);
                recipeHolder.vegetarianIcon.setVisibility((r.getIsVegetarian()) ? ImageView.VISIBLE : ImageView.GONE);
                recipeHolder.cookTime.setText(r.getTimeOfCook().toString());
                recipeHolder.difficulty.setText(r.getDifficultyString());
                recipeHolder.serveHot.setText(r.getServeHotString());
                recipeHolder.userScore.setRating(r.getUserVote().floatValue());
                recipeHolder.numberOfPeople.setText(getResources().getQuantityString(R.plurals.ingredients_for_x_people, numberOfPeople, numberOfPeople));

                Iterator<IngredientAmount> ingredients = r.getIngredients();
                while (ingredients.hasNext()) {
                    IngredientAmount i = ingredients.next();
                    View ingredientView = inflater.inflate(R.layout.list_item_ingredient, null);
                    if (ingredientView != null) {
                        ViewHolderIngredientAmount holderIngredientAmount = new ViewHolderIngredientAmount();
                        holderIngredientAmount.ingredientName = (TextView) ingredientView.findViewById(R.id.list_item_ingredient_name);
                        holderIngredientAmount.nutritionalGroup = (TextView) ingredientView.findViewById(R.id.list_item_ingredient_details);
                        holderIngredientAmount.ingredientAmountContainer = (LinearLayout) ingredientView.findViewById(R.id.list_item_ingredient_container_amount);
                        holderIngredientAmount.amount = (TextView) ingredientView.findViewById(R.id.list_item_ingredient_amount);
                        holderIngredientAmount.unitOfMeasurement = (TextView) ingredientView.findViewById(R.id.list_item_ingredient_amount_unit);
                        ingredientView.setTag(holderIngredientAmount);

                        holderIngredientAmount.ingredientName.setText(i.getIngredient().getName());
                        holderIngredientAmount.nutritionalGroup.setText(i.getIngredient().getProductType().getName() + ((i.getIngredient().getProductType().getNutritionalGroup() != null) ? " | " + i.getIngredient().getProductType().getNutritionalGroup().getName() : ""));

                        if (i.getAmountPerPerson() != null && i.getUnitOfMeasurement() != null) {
                            holderIngredientAmount.amount.setText(i.getAmountPerPersonString(numberOfPeople));
                            holderIngredientAmount.unitOfMeasurement.setText(i.getUnitOfMeasurementString(numberOfPeople));
                        } else {
                            holderIngredientAmount.ingredientAmountContainer.setVisibility(LinearLayout.GONE);
                        }

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1);
                        recipeHolder.ingredients.addView(ingredientView, params);
                    }
                }

                Iterator<RecipeStep> steps = r.getSteps();
                while (steps.hasNext()) {
                    RecipeStep p = steps.next();
                    View viewSteps = inflater.inflate(R.layout.list_item_recipe_step, null);
                    if (viewSteps != null) {
                        ViewHolderRecipeStep holderSteps = new ViewHolderRecipeStep();
                        holderSteps.description = (TextView) viewSteps.findViewById(R.id.list_item_recipe_step_description);
                        holderSteps.durationContainer = (LinearLayout) viewSteps.findViewById(R.id.list_item_recipe_step_container_duration);
                        holderSteps.duration = (TextView) viewSteps.findViewById(R.id.list_item_recipe_step_duration);
                        holderSteps.unitOfTime = (TextView) viewSteps.findViewById(R.id.list_item_recipe_step_duration_unit);
                        viewSteps.setTag(holderSteps);

                        holderSteps.description.setText(p.getDescription());

                        if (p.getDuration() != null && p.getUnitOfTime() != null) {
                            holderSteps.duration.setText(String.valueOf(p.getDuration()));
                            holderSteps.unitOfTime.setText(p.getUnitOfTimeString());
                        } else {
                            holderSteps.durationContainer.setVisibility(LinearLayout.GONE);
                        }

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1);
                        recipeHolder.steps.addView(viewSteps, params);
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            getView().findViewById(R.id.fragment_recipe_info_container).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.fragment_recipe_info_progress_bar).setVisibility(View.GONE);
        }
    }
}
