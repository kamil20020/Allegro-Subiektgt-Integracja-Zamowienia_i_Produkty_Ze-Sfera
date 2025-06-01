package pl.kamil_dywan.external.allegro.own.order;

public enum OrderStatus {

    BOUGHT("BOUGHT"),
    FILLED_IN("FILLED_IN"),
    READY_FOR_PROCESSING("READY_FOR_PROCESSING"),
    BUYER_CANCELLED("BUYER_CANCELLED"),
    FULFILLMENT_STATUS_CHANGED("FULFILLMENT_STATUS_CHANGED"),
    AUTO_CANCELLED("AUTO_CANCELLED");

    private String name;

    OrderStatus(String name){

        this.name = name;
    }

    @Override
    public String toString(){

        return name;
    }
}
