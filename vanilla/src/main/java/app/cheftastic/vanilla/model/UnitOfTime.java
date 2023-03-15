package app.cheftastic.vanilla.model;

public class UnitOfTime {
    private int id;
    private String name;
    private String namePlural;
    private String symbol;

    public UnitOfTime(int id, String name, String namePlural, String symbol) {
        setName(name);
        setNamePlural(namePlural);
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
}
