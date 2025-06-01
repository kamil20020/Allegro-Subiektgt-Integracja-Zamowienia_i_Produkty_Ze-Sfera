package pl.kamil_dywan.file;

public enum EppGroupSpecialType {

    EMPTY_HEADER("EMPTY_HEADER"),
    EMPTY_CONTENT("EMPTY_CONTENT");

    private final String name;

    private EppGroupSpecialType(String name) {

        this.name = name;
    }

    @Override
    public String toString(){

        return name;
    }
}
