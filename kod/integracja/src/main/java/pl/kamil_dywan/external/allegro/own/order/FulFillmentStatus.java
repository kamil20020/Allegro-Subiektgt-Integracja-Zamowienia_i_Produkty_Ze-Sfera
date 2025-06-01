package pl.kamil_dywan.external.allegro.own.order;

public enum FulFillmentStatus {

    NEW("NEW"),
    PROCESSING("PROCESSING"),
    READY_FOR_SHIPMENT("READY_FOR_SHIPMENT"),
    READY_FOR_PICKUP("READY_FOR_PICKUP"),
    SENT("SENT"),
    PICKED_UP("PICKED_UP"),
    CANCELLED("CANCELLED"),
    SUSPENDED("SUSPENDED"),
    RETURNED("RETURNED");

    private String name;

    private FulFillmentStatus(String name){

        this.name = name;
    }

    @Override
    public String toString(){

        return name;
    }
}
