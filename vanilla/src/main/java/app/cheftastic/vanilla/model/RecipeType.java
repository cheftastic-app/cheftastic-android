package app.cheftastic.vanilla.model;

public class RecipeType {
    private int id;
    private String name;
    private RecipeCategory recipeCategory;

    public RecipeType(int id, String name, RecipeCategory recipeCategory) {
        setRecipeCategory(recipeCategory);
    }

    public RecipeCategory getRecipeCategory() {
        return recipeCategory;
    }

    public void setRecipeCategory(RecipeCategory recipeCategory) {
        this.recipeCategory = recipeCategory;
    }
}
