package app.cheftastic.vanilla.model;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Random;
import java.util.TreeSet;

import app.cheftastic.vanilla.CheftasticCalendar;
import app.cheftastic.vanilla.SQLiteHandler;

public class WeeklyMenu {
    public static final int DAYS_OF_WEEK = 7;
    public static final int MONDAY = 0;
    public static final int TUESDAY = 1;
    public static final int WEDNESDAY = 2;
    public static final int THURSDAY = 3;
    public static final int FRIDAY = 4;
    public static final int SATURDAY = 5;
    public static final int SUNDAY = 6;

    public static final int VALUE_VERY_LOW = 0;
    public static final int VALUE_LOW = 1;
    public static final int VALUE_MEDIUM = 2;
    public static final int VALUE_HIGH = 3;
    public static final int VALUE_VERY_HIGH = 4;

    private static final int ERROR = -1;

    private DailyMenu[] weeklyMenu;
    private double nutritionalAmounts[];
    private double targetNutritionalPercentages[];
    private CheftasticCalendar weekCalendar;

    public WeeklyMenu(long dayId) {
        weeklyMenu = new DailyMenu[DAYS_OF_WEEK];
        nutritionalAmounts = new double[NutritionalGroup.getNumberOfNutritionalGroups()];
        weekCalendar = new CheftasticCalendar(dayId);

        addDailyMenu(new DailyMenu(weekCalendar.getDayOfWeekId(CheftasticCalendar.MONDAY)));
        addDailyMenu(new DailyMenu(weekCalendar.getDayOfWeekId(CheftasticCalendar.TUESDAY)));
        addDailyMenu(new DailyMenu(weekCalendar.getDayOfWeekId(CheftasticCalendar.WEDNESDAY)));
        addDailyMenu(new DailyMenu(weekCalendar.getDayOfWeekId(CheftasticCalendar.THURSDAY)));
        addDailyMenu(new DailyMenu(weekCalendar.getDayOfWeekId(CheftasticCalendar.FRIDAY)));
        addDailyMenu(new DailyMenu(weekCalendar.getDayOfWeekId(CheftasticCalendar.SATURDAY)));
        addDailyMenu(new DailyMenu(weekCalendar.getDayOfWeekId(CheftasticCalendar.SUNDAY)));

        targetNutritionalPercentages = NutritionalGroup.getNutritionalGroupsTargetPercentage();
    }

    public static int getNutritionalScore(double score) {
        if (score < 0.65) {
            return VALUE_VERY_LOW;
        } else if (score < 0.75) {
            return VALUE_LOW;
        } else if (score < 0.85) {
            return VALUE_MEDIUM;
        } else if (score < 0.94) {
            return VALUE_HIGH;
        } else {
            return VALUE_VERY_HIGH;
        }
    }

    public boolean addDailyMenu(DailyMenu dailyMenu) {
        if (weekCalendar == null) {
            weekCalendar = new CheftasticCalendar(dailyMenu.getDayId());
        }

        if (CheftasticCalendar.weeksBetween(weekCalendar.getDayId(), dailyMenu.getDayId()) == 0) {
            int dayOfWeek = getDayOfWeek(new CheftasticCalendar(dailyMenu.getDayId()).getDayOfWeek());

            if (dayOfWeek != ERROR) {
                if (weeklyMenu[dayOfWeek] != null) {
                    removeNutritionalValue(weeklyMenu[dayOfWeek]);
                }

                weeklyMenu[dayOfWeek] = dailyMenu;
                addNutritionalValue(dailyMenu);
                return true;
            }
        }

        return false;
    }

    public void clearMenu() {
        for (DailyMenu dailyMenu : weeklyMenu) {
            if (dailyMenu.getDayId() >= new CheftasticCalendar().getDayId()) {
                removeNutritionalValue(dailyMenu);
                dailyMenu.clearMenu();
            }
        }
    }

    private void addRecipe(long dayId, int mealId, Recipe recipe) {
        int dayOfWeek = getDayOfWeek(dayId);
        if (weeklyMenu[dayOfWeek] == null) {
            weeklyMenu[dayOfWeek] = new DailyMenu(dayId);
        }

        weeklyMenu[dayOfWeek].addRecipeAndSave(recipe, mealId);
        addNutritionalValue(recipe);
    }

    private int getDayOfWeek(long dayId) {
        return getDayOfWeek(new CheftasticCalendar(dayId).getDayOfWeek());
    }

    private int getDayOfWeek(int dayOfWeek) {
        switch (dayOfWeek) {
            case 1:
                return MONDAY;

            case 2:
                return TUESDAY;

            case 3:
                return WEDNESDAY;

            case 4:
                return THURSDAY;

            case 5:
                return FRIDAY;

            case 6:
                return SATURDAY;

            case 7:
                return SUNDAY;

            default:
                return ERROR;
        }
    }

    public double[] getNutritionalInfoPercentages() {
        double total = 0d;
        for (double amounts : nutritionalAmounts) {
            total += amounts;
        }

        double[] percentages = new double[nutritionalAmounts.length];
        for (int i = 0; i < nutritionalAmounts.length; i++) {
            percentages[i] = (total == 0d) ? 0d : nutritionalAmounts[i] / total;
        }

        return percentages;
    }

    private void addNutritionalValue(DailyMenu m) {
        double[] menuNutritionalAmounts = m.getNutritionalInfo();

        nutritionalAmounts[0] += menuNutritionalAmounts[0];
        nutritionalAmounts[1] += menuNutritionalAmounts[1];
        nutritionalAmounts[2] += menuNutritionalAmounts[2];
        nutritionalAmounts[3] += menuNutritionalAmounts[3];
        nutritionalAmounts[4] += menuNutritionalAmounts[4];
        nutritionalAmounts[5] += menuNutritionalAmounts[5];
    }

    private void addNutritionalValue(Recipe r) {
        double[] recipeNutritionalAmounts = r.getNutritionalInfo();

        nutritionalAmounts[0] += recipeNutritionalAmounts[0];
        nutritionalAmounts[1] += recipeNutritionalAmounts[1];
        nutritionalAmounts[2] += recipeNutritionalAmounts[2];
        nutritionalAmounts[3] += recipeNutritionalAmounts[3];
        nutritionalAmounts[4] += recipeNutritionalAmounts[4];
        nutritionalAmounts[5] += recipeNutritionalAmounts[5];
    }

    private void removeNutritionalValue(DailyMenu m) {
        double[] menuNutritionalAmounts = m.getNutritionalInfo();

        nutritionalAmounts[0] -= menuNutritionalAmounts[0];
        nutritionalAmounts[1] -= menuNutritionalAmounts[1];
        nutritionalAmounts[2] -= menuNutritionalAmounts[2];
        nutritionalAmounts[3] -= menuNutritionalAmounts[3];
        nutritionalAmounts[4] -= menuNutritionalAmounts[4];
        nutritionalAmounts[5] -= menuNutritionalAmounts[5];
    }

    private ArrayList<MenuRecipe> getUnassignedMeals() {
        ArrayList<MenuRecipe> unassignedMeals = new ArrayList<MenuRecipe>();
        for (DailyMenu dailyMenu : weeklyMenu) {
            unassignedMeals.addAll(dailyMenu.getUnassignedMeals());
        }

        return unassignedMeals;
    }

    public double getNutritionalScore() {
        double score;
        double[] nutritionalInfoPercentages = getNutritionalInfoPercentages();

        double[] deviations = new double[nutritionalInfoPercentages.length];
        for (int i = 0; i < nutritionalInfoPercentages.length; i++) {
            deviations[i] = targetNutritionalPercentages[i] - nutritionalInfoPercentages[i];
        }

        double sumDeviations = 0;
        double medianDeviations = 0;
        double maxDeviation = 0;
        double minDeviation = 1;
        for (double deviation : deviations) {
            double deviationAbs = Math.abs(deviation);
            sumDeviations += deviationAbs;
            medianDeviations += (deviationAbs / deviations.length);
            maxDeviation = (deviationAbs > maxDeviation) ? deviationAbs : maxDeviation;
            minDeviation = (deviationAbs < minDeviation) ? deviationAbs : minDeviation;
        }

        if (sumDeviations == 1d) {
            return 0;
        }

        score = 1 - (sumDeviations * 2 + medianDeviations + maxDeviation + minDeviation) / 5;

        return score;
    }

    public static class MenuGenerator extends AsyncTask<Long, Double, Double> {
        private Handler handler;
        private boolean keepMeals;
        private int assignedMeals;
        private int numberOfMealsToAssign;
        private long dayId;

        public MenuGenerator(Handler handler, boolean keepMeals) {
            this(handler, keepMeals, 0, 0);
        }

        public MenuGenerator(Handler handler, boolean keepMeals, int assignedMeals, int numberOfMealsToAssign) {
            this.handler = handler;
            this.keepMeals = keepMeals;
            this.assignedMeals = assignedMeals;
            this.numberOfMealsToAssign = numberOfMealsToAssign;
        }

        @Override
        @SuppressWarnings("ConstantConditions")
        protected Double doInBackground(Long... params) {
            this.dayId = params[0];
            WeeklyMenu weeklyMenu = new WeeklyMenu(params[0]);
            if (!keepMeals) {
                weeklyMenu.clearMenu();
            }

            ArrayList<Recipe> firstCourses = SQLiteHandler.retrieveRecipesToCompute(RecipeCategory.FIRST_COURSE);
            ArrayList<Recipe> secondCourses = SQLiteHandler.retrieveRecipesToCompute(RecipeCategory.SECOND_COURSE);
            ArrayList<Recipe> desserts = SQLiteHandler.retrieveRecipesToCompute(RecipeCategory.DESSERT);

            double[] deviations = new double[6];

            ArrayList<MenuRecipe> unassignedMeals = weeklyMenu.getUnassignedMeals();
            if (numberOfMealsToAssign == 0) {
                numberOfMealsToAssign = unassignedMeals.size();
            }

            TreeSet<Integer> assignedMeals = new TreeSet<Integer>();
            Random random = new Random();
            while (!isCancelled() && !unassignedMeals.isEmpty()) {
                for (int j = 0; j < weeklyMenu.targetNutritionalPercentages.length; j++) {
                    deviations[j] = weeklyMenu.targetNutritionalPercentages[j] - weeklyMenu.getNutritionalInfoPercentages()[j];
                }

                MenuRecipe menuRecipe = unassignedMeals.remove(random.nextInt(unassignedMeals.size()));

                ArrayList<Recipe> recipes;
                switch (menuRecipe.getRecipeCategoryId()) {
                    case (RecipeCategory.FIRST_COURSE):
                        recipes = firstCourses;
                        break;

                    case (RecipeCategory.SECOND_COURSE):
                        recipes = secondCourses;
                        break;

                    case (RecipeCategory.DESSERT):
                        recipes = desserts;
                        break;

                    default:
                        recipes = new ArrayList<Recipe>();
                }

                Recipe recipeSelected = null;
                double recipeSelectedScore = 0;
                for (Recipe recipe : recipes) {
                    double score = 0d;
                    double[] recipeNutritionalScore = recipe.getNutritionalInfo();
                    for (int j = 0; j < recipeNutritionalScore.length; j++) {
                        score += recipeNutritionalScore[j] * deviations[j];
                    }

                    score *= 1 + ((recipe.getUserVote() == null) ? 0 : 0.05 * recipe.getUserVote());

                    long dayIdLastUsage = (recipe.getLastUsageDayId() == null) ? 0 : recipe.getLastUsageDayId();
                    if (dayIdLastUsage != 0) {
                        score *= Math.min(1, 0.25 * Math.abs(CheftasticCalendar.weeksBetween(dayIdLastUsage, weeklyMenu.weekCalendar.getDayId())));
                    }

                    if (assignedMeals.contains(recipe.getId())) {
                        score *= 0.1;
                    }

                    if (recipeSelected == null || score > recipeSelectedScore) {
                        recipeSelected = recipe;
                        recipeSelectedScore = score;
                    }
                }

                weeklyMenu.addRecipe(menuRecipe.getDayId(), menuRecipe.getMealId(), Recipe.retrieveRecipe(recipeSelected.getId()));
                assignedMeals.add(recipeSelected.getId());

                publishProgress((((double) ++this.assignedMeals) / numberOfMealsToAssign) * 100);
            }

            return weeklyMenu.getNutritionalScore();
        }

        @Override
        protected void onProgressUpdate(Double... values) {
            super.onProgressUpdate(values);
            if (handler != null) {
                handler.onPublishProgress(values[0]);
            }
        }

        @Override
        protected void onPostExecute(Double score) {
            super.onPostExecute(score);
            if (handler != null) {
                handler.onMenuGenerated();
            }
        }

        public long getDayId() {
            return dayId;
        }

        public int getAssignedMeals() {
            return assignedMeals;
        }

        public int getNumberOfMealsToAssign() {
            return numberOfMealsToAssign;
        }

        public interface Handler {

            public void onPublishProgress(double percentage);

            public void onMenuGenerated();
        }
    }
}
