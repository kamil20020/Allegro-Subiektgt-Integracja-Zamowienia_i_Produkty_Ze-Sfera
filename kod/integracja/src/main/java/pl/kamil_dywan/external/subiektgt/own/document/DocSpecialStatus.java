package pl.kamil_dywan.external.subiektgt.own.document;

public enum DocSpecialStatus {

    NOT_USED(0);

    private final Integer value;

    private DocSpecialStatus(Integer value) {

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
