package pl.kamil_dywan.external.subiektgt.own;

public enum TypeOfTransaction {

    COUNTRY(0);

    private final Integer value;

    private TypeOfTransaction(Integer value){

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
