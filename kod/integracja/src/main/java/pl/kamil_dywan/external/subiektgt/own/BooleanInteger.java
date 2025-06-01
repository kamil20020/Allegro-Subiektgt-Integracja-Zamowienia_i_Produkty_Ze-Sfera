package pl.kamil_dywan.external.subiektgt.own;

public enum BooleanInteger {

    NO(0),
    YES(1);

    private final Integer value;

    private BooleanInteger(Integer value) {

        this.value = value;
    }

    public Integer getValue(){

        return value;
    }

    @Override
    public String toString(){

        return value.toString();
    }
}
