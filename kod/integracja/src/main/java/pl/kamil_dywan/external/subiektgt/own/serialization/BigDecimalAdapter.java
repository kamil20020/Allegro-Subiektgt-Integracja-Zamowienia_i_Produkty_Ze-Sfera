package pl.kamil_dywan.external.subiektgt.own.serialization;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.math.BigDecimal;

public class BigDecimalAdapter extends XmlAdapter<String, BigDecimal> {

    @Override
    public BigDecimal unmarshal(String s) throws Exception {

        if(s == null){
            return null;
        }

        s = s.replace(",", ".");

        return new BigDecimal(s);
    }

    @Override
    public String marshal(BigDecimal v) throws Exception {

        if(v == null){
            return null;
        }

        return v.toString().replace(".", ",");
    }
}
