package pl.kamil_dywan.external.subiektgt.own.document;

public enum DocMoneyRoundMethod {

    TO_SECOND_DECIMAL_PLACE(0);

    private final Integer value;

    private DocMoneyRoundMethod(Integer value) {

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
