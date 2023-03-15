package app.cheftastic.vanilla.model;

import app.cheftastic.vanilla.CheftasticCalendar;

public class MenuRecipe {
    public static final int LUNCH = 1;
    public static final int DINNER = 2;

    private long dayId;
    private int mealId;
    private int recipeCategoryId;
    private Recipe recipe;

    public MenuRecipe(long dayId, int mealId, Recipe recipe) {
        setDayId(dayId);
        setMealId(mealId);
        setRecipeCategoryId(recipe);
        setRecipe(recipe);
    }

    public MenuRecipe(long dayId, int mealId, int recipeCategoryId) {
        setDayId(dayId);
        setMealId(mealId);
        setRecipeCategoryId(recipeCategoryId);
    }

    public long getDayId() {
        return dayId;
    }

    private void setDayId(long dayId) {
        if (!CheftasticCalendar.isValidDayId(dayId)) {
            throw new NumberFormatException("El identificador del día no es un identificador válido");
        }

        this.dayId = dayId;
    }

    public int getMealId() {
        return mealId;
    }

    private void setMealId(int mealId) {
        if (mealId != LUNCH && mealId != DINNER) {
            throw new NumberFormatException("El identificador de la comida no es un identificador válido.");
        }

        this.mealId = mealId;
    }

    public int getRecipeCategoryId() {
        return recipeCategoryId;
    }

    private void setRecipeCategoryId(Recipe recipe) {
        recipeCategoryId = recipe.getRecipeType().getRecipeCategory().getId();
    }

    private void setRecipeCategoryId(int id) {
        this.recipeCategoryId = id;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public boolean setRecipe(Recipe recipe) {
        if (recipe == null) {
            this.recipe = null;
        } else if (recipeCategoryId == recipe.getRecipeType().getRecipeCategory().getId()) {
            this.recipe = recipe;
            return true;
        }

        return false;
    }

    public void removeRecipe() {
        this.setRecipe(null);
    }

    public boolean isRecipeAssigned() {
        return recipe != null;
    }
}
