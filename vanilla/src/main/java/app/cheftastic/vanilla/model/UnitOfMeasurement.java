package app.cheftastic.vanilla.model;

public class UnitOfMeasurement {
    private int id;
    private String name;
    private String namePlural;
    private String symbol;
    private UnitOfMeasurement referenceUnit;
    private Double conversionFactor;

    public UnitOfMeasurement(int id, String name, String namePlural, String symbol, UnitOfMeasurement referenceUnit, Double conversionFactor) {
        setName(name);
        setNamePlural(namePlural);
        setReferenceUnit(referenceUnit, conversionFactor);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamePlural() {
        return namePlural;
    }

    public void setNamePlural(String namePlural) {
        this.namePlural = namePlural;
    }

    public UnitOfMeasurement getReferenceUnit() {
        return referenceUnit;
    }

    public Double getConversionFactor() {
        return conversionFactor;
    }

    public void setReferenceUnit(UnitOfMeasurement unit, Double conversionFactor) {
        if (unit != null && conversionFactor != null) {
            this.referenceUnit = unit;
            this.conversionFactor = conversionFactor;
        } else {
            this.referenceUnit = null;
            this.conversionFactor = null;
        }
    }
}
