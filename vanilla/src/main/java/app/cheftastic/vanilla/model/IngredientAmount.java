package app.cheftastic.vanilla.model;

import java.util.Collection;

public class IngredientAmount {
    private Ingredient ingredient;
    private UnitOfMeasurement unitOfMeasurement;
    private Double amountPerPerson;

    public IngredientAmount(Ingredient ingredient, UnitOfMeasurement unitOfMeasurement, Double amountPerPerson) {
        setIngredient(ingredient);
        setAmountPerPerson(unitOfMeasurement, amountPerPerson);
    }

    public static Double[] calculateNutritionalGroupsAmounts(Collection<IngredientAmount> ingredients) {
        Double[] amounts = new Double[]{(double) 0, (double) 0, (double) 0, (double) 0, (double) 0, (double) 0};

        for (IngredientAmount c : ingredients) {
            Double amount = (double) 0;
            if (c.getUnitOfMeasurement() != null) {
                if (c.getUnitOfMeasurement().getName().toLowerCase().equals("gramo") || c.getUnitOfMeasurement().getName().toLowerCase().equals("mililitro")) {
                    amount = c.getAmountPerPerson();
                } else if (c.getUnitOfMeasurement().getReferenceUnit() != null && (c.getUnitOfMeasurement().getReferenceUnit().getName().toLowerCase().equals("gramo") || c.getUnitOfMeasurement().getReferenceUnit().getName().toLowerCase().equals("mililitro"))) {
                    amount = c.getAmountPerPerson() * c.getUnitOfMeasurement().getConversionFactor();

                } else if (c.getUnitOfMeasurement().getName().toLowerCase().equals("unidad")) {
                    amount = c.getAmountPerPerson() * ((c.getIngredient().getUnitaryWeight() != null) ? c.getIngredient().getUnitaryWeight() : 0);
                }
            }

            if (c.getIngredient().getProductType().getNutritionalGroup() != null) {
                amounts[c.getIngredient().getProductType().getNutritionalGroup().getId() - 1] += amount;
            }
        }

        return amounts;
    }

    public static boolean validForVegetarians(Collection<IngredientAmount> ingredients) {
        boolean valid = true;

        for (IngredientAmount c : ingredients) {
            if (c == null || c.getIngredient() == null || c.getIngredient().getIsVegetarian() == null || !c.getIngredient().getIsVegetarian()) {
                valid = false;
                break;
            }
        }
        return valid;
    }

    public static boolean areGlutenFree(Collection<IngredientAmount> ingredients) {
        boolean valid = true;

        for (IngredientAmount c : ingredients) {
            if (c == null || c.getIngredient() == null || c.getIngredient().getContainsGluten() == null || c.getIngredient().getContainsGluten()) {
                valid = false;
                break;
            }
        }

        return valid;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public UnitOfMeasurement getUnitOfMeasurement() {
        return unitOfMeasurement;
    }

    public String getUnitOfMeasurementString(int numPeople) {
        if (amountPerPerson != null && unitOfMeasurement != null) {
            Double amount = amountPerPerson * numPeople;
            return (amount.equals(1.0)) ? unitOfMeasurement.getName() : unitOfMeasurement.getNamePlural();
        }

        return "";
    }

    public Double getAmountPerPerson() {
        return amountPerPerson;
    }

    public String getAmountPerPersonString(int numPeople) {
        if (amountPerPerson != null && unitOfMeasurement != null) {
            Double amount = amountPerPerson * numPeople;
            return String.valueOf((amount % 1 == 0) ? amount.intValue() : String.valueOf(amount));
        }

        return "";
    }

    public void setAmountPerPerson(UnitOfMeasurement unitOfMeasurement, Double amountPerPerson) {
        if (unitOfMeasurement != null && amountPerPerson != null) {
            this.unitOfMeasurement = unitOfMeasurement;
            this.amountPerPerson = amountPerPerson;
        } else {
            this.unitOfMeasurement = null;
            this.amountPerPerson = null;
        }
    }
}
