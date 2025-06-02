package pl.kamil_dywan.external.sfera.generated;

public enum ResponseStatus {

    SUCCESS("success"),
    ERROR("fail");

    private String name;

    private ResponseStatus(String name){

        this.name = name;
    }

    @Override
    public String toString(){

        return name;
    }
}
