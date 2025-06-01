package pl.kamil_dywan.external.subiektgt.own;

public enum PriceCategory {

    RETAIL("Detaliczna"),
    WHOLESALE("Hurtowa"),
    SPECIAL("Specjalna");

    private String name;

    private PriceCategory(String name){

        this.name = name;
    }

    @Override
    public String toString(){

        return name;
    }
}
