package app.cheftastic.vanilla.model;

import app.cheftastic.vanilla.SQLiteHandler;

public class NutritionalGroup {
    private int id;
    private String name;
    private String details;
    private Double recommendedPercentage;

    public NutritionalGroup(int id, String name, String details, Double recommendedPercentage) {
        setId(id);
        setName(name);
        setDetails(details);
        setRecommendedPercentage(recommendedPercentage);
    }

    public static NutritionalGroup retrieveNutritionalGroup(int id) {
        return SQLiteHandler.retrieveNutritionalGroup(id);
    }

    public static int getNumberOfNutritionalGroups() {
        return 6;
    }

    public static double[] getNutritionalGroupsTargetPercentage() {
        double[] target = new double[getNumberOfNutritionalGroups()];
        target[0] = NutritionalGroup.retrieveNutritionalGroup(1).getRecommendedPercentage();
        target[1] = NutritionalGroup.retrieveNutritionalGroup(2).getRecommendedPercentage();
        target[2] = NutritionalGroup.retrieveNutritionalGroup(3).getRecommendedPercentage();
        target[3] = NutritionalGroup.retrieveNutritionalGroup(4).getRecommendedPercentage();
        target[4] = NutritionalGroup.retrieveNutritionalGroup(5).getRecommendedPercentage();
        target[5] = NutritionalGroup.retrieveNutritionalGroup(6).getRecommendedPercentage();

        return target;
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

    public String getDetails() {
        return this.details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Double getRecommendedPercentage() {
        return this.recommendedPercentage;
    }

    public void setRecommendedPercentage(Double recommendedPercentage) {
        this.recommendedPercentage = recommendedPercentage;
    }
}
