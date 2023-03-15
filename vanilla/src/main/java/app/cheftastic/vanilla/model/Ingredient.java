package app.cheftastic.vanilla.model;

import java.sql.Date;

public class Ingredient {
    private int id;
    private String name;
    private String pluralName;
    private Integer unitaryWeight;
    private Boolean containsGluten;
    private Boolean isVegetarian;
    private Boolean isVerified;
    private Date creationDate;
    private ProductType productType;
    private int authorId;

    public Ingredient(int id, String name, String pluralName, Integer unitaryWeight,
                      Boolean containsGluten, Boolean isVegetarian, Boolean isVerified, String creationDate,
                      ProductType productType, int authorId) {
        setName(name);
        setUnitaryWeight(unitaryWeight);
        setContainsGluten(containsGluten);
        setIsVegetarian(isVegetarian);
        setProductType(productType);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getUnitaryWeight() {
        return unitaryWeight;
    }

    private void setUnitaryWeight(Integer unitaryWeight) {
        this.unitaryWeight = unitaryWeight;
    }

    public Boolean getContainsGluten() {
        return this.containsGluten;
    }

    public void setContainsGluten(Boolean containsGluten) {
        this.containsGluten = containsGluten;
    }

    public Boolean getIsVegetarian() {
        return this.isVegetarian;
    }

    public void setIsVegetarian(Boolean isVegetarian) {
        this.isVegetarian = isVegetarian;
    }

    public ProductType getProductType() {
        return this.productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }
}
