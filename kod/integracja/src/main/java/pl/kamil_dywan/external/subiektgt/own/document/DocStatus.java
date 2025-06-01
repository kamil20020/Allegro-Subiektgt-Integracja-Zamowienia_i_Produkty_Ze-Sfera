package pl.kamil_dywan.external.subiektgt.own.document;

public enum DocStatus {

    MADE(1);

    private final Integer value;

    private DocStatus(Integer value){

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
