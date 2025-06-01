package pl.kamil_dywan.external.subiektgt.own.product;

public enum ProductType {

    GOODS(1),
    SERVICES(2);

    private final Integer type;

    private ProductType(Integer type){

        this.type = type;
    }

    @Override
    public String toString(){

        return type.toString();
    }

    public static ProductType valueOf(Integer type){

        return switch(type){

            case 1 -> GOODS;
            case 2 -> SERVICES;
            default -> throw new IllegalArgumentException("Invalid value of Product Type: " + type);
        };
    }
}
