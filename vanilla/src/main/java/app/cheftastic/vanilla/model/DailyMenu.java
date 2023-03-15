package app.cheftastic.vanilla.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeMap;

import app.cheftastic.vanilla.CheftasticCalendar;
import app.cheftastic.vanilla.SQLiteHandler;

public class DailyMenu {
    private long dayId;
    private double nutritionalAmounts[];
    private TreeMap<Integer, TreeMap<Integer, MenuRecipe>> menu;

    public DailyMenu(long dayId) {
        setDayId(dayId);
        nutritionalAmounts = new double[]{0d, 0d, 0d, 0d, 0d, 0d};
        menu = newMenu();

        addRecipes(SQLiteHandler.retrieveDailyMenu(dayId));
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

    public MenuRecipe getRecipe(int mealId, int categoryId) {
        TreeMap<Integer, MenuRecipe> mealMenu = menu.get(mealId);
        if (mealMenu != null) {
            return mealMenu.get(categoryId);
        }

        return null;
    }

    public double[] getNutritionalInfo() {
        return nutritionalAmounts.clone();
    }

    public void addRecipe(MenuRecipe recipe) {
        if (recipe.getDayId() == this.dayId && (recipe.getMealId() == MenuRecipe.LUNCH || recipe.getMealId() == MenuRecipe.DINNER)) {
            TreeMap<Integer, MenuRecipe> mealMenu = menu.get(recipe.getMealId());

            if (mealMenu == null) {
                mealMenu = new TreeMap<Integer, MenuRecipe>();
                menu.put(recipe.getMealId(), mealMenu);
            }

            MenuRecipe menuRecipePrevious = mealMenu.put(recipe.getRecipeCategoryId(), recipe);

            addNutritionalValue(recipe);

            if (menuRecipePrevious != null && menuRecipePrevious.isRecipeAssigned()) {
                removeNutritionalValue(menuRecipePrevious);
            }
        }
    }

    public void addRecipes(Collection<MenuRecipe> recipes) {
        for (MenuRecipe p : recipes) {
            addRecipe(p);
        }
    }

    public void removeRecipe(int mealId, int categoryId) {
        TreeMap<Integer, MenuRecipe> mealMenu = menu.get(mealId);
        MenuRecipe menuRecipe = mealMenu.get(categoryId);

        if (menuRecipe != null && menuRecipe.isRecipeAssigned()) {
            removeNutritionalValue(menuRecipe);

            SQLiteHandler.deleteMenuRecipe(dayId, mealId, categoryId);

            SQLiteHandler.updateRecipeDayLastUsage(menuRecipe.getRecipe().getId(), null);

            menuRecipe.removeRecipe();
        }
    }

    public void addRecipeAndSave(MenuRecipe recipe) {
        addRecipe(recipe);

        SQLiteHandler.storeMenuRecipe(recipe);

        SQLiteHandler.updateRecipeDayLastUsage(recipe.getRecipe().getId(), recipe.getDayId());
    }

    public void addRecipeAndSave(Recipe recipe, int mealId) {
        MenuRecipe menuRecipe = new MenuRecipe(this.dayId, mealId, recipe);
        addRecipeAndSave(menuRecipe);
    }

    public void clearMenu() {
        for (TreeMap<Integer, MenuRecipe> mealMenu : menu.values()) {
            for (MenuRecipe menuRecipe : mealMenu.values()) {
                if (menuRecipe.isRecipeAssigned()) {
                    SQLiteHandler.deleteMenuRecipe(dayId, menuRecipe.getMealId(), menuRecipe.getRecipeCategoryId());

                    SQLiteHandler.updateRecipeDayLastUsage(menuRecipe.getRecipe().getId(), null);
                }
            }
        }

        menu = newMenu();

        nutritionalAmounts = new double[]{0d, 0d, 0d, 0d, 0d, 0d};
    }

    private void addNutritionalValue(MenuRecipe menuRecipe) {
        Recipe recipe = menuRecipe.getRecipe();
        nutritionalAmounts[0] += recipe.getAmountNutritionalGroup1();
        nutritionalAmounts[1] += recipe.getAmountNutritionalGroup2();
        nutritionalAmounts[2] += recipe.getAmountNutritionalGroup3();
        nutritionalAmounts[3] += recipe.getAmountNutritionalGroup4();
        nutritionalAmounts[4] += recipe.getAmountNutritionalGroup5();
        nutritionalAmounts[5] += recipe.getAmountNutritionalGroup6();
    }

    private void removeNutritionalValue(MenuRecipe menuRecipe) {
        Recipe recipe = menuRecipe.getRecipe();
        nutritionalAmounts[0] -= recipe.getAmountNutritionalGroup1();
        nutritionalAmounts[1] -= recipe.getAmountNutritionalGroup2();
        nutritionalAmounts[2] -= recipe.getAmountNutritionalGroup3();
        nutritionalAmounts[3] -= recipe.getAmountNutritionalGroup4();
        nutritionalAmounts[4] -= recipe.getAmountNutritionalGroup5();
        nutritionalAmounts[5] -= recipe.getAmountNutritionalGroup6();
    }

    private TreeMap<Integer, TreeMap<Integer, MenuRecipe>> newMenu() {
        TreeMap<Integer, TreeMap<Integer, MenuRecipe>> dailyMenu = new TreeMap<Integer, TreeMap<Integer, MenuRecipe>>();
        TreeMap<Integer, MenuRecipe> mealMenu = new TreeMap<Integer, MenuRecipe>();
        mealMenu.put(RecipeCategory.FIRST_COURSE, new MenuRecipe(this.dayId, MenuRecipe.LUNCH, RecipeCategory.FIRST_COURSE));
        mealMenu.put(RecipeCategory.SECOND_COURSE, new MenuRecipe(this.dayId, MenuRecipe.LUNCH, RecipeCategory.SECOND_COURSE));
        mealMenu.put(RecipeCategory.DESSERT, new MenuRecipe(this.dayId, MenuRecipe.LUNCH, RecipeCategory.DESSERT));
        dailyMenu.put(MenuRecipe.LUNCH, mealMenu);

        mealMenu = new TreeMap<Integer, MenuRecipe>();
        mealMenu.put(RecipeCategory.FIRST_COURSE, new MenuRecipe(this.dayId, MenuRecipe.DINNER, RecipeCategory.FIRST_COURSE));
        mealMenu.put(RecipeCategory.SECOND_COURSE, new MenuRecipe(this.dayId, MenuRecipe.DINNER, RecipeCategory.SECOND_COURSE));
        mealMenu.put(RecipeCategory.DESSERT, new MenuRecipe(this.dayId, MenuRecipe.DINNER, RecipeCategory.DESSERT));
        dailyMenu.put(MenuRecipe.DINNER, mealMenu);

        return dailyMenu;
    }

    public ArrayList<MenuRecipe> getUnassignedMeals() {
        ArrayList<MenuRecipe> unassignedMeals = new ArrayList<MenuRecipe>();

        for (TreeMap<Integer, MenuRecipe> mealMenu : this.menu.values()) {
            for (MenuRecipe menuRecipe : mealMenu.values()) {
                if (!menuRecipe.isRecipeAssigned() && menuRecipe.getDayId() >= new CheftasticCalendar().getDayId()) {
                    unassignedMeals.add(menuRecipe);
                }
            }
        }

        return unassignedMeals;
    }
}
