package app.cheftastic.vanilla.model;

public class ProductType {
    private int id;
    private String name;
    private NutritionalGroup nutritionalGroup;

    public ProductType(int id, String name, NutritionalGroup nutritionalGroup) {
        setName(name);
        setNutritionalGroup(nutritionalGroup);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NutritionalGroup getNutritionalGroup() {
        return this.nutritionalGroup;
    }

    public void setNutritionalGroup(NutritionalGroup nutritionalGroup) {
        this.nutritionalGroup = nutritionalGroup;
    }
}
