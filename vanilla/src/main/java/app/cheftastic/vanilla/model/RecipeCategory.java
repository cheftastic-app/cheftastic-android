package app.cheftastic.vanilla.model;

public class RecipeCategory {
    public static final int STARTER = 1;
    public static final int FIRST_COURSE = 2;
    public static final int SECOND_COURSE = 3;
    public static final int GARNISH = 4;
    public static final int DESSERT = 5;

    private int id;
    private String name;

    public RecipeCategory(int id, String name) {
        setId(id);
        setName(name);
    }

    public int getId() {
        return this.id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
