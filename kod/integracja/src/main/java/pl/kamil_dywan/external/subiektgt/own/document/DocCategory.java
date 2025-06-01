package pl.kamil_dywan.external.subiektgt.own.document;

public enum DocCategory {

    RETAIL("Detal", "Sprzedaż detaliczna");

    private final String value;
    private final String longValue;

    private DocCategory(String value, String longValue){

        this.value = value;
        this.longValue = longValue;
    }

    public String getLongValue(){

        return longValue;
    }

    @Override
    public String toString(){

        return value;
    }
}
