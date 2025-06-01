package pl.kamil_dywan.external.allegro.own;

public enum Currency {

    PLN("PLN");

    private String name;

    Currency(String name){

        this.name = name;
    }

    @Override
    public String toString(){

        return name;
    }
}
