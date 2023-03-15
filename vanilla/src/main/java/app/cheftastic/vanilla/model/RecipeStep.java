package app.cheftastic.vanilla.model;

public class RecipeStep {
    private int id;
    private int recipeId;
    private int sorting;
    private String description;
    private UnitOfTime unitOfTime;
    private Integer duration;

    public RecipeStep(int id, int recipeId, int sorting, String description, UnitOfTime unitOfTime, Integer duration) {
        setSorting(sorting);
        setDescription(description);
        setDuration(unitOfTime, duration);
    }

    public int getSorting() {
        return sorting;
    }

    private void setSorting(int sorting) {
        this.sorting = sorting;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UnitOfTime getUnitOfTime() {
        return unitOfTime;
    }

    public String getUnitOfTimeString() {
        if (duration != null && unitOfTime != null) {
            return (duration.equals(1) ? unitOfTime.getName() : unitOfTime.getNamePlural());
        }

        return "";
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(UnitOfTime unitOfTime, Integer duration) {
        if (unitOfTime != null && duration != null) {
            this.unitOfTime = unitOfTime;
            this.duration = duration;
        } else {
            this.unitOfTime = null;
            this.duration = null;
        }
    }
}
