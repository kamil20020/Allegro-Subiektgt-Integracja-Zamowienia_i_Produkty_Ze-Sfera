package pl.kamil_dywan.external.subiektgt.own.product;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;

@XmlEnum
public enum UOMCode {

    @XmlEnumValue("szt.") UNIT("szt.");

    private String value;

    private UOMCode(String value){

        this.value = value;
    }

    @Override
    public String toString(){

        return value;
    }
}
