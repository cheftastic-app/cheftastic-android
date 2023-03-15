package app.cheftastic.vanilla.model;

import android.content.ContentValues;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

import app.cheftastic.R;
import app.cheftastic.vanilla.App;
import app.cheftastic.vanilla.SQLiteHandler;

public class Recipe {
    private int id;
    private String name;
    private String description;
    private Integer timeOfCook;
    private Integer difficulty;
    private Boolean isServeHot;
    private Double totalVotes;
    private int numberOfVotes;
    private Double userVote;
    private Boolean isVerified;
    private Boolean isUserFrequent;
    private Date creationDate;
    private Boolean isVegetarian;
    private Boolean isGlutenFree;
    private double[] nutritionalGroupsAmounts;
    private Boolean isComputed;
    private Long lastUsageDayId;
    private Long lastUsageDayTempId;
    private RecipeType recipeType;
    private int authorId;
    private TreeMap<Integer, RecipeStep> steps;
    private ArrayList<IngredientAmount> ingredients;
    private ArrayList<Recipe> garnishes;

    private boolean needsToRecalculateAmounts;
    private boolean needsToRecalculateGlutenFree;
    private boolean needsToRecalculateVegetarian;

    public Recipe(int id, String name, String description, Integer timeOfCook, Integer difficulty,
                  Boolean isServeHot, Double totalVotes, int numberOfVotes, Double userVote,
                  Boolean isVerified, Boolean isUserFrequent, String creationDate, Boolean isVegetarian,
                  Boolean isGlutenFree, Double amountNutritionalGroup1, Double amountNutritionalGroup2,
                  Double amountNutritionalGroup3, Double amountNutritionalGroup4, Double amountNutritionalGroup5,
                  Double amountNutritionalGroup6, Boolean isComputed, Long lastUsageDayId, Long lastUsageDayTempId,
                  RecipeType recipeType, int authorId) {
        setId(id);
        setName(name);
        setDescription(description);
        setTimeOfCook(timeOfCook);
        setDifficulty(difficulty);
        setIsServeHot(isServeHot);
        setUserVote(userVote);
        setIsVerified(isVerified);
        setIsVegetarian(isVegetarian);
        setIsGlutenFree(isGlutenFree);
        nutritionalGroupsAmounts = new double[6];
        setAmountNutritionalGroup1(amountNutritionalGroup1);
        setAmountNutritionalGroup2(amountNutritionalGroup2);
        setAmountNutritionalGroup3(amountNutritionalGroup3);
        setAmountNutritionalGroup4(amountNutritionalGroup4);
        setAmountNutritionalGroup5(amountNutritionalGroup5);
        setAmountNutritionalGroup6(amountNutritionalGroup6);
        setIsComputed(isComputed);
        this.lastUsageDayId = lastUsageDayId;
        setRecipeType(recipeType);

        steps = new TreeMap<Integer, RecipeStep>();
        ingredients = new ArrayList<IngredientAmount>();

        needsToRecalculateAmounts = true;
        needsToRecalculateGlutenFree = true;
        needsToRecalculateVegetarian = true;
    }

    public static Recipe retrieveRecipe(int id) {
        return SQLiteHandler.retrieveRecipe(id);
    }

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTimeOfCook() {
        return timeOfCook;
    }

    public void setTimeOfCook(Integer timeOfCook) {
        this.timeOfCook = timeOfCook;
    }

    public String getDifficultyString() {
        if (difficulty != null) {
            switch (difficulty) {
                case 1:
                    return App.getContext().getString(R.string.difficulty_1);
                case 2:
                    return App.getContext().getString(R.string.difficulty_2);
                case 3:
                    return App.getContext().getString(R.string.difficulty_3);
                case 4:
                    return App.getContext().getString(R.string.difficulty_4);
                case 5:
                    return App.getContext().getString(R.string.difficulty_5);
                default:
                    return "";
            }
        }

        return "";
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public void setIsServeHot(Boolean isServeHot) {
        this.isServeHot = isServeHot;
    }

    public String getServeHotString() {
        if (isServeHot != null) {
            return (isServeHot) ? App.getContext().getString(R.string.serve_hot) : App.getContext().getString(R.string.serve_cold);
        }

        return "";
    }

    public Double getUserVote() {
        return (userVote != null) ? userVote : 0.0;
    }

    public void setUserVote(Double userVote) {
        this.userVote = userVote;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = (isVerified == null) ? false : isVerified;
    }

    public Boolean getIsVegetarian() {
        return isVegetarian;
    }

    public void setIsVegetarian(Boolean isVegetarian) {
        this.isVegetarian = isVegetarian;
    }

    public Boolean getIsGlutenFree() {
        return isGlutenFree;
    }

    public void setIsGlutenFree(Boolean isGlutenFree) {
        this.isGlutenFree = isGlutenFree;
    }

    public Double getAmountNutritionalGroup1() {
        return this.nutritionalGroupsAmounts[0];
    }

    private void setAmountNutritionalGroup1(Double amount) {
        this.nutritionalGroupsAmounts[0] = (amount == null) ? 0 : amount;
    }

    public Double getAmountNutritionalGroup2() {
        return this.nutritionalGroupsAmounts[1];
    }

    private void setAmountNutritionalGroup2(Double amount) {
        this.nutritionalGroupsAmounts[1] = (amount == null) ? 0 : amount;
    }

    public Double getAmountNutritionalGroup3() {
        return this.nutritionalGroupsAmounts[2];
    }

    private void setAmountNutritionalGroup3(Double amount) {
        this.nutritionalGroupsAmounts[2] = (amount == null) ? 0 : amount;
    }

    public Double getAmountNutritionalGroup4() {
        return this.nutritionalGroupsAmounts[3];
    }

    private void setAmountNutritionalGroup4(Double amount) {
        this.nutritionalGroupsAmounts[3] = (amount == null) ? 0 : amount;
    }

    public Double getAmountNutritionalGroup5() {
        return this.nutritionalGroupsAmounts[4];
    }

    private void setAmountNutritionalGroup5(Double amount) {
        this.nutritionalGroupsAmounts[4] = (amount == null) ? 0 : amount;
    }

    public Double getAmountNutritionalGroup6() {
        return this.nutritionalGroupsAmounts[5];
    }

    private void setAmountNutritionalGroup6(Double amount) {
        this.nutritionalGroupsAmounts[5] = (amount == null) ? 0 : amount;
    }

    public double[] getNutritionalInfo() {
        return nutritionalGroupsAmounts.clone();
    }

    private boolean setAmountsNutritionalGroups(Double[] amounts) {
        if (amounts.length == 6) {
            setAmountNutritionalGroup1(amounts[0]);
            setAmountNutritionalGroup2(amounts[1]);
            setAmountNutritionalGroup3(amounts[2]);
            setAmountNutritionalGroup4(amounts[3]);
            setAmountNutritionalGroup5(amounts[4]);
            setAmountNutritionalGroup6(amounts[5]);

            return true;
        }

        return false;
    }

    public Boolean getIsComputed() {
        return this.isComputed;
    }

    public void setIsComputed(Boolean isComputed) {
        this.isComputed = isComputed;
    }

    public Long getLastUsageDayId() {
        return lastUsageDayId;
    }

    public RecipeType getRecipeType() {
        return recipeType;
    }

    public void setRecipeType(RecipeType recipeType) {
        this.recipeType = recipeType;
    }

    public Iterator<RecipeStep> getSteps() {
        return this.steps.values().iterator();
    }

    public void setSteps(TreeMap<Integer, RecipeStep> steps) {
        this.steps = steps;
    }

    public void setIngredients(ArrayList<IngredientAmount> ingredients) {
        this.ingredients = ingredients;

        needsToRecalculateAmounts = true;
        needsToRecalculateGlutenFree = true;
        needsToRecalculateVegetarian = true;
    }

    public Iterator<IngredientAmount> getIngredients() {
        return this.ingredients.iterator();
    }

    public double[] calculateNutritionalGroupAmounts() {
        setAmountsNutritionalGroups(IngredientAmount.calculateNutritionalGroupsAmounts(ingredients));

        needsToRecalculateAmounts = false;

        return getNutritionalInfo();
    }

    public boolean calculateIsVegetarian() {
        setIsVegetarian(IngredientAmount.validForVegetarians(ingredients));

        needsToRecalculateVegetarian = false;

        return getIsVegetarian();
    }

    public boolean calculateIsGlutenFree() {
        setIsGlutenFree(IngredientAmount.areGlutenFree(ingredients));

        needsToRecalculateGlutenFree = false;

        return getIsGlutenFree();
    }

    public void processIngredients(SQLiteDatabase db) {
        updateNutritionalGroupValues(db);
        updateIsGlutenFree(db);
        updateIsVegetarian(db);
        updateComputedValues(db);
    }

    private void updateNutritionalGroupValues(SQLiteDatabase db) {
        Resources r = App.getContext().getResources();

        if (db != null && r != null) {
            if (needsToRecalculateGlutenFree) {
                this.calculateNutritionalGroupAmounts();
            }

            ContentValues values = new ContentValues();
            values.put(SQLiteHandler.TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_1, getAmountNutritionalGroup1());
            values.put(SQLiteHandler.TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_2, getAmountNutritionalGroup2());
            values.put(SQLiteHandler.TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_3, getAmountNutritionalGroup3());
            values.put(SQLiteHandler.TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_4, getAmountNutritionalGroup4());
            values.put(SQLiteHandler.TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_5, getAmountNutritionalGroup5());
            values.put(SQLiteHandler.TABLE_RECIPE_COLUMN_NUTRITIONAL_GROUP_6, getAmountNutritionalGroup6());

            String[] whereArgs = {Integer.toString(getId())};

            db.update(SQLiteHandler.TABLE_RECIPE, values, SQLiteHandler.TABLE_RECIPE_COLUMN_ID + " = ?", whereArgs);
        }
    }

    private void updateIsGlutenFree(SQLiteDatabase db) {
        Resources r = App.getContext().getResources();

        if (db != null && r != null) {
            if (needsToRecalculateGlutenFree) {
                this.calculateIsGlutenFree();
            }

            ContentValues values = new ContentValues();
            values.put(SQLiteHandler.TABLE_RECIPE_COLUMN_GLUTEN_FREE, (getIsGlutenFree() != null && getIsGlutenFree()) ? 1 : 0);

            String[] whereArgs = {Integer.toString(getId())};

            db.update(SQLiteHandler.TABLE_RECIPE, values, SQLiteHandler.TABLE_RECIPE_COLUMN_ID + " = ?", whereArgs);
        }
    }

    private void updateIsVegetarian(SQLiteDatabase db) {
        Resources r = App.getContext().getResources();

        if (db != null && r != null) {
            if (needsToRecalculateVegetarian) {
                this.calculateIsVegetarian();
            }

            ContentValues values = new ContentValues();
            values.put(SQLiteHandler.TABLE_RECIPE_COLUMN_VEGETARIAN, (getIsVegetarian() != null && getIsVegetarian()) ? 1 : 0);

            String[] whereArgs = {Integer.toString(getId())};

            db.update(SQLiteHandler.TABLE_RECIPE, values, SQLiteHandler.TABLE_RECIPE_COLUMN_ID + " = ?", whereArgs);
        }
    }

    private void updateComputedValues(SQLiteDatabase db) {
        Resources r = App.getContext().getResources();

        if (db != null && r != null) {
            setIsComputed((!needsToRecalculateAmounts) && (!needsToRecalculateGlutenFree) && (!needsToRecalculateVegetarian));

            ContentValues values = new ContentValues();
            values.put(SQLiteHandler.TABLE_RECIPE_COLUMN_SCORED, (getIsComputed() != null && getIsComputed()) ? 1 : 0);

            String[] whereArgs = {Integer.toString(getId())};

            db.update(SQLiteHandler.TABLE_RECIPE, values, SQLiteHandler.TABLE_RECIPE_COLUMN_ID + " = ?", whereArgs);
        }
    }
}