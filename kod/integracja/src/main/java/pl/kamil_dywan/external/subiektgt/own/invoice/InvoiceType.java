package pl.kamil_dywan.external.subiektgt.own.invoice;

import jakarta.xml.bind.annotation.XmlEnum;

@XmlEnum
public enum InvoiceType {

    VAT("Faktura VAT");

    private String name;

    InvoiceType(String name){

        this.name = name;
    }

    @Override
    public String toString(){

        return name;
    }
}
