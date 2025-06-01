package pl.kamil_dywan.external.allegro.own;

public enum OfferProductStatus {

    ACTIVE("ACTIVE");

    private String name;

    private OfferProductStatus(String name){

        this.name = name;
    }

    @Override
    public String toString(){

        return name;
    }
}
