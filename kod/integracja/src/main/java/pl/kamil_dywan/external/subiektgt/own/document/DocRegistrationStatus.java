package pl.kamil_dywan.external.subiektgt.own.document;

public enum DocRegistrationStatus {

    NOT_REGISTERED(0);

    private final Integer value;

    private DocRegistrationStatus(Integer value){

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
