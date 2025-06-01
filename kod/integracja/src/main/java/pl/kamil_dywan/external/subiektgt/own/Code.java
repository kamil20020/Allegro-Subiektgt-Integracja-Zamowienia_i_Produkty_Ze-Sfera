package pl.kamil_dywan.external.subiektgt.own;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;

@XmlEnum
public enum Code {

    @XmlEnumValue("INV") INVOICE("INV"),
    PLN("PLN"),
    PL("PL"),
    FII("FII"),
    LID("LID"),
    Z("Z"),
    H("H"),
    L("L"),
    @XmlEnumValue("14I") Code14I("14I"),
    @XmlEnumValue("00I") Code00I("00I"),
    MAG("MAG");

    private final String name;

    Code(String name){

        this.name = name;
    }

    @Override
    public String toString(){

        return name;
    }
}
